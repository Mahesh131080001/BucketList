package in.teachcoder.bucketlist.models;

import com.firebase.client.ServerValue;

import java.util.HashMap;
import java.util.jar.Pack200;

import in.teachcoder.bucketlist.Constants;

/**
 * Created by Mahesh on 5/22/2016.
 */
public class Users {

    String username,useremail;
    HashMap<String, Object> createdAt;


    public Users()
    {

    }


    public Users(String username,String useremail,HashMap<String,Object> createdAt)
    {
        this.username = username;
        this.useremail =useremail;
        this.createdAt =createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public HashMap<String, Object> getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(HashMap<String, Object> createdAt) {
        this.createdAt = createdAt;
    }
}
