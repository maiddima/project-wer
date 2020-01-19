package maiddima.imperfect.wer.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

import maiddima.imperfect.wer.Constants;
import maiddima.imperfect.wer.R;
import maiddima.imperfect.wer.RequestQueueSingleton;
import maiddima.imperfect.wer.ShakeEventListener;
import maiddima.imperfect.wer.model.Message;
import maiddima.imperfect.wer.model.MessageAdapter;


public class MainActivity extends AppCompatActivity {



    private EditText et_studentMessage;
    private ImageView iv_sendMessage;
    private ListView list_messages;
    private MessageAdapter msgAdapter;

    double locationLatitude;
    double locationLongitude;

    RequestQueue requestQueue;

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    FusedLocationProviderClient mFusedLocationClient;

    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Attach the adapter to a ListView
        msgAdapter = new MessageAdapter(this);
        list_messages = findViewById(R.id.list_messages);
        list_messages.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list_messages.setAdapter(msgAdapter);

        msgAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                list_messages.setSelection(msgAdapter.getCount() - 1);
            }
        });

        et_studentMessage = findViewById(R.id.et_message);
        iv_sendMessage = findViewById(R.id.iv_sendMessage);

        iv_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMessages();
                et_studentMessage.getText().clear();
            }
        });

        requestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext())
                .getRequestQueue();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                getMessages();
                Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getMessages();

        getLastLocation();
    }

    public void postMessages() {

        getLastLocation();

        JSONObject message = new JSONObject();

        try {
            message.put("student_id", 33173463);
            message.put("gps_lat", locationLatitude);
            message.put("gps_long",locationLongitude);
            message.put("student_message", et_studentMessage.getText().toString());
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,Constants.BASE_URL,message,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("RAW MESSAGE : ",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERROR MESSAGE : ",error.toString());
            }
        });

        jsonObjectRequest.setTag("Post Message");
        requestQueue.add(jsonObjectRequest);
        getMessages();
    }


    public void getMessages() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, Constants.BASE_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                msgAdapter.clear();
                try{
                    for(int i = response.length()-1; i >= 0; i--){
                        // Get current json object
                        JSONObject message = response.getJSONObject(i);

                        // Get the current student (json object) data
                        int msgId = message.getInt("id");
                        int student_id = message.getInt("student_id");
                        double lat = message.getDouble("gps_lat");
                        double lon = message.getDouble("gps_long");
                        String student_message = message.getString("student_message");

                        if (student_id != 20130039){
                            Message msg = new Message(student_id,lat,lon,student_message,msgId);
                            list_messages.setAdapter(msgAdapter);
                            msgAdapter.add(msg);
                            list_messages.setSelection(list_messages.getCount() - 1);
                        }

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

                Log.i("RAW MESSAGE : ",response.toString());
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.i("ERROR MESSAGE : ",error.toString());
            }
        }
        );

        jsonArrayRequest.setTag("Get Messages");
        requestQueue.add(jsonArrayRequest);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    requestNewLocationData();
                                    double lat = location.getLatitude();
                                    double lon = location.getLongitude();

                                    BigDecimal la = new BigDecimal(lat).setScale(3, RoundingMode.HALF_UP);
                                    BigDecimal lo = new BigDecimal(lon).setScale(3, RoundingMode.HALF_UP);

                                    locationLatitude = la.doubleValue();
                                    locationLongitude = lo.doubleValue();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(5);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            double lat = mLastLocation.getLatitude();
            double lon = mLastLocation.getLongitude();

            BigDecimal la = new BigDecimal(lat).setScale(4, RoundingMode.HALF_UP);
            BigDecimal lo = new BigDecimal(lon).setScale(4, RoundingMode.HALF_UP);

            locationLatitude = la.doubleValue();
            locationLongitude = lo.doubleValue();
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);

        getMessages();

        if (checkPermissions()) {
            getLastLocation();
        }
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}
