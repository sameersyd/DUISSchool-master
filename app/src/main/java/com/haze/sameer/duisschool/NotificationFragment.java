package com.haze.sameer.duisschool;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {


    public NotificationFragment() {
        // Required empty public constructor
    }

    List<NotificationObject> notiList;
    RecyclerView recyclerView;
    int user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        AndroidNetworking.initialize(view.getContext().getApplicationContext());

        SharedPreferences prefs = this.getActivity().getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);

        recyclerView = (RecyclerView)view.findViewById(R.id.notification_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        notiList = new ArrayList<>();
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
        animSelect = (LottieAnimationView)loadDialog.findViewById(R.id.loading_one);
        animSelect.setAnimation("blueline.json");
        animSelect.playAnimation();
        animSelect.loop(true);

        Window window = loadDialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loadDialog.show();

        AndroidNetworking.get("http://roots.atlasglobalsys.com/api/getResponse/newsBoard/"+user_id)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject rootObject = new JSONObject(response);
                            JSONObject data = rootObject.getJSONObject("data");
                            JSONArray notification_list = data.getJSONArray("notificationlist");
                            for(int i=0;i<notification_list.length();i++){
                                JSONObject notiData = new JSONObject(notification_list.get(i).toString());
                                notiList.add(new NotificationObject(notiData.getString("title"),
                                        notiData.getString("message"),
                                        notiData.getString("date"),
                                        (notiData.getString("notification_id").equals("unread"))?true:false));
                            }
                        } catch (JSONException e) {
                            loadDialog.dismiss();
                            Toast.makeText(getContext(), e+"", Toast.LENGTH_SHORT).show();
                        }finally {
                            loadDialog.dismiss();
                            NotificationAdapter adapter = new NotificationAdapter(view.getContext(), notiList);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        loadDialog.dismiss();
                        Toast.makeText(view.getContext(), anError+"", Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }

}
