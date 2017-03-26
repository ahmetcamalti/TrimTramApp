package ahmet.example.com.trimtramandroidapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {

    // username
    private String mUserName;

    // Shared preferences name;
    public static final String PREFS_NAME = "APP_PREF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Username EditText
        final EditText userNameEditText = (EditText) findViewById(R.id.edittext_username);

        Button button = (Button) findViewById(R.id.button_signin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get username from edittext
                mUserName = userNameEditText.getText().toString();

                /// for validation
                boolean error = false;

                if (mUserName.isEmpty()) {
                    // if username empty set error
                    userNameEditText.setError(getResources().getString(R.string.error_field_required));
                    error = true;
                }

                if (!error) {
                    String url = "users/addUser/" + mUserName;
                    HashMap<String, String> data = new HashMap<String, String>();
                    AsyncTaskModuler moduler = new AsyncTaskModuler(SignInActivity.this, data, url, completeSignIn);
                    moduler.execute();
                }
            }
        });
    }

    OnTaskCompleteListener completeSignIn = new OnTaskCompleteListener() {

        @Override
        public void onCompleteListener(String response) {
            try {
                addUserInfoToPref(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void addUserInfoToPref(String response) throws JSONException {
        // convert response data to json object
        JSONObject serverResponse = new JSONObject(response);

        // check your log for json response
        Log.v("SignInServerResponse", serverResponse.toString());

        // json success tag
        int success = 0;
        success = serverResponse.getInt("success");

        if (success == 1) {
            JSONObject userData = serverResponse.getJSONObject("data");

            SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

            // Writing data to SharedPreferences
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("user_id", userData.getString("_id"));
            editor.putString("user_name", userData.getString("username"));
            editor.putString("user_private_key", userData.getString("private_key"));
            editor.putBoolean("logged_in", true);

            // save data to shared preferences
            editor.commit();

            // start main activity intent
            Intent theIntent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(theIntent);
        } else {
            String serverMessage = serverResponse.getString("message");

            Toast.makeText(SignInActivity.this, serverMessage,
                    Toast.LENGTH_LONG).show();
        }
    }
}
