package me.myds.g2u.g2u_calendar.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import me.myds.g2u.g2u_calendar.BaseRecyclerAdapter;
import me.myds.g2u.g2u_calendar.DateChanged;
import me.myds.g2u.g2u_calendar.R;
import me.myds.g2u.g2u_calendar.ScheduleDAO;
import me.myds.g2u.g2u_calendar.activity.CalendarActivity;

public class WeeklyFragment extends Fragment implements DateChanged {

    private LinearLayout colsWeek;

    private ArrayList<WeekItem> weekItems = new ArrayList<>();
    private String[] weekName= {"일", "월", "화", "수", "목", "금", "토"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewLayout = inflater.inflate(R.layout.fragment_weekly, container,false);

        colsWeek = viewLayout.findViewById(R.id.colsWeek);

        for(int i = 0 ; 7 > i; i++){
            View itemView = inflater.inflate(R.layout.subview_item_week,colsWeek,false);
            WeekItem weekItem = new WeekItem(itemView);

            if(i==0)weekItem.imgWeek.setBackgroundColor(Color.RED);
            if(i==6)weekItem.imgWeek.setBackgroundColor(Color.BLUE);

            weekItems.add(weekItem);

            colsWeek.addView(itemView,new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1.0f
            ));
        }

        long timestamp = getArguments().getLong(CalendarActivity.ARG_TIMESTAMP);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(timestamp);
        dateChanged(cal);
        return viewLayout;
    }

    @Override
    public void dateChanged(Calendar calendar) {
        ScheduleDAO scheduleDAO = ScheduleDAO.getInstance(getContext());
        Calendar cal = (Calendar) calendar.clone();
        cal.set(Calendar.DAY_OF_WEEK,1);
        for(WeekItem weekItem : weekItems){
            weekItem.txtWeek.setText(String.format("%s\n%d/%d",
                    weekName[cal.get(Calendar.DAY_OF_WEEK)-1],
                    cal.get(Calendar.MONTH)+1,
                    cal.get(Calendar.DAY_OF_MONTH)));
            weekItem.adapter.dataList.clear();
            weekItem.adapter.notifyDataSetChanged();
            cal.add(Calendar.DAY_OF_WEEK,+1);
        }
        cal.add(Calendar.DAY_OF_WEEK,-7);
        ScheduleDAO.ymd start = ScheduleDAO.timestamp2ymd(cal.getTimeInMillis());
        cal.add(Calendar.DAY_OF_WEEK,+7);
        ScheduleDAO.ymd end = ScheduleDAO.timestamp2ymd(cal.getTimeInMillis());
        ArrayList<ScheduleDAO.ScheduleBean> schedules = scheduleDAO.getSchedules(start, end);
        for(ScheduleDAO.ScheduleBean scheduleBean: schedules){
            cal.setTimeInMillis(scheduleBean.timestamp);
            WeekItem weekItem = weekItems.get(cal.get(Calendar.DAY_OF_WEEK)-1);
            weekItem.adapter.dataList.add(scheduleBean);
            weekItem.adapter.notifyItemInserted(weekItem.adapter.dataList.size()-1);
        }
    }


    public static class WeekItem{
        public View itemView;
        public ImageView imgWeek;
        public TextView txtWeek;
        public RecyclerView lstSchedule;
        private RecyclerView.LayoutManager layoutManager;
        public BaseRecyclerAdapter<ScheduleDAO.ScheduleBean,ShecduleViewHolder> adapter;

        public WeekItem(View _itemView){
            itemView = _itemView;
            imgWeek = itemView.findViewById(R.id.imgWeek);
            txtWeek = itemView.findViewById(R.id.txtWeek);
            lstSchedule = itemView.findViewById(R.id.lstSchedule);

            layoutManager = new LinearLayoutManager(itemView.getContext());
            adapter = new BaseRecyclerAdapter<ScheduleDAO.ScheduleBean, ShecduleViewHolder>(
                    R.layout.item_weekly, ShecduleViewHolder.class
            ) {
                @Override
                public void dataConvertViewHolder(ShecduleViewHolder holder, ScheduleDAO.ScheduleBean data) {
                    holder.txtTitle.setText(data.title);
                }
            };

            lstSchedule.setLayoutManager(layoutManager);
            lstSchedule.setAdapter(adapter);
        }

        public static class ShecduleViewHolder extends RecyclerView.ViewHolder{

            public TextView txtTitle;
            public ShecduleViewHolder(View itemView) {
                super(itemView);
                txtTitle = itemView.findViewById(R.id.txtTitle);
            }
        }
    }
}
