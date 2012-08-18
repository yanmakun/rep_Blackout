package jp.gr.uchiwa.blackout.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.gr.uchiwa.blackout.R;
import jp.gr.uchiwa.blackout.activity.BlackoutScheduleActivity;
import jp.gr.uchiwa.blackout.widget.BlackoutScheduleWidget;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.IBinder;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;

public class BlackoutScheduleWidgetService extends Service {

    public final static String FIRST_ACTION = "FIRST_ACTION";
    private final String BUTTON_CLICK_ACTION = "BUTTON_CLICK_ACTION";
    private final SimpleDateFormat sdformat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    @Override
	public void onStart(Intent intent, int startId) {
		
		// TODO 自動生成されたメソッド・スタブ
		super.onStart(intent, startId);
		
        Context context = this.getApplicationContext();
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_blackout);


        // ボタンが押された場合、情報を更新する。
        if (BUTTON_CLICK_ACTION.equals(intent.getAction())) {
    		Log.v("BlackoutScheduleWidgetService", "BUTTON_CLICK_ACTION");

    		// スケジュール情報表示
    		setSchedule(remoteViews);
        }
        else if (FIRST_ACTION.equals(intent.getAction())) {
    		Log.v("BlackoutScheduleWidgetService", "FIRST_ACTION");
    		//insertBukkenData();
        }
        else
        {
    		Log.v("BlackoutScheduleWidgetService", "other action");

    		// スケジュール情報表示
    		setSchedule(remoteViews);
            
            /* ----------------------------------------  */
            /* テキストをクリックしたらアクティビティが起動するよう設定する。      */
            /* ----------------------------------------  */
            // 起動するActivityのIntentを作成する
            Intent newIntent = new Intent(context, BlackoutScheduleActivity.class);
            // PendingIntentを取得する
            PendingIntent pendingNew = PendingIntent.getActivity(context, 0, newIntent, 0);
            // クリックイベントが上書き消去されないように、updateAppWidgetの前に記述
            remoteViews.setOnClickPendingIntent(R.id.txtvwWidget, pendingNew);
             
            /* -----------------------------------------------------  */
            /* ボタンをクリックしたらPendingIntentによりサービスが発動するよう設定する。      */
            /* -----------------------------------------------------  */
            Intent buttonIntent = new Intent();
            buttonIntent.setAction("BUTTON_CLICK_ACTION");
            PendingIntent pendingButton = PendingIntent.getService(context, 0, buttonIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.btnDataUpdate, pendingButton);

        }
       	
        // ウィジェット情報更新
        ComponentName thisWidget = new ComponentName(context, BlackoutScheduleWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
        
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	/**
	 * 
	 * @param remoteViews
	 */
	private void setSchedule(RemoteViews remoteViews) {
		
		// ボタン上に停電情報を展開
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();

		// ボタン上に停電情報を展開
		remoteViews.setTextViewText(R.id.txtvwWidget, sdformat.format(date) + " 現在\n" + getDisplayData(calendar));
	}

	/**
	 * 
	 * @param calendar
	 * @return
	 */
    private String getDisplayData(Calendar calendar) {
    	StringBuffer sb = new StringBuffer();
    	sb.append("　　");
    	
        final Db db = new Db(this);
        
        SimpleDateFormat sdformatDate = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdformatTime = new SimpleDateFormat("HH:mm");
        Calendar dateWhere = (Calendar)calendar.clone();
        
        if ("21:00".compareTo(sdformatTime.format(dateWhere.getTime())) <= 0) {
        	dateWhere.add(Calendar.DATE, 1);
        	sb.append("明日");
        }
        String param = sdformatDate.format(dateWhere.getTime());
        
    	StringBuffer sbSql = new StringBuffer();
    	sbSql.append("select ");
    	sbSql.append("    count(bu.no) total ");
    	sbSql.append("from ");
    	sbSql.append("    m_bukken bu, ");
    	sbSql.append("    t_schedule sc ");
    	sbSql.append("where ");
    	sbSql.append("     bu.sub_group_name = sc.sub_group ");
    	sbSql.append(" and sc.priority = 1 ");
    	sbSql.append(" and sc.do_date = ?; ");
        
        final Cursor cursor = db.getDatabase().rawQuery(sbSql.toString(),  new String[]{param});

        cursor.moveToFirst();
        int count = cursor.getCount();
        for (int i = 0; i < count; i++) {
            final String cnt = cursor.getString(0);
            sb.append("予定：" + cnt + "件");
            cursor.moveToNext();
        }
        db.getDatabase().close();
        return sb.toString();
    }
    
    /**
     * 
     */
    private void insertBukkenData() {
        final Db db = new Db(this);
        final SQLiteDatabase database = db.getDatabase();
        ContentValues values = new ContentValues();
        values.put(Db.Bukken.COL_NO.toString(), 1);
        values.put(Db.Bukken.COL_BUKKEN_NAME.toString(), "自宅");
        values.put(Db.Bukken.COL_SUB_GROUP_NAME.toString(), "A23");
        values.put(Db.Bukken.COL_URGENT_CONTACT.toString(), "092-111-1111");
        values.put(Db.Bukken.COL_CHARGE_NAME.toString(), "Aさん");
        values.put(Db.Bukken.COL_REMARKS.toString(), "備考XXXXXX");
        long id = database.insert(Db.Bukken.TABLE_NAME, null, values);
        
        if (id <= 0) {
    		Log.e("BlackoutScheduleWidgetService", "insertBukkenData");
        }
    }

}
