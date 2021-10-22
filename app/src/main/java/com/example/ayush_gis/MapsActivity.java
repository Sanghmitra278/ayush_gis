package com.example.ayush_gis;

import android.Manifest;
import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DirectAction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.ayush_gis.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private Context mContext = MapsActivity.this;
    private String TAG = MapsActivity.class.getName();
    private FusedLocationProviderClient mFusedLocationClient, fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
//    private TextView txtlatitude, txtlongitude, txtLocationAccuracy;
//    private GoogleMap googleMap;
    private Marker currentLocationMarker;
    private Location currentLocation;
    private LocationCallback mLocationCallback;
    private String lat = "0.0", lon = "0.0";
    private File actualImage, compressedImage;
    private Uri fileUri;
    private String base64Img = null;
    private SharedPreferences statusPermissionSp;
//    private Toolbar toolbar;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5445;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private boolean firstTimeFlag = true, isGPS = false;
    private static final String IMAGE_DIRECTORY_NAME = "progress_monitoring";
    private final static int IMAGE_RESULT = 200;
    private static final int MEDIA_TYPE_IMAGE = 1;

    private Marker cur_loc;
    private ScrollView mScrollView;

    LinearLayout hospital_layout,medical_layout,_building_type_layout;
    LinearLayout ll_post1,ll_post2,ll_post3;
    Spinner scr_ayush_type;
    Spinner scr_office_type;
    Spinner scr_district;
    Spinner scr_block;
    Spinner scr_vidhansabha;
    EditText get_post_name;
    EditText get_post_filled;
    EditText get_post_vacant;
    EditText get_no_of_post;
    Spinner scr_class;
    Spinner scr_building_type;
    EditText get_office_name;
    EditText get_office_address;
    EditText get_landmark;
    EditText get_police_station;
    EditText get_pincode;
    EditText get_estb_year;
    EditText get_area;
    EditText get_rooms_available;
    TextView get_accu;
    TextView get_lat;
    TextView get_lon;
    RadioButton Rb_bath_yes_use,Rb_bath_yes_not_use, Rb_bath_no;
    RadioButton Rb_furn_sufficient,Rb_furn_insuficient;
    RadioButton Rb_internet_yes_use,Rb_internet_yes_not_use, Rb_internet_no,Rb_internet_not_applicable;
    String bathroom, furniture, internet;
    Button submit_btn,capture_photo;
    ImageView tv_photo;
    private String base64_pdf;
    Apiinterface apiinterface;
    TextView save_district ;
    TextView save_office_type;
    TextView save_block;
    TextView save_vidhansabha;
    LinearLayout layout_office_type;
    Spinner office_name;

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mScrollView = (ScrollView) findViewById(R.id.mScrollView);

        hospital_layout = (LinearLayout) findViewById( R.id.ll_for_hospital);
        medical_layout = (LinearLayout) findViewById( R.id.ll_for_medical_college);
        _building_type_layout = (LinearLayout) findViewById( R.id.ll_building_type);

        ll_post1 = (LinearLayout) findViewById( R.id.ll_post1);
        ll_post2 = (LinearLayout) findViewById( R.id.ll_post2);
        ll_post3 = (LinearLayout) findViewById( R.id.ll_post3);

        scr_ayush_type = (Spinner) findViewById( R.id.spin_ayush_type );
        scr_office_type = (Spinner) findViewById( R.id.spin_office_type );
        scr_district = (Spinner) findViewById( R.id.spin_district );
        scr_block = (Spinner) findViewById( R.id.spin_block );
        scr_vidhansabha = (Spinner) findViewById( R.id.spin_vidhansabha );
