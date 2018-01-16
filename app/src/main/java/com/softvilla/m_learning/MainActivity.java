package com.softvilla.m_learning;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.mock.MockPackageManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation, start, end;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION, destName;
    LatLng addressPos, finalAddressPos, destDetail = null;
    LocationManager locationManager;


    private static final int CAMERA_REQUEST = 1888;
    private static final int REQ_CODE_PICK_SOUNDFILE = 123;
    private static final int REQ_CODE_PICK_VIDEOFILE = 321;
    LinearLayout linearLayout,linearLayoutvideo,linearLink;
    double testLat,testLng;
    String[] s = {"Graduation", "Intermediate", "Matriculation" };
    String lable;

    private static final float END_SCALE = 0.7f;
    ImageView fullscreenmap;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView labelView,home,about;
    private View contentView;
    EditText description;
    Spinner sItems;
    //LocationManager locationManager;
    Location location;
    String mprovider;
    Criteria criteria;
    GoogleMap mGoogleMap;
    SupportMapFragment mFragment;
    Marker currLocationMarker;
    //Location mLastLocation;
    //GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusCheck();
        fullscreenmap = (ImageView) findViewById(R.id.fullscreenmap);
        fullscreenmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,FirstMain.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);

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



        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
        //mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
       // android.location.Address address = addressList.get(0);
        //LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        //mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(location));
        //mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//


        /*drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);*/
        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //home = (TextView) findViewById(R.id.home);
        //about = (TextView) findViewById(R.id.about);
        /*contentView = findViewById(R.id.content);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.nav_camera) {

                    if (ImageInfo.lat ==0.0 && ImageInfo.lng ==0.0){
                        Toast.makeText(MainActivity.this, "Wait for Getting Location.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Intent main2Activity = new Intent(MainActivity.this,Main2Activity.class);
                        startActivity(main2Activity);
                    }

                } *//*else if (id == R.id.nav_Map) {
                    *//**//*Intent maps = new Intent(MainActivity.this,Main2Activity.class);
                    startActivity(maps);*//**//*
                    //Toast.makeText(MainActivity.this, "Gallery..", Toast.LENGTH_SHORT).show();
                }*//*

                else if (id == R.id.nav_List_Of_Mark_objects) {
                    Intent maps = new Intent(MainActivity.this,ListOfMarkedPlaces.class);
                    startActivity(maps);
                    //Toast.makeText(MainActivity.this, "In Progress...", Toast.LENGTH_SHORT).show();
                }

                //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        toolbar.setNavigationIcon(new DrawerArrowDrawable(this));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     if (drawerLayout.isDrawerOpen(navigationView)) {
                                                         drawerLayout.closeDrawer(navigationView);
                                                     }
                                                     else {
                                                         drawerLayout.openDrawer(navigationView);
//
                                                     }
                                                 }
                                             }
        );

        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                           @Override
                                           public void onDrawerSlide(View drawerView, float slideOffset) {
                                               //labelView.setVisibility(slideOffset > 0 ? View.VISIBLE : View.GONE);

                                               // Scale the View based on current slide offset
                                               final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                                               final float offsetScale = 1 - diffScaledOffset;
                                               contentView.setScaleX(offsetScale);
                                               contentView.setScaleY(offsetScale);

                                               // Translate the View, accounting for the scaled width
                                               final float xOffset = drawerView.getWidth() * slideOffset;
                                               final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                                               final float xTranslation = xOffset - xOffsetDiff;
                                               contentView.setTranslationX(xTranslation);
                                           }

                                           @Override
                                           public void onDrawerClosed(View drawerView) {
                                               //labelView.setVisibility(View.GONE);
                                           }
                                       }
        );*/
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


    /*@Override
    public void onDestroy() {
        super.onDestroy();
        final FragmentManager fragManager = this.getFragmentManager();
        final Fragment fragment = fragManager.findFragmentById(R.id.fragmentId);
        if(fragment!=null){
            fragManager.beginTransaction().remove(fragment).commit();
        }
    }
*/
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
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("isLogin","0");
            editor.apply();
            startActivity(new Intent(MainActivity.this,LogIn.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {


            /*final Bitmap photo = (Bitmap) data.getExtras().get("data");


            View alertView = getLayoutInflater().inflate(R.layout.alert, null);


            linearLayout = (LinearLayout) findViewById(R.id.lin);
            linearLayoutvideo = (LinearLayout) findViewById(R.id.linvideo);


            if (requestCode == REQ_CODE_PICK_SOUNDFILE && resultCode == Activity.RESULT_OK){
                if ((data != null) && (data.getData() != null)){

                    final Uri imageUri = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(imageUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    final ImageView showimage = new ImageView(this);

                    BitmapDrawable bD = new BitmapDrawable(getResources(),bitmap);
                    showimage.setBackgroundDrawable(bD);

                    LinearLayout.LayoutParams ImageParams = new LinearLayout.LayoutParams(200, 200);
                    showimage.setLayoutParams(ImageParams);
                    showimage.requestLayout();

                    final LinearLayout linear = new LinearLayout(this);
                    //setContentView(linear);
                    linear.setOrientation(LinearLayout.HORIZONTAL);

                    linear.addView(showimage);
                    final ImageView image = new ImageView(this);
                    image.setImageResource(R.drawable.crosssign);
                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //linear.removeView(showimage);
                            //image.setVisibility(View.INVISIBLE);
                            linearLayout.removeView(linear);
                        }
                    });


                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

                    image.setLayoutParams(layoutParams);
                    image.requestLayout();
                    linear.addView(image);
                    linearLayout.addView(linear);

                }
            }


            if (requestCode == REQ_CODE_PICK_VIDEOFILE && resultCode == Activity.RESULT_OK){
                if ((data != null) && (data.getData() != null)){

                    final Uri imageUri = data.getData();

                    final ImageView showimagevideo = new ImageView(this);
                    LinearLayout.LayoutParams layoutParamsvideo1 = new LinearLayout.LayoutParams(200,200);

                    showimagevideo.setLayoutParams(layoutParamsvideo1);
                    showimagevideo.requestLayout();

                *//*Bitmap bmThumbnail;

//MICRO_KIND, size: 96 x 96 thumbnail
                bmThumbnail = ThumbnailUtils.createVideoThumbnail(String.valueOf(imageUri), MediaStore.Video.Thumbnails.MICRO_KIND);
                showimagevideo.setImageBitmap(bmThumbnail);

// MINI_KIND, size: 512 x 384 thumbnail
                *//**//*bmThumbnail = ThumbnailUtils.createVideoThumbnail(String.valueOf(imageUri), MediaStore.Images.Thumbnails.MINI_KIND);
                imageview_mini.setImageBitmap(bmThumbnail);*//**//*

                LinearLayout.LayoutParams ImageParams = new LinearLayout.LayoutParams(200, 200);
                showimagevideo.setLayoutParams(ImageParams);
                showimagevideo.requestLayout();*//*


                    Glide
                            .with( this )
                            .load( String.valueOf(imageUri) )
                            .into( showimagevideo );

                    final LinearLayout linearvideo = new LinearLayout(this);
                    //setContentView(linear);
                    linearvideo.setOrientation(LinearLayout.HORIZONTAL);

                    linearvideo.addView(showimagevideo);
                    final ImageView imagevideo = new ImageView(this);
                    imagevideo.setImageResource(R.drawable.crosssign);
                    imagevideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //linear.removeView(showimage);
                            //image.setVisibility(View.INVISIBLE);
                            linearLayoutvideo.removeView(linearvideo);
                        }
                    });


                    LinearLayout.LayoutParams layoutParamsvideo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

                    imagevideo.setLayoutParams(layoutParamsvideo);
                    imagevideo.requestLayout();
                    linearvideo.addView(imagevideo);
                    linearLayoutvideo.addView(linearvideo);

                }
            }

            description = (EditText) alertView.findViewById(R.id.description);
            description.setHint("Enter Description");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, s);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            sItems = (Spinner)  alertView.findViewById(R.id.mySpinner);
            sItems.setAdapter(adapter);
            sItems.setOnItemSelectedListener(this);



            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Add Object Detail");
            alertDialog.setView(alertView);

            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String password = description.getText().toString();
                            AddLocaton addLocaton = new AddLocaton();
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                            final byte[] imageBytes = byteArrayOutputStream.toByteArray();
                            final String StringPhoto = Base64.encodeToString(imageBytes,Base64.DEFAULT);

                            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            criteria = new Criteria();

                            mprovider = locationManager.getBestProvider(criteria, false);

                            if (mprovider != null && !mprovider.equals("")) {
                                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                location = locationManager.getLastKnownLocation(mprovider);
                                locationManager.requestLocationUpdates(mprovider,12000,7,MainActivity.this);

                                if (location != null)
                                    onLocationChanged(location);
                                else
                                    Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
                            }



                            if(TextUtils.isEmpty(password)){
                                description.setError("Enter Object Description");
                            }
                            else {
                                addLocaton.LocationName = password;
                                addLocaton.Image = StringPhoto;
                                addLocaton.LocationLng = testLng;
                                addLocaton.LocationLat = testLat;
                                addLocaton.studyLevel = lable;
                                addLocaton.save();
                            }

                            Toast.makeText(MainActivity.this, "Your Current Location is: "+testLat+"\n"+testLng, Toast.LENGTH_SHORT).show();


                            //Toast.makeText(MainActivity.this, "Saved Successfully.", Toast.LENGTH_SHORT).show();
                        }

                    });


            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            //alertDialog.setView(sp);
            alertDialog.show();*/

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        lable = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this, "you selected: " +lable, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }





    public void selectVideo(View view) {

        Intent intent;
        intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQ_CODE_PICK_VIDEOFILE);
    }

    public void selectImage(View view) {

        Intent intent;
        intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        /*intent.setType("audio/mpeg");
        intent.setType("audio*//*");*/
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQ_CODE_PICK_SOUNDFILE);
    }

    public void addLink(View view) {

        final EditText adddLink = new EditText(this);
        LinearLayout.LayoutParams layoutParamsvideoLink = new LinearLayout.LayoutParams(750,LinearLayout.LayoutParams.WRAP_CONTENT);

        adddLink.setLayoutParams(layoutParamsvideoLink);
        adddLink.requestLayout();

        final LinearLayout linearLink1 = new LinearLayout(this);
        //setContentView(linear);
        linearLink1.setOrientation(LinearLayout.HORIZONTAL);

        linearLink1.addView(adddLink);
        final ImageView imageLink = new ImageView(this);
        imageLink.setImageResource(R.drawable.crosssign);
        imageLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //linear.removeView(showimage);
                //image.setVisibility(View.INVISIBLE);
                linearLink.removeView(linearLink1);
            }
        });

        LinearLayout.LayoutParams layoutParamsLink = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        imageLink.setLayoutParams(layoutParamsLink);
        imageLink.requestLayout();
        linearLink1.addView(imageLink);
        linearLink.addView(linearLink1);
    }


    public void statusCheck() {
        final LocationManager manager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.LightDialogTheme);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        MainActivity.this.finish();
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        MainActivity.this.overridePendingTransition(0,0);
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
