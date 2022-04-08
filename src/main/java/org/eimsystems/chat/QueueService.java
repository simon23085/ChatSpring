package org.eimsystems.chat;

import jdk.jfr.Description;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;

/**
 * Service to handle all Queue Aspects
 */
@Description("Service to handle all Queue Aspects")
public class QueueService {
    public static void  store(Message message,long id){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myEntityManager");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        MessageQueue messageQueue = em.getReference(MessageQueue.class,id);
        messageQueue.addToQueue(message);
        em.getTransaction().commit();
        em.close();
    }
    public static ArrayList<Message> get(long id){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myEntityManager");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        MessageQueue messageQueue = em.getReference(MessageQueue.class, id);
        ArrayList<Message>list=messageQueue.getQueue();
        messageQueue.clearQueue();
        em.getTransaction().commit();
        em.close();
        return list;
    }
}
