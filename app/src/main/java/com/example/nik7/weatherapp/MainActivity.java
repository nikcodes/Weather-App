package com.example.nik7.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String text;
    Button b;
    EditText city;
    TextView result;

    class DownloadText extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream input = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(input);

                String result = "";
                int c = reader.read();
                while (c != -1) {
                    result += (char) c;
                    c = reader.read();
                }

                return result;
            }
            catch (Exception e){
                e.printStackTrace();
                return "Error";
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject part;
                JSONArray a;
                JSONObject ob = new JSONObject(s);

                String main = ob.getString("main");
                Log.i("main",main);



                if (main.charAt(0) == '{'){
                    part = new JSONObject(main);
                }

                else{
                    a = new JSONArray(main);
                    part = a.getJSONObject(0);

                }

                Log.i("pressure",part.getString("pressure"));

                text += "temp : " + part.getString("temp") + '\n' + "   ";
                text += "min temp : " + part.getString("temp_min") + '\n' + "   ";
                text += "max temp : " + part.getString("temp_max") + '\n' + "   ";
                Log.i("total",Integer.toString(text.length()));



                String weather = ob.getString("weather");
                if (weather.charAt(0) == '{'){
                    part = new JSONObject(weather);
                }

                else{
                    a = new JSONArray(weather);
                    part = a.getJSONObject(0);

                }

                text += "main : " + part.getString("main") + '\n' + "   ";
                text += "description : " + part.getString("description") + '\n' + "   ";
                Log.i("total",Integer.toString(text.length()));

                result.setText("");
                if (!text.equals(""))
                    result.setText(text);

            }
            catch (Exception e){
                e.printStackTrace();
                result.setText("   Entered city is not valid");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = findViewById(R.id.button);
        result = findViewById(R.id.result);
        city = findViewById(R.id.city);


    }


    public void Display(View view){
        text = "   ";
        String cityEntered = city.getText().toString();
        String url = "http://openweathermap.org/data/2.5/weather?q=" + cityEntered + "&appid=b6907d289e10d714a6e88b30761fae22";
        DownloadText task = new DownloadText();
        task.execute(url);

        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(city.getWindowToken(),0);
    }
}
