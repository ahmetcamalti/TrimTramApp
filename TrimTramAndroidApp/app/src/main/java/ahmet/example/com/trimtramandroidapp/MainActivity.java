package ahmet.example.com.trimtramandroidapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
    }
}
