package org.eimsystems.chat;

import javax.persistence.*;
import java.util.ArrayList;

@SuppressWarnings("JpaAttributeTypeInspection")
@Entity
@Table(name="queue", schema = "chat")
public class MessageQueue {
    @Id
    @Column(name="id",nullable = false)
    private long id;
    //@ElementCollection
    private ArrayList<Message> queue = new ArrayList<Message>();
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<Message> getQueue() {
        return queue;
    }

    public void setQueue(ArrayList<Message> queue) {
        this.queue = queue;
    }
    public void clearQueue(){
        queue.clear();
    }
    public void addToQueue(ArrayList<Message> list){
        queue.addAll(list);
    }
    public void addToQueue(Message message){
        queue.add(message);
    }
}
