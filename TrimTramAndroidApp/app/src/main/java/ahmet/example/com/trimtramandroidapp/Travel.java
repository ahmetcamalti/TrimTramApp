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

    public void createFromJSONString(String jsonData) throws JSONException {
        // convert travel string data to json object
        JSONObject travel = new JSONObject(jsonData);

        // set title of travel object
        this.setTitle(travel.getString("title"));
        // set time of travel object
        this.setTime(travel.getInt("time"));

        // get place data as string
        String placeDataString = travel.getString("place");

        // create place object
        Place thePlace = new Place();
        // set place data from json string
        thePlace.createFromJSONString(placeDataString);

        // set place of travel object
        this.setPlace(thePlace);

        // set users of travel object
        ArrayList<User> users = new ArrayList<User>();
        if (travel.has("users")) {
            JSONArray usersJSON = travel.getJSONArray("users");
            for (int i = 0; i < usersJSON.length(); i++) {
                // create user object
                User theUser = new User();
                // create user data string
                String userDataString = usersJSON.getString(i);
                // create user object from json string
                theUser.createFromJSONString(userDataString);
                // add user object to array lsit
                users.add(theUser);
            }
        }

        // set user array list of travel object
        this.setUsers(users);
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
