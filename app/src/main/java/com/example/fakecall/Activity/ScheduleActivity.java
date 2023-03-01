package com.example.fakecall.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fakecall.AlarmReceiver;
import com.example.fakecall.CovertIMG;
import com.example.fakecall.Fagment.TabViewDataSingleton;
import com.example.fakecall.FragmentHome;
import com.example.fakecall.FragmentMain;
import com.example.fakecall.R;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;


public class ScheduleActivity extends AppCompatActivity {
    private PendingIntent alarmIntent;
    private AlarmManager alarmMgr;
    SharedPreferences sharedPref;
   // private AdView mAdView;
   // AdRequest adRequestint;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        //ADS
//        mAdView = (AdView) findViewById(R.id.banner_AdView);
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice("0224C93FFD644350DCD7F3D7557C6A5C").build();
//        mAdView.loadAd(adRequest);
        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, AlarmReceiver.class), PendingIntent.FLAG_IMMUTABLE);
        sharedPref = getSharedPreferences("file", 0);
    }

    public void onButtonClick(View view) {
        int timer = 0;
        String time = "0";
        switch (view.getId()) {
            case R.id._now:
                time = "0 Sec";
                timer = 0;
                break;
            case R.id._10s:
                timer = 10;
                time = "10 Sec";
                break;
            case R.id._30s:
                time = "30 Sec";
                timer = 30;
                break;
            case R.id._01m:
                time = "1 Min";
                timer = 60;
                break;
            case R.id._5m:
                time = "5 Min";
                timer = 300;
                break;
            case R.id._15m:
                time = "15 Min";
                timer = 900;
                break;
            case R.id._30m:
                time = "30 Min";
                break;
        }
        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + ((long) (timer * 1000)), alarmIntent);
        ImageView abc =  null;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + ((long) (timer * 1000)), alarmIntent);
//        }
        if (alarmIntent!= null&&alarmMgr!= null)
        {
            String name = sharedPref.getString("name", "");
            String phone = sharedPref.getString("number", "");
//            byte[] img = new byte[0];
////            if (img!= null)
////            {
//                img = TabViewDataSingleton.getImg() ;
            //}

            FragmentHome.saveData(getBaseContext() ,name , phone, TabViewDataSingleton.getImg() );
        }
            Toast.makeText(this, "Call timer Set to: " + time, Toast.LENGTH_LONG).show();
        finish();
    }
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplication(), FragmentMain.class));
        finish();
    }
}
