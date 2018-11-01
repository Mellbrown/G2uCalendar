package me.myds.g2u.g2u_calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class ScheduleDAO extends SQLiteOpenHelper{

    private static ScheduleDAO instance = null;
    public static ScheduleDAO getInstance(Context context){
        if(instance == null)
            instance = new ScheduleDAO(context,"g2u-calendar.db",null,1);
        return instance;
    }

    public ScheduleDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ScheduleDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table schedule(db_id integer primary key autoincrement, title text, date timestamp)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addSchedule(ScheduleBean schedule){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL( String.format("insert into schedule(title, date) values('%s', '%d')",schedule.title, schedule.timestamp));
        db.close();
    }


    public ArrayList<ScheduleBean> getSchedules(ymd startDate, ymd endDate){
        ArrayList<ScheduleBean> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                String.format("select * from schedule where %d <= date and date < %d order by db_id asc",
                        ymd2timestamp(startDate.year, startDate.month, startDate.dayOfMonth),
                        ymd2timestamp(endDate.year, endDate.month, endDate.dayOfMonth)),null);
        while(cursor.moveToNext()){
            result.add(new ScheduleBean(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getLong(2)
            ));
        }
        return result;
    }

    public static class ScheduleBean{
        public Integer db_id;
        public String title;
        public long timestamp;
        public ScheduleBean(String _title,long _timestamp){
            title = _title;
            timestamp = _timestamp;
        }
        public ScheduleBean(Integer _db_id, String _title,long _timestamp){
            db_id = _db_id;
            title = _title;
            timestamp = _timestamp;
        }
        public ScheduleBean(String _title, int _year, int _month, int _dayOfMonth){
            title = _title;
            timestamp = ymd2timestamp(_year,_month,_dayOfMonth);
        }

        public ymd getYmd(){
            return timestamp2ymd(timestamp);
        }
    }

    public static class ymd implements Cloneable{
        public int year;
        public int month;
        public int dayOfMonth;

        public ymd(int _year, int _month, int _dayOfMonth){
            year = _year;
            month = _month;
            dayOfMonth = _dayOfMonth;
        }
    }
    public static long ymd2timestamp(int year, int month, int dayOfMonth){
        Calendar cal  = new GregorianCalendar();
        cal.set(year,month -1,dayOfMonth,00,00);
        return cal.getTimeInMillis();
    }
    public static ymd timestamp2ymd(long timestamp){
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(timestamp);
        return new ymd(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
    }


}
