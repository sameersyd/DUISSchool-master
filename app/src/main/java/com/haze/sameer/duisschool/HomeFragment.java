package com.haze.sameer.duisschool;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener{


    public HomeFragment() {

    }
    List<Product> productList;
    RecyclerView recyclerView;
    TextView stuName,stuClass;

    Activity a;
    int pos,user_id;

    LinearLayout attendBtn,messageBtn,profileBtn,marksBtn,trackBtn,feesBtn;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            a=(Activity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home , container, false);
        AndroidNetworking.initialize(view.getContext().getApplicationContext());

        SharedPreferences prefs = this.getActivity().getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE);
        user_id = prefs.getInt("user_id",0);

        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_home_actRecycler);
        attendBtn = (LinearLayout)view.findViewById(R.id.fragment_home_attendBtn);
        messageBtn = (LinearLayout)view.findViewById(R.id.fragment_home_messageBtn);
        profileBtn = (LinearLayout)view.findViewById(R.id.fragment_home_profileBtn);
        marksBtn = (LinearLayout)view.findViewById(R.id.fragment_home_marksBtn);
        trackBtn = (LinearLayout)view.findViewById(R.id.fragment_home_trackBtn);
        feesBtn = (LinearLayout)view.findViewById(R.id.fragment_home_feesBtn);
        stuName = (TextView) view.findViewById(R.id.fragment_home_studentName);
        stuClass = (TextView) view.findViewById(R.id.fragment_home_studentClass);

        final Dialog loadDialog = new Dialog(view.getContext());
        loadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadDialog.setContentView(R.layout.loading_one);
        loadDialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK)
                    loadDialog.dismiss();
                return true;
            }
        });
        LottieAnimationView animSelect;
        animSelect = (LottieAnimationView) loadDialog.findViewById(R.id.loading_one);
        animSelect.setAnimation("blueline.json");
        animSelect.playAnimation();
        animSelect.loop(true);

        Window window = loadDialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loadDialog.show();

        AndroidNetworking.get("http://roots.atlasglobalsys.com/api/getResponse/profile/"+user_id)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject rootObject = new JSONObject(response);
                            JSONObject data = rootObject.getJSONObject("data");
                            JSONObject student = data.getJSONObject("student");
                            stuName.setText(student.getString("firstname") + " " + student.getString("lastname"));
                            stuClass.setText(student.getString("class") + " - " + student.getString("section"));
                            loadDialog.dismiss();
                        } catch (JSONException e) {
                            loadDialog.dismiss();
                            Toast.makeText(view.getContext(), e + "", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        loadDialog.dismiss();
                        Toast.makeText(view.getContext(), anError + "", Toast.LENGTH_SHORT).show();
                    }
                });

        attendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = 0;
                ((HomeActivity)a).performStreamClick(pos);
            }
        });
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = 1;
                ((HomeActivity)a).performStreamClick(pos);
            }
        });
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = 2;
                ((HomeActivity)a).performStreamClick(pos);
            }
        });
        marksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = 3;
                ((HomeActivity)a).performStreamClick(pos);
            }
        });
        trackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(getContext(),TrackActivity.class));
            }
        });
        feesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(getContext(),FeesActivity.class));
            }
        });

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        //initializing the productlist
        productList = new ArrayList<>();
        //adding some items to our list
        productList.add(new Product("Karate", R.drawable.act_karate));
        productList.add(new Product("Yoga", R.drawable.act_yoga));
        productList.add(new Product("Dance", R.drawable.act_bharathanatiyam));
        productList.add(new Product("Drawing", R.drawable.act_drawing));
        productList.add(new Product("Music", R.drawable.act_music));
        productList.add(new Product("Keyboard", R.drawable.act_keyboard));
        //creating recyclerview adapter
        ProductAdapter adapter = new ProductAdapter(getContext(), productList);
        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View view) {
        ((HomeActivity)a).performStreamClick(pos);
    }

}
