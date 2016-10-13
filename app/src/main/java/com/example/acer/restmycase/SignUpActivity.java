package com.example.acer.restmycase;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.http.HttpResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    JSONParser jsonParser = new JSONParser();
    private static String url_create_product = "http://localhost/restmycase/create_product.php";
    ProgressDialog pDialog;
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private String name;
    private String email;
    private String password;
//    @InjectView(R.id.input_name) EditText _nameText;
//     @InjectView(R.id.input_email1) EditText _emailText;
//     @InjectView(R.id.input_password1) EditText _passwordText;
//     @InjectView(R.id.btn_signup) Button _signupButton;
//     @InjectView(R.id.link_login) TextView _loginLink;
   EditText _nameText;
    EditText _emailText;
    EditText _passwordText;
    TextView _loginLink;
    Button _signupButton;

     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
       //  ButterKnife.inject(this);
         _nameText=(EditText)findViewById(R.id.input_name);
          _emailText=(EditText)findViewById(R.id.input_email1);
         _passwordText=(EditText)findViewById(R.id.input_password1);
          _loginLink=(TextView)findViewById(R.id.link_login) ;
          _signupButton=(Button)findViewById(R.id.btn_signup);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

       final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this, R.style.MyAlertDialogStyle);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

         name = _nameText.getText().toString();
         email = _emailText.getText().toString();
         password = _passwordText.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 1000);
    }


    public void onSignupSuccess() {
         new Signup().execute();
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {

        Toast.makeText(getBaseContext(), "SignUp failed.Please verify credentials. ", Toast.LENGTH_SHORT).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        password = _passwordText.getText().toString();
       name = _nameText.getText().toString();
       email = _emailText.getText().toString();


        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    /**
     * Background Async Task to Create new product
     * */
    class Signup extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(SignUpActivity.this);
//            pDialog.setMessage("Signing up ..");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.setButton("OK",new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog,int which)
//                {
//                    dialog.cancel();
//                }
//            });
//            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("emailid", email));
            params.add(new BasicNameValuePair("username", name));
            params.add(new BasicNameValuePair("pass", password));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,"POST", params);

            // check log cat fro response
         //   Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
           // pDialog.setMessage("Done");
          //  pDialog.dismiss();
        }

    }
}
