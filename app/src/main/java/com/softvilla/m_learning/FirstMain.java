package com.softvilla.m_learning;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.mock.MockPackageManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FirstMain extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    private GoogleMap mMap;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation, start, end;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION, destName;
    RelativeLayout mapRealitiveLayout;
    TextView addObject,markedObject,trips;
    ImageView fullscreenmap;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_main);
        mapRealitiveLayout = (RelativeLayout) findViewById(R.id.maprl);
        addObject = (TextView) findViewById(R.id.addObject);
        markedObject = (TextView) findViewById(R.id.markedObject);
        trips = (TextView) findViewById(R.id.trips);
        fullscreenmap = (ImageView) findViewById(R.id.fullscreenmap);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(FirstMain.this);


        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                //execute every time, else your else part will work
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        fullscreenmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstMain.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        final SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(FirstMain.this);
        preferences1.getString("selectedTrip","");

        addObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImageInfo.lat ==0.0 && ImageInfo.lng ==0.0){
                    Toast.makeText(FirstMain.this, "Wait for Getting Location.", Toast.LENGTH_SHORT).show();

                }

                else if(preferences1.getString("selectedTrip","") != ""){
                    final ProgressDialog pDialog = new ProgressDialog(FirstMain.this);
                    pDialog.setMessage("Loading...");
                    pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
                    pDialog.show();
                    AndroidNetworking.post("http://mlearningfieldtrips.com/Api/CheckTripes")

                            .addBodyParameter("trip", preferences1.getString("selectedTrip",""))
                            .setTag("test")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {
                                   if(response.equalsIgnoreCase("true")){
                                       startActivity(new Intent(FirstMain.this,Main2Activity.class));
                                   }
                                   else
                                   {
                                       final AlertDialog.Builder builder = new AlertDialog.Builder(FirstMain.this, R.style.LightDialogTheme);
                                       builder.setMessage("Your Selected Trip is Deleted by Admin. Please Select Trip And Try Again.")
                                               .setCancelable(false)
                                               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                   public void onClick(final DialogInterface dialog, final int id) {
                                                       //FirstMain.this.finish();
                                                       startActivity(new Intent(FirstMain.this,Trips.class));
                                                       //FirstMain.this.overridePendingTransition(0,0);
                                                   }
                                               });
                            /*.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    dialog.cancel();
                                }
                            });*/
                                       final AlertDialog alert = builder.create();
                                       alert.show();
                                   }
                                   pDialog.dismiss();
                                    // do anything with response
                                }
                                @Override
                                public void onError(ANError error) {
                                    Toast.makeText(FirstMain.this, error.toString(), Toast.LENGTH_SHORT).show();
                                    pDialog.dismiss();
                                    // handle error
                                }
                            });
                }

                if(preferences1.getString("selectedTrip","") == "" || preferences1.getString("selectedTrip",null) == null){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(FirstMain.this, R.style.LightDialogTheme);
                    builder.setMessage("Please Select Trip First")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    //FirstMain.this.finish();
                                    startActivity(new Intent(FirstMain.this,Trips.class));
                                    //FirstMain.this.overridePendingTransition(0,0);
                                }
                            });
                            /*.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    dialog.cancel();
                                }
                            });*/
                    final AlertDialog alert = builder.create();
                    alert.show();
                }

                /*else{
                    startActivity(new Intent(FirstMain.this,Main2Activity.class));
                }*/
            }
        });

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        //Manifest.permission.ACCESS_COARSE_LOCATION,
                       // Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        markedObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstMain.this,ListOfMarkedPlaces.class));
            }
        });

        trips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstMain.this,Trips.class));
            }
        });
        statusCheck();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            showDialog(999);
//            Toast.makeText(getApplicationContext(), "ca",
//                    Toast.LENGTH_SHORT)
//                    .show();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(FirstMain.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("isLogin","0");
            editor.apply();
            startActivity(new Intent(FirstMain.this,LogIn.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));*/
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //start = location;
        ImageInfo.lat = location.getLatitude();
        ImageInfo.lng = location.getLongitude();

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);


        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        //stop location updates
        /*if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }*/
    }


    public void statusCheck() {
        final LocationManager manager = (LocationManager) FirstMain.this.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(FirstMain.this, R.style.LightDialogTheme);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        FirstMain.this.finish();
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        FirstMain.this.overridePendingTransition(0,0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


}
