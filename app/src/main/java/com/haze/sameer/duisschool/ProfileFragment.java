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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }

    RelativeLayout openStudent,openParent;
    ImageView openStudentImg,openParentImg;
    LinearLayout studentDetails,parentDetails;
    Boolean studentOpenBoo,parentOpenBoo;
    TextView stuName,stuStan,stuSec,stuGender,stuDOB,stuBloodGroup,stuDayScholar,stuBusNo,
            stuAdmissionNo,stuAddress,stuCity,stuState,stuPostalCode,stuContactMobileNo;
    TextView parentFatherName,parentFatherOccupation,parentFatherMobileNo,parentFatherEmail;
    TextView parentMotherName,parentMotherOccupation,parentMotherMobileNo,parentMotherEmail;
    int user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile , container, false);
        AndroidNetworking.initialize(view.getContext().getApplicationContext());

        SharedPreferences prefs = this.getActivity().getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);

        studentOpenBoo = true;
        parentOpenBoo = false;
        openStudent = (RelativeLayout)view.findViewById(R.id.fragment_profile_studentInfoBtn);
        openParent = (RelativeLayout)view.findViewById(R.id.fragment_profile_parentInfoBtn);
        openStudentImg = (ImageView)view.findViewById(R.id.fragment_profile_studentInfoOpenBtn);
        openParentImg = (ImageView)view.findViewById(R.id.fragment_profile_parentInfoOpenBtn);
        studentDetails = (LinearLayout)view.findViewById(R.id.fragment_profile_studentInfoLinear);
        parentDetails = (LinearLayout) view.findViewById(R.id.fragment_profile_parentInfoLinear);

        stuName = (TextView)view.findViewById(R.id.fragment_profile_stuName);
        stuStan = (TextView)view.findViewById(R.id.fragment_profile_stuStandard);
        stuSec = (TextView)view.findViewById(R.id.fragment_profile_stuSection);
        stuGender = (TextView)view.findViewById(R.id.fragment_profile_stuGender);
        stuDOB = (TextView)view.findViewById(R.id.fragment_profile_stuDOB);
        stuBloodGroup = (TextView)view.findViewById(R.id.fragment_profile_stuBloodGroup);
        stuDayScholar = (TextView)view.findViewById(R.id.fragment_profile_stuDayScholar);
        stuBusNo = (TextView)view.findViewById(R.id.fragment_profile_stuBusNo);
        stuAdmissionNo = (TextView)view.findViewById(R.id.fragment_profile_stuAdmissionNo);
        stuAddress = (TextView)view.findViewById(R.id.fragment_profile_stuAddress);
        stuCity = (TextView)view.findViewById(R.id.fragment_profile_stuCity);
        stuState = (TextView)view.findViewById(R.id.fragment_profile_stuState);
        stuPostalCode = (TextView)view.findViewById(R.id.fragment_profile_stuPostalCode);
        stuContactMobileNo = (TextView)view.findViewById(R.id.fragment_profile_stuContactMobileNo);

        parentFatherName = (TextView)view.findViewById(R.id.fragment_profile_parent_fatherName);
        parentFatherOccupation = (TextView)view.findViewById(R.id.fragment_profile_parent_fatherOccupation);
        parentFatherMobileNo = (TextView)view.findViewById(R.id.fragment_profile_parent_fatherMobileNo);
        parentFatherEmail = (TextView)view.findViewById(R.id.fragment_profile_parent_fatherEmail);

        parentMotherName = (TextView)view.findViewById(R.id.fragment_profile_parent_motherName);
        parentMotherOccupation = (TextView)view.findViewById(R.id.fragment_profile_parent_motherOccupation);
        parentMotherMobileNo = (TextView)view.findViewById(R.id.fragment_profile_parent_motherMobileNo);
        parentMotherEmail = (TextView)view.findViewById(R.id.fragment_profile_parent_motherEmail);

        checkBoolean(view);

        openStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (studentOpenBoo){
                    studentOpenBoo = false;
                    checkBoolean(view);
                }else {
                    studentOpenBoo = true;
                    checkBoolean(view);
                }
            }
        });
        openParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (parentOpenBoo){
                    parentOpenBoo = false;
                    checkBoolean(view);
                }else {
                    parentOpenBoo = true;
                    checkBoolean(view);
                }
            }
        });

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

        AndroidNetworking.get("http://roots.atlasglobalsys.com/api/getResponse/profile/"+user_id)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject rootObject = new JSONObject(response);
                            JSONObject data = rootObject.getJSONObject("data");
                            JSONObject student = data.getJSONObject("student");

                            stuName.setText(student.getString("firstname")+" "+student.getString("lastname"));
                            stuStan.setText(student.getString("class"));
                            stuSec.setText(student.getString("section"));
                            stuGender.setText(student.getString("gender"));
                            stuDOB.setText(student.getString("dob"));
                            stuBloodGroup.setText(student.getString("blood_group"));
                            stuDayScholar.setText("");                                                 //NotAvailable in API
                            stuBusNo.setText(student.getString("vehicle_no"));
                            stuAdmissionNo.setText(student.getString("admission_no"));
                            stuAddress.setText(student.getString("current_address"));
                            stuCity.setText(student.getString("city"));
                            stuState.setText(student.getString("state"));
                            stuPostalCode.setText(student.getString("pincode"));
                            stuContactMobileNo.setText(student.getString("mobileno"));

                            parentFatherName.setText(student.getString("father_name"));
                            parentFatherOccupation.setText(student.getString("father_occupation"));
                            parentFatherMobileNo.setText(student.getString("father_phone"));
                            parentFatherEmail.setText("");                                             //NotAvailable in API

                            parentMotherName.setText(student.getString("mother_name"));
                            parentMotherOccupation.setText(student.getString("mother_occupation"));
                            parentMotherMobileNo.setText(student.getString("mother_phone"));
                            parentMotherEmail.setText("");                                             //NotAvailable in API

                            loadDialog.dismiss();

                        } catch (JSONException e) {
                            loadDialog.dismiss();
                            e.printStackTrace();
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

    public void checkBoolean(View view){
        if (studentOpenBoo) {
            studentDetails.setVisibility(View.VISIBLE);
            openStudentImg.setImageDrawable(view.getResources().getDrawable(R.drawable.minus_666));
        }else {
            studentDetails.setVisibility(View.GONE);
            openStudentImg.setImageDrawable(view.getResources().getDrawable(R.drawable.plus_666));
        }

        if (parentOpenBoo) {
            parentDetails.setVisibility(View.VISIBLE);
            openParentImg.setImageDrawable(view.getResources().getDrawable(R.drawable.minus_666));
        }else {
            parentDetails.setVisibility(View.GONE);
            openParentImg.setImageDrawable(view.getResources().getDrawable(R.drawable.plus_666));
        }
    }

}
