package ahmet.example.com.trimtramandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ahmet on 26-Mar-17.
 */
public class TravelTable {
    private Activity theActivity;

    public TravelTable(Activity theActivity) {
        this.theActivity = theActivity;
    }

    public void addTravelsToUI(String response, final String currentUserId) throws JSONException {
        ArrayList<Travel> travels = new ArrayList<Travel>();

        // create json array from json object
        JSONObject serverResponse = new JSONObject(response);

        // get response success
        if (serverResponse.getInt("success") == 0) {
            // if there is an error on server, return
            String serverMessage = serverResponse.getString("message");

            // give feedback to user
            Toast.makeText(this.theActivity, serverMessage,
                    Toast.LENGTH_LONG).show();

            return;
        }

        // convert to travels data to json array
        JSONArray travelsData = serverResponse.getJSONArray("data");

        // travel all array rows
        for (int i = 0; i < travelsData.length(); i++) {
            Travel theTravel = new Travel();

            // get row as string from json array
            String theTravelDataString = travelsData.getString(i);
            // create travel object from json string
            theTravel.createFromJSONString(theTravelDataString, currentUserId);
            // add travel object to travels list
            travels.add(theTravel);
        }

        // travels table layout
        TableLayout travelsTableLayout = (TableLayout) this.theActivity.findViewById(R.id.table_travels);

        for (int i = 0; i < travels.size(); i++) {
            final Travel theTravel = travels.get(i);

            // inflate travel row table row layout
            TableRow travelRow = (TableRow) View.inflate(this.theActivity, R.layout.tablerow_travel, null);
            TableLayout.LayoutParams lp =
                    new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT);

            // add some margin beetwen rows
            lp.setMargins(0, 25, 0, 0);
            travelRow.setLayoutParams(lp);

            TextView travelTitleTextView = (TextView) travelRow.findViewById(R.id.textview_travel_title);
            TextView travelTimeTextView = (TextView) travelRow.findViewById(R.id.textview_time);
            TextView travelPlacesTextView = (TextView) travelRow.findViewById(R.id.textview_places);
            TextView travelActionTextView = (TextView) travelRow.findViewById(R.id.textview_action);

            travelTitleTextView.setText(theTravel.getTitle());
            travelTimeTextView.setText(Integer.toString(theTravel.getTime()));

            // check object exist or null
            if (theTravel.getPlace() != null)
                travelPlacesTextView.setText(theTravel.getPlace().getTitle());

            if (theTravel.getCurrentUserIsJoined() != null &&  theTravel.getCurrentUserIsJoined() == false) {
                // if current user didnt join this travel
                travelActionTextView.setText(this.theActivity.getResources().getString(R.string.table_row_action));

                travelActionTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateUserToTravel("add", theTravel.getId(), currentUserId);
                    }
                });
            } else {
                // if current user joined this travel
                travelActionTextView.setText(this.theActivity.getResources().getString(R.string.table_row_action_true));

                travelActionTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateUserToTravel("remove", theTravel.getId(), currentUserId);
                    }
                });
            }

            // add row to table layout
            travelsTableLayout.addView(travelRow, lp);
        }
    }

    public void updateUserToTravel(String which, String travelId, String currentUserId) {
        String url = "travels/" + which + "/" + travelId + "/" + currentUserId;
        Log.e("updateUserURL", url);

        AsyncTaskModuler moduler = new AsyncTaskModuler(this.theActivity, null, url, completeAddUserToTravel);
        moduler.execute();
    }

    OnTaskCompleteListener completeAddUserToTravel = new OnTaskCompleteListener() {

        @Override
        public void onCompleteListener(String response) {
            try {
                openMyTravels(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void openMyTravels(String response) throws JSONException {
        Log.e("updateUserTravel", response);
        // response convert to json object
        JSONObject serverResponse = new JSONObject(response);

        // get response success
        if (serverResponse.getInt("success") == 1) {
            // open travel detail activity
            Intent theIntent = new Intent(this.theActivity, MyTravelsActivity.class);
            theActivity.startActivity(theIntent);
        } else {
            // if there is an error on server
            String serverMessage = serverResponse.getString("message");

            Toast.makeText(this.theActivity, serverMessage,
                    Toast.LENGTH_LONG).show();
        }
    }
}