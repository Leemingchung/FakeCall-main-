package com.example.fakecall.Adapter;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.fakecall.ModelScreen;
import com.example.fakecall.ModelSound;
import com.example.fakecall.MyIntdexGrid;
import com.example.fakecall.MyMediaPlayer;
import com.example.fakecall.R;
import com.example.fakecall.SelectSreen;

import java.util.List;

public class GridAdapter extends BaseAdapter {
    private  int selectposition = -1 ;
    Context context ;
    List<ModelScreen> list;
    public static int idlayout ;
    int position ;
    private  int checkedPosition =-1 ;
    public GridAdapter(Context context, List<ModelScreen> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder ;
        final ModelScreen model = list.get(i);
        MyIntdexGrid.getInstance().reset();
        MyIntdexGrid.currentIndex = i;
        if (view == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item, null);
            viewHolder.textView = view.findViewById(R.id.nameItem);
            viewHolder.imageView = view.findViewById(R.id.imageItem);
            viewHolder.done = view.findViewById(R.id.done);
            viewHolder.cardView = view.findViewById(R.id.carviewScreen) ;
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(model.getName());
        viewHolder.imageView.setImageResource(model.getImageView());
//        if (checkedPosition == position)
//        {
//
//            viewHolder.done.setVisibility(View.VISIBLE);
//        }
//        else {
//
//            viewHolder.done.setVisibility(View.GONE);
//        }
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idlayout = i ;
                Toast.makeText(context, "Bạn đã chọn màn hình " + model.getName(), Toast.LENGTH_SHORT).show();
                //setsingselecttion(i);
            }
        });
        return  view ;
    }
    public void setsingselecttion(int adapterpossition)
    {
        checkedPosition = adapterpossition ;
        notifyDataSetChanged();
    }
    public class ViewHolder {
        TextView textView;
        ImageView imageView;
        ImageView done ;
        CardView cardView ;
    }
}
