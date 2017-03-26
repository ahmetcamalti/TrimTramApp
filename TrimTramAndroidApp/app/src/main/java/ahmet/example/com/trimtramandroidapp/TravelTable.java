package ahmet.example.com.trimtramandroidapp;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ahmet on 26-Mar-17.
 */
public class TravelTable {
    private TableLayout travelsTableLayout;
    private Activity theActivity;

    public TravelTable(Activity theActivity) {
        this.theActivity = theActivity;
        travelsTableLayout = (TableLayout) this.theActivity.findViewById(R.id.table_travels);
    }

    public void addTravelsToUI(String response) throws JSONException {
        ArrayList<Travel> travels = new ArrayList<Travel>();

        // create json array from json object
        JSONObject json = new JSONObject(response);
        JSONArray travelsData = json.getJSONArray("data");

        // travel all array rows
        for (int i = 0; i < travelsData.length(); i++) {
            Travel theTravel = new Travel();

            // get row as string from json array
            String theTravelDataString = travelsData.getString(i);
            // create travel object from json string
            theTravel.createFromJSONString(theTravelDataString);
            // add travel object to travels list
            travels.add(theTravel);
        }

        for (int i = 0; i < travels.size(); i++) {
            Travel theTravel = travels.get(i);

            TableRow travelRow = (TableRow) View.inflate(this.theActivity, R.layout.tablerow_travel, null);
            TableLayout.LayoutParams lp =
                    new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT);

            lp.setMargins(0, 25, 0, 0);
            travelRow.setLayoutParams(lp);

            TextView travelTitleTextView = (TextView) travelRow.findViewById(R.id.textview_travel_title);
            TextView travelTimeTextView = (TextView) travelRow.findViewById(R.id.textview_time);
            TextView travelPlacesTextView = (TextView) travelRow.findViewById(R.id.textview_places);

            travelTitleTextView.setText(theTravel.getTitle());
            travelTimeTextView.setText(Integer.toString(theTravel.getTime()));
            travelPlacesTextView.setText(theTravel.getPlace().getTitle());

            // add row to table layout
            travelsTableLayout.addView(travelRow, lp);
        }
    }
}
