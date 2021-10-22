package com.example.ayush_gis;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    TextView username;
    TextView user_mob_num;
    TextView uprsac_division;
    EditText admin_name;
    EditText admin_pass;
    Button adm_lgn;
    Apiinterface apiinterface;
    TextView register_yourself, tv_forgot;
    CheckBox cb_remember;

    //    String usernamee ;
//    String passwordd ;
    private Context mContext = LoginActivity.this;
    String default_user_name = null;
    String default_password = null;
    String entered_user_name = " ";
    String  entered_password = " ";
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private long fileSize = 0;

    ////////////////////////////
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String user_Name_save = "nameKey";
    public static final String password_save = "phoneKey";
    public static final boolean true_false = false;
    public static final String Email = "emailKey";
    boolean unused = true;

//    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences();
//    Boolean statusLocked = prefs.edit().putBoolean("locked", true).commit();

    SharedPreferences sharedpreferences;

    ////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        admin_name =findViewById(R.id.admin_name);
        admin_pass = findViewById(R.id.admin_pass);
        adm_lgn = (Button) findViewById(R.id.adm_lgn);
        cb_remember = (CheckBox) findViewById(R.id.cb_remember);

        username = (TextView) findViewById(R.id.username);
        user_mob_num = (TextView) findViewById(R.id.user_mob_num);
        uprsac_division = (TextView) findViewById(R.id.uprsac_division);
        tv_forgot = (TextView) findViewById(R.id.tv_forgot_password);

        register_yourself = (TextView) findViewById(R.id.register_yourself);

//        textDummyHintUserName = (TextView) findViewById(R.id.text_dummy_hint_username);
//        textDummyHintPassword = (TextView) findViewById(R.id.text_dummy_hint_password);
        admin_name = (EditText) findViewById(R.id.admin_name);
        admin_pass = (EditText) findViewById(R.id.admin_pass);
//            usernamee = admin_name.getText().toString();
//            passwordd = admin_pass.getText().toString();

        apiinterface = ApiClient.getClient().create(Apiinterface.class);
        // Username


        // Password

        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String admin_mob = admin_name.getText().toString();

                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                intent.putExtra("mobile", admin_mob);
                startActivity(intent);
                progressBar = new ProgressDialog(view.getContext());
                progressBar.setCancelable(true);
                progressBar.setMessage("Please Wait...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                progressBar.show();
                //reset progress bar and filesize status
                progressBarStatus = 0;
                fileSize = 0;

            }
        });
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        cb_remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Log.d("mytag","checkbox is-----true----");
//                    Prefs.getPrefInstance().setValue(Login_Activity.this, Const.CHECKBOX_STATUS, "1");
//                    String userName =Prefs.getPrefInstance().getValue(context, Const.LOGIN_USERNAME, "");
//                    String password =Prefs.getPrefInstance().getValue(context, Const.LOGIN_PASSWORD, "");
//                    Log.d("mytag","userName and password id----"+userName +"         "+password);
//                    admin_name.setText(userName);
//                    admin_pass.setText(password);

//                    String n  = ed1.getText().toString();
                    String username  = admin_name.getText().toString();
                    String passsword  = admin_pass.getText().toString();

                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString(user_Name_save, username);
                    editor.putString(password_save, passsword);
                    editor.putBoolean(Email, true).apply();

//                    editor.putString(Email, e);
                    editor.commit();
                    Toast.makeText(LoginActivity.this,"Login Credential will be Saved for future",Toast.LENGTH_LONG).show();
                }else{
                    Log.d("mytag","checkbox is-----false----");
//                    Prefs.getPrefInstance().setValue(LoginActivity.this, Const.CHECKBOX_STATUS, "0");
                }
            }
        });

        if (sharedpreferences.getBoolean(Email,false) == true)
        {
            admin_name.setText(sharedpreferences.getString(user_Name_save,"nameKey"));
            admin_pass.setText(sharedpreferences.getString(password_save,"phoneKey"));

        }


//        editor.get
//  if((usernamee == null)){
//    Toast.makeText(Login_Activity.this, "Kindly enter USERNAME TO proceed", Toast.LENGTH_SHORT).show();
//   }
//
//   else if(passwordd == null){
//
//         Toast.makeText(Login_Activity.this, "Kindly enter PASSWORD TO proceed", Toast.LENGTH_SHORT).show();
//
//     }


