package me.myds.g2u.g2u_calendar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import me.myds.g2u.g2u_calendar.BaseRecyclerAdapter;
import me.myds.g2u.g2u_calendar.R;
import me.myds.g2u.g2u_calendar.ScheduleDAO;

public class DailyFragment extends Fragment{

    public RecyclerView lstSchedule;
    private RecyclerView.LayoutManager layoutManager;
    public BaseRecyclerAdapter<ScheduleDAO.ScheduleBean, DailyScheduleItem> adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewLayout = inflater.inflate(R.layout.fragment_daily, container,false);

        lstSchedule = viewLayout.findViewById(R.id.lstSchedule);

        layoutManager = new LinearLayoutManager(getContext());
        adapter = new BaseRecyclerAdapter<ScheduleDAO.ScheduleBean, DailyScheduleItem>(
                R.layout.item_daily, DailyScheduleItem.class
        ) {
            @Override
            public void dataConvertViewHolder(DailyScheduleItem holder, ScheduleDAO.ScheduleBean data) {
                holder.title.setText(data.title);
            }
        };

        lstSchedule.setLayoutManager(layoutManager);
        lstSchedule.setAdapter(adapter);

        return viewLayout;
    }

    public static class DailyScheduleItem extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView title;

        public DailyScheduleItem(View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = itemView.findViewById(R.id.title);
        }
    }
}
