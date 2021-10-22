package com.example.ayush_gis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DBTActivity extends AppCompatActivity {
    Button submit_btn;
    Spinner scr_opd_type,scr_ipd_type,scr_department_type;
    EditText get_male_opd_new, get_female_opd_new, get_adhar_patient_opd_new,get_mob_no_opd_new,get_total_opd_new;
    EditText get_male_opd_old,get_female_opd_old,get_adhar_patient_opd_old,get_mob_no_opd_old,get_total_opd_old;
    EditText  get_male_ipd_new,get_female_ipd_new,get_adhar_patient_ipd_new,get_mob_no_ipd_new,get_total_ipd_new;
    EditText  get_male_ipd_old,get_female_ipd_old,get_adhar_patient_ipd_old,get_mob_no_ipd_old,get_total_ipd_old;
    String opd_ipd_choose, opd_type, ipd_type;

    RadioGroup rg_opd_ipd_choose, rg_opd_choose, rg_ipd_choose;
    RadioButton rb_opd, rb_ipd, rb_opd_new, rb_opd_old, rb_ipd_new, rb_ipd_old;
    LinearLayout ll_opd_new,ll_opd_old,ll_ipd_new,ll_ipd_old;
    ImageView tv_photo;

    Apiinterface apiinterface;
    private Context mContext = DBTActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_b_t);