//        scr_post_name = (Spinner) findViewById( R.id.spin_post_name );
        scr_class = (Spinner) findViewById( R.id.spin_class );
        scr_building_type = (Spinner) findViewById( R.id.spin_building_type );
        get_post_name=(EditText) findViewById(R.id.edit_post_name);

        get_office_name =(EditText) findViewById(R.id.edit_office_name);
        get_office_address =(EditText) findViewById(R.id.edit_office_address);
        get_landmark =(EditText) findViewById(R.id.edit_landmark);
        get_police_station =(EditText) findViewById(R.id.edit_police_station);
        get_pincode =(EditText) findViewById(R.id.edit_pincode);
        get_no_of_post =(EditText) findViewById(R.id.edit_no_of_post);
        get_post_filled =(EditText) findViewById(R.id.edit_post_filled);
        get_post_vacant =(EditText) findViewById(R.id.edit_post_vacant);
        get_estb_year =(EditText) findViewById(R.id.edit_estb_year);
        get_area =(EditText) findViewById(R.id.edit_area);
        get_rooms_available =(EditText) findViewById(R.id.edit_rooms_avaialable);
        get_accu = findViewById(R.id.get_accu);
        get_lat = findViewById(R.id.get_lat);
        get_lon = findViewById(R.id.get_lon);
        tv_photo = (ImageView) findViewById(R.id.tv_photo);
        layout_office_type =  findViewById(R.id.layout_office_type);
        office_name =  findViewById(R.id.office_name);

        submit_btn = findViewById(R.id.submit_btn);
        capture_photo= findViewById(R.id.capture_photo);
        apiinterface = ApiClient.getClient().create(Apiinterface.class);
        mScrollView = (ScrollView) findViewById(R.id.mScrollView);

        initViews();
        checkAndRequestPermissions();
        get_list_of_district();
        scr_office_type.setOnItemSelectedListener( new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    hospital_layout.setVisibility( View.GONE );
                    medical_layout.setVisibility( View.GONE );

                }
                if (position > 1) {

                    hospital_layout.setVisibility( View.VISIBLE );
                    medical_layout.setVisibility( View.GONE );
                }
                if (position > 2) {

                    hospital_layout.setVisibility( View.GONE );
                    medical_layout.setVisibility( View.VISIBLE );
                }
                if (position > 3) {

                    hospital_layout.setVisibility( View.GONE );
                    medical_layout.setVisibility( View.GONE );
                }
                if (position > 4) {

                    hospital_layout.setVisibility( View.GONE );
                    medical_layout.setVisibility( View.GONE );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );
        scr_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {            //on item selected listener spinner android  https://stackoverflow.com/questions/1337424/android-spinner-get-the-selected-item-change-event
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {  //for_state_pick
                // your code here
//                if (position == 0) {
//                    Toast.makeText(MapsActivity.this, "Please Select Division first", Toast.LENGTH_SHORT).show();
////                    return;
//                }
                if (position > 0){
                    get_ayush_type(scr_district.getSelectedItem().toString().trim());

                   // save_district.setText(scr_district.getSelectedItem().toString());
                }

//                if (position > 0 && scr_district.getSelectedItem().toString() == "Other"){
////                    layout_e_dis.setVisibility(View.VISIBLE);
////                    layout_s_dis.setVisibility(View.GONE);
////                    save_dis.setText(get_other_dis.getText().toString());
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        scr_ayush_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {            //on item selected listener spinner android  https://stackoverflow.com/questions/1337424/android-spinner-get-the-selected-item-change-event
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {  //for_state_pick
                // your code here
//                if (position == 0) {
//                    Toast.makeText(MapsActivity.this, "Please Select Division first", Toast.LENGTH_SHORT).show();
////                    return;
//                }
                if (position > 0){
                    get_office_type_from_ayush_stram(scr_district.getSelectedItem().toString(),scr_ayush_type.getSelectedItem().toString());
                    //get_ayush_type(scr_district.getSelectedItem().toString().trim());
//if(scr_ayush_type.getSelectedItem().toString() == "Homoeopathy" ) {layout_office_type.setVisibility(View.GONE); }
//if (scr_ayush_type.getSelectedItem().toString() == "Ayurveda" ){layout_office_type.setVisibility(View.VISIBLE);}
//if (scr_ayush_type.getSelectedItem().toString() == "Unani" ){layout_office_type.setVisibility(View.VISIBLE);}

                    // save_district.setText(scr_district.getSelectedItem().toString());
                }

//                if (position > 0 && scr_district.getSelectedItem().toString() == "Other"){
////                    layout_e_dis.setVisibility(View.VISIBLE);
////                    layout_s_dis.setVisibility(View.GONE);
////                    save_dis.setText(get_other_dis.getText().toString());
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        scr_office_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {            //on item selected listener spinner android  https://stackoverflow.com/questions/1337424/android-spinner-get-the-selected-item-change-event
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {  //for_state_pick
                // your code here
//                if (position == 0) {
//                    Toast.makeText(MapsActivity.this, "Please Select Division first", Toast.LENGTH_SHORT).show();
////                    return;
//                }
                if (position > 0){
                    get_office_names_list_from_dis_ayush_stram_office_type(scr_district.getSelectedItem().toString(),scr_ayush_type.getSelectedItem().toString(),scr_office_type.getSelectedItem().toString());
                    //get_ayush_type(scr_district.getSelectedItem().toString().trim());
//if(scr_ayush_type.getSelectedItem().toString() == "Homoeopathy" ) {layout_office_type.setVisibility(View.GONE); }
//if (scr_ayush_type.getSelectedItem().toString() == "Ayurveda" ){layout_office_type.setVisibility(View.VISIBLE);}
//if (scr_ayush_type.getSelectedItem().toString() == "Unani" ){layout_office_type.setVisibility(View.VISIBLE);}

                    // save_district.setText(scr_district.getSelectedItem().toString());
                }

//                if (position > 0 && scr_district.getSelectedItem().toString() == "Other"){
////                    layout_e_dis.setVisibility(View.VISIBLE);
////                    layout_s_dis.setVisibility(View.GONE);
////                    save_dis.setText(get_other_dis.getText().toString());
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


     scr_building_type.setOnItemSelectedListener( new Spinner.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

             if (position > 0) {
                 _building_type_layout.setVisibility( View.GONE );

             }
             if (position > 1) {
                 _building_type_layout.setVisibility( View.GONE );

             }
             if (position > 2) {
                 _building_type_layout.setVisibility( View.GONE );

             }
             if (position >3) {

                 _building_type_layout.setVisibility( View.VISIBLE );

             }
         }

         @Override
         public void onNothingSelected(AdapterView<?> parent) {

         }
     } );
//        get_office_name.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                String office_name = get_office_name.getText().toString();
//
//                if(office_name.isEmpty() ){
//                    get_office_name.requestFocus();
//                    get_office_name.setError("Enter Office Address");
//
//                    return;
//
//                }
//            }
//        });
//        get_office_address.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                String office_address = get_office_address.getText().toString();
//
//                if(office_address.isEmpty() ){
//                    get_office_address.requestFocus();
//                    get_office_address.setError("Enter Office Address");
//
//                    return;
//
//                }
//            }
//        });
//        get_landmark.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                String landmark = get_landmark.getText().toString();
//
//                if(landmark.isEmpty() ){
//                    get_landmark.requestFocus();
//                    get_landmark.setError("Enter Landmark");
//
//                    return;
//
//                }
//            }
//        });
//        get_police_station.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                String police_station = get_police_station.getText().toString();
//
//                if(police_station.isEmpty() ){
//                    get_police_station.requestFocus();
//                    get_police_station.setError("Enter Police Station Name");
//
//                    return;
//
//                }
//            }
//        });
//        get_pincode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                String pincode = get_pincode.getText().toString();
//
//                if(pincode.isEmpty() ){
//                    get_pincode.requestFocus();
//                    get_pincode.setError("Enter Pincode");
//
//                    return;
//
//                }
//            }
//        });
//        get_post_name.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                String post_name = get_post_name.getText().toString();
//
//                if(post_name.isEmpty() ){
//                    get_post_name.requestFocus();
//                    get_post_name.setError("Enter Post Name");
//
//                    return;
//
//                }
//            }
//        });
//        get_no_of_post.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                String post_no = get_no_of_post.getText().toString();
//
//                if(post_no.isEmpty() ){
//                    get_no_of_post.requestFocus();
//                    get_no_of_post.setError("Enter No. of Post");
//
//                    return;
//
//                }
//            }
//        });
//        get_post_filled.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                String post_filled = get_post_filled.getText().toString();
//
//                if(post_filled.isEmpty() ){
//                    get_post_filled.requestFocus();
//                    get_post_filled.setError("Enter No. of Post Filled");
//
//                    return;
//
//                }
//            }
//        });
//        get_post_vacant.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                String post_vacant = get_post_vacant.getText().toString();
//
//                if(post_vacant.isEmpty() ){
//                    get_post_vacant.requestFocus();
//                    get_post_vacant.setError("Enter No. of Post Vacant");
//
//                    return;
//
//                }
//            }
//        });
//        get_estb_year.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                String estb_year = get_estb_year.getText().toString();
//
//                if(estb_year.isEmpty() ){
//                    get_estb_year.requestFocus();
//                    get_estb_year.setError("Enter Establishment Year");
//
//                    return;
//
//                }
//            }
//        });
//        get_area.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                String area = get_area.getText().toString();
//
//                if(area.isEmpty() ){
//                    get_area.requestFocus();
//                    get_area.setError("Enter Total Area");
//
//                    return;
//
//                }
//            }
//        });
//        get_rooms_available.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                String available_room = get_rooms_available.getText().toString();
//
//                if(available_room.isEmpty() ){
//                    get_rooms_available.requestFocus();
//                    get_rooms_available.setError("Enter No. of Rooms Available");
//
//                    return;
//
//                }
//            }
//        });
        capture_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkAndRequestPermissions()) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    hideSoftKeyboard();
                    cameraIntent();
                } else {
                    checkAndRequestPermissions();
                }
            }
        });

    }

