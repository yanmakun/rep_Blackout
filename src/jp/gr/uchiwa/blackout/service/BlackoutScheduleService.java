package jp.gr.uchiwa.blackout.service;

import java.util.ArrayList;
import java.util.List;

import jp.gr.uchiwa.blackout.model.TimeZoneDetail;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class BlackoutScheduleService {

    /**
     * DBより表示されている"日付"で時間割リストを設定する
     * 
     * @return
     */
    @SuppressWarnings("finally")
	public List<TimeZoneDetail> getTimeZoneListForSQLite(Context context,String date){
    
    	List<TimeZoneDetail> resultList = new ArrayList<TimeZoneDetail>();
    	
    	// DBの定義
    	final Db db = new Db(context);
    	
    	StringBuffer sql= new StringBuffer();
    	sql.append("SELECT " + Db.Bukken.COL_NO + "," + Db.Bukken.COL_BUKKEN_NAME + "," + Db.Bukken.TABLE_NAME + "." +Db.Bukken.COL_SUB_GROUP_NAME +"," + Db.Schedule.COL_START_TIME + ","+ Db.Schedule.COL_END_TIME + "," + Db.Schedule.COL_PRIORITY + " ");
    	sql.append("FROM " + Db.Schedule.TABLE_NAME + " INNER JOIN " + Db.Bukken.TABLE_NAME + " ");
    	sql.append("WHERE " + Db.Schedule.TABLE_NAME + "." + Db.Schedule.COL_SUB_GROUP + " = " + Db.Bukken.TABLE_NAME + "." + Db.Bukken.COL_SUB_GROUP_NAME + " ");
    	sql.append("AND " + Db.Schedule.COL_DO_DATE + " = '" + date + "' ");
    	sql.append("ORDER BY " + Db.Schedule.COL_START_TIME + "," + Db.Schedule.COL_PRIORITY);

    	Log.v("SQL",sql.toString());

    	final Cursor cursor = db.getDatabase().rawQuery(sql.toString(),null);
    	
    	try{    	
	        for (final Cursor _ : Db.toIterable(cursor)) {
	        	TimeZoneDetail timeZoneDetail = new TimeZoneDetail(
		        			Db.getString(cursor, Db.Bukken.COL_NO),
		        			Db.getString(cursor, Db.Bukken.COL_BUKKEN_NAME),
		        			Db.getString(cursor, Db.Bukken.COL_SUB_GROUP_NAME),
		        			date,
		        			Db.getString(cursor, Db.Schedule.COL_START_TIME),
		        			Db.getString(cursor, Db.Schedule.COL_END_TIME),
		        			Db.getInteger(cursor, Db.Schedule.COL_PRIORITY)
	        			);
	        	resultList.add(timeZoneDetail);
	        	Log.v("SQL",timeZoneDetail.toString());
	        }
    	}catch(Exception e){
    		Log.v("ERR",e.toString());
    	}finally{
            cursor.close();
            db.close();
        	return resultList;
    	}
    }
}
