package jp.gr.uchiwa.blackout.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import jp.gr.uchiwa.blackout.R;
import jp.gr.uchiwa.blackout.activity.BlackoutScheduleActivity;
import jp.gr.uchiwa.blackout.widget.BlackoutScheduleWidget;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * 
 * @author matsumoto
 *
 */
public class BlackoutScheduleWidgetService extends Service {

    public final static String FIRST_ACTION = "FIRST_ACTION";
    private final String BUTTON_CLICK_ACTION = "BUTTON_CLICK_ACTION";
    private final SimpleDateFormat sdformat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    @Override
	public void onStart(Intent intent, int startId) {
		
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

    		// タイトル設定
    		remoteViews.setTextViewText(R.id.txtvwTitle, "　団扇～九電計画停電～");

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
            remoteViews.setOnClickPendingIntent(R.id.txtvwTitle, pendingNew);
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
		remoteViews.setTextViewText(R.id.txtvwWidget, "　" + sdformat.format(date) + " 時点　" + getDisplayData(calendar));
	}

	/**
	 * 
	 * @param calendar
	 * @return
	 */
    private String getDisplayData(Calendar calendar) {
    	StringBuffer sb = new StringBuffer();
    	
        final Db db = new Db(this);
        
        SimpleDateFormat sdformatDate = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdformatTime = new SimpleDateFormat("HH:mm");
        Calendar dateWhere = (Calendar)calendar.clone();
        
        if ("21:00".compareTo(sdformatTime.format(dateWhere.getTime())) <= 0) {
        	dateWhere.add(Calendar.DATE, 1);
        	sb.append("明日の");
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
    	sbSql.append(" and sc.do_date = '" + param + "'");
    	
    	StringBuffer sbSqlMain = new StringBuffer();
    	sbSqlMain.append("select ");
    	sbSqlMain.append("    scam.total total_am, ");
    	sbSqlMain.append("    scpm.total total_pm ");
    	sbSqlMain.append("from ");
    	sbSqlMain.append("    (" + sbSql.toString() + " and sc.start_time < '12:00'" + ") scam, ");
    	sbSqlMain.append("    (" + sbSql.toString() + " and sc.start_time >= '12:00'" + ") scpm ");
    	sbSqlMain.append(" ; ");
        
        final Cursor cursor = db.getDatabase().rawQuery(sbSqlMain.toString(),  null);
        
        cursor.moveToFirst();
        int count = cursor.getCount();
        for (int i = 0; i < count; i++) {
        	String strYotei = new String();
        	strYotei = String.format("予定\n　　 午前:%2s件\n　　 午後:%2s件", cursor.getString(0), cursor.getString(1));
        	
            sb.append(strYotei);
            cursor.moveToNext();
        }
        db.getDatabase().close();
        return sb.toString();
    }

}
