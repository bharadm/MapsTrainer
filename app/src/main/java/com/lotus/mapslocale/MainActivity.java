package com.lotus.mapslocale;

import android.content.Intent;
import android.net.http.RequestQueue;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.lotus.mapslocale.R.drawable.common_google_signin_btn_icon_dark;

public class MainActivity extends AppCompatActivity {
    com.android.volley.RequestQueue requestQueue;
    String inserturl="http://lotusworks.16mb.com/correction.php";
    Button s,submis;
    TextView result;
    EditText editText[]=new EditText[10];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        editText[0]=(EditText)findViewById(R.id.et_fn);
        editText[1]=(EditText)findViewById(R.id.et_ln);
        editText[2]=(EditText)findViewById(R.id.et_ma);
        editText[3]=(EditText)findViewById(R.id.et_pa);
        editText[4]=(EditText)findViewById(R.id.et_cat);
        editText[5]=(EditText)findViewById(R.id.et_des);
        editText[6]=(EditText)findViewById(R.id.et_pa);
        editText[7]=(EditText)findViewById(R.id.et_av);
        editText[8]=(EditText)findViewById(R.id.et_lo);
        editText[9]=(EditText)findViewById(R.id.et_lo1);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
/*        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        */
    }
    public void bt_submit1(View v){
        Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
        StringRequest request=new StringRequest(Request.Method.POST, inserturl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.print(""+response);
                Toast.makeText(MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("Firstname",editText[0].getText().toString());
                params.put("Lastname",editText[1].getText().toString());
                params.put("Email",editText[2].getText().toString());
                params.put("Password1",editText[3].getText().toString());
                params.put("Category",editText[4].getText().toString());
                params.put("Description",editText[5].getText().toString());
                params.put("Price",editText[6].getText().toString());
                params.put("Availability",editText[7].getText().toString());
                params.put("Location",editText[8].getText().toString()+","+editText[9].getText().toString());
//                Toast.makeText(MainActivity.this, "Working i guess", Toast.LENGTH_SHORT).show();
                return params;
            }
        };
        requestQueue.add(request);
    }
    public void bt_home(View view){
        startActivity(new Intent(this,Main3Activity.class));
        finish();
    }
}
