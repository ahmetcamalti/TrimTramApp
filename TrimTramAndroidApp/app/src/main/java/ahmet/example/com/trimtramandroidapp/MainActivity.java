package ahmet.example.com.trimtramandroidapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    // SignIn Async Task
    private GetAllTravels mAuthTask = null;

    // OkHttpClient Library
    private OkHttpClient client = new OkHttpClient();

    // Shared preferences name;
    public static final String PREFS_NAME = "APP_PREF";

    // CircularProgressView Library
    private CircularProgressView progressView;

    // All travels data
    private ArrayList<Travel> travels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        travels = new ArrayList<Travel>();

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

        // Show a progress spinner, and kick off a background task to
        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        progressView.setIndeterminate(true);
        progressView.startAnimation();

        // perform the add book attempt.
        mAuthTask = new GetAllTravels();
        mAuthTask.execute((Void) null);
    }

    public void addTravelsToUI(JSONObject json) throws JSONException {
        // create json array from json object
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

        Log.e("Travels", travels.toString());

        TableLayout travelsTableLayout = (TableLayout) findViewById(R.id.table_travels);

        for (int i = 0; i < travels.size(); i++) {
            Travel theTravel = travels.get(i);

            TableRow travelRow = (TableRow) View.inflate(MainActivity.this, R.layout.tablerow_travel, null);
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

    /**
     * Represents an asynchronous get all travels data
     */
    public class GetAllTravels extends AsyncTask<Void, Void, Boolean> {

        JSONObject json;

        private String serverMessage;

        GetAllTravels() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            int success = 0;

            try {
                // Simulate network access.
                String responseData = null;
                String url = "http://zabalunga.herokuapp.com/travels/all";
                try {
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        responseData = response.body().string();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // convert response data to json object
                json = new JSONObject(responseData);

                // check your log for json response
                Log.v("LoginServerResponse", json.toString());

                // json success tag
                success = json.getInt("success");

                serverMessage = json.getString("message");

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // Interrupt Exception
                return false;
            } catch (JSONException e) {
                // JSON Exception
                e.printStackTrace();
            }

            return (success == 1);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                // Stop progress spinner
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();

                try {
                    addTravelsToUI(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(MainActivity.this, serverMessage,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            // set null async task
            mAuthTask = null;
        }
    }
}
