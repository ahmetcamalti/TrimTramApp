package ahmet.example.com.trimtramandroidapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;

import java.util.HashMap;

public class MyTravelsActivity extends AppCompatActivity {

    private User currentUser;

    private AsyncTaskModuler moduler;

    // Shared preferences name;
    public static final String PREFS_NAME = "APP_PREF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_travels);

        SharedPreferences session = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        currentUser = new User();
        currentUser.setId(session.getString("user_id", null));
        currentUser.setName(session.getString("user_name", null));
        currentUser.setPrivateKey(session.getString("user_private_key", null));

        // get current user travels data from server
        getMyTravels();
    }

    /*
     * Gett current user travels data from server
     */
    public void getMyTravels() {
        String url = "users/myTravels/" + currentUser.getId() + "/" + currentUser.getPrivateKey();
        HashMap<String, String> data = new HashMap<String, String>();
        moduler = new AsyncTaskModuler(MyTravelsActivity.this, data, url, completeGetAllTravels);
        moduler.execute();
    }

    OnTaskCompleteListener completeGetAllTravels = new OnTaskCompleteListener() {

        @Override
        public void onCompleteListener(String response) {
            try {
                TravelTable travelTable = new TravelTable(MyTravelsActivity.this);

                // add travel data to UI
                travelTable.addTravelsToUI(response, null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
