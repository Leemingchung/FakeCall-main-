package com.example.fakecall;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Images;
//import android.support.v4.content.CursorLoader;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.example.fakecall.Activity.ScheduleActivity;
import com.example.fakecall.Activity.SoundActivity;
import com.example.fakecall.DAO.CharacterDAO;
import com.example.fakecall.DAO.ModelCharacter;
import com.example.fakecall.Database.SQL;
import com.example.fakecall.Fagment.TabViewDataSingleton;
import com.google.android.material.tabs.TabLayout;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class FragmentHome extends Fragment implements IOnBackPressed {
    private static final int REQUEST_READ_PERMISSION = 766;
    private static final int REQUEST_WRITE_PERMISSION = 766;
    int ON_CHAR_CLICK = 2;
    int ON_CLICK;
    int ON_DIALOG_CLICK = 0;
    int ON_MORE_CLICK = 3;
    int ON_SHADLE_CLICK = 1;
    static ImageView callerImage;
    CustomDialog customDialog;
    int dialogId;
    static EditText nameEditText;
    static EditText phoneEditText;
    Button call, more;
    CircularImageView circularImageView;
    TextView ringtoneClick, Phonebook, soundClick, bookPhone;
    int picker;
    static CharacterDAO characterDAO;
    static ArrayList<ModelCharacter> list;
    SharedPreferences sharedPref;
    private static final int CONTACT_PERMISSION_CODE = 8;
    private static final int CONTACT_PICK_CODE = 9;
    private static final int CONTACT_PICK_FILE = 99;
    private static final int RESULT_OKE = -1;
    static CharAdapter adapter;
    static CharacterActivity characterActivity;
    //    private AdView mAdView;
//    AdRequest adRequestint;
    static ImageView img3;
    boolean isFirstRun;
    //
    TextView nameRing;
    MediaPlayer mediaPlayer;
    static TextView nameSound;

    public void onResume() {
        super.onResume();
        if (TabViewDataSingleton.getName()!= null&& TabViewDataSingleton.getSdt()!= null)
        {
            nameEditText.setText(TabViewDataSingleton.getName());
            phoneEditText.setText(TabViewDataSingleton.getSdt());

        }
        if (callerImage!= null)
        {

        if (TabViewDataSingleton.getImg()== null)
        {
            Glide.with(getContext())
                    .load(R.drawable.person)
                    .asBitmap()
                    .into(callerImage);
        }
        else {

            Glide.with(getContext())
                    .load(TabViewDataSingleton.getImg())
                    .asBitmap()
                    .into(callerImage);
        }
        }
        //setCaller();
    }
    void setCaller() {
        String name = sharedPref.getString("name", nameEditText.getText().toString());
        String phone = sharedPref.getString("number", phoneEditText.getText().toString());
        String image = sharedPref.getString("image", "");
        nameEditText.setText(name);
        phoneEditText.setText(phone);

    }
    public void rateUs() {
        final SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyPREFERENCES", 0);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext(), androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);//Theme_AppCompat_Light_Dialog_Alert
        alert.setTitle("Rate Us:");
        alert.setMessage(getString(R.string.rate_dialog_message));
        alert.setPositiveButton(getString(R.string.rate_dialog_ok), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getActivity().getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
                sharedpreferences.edit().putBoolean("rate", true).apply();
                dialog.dismiss();
                getActivity().finish();
            }
        });
        alert.setNegativeButton(getString(R.string.rate_dialog_no), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
                sharedpreferences.edit().putBoolean("rate", true).apply();
            }
        });
        alert.setNeutralButton(getString(R.string.rate_dialog_cancel), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
                sharedpreferences.edit().putBoolean("remind", true).apply();
            }
        });
        alert.create();
        alert.show();
    }

    public boolean onBackPressed() {
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyPREFERENCES", 0);
        boolean alreadyRated = sharedpreferences.getBoolean("rate", false);
        boolean remindMeLater = sharedpreferences.getBoolean("remind", false);
        if (!alreadyRated) {
            rateUs();
        } else if (alreadyRated && remindMeLater) {
            rateUs();
        } else {
            super.getActivity().onBackPressed();
        }
        return alreadyRated;
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onDestroy() {
        Log.d("j", "ondes: ");
        super.onDestroy();
    }

    public void loadEditext(String name, String phone) {
        nameEditText.setText(name);
        phoneEditText.setText(phone);
    }
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test, container, false);
        nameEditText = view.findViewById(R.id.caller_name);
        phoneEditText = view.findViewById(R.id.caller_number);
        callerImage = view.findViewById(R.id.caller_image);
        loadEditext(TabViewDataSingleton.getName(), TabViewDataSingleton.getSdt());
        nameSound = view.findViewById(R.id.nameSound);
        //
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String name = sharedPreferences.getString("nameAudio", "");
        nameSound.setText(name);
        //
        if (callerImage == null) {
            Glide.with(getContext())
                    .load(R.drawable.person)
                    .asBitmap()
                    .into(callerImage);
//        } else {
//            phoneEditText.setText(TabViewDataSingleton.getSdt());
//            Glide.with(getContext())
//                    .load(R.drawable.person)
//                    .asBitmap()
//                    .into(callerImage);
        }
        SQL sql = new SQL(getContext());
        SQLiteDatabase readableDatabase = sql.getReadableDatabase();
        readableDatabase.close();
        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getContext());
        //
        isFirstRun = sharedpref.getBoolean("FIRSTRUN", true);

        if (isFirstRun) {
            SharedPreferences.Editor editor = sharedpref.edit();
            editor.putBoolean("FIRSTRUN", false);
            editor.commit();
            Insert();
        }
        circularImageView = view.findViewById(R.id.caller_image);
        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CustomDialog cdd = new CustomDialog(getActivity(), 0);
                setDialog(cdd, 0);
                ON_CLICK = ON_DIALOG_CLICK;
                cdd.show();
                cdd.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        onDialogDismiss(cdd.buttonClick, 0);
                    }
                });
            }
        });
        sharedPref = getActivity().getSharedPreferences("file", 0);
