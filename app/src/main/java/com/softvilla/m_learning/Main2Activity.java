package com.softvilla.m_learning;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class Main2Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, LocationListener {


    private static final int REQ_CODE_PICK_SOUNDFILE = 1;
    private static final int REQ_CODE_PICK_VIDEOFILE = 321;
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_REQUEST_ANGLES = 3214;
    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider;
    double testLat, testLng;
    String[] s = {"","Graduation", "Intermediate", "Matriculation"};
    String studylevel = "<--Select Study Level-->";
    String lable;
    String lableTrip;
    EditText description;
    Spinner sItems;
    //Spinner sItemstrip;
    Button addLink;
    Location location;
    String mprovider;
    Criteria criteria;
    Bitmap photo;
    String password;
    AddLocaton addLocaton;
    DescriptionInfo descriptionInfo;
    String StringPhoto;
    private String selectedPath;
    private TextView textView;
    EditText editTextName;
    ArrayList<String>  images;
    ArrayList<String>  videos;
    ArrayList<String>  links;
    ArrayList<String>  allList;
    ArrayList<String> allList1;
    //ArrayList<String> allListtrip;

    public static final String UPLOAD_URL_IMAGE = "https://malikparactice.000webhostapp.com/ImageUpload/mupload.php";
    public static final String UPLOAD_URL_URL = "http://mlearningfieldtrips.com/Api/url";
    public static final String UPLOAD_URL = "http://mlearningfieldtrips.com/Api/selectedImage";
   // public static final String UPLOAD_CAPTURED_URL = "https://malikparactice.000webhostapp.com/ImageUpload/captured.php";
    public static final String UPLOAD_CAPTURED_URL = "http://mlearningfieldtrips.com/Api/capturedImage";
    public static final String UPLOAD_URL_STUDYLEVEL = "http://mlearningfieldtrips.com/Api/getStudyLevel";
    public static final String UPLOAD_URL_SAVESTUDYLEVEL = "http://mlearningfieldtrips.com/Api/saveStudyLevel";
    public static final String UPLOAD_URL_GETPREF = "http://mlearningfieldtrips.com/Api/getPrefrences";
    public static final String UPLOAD_URL_SAVEPREF = "http://mlearningfieldtrips.com/Api/savePreferences";


    private String KEY_IMAGE = "image";
    private String KEY_DESCRIPTION = "description";
    private String KEY_URL = "url";
    private String KEY_COORDINATES = "coordinates";
    private String KEY_LNG = "lng";

    private String KEY_CAPTURED_IMAGE = "cimage";
    public static final String UPLOAD_KEY = "image";
    public static final String KEY_CATAGORY = "catagory";
    public static final String KEY_LEVEL = "level";
    private Bitmap bitmap;
    private Bitmap capturedbitmap;
    File compressedImageFile;
    EditText adddLink;
    private Uri filePath;
    private Uri filePathCaptured;
    String filePath1;

    Button selectImage,selectVideo;
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();

    private long fileSize = 0;
    TextView showPath;
    LinearLayout linearLayout, linearLayoutvideo, linearLink,LinearLayoutCapturedImage;
    String imagepath;
    String object_id,Object_id_link,Object_id_level,Object_id_video;
    boolean isVideo = false;


    int idx;
    Cursor cursor;
    File finalFile;
    private File root;
    private ArrayList<File> fileList = new ArrayList<File>();
    private LinearLayout view;
    LinearLayout linearLink1;
    StateVO stateVO;
    Spinner spinner;
    boolean ischecked = false;
    int count;

    ArrayList<String> prefId;
   // ArrayList<String> tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        prefId = new ArrayList<String>();
        //tripId = new ArrayList<String>();
//        selectImage = (Button) findViewById(R.id.selectFile);
//        selectVideo = (Button) findViewById(R.id.video);
//        addLink = (Button) findViewById(R.id.addLink);
        //Toast.makeText(this, "Main2Activity", Toast.LENGTH_SHORT).show();
        //editTextName = (EditText) findViewById(R.id.description);
          linearLayout = (LinearLayout) findViewById(R.id.lin);
//        linearLayoutvideo = (LinearLayout) findViewById(R.id.linvideo);
//        linearLink = (LinearLayout) findViewById(R.id.linearLink);
          LinearLayoutCapturedImage = (LinearLayout) findViewById(R.id.captured);

        lable = "Select Preferences";
       // lableTrip = "Select Trip";

        StateVO.CheckedData = new ArrayList<>();



        images = new ArrayList<>();
       /* videos = new ArrayList<>();
        links = new ArrayList<>();
        allList = new ArrayList<>();
*/

        //getStudyLevel();
        getPrefrences();
        //getTrip();

        //textView  = (TextView) findViewById(R.id.textView);

        Intent cameraIntent2 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent2, CAMERA_REQUEST);


        /*view = (LinearLayout) findViewById(R.id.view);

        //getting SDcard root path
        root = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath());
        getfile(root);

        for (int i = 0; i < fileList.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(fileList.get(i).getName());
            textView.setPadding(5, 5, 5, 5);

            System.out.println(fileList.get(i).getName());

            if (fileList.get(i).isDirectory()) {
                textView.setTextColor(Color.parseColor("#FF0000"));
            }
            view.addView(textView);
        }*/


        /*latituteField = (TextView) findViewById(R.id.TextView02);
        longitudeField = (TextView) findViewById(R.id.TextView04);*/

        // Get the location manager
        /*locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            latituteField.setText("Location not available");
            longitudeField.setText("Location not available");
        }*/
    }



    /*public ArrayList<File> getfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    fileList.add(listFile[i]);
                    getfile(listFile[i]);

                } else {
                    if (listFile[i].getName().endsWith(".png")
                            || listFile[i].getName().endsWith(".jpg")
                            || listFile[i].getName().endsWith(".jpeg")
                            || listFile[i].getName().endsWith(".gif"))

                    {
                        fileList.add(listFile[i]);
                    }
                }

            }
        }
        return fileList;
    }*/

   /* @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }*/

    /* Remove the locationlistener updates when Activity is paused */
    /*@Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }*/


    @Override
    public void onLocationChanged(Location location) {

        /*int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());*/
        /*ImageInfo.lat = location.getLatitude();
        ImageInfo.lng = location.getLongitude();
*/
        /*latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));*/

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String s) {

        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    public void DownLoad(View view) {
        progressBar = new ProgressDialog(view.getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("File downloading ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        //reset progress bar status
        progressBarStatus = 0;

        //reset filesize
        fileSize = 0;

        new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < 100) {

                    // process some tasks
                    progressBarStatus = doSomeTasks();

                    // your computer is too fast, sleep 1 second
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    progressBarHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });
                }

                // ok, file is downloaded,
                if (progressBarStatus >= 100) {

                    // sleep 2 seconds, so that you can see the 100%
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // close the progress bar dialog
                    progressBar.dismiss();
                }
            }
        }).start();


    }

    public int doSomeTasks() {

        while (fileSize <= 1000000) {

            fileSize++;

            if (fileSize == 100000) {
                return 10;
            } else if (fileSize == 200000) {
                return 20;
            } else if (fileSize == 300000) {
                return 30;
            }
            // ...add your own

        }

        return 100;

    }

    public void selectFile(View view) {

        Intent cameraIntent2 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent2, CAMERA_REQUEST);
        /*Intent intent;
        intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        *//*intent.setType("audio/mpeg");
        intent.setType("audio*//**//*");*//*
        intent.setType("image*//*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQ_CODE_PICK_SOUNDFILE);
        //selectImage.setVisibility(View.INVISIBLE);*/
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri tempUri = getImageUri(getApplicationContext(), photo);

            File finalFile = new File(getRealPathFromURI(tempUri));
            try {
                compressedImageFile = new Compressor(this).compressToFile(finalFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                bitmap = new Compressor(this).setQuality(100).compressToBitmap(compressedImageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }



            String imagee = getStringImage(bitmap);
            images.add(imagee);
            //bitmap = BitmapFactory.decodeStream(imageStream);
            final ImageView showimage = new ImageView(this);
            showimage.setImageBitmap(bitmap);
            //showimage.setTag(imagee);

//                BitmapDrawable bD = new BitmapDrawable(getResources(), bitmap);
//                showimage.setBackgroundDrawable(bD);

            LinearLayout.LayoutParams ImageParams = new LinearLayout.LayoutParams(200, 200);
            showimage.setLayoutParams(ImageParams);
            showimage.requestLayout();

            final LinearLayout linear = new LinearLayout(this);
            //linear.setId(R.id.linear);
            //setContentView(linear);
            linear.setOrientation(LinearLayout.HORIZONTAL);

            linear.addView(showimage);
            linear.setTag(imagee);
            final ImageView image = new ImageView(this);
            image.setImageResource(R.drawable.crossign);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String removeImage = (String) linear.getTag();
                    images.remove(removeImage);
                    //linear.removeView(showimage);
                    //image.setVisibility(View.INVISIBLE);
                    linearLayout.removeView(linear);
                    //images.clear();
                    //selectImage.setVisibility(View.VISIBLE);
                }
            });


            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            image.setLayoutParams(layoutParams);
            image.requestLayout();
            linear.addView(image);
            linearLayout.addView(linear);

            //Toast.makeText(this, "in 1", Toast.LENGTH_SHORT).show();
            /*Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri tempUri = getImageUri(getApplicationContext(), photo);
            File finalFile = new File(getRealPathFromURI(tempUri));
            try {
                compressedImageFile = new Compressor(this).compressToFile(finalFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                capturedbitmap = new Compressor(this).compressToBitmap(compressedImageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }





            final ImageView showimage =new ImageView(this) ;
            showimage.setImageBitmap(capturedbitmap);



            LinearLayout.LayoutParams ImageParams = new LinearLayout.LayoutParams(300, 300);
            showimage.setLayoutParams(ImageParams);
            showimage.requestLayout();

            final LinearLayout linear = new LinearLayout(this);
            //setContentView(linear);
            linear.setOrientation(LinearLayout.HORIZONTAL);

            linear.addView(showimage);
            final ImageView image = new ImageView(this);
            image.setImageResource(R.drawable.crossign);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //linear.removeView(showimage);
                    //image.setVisibility(View.INVISIBLE);


                    LinearLayoutCapturedImage.removeView(linear);

                    *//*Intent cameraIntent2 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent2, CAMERA_REQUEST);
*//*

                }
            });


            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            image.setLayoutParams(layoutParams);
            image.requestLayout();
            //linear.addView(image);
            //LinearLayoutCapturedImage.addView(linear);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            final byte[] imageBytes = byteArrayOutputStream.toByteArray();
            StringPhoto = Base64.encodeToString(imageBytes, Base64.DEFAULT);*/

        }


        /*if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            if ((data != null) && (data.getData() != null)) {

                //Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                *//*filePath = data.getData();

                Bitmap photo = null;
                Uri tempUri = null;

                try {
                    photo = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);//.openInputStream(filePath));

                  tempUri = getImageUri(getApplicationContext(), photo);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*//*
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri tempUri = getImageUri(getApplicationContext(), photo);

                File finalFile = new File(getRealPathFromURI(tempUri));
                try {
                    compressedImageFile = new Compressor(this).compressToFile(finalFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    bitmap = new Compressor(this).compressToBitmap(compressedImageFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String imagee = getStringImage(bitmap);
                images.add(imagee);
                //bitmap = BitmapFactory.decodeStream(imageStream);
                final ImageView showimage = new ImageView(this);
                showimage.setImageBitmap(bitmap);
                //showimage.setTag(imagee);

//                BitmapDrawable bD = new BitmapDrawable(getResources(), bitmap);
//                showimage.setBackgroundDrawable(bD);

                LinearLayout.LayoutParams ImageParams = new LinearLayout.LayoutParams(200, 200);
                showimage.setLayoutParams(ImageParams);
                showimage.requestLayout();

                final LinearLayout linear = new LinearLayout(this);
                //linear.setId(R.id.linear);
                //setContentView(linear);
                linear.setOrientation(LinearLayout.HORIZONTAL);

                linear.addView(showimage);
                linear.setTag(imagee);
                final ImageView image = new ImageView(this);
                image.setImageResource(R.drawable.crossign);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String removeImage = (String) linear.getTag();
                        images.remove(removeImage);
                        //linear.removeView(showimage);
                        //image.setVisibility(View.INVISIBLE);
                        linearLayout.removeView(linear);
                        //images.clear();
                        //selectImage.setVisibility(View.VISIBLE);
                    }
                });


                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                image.setLayoutParams(layoutParams);
                image.requestLayout();
                linear.addView(image);
                linearLayout.addView(linear);

            }
        }*/


        /*if (requestCode == REQ_CODE_PICK_VIDEOFILE && resultCode == Activity.RESULT_OK) {
            if ((data != null) && (data.getData() != null)) {

                final Uri imageUri = data.getData();
                selectedPath = getPath(imageUri);
                //textView.setText(selectedPath);
                *//*String destinationUriString = null;
                try {
                    filePath1 = SiliCompressor.with(this).compressVideo(selectedPath, selectedPath);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }*//*

                final ImageView showimagevideo = new ImageView(this);
                LinearLayout.LayoutParams layoutParamsvideo1 = new LinearLayout.LayoutParams(200, 200);

                showimagevideo.setLayoutParams(layoutParamsvideo1);
                showimagevideo.requestLayout();

                *//*String s = getStringImage(selectedPath)
                videos.add(selectedPath);*//*
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
                        .with(this)
                        .load(String.valueOf(imageUri))
                        .into(showimagevideo);
                isVideo = true;

                final LinearLayout linearvideo = new LinearLayout(this);
                //setContentView(linear);
                linearvideo.setOrientation(LinearLayout.HORIZONTAL);

                linearvideo.addView(showimagevideo);
                final ImageView imagevideo = new ImageView(this);
                imagevideo.setImageResource(R.drawable.crossign);
                imagevideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //linear.removeView(showimage);
                        //image.setVisibility(View.INVISIBLE);
                        linearLayoutvideo.removeView(linearvideo);
                        //selectVideo.setVisibility(View.VISIBLE);
                    }
                });


                LinearLayout.LayoutParams layoutParamsvideo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                imagevideo.setLayoutParams(layoutParamsvideo);
                imagevideo.requestLayout();
                linearvideo.addView(imagevideo);
                linearLayoutvideo.addView(linearvideo);

            }
        }*/


        /*addLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adddLink = new EditText(Main2Activity.this);
                LinearLayout.LayoutParams layoutParamsvideoLink = new LinearLayout.LayoutParams(850, LinearLayout.LayoutParams.WRAP_CONTENT);

                adddLink.setLayoutParams(layoutParamsvideoLink);
                adddLink.requestLayout();




                linearLink1 = new LinearLayout(Main2Activity.this);
                //setContentView(linear);
                linearLink1.setOrientation(LinearLayout.HORIZONTAL);

                linearLink1.addView(adddLink);
                String link = adddLink.getText().toString();
                //links.add(link);
                linearLink1.setTag(link);
                final ImageView imageLink = new ImageView(Main2Activity.this);
                imageLink.setImageResource(R.drawable.crossign);
                imageLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //linear.removeView(showimage);
                        //image.setVisibility(View.INVISIBLE);
                        String removeLinks = (String) linearLink1.getTag();
                        links.remove(removeLinks);
                        linearLink.removeView(linearLink1);
                        //addLink.setVisibility(View.VISIBLE);
                    }
                });

                LinearLayout.LayoutParams layoutParamsLink = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                imageLink.setLayoutParams(layoutParamsLink);
                imageLink.requestLayout();
                linearLink1.addView(imageLink);
                linearLink.addView(linearLink1);
                //addLink.setVisibility(View.INVISIBLE);

            }
        });
*/


        description = (EditText) findViewById(R.id.description);
        description.setHint("Enter Tittle");


       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, s);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/





        /*sItems = (Spinner) findViewById(R.id.mySpinner);
        sItems.setAdapter(adapter);
        sItems.setOnItemSelectedListener(this);*/

        Button saveAllData = (Button) findViewById(R.id.saveAllData);
        saveAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(description.getText().toString()) ) {

                    if(images.size()>0 && /*StateVO.CheckedData.size()>0 &&*/ !lable.equalsIgnoreCase("Select Preferences") /*&& !lableTrip.equalsIgnoreCase("Select Trip")*/) {
                        uploadCapturedImage();
                    /*if(images.size() > 0){
                        uploadImageToServer();
                    }*/
                    }
                    else {
                        Toast.makeText(Main2Activity.this, "Select All Values. Image and Preferences", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    description.setError("Enter Description");
                }


                //uploadUrl();
                //Toast.makeText(Main2Activity.this, getStringImage(capturedbitmap).substring(0,40), Toast.LENGTH_SHORT).show();

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                criteria = new Criteria();

                mprovider = locationManager.getBestProvider(criteria, false);

                /*if (mprovider != null && !mprovider.equals("")) {
                    if (ActivityCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    location = locationManager.getLastKnownLocation(mprovider);
                    locationManager.requestLocationUpdates(mprovider,12000,7,Main2Activity.this);

                    if (location != null)
                        onLocationChanged(location);
                    else
                        Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
                }*/



                /*if(TextUtils.isEmpty(password)){
                    description.setError("Enter Object Description");
                }*/


                    /*password = description.getText().toString();

                    descriptionInfo = new DescriptionInfo();
                    descriptionInfo.descriptionTitle = password;
                    //descriptionInfo.identity = DescriptionInfoData.identity;
                    descriptionInfo.save();


                    addLocaton = new AddLocaton();
                   //addLocaton.LocationName = password;
                    addLocaton.Image = StringPhoto;
                    addLocaton.identityimg = DescriptionInfoData.identity;
                    //addLocaton.LocationLat = ImageInfo.lat;
                    //addLocaton.studyLevel = lable;
                    addLocaton.save();*/


            }
        });

    }


    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }


   /* public void selectVideo(View view) {

        Intent intent;
        intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("video*//*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQ_CODE_PICK_VIDEOFILE);
        //selectVideo.setVisibility(View.INVISIBLE);
    }*/


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
         /*lable = adapterView.getSelectedItemId();

        lable = adapterView.get(i).getId();*/

        sItems = (Spinner)adapterView;
        //sItemstrip = (Spinner)adapterView;
        if(sItems.getId() == R.id.mySpinner){
            TextView selectedText = (TextView) findViewById(R.id.selectPreferenceText);
            lable = adapterView.getItemAtPosition(i).toString();
            selectedText.setText(lable);
        }

       /* if(sItems.getId() == R.id.mySpinnertrip) {
            TextView selectedTextTrip = (TextView) findViewById(R.id.selecttripText);
            lableTrip = adapterView.getItemAtPosition(i).toString();
            selectedTextTrip.setText(lableTrip);
        }*/
        count = i;
        /*CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
        String cvalue = String.valueOf(checkBox.isChecked());
        allList.add(cvalue);*/

        //Toast.makeText(this, "you selected: " + lable, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

            Toast.makeText(Main2Activity.this, "Please Add One Study Level Atleast", Toast.LENGTH_SHORT).show();


    }


    /*private void uploadVideo(*//*final String response*//*) {
        class UploadVideo extends AsyncTask<Void, Void, String> {

            ProgressDialog uploading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                uploading = ProgressDialog.show(Main2Activity.this, "Uploading File", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploading.dismiss();
                uploading.setCancelable(false);
                Toast.makeText(Main2Activity.this, "Successfully Uploaded.", Toast.LENGTH_SHORT).show();
                *//*textViewResponse.setText(Html.fromHtml("<b>Uploaded at <a href='" + s + "'>" + s + "</a></b>"));
                textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());*//*
            }

            @Override
            protected String doInBackground(Void... params) {
                Upload u = new Upload();
                String msg = u.uploadVideo(selectedPath,Object_id_video);
                return msg;
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }*/


    private void uploadImageToServer(final String response){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        int i;
                        for (i = 0; i < images.size() ; i++) {


                            //Adding parameters
                            AddLocaton addLocaton = new AddLocaton();
                            addLocaton.Image = images.get(i);
                            addLocaton.ObjectIdentity = response;
                            addLocaton.save();

                        }

                        startActivity(new Intent(Main2Activity.this,FirstMain.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        loading.dismiss();
                        Toast.makeText(Main2Activity.this, "Object Add Successfully.", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
//                      Toast
                        Toast.makeText(Main2Activity.this,"Add Image.", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                //String image = getStringImage(bitmap);  //getStringImage(bitmap);


                //String catagory = sItems.getSelectedItem().toString();
                //Getting Image Name
                //String name = editTextName.getText().toString().trim();
                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();
                int i;
                for (i = 0; i < images.size()  ; i++) {

                    //Adding parameters
                    params.put(KEY_IMAGE+i, images.get(i));

                }
                    //params.put(KEY_CATAGORY, catagory);

                    //returning parameters
               // params.put(KEY_CAPTURED_IMAGE,image);
                params.put("size", String.valueOf(i));
                params.put("id", response);
                    return params;

            }
        };

        int socketTimeout = 300000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    private void uploadCapturedImage() {



        //Showing the progress dialog
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setTitle("Uploading...");
        pDialog.setMessage("Uploading Image");
        pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_CAPTURED_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        object_id = response;
                       /* Object_id_link = response;
                        Object_id_level = response;
                        Object_id_video = response;
*/

                        DescriptionInfo descriptionInfo = new DescriptionInfo();

                        int catagory = (int) sItems.getSelectedItemId();
                        double latitude = ImageInfo.lat;
                        double longitude = ImageInfo.lng;

                        descriptionInfo.identity = response;
                        descriptionInfo.descriptionTitle = description.getText().toString();
                        descriptionInfo.preference = String.valueOf(catagory);
                        descriptionInfo.coordinates = String.valueOf(latitude)+","+String.valueOf(longitude);
                        descriptionInfo.save();


                        pDialog.dismiss();
                        //Showing toast message of the response
                        //Toast.makeText(Main2Activity.this, response , Toast.LENGTH_LONG).show();
                        if(images.size() > 0){
                            uploadImageToServer(response);
                        }

                        /*if(StateVO.CheckedData.size() > 0){
                           // Toast.makeText(Main2Activity.this, links.toString(), Toast.LENGTH_SHORT).show();
                            saveStudyLevel(response);
                        }*/
                        savePrefrences(response);
                        saveTripes(response);





                       /* int i = linearLink.getChildCount();
                        //Toast.makeText(Main2Activity.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                        for(int j = 0 ; j < i ; j++){
                            LinearLayout layout2 = (LinearLayout) linearLink.getChildAt(j);
                            EditText editText = (EditText) layout2.getChildAt(0);
                            links.add(editText.getText().toString());



                        }
                        if(links.size() > 0){
                            Toast.makeText(Main2Activity.this, links.toString(), Toast.LENGTH_SHORT).show();
                            uploadUrl(response);
                        }
                        if(allList.size() > 0){
                            Toast.makeText(Main2Activity.this, links.toString(), Toast.LENGTH_SHORT).show();
                            saveStudyLevel(response);
                        }
                       *//* if(isVideo==true){
                            uploadVideo();
                        }*/

                    }



                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Main2Activity.this ,"No Network", Toast.LENGTH_LONG ).show();
                        pDialog.hide();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                //Getting Captured Image
                //String sendImage = getStringImage(capturedbitmap);
                //Getting Study Level

                //Getting Image Name
                double latitude = ImageInfo.lat;
                double longitude = ImageInfo.lng;
                String coordinates = String.valueOf(latitude)+","+String.valueOf(longitude);
                String description1 = description.getText().toString();
                int catagory = (int) sItems.getSelectedItemId() - 1;
                //int catagorytrip = (int) sItemstrip.getSelectedItemId() - 1;

                    map.put("pref",prefId.get(catagory));
                   // map.put("trip",tripId.get(catagorytrip));
                    map.put(KEY_DESCRIPTION, description1);
                    map.put(KEY_COORDINATES, coordinates);
                    map.put("userId", PreferenceManager.getDefaultSharedPreferences(Main2Activity.this).getString("userId",""));



                //Getting Links
                //String link = adddLink.getText().toString().trim();
                //Getting Latitude and Longitude

                //map.put(KEY_USERNAME,username);

               // map.put(KEY_CATAGORY, catagory);

                //map.put(KEY_LINK, link);

                //map.put(KEY_LNG, String.valueOf(longitude));


                return map;
            }

        };

        int socketTimeout = 300000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

   /* private void uploadUrl(final String responselink) {



        //Showing the progress dialog
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setTitle("Uploading...");
        pDialog.setMessage("Uploading Image");
        pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        pDialog.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(Main2Activity.this, response , Toast.LENGTH_LONG).show();
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Main2Activity.this ,error.toString(), Toast.LENGTH_LONG ).show();
                        pDialog.hide();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();


                int i;
                *//*Toast.makeText(Main2Activity.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                for(int j = 0 ; j < i ; j++){
                    EditText editText = (EditText) linearLink1.getChildAt(j);
                    links.add(editText.getText().toString());

                }*//*
                for (i = 0; i < links.size() ; i++) {
                    //String link = adddLink.getText().toString().trim();
                    map.put(KEY_URL+i, links.get(i));
                }
                map.put("size", String.valueOf(i));
                map.put("id", responselink);

                return map;
            }

        };

        int socketTimeout = 300000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
*/

    /*private void getStudyLevel() {



        //Showing the progress dialog
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setTitle("Downloading...");
        pDialog.setMessage("Loading Study Level");
        pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL_STUDYLEVEL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {



                        ArrayList<StateVO> listVOs = new ArrayList<>();

                        stateVO = new StateVO();
                        //stateVO.setTitle("Select Study Levels");
                        stateVO.setSelected(false);
                        listVOs.add(stateVO);

                        //spinner = (Spinner) findViewById(R.id.spinner);
                        allList = new ArrayList<>();
                        allList.add("Select Study Levels");
                        //pDialog.dismiss();
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = jsonArray.length() -1; i >= 0;i--){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                //String[] allList = (String[]) jsonObject1.get("level");

                                allList.add(jsonObject1.getString("level"));

                                //ischecked = false;

                                stateVO = new StateVO();
                                stateVO.setTitle(jsonObject1.getString("level"));
                                stateVO.id = jsonObject1.getString("id");
                                stateVO.setSelected(false);
                                listVOs.add(stateVO);

                            }

                            *//*for (int j = 0; j < allList.size(); j++) {
                                stateVO = new StateVO();
                                stateVO.setTitle(allList.get(j));
                                stateVO.setSelected(false);
                                listVOs.add(stateVO);



                            }*//*

                            *//*ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main2Activity.this, android.R.layout.simple_spinner_item, allList);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            sItems = (Spinner) findViewById(R.id.mySpinner);
                            sItems.setAdapter(adapter);
                            sItems.setOnItemSelectedListener(Main2Activity.this);*//*

                            MyAdapter myAdapter = new MyAdapter(Main2Activity.this, 0, listVOs);
                            spinner.setAdapter(myAdapter);
                            spinner.setOnItemSelectedListener(Main2Activity.this);

                            pDialog.dismiss();
                        }catch (JSONException e){
                            pDialog.dismiss();
                            // Toast.makeText(LogIn.this,"Json Error",Toast.LENGTH_SHORT).show();

                            Toast.makeText(Main2Activity.this,"Server Error.",Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }


                        //Showing toast message of the response
                       // Toast.makeText(Main2Activity.this, response , Toast.LENGTH_LONG).show();
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(Main2Activity.this ,error.toString(), Toast.LENGTH_LONG ).show();
                        pDialog.hide();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                *//*String catagory = spinner.getSelectedItem().toString();

                int i;
                for (i = 0; i < allList.size() ; i++) {
                    //String link = adddLink.getText().toString().trim();
                    map.put(KEY_LEVEL+i, allList.get(i));
                }
                map.put("size", String.valueOf(i));
                map.put("id", responselevel);

                //map.put(KEY_LEVEL, catagory);*//*

                return map;
            }

        };

        int socketTimeout = 300000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/


    private void saveStudyLevel(final String responselevel) {



        //Showing the progress dialog
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setTitle("Uploading...");
        pDialog.setMessage("Uploading Study Level");
        pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL_SAVESTUDYLEVEL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //startActivity(new Intent(Main2Activity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        pDialog.dismiss();
                        //Toast.makeText(Main2Activity.this, "Object Add Successfully.", Toast.LENGTH_SHORT).show();
                        //Showing toast message of the response
                        // Toast.makeText(Main2Activity.this, response , Toast.LENGTH_LONG).show();
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(Main2Activity.this ,error.toString(), Toast.LENGTH_LONG ).show();
                        pDialog.hide();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                //allList = spinner.getSelectedItem().toString();
               // CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
                //String cvalue = String.valueOf(checkBox.isChecked());


                //ischecked = true;
                int i;

                    for (i = 0; i < StateVO.CheckedData.size() ; i++) {
                        //String link = adddLink.getText().toString().trim();
                        map.put(KEY_LEVEL+i, StateVO.CheckedData.get(i));
                    }
                    map.put("size", String.valueOf(i));
                    map.put("id", responselevel);


                //map.put(KEY_LEVEL, catagory);

                return map;
            }

        };

        int socketTimeout = 300000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getPrefrences() {



        //Showing the progress dialog
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setTitle("Downloading...");
        pDialog.setMessage("Loading Preferences");
        pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL_GETPREF,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        allList1 = new ArrayList<>();
                        allList1.add("Select Preferences");
                        prefId.add("0");

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = jsonArray.length() -1; i >= 0;i--){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                //String[] allList = (String[]) jsonObject1.get("level");


                                allList1.add(jsonObject1.getString("pref"));
                                prefId.add(jsonObject1.getString("id"));

                                //ischecked = false;


                            }
                            sItems = (Spinner) findViewById(R.id.mySpinner);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main2Activity.this,android.R.layout.simple_spinner_item, allList1);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                            //sItems.setPrompt("Select Preferences");
                           // sItems.setGravity(Gravity.BOTTOM);
                            sItems.setAdapter(adapter);

                            sItems.setOnItemSelectedListener(Main2Activity.this);





                            pDialog.dismiss();
                        }catch (JSONException e){
                            pDialog.dismiss();
                            // Toast.makeText(LogIn.this,"Json Error",Toast.LENGTH_SHORT).show();

                            Toast.makeText(Main2Activity.this,"Server Error.",Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }



                        //Showing toast message of the response
                        // Toast.makeText(Main2Activity.this, response , Toast.LENGTH_LONG).show();
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Main2Activity.this ,"No Network", Toast.LENGTH_LONG ).show();
                        pDialog.hide();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                //allList = spinner.getSelectedItem().toString();
                // CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
                //String cvalue = String.valueOf(checkBox.isChecked());


                //ischecked = true;
                /*int i;

                for (i = 0; i < StateVO.CheckedData.size() ; i++) {
                    //String link = adddLink.getText().toString().trim();
                    map.put(KEY_LEVEL+i, StateVO.CheckedData.get(i));
                }
                map.put("size", String.valueOf(i));
                map.put("id", responselevel);
*/

                //map.put(KEY_LEVEL, catagory);

                return map;
            }

        };

        int socketTimeout = 300000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void getTrip() {



        //Showing the progress dialog
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setTitle("Downloading...");
        pDialog.setMessage("Loading Trips");
        pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://mlearningfieldtrips.com/Api/getTripes",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        /*allListtrip = new ArrayList<>();
                        allListtrip.add("Select Trip");
                        tripId.add("0");
*/
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = jsonArray.length() -1; i >= 0;i--){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                //String[] allList = (String[]) jsonObject1.get("level");


                                //allListtrip.add(jsonObject1.getString("trip"));
                                //tripId.add(jsonObject1.getString("id"));

                                //ischecked = false;


                            }
                           // sItemstrip = (Spinner) findViewById(R.id.mySpinnertrip);
                            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main2Activity.this,android.R.layout.simple_spinner_item, allListtrip);

                            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                            //sItems.setPrompt("Select Preferences");
                            // sItems.setGravity(Gravity.BOTTOM);
                           // sItemstrip.setAdapter(adapter);

                            //sItemstrip.setOnItemSelectedListener(Main2Activity.this);





                            pDialog.dismiss();
                        }catch (JSONException e){
                            pDialog.dismiss();
                            // Toast.makeText(LogIn.this,"Json Error",Toast.LENGTH_SHORT).show();

                            Toast.makeText(Main2Activity.this,"Server Error.",Toast.LENGTH_SHORT).show();

                            e.printStackTrace();
                        }



                        //Showing toast message of the response
                        // Toast.makeText(Main2Activity.this, response , Toast.LENGTH_LONG).show();
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Main2Activity.this ,"No Network", Toast.LENGTH_LONG ).show();
                        pDialog.hide();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                //allList = spinner.getSelectedItem().toString();
                // CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
                //String cvalue = String.valueOf(checkBox.isChecked());


                //ischecked = true;
                /*int i;

                for (i = 0; i < StateVO.CheckedData.size() ; i++) {
                    //String link = adddLink.getText().toString().trim();
                    map.put(KEY_LEVEL+i, StateVO.CheckedData.get(i));
                }
                map.put("size", String.valueOf(i));
                map.put("id", responselevel);
*/

                //map.put(KEY_LEVEL, catagory);

                return map;
            }

        };

        int socketTimeout = 300000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void savePrefrences(final String responselevel) {



        //Showing the progress dialog
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setTitle("Uploading...");
        pDialog.setMessage("Uploading Image");
        pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL_SAVEPREF,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        /*allList1 = new ArrayList<>();
                        allList1.add("Select Preferences");

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = jsonArray.length() -1; i >= 0;i--){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                //String[] allList = (String[]) jsonObject1.get("level");


                                allList1.add(jsonObject1.getString("pref"));

                                //ischecked = false;


                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main2Activity.this,android.R.layout.simple_spinner_item, allList1);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            sItems = (Spinner) findViewById(R.id.mySpinner);
                            sItems.setAdapter(adapter);
                            sItems.setOnItemSelectedListener(Main2Activity.this);

*/

                            pDialog.dismiss();

                            // Toast.makeText(LogIn.this,"Json Error",Toast.LENGTH_SHORT).show();





                        //Showing toast message of the response
                        // Toast.makeText(Main2Activity.this, response , Toast.LENGTH_LONG).show();
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Main2Activity.this ,"No Network", Toast.LENGTH_LONG ).show();
                        pDialog.hide();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                int catagory = (int) sItems.getSelectedItemId();

                allList1.indexOf(lable);
                map.put("id",responselevel);
                map.put("pref",prefId.get(allList1.indexOf(lable)));

                //allList = spinner.getSelectedItem().toString();
                // CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
                //String cvalue = String.valueOf(checkBox.isChecked());


                //ischecked = true;
                /*int i;

                for (i = 0; i < StateVO.CheckedData.size() ; i++) {
                    //String link = adddLink.getText().toString().trim();
                    map.put(KEY_LEVEL+i, StateVO.CheckedData.get(i));
                }
                map.put("size", String.valueOf(i));
                map.put("id", responselevel);
*/

                //map.put(KEY_LEVEL, catagory);

                return map;
            }

        };

        int socketTimeout = 300000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void saveTripes(final String responsetrips) {



        //Showing the progress dialog
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setTitle("Uploading...");
        pDialog.setMessage("Uploading Trip");
        pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://mlearningfieldtrips.com/Api/saveTrip",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        pDialog.dismiss();

                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Main2Activity.this ,"No Network", Toast.LENGTH_LONG ).show();
                        pDialog.hide();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();

                //int catagory = (int) sItems.getSelectedItemId();

                final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Main2Activity.this);
                preferences.getString("selectedTrip","");

                //allListtrip.indexOf(lableTrip);
                map.put("id",responsetrips);
                map.put("trip",preferences.getString("selectedTrip",""));

                return map;
            }

        };

        int socketTimeout = 300000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] byteArray = bytes.toByteArray();
        inImage = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);

    }



    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        // Raw height and width of image
        int inSampleSize = 1;
        final int height = options.outHeight;
        final int width = options.outWidth;


        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void getAddress(Context context, double LATITUDE, double LONGITUDE) {

        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {



                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();// Only if available else return NULL



               /* Log.d(TAG, "getAddress:  address" + address);
                Log.d(TAG, "getAddress:  city" + city);
                Log.d(TAG, "getAddress:  state" + state);
                Log.d(TAG, "getAddress:  postalCode" + postalCode);
                Log.d(TAG, "getAddress:  knownName" + knownName);*/

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    @Override
    public void onBackPressed() {
        Intent mainAgain = new Intent(this,FirstMain.class);
        mainAgain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainAgain);
    }
}

