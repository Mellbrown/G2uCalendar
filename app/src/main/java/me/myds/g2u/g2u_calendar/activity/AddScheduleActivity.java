package me.myds.g2u.g2u_calendar.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import me.myds.g2u.g2u_calendar.R;
import me.myds.g2u.g2u_calendar.ScheduleDAO;

public class AddScheduleActivity extends AppCompatActivity {

    public static final String RESULT_TITLE = "RESULT_TITLE";
    public static final String RESULT_DATE = "RESULT_DATE";

    private CalendarView calendarView;
    private TextView txtTitle;
    private FloatingActionButton btnConfirm;
    private long timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        calendarView = findViewById(R.id.calendarView);
        txtTitle = findViewById(R.id.txtTitle);
        btnConfirm = findViewById(R.id.btnConfirm);

        timestamp = new Date().getTime();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                timestamp = ScheduleDAO.ymd2timestamp(year,month+1,dayOfMonth);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtTitle.getText().toString().equals("")) {
                    Toast.makeText(AddScheduleActivity.this, "제목 입력", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent result = new Intent();
                result.putExtra(RESULT_TITLE,txtTitle.getText().toString());
                result.putExtra(RESULT_DATE,timestamp);
                setResult(1,result);
                finish();
            }
        });
    }
}
