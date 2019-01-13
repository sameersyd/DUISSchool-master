package com.haze.sameer.duisschool;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AndroidNetworking.initialize(getApplicationContext());

        RelativeLayout login = (RelativeLayout)findViewById(R.id.login_loginBtn);
        username = (EditText) findViewById(R.id.login_usernameEdit);
        password = (EditText) findViewById(R.id.login_passwordEdit);

        final Dialog loadDialog = new Dialog(this);
        loadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadDialog.setContentView(R.layout.loading_one);
        loadDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    loadDialog.dismiss();
                }
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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDialog.show();
                if (username.getText().toString().isEmpty()||username.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Enter Username", Toast.LENGTH_SHORT).show();
                    loadDialog.dismiss();
                    return;
                }
                if (password.getText().toString().isEmpty()||password.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    loadDialog.dismiss();
                    return;
                }

                AndroidNetworking.get("http://roots.atlasglobalsys.com/api/getResponse/login")
                        .addQueryParameter("username",username.getText().toString())
                        .addQueryParameter("password",password.getText().toString())
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject rootObject = new JSONObject(response);
                                    JSONObject data = rootObject.getJSONObject("data");
                                    int status = rootObject.getInt("status");
                                    if (status==1){
                                        Toast.makeText(LoginActivity.this, rootObject
                                                .getString("message")+"", Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor editor = getSharedPreferences(
                                                getString(R.string.shared_preference), MODE_PRIVATE).edit();
                                        editor.putInt("user_id",
                                                Integer.parseInt(data.getString("id")));    //gets and stores the id of the user
                                        editor.putInt("login", 1);                          //stores 1 as user is logged in
                                        editor.apply();
                                        loadDialog.dismiss();
                                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                        finish();
                                    }else if (status==0){
                                        loadDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, rootObject
                                                .getString("message")+"", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    loadDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, e+"", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onError(ANError anError) {
                                loadDialog.dismiss();
                                Toast.makeText(LoginActivity.this, anError+"", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
