package com.example.fakecall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fakecall.Adapter.ViewPageAdapter;
import com.example.fakecall.Fagment.TabViewDataSingleton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FragmentMain extends AppCompatActivity {
    static TabLayout tablayout ;
    private ViewPager2 viewPager ;
    static Toolbar mtoolbar ;
    private ViewPageAdapter pageAdapter  ;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main3);
        tablayout = findViewById(R.id.tablayout) ;
        viewPager = findViewById(R.id.view_page) ;
        pageAdapter = new ViewPageAdapter(this) ;
        viewPager.setAdapter(pageAdapter);
        mtoolbar =  findViewById(R.id.toolbar) ;
        mtoolbar.setTitle("Fake Call");
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                if(tab.getPosition()==0)
//                {
//                }
                tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
       // setSupportActionBar(mtoolbar);
        new TabLayoutMediator(tablayout, viewPager, (tab, position) -> {
//            MenuItem item = (MenuItem) findViewById(R.id.delete);
//            item.setVisible(false);
            this.invalidateOptionsMenu() ;
            switch (position)
            {
                case 0 :
                    tab.setIcon(R.drawable.call);
                    tab.setText("home") ;
                    break;
                case 1:
                    tab.setIcon(R.drawable.book);
                    tab.setText("Character") ;
                    break ;
            }
        }) .attach();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu , menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId() ;
        switch (id){
            case 0 :
                Toast.makeText(this, "remote", Toast.LENGTH_SHORT).show();
            case 1 :
                Toast.makeText(this, "xoa", Toast.LENGTH_SHORT).show();
            default:
                break;
        }
        return  true ;
    }


}