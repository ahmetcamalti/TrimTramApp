package ahmet.example.com.trimtramandroidapp;

/**
 * Created by Ahmet on 25-Mar-17.
 */
public class User {
    private String userId;
    private String userName;
    private String privateKey;

    /**
     * @param userName
     * Constructor
     */
    public User(String userName) {
        this.userName = userName;
    }

    /**
     * @param userId
     * @param userName
     * @param privateKey
     * Constructor
     */
    public User(String userId, String userName, String privateKey) {
        this.userId = userId;
        this.userName = userName;
        this.privateKey = privateKey;
    }

    /**
     * @return String
     * This method return user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     * This method set user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return String
     * This method return user
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     * This method set user id
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return String
     * This method return user private key
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * @param privateKey
     * This method set user private key
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
