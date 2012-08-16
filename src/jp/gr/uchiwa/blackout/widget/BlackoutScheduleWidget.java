package jp.gr.uchiwa.blackout.widget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jp.gr.uchiwa.blackout.R;
import jp.gr.uchiwa.blackout.activity.BlackoutScheduleActivity;
import jp.gr.uchiwa.blackout.model.TimeZone;
import jp.gr.uchiwa.blackout.model.TimeZoneDetail;
import jp.gr.uchiwa.blackout.service.BlackoutScheduleWidgetService;
import jp.gr.uchiwa.blackout.service.Db;
import jp.gr.uchiwa.blackout.service.ScheduleService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;

/**
 * 
 * @author matsumoto
 *
 */
public class BlackoutScheduleWidget extends AppWidgetProvider {

	public static final Uri CONTENT_URI = Uri.parse("content://jp.gr.uchiwa.blackout.activity");
	  
	/**
	 * 
	 */
	@Override
	public void onEnabled(Context context) {
		Log.v("BukkenWidgetProvider", "onEnabled");
		
		super.onEnabled(context);
		
		// DB読み込み
        new ScheduleService(context).refreshInBackground();
        /*
        // データ作成時に使用
        // ウィジェット初回作成時にサービス起動
        Intent serviceIntent = new Intent(context, BlackoutScheduleWidgetService.class);
        serviceIntent.setAction(BlackoutScheduleWidgetService.FIRST_ACTION);
        context.startService(serviceIntent);
        */
	}
	
	/**
	 * 
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		
		Log.v("BukkenWidgetProvider", "onUpdate");

		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		// ウィジェット生成時＆ボタンクリック時にサービス起動
        Intent serviceIntent = new Intent(context, BlackoutScheduleWidgetService.class);
        context.startService(serviceIntent);
	}
	
	/**
	 * 
	 */
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		Log.v("BukkenWidgetProvider", "onDeleted");
		super.onDeleted(context, appWidgetIds);
	}
	
	/**
	 * 
	 */
	@Override
	public void onDisabled(Context context) {
		Log.v("BukkenWidgetProvider", "onDisabled");
		super.onDisabled(context);
	}
	
	/**
	 * 
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("BukkenWidgetProvider", "onReceive");
		super.onReceive(context, intent);
	}
}
