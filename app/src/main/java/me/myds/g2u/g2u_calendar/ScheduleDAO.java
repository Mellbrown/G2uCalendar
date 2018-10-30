package me.myds.g2u.g2u_calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class ScheduleDAO extends SQLiteOpenHelper{
    public ScheduleDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ScheduleDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table schedule(_id integer primary key autoincrement, title text, date timestamp)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addSchedule(ScheduleBean schedule){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL( String.format("insert into schedule(title, date) values(%s, %ld)",schedule.title, schedule.timestamp));
        db.close();
    }

    public HashMap<Long,ScheduleBean> getSchedules(int year, int month){
        HashMap<Long, ScheduleBean> result = new HashMap<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                String.format("select * from schedule where %ld <= date and date < %ld",
                        ymd2timestamp(year,month,1),
                        ymd2timestamp(year,month+1,1)),null);
        while(cursor.moveToNext()){
            result.put(cursor.getLong(2),new ScheduleBean(
                    cursor.getString(1),
                    cursor.getLong(0)
            ));
        }
        return result;
    }

    public static class ScheduleBean{
        public String title;
        public long timestamp;
        public ScheduleBean(String _title,long _timestamp){
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

    public static class ymd{
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
