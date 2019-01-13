package com.haze.sameer.duisschool;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.pusher.pushnotifications.PushNotifications;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "HomeActivity";
    BottomNavigationView bottomNavigationView;
    public Toolbar mToolbar;
    int unreadNum = 0,user_id;
    boolean unreadNoti = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AndroidNetworking.initialize(getApplicationContext());

        PushNotifications.start(getApplicationContext(), "e4b1481a-3210-47bc-b684-206e5f9fd00f");
        PushNotifications.subscribe("hello");

        SharedPreferences prefs = getSharedPreferences(getString(R.string.shared_preference), MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);

        mToolbar = (Toolbar)findViewById(R.id.swipe_toolbar);
        setSupportActionBar(mToolbar);

        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_open_icon);

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.home_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        TextView navUserName = (TextView) headerView.findViewById(R.id.nav_header_nameTxt);
        AndroidNetworking.get("http://roots.atlasglobalsys.com/api/getResponse/profile/"+user_id)          //To set student name in navigationDrawer
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject rootObject = new JSONObject(response);
                            JSONObject data = rootObject.getJSONObject("data");
                            JSONObject student = data.getJSONObject("student");
                            navUserName.setText(student.getString("firstname") + " " + student.getString("lastname"));
                        } catch (JSONException e) {
                            Toast.makeText(HomeActivity.this, e + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(HomeActivity.this, anError + "", Toast.LENGTH_SHORT).show();
                    }
                });

        AndroidNetworking.get("http://roots.atlasglobalsys.com/api/getResponse/newsBoard/"+user_id)       //To Display unread notifications
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
                                if(notiData.getString("notification_id").equals("unread")){
                                    unreadNum++;
                                    unreadNoti = true;
                                }
                            }
                        } catch (JSONException e) {
                            Toast.makeText(HomeActivity.this, e+"", Toast.LENGTH_SHORT).show();
                        }finally {
                            if (unreadNoti){
                                NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(HomeActivity.this)
                                        .setSmallIcon(R.drawable.roots_icon)
                                        .setContentTitle("Roots Foster Learning")
                                        .setContentText("You have "+unreadNum+" unread notifications");

                                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(0, mBuilder.build());
                            }
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(HomeActivity.this, anError+"", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void performStreamClick(int pos){
        View view = null;
        switch (pos){
            case 0:
                view = bottomNavigationView.findViewById(R.id.navigation_attendence);
                break;
            case 1:
                view = bottomNavigationView.findViewById(R.id.navigation_notification);
                break;
            case 2:
                view = bottomNavigationView.findViewById(R.id.navigation_profile);
                break;
            case 3:
                view = bottomNavigationView.findViewById(R.id.navigation_marks);
                break;
        }
        view.performClick();
    }

    HomeFragment homeFragment = new HomeFragment();
    AttendanceFragment attendanceFragment = new AttendanceFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    MarksFragment marksFragment = new MarksFragment();
    NotificationFragment notificationFragment = new NotificationFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        try {
            DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
            View view = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, homeFragment).commit();
                    getSupportActionBar().setTitle(getString(R.string.title_home));
                    return true;

                case R.id.navigation_attendence:
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, attendanceFragment).commit();
                    getSupportActionBar().setTitle(getString(R.string.title_attendence));
                    return true;

                case R.id.navigation_profile:
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, profileFragment).commit();
                    getSupportActionBar().setTitle(getString(R.string.title_profile));
                    return true;

                case R.id.navigation_marks:
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, marksFragment).commit();
                    getSupportActionBar().setTitle(getString(R.string.title_marks));
                    return true;

                case R.id.navigation_notification:
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, notificationFragment).commit();
                    getSupportActionBar().setTitle(getString(R.string.title_notifications));
                    return true;

                case R.id.navdraw_home:
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, homeFragment).commit();
                    getSupportActionBar().setTitle(getString(R.string.title_home));
                    drawerLayout.closeDrawer(GravityCompat.START);
                    view = bottomNavigationView.findViewById(R.id.navigation_home);
                    view.performClick();
                    return true;

                case R.id.navdraw_attendance:
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, attendanceFragment).commit();
                    getSupportActionBar().setTitle(getString(R.string.title_attendence));
                    drawerLayout.closeDrawer(GravityCompat.START);
                    view = bottomNavigationView.findViewById(R.id.navigation_attendence);
                    view.performClick();
                    return true;

                case R.id.navdraw_notifications:
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, notificationFragment).commit();
                    getSupportActionBar().setTitle(getString(R.string.title_notifications));
                    drawerLayout.closeDrawer(GravityCompat.START);
                    view = bottomNavigationView.findViewById(R.id.navigation_notification);
                    view.performClick();
                    return true;

                case R.id.navdraw_homework:
                    startActivity(new Intent(HomeActivity.this,HomeworkActivity.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;

                case R.id.navdraw_marks:
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, marksFragment).commit();
                    getSupportActionBar().setTitle(getString(R.string.title_marks));
                    drawerLayout.closeDrawer(GravityCompat.START);
                    view = bottomNavigationView.findViewById(R.id.navigation_marks);
                    view.performClick();
                    return true;

                case R.id.navdraw_track:
                    startActivity(new Intent(HomeActivity.this,MapsActivity.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;

                case R.id.navdraw_profile:
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, profileFragment).commit();
                    getSupportActionBar().setTitle(getString(R.string.title_profile));
                    drawerLayout.closeDrawer(GravityCompat.START);
                    view = bottomNavigationView.findViewById(R.id.navigation_profile);
                    view.performClick();
                    return true;

                case R.id.navdraw_aboutschool:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Toast.makeText(this, "About School", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.navdraw_contact:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Toast.makeText(this, "Contact", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.navdraw_logout:
                    drawerLayout.closeDrawer(GravityCompat.START);
                    new AlertDialog.Builder(this).setIcon(R.drawable.logout_black).setTitle("Logout")
                            .setMessage("Are you sure you want to logout?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor editor = getSharedPreferences(
                                            getString(R.string.shared_preference), MODE_PRIVATE).edit();
                                    editor.putString("user_id","");
                                    editor.putInt("login", 0);
                                    editor.apply();
                                    startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                                    finish();
                                }
                            }).setNegativeButton("No", null).show();
                    return true;
            }
        }catch (Exception e){
            Log.v("HomActivity:5487:",e+"");
        }
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
