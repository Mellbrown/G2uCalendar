package me.myds.g2u.g2u_calendar.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import me.myds.g2u.g2u_calendar.DateChanged;
import me.myds.g2u.g2u_calendar.R;
import me.myds.g2u.g2u_calendar.ScheduleDAO;
import me.myds.g2u.g2u_calendar.fragment.DailyFragment;
import me.myds.g2u.g2u_calendar.fragment.MonthlyFragment;
import me.myds.g2u.g2u_calendar.fragment.WeeklyFragment;

public class CalendarActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private BottomNavigationView nav;

    private SharedPreferences preferences;
    private String curpage;
    public static final String LAST_PAGE = "LAST_PAGE";
    public static final String MONTH_PAGE = "MONTH_PAGE";
    public static final String WEEK_PAGE = "WEEK_PAGE";
    public static final String DAY_PAGE = "DAY_PAGE";

    private TextView txtDate;
    private FloatingActionButton btnPrev;
    private FloatingActionButton btnNext;

    private Calendar calendar;
    ScheduleDAO scheduleDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        nav = findViewById(R.id.nav);
        txtDate = findViewById(R.id.txtDate);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        btnPrev.setOnClickListener(onClickDateMoveListener);
        btnNext.setOnClickListener(onClickDateMoveListener);

        fragmentManager = getSupportFragmentManager();
        nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        scheduleDAO = new ScheduleDAO(this,"g2u-calendar.db",null,1);

        preferences = getSharedPreferences(LAST_PAGE,MODE_PRIVATE);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (preferences.getString(LAST_PAGE,MONTH_PAGE)){
            case MONTH_PAGE:
                fragmentTransaction.add(R.id.frame,new MonthlyFragment(),MONTH_PAGE);
                fragmentTransaction.commitNow();
                curpage = MONTH_PAGE;
                break;
            case WEEK_PAGE:
                fragmentTransaction.add(R.id.frame,new WeeklyFragment(),WEEK_PAGE);
                fragmentTransaction.commitNow();
                curpage = WEEK_PAGE;
                break;
            case DAY_PAGE:
                fragmentTransaction.add(R.id.frame,new DailyFragment(),DAY_PAGE);
                fragmentTransaction.commitNow();
                curpage = DAY_PAGE;
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
                    if(curpage == MONTH_PAGE) return true;
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, new MonthlyFragment(), MONTH_PAGE);
                    fragmentTransaction.commitNow();
                    curpage = MONTH_PAGE;
                    preferences.edit().putString(LAST_PAGE,MONTH_PAGE).commit();
                    return true;
                case R.id.nav_weekly:
                    if(curpage == WEEK_PAGE) return true;
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, new WeeklyFragment(), WEEK_PAGE);
                    fragmentTransaction.commitNow();
                    curpage = WEEK_PAGE;
                    preferences.edit().putString(LAST_PAGE,WEEK_PAGE).commit();
                    return true;
                case R.id.nav_daily:
                    if(curpage == DAY_PAGE) return true;
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, new DailyFragment(), DAY_PAGE);
                    fragmentTransaction.commitNow();
                    curpage = DAY_PAGE;
                    preferences.edit().putString(LAST_PAGE,DAY_PAGE).commit();
                    return true;
            }
            return false;
        }
    };

    private void setDateText(){
        switch (curpage){
            case MONTH_PAGE: txtDate.setText(String.format("%d년 %d월",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1));
                break;
            case WEEK_PAGE: txtDate.setText(String.format("%d년 %d월 %d주",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.WEEK_OF_MONTH)));
                break;
            case DAY_PAGE: txtDate.setText(String.format("%년 %월 %d일",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.DAY_OF_WEEK)));
                break;
        }
    }

    private View.OnClickListener onClickDateMoveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.btnPrev:{
                    switch (curpage){
                        case MONTH_PAGE: calendar.add(Calendar.MONTH,-1); break;
                        case WEEK_PAGE: calendar.add(Calendar.DAY_OF_MONTH,-7); break;
                        case DAY_PAGE: calendar.add(Calendar.DAY_OF_MONTH, -1);break;
                    }
                    setDateText();
                    DateChanged dateChanged = (DateChanged) fragmentManager.getFragments().get(0);
                    dateChanged.dateChanged(calendar);
                }break;
                case R.id.btnNext:{
                    switch (curpage){
                        case MONTH_PAGE: calendar.add(Calendar.MONTH,+1); break;
                        case WEEK_PAGE: calendar.add(Calendar.DAY_OF_MONTH,+7); break;
                        case DAY_PAGE: calendar.add(Calendar.DAY_OF_MONTH, +1);break;
                    }
                    setDateText();
                    DateChanged dateChanged = (DateChanged) fragmentManager.getFragments().get(0);
                    dateChanged.dateChanged(calendar);
                }break;
            }

        }
    };
}