//        more = view.findViewById(R.id.button3) ;
//        more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ON_CLICK = ON_MORE_CLICK;
//                Intent intent = new Intent("android.intent.action.VIEW");
//                intent.setData(Uri.parse("https://play.google.com/store/apps/dev?id="+getResources().getString(R.string.developer_id)));
//                startActivity(intent);
//            }
//        });
        //
        ringtoneClick = view.findViewById(R.id.textView_ringtone);
        ringtoneClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pic1();
            }
        });
        // call
        call = view.findViewById(R.id.button2);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ON_CLICK = ON_SHADLE_CLICK;
                Log.e("TAG", "name : "+ nameEditText.getText().toString());
                Log.e("TAG", "phone: "+ phoneEditText.getText().toString());

                if (nameEditText.getText().toString().trim().equals("") && phoneEditText.getText().toString().trim().equals(""))
                {
                    Toast.makeText(getContext(), "Bạn cần điền đầy đủ tên và số điện thoại  ", Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(getActivity(), ScheduleActivity.class));
                    getActivity().finish();
                }

            }
        });
        // character
        Phonebook = view.findViewById(R.id.textView_phoneBook);
        Phonebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ON_CLICK = ON_CHAR_CLICK;
                if (checkContectPermission()) {
                    pickContactInten();
                } else {
                    requestContectPermission();
                }
            }
        });
        //
        soundClick = view.findViewById(R.id.textView);
        nameRing = view.findViewById(R.id.nameRingtone);
        onClickRequestPermision();
        soundClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SoundActivity.class);
                        startActivity(intent);
            }
        });
        bookPhone = view.findViewById(R.id.bookphone);
        bookPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Intent intent = new Intent(getContext(), SelectSreen.class);
                startActivity(intent);
            }
        });

        //ADS
