package com.example.ayush_gis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextView get_user_num, link_login_tv;
    TextView toggle1,toggle2;
    EditText get_password,confirmed_password;
    Button submit_details;
    private ProgressBar progressBar;
    Apiinterface apiinterface;
    private Context mContext = ForgotPasswordActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

//        get_user_num = findViewById(R.id.get_user_number_f);

        get_password = findViewById(R.id.get_password_f);
        confirmed_password = (EditText) findViewById(R.id.confirmed_password_f);
        link_login_tv=(TextView)findViewById(R.id.link_login_tv_f);
        toggle1=(TextView)findViewById(R.id.tv_toggle1);
        toggle2=(TextView)findViewById(R.id.tv_toggle2);

        link_login_tv=(TextView)findViewById(R.id.link_login_tv_f);
        submit_details = findViewById(R.id.submit_btn_f);
        apiinterface = ApiClient.getClient().create(Apiinterface.class);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_f);

//        Bundle bn = getIntent().getExtras();
//        String receivingdata = bn.getString("mobile");
//
//        get_user_num.setText(String.valueOf(receivingdata));
//        submit_number(receivingdata);

        link_login_tv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ForgotPasswordActivity.this, LoginActivity.class );
                startActivity( intent );
            }
        } );

        toggle1.setVisibility(View.GONE);
        get_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        toggle2.setVisibility(View.GONE);
        confirmed_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        get_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(get_password.getText().length()>0) {
                    toggle1.setVisibility(View.VISIBLE);
                } else {
                    toggle1.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        toggle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toggle1.getText() == "SHOW") {
                    toggle1.setText("HIDE");
                    get_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    get_password.setSelection(get_password.length());
                } else {
                    toggle1.setText("SHOW");
                    get_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    get_password.setSelection(get_password.length());
                }
            }
        });
        confirmed_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(confirmed_password.getText().length()>0) {
                    toggle2.setVisibility(View.VISIBLE);
                } else {
                    toggle2.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String password = get_password.getText().toString();
                String confirm_password = confirmed_password.getText().toString();
                if (!confirm_password.equals(password)) {
                    confirmed_password.setError("Password does not match");
                    confirmed_password.setTextColor(Color.parseColor("black"));

                }
                return;
            }
        });
        toggle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toggle2.getText() == "SHOW") {
                    toggle2.setText("HIDE");
                    confirmed_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    confirmed_password.setSelection(confirmed_password.length());
                } else {
                    toggle2.setText("SHOW");
                    confirmed_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirmed_password.setSelection(confirmed_password.length());
                }
            }
        });
        submit_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                if (TextUtils.isEmpty(get_password.getText().toString().trim())) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please Enter your Password...", Toast.LENGTH_SHORT).show();

                    return;
                }
                if (TextUtils.isEmpty(confirmed_password.getText().toString().trim())) {
                    Toast.makeText(ForgotPasswordActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                submit_data();
            }

        });
    }

    private void submit_data() {

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
//        String get_uprsac_div = scrl_uprsac_div.getSelectedItem().toString().trim();
        String user_num = get_user_num.getText().toString();
//        String user_full_name = get_user_full_name.getText().toString();
        String confirm_password = confirmed_password.getText().toString();
////post_data_method_by_jai
        Call<response_to_submit> todoPostCall = apiinterface.send_updated_password(
                confirm_password,
                user_num);   //Response_pr_my_app=response type


        todoPostCall.enqueue(new Callback<response_to_submit>() {            //enqueue is a method from retrofi to call

            @Override
            public void onResponse(Call<response_to_submit> call, Response<response_to_submit> response)
            {
                //                      Log.e(TAG, "onResponse: "+ response.body() );
                {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    if (response.code() == 200) {
                        if (response.body() .isStatus() == true) {
                            String msg = response.body().getMsg();
                            showSubmitDataDialog(msg);
//                            progressBar.setVisibility(View.GONE);
                        }

                        else if (response.body() .isStatus() == false) {
                            String msg = response.body().getMsg();
                            showSubmitData_error_Dialog(msg);
                        }
                    }
                }

                Toast.makeText(ForgotPasswordActivity.this,
                        response.body().getMsg(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<response_to_submit> call, Throwable t) {
                //                Log.e(TAG, "onFailure: "  + t.getLocalizedMessage() );
                Toast.makeText(ForgotPasswordActivity.this,
                        "SOMETHING WENT WRONG " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void showSubmitData_error_Dialog(String msg) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext, R.style.CustomDialogTheme);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("RETRY",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(mContext, User_registration_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showSubmitDataDialog(String msg) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext, R.style.CustomDialogTheme);
        builder.setTitle(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("LOGIN",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void submit_number(String used_number) {
        Call<response_to_number_login> todoPostCall = apiinterface.send_number(used_number);   //Response_pr_my_app=response type
        todoPostCall.enqueue(new Callback<response_to_number_login>() {            //enqueue is a method from retrofi to call

            @Override
            public void onResponse(Call<response_to_number_login> call, Response<response_to_number_login> response)
            {
                //                      Log.e(TAG, "onResponse: "+ response.body() );


                assert response.body() != null;
                if (!response.body().getStatus() == true){    // true or false boolean value from api will be parser

                    number_validation();

//                    for (int i = 0; i < response.body().getStatus().size(); i++) {
//
////                    String usernam = response.body().getData().get(i).get_username();
////                        username.setText(response.body().getData().get(i).getUsername());
////                        user_mob_num.setText(response.body().getData().get(i).getUserMobNum());
////                        uprsac_division.setText(response.body().getData().get(i).getUprsacDivision());
//                    }
//                    Intent i = new Intent(Login_Activity.this, MapsActivity.class);   ////opening another activity
//                    i.putExtra("username",username.getText().toString());
//                    i.putExtra("user_mob_num",user_mob_num.getText().toString());
//                    i.putExtra("uprsac_division",uprsac_division.getText().toString());
//                    startActivity(i);
//                    finish();///// now on pressing back button application will get closed

                }

                else {

                    String msg = "Invalid User Name/Password...!!";
//                    showSubmitDataDialog(msg);
                    Toast.makeText(ForgotPasswordActivity.this, "Access granted", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<response_to_number_login> call, Throwable t) {
                //                Log.e(TAG, "onFailure: "  + t.getLocalizedMessage() );
                Toast.makeText(ForgotPasswordActivity.this,
                        "SOMETHING WENT WRONG " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
     }

    private void number_validation() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext, R.style.CustomDialogTheme);
        builder.setMessage("We haven't found any account associated to this number");
        builder.setCancelable(false);
        builder.setPositiveButton("Change Number",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(mContext, MobileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}