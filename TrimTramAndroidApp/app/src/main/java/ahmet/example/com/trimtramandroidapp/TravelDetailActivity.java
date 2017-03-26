package ahmet.example.com.trimtramandroidapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TravelDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_detail);

        String travelId = getIntent().getStringExtra("TRAVEL_ID");

        String url = "travels/get/" + travelId;
        HashMap<String, String> data = new HashMap<String, String>();
        AsyncTaskModuler moduler = new AsyncTaskModuler(TravelDetailActivity.this, data, url, completeGetTravelData);
        moduler.execute();
    }

    OnTaskCompleteListener completeGetTravelData = new OnTaskCompleteListener() {

        @Override
        public void onCompleteListener(String response) {
            try {
                addTravelDataToUI(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void addTravelDataToUI(String response) throws JSONException {
        JSONObject responseData = new JSONObject(response);

        String travelDataString = responseData.getString("data");

        Travel theTravel = new Travel();
        theTravel.createFromJSONString(travelDataString, null);


    }
}
