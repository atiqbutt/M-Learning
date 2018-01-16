package com.softvilla.m_learning;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class LogIn extends AppCompatActivity {

    public static final String LOGIN_URL = "http://mlearningfieldtrips.com/Api/login";
    private EditText userName,password;
    private Button logIn;
    private TextView teacher,student;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        userName = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        logIn = (Button) findViewById(R.id.loginbtn);
//        teacher = (TextView) findViewById(R.id.teacher);
//        student = (TextView) findViewById(R.id.student);
    }

    private void userLogin() {
        final String username = userName.getText().toString();
        final String Password = password.getText().toString();



        boolean isEmptyName=false;
        if(TextUtils.isEmpty(userName.getText().toString())) {
            userName.setError("Enter Your Name");
            isEmptyName = true;
        }
        boolean isEmptyPass=false;
        if(TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Enter Password");
            isEmptyPass = true;
        }

        if(!isEmptyName && !isEmptyPass){

            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Signing in...");
            pDialog.setCancelable(false);
//                            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pDialog.setIndeterminate(true);
            pDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String msg = jsonObject.getString("msg");
                                if(msg.equalsIgnoreCase("false")){
                                    Toast.makeText(LogIn.this, "Wrong Username Or Password", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    String roleId = jsonObject.getString("role_id");
                                    if(!roleId.equalsIgnoreCase("2")){
                                        Toast.makeText(LogIn.this, "Wrong Username Or Password", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LogIn.this);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("isLogin","1");
                                        editor.putString("userId",jsonObject.getString("id"));
                                        //editor.putString("role_id",jsonObject.getString("role_id"));
                                        editor.apply();
                                        startActivity(new Intent(LogIn.this,FirstMain.class));
                                        finish();
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            pDialog.dismiss();

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(LogIn.this,"Server Error Or Network Problem.", Toast.LENGTH_LONG ).show();
                            pDialog.hide();
                        }
                    }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("username",username);
                    map.put("password",MD5(Password));
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


    public void Map(View view) {

        userLogin();
        /*Intent mainActvity = new Intent(this,MainActivity.class);
        startActivity(mainActvity);*/
    }

    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
