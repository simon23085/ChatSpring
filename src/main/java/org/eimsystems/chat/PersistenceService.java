package org.eimsystems.chat;


import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.event.EventListener;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class PersistenceService {
    Logger logger;

    @EventListener(ApplicationPreparedEvent.class)
    public void init(){
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.ALL);
        logger.info("init()");
    }

    public boolean register(User user){
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.info("register()");
        logger.info(user.getUsername());
        logger.info(user.toString());
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myEntityManager");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        if (!em.contains(user)) {
            logger.info("user is not contained by EntityManager");
            List l = em.createNamedQuery("same")
                    .setParameter("custmail", user.getEmail())
                    .setParameter("custtel", user.getTel())
                    .setParameter("custname", user.getUsername())
                    .getResultList();
            if(l.size()>0){
                logger.info("found " + l.size() + " users with identically email, tel or username");
                em.close();
                return false;
            }else {
                logger.info("no user found with identically email, tel or username, persist user now");
                em.persist(user);
                em.getTransaction().commit();
                em.close();
                return true;
            }
        } else {
            em.close();
            return false;
        }

    }
    //todo check that user details can be changed(unique etc.)
    public User updateUser(User user){
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.info("updateUser()");
        logger.info(user.toString());

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myEntityManager");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User oldUser = em.getReference(User.class, user.getId());
        if(!oldUser.equals(user)){
            em.detach(oldUser);
            em.persist(user);
            em.getTransaction().commit();
        }
        em.close();
        return user;
    }

    public User getUser(long id){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myEntityManager");
        EntityManager em = emf.createEntityManager();
        User user = em.getReference(User.class,id);
        em.close();
        return user;
    }
    public User getUser(String username){
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myEntityManager");
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User>  cr = criteriaBuilder.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        cr.select(root).where(criteriaBuilder.equal(root.get("username"), username));
        Session session = (Session) em.getDelegate();
        Query<User> query = session.createQuery(cr);
        List<User> results = query.getResultList();
        if(results.size()==0){
            logger.warning("could not find user with the username: " + username);
            em.close();
            return null;
        }else if (results.size()>1){
            logger.warning("more than one user found with a username");
            em.close();
            return null;
        }else {
            em.close();
            return results.get(0);
        }
    }

    public boolean deregister(User user) {
        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.info("deregister()");
        logger.info(user.toString());
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myEntityManager");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        if (em.contains(user)) {
            em.remove(user);
            em.getTransaction().commit();
            em.close();
            return true;
        }
        User user1 = em.find(User.class, user.getId());
        if(user1!=null){
            em.remove(user1);
            em.getTransaction().commit();
            em.close();
            return true;
        }
        User user2 = getUser(user.getUsername());
        if(user2!=null){
            User user3 = em.find(User.class, user2.getId());
            if(!user2.equals(user3))throw new RuntimeException("user2 and user3 should be equal");
            em.remove(user3);
            em.getTransaction().commit();
            em.close();
            return true;
        }
        return false;
    }
    public void persistKey(KeyExchange keyExchange){
        logger.info("keyExchange");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myEntityManager");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(keyExchange);
        em.getTransaction().commit();
        em.close();
    }
    public List<KeyExchange> getKeyExchange(long id){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myEntityManager");
        EntityManager em = emf.createEntityManager();
        return em.createNamedQuery("getKeys")
                .setParameter("custid", id)
                .getResultList();
    }

    public List<User> search(String query){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myEntityManager");
        List<User> list = new ArrayList<>();
        list.add(getUser(query));
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User>  cr = criteriaBuilder.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        Predicate p0 = criteriaBuilder.like(root.get("email"), query);
        Predicate p1 = criteriaBuilder.like(root.get("tel"), query);
        Predicate p01 = criteriaBuilder.or(p0,p1);
        cr.select(root).where(p01);
        Session session = (Session) em.getDelegate();
        Query<User> query1 = session.createQuery(cr);
        list.addAll(query1.getResultList());
        return list;
    }

}
