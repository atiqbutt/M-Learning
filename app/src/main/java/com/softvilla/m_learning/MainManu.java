package com.softvilla.m_learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainManu extends AppCompatActivity implements Recycler_View_Adapter.OnCardClickListner {

    public static final String LOGIN_URL = "https://malikparactice.000webhostapp.com/ImageJsonData.php";
    List<ImageInfo> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_manu);

        data = new ArrayList<ImageInfo>();

        /*RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        Recycler_View_Adapter adapter = new Recycler_View_Adapter(data, this);
        recyclerView.setAdapter(adapter);
        adapter.setOnCardClickListner(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        ShowData();*/

    }

    @Override
    public void OnCardClicked(View view, int position) {

    }


    /*private void ShowData() {


            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
            pDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {


                            try{
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i = 0; i<jsonArray.length();i++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    ImageInfo obj = new ImageInfo();
                                    obj.id = jsonObject1.getString("id");
                                    obj.photo = jsonObject1.getString("photo");
                                    data.add(obj);

                                    //Toast.makeText(MainManu.this, ob, Toast.LENGTH_SHORT).show();

                                }
                                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
                                Recycler_View_Adapter adapter = new Recycler_View_Adapter(data, MainManu.this);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(MainManu.this));
                                pDialog.dismiss();
                            }catch (JSONException e){
                                pDialog.dismiss();
                                // Toast.makeText(LogIn.this,"Json Error",Toast.LENGTH_SHORT).show();

                                Toast.makeText(MainManu.this,e.toString(),Toast.LENGTH_SHORT).show();

                                e.printStackTrace();
                            }
                            Toast.makeText(MainManu.this, String.valueOf(data.size()), Toast.LENGTH_SHORT).show();


                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainManu.this,"Connection Failed", Toast.LENGTH_LONG ).show();
                            pDialog.hide();
                        }
                    }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    //map.put("id","");
                    return map;
                }

            };

            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }*/



}




