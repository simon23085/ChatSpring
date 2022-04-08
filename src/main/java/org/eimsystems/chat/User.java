package org.eimsystems.chat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
@Entity
@Table(name="user", schema = "chat")
@NamedQuery(name="same", query ="SELECT u FROM User u WHERE u.email LIKE :custmail  or u.tel LIKE :custtel or u.username LIKE :custname")
public class User implements Serializable,Comparable< User >{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private int pw;
    @Column(unique=true)
    private String username;
    private String name;
    @Column(name = "first_name")
    private String firstName;
    private String email;
    private String tel;
    private Locale local;
    private Date birthday;
    @Column(name = "profile_picture")
    private byte[] profilePicture;
    public User(int pw, String username){
        this.pw = pw;
        this.username = username;
    }
    protected  User(){
    }
    public User(long id, int pw, String username){
        this.id = id;
        this.pw = pw;
        this.username = username;
    }
    public User(String name, String firstName, String email, String tel, Locale local, Date birthday,
                    String username, int pw) {
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.tel = tel;
        this.local = local;
        this.birthday = birthday;
        this.username = username;
        this.pw = pw;
    }
    public User(String name, String firstName, String email, String tel, Locale local, Date birthday,
                    String username, int pw, long id) {
        super();
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.tel = tel;
        this.local = local;
        this.birthday = birthday;
        this.username = username;
        this.pw = pw;
        this.id=id;
    }
    public User(String name, String firstName, String email, String tel, Locale local, Date birthday,
                String username, int pw, long id, byte[] profilePicture) {
        super();
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.tel = tel;
        this.local = local;
        this.birthday = birthday;
        this.username = username;
        this.pw = pw;
        this.id=id;
        this.profilePicture=profilePicture;
    }
    public User(String name, String firstName, String email, String tel, Locale local, Date birthday,
                String username, int pw, byte[] profilePicture) {
        super();
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.tel = tel;
        this.local = local;
        this.birthday = birthday;
        this.username = username;
        this.pw = pw;
        this.profilePicture=profilePicture;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public boolean isLoggedIn(){
        return id!=0L;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPw() {
        return pw;
    }

    public void setPw(int pw) {
        this.pw = pw;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Locale getLocal() {
        return local;
    }

    public void setLocal(Locale local) {
        this.local = local;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", pw=" + pw +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", local=" + local +
                ", birthday=" + birthday +
                ", profilePicture=" + Arrays.toString(profilePicture) +
                '}';
    }

    @Override
    public int compareTo(User o) {
        return ((Long)this.getId()).compareTo(o.getId());
    }
}
