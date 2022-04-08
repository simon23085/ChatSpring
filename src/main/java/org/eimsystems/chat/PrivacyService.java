package org.eimsystems.chat;

import jdk.jfr.Description;

import java.util.Collection;

/**
 * Service to handle all Data privacy request, like request for DSGVO
 */
@Description("Service to handle all Data privacy request, like request for DSGVO")
public class PrivacyService {
    public Collection<? extends Object> getData(long id){
        //todo get all data to given id and return it
        return null;
    }
    public Collection<? extends Object> getData(String username){
        //todo get all data to the given username and return it
        return null;
    }
    public void changeData(String key, String value){
        //todo change the entry in the database to the new value
    }
    public void deleteData(String username){
        //todo delete all the data to the given username
    }
    public void deleteData(long id){
        //todo delete all the data to the given id
    }
}
