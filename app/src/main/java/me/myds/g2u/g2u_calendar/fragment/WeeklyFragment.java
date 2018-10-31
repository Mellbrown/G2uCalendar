package me.myds.g2u.g2u_calendar.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import me.myds.g2u.g2u_calendar.R;

public class WeeklyFragment extends Fragment{

    private TextView txtWeekOfMonth;
    private FloatingActionButton btnPrev;
    private FloatingActionButton btnNext;
    private LinearLayout colsWeek;

    private ArrayList<WeekItem> weekItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewLayout = inflater.inflate(R.layout.fragment_weekly, container,false);

        txtWeekOfMonth = viewLayout.findViewById(R.id.txtWeekOfMonth);
        btnPrev = viewLayout.findViewById(R.id.btnPrev);
        btnNext = viewLayout.findViewById(R.id.btnNext);
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

        return viewLayout;
    }

    public static class WeekItem{
        public View itemView;
        public ImageView imgWeek;
        public RecyclerView lstSchedule;

        public WeekItem(View _itemView){
            itemView = _itemView;
            imgWeek = itemView.findViewById(R.id.imgWeek);
            lstSchedule = itemView.findViewById(R.id.lstSchedule);
        }
    }
}
