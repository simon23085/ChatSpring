package org.eimsystems.chat;


import javax.persistence.NamedQuery;
import java.security.PublicKey;
//todo fix argument in NamedQuery
@NamedQuery(name="getKeys", query ="SELECT k FROM KeyExchange k WHERE k.id IS custid")
public class KeyExchange {
private PublicKey publicKey;
private long id;

    public KeyExchange(PublicKey publicKey, long id) {
        this.publicKey = publicKey;
        this.id = id;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
