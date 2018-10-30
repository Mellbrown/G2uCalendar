package me.myds.g2u.g2u_calendar.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import me.myds.g2u.g2u_calendar.R;
import me.myds.g2u.g2u_calendar.fragment.DailyFragment;
import me.myds.g2u.g2u_calendar.fragment.MonthlyFragment;
import me.myds.g2u.g2u_calendar.fragment.WeeklyFragment;

public class CalendarActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private BottomNavigationView nav;

    private SharedPreferences preferences;
    private int curpage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        nav = findViewById(R.id.nav);

        fragmentManager = getSupportFragmentManager();
        nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        preferences = getSharedPreferences("app",MODE_PRIVATE);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (preferences.getInt("last_page",0)){
            case 0: //monthly
                fragmentTransaction.add(R.id.frame,new MonthlyFragment(),"monthly");
                fragmentTransaction.commitNow();
                curpage = R.id.nav_monthly;
                break;
            case 1: //weekly
                fragmentTransaction.add(R.id.frame,new WeeklyFragment(),"weekly");
                fragmentTransaction.commitNow();
                curpage = R.id.nav_weekly;
                break;
            case 2: //daily
                fragmentTransaction.add(R.id.frame,new DailyFragment(),"daily");
                fragmentTransaction.commitNow();
                curpage = R.id.nav_daily;
                break;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction;
            switch (item.getItemId()) {
                case R.id.nav_monthly:
                    if(curpage == R.id.nav_monthly) return true;
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, new MonthlyFragment(), "monthly");
                    fragmentTransaction.commitNow();
                    curpage = R.id.nav_monthly;
                    preferences.edit().putInt("last_page",0).commit();
                    return true;
                case R.id.nav_weekly:
                    if(curpage == R.id.nav_weekly) return true;
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, new WeeklyFragment(), "weekly");
                    fragmentTransaction.commitNow();
                    curpage = R.id.nav_weekly;
                    preferences.edit().putInt("last_page",1).commit();
                    return true;
                case R.id.nav_daily:
                    if(curpage == R.id.nav_daily) return true;
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, new DailyFragment(), "daily");
                    fragmentTransaction.commitNow();
                    curpage = R.id.nav_daily;
                    preferences.edit().putInt("last_page",2).commit();
                    return true;
            }
            return false;
        }
    };
}
