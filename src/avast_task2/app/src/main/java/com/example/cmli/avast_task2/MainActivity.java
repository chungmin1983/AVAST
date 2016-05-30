package com.example.cmli.avast_task2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;

import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.json.JSONObject;
import org.json.JSONArray;
import java.util.Iterator;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

public class MainActivity extends AppCompatActivity {

	private EditText name;
    private TextView result;
    
    private Spinner age;  
    private ArrayAdapter<String> adapter;  
    private static final String[] ageList={"0","1","2","3","4","5","6"}; 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.name = (EditText) this.findViewById(R.id.name);
        this.result = (TextView) this.findViewById(R.id.result);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        
        this.age = (Spinner) findViewById(R.id.age);  
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ageList);  
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        this.age.setAdapter(adapter);

        Button butAdd = (Button)findViewById(R.id.addButton);
        butAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIPut();
            }
        });
        Button butUpdate = (Button)findViewById(R.id.updateButton);
        butUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIPost();
            }
        });
        Button butGet = (Button)findViewById(R.id.getButton);
        butGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIGet();
            }
        });
        Button butDel = (Button)findViewById(R.id.delButton);
        butDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIDelete();
            }
        });
    }

    String myIP = "140.109.19.99";
    int myPort = 3000;

    private void APIPut() {
        String x = URLPost(myIP, myPort, "/a_put?" +
            this.name.getText().toString() + "=" +
            this.age.getSelectedItem().toString()
        );
        this.result.setText("Add" + "   " + this.name.getText().toString());
    }

    private void APIPost() {
        String x = URLPost(myIP, myPort, "/a_post?" +
            this.name.getText().toString() + "=" +
            this.age.getSelectedItem().toString()
        );
        this.result.setText("Update" + "   " + this.name.getText().toString());
    }

    private void APIGet() {
        String x = URLGet(myIP, myPort, "/get/All");
        this.result.setText(JSONGet(x));
    }

    private void APIDelete() {
        String x = URLGet(myIP, myPort, "/delete/" +  this.name.getText().toString());
        this.result.setText("Delete" + "   " + this.name.getText().toString());    
    }

    private String JSONGet(String s) {
        String out = "";
        try {
            JSONObject jsonObj = new JSONObject(s);
            Iterator<String> iter = jsonObj.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                out = out + key;
                JSONObject jsonObj1 = new JSONObject(jsonObj.getString(key));
                out = out + "   " + jsonObj1.getString("age");
                out = out + "   " + jsonObj1.getString("time") + "\n";
            }
            return out;
        }
        catch (Exception e) {
            return "Error";
        }
    }

    public static String URLPost(String host, int port, String path) {
        try{
            URL url = new URL("http", host, port, path);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            String line;
            StringBuilder sb = new StringBuilder();
            BufferedReader out = new BufferedReader(new 
                                     InputStreamReader(conn.getInputStream()));
            while ((line = out.readLine()) != null) {
                sb.append(line);
            }
            out.close();
            return sb.toString();
        }
        catch (Exception e) {
            return "Error: URLPost";
        }
    }

    public static String URLGet(String host, int port, String path) {
        try{
            URL url = new URL("http", host, port, path);
            URLConnection conn = url.openConnection();
            conn.setDoInput(true);
            conn.setAllowUserInteraction(true);
            conn.connect();
            StringBuilder sb = new StringBuilder();
            BufferedReader in = new BufferedReader(
            new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            return sb.toString();
        }
        catch (Exception e) {
            return "Error: URLGet";
        }
    }
}
