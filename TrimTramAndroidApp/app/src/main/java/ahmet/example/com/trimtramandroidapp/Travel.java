package ahmet.example.com.trimtramandroidapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ahmet on 25-Mar-17.
 */
public class Travel {
    private String id;
    private String title;
    private int time;
    private Place place;
    private ArrayList<User> users;
    private Boolean currentUserIsJoined;

    public Travel() {
    }

    /**
     * @param title
     * @param time
     * @param place
     * @param users
     */
    public Travel(String title, int time, Place place, ArrayList<User> users) {
        this.title = title;
        this.time = time;
        this.place = place;
        this.users = users;
        this.currentUserIsJoined = false;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return int
     */
    public int getTime() {
        return time;
    }

    /**
     * @param time
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * @return String
     */
    public Place getPlace() {
        return place;
    }

    /**
     * @param place
     */
    public void setPlace(Place place) {
        this.place = place;
    }

    /**
     * @return ArrayList
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * @param users
     */
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public Boolean getCurrentUserIsJoined() {
        return currentUserIsJoined;
    }

    public void setCurrentUserIsJoined(Boolean currentUserIsJoined) {
        this.currentUserIsJoined = currentUserIsJoined;
    }

    public void createFromJSONString(String jsonData, String currentUserId) throws JSONException {
        // convert travel string data to json object
        JSONObject travel = new JSONObject(jsonData);

        // set id of travel object
        this.setId(travel.getString("_id"));
        // set title of travel object
        this.setTitle(travel.getString("title"));
        // set time of travel object
        this.setTime(travel.getInt("time"));

        // get place data as string
        String placeDataString = travel.getString("place");

        // this is not a solution just by pass for one thing (MyTravelsActivity) :)
        if (placeDataString.length() > 24) {
            // create place object
            Place thePlace = new Place();
            // set place data from json string
            thePlace.createFromJSONString(placeDataString);

            // set place of travel object
            this.setPlace(thePlace);
        } else {
            this.setPlace(null);
        }

        // set users of travel object

        ArrayList<User> users = new ArrayList<User>();
        if (travel.has("users")) {
            JSONArray usersJSON = travel.getJSONArray("users");

            for (int i = 0; i < usersJSON.length(); i++) {
                // create user data string
                String userDataString = usersJSON.getString(i);

                // this is not a solution just by pass for one thing (MyTravelsActivity) :)
                if(userDataString.length() > 25) {
                    // create user object
                    User theUser = new User();

                    // create user object from json string
                    theUser.createFromJSONString(userDataString);

                    // sign to user joined or not
                    if (currentUserId == theUser.getId())
                        this.setCurrentUserIsJoined(true);
                    else
                        this.setCurrentUserIsJoined(false);

                    // add user object to array lsit
                    users.add(theUser);
                } else {
                    users.add(null);
                }
            }
        }

        // set user array list of travel object
        this.setUsers(users);
        this.setUsers(null);
    }

    @Override
    public String toString() {
        return "Travel{" +
                "title='" + title + '\'' +
                ", time=" + time +
                ", place='" + place + '\'' +
                ", users=" + users +
                '}';
    }
}
