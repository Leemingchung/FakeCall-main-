package com.example.fakecall.Activity;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fakecall.Adapter.AdapterSound;
import com.example.fakecall.FragmentHome;
import com.example.fakecall.ModelSound;
import com.example.fakecall.R;
import com.example.fakecall.RecordDialog;
import com.example.fakecall.SelectSreen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SoundActivity extends AppCompatActivity {
    FloatingActionButton btnFloat ;
    TextView cancle ;
    TextView stop  ;
    TextView play ;
    Chronometer cmTimer;
    Boolean resume = false;
    long elapsedTime;
    SharedPreferences sharedPref;
    private  static   int MICROPHONE_PERMISSION_CODE = 200 ;

    private MediaPlayer mPlayer = null;
    private MediaRecorder mRecorder = null;
    public String path;

    boolean playStarted = false;

    boolean recordStarted = false;
    private  String fileName = "/Download/callervoice.3gp" ;
    public Context c;
    public static final int PERMIT = 1 ;
    ContentResolver contentResolver ;
    Cursor cursor ;
    Context context ;
    Uri uri ;
    static List<ModelSound> list = new ArrayList<>();
    static AdapterSound adapter ;
    static RecyclerView recyclerView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);
        btnFloat = findViewById(R.id.floatingSound) ;
        recyclerView = findViewById(R.id.recicleview) ;
        context = getApplicationContext() ;


        list = new ArrayList<>() ;
        getAudio();
        getpermit() ;
        adapter = new AdapterSound(context , list) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this , LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        btnFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogLogin();
                if (isMicrophonePresent())
                {
                    getMicrophonePermisstion();
                }
            }
        });
    }
    private void onRecord(boolean started) {
        Toast.makeText(getApplicationContext(),"on record:"+ started, Toast.LENGTH_SHORT).show();
        if (started) {
            stopRecording();
        } else {
            startRecording();
        }
    }
    public  void DialogLogin()
    {
        Dialog dialog = new Dialog(this) ;
        dialog.setContentView(R.layout.dialogrecord);
        cancle = dialog.findViewById(R.id.tv_cancel) ;
        stop = dialog.findViewById(R.id.tv_stop);
        play = dialog.findViewById(R.id.play) ;

        cmTimer = (Chronometer) dialog.findViewById(R.id.cmTimer);
        cmTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer arg0) {
                if (!resume) {
                    long minutes = ((SystemClock.elapsedRealtime() - cmTimer.getBase())/1000) / 60;
                    long seconds = ((SystemClock.elapsedRealtime() - cmTimer.getBase())/1000) % 60;
                    elapsedTime = SystemClock.elapsedRealtime();
                    Log.d(TAG, "onChronometerTick: " + minutes + " : " + seconds);
                } else {
                    long minutes = ((elapsedTime - cmTimer.getBase())/1000) / 60;
                    long seconds = ((elapsedTime - cmTimer.getBase())/1000) % 60;
                    elapsedTime = elapsedTime + 1000;
                    Log.d(TAG, "onChronometerTick: " + minutes + " : " + seconds);
                }
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onPlay(true);
                onRecord(true);
                dialog.dismiss();
                return;
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(true) ;
                onRecord(recordStarted);
               cmTimer.start();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                cmTimer.stop();
                onRecord(true);
                copyFile();
                dialog.dismiss();
            }
        });

         dialog.show();
    }
    public  boolean isMicrophonePresent()
    {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            return true ;
        }
        else {
            return false ;
        }
    }
    public  void getMicrophonePermisstion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE);
        }
    }

    private void startRecording() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
        File f = new File(path);
        File file[] = f.listFiles();
        recordStarted = true;
        mRecorder = new MediaRecorder();
        File audioFolder = new File(Environment.getExternalStorageDirectory() + "/Download",
                "newfolder");
        if (!audioFolder.exists()) {
            boolean success = audioFolder.mkdir();
            if (success) {
                Toast.makeText(getApplicationContext(), " new folder  ", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), " new folder 123  ", Toast.LENGTH_SHORT).show();
            }
        }
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(Environment.getExternalStorageDirectory() + "/temp.3gp");
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
            mRecorder.start();

            Toast.makeText(getApplicationContext(), " đang ghi âm  ", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), " đang ghi âm  " + e, Toast.LENGTH_SHORT).show();
            Log.e("APP", "prepare() failed");
        }
    }

    private void stopRecording() {
        recordStarted = false;
        try {
            if (mRecorder != null) {
                mRecorder.release();
                Toast.makeText(getApplicationContext(), "đã dừng ", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
        }
    }
    private void startPlaying() {
        playStarted = true;

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath() +  "/temp.3gp")   ;
            Toast.makeText(c, " " + Environment.getExternalStorageDirectory().getAbsolutePath() +  "/temp.3gp", Toast.LENGTH_SHORT).show();
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("APP", "prepare() failed");
            //Toast.makeText(c, " bắt đầu ghi âm "+e, Toast.LENGTH_SHORT).show();
        }
    }
    public void onBackPressed() {
        onRecord(true);
        AdapterSound.pausePlay();
        FragmentHome.loadTv(getApplicationContext());
        super.onBackPressed();
        loadLisview(context);
    }
    private void copyFile() {
        FileNotFoundException fnfe1;
        InputStream inputStream;
        Exception e;
        OutputStream outputStream;
        try {
            OutputStream out = null;
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + fileName);
            InputStream in = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() +  "/temp.3gp");
            try {
                out = new FileOutputStream(dir);
            } catch (FileNotFoundException e2) {
                fnfe1 = e2;
                inputStream = in;
                Log.e("tag", fnfe1.getMessage());
                Toast.makeText(c, "falid e2"+fnfe1.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e3) {
                e = e3;
                inputStream = in;
                Log.e("tag", e.getMessage());
                Toast.makeText(c, "falid e3"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    int read = in.read(buffer);
                    if (read != -1) {
                        out.write(buffer, 0, read);
                    } else {
                        in.close();
                        try {
                            out.flush();
                            out.close();
                            new File(Environment.getExternalStorageDirectory().getAbsolutePath() +  "/temp.3gp");
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("audio", Environment.getExternalStorageDirectory().getAbsolutePath() + fileName);
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Recorded Audio set to caller Voice", Toast.LENGTH_SHORT).show();
                            return;
                        } catch (FileNotFoundException e4) {
                            fnfe1 = e4;
                            outputStream = out;
                            Log.e("tag", fnfe1.getMessage());
                            Toast.makeText(getApplicationContext(), "falid 34"+fnfe1.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e5) {
                            e = e5;
                            outputStream = out;
                            Log.e("tag", e.getMessage());
                            Toast.makeText(c, "falid catch e5"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (FileNotFoundException e6) {
                fnfe1 = e6;
                outputStream = out;
                inputStream = in;
                Log.e("tag", fnfe1.getMessage());
                Toast.makeText(getApplicationContext(), "falid e6"+fnfe1.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (Exception e7) {
                e = e7;
                outputStream = out;
                inputStream = in;
                Log.e("tag", e.getMessage());
                Toast.makeText(getApplicationContext(), "falid e7"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e8) {
            fnfe1 = e8;
            Log.e("tag", fnfe1.getMessage());
            Toast.makeText(getApplicationContext(), "falid e8"+fnfe1.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e9) {
            e = e9;
            Log.e("tag", e.getMessage());
            Toast.makeText(getApplicationContext(), "falid e9"+e9.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void getAudio()
    {
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC +" != 0";
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,null);
        while(cursor.moveToNext()){
            ModelSound songData = new ModelSound(cursor.getString(1),cursor.getString(0),cursor.getString(2));
            if(new File(songData.getPath()).exists())
                list.add(songData);
        }
    }
    private void getpermit()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    AlertDialog.Builder alertb = new AlertDialog.Builder(SoundActivity.this) ;
                    alertb.setTitle("PLZ G` ") ;
                    alertb.setPositiveButton("oke", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(SoundActivity.this , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , PERMIT);

                        }
                    }) ;

                    alertb.setNegativeButton("cancel" , null) ;
                    AlertDialog alertDialog = alertb.create() ;
                    alertDialog.show();
                }
                else {
                    ActivityCompat.requestPermissions(SoundActivity.this , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , PERMIT);
                }
            }
        }
    }
    public static void loadLisview(Context context )
    {
        adapter = new AdapterSound(context , list) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case PERMIT:
            {
                if (grantResults.length > 0 && grantResults[0]  == PackageManager .PERMISSION_GRANTED )
                {

                }
            }
        }
    }
}