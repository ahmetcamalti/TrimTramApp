package ahmet.example.com.trimtramandroidapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SignInActivity extends AppCompatActivity {

    // username
    private String mUserName;

    // SignIn Async Task
    private SignIn mAuthTask = null;

    // OkHttpClient Library
    private OkHttpClient client = new OkHttpClient();

    // CircularProgressView Library
    private CircularProgressView progressView;

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
                    // Show a progress spinner, and kick off a background task to
                    progressView = (CircularProgressView) findViewById(R.id.progress_view);
                    progressView.setIndeterminate(true);
                    progressView.startAnimation();

                    // perform the add book attempt.
                    mAuthTask = new SignIn(mUserName);
                    mAuthTask.execute((Void) null);
                }
            }
        });
    }

    /**
     * Represents an asynchronous add book task for new book
     */
    public class SignIn extends AsyncTask<Void, Void, Boolean> {

        private User mUser;

        private String serverMessage;

        SignIn(String userName) {
            mUser = new User(userName);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            int success = 0;

            try {
                // Simulate network access.
                String responseData = null;
                String url = "http://trimtramapp.herokuapp.com/users/addUser/" + mUserName;
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
                JSONObject json = new JSONObject(responseData);

                // check your log for json response
                Log.v("Login Server Response", json.toString());

                // json success tag
                success = json.getInt("success");

                if (success == 1) {
                    JSONObject userData = json.getJSONObject("user");

                    mUser.setUserId(userData.getString("_id"));
                    mUser.setPrivateKey(userData.getString("private_key"));
                }
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

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

                // Writing data to SharedPreferences
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("user_id", mUser.getUserId());
                editor.putString("user_name", mUser.getUserName());
                editor.putString("user_private_key", mUser.getPrivateKey());
                editor.putBoolean("logged_in", true);

                // save data to shared preferences
                editor.commit();

                // start main activity intent
                Intent theIntent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(theIntent);
            } else {
                Toast.makeText(SignInActivity.this, serverMessage,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            // set null async task
            mAuthTask = null;

            // Stop progress spinner
            progressView.setVisibility(View.GONE);
            progressView.stopAnimation();
        }
    }
}
