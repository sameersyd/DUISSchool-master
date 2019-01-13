package com.haze.sameer.duisschool;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AttendanceFragment extends Fragment {

    public AttendanceFragment() {
        // Required empty public constructor
    }
    TextView stuName,stuClass;
    int user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance , container, false);

        AndroidNetworking.initialize(view.getContext().getApplicationContext());

        SharedPreferences prefs = this.getActivity().getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);

        stuName = (TextView) view.findViewById(R.id.fragment_attendence_studentName);
        stuClass = (TextView) view.findViewById(R.id.fragment_attendence_studentClass);

        String[] separated = new String[0];
        try {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = df.format(c);
            separated = formattedDate.split("-");
        } catch (Exception e) {
            Toast.makeText(view.getContext(), e + "", Toast.LENGTH_SHORT).show();
        }

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

        List<EventDay> events = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        events.add(new EventDay(calendar, R.drawable.attendance_day));
        CalendarView calendarView = (CalendarView) view.findViewById(R.id.fragment_attendence_calendarView);

        loadDialog.show();
        AndroidNetworking.get("http://roots.atlasglobalsys.com/api/getResponse/attendance/"+user_id)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject rootObject = new JSONObject(response);
                            JSONArray data = rootObject.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject attenData = new JSONObject(data.get(i).toString());
                                Calendar calendar1 = Calendar.getInstance();

                                SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
                                String inputString1;
                                String inputString2;

                                String[] input1 = new String[0];
                                try {
                                    Date c = Calendar.getInstance().getTime();
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c);
                                    input1 = formattedDate.split("-");
                                } catch (Exception e) {
                                    Toast.makeText(view.getContext(), e + "", Toast.LENGTH_SHORT).show();
                                }
                                inputString1 = input1[0]+" "+(Integer.parseInt(input1[1])-1)+" "+input1[2];         //setCurrentDate for parameterOne(findDaysDifference)

                                String[] input2 = new String[0];
                                try{
                                    String dateFormat = attenData.getString("date");
                                    input2 = dateFormat.split("-");
                                } catch (Exception e) {
                                    Toast.makeText(view.getContext(), e + "", Toast.LENGTH_SHORT).show();
                                }
                                inputString2 = input2[2]+" "+(Integer.parseInt(input2[1])-1)+" "+input2[0];                               //setCurrentDate for parameterTwo(findDaysDifference)

                                try {
                                    Date date1 = myFormat.parse(inputString1);
                                    Date date2 = myFormat.parse(inputString2);
                                    long diff = date2.getTime() - date1.getTime();                                  //finds difference between two dates
                                    calendar1.add(Calendar.DAY_OF_MONTH, (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));         //(parameterOne Calender.DAY_OF_MONTH, parameterTwo datesDifference)
                                } catch (ParseException e) {
                                    Toast.makeText(view.getContext(), "", Toast.LENGTH_SHORT).show();
                                }
                                if (attenData.getString("title").equals("Absent"))
                                    events.add(new EventDay(calendar1, R.drawable.attendance_absent));
                                else if (attenData.getString("title").equals("Present"))
                                    events.add(new EventDay(calendar1, R.drawable.attendance_present));
                                else if (attenData.getString("title").equals("Late"))
                                    events.add(new EventDay(calendar1, R.drawable.attendance_late));
                                else if (attenData.getString("title").equals("Holiday"))
                                    events.add(new EventDay(calendar1, R.drawable.attendance_holiday));
                                else if (attenData.getString("title").equals("Half Day"))
                                    events.add(new EventDay(calendar1, R.drawable.attendance_halfday));
                            }
                        } catch (JSONException e) {
                            Toast.makeText(view.getContext(), e + "", Toast.LENGTH_SHORT).show();
                        } finally {
                            loadDialog.dismiss();
                            calendarView.setEvents(events);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        loadDialog.dismiss();
                        Toast.makeText(view.getContext(), anError + "", Toast.LENGTH_SHORT).show();
                    }
                });
        try {
            calendar.set(Integer.parseInt(separated[2]), Integer.parseInt(separated[1]) - 1, Integer.parseInt(separated[0]));
            calendarView.setDate(calendar);
        } catch (Exception e) {
            Toast.makeText(view.getContext(), e + "", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

}
