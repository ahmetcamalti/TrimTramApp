package ahmet.example.com.trimtramandroidapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import com.guna.libmultispinner.MultiSelectionSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventFinderActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {

    private String[] timeList = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
            "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};

    private ArrayList<String> placeTitles;
    private ArrayList<Place> places;

    private MultiSelectionSpinner multiSelectionTimeSpinner;
    private MultiSelectionSpinner multiSelectionPlaceSpinner;

    private LinearLayout travelSearchLayout;
    private TableLayout travelTableLayout;

    // Shared preferences name;
    public static final String PREFS_NAME = "APP_PREF";

    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_finder);

        SharedPreferences session = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        // get user id from shared preferences
        currentUserId = session.getString("user_id", null);

        travelSearchLayout = (LinearLayout) findViewById(R.id.travel_search_layout);

        travelTableLayout = (TableLayout) findViewById(R.id.table_travels);
        travelTableLayout.setVisibility(View.GONE);

        placeTitles = new ArrayList<String>();
        places = new ArrayList<Place>();

        String[] array = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};

        // multi selection spinner for times data
        multiSelectionTimeSpinner = (MultiSelectionSpinner) findViewById(R.id.timeSpinner);
        // set spinner data
        multiSelectionTimeSpinner.setItems(array);
        // set spinner listener
        multiSelectionTimeSpinner.setListener(this);

        Button travelSearchButton = (Button) findViewById(R.id.button_travel_search);
        travelSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected times
                List<String> timesDataForFilter = multiSelectionTimeSpinner.getSelectedStrings();

                // get selected places indices
                List<Integer> selectedIndices = multiSelectionPlaceSpinner.getSelectedIndices();

                ArrayList<String> placesDataForFilter = new ArrayList<String>();
                // get selected places id
                for (int i = 0; i < selectedIndices.size(); i++)
                    placesDataForFilter.add(places.get(selectedIndices.get(i)).getId());

                findTravelByFilter(timesDataForFilter, placesDataForFilter);
            }
        });

        // get all place data for spinner
        getPlaceSpinnerData();
    }

    public void getPlaceSpinnerData() {
        String url = "places/all";
        HashMap<String, String> data = new HashMap<String, String>();
        AsyncTaskModuler moduler = new AsyncTaskModuler(EventFinderActivity.this, data, url, completeGetPlaces);
        moduler.execute();
    }

    OnTaskCompleteListener completeGetPlaces = new OnTaskCompleteListener() {

        @Override
        public void onCompleteListener(String response) {
            travelSearchLayout.setVisibility(View.VISIBLE);

            try {
                // call create places spinner method
                createPlacesSpinner(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void createPlacesSpinner(String response) throws JSONException {
        // convert server response to json object
        JSONObject serverResponse = new JSONObject(response);

        // get server response success
        if (serverResponse.getInt("success") == 1) {
            // convert places data to json array
            JSONArray placesData = serverResponse.getJSONArray("data");

            // create Place object from raw data
            for (int i = 0; i < placesData.length(); i++) {
                // get row
                String placeDataString = placesData.getString(i);

                Place thePlace = new Place();
                // set place data from string
                thePlace.createFromJSONString(placeDataString);

                // add place to Place ArrayList for user selection
                places.add(i, thePlace);
                // add place title for place multi selection spinner
                placeTitles.add(thePlace.getTitle());
            }

            // multi selection spinner for places data
            multiSelectionPlaceSpinner = (MultiSelectionSpinner) findViewById(R.id.placeSpinner);
            // set spinner data
            multiSelectionPlaceSpinner.setItems(placeTitles);
            // set spinner listener
            multiSelectionPlaceSpinner.setListener(this);
        } else {
            // get server message
            String serverMessage = serverResponse.getString("message");

            // give feedback to user
            Toast.makeText(EventFinderActivity.this, serverMessage,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void findTravelByFilter(List<String> timesDataForFilter, ArrayList<String> placesDataForFilter) {
        // create url with data
        String url = "travels/candidates/{\"times\":" + timesDataForFilter.toString() + ",\"places\":[";
        for (int i = 0; i < placesDataForFilter.size(); i++) {
            if (i > 0) url += ", ";
            url += "\"" + placesDataForFilter.get(i) + "\"";
        }
        url += "]}";

        // call async task for server request
        AsyncTaskModuler moduler = new AsyncTaskModuler(EventFinderActivity.this, null, url, completeTravelByFilter);
        moduler.execute();

        // visibility gone filter form layout
        travelSearchLayout.setVisibility(View.GONE);
    }

    OnTaskCompleteListener completeTravelByFilter = new OnTaskCompleteListener() {

        @Override
        public void onCompleteListener(String response) {
            // visibility visible travel table
            travelTableLayout.setVisibility(View.VISIBLE);

            try {
                // travel table module
                TravelTable travelTable = new TravelTable(EventFinderActivity.this);

                // add travel data to UI
                travelTable.addTravelsToUI(response, currentUserId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void selectedIndices(List<Integer> indices) {
    }

    @Override
    public void selectedStrings(List<String> strings) {
    }
}
