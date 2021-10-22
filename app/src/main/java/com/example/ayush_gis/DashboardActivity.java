package com.example.ayush_gis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    CardView cv_ayush;
    CardView cv_dbt;
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private long fileSize = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        cv_ayush = (CardView) findViewById( R.id.ayush_cardView ); // creating a CardView and assigning a value.
        cv_dbt = (CardView) findViewById( R.id.dbt_cardView ); // creating a CardView and assigning a value.

        cv_ayush.setOnClickListener( this );
        cv_dbt.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        Intent i;

        if (v.getId() == R.id.ayush_cardView) {
            i = new Intent( this, MapsActivity.class );
            startActivity( i );
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
        if (v.getId() == R.id.dbt_cardView) {
            i = new Intent( this, DBTActivity.class );
            startActivity( i );
            progressBar = new ProgressDialog(v.getContext());
            progressBar.setCancelable(true);
            progressBar.setMessage("Please Wait ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();
            //reset progress bar and filesize status
            progressBarStatus = 0;
            fileSize = 0;

        }
    }
}