package ahmet.example.com.trimtramandroidapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ahmet on 25-Mar-17.
 */
public class User {
    private String id;
    private String name;
    private String privateKey;

    public User() {}

    /**
     * @param name
     * Constructor
     */
    public User(String name) {
        this.name = name;
    }

    /**
     * @param id
     * @param name
     * @param privateKey
     * Constructor
     */
    public User(String id, String name, String privateKey) {
        this.id = id;
        this.name = name;
        this.privateKey = privateKey;
    }

    /**
     * @return String
     * This method return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     * This method set id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return String
     * This method return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     * This method set name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String
     * This method return private key
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * @param privateKey
     * This method set private key
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void createFromJSONString(String jsonData) throws JSONException {
        // convert user string data to json object
        JSONObject userJSON = new JSONObject(jsonData);

        // set id of user object
        this.setId(userJSON.getString("_id"));
        // set name of user object
        this.setName(userJSON.getString("username"));
        // set privateKey of user object
        this.setPrivateKey(userJSON.getString("private_key"));
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", privateKey='" + privateKey + '\'' +
                '}';
    }
}
