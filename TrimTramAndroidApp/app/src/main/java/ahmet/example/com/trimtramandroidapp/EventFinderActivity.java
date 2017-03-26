package ahmet.example.com.trimtramandroidapp;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_finder);

        travelSearchLayout = (LinearLayout) findViewById(R.id.travel_search_layout);

        travelTableLayout = (TableLayout) findViewById(R.id.table_travels);
        travelTableLayout.setVisibility(View.GONE);

        placeTitles = new ArrayList<String>();
        places = new ArrayList<Place>();

        String[] array = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};
        multiSelectionTimeSpinner = (MultiSelectionSpinner) findViewById(R.id.timeSpinner);
        multiSelectionTimeSpinner.setItems(array);
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

    @Override
    public void selectedIndices(List<Integer> indices) {
    }

    @Override
    public void selectedStrings(List<String> strings) {
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
                createPlacesSpinner(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void createPlacesSpinner(String response) throws JSONException {
        JSONObject serverResponse = new JSONObject(response);

        if (serverResponse.getInt("success") == 1) {
            JSONArray placesData = serverResponse.getJSONArray("data");

            for (int i = 0; i < placesData.length(); i++) {
                String placeDataString = placesData.getString(i);
                Place thePlace = new Place();
                thePlace.createFromJSONString(placeDataString);

                places.add(i, thePlace);
                placeTitles.add(thePlace.getTitle());
            }

            multiSelectionPlaceSpinner = (MultiSelectionSpinner) findViewById(R.id.placeSpinner);
            multiSelectionPlaceSpinner.setItems(placeTitles);
            multiSelectionPlaceSpinner.setListener(this);
        } else {
            String serverMessage = serverResponse.getString("message");

            Toast.makeText(EventFinderActivity.this, serverMessage,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void findTravelByFilter(List<String> timesDataForFilter, ArrayList<String> placesDataForFilter) {
        String url = "travels/candidates/{\"times\":" + timesDataForFilter.toString() + ",\"places\":[";
        for (int i = 0; i < placesDataForFilter.size(); i++) {
            if (i > 0) url += ", ";
            url += "\"" + placesDataForFilter.get(i) + "\"";
        }
        url += "]}";

        HashMap<String, String> data = new HashMap<String, String>();
        AsyncTaskModuler moduler = new AsyncTaskModuler(EventFinderActivity.this, data, url, completeTravelByFilter);
        moduler.execute();

        travelSearchLayout.setVisibility(View.GONE);
    }

    OnTaskCompleteListener completeTravelByFilter = new OnTaskCompleteListener() {

        @Override
        public void onCompleteListener(String response) {
            travelTableLayout.setVisibility(View.VISIBLE);

            try {
                TravelTable travelTable = new TravelTable(EventFinderActivity.this);

                // add travel data to UI
                travelTable.addTravelsToUI(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
