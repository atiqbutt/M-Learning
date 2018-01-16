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
import java.util.Map;

public class Trips extends AppCompatActivity {

   // ArrayList<String> allListtrip;
    ArrayList<String> tripId;
    RecyclerView recyclerView;
    Recycler_View_Adapter_Trip adapter;
    ArrayList<TripModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_trips);
        tripId = new ArrayList<String>();
        data = new ArrayList<TripModel>();

        getTrip();
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
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Trips.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("isLogin","0");
            editor.apply();
            startActivity(new Intent(Trips.this,LogIn.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Trips.this);
                        String tripId = preferences.getString("selectedTrip","");
                        int index = -1;
                        //allListtrip = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i<jsonArray.length();i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                TripModel obj = new TripModel();
                                obj.name = jsonObject1.getString("trip");
                                obj.tripid = jsonObject1.getString("id");
                                /*SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(Trips.this);
                                SharedPreferences.Editor editor = preferences1.edit();
                                editor.putString("trip_id",obj.tripid);
                                editor.apply();*/


                                if(tripId.equalsIgnoreCase(obj.tripid)){
                                    index = i;
                                    obj.isChecked = true;
                                }
                                else {
                                    obj.isChecked = false;
                                }
                                data.add(obj);
                                //Toast.makeText(Trips.this, obj.name+obj.tripid, Toast.LENGTH_SHORT).show();

                            }

                        /*allListtrip.add("Select Trip");
                        tripId.add("0");


*/
                        /*try{
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = jsonArray.length() -1; i >= 0;i--){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                //String[] allList = (String[]) jsonObject1.get("level");


                                allListtrip.add(jsonObject1.getString("trip"));
                                tripId.add(jsonObject1.getString("id"));

                                //ischecked = false;


                            }*/
                            // sItemstrip = (Spinner) findViewById(R.id.mySpinnertrip);
                            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main2Activity.this,android.R.layout.simple_spinner_item, allListtrip);

                            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                            //sItems.setPrompt("Select Preferences");
                            // sItems.setGravity(Gravity.BOTTOM);
                            // sItemstrip.setAdapter(adapter);

                            //sItemstrip.setOnItemSelectedListener(Main2Activity.this);



                            recyclerView = (RecyclerView) findViewById(R.id.tripsrecycler);


                            adapter = new Recycler_View_Adapter_Trip(Trips.this,data, index);
                            recyclerView.setAdapter(adapter);
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(Trips.this);
                            mLayoutManager.setReverseLayout(true);
                            mLayoutManager.setStackFromEnd(true);
                            recyclerView.setLayoutManager(mLayoutManager);

                            pDialog.dismiss();
                        }catch (JSONException e){

                            // Toast.makeText(LogIn.this,"Json Error",Toast.LENGTH_SHORT).show();

                            Toast.makeText(Trips.this,e.toString()+"Server Error.",Toast.LENGTH_SHORT).show();
                            pDialog.dismiss();
                            e.printStackTrace();
                        }



                        //Showing toast message of the response
                        // Toast.makeText(Main2Activity.this, response , Toast.LENGTH_LONG).show();
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Trips.this ,"No Network", Toast.LENGTH_LONG ).show();
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
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Trips.this);
                preferences.getString("id","");
                map.put("id", preferences.getString("userId",""));

                return map;
            }

        };

        int socketTimeout = 300000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
