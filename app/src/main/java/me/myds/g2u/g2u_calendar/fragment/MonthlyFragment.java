package me.myds.g2u.g2u_calendar.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import me.myds.g2u.g2u_calendar.R;

public class MonthlyFragment extends Fragment{
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

        return viewLayout;
    }

    public static class MonthlyItemView{
        public View itemView;
        public CardView container;
        public ImageView imgWeek;
        public TextView txtDayOfMonth;
        public CardView card;
        public TextView cntSchedule;

        public MonthlyItemView(View _itemView){
            itemView = _itemView;
            container = itemView.findViewById(R.id.container);
            imgWeek = itemView.findViewById(R.id.imgWeek);
            txtDayOfMonth = itemView.findViewById(R.id.txtDayOfMonth);
            card = itemView.findViewById(R.id.card);
            cntSchedule = itemView.findViewById(R.id.cntSchedule);
        }
    }
}

