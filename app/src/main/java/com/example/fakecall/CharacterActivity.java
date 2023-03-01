package com.example.fakecall;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.techx.fakecallprank.Charactor;

import com.example.fakecall.DAO.CharacterDAO;
import com.example.fakecall.DAO.ModelCharacter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class CharacterActivity extends Fragment {
    static final /* synthetic */ boolean $assertionsDisabled = (!CharacterActivity.class.desiredAssertionStatus());
    static CharAdapter adapter;
    static ArrayList<ModelCharacter> list;
    static Intent returnIntent;
    static CharacterDAO characterDAO;
    //dView mAdView;
    static ListView listView;
    FloatingActionButton floatingActionButton ;

    public boolean onBackPressed() {
        //  super.onBackPressed();
        getActivity().setResult(0, returnIntent);
        //  getActivity().finish();
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_character, container, false);
        //ADS
        //mAdView = (AdView) findViewById(R.id.banner_AdView);
        // AdRequest adRequest = new AdRequest.Builder().addTestDevice("0224C93FFD644350DCD7F3D7557C6A5C").build();
        //mAdView.loadAd(adRequest);

        //
        listView = view.findViewById(R.id.list_view);
        floatingActionButton = view.findViewById(R.id.floating) ;
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int kq =   characterDAO.delete() ;
                Log.e("TAG", " đã delete" );
                Toast.makeText(getContext(), ""+kq, Toast.LENGTH_SHORT).show();
               if (kq>0)
               {
                   list.clear();
                   list.addAll(characterDAO.getAll()) ;
                   adapter.notifyDataSetChanged();
                   Toast.makeText( getContext() , " Xóa thành công " , Toast.LENGTH_SHORT).show();
               }
                TabLayout.Tab tabLayout = FragmentMain.tablayout.getTabAt(0) ;
                tabLayout.select();
            }
        });
        characterDAO = new CharacterDAO(getContext());
        list = new ArrayList<>();
        list = characterDAO.getAll();
        adapter = new CharAdapter(getContext(), list);
        listView.setAdapter(adapter);
        if ($assertionsDisabled || listView != null) {
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    characterClick(position);
                }
            });
            returnIntent = new Intent();
            return view;
        }
        throw new AssertionError();
    }


    public static  void Reset()
    {
        try {
            characterDAO = new CharacterDAO(adapter.context);
            characterDAO = new CharacterDAO(listView.getContext());
            list = new ArrayList<>() ;
            list = characterDAO.getAll() ;
            adapter = new CharAdapter(adapter.context, list) ;
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void  characterClick(int pos) {
        Toast.makeText(getContext(), " có chạy vào ", Toast.LENGTH_SHORT).show();
        ModelCharacter modelCharacter = list.get(pos);
        Bundle bundle = new Bundle();
        bundle.putString("name", modelCharacter.getName());
        bundle.putString("number", modelCharacter.getSdt());
        bundle.putByteArray("img", modelCharacter.getHinhanh());
        getParentFragmentManager().setFragmentResult("data1", bundle);
        TabLayout.Tab tabLayout = FragmentMain.tablayout.getTabAt(0) ;
        tabLayout.select();
    }


}