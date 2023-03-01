package com.example.fakecall.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakecall.Activity.SoundActivity;
import com.example.fakecall.Employee;
import com.example.fakecall.ModelSound;
import com.example.fakecall.MyMediaPlayer;
import com.example.fakecall.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AdapterSound extends RecyclerView.Adapter<AdapterSound.ViewHodel> {
    private Context context;
    private List<ModelSound> list;
    static MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    static boolean check;
    int x = 0;
    private int checkedPosition = -1;
    public AdapterSound(Context context, List<ModelSound> arrayList) {
        this.context = context;
        this.list = arrayList;
    }

    public void Setemployees(ArrayList<ModelSound> modelSounds) {
        this.list = new ArrayList<>();
        this.list = modelSounds;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemsound, parent, false);
        return new AdapterSound.ViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodel holder, int position) {
        ModelSound modelSound = list.get(position);
        holder.bind();
        holder.name.setText(modelSound.getTitle());
        holder.kb.setText(convertToMMSS(modelSound.getDuration()));
        if (checkedPosition == position) {
            holder.name.setTextColor(Color.parseColor("#FF0000"));
        } else {
            holder.name.setTextColor(Color.parseColor("#000000"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(modelSound.getPath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    saveAudio(modelSound.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                saveName(modelSound.getTitle());
                //check = mediaPlayer.isPlaying();
                //notifyItemChanged(position);
                setsingselecttion(position);
            }
        });
    }
    public void setsingselecttion(int adapterpossition) {
       // notifyItemChanged(checkedPosition);
        checkedPosition = adapterpossition;
        //notifyItemChanged(checkedPosition);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public void saveAudio(String path) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("audio", path);
        editor.apply();
    }
    public static String convertToMMSS(String duration) {
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
    public static void pausePlay() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }
    public void saveName(String name) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nameAudio", name);
        editor.apply();

    }
    public class ViewHodel extends RecyclerView.ViewHolder {
        TextView name, kb;
        CardView cardView;
        ImageView imgitemsound;

        public ViewHodel(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            kb = itemView.findViewById(R.id.mb);
            cardView = itemView.findViewById(R.id.carviewSound);
            imgitemsound = itemView.findViewById(R.id.Img_item_sound);
        }

        public void bind( ) {
            if (checkedPosition == -1) {
                imgitemsound.setImageResource(R.drawable.play);
            } else {
                if (checkedPosition == getAdapterPosition()) {
                    imgitemsound.setImageResource(R.drawable.music);
                } else {
                    imgitemsound.setImageResource(R.drawable.play);
                }

            }

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    imgitemsound.setImageResource(R.drawable.play);
//                    if (checkedPosition != getAdapterPosition()) {
//                        notifyItemChanged(checkedPosition);
//                        checkedPosition = getAdapterPosition();
//                    }
//                }
//            });
        }
    }
}
