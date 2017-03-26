package ahmet.example.com.trimtramandroidapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.json.JSONException;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // Shared preferences name;
    public static final String PREFS_NAME = "APP_PREF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences session = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Check the user logged in ?
        Boolean logged_in = session.getBoolean("logged_in", false);
        if (logged_in == false) {
            Intent theIntent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(theIntent);
        }

        // init floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // set onclick listener to fab button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(MainActivity.this, EventFinderActivity.class);
                startActivity(intent);
            }
        });

        // get travel data from server
        getAllTravels();
    }

    /*
     * Getting all travels data from server
     * Maybe this method can converted to location radius
     */
    public void getAllTravels() {
        String url = "travels/all";
        HashMap<String, String> data = new HashMap<String, String>();
        AsyncTaskModuler moduler = new AsyncTaskModuler(MainActivity.this, data, url, completeGetAllTravels);
        moduler.execute();
    }

    OnTaskCompleteListener completeGetAllTravels = new OnTaskCompleteListener() {

        @Override
        public void onCompleteListener(String response) {
            try {
                TravelTable travelTable = new TravelTable(MainActivity.this);

                // add travel data to UI
                travelTable.addTravelsToUI(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
