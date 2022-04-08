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
import javax.persistence.criteria.Root;
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
            List l = em.createNamedQuery("same")
                    .setParameter("custmail", user.getEmail())
                    .setParameter("custtel", user.getTel())
                    .setParameter("custname", user.getUsername())
                    .getResultList();
            if(l.size()>0){
                em.close();
                return false;
            }else {
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
            logger.warning("could not find user with his username");
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
        } else {
            em.close();
            return false;
        }
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

    public User search(String query){
        //todo search for it with username, email and tel
        return null;
    }

}
