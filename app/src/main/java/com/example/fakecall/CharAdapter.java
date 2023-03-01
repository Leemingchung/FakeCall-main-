package com.example.fakecall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.fakecall.DAO.ModelCharacter;
import com.example.fakecall.Fagment.TabViewDataSingleton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class CharAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    ArrayList<ModelCharacter> categroyArrayList;
    Context context;
    static  boolean check= false ;
    public class Holder {
        ImageView img;
        TextView tv;
        TextView tv2;
        CardView carview ;
    }

    public CharAdapter(Context mainActivity, ArrayList<ModelCharacter> categroyArrayList) {
        this.categroyArrayList = categroyArrayList;
        this.context = mainActivity;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return this.categroyArrayList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        View rowView = null;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.character_row, null);
            holder = new Holder();
            holder.tv = (TextView) rowView.findViewById(R.id.text_row);
            holder.tv2 = (TextView) rowView.findViewById(R.id.text_row2);
            holder.img = (ImageView) rowView.findViewById(R.id.img_row);
            holder.carview = (CardView) rowView.findViewById(R.id.carview) ;
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }
        holder.tv.setText((categroyArrayList.get(position)).getName());
        holder.tv2.setText((categroyArrayList.get(position)).getSdt());
            Glide.with(context).load(categroyArrayList.get(position).getHinhanh())
                    .asBitmap()
                    .into(holder.img) ;
        holder.carview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = categroyArrayList.get(position).getName() ;
                String sdt = categroyArrayList.get(position).getSdt() ;
                byte[] img = categroyArrayList.get(position).getHinhanh() ;
                TabViewDataSingleton.setName(name);
                TabViewDataSingleton.setSdt(sdt);
                TabViewDataSingleton.setImg(img);
                TabLayout.Tab tabLayout = FragmentMain.tablayout.getTabAt(0) ;
                tabLayout.select();

            }
        });
        return rowView;
    }
//    public void characterClick(int pos) {
//
//        ModelCharacter modelCharacter = categroyArrayList.get(pos);
//        Bundle bundle = new Bundle();
//        bundle.putString("name","chung");
////        bundle.putString("name", modelCharacter.getName());
////        bundle.putString("number", modelCharacter.getSdt());
////        bundle.putByteArray("img", modelCharacter.getHinhanh());
//       FragmentMain fragmentMain = new FragmentMain() ;
//       //fragmentMain.setAb
//        TabLayout.Tab tabLayout = FragmentMain.tablayout.getTabAt(0) ;
//        tabLayout.select();
//    }
}
