package com.example.proofofconcept;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
     ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Create default options which will be used for every
//  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
           .cacheInMemory(true)
                .cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
           .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);

        listView = findViewById(R.id.listview);

        new Task().execute("https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json");



    }

}

// <----------Creating a sync class to sync the url connect and set data to list-------------->


 class Task extends AsyncTask<String, String, List<Models>> {

     private Context tag;


     @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast toast = Toast.makeText(tag, "Loading Data...", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    protected List<Models> doInBackground(String... urls) {

        //       <------ setup server connection------->

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream istream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(istream));

            StringBuffer buffer = new StringBuffer();

            String line;

            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }

            String finalJson = buffer.toString();

            JSONObject parentJ = new JSONObject(finalJson);
            JSONArray parentA = parentJ.getJSONArray("rows");

            Gson gson = new Gson();
            List<Models> modelsList = new ArrayList<>();
            for(int i = 0; i < parentA.length(); i++){
                JSONObject finalJ = parentA.getJSONObject(i);
                Models models = gson.fromJson(finalJ.toString(), Models.class);
//                Models models = new Models();
//                models.setTitle(finalJ.getString("title"));
//                models.setDescription(finalJ.getString("description"));
//                models.setImageHref(finalJ.getString("imageHref"));

//         <--------------addiing final object in the list------------->
                modelsList.add(models);
            }

            return modelsList;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(connection != null) {
                connection.disconnect();
            }
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
     @Override
     protected void onPostExecute(List<Models> result) {
         super.onPostExecute(result);

//            <--------------- data to be set in the list ------------->

         Adapter adapter = new Adapter(tag, R.layout.row, result);

         listView.setAdapter(adapter);

     }

     }












