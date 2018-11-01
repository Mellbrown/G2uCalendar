package me.myds.g2u.g2u_calendar.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import me.myds.g2u.g2u_calendar.DateChanged;
import me.myds.g2u.g2u_calendar.R;
import me.myds.g2u.g2u_calendar.ScheduleDAO;

public class MonthlyFragment extends Fragment implements DateChanged {
    private TableLayout table;

    private ArrayList<TableRow> tableRows = new ArrayList<>();
    private ArrayList<MonthlyItemView> monthlyItemes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewLayout = inflater.inflate(R.layout.fragment_monthly, container,false);

        table = viewLayout.findViewById(R.id.table);

        for(int i = 0 ; 6 > i; i++){
            TableRow tableRow = new TableRow(getContext());
            tableRow.setWeightSum(7);
            tableRows.add(tableRow);

            for(int j = 0; 7 > j; j++){
                View itemView = inflater.inflate(R.layout.item_monthly,tableRow,false);
                MonthlyItemView monthlyItemView = new MonthlyItemView(itemView);
                monthlyItemes.add(monthlyItemView);

                if(j==0)monthlyItemView.imgWeek.setBackgroundColor(Color.RED);
                if(j==6)monthlyItemView.imgWeek.setBackgroundColor(Color.BLUE);

                tableRow.addView(itemView,new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
            }

            table.addView(tableRow, new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1.0f
            ));
        }

        if(calendar != null && scheduleDAO != null){
            dateChanged(this.calendar, this.scheduleDAO);
        }
        return viewLayout;
    }

    private Calendar calendar = null;
    private ScheduleDAO scheduleDAO = null;
    public void delayDateChanged(Calendar calendar,ScheduleDAO scheduleDAO){
        this.calendar = calendar ;
        this.scheduleDAO = scheduleDAO ;
    }

    @Override
    public void dateChanged(Calendar cal,ScheduleDAO scheduleDAO) {
        Calendar cl = (Calendar) cal.clone();
        cl.add(Calendar.MONTH,1);
        ScheduleDAO.ymd start = new ScheduleDAO.ymd(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, 1);
        ScheduleDAO.ymd end = new ScheduleDAO.ymd(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH)+1, 1);
        ArrayList<ScheduleDAO.ScheduleBean> schedules = scheduleDAO.getSchedules(start, end);

        cl = (Calendar) cal.clone();
        cl.set(Calendar.DAY_OF_MONTH, 1);
        int startItem = cl.get(Calendar.DAY_OF_WEEK) - 1;
        int endItem = cl.getActualMaximum(Calendar.DAY_OF_MONTH) + startItem;
        for(int i = 0 ; monthlyItemes.size() > i ; i++){
            if(startItem <= i && i < endItem){
                MonthlyItemView monthlyItemView = monthlyItemes.get(i);
                monthlyItemView.container.setVisibility(View.VISIBLE);
                monthlyItemView.txtDayOfMonth.setText((i - startItem + 1)+"");
                monthlyItemView.card.setVisibility(View.INVISIBLE);
                monthlyItemView.cntSchedule = 0;
            } else
                monthlyItemes.get(i).container.setVisibility(View.INVISIBLE);
        }

        for(ScheduleDAO.ScheduleBean scheduleBean : schedules){
            MonthlyItemView monthlyItemView = monthlyItemes.get(scheduleBean.getYmd().dayOfMonth + startItem - 1);
            monthlyItemView.card.setVisibility(View.VISIBLE);
            monthlyItemView.cntSchedule++;
            monthlyItemView.txtCntSchedule.setText("+ " + monthlyItemView.cntSchedule);
        }
    }

    public static class MonthlyItemView{
        public View itemView;
        public CardView container;
        public ImageView imgWeek;
        public TextView txtDayOfMonth;
        public CardView card;
        public TextView txtCntSchedule;
        public int cntSchedule = 0;

        public MonthlyItemView(View _itemView){
            itemView = _itemView;
            container = itemView.findViewById(R.id.container);
            imgWeek = itemView.findViewById(R.id.imgWeek);
            txtDayOfMonth = itemView.findViewById(R.id.txtDayOfMonth);
            card = itemView.findViewById(R.id.card);
            txtCntSchedule = itemView.findViewById(R.id.cntSchedule);
        }
    }
}

