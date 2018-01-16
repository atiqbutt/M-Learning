package com.softvilla.m_learning;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOfMarkedPlaces extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapterMarkedPlaces adapter;
    List<DescriptionInfo> data;
    public static final String GET_LIST_URL = "http://mlearningfieldtrips.com/Api/getObjectList";
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_marked_places);
        data = new ArrayList<DescriptionInfo>();
        recyclerView = (RecyclerView) findViewById(R.id.markedListRecycler);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ListOfMarkedPlaces.this);
        sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(ListOfMarkedPlaces.this);

        /*adapter = new RecyclerAdapterMarkedPlaces(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
       // recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<DescriptionInfo> locatons = DescriptionInfo.listAll(DescriptionInfo.class);*/
       // Toast.makeText(this, String.valueOf(locatons.size()), Toast.LENGTH_SHORT).show();


            GetMarkedObjectList();


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
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ListOfMarkedPlaces.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("isLogin","0");
            editor.apply();
            startActivity(new Intent(ListOfMarkedPlaces.this,LogIn.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void GetMarkedObjectList() {


        if(true){

            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
            pDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_LIST_URL,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {


                            List<AddLocaton> child = AddLocaton.listAll(AddLocaton.class);
                            List<DescriptionInfo> info = DescriptionInfo.listAll(DescriptionInfo.class);
                            try{
                                JSONArray jsonArray = new JSONArray(response);
                                if(jsonArray.length()>0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        DescriptionInfo desc = new DescriptionInfo();
                                        //AddLocaton obj = new AddLocaton();
                                        desc.coordinates = jsonObject1.getString("image");
                                        desc.descriptionTitle = jsonObject1.getString("title");
                                        desc.identity = jsonObject1.getString("id");
                                        data.add(desc);
                                        //desc.identity = jsonObject1.getString("id"+i);


                                    }


                                    recyclerView = (RecyclerView) findViewById(R.id.markedListRecycler);


                                    adapter = new RecyclerAdapterMarkedPlaces(ListOfMarkedPlaces.this, data);
                                    recyclerView.setAdapter(adapter);
                                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(ListOfMarkedPlaces.this);
                                    mLayoutManager.setReverseLayout(true);
                                    mLayoutManager.setStackFromEnd(true);
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    pDialog.dismiss();
                                   // Toast.makeText(ListOfMarkedPlaces.this, sharedPreferences.getString("userId","")+sharedPreferences1.getString("selectedTrip",""), Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(ListOfMarkedPlaces.this, "No Object Added Against Selected Trips", Toast.LENGTH_SHORT).show();
                                    pDialog.dismiss();
                                }
                            }catch (JSONException e){
                                pDialog.dismiss();
                                // Toast.makeText(LogIn.this,"Json Error",Toast.LENGTH_SHORT).show();

                                //Toast.makeText(LogIn.this,e.toString(),Toast.LENGTH_SHORT).show();


                                e.printStackTrace();
                            }

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ListOfMarkedPlaces.this,"Connection Failed", Toast.LENGTH_LONG ).show();
                            pDialog.hide();
                        }
                    }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    map.put("id",sharedPreferences.getString("userId",""));
                    map.put("trip_id",sharedPreferences1.getString("selectedTrip",""));

                    return map;
                }

            };

            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }



    }

   /* ArrayList<DescriptionInfoData> getList(){
        ArrayList<DescriptionInfoData> data = new ArrayList<DescriptionInfoData>();
        List<DescriptionInfo> locatons = AddLocaton.listAll(DescriptionInfo.class);

        for (DescriptionInfo obj : locatons){
            DescriptionInfoData obj2 = new DescriptionInfoData();
            obj2.descriptionTitle = obj.descriptionTitle;
            obj2.identity = obj.identity;
            *//*obj2.LocationLat = obj.LocationLat;
            obj2.LocationLng = obj.LocationLng;
            obj2.Image = obj.Image;
            obj2.studyLevel = obj.studyLevel;*//*
            data.add(obj2);
        }

        return data;
    }*/
}
