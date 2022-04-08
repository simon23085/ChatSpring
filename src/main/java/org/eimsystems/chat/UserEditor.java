package org.eimsystems.chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

public class UserEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        ObjectMapper mapper = new ObjectMapper();

        User value = null;

        try {
            value = new User();
            JsonNode root = mapper.readTree(text);
            value.setEmail(root.path("email").asText());
            value.setId(root.path("Id").asLong());
            value.setBirthday(new Date(root.path("birthday").asText()));
            value.setFirstName(root.path("firstName").asText());
            value.setName(root.path("name").asText());
            value.setLocal(new Locale(root.path("local").asText()));
            value.setProfilePicture(root.path("profilePicture").binaryValue());
            value.setTel(root.path("tel").asText());
            value.setPw(root.path("pw").asInt());
            value.setUsername(root.path("username").asText());
        } catch (IOException e) {
            // handle error
        }

        setValue(value);
    }
}