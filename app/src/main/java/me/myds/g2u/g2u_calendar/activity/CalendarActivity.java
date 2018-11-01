package me.myds.g2u.g2u_calendar.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.TextView;

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
    private String curpage = "";
    public static final String LAST_PAGE = "LAST_PAGE";
    public static final String MONTH_PAGE = "MONTH_PAGE";
    public static final String WEEK_PAGE = "WEEK_PAGE";
    public static final String DAY_PAGE = "DAY_PAGE";

    private TextView txtDate;
    private FloatingActionButton btnPrev;
    private FloatingActionButton btnNext;
    private FloatingActionButton btnAdd;

    private Calendar calendar;
    ScheduleDAO scheduleDAO;
    public static final String ARG_TIMESTAMP = "ARG_TIMESTAMP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        nav = findViewById(R.id.nav);
        txtDate = findViewById(R.id.txtDate);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnAdd = findViewById(R.id.btnAdd);

        btnPrev.setOnClickListener(onClickDateMoveListener);
        btnNext.setOnClickListener(onClickDateMoveListener);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CalendarActivity.this, AddScheduleActivity.class),1);
            }
        });

        fragmentManager = getSupportFragmentManager();
        nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        scheduleDAO = ScheduleDAO.getInstance(this);

        calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        preferences = getSharedPreferences(LAST_PAGE,MODE_PRIVATE);
        switch (preferences.getString(LAST_PAGE,MONTH_PAGE)){
            case MONTH_PAGE:
                nav.setSelectedItemId(R.id.nav_monthly);
                break;
            case WEEK_PAGE:
                nav.setSelectedItemId(R.id.nav_weekly);
                break;
            case DAY_PAGE:
                nav.setSelectedItemId(R.id.nav_daily);
                break;
        }
    }
    private void setFragment(Fragment fragment,String fragmentTag){
        Bundle arg = new Bundle();
        arg.putLong(ARG_TIMESTAMP, calendar.getTimeInMillis());
        fragment.setArguments(arg);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment,fragmentTag);
        fragmentTransaction.commit();
        setDateText();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_monthly:
                    if(curpage.equals(MONTH_PAGE)) return true;
                    curpage = MONTH_PAGE;
                    preferences.edit().putString(LAST_PAGE,MONTH_PAGE).apply();
                    calendar.setTime(new Date());
                    setFragment(new MonthlyFragment(), MONTH_PAGE);
                    return true;
                case R.id.nav_weekly:
                    if(curpage.equals(WEEK_PAGE)) return true;
                    curpage = WEEK_PAGE;
                    preferences.edit().putString(LAST_PAGE,WEEK_PAGE).apply();
                    calendar.setTime(new Date());
                    setFragment(new WeeklyFragment(), WEEK_PAGE);
                    return true;
                case R.id.nav_daily:
                    if(curpage.equals(DAY_PAGE)) return true;
                    curpage = DAY_PAGE;
                    preferences.edit().putString(LAST_PAGE,DAY_PAGE).apply();
                    calendar.setTime(new Date());
                    setFragment(new DailyFragment(), DAY_PAGE);
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
            case DAY_PAGE: txtDate.setText(String.format("%d년 %d월 %d일",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.DAY_OF_MONTH)));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode ==1 && resultCode == 1){
            String resultTitle = data.getStringExtra(AddScheduleActivity.RESULT_TITLE);
            long resultDate = data.getLongExtra(AddScheduleActivity.RESULT_DATE, 0);
            scheduleDAO.addSchedule(new ScheduleDAO.ScheduleBean(resultTitle,resultDate));
            DateChanged dateChanged = (DateChanged) fragmentManager.getFragments().get(0);
            dateChanged.dateChanged(calendar);
        }
    }
}
