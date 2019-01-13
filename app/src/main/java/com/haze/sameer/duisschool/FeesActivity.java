package com.haze.sameer.duisschool;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
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

public class FeesActivity extends AppCompatActivity {

    TextView nameTxt,admissionTxt,rollTxt,classTxt,secTxt;
    int user_id;
    List<FeesObject> feesList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees);
        AndroidNetworking.initialize(getApplicationContext());

        SharedPreferences prefs = getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);

        nameTxt = (TextView)findViewById(R.id.fees_nameTxt);
        admissionTxt = (TextView)findViewById(R.id.fees_admissionTxt);
        rollTxt = (TextView)findViewById(R.id.fees_rollTxt);
        classTxt = (TextView)findViewById(R.id.fees_classTxt);
        secTxt = (TextView)findViewById(R.id.fees_sectionTxt);

        recyclerView = (RecyclerView)findViewById(R.id.fees_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        feesList = new ArrayList<>();

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
        AndroidNetworking.get("http://roots.atlasglobalsys.com/api/getResponse/fees/"+user_id)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.v("feesList",feesList+"");
                            JSONObject rootObject = new JSONObject(response);
                            JSONObject data = rootObject.getJSONObject("data");
                            JSONObject student = data.getJSONObject("student");
                            nameTxt.setText(student.getString("firstname") + " " + student.getString("lastname"));
                            admissionTxt.setText(student.getString("admission_no"));
                            rollTxt.setText(student.getString("roll_no"));
                            classTxt.setText(student.getString("class"));
                            secTxt.setText(student.getString("section"));
                            JSONArray stuFee = data.getJSONArray("student_due_fee");
                            for(int i=0;i<stuFee.length();i++){
                                JSONObject feesData = new JSONObject(stuFee.get(i).toString());
                                JSONArray feeArray = feesData.getJSONArray("fees");
                                for (int j=0;j<feeArray.length();j++) {
                                    JSONObject incFeesdata = new JSONObject(feeArray.get(i).toString());
                                    feesList.add(new FeesObject(
                                            incFeesdata.getString("type"),
                                            incFeesdata.getString("name"),
                                            incFeesdata.getString("amount"),
                                            incFeesdata.getString("due_date"),
                                            incFeesdata.getString("is_active")));
                                }
                            }
                        } catch (JSONException e) {
                            loadDialog.dismiss();
                            Toast.makeText(FeesActivity.this, e+"", Toast.LENGTH_SHORT).show();
                        }finally {
                            loadDialog.dismiss();
                            FeesAdapter adapter = new FeesAdapter(FeesActivity.this, feesList);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        loadDialog.dismiss();
                        Toast.makeText(FeesActivity.this, anError+"", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
