package com.lotus.mapslocale;

import android.app.AlertDialog;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    String json;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "Firstname";
    private static final String TAG_LOCATION = "Location";
    JSONArray name = null;
    String showurl = "http://lotusworks.16mb.com/display.php";
    AlertDialog.Builder alert;
    com.android.volley.RequestQueue requestQueue;
    AlertDialog alertDialog;
    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        //getData();
    }
    /*public Location getLocation(String provider) {
        if (locationManager.isProviderEnabled(provider)) {
            locationManager.requestLocationUpdates(provider,
                    6000, MIN_DISTANCE_FOR_UPDATE, myLocationListener);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(provider);
                return location;
            }
        }
        return null;
    }*/

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    String location[]=new String[20];
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getData(googleMap);
    }

    public void getData(final GoogleMap googleMap1) {
        class GetDataJson extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httpPost = new HttpPost("http://lotusworks.16mb.com/databaa.php");
                httpPost.setHeader("Content-type", "application/json");
                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    inputStream = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder builder = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line + "\n");
                    }
                    result = builder.toString();

                } catch (Exception e) {

                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (Exception e) {

                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                json = s;
                showlist(googleMap1);
            }
        }
        GetDataJson dataJson = new GetDataJson();
        dataJson.execute();
    }

    float degres[] = new float[2];

    protected void showlist(GoogleMap googleMap) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            name = jsonObject.getJSONArray(TAG_RESULTS);
            for (int i = 0,k=0; i < name.length(); i++) {
                JSONObject c = name.getJSONObject(i);
                String id = c.getString(TAG_NAME);
                location[k++] = c.getString(TAG_LOCATION);
                lata(location[k-1], id, googleMap);
//               c`
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void lata(String matter, String name, GoogleMap googleMap) {
        int k = 0, l = 0, m = 0;
        for (int i = 0; i < matter.length(); i++) {
            if (matter.charAt(i) == ',') {
                degres[k++] = Float.parseFloat(matter.substring(l, i));
                Toast.makeText(this, "" + String.valueOf(degres[k - 1]), Toast.LENGTH_SHORT).show();
                l = i;
                break;
            }
        }
        degres[1] = Float.parseFloat(matter.substring(l + 1));
        //Toast.makeText(this, ""+String.valueOf(degres[1]), Toast.LENGTH_SHORT).show();
        final GoogleMap mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(new LatLng(degres[0], degres[1])).title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.unit)));
    }

    public void filter(final String title, final String locatio){
        StringRequest request=new StringRequest(Request.Method.POST, showurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.print(""+response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("Firstname",title);
                params.put("Location",locatio);
//                Toast.makeText(MainActivity.this, "Working i guess", Toast.LENGTH_SHORT).show();
                return params;
            }
        };
        requestQueue.add(request);
    }

    public void locacheck(String loca,String title){
        for(int i=0;i<location.length;i++){
            if(location[i].contains(loca)){
                filter(title,location[i]);
                break;
            }
        }
    }
    public void some() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                showurl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    JSONArray students = response.getJSONArray("information");
                    for (int i = 0; i < students.length(); i++) {
                        JSONObject student = students.getJSONObject(i);
                        String firstname = student.getString("Firstname");
                        String lastname = student.getString("Lastname");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void something(){
        new AlertDialog.Builder(getApplicationContext())
                .setTitle("")
                .setMessage("");
    }
}
