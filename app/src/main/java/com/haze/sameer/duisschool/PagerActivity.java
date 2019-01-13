package com.haze.sameer.duisschool;

import android.animation.ArgbEvaluator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PagerActivity extends AppCompatActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    ImageView zero, one, two, three;
    ImageView[] indicators;

    CoordinatorLayout mCoordinator;
    LinearLayout indicatorsLin;

    TextView skipTxt,nextTxt,doneTxt;

    static final String TAG = "PagerActivity";

    int page = 0;   //  to track page position

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.shared_preference), MODE_PRIVATE);
        int restoredText = prefs.getInt("login", 0);
        if (restoredText == 1) {
            startActivity(new Intent(PagerActivity.this,HomeActivity.class));
            finish();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        indicatorsLin = (LinearLayout)findViewById(R.id.introIndicators);

        skipTxt = (TextView)findViewById(R.id.pager_skipTxt);
        nextTxt = (TextView)findViewById(R.id.pager_nextTxt);
        doneTxt = (TextView)findViewById(R.id.pager_doneTxt);

        zero = (ImageView) findViewById(R.id.intro_indicator_0);
        one = (ImageView) findViewById(R.id.intro_indicator_1);
        two = (ImageView) findViewById(R.id.intro_indicator_2);
        three = (ImageView) findViewById(R.id.intro_indicator_3);

        mCoordinator = (CoordinatorLayout) findViewById(R.id.main_content);

        indicators = new ImageView[]{zero, one, two, three};

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(page);
        updateIndicators(page);

        final ArgbEvaluator evaluator = new ArgbEvaluator();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                page = position;
                updateIndicators(page);

                doneTxt.setVisibility(position == 3 ? View.VISIBLE : View.GONE);
                nextTxt.setVisibility(position == 3 ? View.GONE : View.VISIBLE);
                skipTxt.setVisibility(position == 3 ? View.GONE : View.VISIBLE);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        skipTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PagerActivity.this,LoginActivity.class));
                finish();
            }
        });
        doneTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PagerActivity.this,LoginActivity.class));
                finish();
            }
        });

    }

    void updateIndicators(int position) {

        switch (position){
            case 0:
                zero.setBackgroundResource(R.drawable.indicator_selected);
                one.setBackgroundResource(R.drawable.indicator_unselected);
                two.setBackgroundResource(R.drawable.indicator_unselected);
                three.setBackgroundResource(R.drawable.indicator_unselected);
                break;
            case 1:
                zero.setBackgroundResource(R.drawable.indicator_selected);
                one.setBackgroundResource(R.drawable.indicator_selected);
                two.setBackgroundResource(R.drawable.indicator_unselected);
                three.setBackgroundResource(R.drawable.indicator_unselected);
                break;
            case 2:
                zero.setBackgroundResource(R.drawable.indicator_selected);
                one.setBackgroundResource(R.drawable.indicator_selected);
                two.setBackgroundResource(R.drawable.indicator_selected);
                three.setBackgroundResource(R.drawable.indicator_unselected);
                break;
            case 3:
                zero.setBackgroundResource(R.drawable.indicator_selected);
                one.setBackgroundResource(R.drawable.indicator_selected);
                two.setBackgroundResource(R.drawable.indicator_selected);
                three.setBackgroundResource(R.drawable.indicator_selected);
                break;

        }

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton("No", null).show();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        ImageView bck;

        int[] bckImg = new int[]{R.drawable.intro_three, R.drawable.intro_one, R.drawable.intro_two, R.drawable.intro_four};
        String[] text_design = new String[]
                {   "It takes a Big Heart to shape Little Champs",
                        "We have Co-curricular activities like Art and craft, Abacus, Hindi, Sports, Medical care and Counciling",
                        "Bharathanatiyam, Karate, Yoga, Drawing, Music and Sports",
                        "Bus tracking App allows Parent's to view the real time location of thier kid's school bus on their mobile." };

        String[] text_head = new String[]{"","Co-Curricular Activities","Activities",""};

        public PlaceholderFragment() {

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pager, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(text_design[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);
            TextView headView = (TextView) rootView.findViewById(R.id.section_head);
            headView.setText(text_head[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);

            bck = (ImageView) rootView.findViewById(R.id.section_bck);
            bck.setBackgroundResource(bckImg[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);

            return rootView;
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);

        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return "SECTION 4";
            }
            return null;
        }
    }

}
