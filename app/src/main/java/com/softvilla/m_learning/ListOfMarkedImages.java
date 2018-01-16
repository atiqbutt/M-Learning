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

public class ListOfMarkedImages extends AppCompatActivity {

    RecyclerView recyclerView;
    Recycler_View_Adapter adapter;
    ArrayList<AddLocaton> data;
    public static final String GET_OBJECTRELATEDIMAGES_URL = "http://mlearningfieldtrips.com/Api/getObjectImage";
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_marked_images);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        data = new ArrayList<AddLocaton>();
        id = "";
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("id");
        }

       // Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

       /* List<AddLocaton> addLocatonList = AddLocaton.listAll(AddLocaton.class);
        for(AddLocaton obj : addLocatonList){
            if(obj.ObjectIdentity.equalsIgnoreCase(id)){
                data.add(obj.Image);
            }
        }

        //Toast.makeText(this, String.valueOf(data.size()), Toast.LENGTH_SHORT).show();
        recyclerView = (RecyclerView) findViewById(R.id.markedImagestRecycler);

        adapter = new Recycler_View_Adapter(this,data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<DescriptionInfo> locatons = DescriptionInfo.listAll(DescriptionInfo.class);*/
        //Toast.makeText(this, String.valueOf(locatons.size()), Toast.LENGTH_SHORT).show();

        GetMarkedObjectRelatedImages();

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
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ListOfMarkedImages.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("isLogin","0");
            editor.apply();
            startActivity(new Intent(ListOfMarkedImages.this,LogIn.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void GetMarkedObjectRelatedImages() {


        if(true){

            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
            pDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_OBJECTRELATEDIMAGES_URL,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {


                            List<AddLocaton> child = AddLocaton.listAll(AddLocaton.class);
                            List<DescriptionInfo> info = DescriptionInfo.listAll(DescriptionInfo.class);
                            try{
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i = 0; i<jsonArray.length();i++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    AddLocaton obj = new AddLocaton();
                                    obj.Image = jsonObject1.getString("image");
                                    data.add(obj);
                                    //desc.identity = jsonObject1.getString("id"+i);




                                }
                                recyclerView = (RecyclerView) findViewById(R.id.markedImagestRecycler);


                                adapter = new Recycler_View_Adapter(ListOfMarkedImages.this,data);
                                recyclerView.setAdapter(adapter);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(ListOfMarkedImages.this);
                                mLayoutManager.setReverseLayout(true);
                                mLayoutManager.setStackFromEnd(true);
                                recyclerView.setLayoutManager(mLayoutManager);
                                pDialog.dismiss();
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
                            Toast.makeText(ListOfMarkedImages.this,"Connection Failed", Toast.LENGTH_LONG ).show();
                            pDialog.hide();
                        }
                    }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ListOfMarkedImages.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    map.put("id",id);
                   // map.put("trip_id",sharedPreferences.getString("trip_id",""));
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
}
