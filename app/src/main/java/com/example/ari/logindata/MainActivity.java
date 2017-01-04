package com.example.ari.logindata;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Context context;
    private Button button_Login;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        pDialog = new ProgressDialog(context);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        button_Login = (Button) findViewById(R.id.buttonLogin);

        button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

        private void login() {
            //Getting values from edit texts
            final String usrname = editTextEmail.getText().toString().trim();
            final String password = editTextPassword.getText().toString().trim();
            pDialog.setMessage("Login Process...");
            showDialog();
            //Creating a string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppVar.LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            //If we are getting success from server
                            if (response.contains(AppVar.LOGIN_SUCCESS)) {
                                hideDialog();
                                gotoCourseActivity();

                            } else {
                                hideDialog();
                                //Displaying an error message on toast
                                Toast.makeText(context, "Invalid username or password", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want
                            hideDialog();
                            Toast.makeText(context, "The server unreachable", Toast.LENGTH_LONG).show();

                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request
                    params.put(AppVar.KEY_EMAIL, usrname);
                    params.put(AppVar.KEY_PASSWORD, password);

                    //returning parameter
                    return params;
                }
            };

            //Adding the string request to the queue
            Volley.newRequestQueue(this).add(stringRequest);

        }

        private void gotoCourseActivity() {
            Intent intent = new Intent(context, Hal2.class);
            startActivity(intent);
            finish();
        }

        private void showDialog() {
            if (!pDialog.isShowing())
                pDialog.show();
        }

        private void hideDialog() {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