//    private void get_block_from_district(String trim) {
//
//    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, IMAGE_RESULT);
    }

    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void initViews() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                mScrollView.requestDisallowInterceptTouchEvent(true);
            }
        });

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult.getLastLocation() == null)
                    return;
                currentLocation = locationResult.getLastLocation();
                if (firstTimeFlag && map != null) {
                    animateCamera(currentLocation);
                    firstTimeFlag = false;
                }
                showMarker(currentLocation);
            }
        };
        // TODO- Start of Latitude & Logitude
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},1000);
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                isGPS = isGPSEnable;
            }
        });
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        get_accu.setText(String.format(Locale.ENGLISH, "%f", location.getAccuracy()));

                        lat = String.valueOf(wayLatitude);
                        lon = String.valueOf(wayLongitude);
                        get_lat.setText(lat  );
                        get_lon.setText(lon);
                        if (mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };
        if (!isGPS) {
//            Toast.makeText(MapsActivity.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
            new GpsUtils(MapsActivity.this).turnGPSOn(
                    new GpsUtils.onGpsListener() {
                @Override
                public void gpsStatus(boolean isGPSEnable) {
                    isGPS = isGPSEnable;
                }
            });
            return;
        }

        getLocation();

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scr_ayush_type.getSelectedItemPosition() == 0) {
                    Toast.makeText(MapsActivity.this, "Please Select Ayush Type...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (scr_district.getSelectedItemPosition() == 0) {
                    Toast.makeText(MapsActivity.this, "Please Select District...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (scr_office_type.getSelectedItemPosition() == 0) {
                    Toast.makeText(MapsActivity.this, "Please Select Office Name...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_office_name.getText().toString().trim())) {
                    Toast.makeText(MapsActivity.this, "Please Enter Office Name...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_office_address.getText().toString().trim())) {
                    Toast.makeText(MapsActivity.this, "Please Enter Office Address...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (scr_block.getSelectedItemPosition() == 0) {
                    Toast.makeText(MapsActivity.this, "Please Select Block/Tehsil...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (scr_vidhansabha.getSelectedItemPosition() == 0) {
                    Toast.makeText(MapsActivity.this, "Please Select Vidhansabha...", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (scr_post_name.getSelectedItemPosition() == 0) {
//                    Toast.makeText(MapsActivity.this, "Please Select Post Name...", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (TextUtils.isEmpty(get_landmark.getText().toString().trim())) {
                    Toast.makeText(MapsActivity.this, "Please Enter Landmark...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_police_station.getText().toString().trim())) {
                    Toast.makeText(MapsActivity.this, "Please Enter nearest Police Station...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_pincode.getText().toString().trim())) {
                    Toast.makeText(MapsActivity.this, "Please Enter Pincode...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_post_name.getText().toString().trim())) {
                    Toast.makeText(MapsActivity.this, "Please Enter Post Name...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_no_of_post.getText().toString().trim())) {
                    Toast.makeText(MapsActivity.this, "Please Enter No of Post...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_post_filled.getText().toString().trim())) {
                    Toast.makeText(MapsActivity.this, "Please Enter No of Post Filled...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_post_vacant.getText().toString().trim())) {
                    Toast.makeText(MapsActivity.this, "Please Enter No of Post Vacant...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (scr_class.getSelectedItemPosition() == 0) {
                    Toast.makeText(MapsActivity.this, "Please Select Class...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_estb_year.getText().toString().trim())) {
                    Toast.makeText(MapsActivity.this, "Please Enter Establishment Year...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (scr_building_type.getSelectedItemPosition() == 0) {
                    Toast.makeText(MapsActivity.this, "Please Select Building Type...", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(get_area.getText().toString().trim())) {
                    Toast.makeText(MapsActivity.this, "Please Enter Total Area...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(get_rooms_available.getText().toString().trim())) {
                    Toast.makeText(MapsActivity.this, "Please Enter Total No of Rooms...", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Rb_bath_yes_use.isChecked()) {
                    bathroom = Rb_bath_yes_use.getText().toString();
                } else if (Rb_bath_yes_not_use.isChecked()) {
                    bathroom = Rb_bath_yes_not_use.getText().toString();
                } else if (Rb_bath_no.isChecked()) {
                    bathroom = Rb_bath_no.getText().toString();
                }
                Toast.makeText(getApplicationContext(), bathroom, Toast.LENGTH_LONG).show(); // print the value of selected option

                if (Rb_furn_sufficient.isChecked()) {
                    furniture = Rb_furn_sufficient.getText().toString();
                } else if (Rb_furn_insuficient.isChecked()) {
                    furniture = Rb_furn_insuficient.getText().toString();
                }
                Toast.makeText(getApplicationContext(), furniture, Toast.LENGTH_LONG).show(); // print the value of selected option

                if (Rb_internet_yes_use.isChecked()) {
                    internet = Rb_internet_yes_use.getText().toString();
                } else if (Rb_internet_yes_not_use.isChecked()) {
                    internet = Rb_internet_yes_not_use.getText().toString();
                } else if (Rb_internet_no.isChecked()) {
                    internet = Rb_internet_no.getText().toString();
                } else if (Rb_internet_not_applicable.isChecked()) {
                    internet = Rb_internet_not_applicable.getText().toString();
                }
                Toast.makeText(getApplicationContext(), internet, Toast.LENGTH_LONG).show(); // print the value of selected option

                if (base64Img == null) {
                    Toast.makeText(MapsActivity.this, "Please Capture Image First !", Toast.LENGTH_SHORT).show();
                    return;
                }
                submit_data();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);//for current location dot
        map.getUiSettings().setZoomControlsEnabled(true);    //set built in zoomin zoomout
        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                return false;
            }
        });

//       map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {             //on_click_marker
//            @Override
//            public void onMapClick(LatLng latLng) {
//                LatLng uttar_pradesh = new LatLng(latLng.latitude, latLng.longitude);
//                if(cur_loc != null)
//                {
//                    cur_loc.remove();
//                    cur_loc = null;
//                }
//                cur_loc= map.addMarker(new MarkerOptions().position(uttar_pradesh).title(latLng.latitude+","+ latLng.longitude));
//
//                lat  = String.valueOf(latLng.latitude);
//                lon = String.valueOf(latLng.longitude);
//                get_lat.setText(lat);
//                get_lon.setText(lon);
//
////                loc.setText("Latitude:\t" + lat + "\nLongitude:\t" + lon );    //set clicked lat long to textview
////                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//            }
//        });

//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {             //on_click_marker
//            @Override
//            public void onMapClick(LatLng latLng) {
//                LatLng sydney = new LatLng(latLng.latitude, latLng.longitude);
//                if(cur_loc != null)
//                {
//                    cur_loc.remove();
//                    cur_loc = null;
//                }
//                cur_loc=mMap.addMarker(new MarkerOptions().position(sydney).title(latLng.latitude+","+ latLng.longitude));
//
//                lat  = String.valueOf(latLng.latitude);
//                lon = String.valueOf(latLng.longitude);
////                get_lat.setText(lat);
////                get_lon.setText(lon);
////                loc.setText("Latitude:\t" + lat + "\nLongitude:\t" + lon );    //set clicked lat long to textview
////                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//            }
//        });

    }

    private void startCurrentLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                return;
            }
        }
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status)
            return true;
        else {
            if (googleApiAvailability.isUserResolvableError(status))
                Toast.makeText(this, "Please Install google play services to use this application", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private boolean checkAndRequestPermissions() {
        int accessFineLocationpermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int accessCoarseLocationpermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int readStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (accessFineLocationpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (accessCoarseLocationpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (readStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writeStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1000);
            return false;
        }
        return true;
    }

    private void animateCamera(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        map.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLng)));
    }
    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(18).build();
    }

    private void showMarker(@NonNull Location currentLocation) {
        lat = String.valueOf(currentLocation.getLatitude());
        lon = String.valueOf(currentLocation.getLongitude());
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//        if (currentLocationMarker == null)
//            currentLocationMarker = map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()).position(latLng));
//        else
//            MarkerAnimation.animateMarkerToGB(currentLocationMarker, latLng, new LatLngInterpolator.Spherical());
    }

/////////////////////////////////////////////

    @Override
    protected void onStop() {
        super.onStop();
        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        boolean isPermissionGranted = statusPermissionSp.getBoolean("isPermissionGranted", false);
//        if (isPermissionGranted) {
            if (isGooglePlayServicesAvailable()) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                startCurrentLocationUpdates();
            }
        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fusedLocationProviderClient = null;
        map = null;
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1000);
        } else {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        lat = String.valueOf(wayLatitude);
                        lon = String.valueOf(wayLongitude);
                        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        map.setMyLocationEnabled(true);
                    } else {
                        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == IMAGE_RESULT) {
                onCaptureImageResult(data);
            }
        } else if (resultCode == RESULT_CANCELED) {
            Log.d(TAG, "onActivityResult: " + "Cancelled");
        } else {
            Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
        }
    }

    ////jai's image capture code
    private void onCaptureImageResult(Intent data) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            bitmap = rotateImageIfRequired(MapsActivity.this, bitmap, fileUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        actualImage = new File(getCacheDir(),System.currentTimeMillis() + ".jpg");
        try {
            actualImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] bitmapdata = bos.toByteArray();
        try {
            FileOutputStream fos = new FileOutputStream(actualImage);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        compressImage();
    }
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
    public void compressImage() {
        if (actualImage == null) {
            Toast.makeText(this, "Please choose an image!", Toast.LENGTH_SHORT).show();
        } else {
            new Compressor(this)
                    .compressToFileAsFlowable(actualImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.functions.Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            compressedImage = file;
                            base64Img = getBase64FromFile(compressedImage);
                            setCompressedImage();
                        }
                    },
                            new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
                            Log.e("error", throwable.getMessage());
                        }
                    });
        }
    }

    private void setCompressedImage() {
        tv_photo.setImageBitmap(BitmapFactory.decodeFile(compressedImage.getAbsolutePath()));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }
    //=====+++++======++++++++++================+========++++++++++++++=====================ends here
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


    //--------------------------------

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    {
//                        SharedPreferences.Editor editor = statusPermissionSp.edit();
//                        editor.putBoolean("isPermissionGranted", true);
//                        editor.commit();
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                        mFusedLocationClient.getLastLocation().addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    wayLatitude = location.getLatitude();
                                    wayLongitude = location.getLongitude();
                                    lat = String.valueOf(wayLatitude);
                                    lon = String.valueOf(wayLongitude);
//                                    txtlatitude.setText(lat);
//                                    txtlongitude.setText(lon);
//                                    txtLocationAccuracy.setText(location.getAccuracy() + "");
                                    map.setMyLocationEnabled(true);
                                } else {
                                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                                }
                            }
                        });
                    } else {
//                        SharedPreferences.Editor editor = statusPermissionSp.edit();
//                        editor.putBoolean("isPermissionGranted", false);
//                        editor.commit();
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, Manifest.permission.CAMERA) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showSettingsDialog();


                        } else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    }
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


        //-----------l0000000000000000000000000000000000.---------------------
        public void submit_data () {

            final ProgressDialog progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Loading...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            String ayush_type = scr_ayush_type.getSelectedItem().toString().trim();
            String district = scr_district.getSelectedItem().toString().trim();
            String office_type = scr_office_type.getSelectedItem().toString().trim();
            String block = scr_block.getSelectedItem().toString().trim();
            String vidhansabha = scr_vidhansabha.getSelectedItem().toString().trim();
            String post_name = get_post_name.getText().toString();
            String class_name = scr_class.getSelectedItem().toString().trim();
            String building_type = scr_building_type.getSelectedItem().toString().trim();
            String office_address = get_office_address.getText().toString();
            String office_name = get_office_name.getText().toString();
            String landmark = get_landmark.getText().toString();
            String police_station = get_police_station.getText().toString();
            String pincode = get_pincode.getText().toString();
            String no_of_post = get_no_of_post.getText().toString();
            String post_filled = get_post_filled.getText().toString();
            String post_vacant = get_post_vacant.getText().toString();
            String estb_year = get_estb_year.getText().toString();
            String total_area = get_area.getText().toString();
            String rooms_available = get_rooms_available.getText().toString();
            String latitude = lat;
            String longitude = lon;
            String photo = base64Img;
            String bathroom_yes_use = Rb_bath_yes_use.getText().toString();
            String bathroom_yes__not_use =Rb_bath_yes_not_use.getText().toString();
            String bathroom_no = Rb_bath_no.getText().toString();
            String furniture_sufficient = Rb_furn_sufficient.getText().toString();
            String furniture_insufficient = Rb_furn_insuficient.getText().toString();
            String internet_yes_use = Rb_internet_yes_use.getText().toString();
            String internet_yes_not_use = Rb_internet_yes_not_use.getText().toString();
            String internet_no = Rb_internet_no.getText().toString();
            String internet_not_applicable = Rb_internet_not_applicable.getText().toString();

            ////post_data_method_by_jai
            Call<response_to_submit> todoPostCall = apiinterface.insert_data(
                    ayush_type,
                    district,
                    office_type,
                    block,
                    vidhansabha,
                    post_name,
                    class_name,
                    building_type,
                    office_name,
                    office_address,
                    landmark,
                    police_station,
                    pincode,
                    no_of_post,
                    total_area,
                    post_filled,
                    post_vacant,
                    estb_year,
                    rooms_available,
                    bathroom_yes_use,
                    bathroom_yes__not_use,
                    bathroom_no,
                    furniture_sufficient,
                    furniture_insufficient,
                    internet_yes_use,
                    internet_yes_not_use,
                    internet_no,
                    internet_not_applicable,
                    latitude,
                    longitude,
                    photo);

            //Response_pr_my_app=response type

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
                    Toast.makeText(MapsActivity.this,
                            response.body().getMsg(), Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFailure(Call<response_to_submit> call, Throwable t) {
                    //                Log.e(TAG, "onFailure: "  + t.getLocalizedMessage() );
                    Toast.makeText(MapsActivity.this,
                            "SOMETHING WENT WRONG " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
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



    public void get_list_of_district(){     //taking selected statte var                                   ////post_data_method_by_jai this methode has been called in setOnItemSelectedListener in on create
        Call<response_to_get_dis_list> todoPostCall = apiinterface.get_dis_list();   //Rresponse_for_district=response type from api_interface  //todopostcall method has been used just in next line only
        todoPostCall.enqueue(new Callback<response_to_get_dis_list>() {            //enqueue is a method from retrofi to call

            @Override
            public void onResponse(Call<response_to_get_dis_list> call, Response<response_to_get_dis_list> response)
            {
                //                      Log.e(TAG, "onResponse: "+ response.body() );

                if (response.body().getStatus()) {   //if get status is true


                    ArrayList<String> dist_list = new ArrayList<>();    // make an array to push recieved values
                    dist_list.add(" Select District");
//                    temp.add("--select district--");               // the first spin value
//                    dist_list.add("select District");

                    for (int i = 0; i < response.body().getData().size(); i++) {
                        String div_list = response.body().getData().get(i).getDistrictList();
                        Collections.sort(dist_list, new Comparator<String>() {
                            @Override
                            public int compare(String s1, String s2) {
                                return s1.compareToIgnoreCase(s2);
                            }
                        });
//                        if (dis_list == null) { holder.txtColor.setText("Alt text") } else { holder.txtColor.setText(p.getColorName())

                        dist_list.add(div_list);


                    }

                    //dist_list.add("Other");
//                    for (int i = 0; i < response.body().getData().size(); i++) {
//                        dist_list.add(response.body().getData().get(i).getBlockName());  // getDistrictList() is from response_for_district class
//                    }

                    ArrayAdapter<String> get_gauge_adapter = new ArrayAdapter<String>(MapsActivity.this, android.R.layout.simple_spinner_item, dist_list); //simple_spinner_item- layout design of spinner  //https://stackoverflow.com/questions/36561828/what-is-arrayadapter read about array adapter
                    get_gauge_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    scr_district.setAdapter(get_gauge_adapter);                         //setting up the layout element                                     // true or false boolean value from api will be parsee


                }


            }

            @Override
            public void onFailure(Call<response_to_get_dis_list> call, Throwable t) {  //will get called if any server side issue is there
                //                Log.e(TAG, "onFailure: "  + t.getLocalizedMessage() );
                Toast.makeText(MapsActivity.this,
                        "SOMETHING WENT WRONG " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void get_ayush_type(String dis_name){     //taking selected statte var                                   ////post_data_method_by_jai this methode has been called in setOnItemSelectedListener in on create
        Call<response_to_get_ayush_type_list> todoPostCall = apiinterface.get_ayush_stream(dis_name);   //Rresponse_for_district=response type from api_interface  //todopostcall method has been used just in next line only
        todoPostCall.enqueue(new Callback<response_to_get_ayush_type_list>() {            //enqueue is a method from retrofi to call

            @Override
            public void onResponse(Call<response_to_get_ayush_type_list> call, Response<response_to_get_ayush_type_list> response)
            {
                //                      Log.e(TAG, "onResponse: "+ response.body() );

                if (response.body().getStatus()) {   //if get status is true


                    ArrayList<String> dist_list = new ArrayList<>();    // make an array to push recieved values
                    dist_list.add(" Select Ayush stream");
//                    temp.add("--select district--");               // the first spin value
//                    dist_list.add("select District");

                    for (int i = 0; i < response.body().getData().size(); i++) {
                        String div_list = response.body().getData().get(i).get_ayush_type();
                        Collections.sort(dist_list, new Comparator<String>() {
                            @Override
                            public int compare(String s1, String s2) {
                                return s1.compareToIgnoreCase(s2);
                            }
                        });
//                        if (dis_list == null) { holder.txtColor.setText("Alt text") } else { holder.txtColor.setText(p.getColorName())

                        dist_list.add(div_list);


                    }

                    //dist_list.add("Other");
//                    for (int i = 0; i < response.body().getData().size(); i++) {
//                        dist_list.add(response.body().getData().get(i).getBlockName());  // getDistrictList() is from response_for_district class
//                    }

                    ArrayAdapter<String> get_gauge_adapter = new ArrayAdapter<String>(MapsActivity.this, android.R.layout.simple_spinner_item, dist_list); //simple_spinner_item- layout design of spinner  //https://stackoverflow.com/questions/36561828/what-is-arrayadapter read about array adapter
                    get_gauge_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    scr_ayush_type.setAdapter(get_gauge_adapter);                         //setting up the layout element                                     // true or false boolean value from api will be parsee


                }


            }

            @Override
            public void onFailure(Call<response_to_get_ayush_type_list> call, Throwable t) {  //will get called if any server side issue is there
                //                Log.e(TAG, "onFailure: "  + t.getLocalizedMessage() );
                Toast.makeText(MapsActivity.this,
                        "SOMETHING WENT WRONG " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
    public void get_office_type_from_ayush_stram(String dis_name, String ayush_stream){     //taking selected statte var                                   ////post_data_method_by_jai this methode has been called in setOnItemSelectedListener in on create
        Call<response_to_get_office_type_from_ayush_stream_and_dis> todoPostCall = apiinterface.get_office_type(dis_name,ayush_stream);   //Rresponse_for_district=response type from api_interface  //todopostcall method has been used just in next line only
        todoPostCall.enqueue(new Callback<response_to_get_office_type_from_ayush_stream_and_dis>() {            //enqueue is a method from retrofi to call

            @Override
            public void onResponse(Call<response_to_get_office_type_from_ayush_stream_and_dis> call, Response<response_to_get_office_type_from_ayush_stream_and_dis> response)
            {
                //                      Log.e(TAG, "onResponse: "+ response.body() );

                if (response.body().getStatus()) {   //if get status is true


                    ArrayList<String> dist_list = new ArrayList<>();    // make an array to push recieved values
                    dist_list.add(" Select Office type");
//                    temp.add("--select district--");               // the first spin value
//                    dist_list.add("select District");

                    for (int i = 0; i < response.body().getData().size(); i++) {
                        String div_list = response.body().getData().get(i).getOfficeCategory();
                        Collections.sort(dist_list, new Comparator<String>() {
                            @Override
                            public int compare(String s1, String s2) {
                                return s1.compareToIgnoreCase(s2);
                            }
                        });
//                        if (dis_list == null) { holder.txtColor.setText("Alt text") } else { holder.txtColor.setText(p.getColorName())

                        dist_list.add(div_list);


                    }

                    //dist_list.add("Other");
//                    for (int i = 0; i < response.body().getData().size(); i++) {
//                        dist_list.add(response.body().getData().get(i).getBlockName());  // getDistrictList() is from response_for_district class
//                    }

                    ArrayAdapter<String> get_gauge_adapter = new ArrayAdapter<String>(MapsActivity.this, android.R.layout.simple_spinner_item, dist_list); //simple_spinner_item- layout design of spinner  //https://stackoverflow.com/questions/36561828/what-is-arrayadapter read about array adapter
                    get_gauge_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    scr_office_type.setAdapter(get_gauge_adapter);                         //setting up the layout element                                     // true or false boolean value from api will be parsee


                }


            }

            @Override
            public void onFailure(Call<response_to_get_office_type_from_ayush_stream_and_dis> call, Throwable t) {  //will get called if any server side issue is there
                //                Log.e(TAG, "onFailure: "  + t.getLocalizedMessage() );
                Toast.makeText(MapsActivity.this,
                        "SOMETHING WENT WRONG " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void get_office_names_list_from_dis_ayush_stram_office_type(String dis_name, String ayush_stream, String office_category){     //taking selected statte var                                   ////post_data_method_by_jai this methode has been called in setOnItemSelectedListener in on create
        Call<response_to_get_list_of_offices> todoPostCall = apiinterface.get_office_list(dis_name,ayush_stream,office_category);   //Rresponse_for_district=response type from api_interface  //todopostcall method has been used just in next line only
        todoPostCall.enqueue(new Callback<response_to_get_list_of_offices>() {            //enqueue is a method from retrofi to call

            @Override
            public void onResponse(Call<response_to_get_list_of_offices> call, Response<response_to_get_list_of_offices> response)
            {
                //                      Log.e(TAG, "onResponse: "+ response.body() );

                if (response.body().getStatus()) {   //if get status is true


                    ArrayList<String> dist_list = new ArrayList<>();    // make an array to push recieved values
                    dist_list.add(" Select Office type");
//                    temp.add("--select district--");               // the first spin value
//                    dist_list.add("select District");

                    for (int i = 0; i < response.body().getData().size(); i++) {
                        String div_list = response.body().getData().get(i).getOfficeName();
                        Collections.sort(dist_list, new Comparator<String>() {
                            @Override
                            public int compare(String s1, String s2) {
                                return s1.compareToIgnoreCase(s2);
                            }
                        });
//                        if (dis_list == null) { holder.txtColor.setText("Alt text") } else { holder.txtColor.setText(p.getColorName())

                        dist_list.add(div_list);


                    }

                    //dist_list.add("Other");
//                    for (int i = 0; i < response.body().getData().size(); i++) {
//                        dist_list.add(response.body().getData().get(i).getBlockName());  // getDistrictList() is from response_for_district class
//                    }

                    ArrayAdapter<String> get_gauge_adapter = new ArrayAdapter<String>(MapsActivity.this, android.R.layout.simple_spinner_item, dist_list); //simple_spinner_item- layout design of spinner  //https://stackoverflow.com/questions/36561828/what-is-arrayadapter read about array adapter
                    get_gauge_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    office_name.setAdapter(get_gauge_adapter);                         //setting up the layout element                                     // true or false boolean value from api will be parsee


                }


            }

            @Override
            public void onFailure(Call<response_to_get_list_of_offices> call, Throwable t) {  //will get called if any server side issue is there
                //                Log.e(TAG, "onFailure: "  + t.getLocalizedMessage() );
                Toast.makeText(MapsActivity.this,
                        "SOMETHING WENT WRONG " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

      }

