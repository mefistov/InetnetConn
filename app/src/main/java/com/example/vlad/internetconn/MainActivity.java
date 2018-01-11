package com.example.vlad.internetconn;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn = null;
    TextView tv = null;
    TextView tv1 = null;
    TextView tv2 = null;

    String url = new String("http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=a0b864a6d7a59953f1fc87e4b7faa1a7");
    //JSONObject jsonObject = null;
    MyPojo myPojo = null;
    public MainActivity() throws MalformedURLException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btnOk);
        btn.setOnClickListener(this);
        tv = findViewById(R.id.tv);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);


    }

    @Override
    public void onClick(View v) {
        Log.e("click", "btn");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String str = getContent(url);
                    tv.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("messege",str);
                            Gson gson = new Gson(); // Or use new GsonBuilder().create();
                            myPojo = gson.fromJson(str, MyPojo.class);

                            tv.setText("City: " + myPojo.getName());
                            tv2.setText("temp: " + myPojo.getMain().getTemp());
                            tv1.setText("Wheather: " + myPojo.getWeather()[0].getMain());

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();





    }


    private String getContent(String path) throws IOException {
        BufferedReader reader = null;
        try {
            URL url = new URL(path);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setReadTimeout(10000);
            c.connect();
            reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                buf.append(line + "\n");
            }
            return (buf.toString());
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private class MyTask extends AsyncTask<String,Void,String> {
        String str;

        @Override
        protected String doInBackground(String... strings) {
            try {
                str = getContent(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            tv.setText(s);
        }
    }
}
