package ahmet.example.com.trimtramandroidapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Ahmet on 26-Mar-17.
 */
public class AsyncTaskModuler  extends AsyncTask<Void, Void, String> {

    private Context context;
    private HashMap<String, String> data;
    private String URL;
    private OnTaskCompleteListener taskdone;
    private String baseURL;

    // OkHttpClient Library
    private OkHttpClient client = new OkHttpClient();

    public AsyncTaskModuler(Context ctx, HashMap<String, String> data, String url, OnTaskCompleteListener taskdone) {

        this.context = ctx;
        this.data = data;
        this.URL = url;
        this.taskdone = taskdone;
        this.baseURL = "http://zabalunga.herokuapp.com/";
    }

    @Override
    protected String doInBackground(Void... params) {
        String responseData = null;

        try {
            // Simulate network access.
            String url = this.baseURL + this.URL;
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

            // check your log for json response
            Log.v("Server Response", responseData);

            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Interrupt Exception
            e.printStackTrace();

            return null;
        }

        return responseData;
    }

    @Override
    protected void onPostExecute(String result) {
        taskdone.onCompleteListener(result);
    }
}