//        adm_lgn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                if(!(admin_name.getText().toString() == null) && !(admin_pass.getText().toString() == null)) {
//
//                    submit_login(admin_name.getText().toString(), admin_pass.getText().toString());
//
//                }
//            }
//        });


//    else {
//        Toast.makeText(Login_Activity.this, "Kindly enter USERNAME/PASSWORD TO proceed", Toast.LENGTH_SHORT).show();
//
//    }

        adm_lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                entered_user_name = admin_name.getText().toString();
//                entered_password = admin_pass.getText().toString();

                if(admin_name.getText().toString().equals("admin") && admin_pass.getText().toString().equals("1234")) {
//                    Toast.makeText(getApplicationContext(), "Redirecting...",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
                    startActivity(intent);
//                    creating progress bar dialog
                    progressBar = new ProgressDialog(v.getContext());
                    progressBar.setCancelable(true);
                    progressBar.setMessage("Please Wait...");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.setProgress(0);
                    progressBar.setMax(100);
                    progressBar.show();
                    //reset progress bar and filesize status
                    progressBarStatus = 0;
                    fileSize = 0;


                }else {
                    Toast.makeText(getApplicationContext(), "WrongCredentials", Toast.LENGTH_SHORT).show();


//
////                if(default_user_name.matches(entered_user_name) && default_password.matches(entered_password))
//                if(admin_name.getText().toString().trim().length() == 0)
//
//                    Toast.makeText(LoginActivity.this, "Kindly enter USERNAME to proceed", Toast.LENGTH_SHORT).show();
//
//                else if  (admin_pass.getText().toString().trim().length() == 0){
//
//                    Toast.makeText(LoginActivity.this, "Kindly enter PASSWORD to proceed", Toast.LENGTH_SHORT).show();
//                }
//
//                //
//                //                    startActivity(new Intent(Login_Activity.this,MapsActivity.class));
//                else {
//                    String msg = "Invalid User Name/Password...!!";
//                    showSubmitDataDialog(msg);

//                    submit_login(entered_user_name, entered_password);
//
                }
            }
        });

        register_yourself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_registration_page();
                progressBar = new ProgressDialog(v.getContext());
                progressBar.setCancelable(true);
                progressBar.setMessage("Please Wait...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                progressBar.show();
                //reset progress bar and filesize status
                progressBarStatus = 0;
                fileSize = 0;
            }
        });

    }

//    public void submit_login(String admin_name , String admin_pass){                                        ////post_data_method_by_jai
//
//        Call<response_to_login> todoPostCall = apiinterface.login(admin_name,admin_pass);   //Response_pr_my_app=response type
//        todoPostCall.enqueue(new Callback<response_to_login>() {            //enqueue is a method from retrofi to call
//
//            @Override
//            public void onResponse(Call<response_to_login> call, Response<response_to_login> response)
//            {
//                //                      Log.e(TAG, "onResponse: "+ response.body() );
//
//
//                assert response.body() != null;
//                if (response.body().getStatus() == true){    // true or false boolean value from api will be parser
//
//                    for (int i = 0; i < response.body().getData().size(); i++) {
//
////                    String usernam = response.body().getData().get(i).get_username();
//                        username.setText(response.body().getData().get(i).getUsername());
//                        user_mob_num.setText(response.body().getData().get(i).getUserMobNum());
//                        uprsac_division.setText(response.body().getData().get(i).getUprsacDivision());
//                    }
//                    Intent i = new Intent(LoginActivity.this, MapsActivity.class);   ////opening another activity
//                    i.putExtra("username",username.getText().toString());
//                    i.putExtra("user_mob_num",user_mob_num.getText().toString());
//                    i.putExtra("uprsac_division",uprsac_division.getText().toString());
//                    startActivity(i);
//                    finish();///// now on pressing back button application will get closed
//
//                }
//
//                else {
//
//                    String msg = "Invalid User Name/Password...!!";
//                    showSubmitDataDialog(msg);
////                    Toast.makeText(Login_Activity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<response_to_login> call, Throwable t) {
//                //                Log.e(TAG, "onFailure: "  + t.getLocalizedMessage() );
//                Toast.makeText(LoginActivity.this,
//                        "SOMETHING WENT WRONG " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//
//            }
//        });
//    }

    private void showSubmitDataDialog(String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext, R.style.CustomDialogTheme);
        builder.setTitle(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(mContext,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    public  void open_registration_page(){

        Intent intent = new Intent(mContext,MobileActivity.class);
        startActivity(intent);

    }
}
