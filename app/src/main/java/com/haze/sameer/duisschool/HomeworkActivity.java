package com.haze.sameer.duisschool;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
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

public class HomeworkActivity extends AppCompatActivity {

    List<HomeworkObject> workList;
    RecyclerView recyclerView;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

        AndroidNetworking.initialize(getApplicationContext());

        SharedPreferences prefs = getSharedPreferences(getString(R.string.shared_preference), MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);

        recyclerView = (RecyclerView)findViewById(R.id.homework_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        workList = new ArrayList<>();
        final Dialog loadDialog = new Dialog(this);
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

        AndroidNetworking.get("http://roots.atlasglobalsys.com/api/getResponse/homework/"+user_id)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject rootObject = new JSONObject(response);
                            JSONObject data = rootObject.getJSONObject("data");
                            JSONArray homework_list = data.getJSONArray("homeworklist");
                            for(int i=0;i<=homework_list.length();i++){
                                JSONObject workData = new JSONObject(homework_list.get(i).toString());
                                workList.add(new HomeworkObject(workData.getString("staff_id"),
                                        workData.getString("create_date"),
                                        workData.getString("submit_date"),
                                        workData.getString("submit_date"),
                                        workData.getString("class"),
                                        workData.getString("section"),
                                        workData.getString("name")));
                            }
                        }catch (JSONException e) {
                            loadDialog.dismiss();
                            e.printStackTrace();
                        }finally {
                            HomeworkAdapter adapter = new HomeworkAdapter(HomeworkActivity.this, workList);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        loadDialog.dismiss();
                        Toast.makeText(HomeworkActivity.this, anError+"", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