//        mAdView = (AdView) findViewById(R.id.banner_AdView);
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice("0224C93FFD644350DCD7F3D7557C6A5C").build();
//        mAdView.loadAd(adRequest);
        //onClickRequestPermision();
        if (ON_CLICK == ON_DIALOG_CLICK) {
            if (customDialog != null) {
                customDialog.show();
                customDialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        onDialogDismiss(customDialog.buttonClick, dialogId);
                    }
                });
            }
        } else if (ON_CLICK == ON_SHADLE_CLICK) {
            startActivity(new Intent(getContext(), ScheduleActivity.class));
            getActivity().finish();
        } else if (ON_CLICK == ON_MORE_CLICK) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse("https://play.google.com/store/apps/dev?id=" + getResources().getString(R.string.developer_id)));
            startActivity(intent);
            getActivity().finish();
        } else if (ON_CLICK == ON_CHAR_CLICK) {
            startActivityForResult(new Intent(getActivity(), CharacterActivity.class), 1);
        }
        nameEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (!sharedPref.getString("name", "").equals(nameEditText.getText().toString())) {
                    saveName(nameEditText.getText().toString());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        phoneEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (!sharedPref.getString("number", "").equals(phoneEditText.getText().toString())) {
                    savePhone(phoneEditText.getText().toString());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        setCaller();
        return view;
    }

    public static void loadTv(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String name = sharedPreferences.getString("nameAudio", "");
        nameSound.setText(name);
    }
    public static void saveData(Context context, String name, String number, byte[] bytes) {
        ModelCharacter character = new ModelCharacter();
        character.setName(nameEditText.getText().toString());
        String sdt = number;
        character.setName(name);
        character.setSdt(number);
        try {
            if (bytes== null)
            {
                Log.e("TAG", " null " );
            }
            else {
                Log.e("TAG", " not null " );
            }
            character.setHinhanh(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        characterDAO = new CharacterDAO(context);
        if (characterDAO.getsdt(sdt) == -1) {
            long kq = characterDAO.inserCharacter(character);
            if (kq > 0) {
                adapter = new CharAdapter(context, list);
                list = new ArrayList<>();
                list.addAll(characterDAO.getAll());
                list.clear();
                list.addAll(characterDAO.getAll());
                adapter.notifyDataSetChanged();
                characterActivity.Reset();
            }
            return;
        } else if (characterDAO.getsdt(sdt) > 0) {
            character.setName(name);
            try {
                character.setHinhanh(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
            adapter = new CharAdapter(context, list);
            characterDAO.update(character);
            list = new ArrayList<>();
            list.clear();
            list.addAll(characterDAO.getAll());
            adapter.notifyDataSetChanged();
            characterActivity.Reset();
            return;
        } else {
            Toast.makeText(context, " Thêm thất bại ", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveName(String name) {
        Editor editor = sharedPref.edit();
        editor.putString("name", name);
        editor.apply();
    }

    private void savePhone(String phone) {
        Editor editor = sharedPref.edit();
        editor.putString("number", phone);
        editor.apply();
    }

    private void saveImg(String img) {
        Editor editor = sharedPref.edit();
        editor.putString("image", img);
        editor.apply();
    }

    void setDialog(CustomDialog c, int dialogId) {
        this.customDialog = c;
        this.dialogId = dialogId;
    }

//    public void moreAppsClick(View v) {
//        ON_CLICK = ON_MORE_CLICK;
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.setData(Uri.parse("https://play.google.com/store/apps/dev?id="+getResources().getString(R.string.developer_id)));
//        startActivity(intent);
//    }

    public void onDialogDismiss(int button, int id) {
        Editor editor;
        if (id == 1) {
            Toast.makeText(getContext(), "button :   " + button, Toast.LENGTH_SHORT).show();
            switch (button) {
                case 0:
                    picker = 0;
                    requestPermission();
                    return;
                case 1:
                    editor = sharedPref.edit();
                    editor.putString("audio", "");
                    editor.apply();
                    if (!sharedPref.getString("audio", "").equals("")) {
                        return;
                    }
                    return;
                case 2:
                    picker = 3;
                    try {
                        RecordDialog recordDialog = new RecordDialog(getContext());
                        recordDialog.show();
                        recordDialog.setOnDismissListener(new OnDismissListener() {
                            public void onDismiss(DialogInterface dialog) {
                                if (!sharedPref.getString("audio", "").equals("")) {
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    requestPermission();
                    return;
                default:
            }
        } else if (id == 0) {
            switch (button) {
                case 0:
                    picker = 1;
                    requestPermission();
                    return;
                case 1:
                    saveImg("");
                    callerImage.setImageResource(R.drawable.person);
                    return;
                case 2:
                    startActivityForResult(new Intent(getActivity(), CharacterActivity.class), 1);
                    TabLayout.Tab tabLayout = FragmentMain.tablayout.getTabAt(1);
                    tabLayout.select();
                    return;
            }
        } else if (id == 2) {
            editor = sharedPref.edit();
            switch (button) {
                case 0:
                    picker = 2;
                    requestPermission();
                    return;
                case 1:
                    editor.putString("ring", "");
                    editor.commit();
                    return;
                default:
            }
        }
    }

    public void Insert() {
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundsamsaung, null);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.gallery_btn_2, null);
        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.gallery_btn_4, null);
        ModelCharacter modelCharacter = new ModelCharacter("Chung", "0388916123", CovertIMG.getBytes(bitmap1));
        ModelCharacter modelCharacter1 = new ModelCharacter("Mamy", "0382365478", CovertIMG.getBytes(bitmap2));
        ModelCharacter modelCharacter2 = new ModelCharacter("Papa", "99999", CovertIMG.getBytes(bitmap3));
        characterDAO = new CharacterDAO(this.getContext());
        if (characterDAO.getsdt(modelCharacter.getSdt()) < 0) {
            characterDAO.inserCharacter(modelCharacter);
            characterDAO.inserCharacter(modelCharacter1);
            characterDAO.inserCharacter(modelCharacter2);
        } else return;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == -1) {
            if (data != null) {
                Uri uri = data.getData();
                String pathhhh = uri.getPath();
//                String ring = getRealPathFromURI(data.getData());
//                Editor editor = sharedPref.edit();
//                editor.putString("ring", ring);
//                editor.apply();
//                createMediaPlayer(uri);
                //
                Editor editor = sharedPref.edit();
                editor.putString("audio", String.valueOf(uri));
                editor.apply();
                editor.putString("ring", pathhhh);
                Log.e("Ring", "onActivityResult: " + pathhhh);
                editor.apply();

                createMediaPlayer(uri);
            }
        }
        //
        if (resultCode == -1) {
            switch (requestCode) {
                case CONTACT_PICK_CODE:
                    contactPicked(data);
                    break;
            }
        }
        if (requestCode == 1) {
            if (resultCode == -1) {
                String name = data.getStringExtra("name");
                String number = data.getStringExtra("number");
                String img = data.getStringExtra("img");
                saveName(name);
                saveImg(img);

                savePhone(number);
            }
        } else if (requestCode == 2) {
            if (resultCode == -1) {
                String audio = getRealPathFromURI(data.getData());
                Editor editor = sharedPref.edit();
                editor.putString("audio", audio);
                editor.apply();
                if (!sharedPref.getString("audio", "").equals("")) {
                }
            }
        } else if (requestCode == 3) {
            if (resultCode == -1) {
                performCrop(data.getData());
            }
        } else if (requestCode == -1) {
            if (resultCode == -1) {
                String ring = getRealPathFromURI(data.getData());
                Editor editor = sharedPref.edit();
                editor.putString("ring", ring);
                editor.apply();


                final CustomDialog cdd = new CustomDialog(getContext(), 2);
                setDialog(cdd, 2);
                ON_CLICK = ON_DIALOG_CLICK;
                cdd.dismiss();
                cdd.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        onDialogDismiss(cdd.buttonClick, 2);
                    }
                });
            }
        } else if (requestCode == 5 && resultCode == -1) {
            saveImg(Environment.getExternalStorageDirectory() + "/Image-Caller.jpg");
        }
        //
        if (resultCode == RESULT_OKE && requestCode > 0) {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                //callerImage.setImageBitmap(bitmap);
//                Glide.with(getContext())
//                        .load(bitmap)
//                        .asBitmap()
//                        .into(callerImage) ;
                if (bitmap == null) {
                    return;
                } else {
                    TabViewDataSingleton.setImg(CovertIMG.getBytes(bitmap));
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    @SuppressLint("Range")
    public String getNameFromUri(Uri uri) {
        String fileName = "";
        Cursor cursor = null;
        cursor = getContext().getContentResolver().query(uri, new String[]{
                MediaStore.Images.ImageColumns.DISPLAY_NAME
        }, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
        }
        if (cursor != null) {
            cursor.close();
        }
        return fileName;
    }

    //
    @SuppressLint("Range")
    public String getRingFromUri(Uri uri) {
        String fileName = "";
        Cursor cursor = null;
        cursor = getContext().getContentResolver().query(uri, new String[]{
                Images.ImageColumns.RELATIVE_PATH
        }, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.RELATIVE_PATH));
        }
        if (cursor != null) {
            cursor.close();
        }
        return fileName;
    }
    private String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = new CursorLoader(getContext(), contentUri, new String[]{"_data"}, null, null, null).loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
       super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == REQUEST_READ_PERMISSION && grantResults[0] == 0) {
                pick();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Log.e("TAG", "grantResults: "+ grantResults[0] +"requestCode"+requestCode );
        if (requestCode == 786 && grantResults[0] == 0) {
            try {
                RecordDialog recordDialog = new RecordDialog(getContext());
                recordDialog.show();
                recordDialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        if (!sharedPref.getString("audio", "").equals("")) {
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CONTACT_PERMISSION_CODE)

        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            pickContactInten();
        }
        else {
            Toast.makeText(getContext(), "Permisson denied", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isExternalStorageWritable() {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }
    public void createMediaPlayer(Uri uri) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        try {
            mediaPlayer.setDataSource(getContext(), uri);
            mediaPlayer.prepare();
            int millis = mediaPlayer.getDuration();
            long total_secs = TimeUnit.SECONDS.convert(millis, TimeUnit.MILLISECONDS);
            long mins = TimeUnit.MINUTES.convert(total_secs, TimeUnit.SECONDS);
            long secs = total_secs - (mins * 60);
//            //
            Editor editor = sharedPref.edit();
            editor.putString("ringnew", uri.toString());
            Log.e("TAG", "uri: "+ uri );
            nameRing.setText(getNameFromUri(uri));
            editor.apply();
            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor1 = sharedPreferences.edit();
            editor1.putString("ringnew", uri.toString());
            editor1.apply();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // releaseMediaPlayer();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void pick() {
        if (picker == 0) {

            pic1();
        } else if (picker == 1) {
            pickImage();
        } else if (picker == 2) {
            pickRing();
        } else {
            requestPermissionMIC();
        }
    }
    void pickRing() {
        Intent intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
        Log.e("ring", "pickRing: " + intent.resolveActivity(getActivity().getPackageManager()));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                startActivityForResult(intent, 4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                startActivityForResult(intent, 99);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(getActivity(), "No app found!", Toast.LENGTH_LONG).show();
        }
    }
    void pic1() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, 99);
    }
    void pickAudio() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                startActivityForResult(intent, 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                startActivityForResult(intent, 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    void pickImage() {
        Intent intent = new Intent("android.intent.action.PICK", Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                startActivityForResult(intent, 3);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                startActivityForResult(intent, 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(getActivity(), "No app found!", Toast.LENGTH_LONG).show();
        }
    }

    private void requestPermission() {
        if (VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, REQUEST_WRITE_PERMISSION);
            return;
        }
        pick();
    }

    private void requestPermissionMIC() {
        if (VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 766);
            return;
        }
        RecordDialog recordDialog = new RecordDialog(getActivity());
        recordDialog.show();
        recordDialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                if (!sharedPref.getString("audio", "").equals("")) {
                }
            }
        });
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 800);
            cropIntent.putExtra("outputY", 800);
            File f = new File(Environment.getExternalStorageDirectory(), "/Image-Caller.jpg");
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Log.e("io", ex.getMessage());
            }
            cropIntent.putExtra("output", Uri.fromFile(f));
            startActivityForResult(cropIntent, 5);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "Whoops - your device doesn't support the crop action!", Toast.LENGTH_LONG).show();
        }
    }

    void onClickRequestPermision() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            return;
        }
        if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permission, REQUEST_WRITE_PERMISSION);
        }
        //
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, REQUEST_READ_PERMISSION);
        }
    }

    private boolean checkContectPermission() {
        boolean resul = ContextCompat.checkSelfPermission(
                getContext(),
                Manifest.permission.READ_CONTACTS) == (PackageManager.PERMISSION_GRANTED
        );
        return resul;
    }

    private void requestContectPermission() {
        String[] permission = {Manifest.permission.READ_CONTACTS};
        ActivityCompat.requestPermissions(getActivity(), permission, CONTACT_PERMISSION_CODE);
    }

    private void pickContactInten() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, CONTACT_PICK_CODE);
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null;
            String phoneNamee = null;
            Uri uri = data.getData();
            cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            Log.e("TAG3", "" + ContactsContract.CommonDataKinds.Phone.NUMBER);
            String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            int hasPhoneIdx = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
            String hasPhone = cursor.getString(hasPhoneIdx);

            if (hasPhone.equalsIgnoreCase("1")) {
                Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                phones.moveToFirst();
                int numberIdx = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String cNumber = phones.getString(numberIdx);
                // phoneEditText.setText (cNumber);
                TabViewDataSingleton.setSdt(cNumber);
            }
            int phoneName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNamee = cursor.getString(phoneName);
            TabViewDataSingleton.setName(phoneNamee);
            //
            Log.e("TAG", "contactPicked: " + phoneNamee);
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNo = cursor.getString(phoneIndex);
            phoneEditText.setText(phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    public static byte[] ImageView_To_Byte(ImageView h) {
        BitmapDrawable drawable = (BitmapDrawable) h.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

}