//        scr_attendance=(Spinner)findViewById(R.id.spin_attendance);
//        scr_opd_type=(Spinner)findViewById(R.id.spin_opd_type);
//        scr_ipd_type=(Spinner)findViewById(R.id.spin_ipd_type);
//        scr_department_type=(Spinner)findViewById(R.id.spin_type_of_departments);

        rb_opd = (RadioButton) findViewById(R.id.opd);
        rb_ipd = (RadioButton) findViewById(R.id.ipd);
        rb_opd_new = (RadioButton) findViewById(R.id.opd_new);
        rb_opd_old = (RadioButton) findViewById(R.id.opd_old);
        rb_ipd_new = (RadioButton) findViewById(R.id.ipd_new);
        rb_ipd_old = (RadioButton) findViewById(R.id.ipd_old);

        ll_opd_new=(LinearLayout)findViewById(R.id.ll_opd_new);
        ll_opd_old=(LinearLayout)findViewById(R.id.ll_opd_old);
        ll_ipd_new=(LinearLayout)findViewById(R.id.ll_ipd_new);
        ll_ipd_old=(LinearLayout)findViewById(R.id.ll_ipd_old);

        tv_photo = findViewById(R.id.tv_photo);

        get_male_opd_new=(EditText) findViewById(R.id.edit_male_opd_new);
        get_female_opd_new=(EditText) findViewById(R.id.edit_female_opd_new);
        get_adhar_patient_opd_new=(EditText) findViewById(R.id.opd_new_edit_adhar_patient);
        get_mob_no_opd_new=(EditText) findViewById(R.id.opd_new_edit_mob_no);
        get_total_opd_new=(EditText) findViewById(R.id.opd_new_edit_total);

        get_male_opd_old=(EditText) findViewById(R.id.edit_male_opd_old);
        get_female_opd_old=(EditText) findViewById(R.id.edit_female_opd_old);
        get_adhar_patient_opd_old=(EditText) findViewById(R.id.opd_old_edit_adhar_patient);
        get_mob_no_opd_old=(EditText) findViewById(R.id.opd_old_edit_mob_no);
        get_total_opd_old=(EditText) findViewById(R.id.opd_old_edit_total);

        get_male_ipd_new=(EditText) findViewById(R.id.edit_male_ipd_new);
        get_female_ipd_new=(EditText) findViewById(R.id.edit_female_ipd_new);
        get_adhar_patient_ipd_new=(EditText) findViewById(R.id.ipd_new_edit_adhar_patient);
        get_mob_no_ipd_new=(EditText) findViewById(R.id.ipd_new_edit_mob_no);
        get_total_ipd_new=(EditText) findViewById(R.id.ipd_new_edit_total);

        get_male_ipd_old=(EditText) findViewById(R.id.edit_male_ipd_old);
        get_female_ipd_old=(EditText) findViewById(R.id.edit_female_ipd_old);
        get_adhar_patient_ipd_old=(EditText) findViewById(R.id.ipd_old_edit_adhar_patient);
        get_mob_no_ipd_old=(EditText) findViewById(R.id.ipd_old_edit_mob_no);
        get_total_ipd_old=(EditText) findViewById(R.id.ipd_old_edit_total);

        apiinterface = ApiClient.getClient().create(Apiinterface.class);

        addListenerOnButton();

        get_male_opd_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String male_opd_new = get_male_opd_new.getText().toString();

                if(male_opd_new.isEmpty()){
                    get_male_opd_new.requestFocus();
                    get_male_opd_new.setError("Enter No.of New Male Patients of OPD");

                    return;
                }
            }
        });
        get_female_opd_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String female_opd_new = get_female_opd_new.getText().toString();

                if(female_opd_new.isEmpty() ){
                    get_female_opd_new.requestFocus();
                    get_female_opd_new.setError("Enter No.of New Female Patients of OPD");

                    return;

                }
            }
        });
        get_adhar_patient_opd_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String adhar_patient_opd_new = get_adhar_patient_opd_new.getText().toString();

                if(adhar_patient_opd_new.isEmpty()){
                    get_adhar_patient_opd_new.requestFocus();
                    get_adhar_patient_opd_new.setError("Enter No.of New Adhaar Seeded Patients of OPD ");

                    return;

                }
            }
        });

        get_mob_no_opd_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String mob_no_opd_old = get_mob_no_opd_new.getText().toString();

                if(mob_no_opd_old.isEmpty() ){
                    get_mob_no_opd_new.requestFocus();
                    get_mob_no_opd_new.setError("Enter Mobile No. of New patients of OPD");

                    return;

                }
            }
        });
        get_total_opd_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String total_opd_new = get_total_opd_new.getText().toString();

                if(total_opd_new.isEmpty()){
                    get_total_opd_new.requestFocus();
                    get_total_opd_new.setError("Enter Total Alloted New patients of OPD");

                    return;
                }
            }
        });
        get_male_opd_old.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String male_opd_old = get_male_opd_old.getText().toString();

                if(male_opd_old.isEmpty() ) {
                    get_male_opd_old.requestFocus();
                    get_male_opd_old.setError("Enter No.of  Old Male Patients of OPD");

                    return;

                }
            }
        });
        get_female_opd_old.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String female_opd_old = get_female_opd_old.getText().toString();

                if(female_opd_old.isEmpty() ){
                    get_female_opd_old.requestFocus();
                    get_female_opd_old.setError("Enter No.of Old Female Patients of OPD");

                    return;

                }
            }
        });
        get_adhar_patient_opd_old.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String adhar_patient_opd_old = get_adhar_patient_opd_old.getText().toString();

                if(adhar_patient_opd_old.isEmpty() ){
                    get_adhar_patient_opd_old.requestFocus();
                    get_adhar_patient_opd_old.setError("Enter No.of Old Adhaar Patients of OPD");

                    return;
                }
            }
        });
        get_mob_no_opd_old.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String mob_no_opd_old = get_mob_no_opd_old.getText().toString();

                if(mob_no_opd_old.isEmpty() ){
                    get_mob_no_opd_old.requestFocus();
                    get_mob_no_opd_old.setError("Enter Mobile No. of OLD patients of OPD");

                    return;
                }
            }
        });
        get_total_opd_old.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String total_opd_old = get_total_opd_old.getText().toString();

                if(total_opd_old.isEmpty() ){
                    get_total_opd_old.requestFocus();
                    get_total_opd_old.setError("Enter Total Alloted Old patients of OPD");

                    return;

                }
            }
        });
        get_male_ipd_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String male_ipd_new = get_male_ipd_new.getText().toString();

                if(male_ipd_new.isEmpty() ){
                    get_male_ipd_new.requestFocus();
                    get_male_ipd_new.setError("Enter No.of New Male Patients of IPD");

                    return;

                }
            }
        });
        get_female_ipd_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String female_ipd_new = get_female_ipd_new.getText().toString();

                if(female_ipd_new.isEmpty() ){
                    get_female_ipd_new.requestFocus();
                    get_female_ipd_new.setError("Enter No.of New Female Patients of IPD");

                    return;
                }
            }
        });
        get_adhar_patient_ipd_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String adhar_patient_ipd_new = get_adhar_patient_ipd_new.getText().toString();

                if(adhar_patient_ipd_new.isEmpty() ){
                    get_adhar_patient_ipd_new.requestFocus();
                    get_adhar_patient_ipd_new.setError("Enter No.of New Adhaar Seeded Patients of IPD");

                    return;
                }
            }
        });
        get_mob_no_ipd_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String mob_no_ipd_new = get_mob_no_ipd_new.getText().toString();

                if(mob_no_ipd_new.isEmpty() ){
                    get_mob_no_ipd_new.requestFocus();
                    get_mob_no_ipd_new.setError("Enter Mobile No. New Patients of IPD");

                    return;
                }
            }
        });
        get_total_ipd_new.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String total_ipd_new = get_total_ipd_new.getText().toString();

                if(total_ipd_new.isEmpty() ){
                    get_total_ipd_new.requestFocus();
                    get_total_ipd_new.setError("Enter Total Alloted New patients of IPD");

                    return;
                }
            }
        });
        get_male_ipd_old.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String male_ipd_old = get_male_ipd_old.getText().toString();

                if(male_ipd_old.isEmpty() ){
                    get_male_ipd_old.requestFocus();
                    get_male_ipd_old.setError("Enter No.of Old Male Patients of IPD");

                    return;

                }
            }
        });
        get_female_ipd_old.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String female_ipd_old = get_female_ipd_old.getText().toString();

                if(female_ipd_old.isEmpty() ) {
                    get_female_ipd_old.requestFocus();
                    get_female_ipd_old.setError("Enter No.of Old Female Patients of IPD");

                    return;
                }
            }
        });
        get_adhar_patient_ipd_old.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String adhar_patient_ipd_old = get_adhar_patient_ipd_old.getText().toString();

                if(adhar_patient_ipd_old.isEmpty() ){
                    get_adhar_patient_ipd_old.requestFocus();
                    get_adhar_patient_ipd_old.setError("Enter No.of Old Adhaar Seeded Patients of IPD");

                    return;

                }
            }
        });
        get_mob_no_ipd_old.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String mob_no_ipd_old = get_mob_no_ipd_old.getText().toString();

                if(mob_no_ipd_old.isEmpty() ){
                    get_mob_no_ipd_old.requestFocus();
                    get_mob_no_ipd_old.setError("Enter Mobile No. Old Patients of IPD ");

                    return;

                }
            }
        });
        get_total_ipd_old.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String total_ipd_old = get_total_ipd_old.getText().toString();

                if(total_ipd_old.isEmpty() ){
                    get_total_ipd_old.requestFocus();
                    get_total_ipd_old.setError("Enter Total Alloted Old patients of IPD");

                    return;
                }
            }
        });

        submit_btn=(Button)findViewById(R.id.submit_btn);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (scr_attendance.getSelectedItemPosition() == 0) {
//                    Toast.makeText(DBTActivity.this, "Please Select the option...", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if (rb_opd.isChecked()) {
                    opd_ipd_choose = rb_opd.getText().toString();
                } else if (rb_ipd.isChecked()){
                    opd_ipd_choose = rb_ipd.getText().toString();
                }
                Toast.makeText(getApplicationContext(), opd_ipd_choose, Toast.LENGTH_LONG).show(); // print the value of selected option

                if (rb_opd_new.isChecked()) {
                    opd_type = rb_opd_new.getText().toString();
                } else if (rb_opd_old.isChecked()){
                    opd_type = rb_opd_old.getText().toString();
                }
                Toast.makeText(getApplicationContext(), opd_type, Toast.LENGTH_LONG).show(); // print the value of selected option

                if (rb_ipd_new.isChecked()) {
                    ipd_type = rb_ipd_new.getText().toString();
                } else if (rb_ipd_old.isChecked()){
                    ipd_type = rb_ipd_old.getText().toString();
                }
                Toast.makeText(getApplicationContext(), ipd_type, Toast.LENGTH_LONG).show(); // print the value of selected option

                if (TextUtils.isEmpty(get_male_opd_new.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the number of New Male Patients of OPD ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_female_opd_new.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the number of New Female Patients of OPD", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_adhar_patient_opd_new.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the number of  Adhaar Seeded  New Patients of OPD", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_mob_no_opd_new.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the Mobile number for OPD New patients", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_total_opd_new.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the no. of  total allotted New patients of OPD", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (TextUtils.isEmpty(get_child_opd_new.getText().toString().trim())) {
//                    Toast.makeText(DBTActivity.this, "Please Enter the number...", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (TextUtils.isEmpty(get_male_opd_old.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the number of Old Male Patients of OPD", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_female_opd_old.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the number of Old Female Patients of OPD", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_adhar_patient_opd_old.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the number of  Adhaar Seeded New Patients of OPD", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_mob_no_opd_old.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the Mobile number of Old OPD Patients of OPD", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_total_opd_old.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the no. of total allotted Old patients of OPD", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (TextUtils.isEmpty(get_child_opd_old.getText().toString().trim())) {
//                    Toast.makeText(DBTActivity.this, "Please Enter the number...", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (TextUtils.isEmpty(get_male_ipd_new.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the number of New Male Patients of IPD", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_female_ipd_new.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the number of Female Patients of IPD", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_adhar_patient_ipd_new.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the number of  Adhaar Seeded New Patients of IPD", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_mob_no_ipd_new.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the Mobile number of New patients of IPD", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_total_ipd_new.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the no. of  total allotted New patients of IPD", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (TextUtils.isEmpty(get_child_ipd_new.getText().toString().trim())) {
//                    Toast.makeText(DBTActivity.this, "Please Enter the number...", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (TextUtils.isEmpty(get_male_ipd_old.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the number of old  Male Patients of IPD", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_female_ipd_old.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the number of Old Female Patients of IPD", Toast.LENGTH_SHORT).show();
                    return;
                } if (TextUtils.isEmpty(get_adhar_patient_ipd_old.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the number of   Adhaar Seeded Old Patients of IPD", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_mob_no_ipd_old.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the Mobile number of Old patients of IPD", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_total_ipd_old.getText().toString().trim())) {
                    Toast.makeText(DBTActivity.this, "Please Enter the no. of  total allotted Old patients of IPD", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (TextUtils.isEmpty(get_child_ipd_old.getText().toString().trim())) {
//                    Toast.makeText(DBTActivity.this, "Please Enter the number...", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                submit_data();
            }
        });
    }
    private void addListenerOnButton() {
        rg_opd_ipd_choose = (RadioGroup) findViewById(R.id.opd_ipd_choose);
        rg_opd_choose = (RadioGroup) findViewById(R.id.opd_choose);
        rg_ipd_choose = (RadioGroup) findViewById(R.id.ipd_choose);

        rg_opd_ipd_choose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                if (checkedId == R.id.opd) {
                    rg_opd_choose.setVisibility(View.VISIBLE);
                    rg_ipd_choose.setVisibility(View.GONE);
                    ll_opd_new.setVisibility(View.GONE);
                    ll_opd_old.setVisibility(View.GONE);
                    ll_ipd_new.setVisibility(View.GONE);
                    ll_ipd_old.setVisibility(View.GONE);
                    submit_btn.setVisibility(View.GONE);

                }
                if (checkedId == R.id.ipd) {
                    rg_opd_choose.setVisibility(View.GONE);
                    rg_ipd_choose.setVisibility(View.VISIBLE);
                    ll_opd_new.setVisibility(View.GONE);
                    ll_opd_old.setVisibility(View.GONE);
                    ll_ipd_new.setVisibility(View.GONE);
                    ll_ipd_old.setVisibility(View.GONE);
                    submit_btn.setVisibility(View.GONE);

                }
            }
        });
        rg_opd_choose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                if (checkedId == R.id.opd_new) {
                    ll_opd_new.setVisibility(View.VISIBLE);
                    ll_opd_old.setVisibility(View.GONE);
                    ll_ipd_new.setVisibility(View.GONE);
                    ll_ipd_old.setVisibility(View.GONE);
                    submit_btn.setVisibility(View.VISIBLE);

                }
                if (checkedId == R.id.opd_old) {
                    ll_opd_new.setVisibility(View.GONE);
                    ll_opd_old.setVisibility(View.VISIBLE);
                    ll_ipd_new.setVisibility(View.GONE);
                    ll_ipd_old.setVisibility(View.GONE);
                    submit_btn.setVisibility(View.VISIBLE);

                }
            }
        });
        rg_ipd_choose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.ipd_new) {
                    ll_ipd_new.setVisibility(View.VISIBLE);
                    ll_ipd_old.setVisibility(View.GONE);
                    ll_opd_new.setVisibility(View.GONE);
                    ll_opd_old.setVisibility(View.GONE);
                    submit_btn.setVisibility(View.VISIBLE);

                }
                if (checkedId == R.id.ipd_old) {
                    ll_ipd_new.setVisibility(View.GONE);
                    ll_ipd_old.setVisibility(View.VISIBLE);
                    ll_opd_new.setVisibility(View.GONE);
                    ll_opd_old.setVisibility(View.GONE);
                    submit_btn.setVisibility(View.VISIBLE);

                }
            }
        });

    }
    public void submit_data () {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String opd_choose = rb_opd.getText().toString();
        String ipd_choose = rb_ipd.getText().toString();
        String opd_new = rb_opd_new.getText().toString();
        String opd_old = rb_opd_old.getText().toString();
        String ipd_new = rb_ipd_new.getText().toString();
        String ipd_old = rb_ipd_old.getText().toString();

        String male_opd_new = get_male_opd_new.getText().toString();
        String female_opd_new = get_female_opd_new.getText().toString();
        String adhar_patient_opd_new = get_adhar_patient_opd_new.getText().toString();
        String mobile_no_opd_new = get_mob_no_opd_new.getText().toString();
        String total_opd_new = get_total_opd_new.getText().toString();
//        String child_opd_new = get_child_opd_new.getText().toString();
        String male_opd_old = get_male_opd_old.getText().toString();
        String female_opd_old = get_female_opd_old.getText().toString();
        String adhar_patient_opd_old = get_adhar_patient_opd_old.getText().toString();
        String mobile_no_opd_old = get_mob_no_opd_old.getText().toString();
        String total_opd_old = get_total_opd_old.getText().toString();
        //        String child_opd_old = get_child_opd_old.getText().toString();

        String male_ipd_old = get_male_ipd_old.getText().toString();
        String female_ipd_old = get_female_ipd_old.getText().toString();
        String adhar_patient_ipd_old = get_adhar_patient_ipd_old.getText().toString();
        String mobile_no_ipd_old = get_mob_no_ipd_old.getText().toString();
        String total_ipd_old = get_total_ipd_old.getText().toString();
//        String child_ipd_old = get_child_ipd_old.getText().toString();
        String male_ipd_new = get_male_ipd_new.getText().toString();
        String female_ipd_new = get_female_ipd_new.getText().toString();
        String adhar_patient_ipd_new = get_adhar_patient_ipd_new.getText().toString();
        String mobile_no_ipd_new = get_mob_no_ipd_new.getText().toString();
        String total_ipd_new = get_total_ipd_new.getText().toString();
//        String child_ipd_new = get_child_ipd_new.getText().toString();

        ////post_data_method_by_jai
        Call<response_to_submit> todoPostCall = apiinterface.insert_data(
                opd_choose,
                ipd_choose,
                opd_new,
                opd_old,
                ipd_new,
                ipd_old,
                male_opd_old,
                female_opd_old,
                adhar_patient_opd_old,
                mobile_no_opd_old,
                total_opd_old,
//                child_opd_old,
                male_opd_new,
                female_opd_new,
                adhar_patient_opd_new,
                mobile_no_opd_new,
                total_opd_new,
//                child_opd_new,
                male_ipd_old,
                female_ipd_old,
                adhar_patient_ipd_old,
                mobile_no_ipd_old,
                total_ipd_old,
//                child_ipd_old,
                male_ipd_new,
                female_ipd_new,
                adhar_patient_ipd_new,
                mobile_no_ipd_new,
                total_ipd_new);

        todoPostCall.enqueue(new Callback<response_to_submit>() {            //enqueue is a method from retrofit to call
            @Override
            public void onResponse(Call<response_to_submit> call, Response<response_to_submit> response) {

                {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            String msg = response.body().getMsg();
                            showSubmitDataDialog(msg);
                        }
                    }
                }
                Toast.makeText(DBTActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<response_to_submit> call, Throwable t) {
                //                Log.e(TAG, "onFailure: "  + t.getLocalizedMessage() );
                Toast.makeText(DBTActivity.this, "SOMETHING WENT WRONG " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
    public String getBase64FromFile(File f) {
        InputStream inputStream = null;
        String encodedFile = "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());
            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile = output.toString();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private void showSubmitDataDialog (String message){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext, R.style.CustomDialogTheme);
        builder.setTitle(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
        }
}
