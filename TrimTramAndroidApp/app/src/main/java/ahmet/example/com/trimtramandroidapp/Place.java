package ahmet.example.com.trimtramandroidapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ahmet on 25-Mar-17.
 */
public class Place {
    private String title;
    private String lat;
    private String lon;

    public Place() {}

    public Place(String title, String lat, String lon) {
        this.title = title;
        this.lat = lat;
        this.lon = lon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void createFromJSONString(String jsonData) throws JSONException {
        // convert place string data to json object
        JSONObject placeJSON = new JSONObject(jsonData);

        // set title of place object
        this.setTitle(placeJSON.getString("title"));
        // set lat of place object
        this.setLat(placeJSON.getString("lat"));
        // set lon of place object
        this.setLon(placeJSON.getString("lon"));
    }

    @Override
    public String toString() {
        return "Place{" +
                "title='" + title + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                '}';
    }
}
