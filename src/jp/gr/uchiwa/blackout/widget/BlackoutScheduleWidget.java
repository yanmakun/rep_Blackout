package jp.gr.uchiwa.blackout.widget;

import java.util.ArrayList;
import java.util.List;
import jp.gr.uchiwa.blackout.R;
import jp.gr.uchiwa.blackout.activity.BlackoutScheduleActivity;
import jp.gr.uchiwa.blackout.model.TimeZone;
import jp.gr.uchiwa.blackout.model.TimeZoneDetail;
import jp.gr.uchiwa.blackout.service.BlackoutScheduleWidgetService;
import jp.gr.uchiwa.blackout.service.ScheduleService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

public class BlackoutScheduleWidget extends AppWidgetProvider {

	public static final Uri CONTENT_URI = Uri.parse("content://jp.gr.uchiwa.blackout.activity");
	  
	@Override
	public void onEnabled(Context context) {
		Log.v("BukkenWidgetProvider", "onEnabled");
		
//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_blackout);
//        remoteViews.setTextViewText(R.id.textView, "へい！");
		
//        Intent intent = new Intent(context, ScheduleService.class);
//        context.startService(intent);
	    
		super.onEnabled(context);
//        new ScheduleService(context).refreshInBackground();
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		
		Log.v("BukkenWidgetProvider", "onUpdate");
/*		
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_blackout);
        
        
        remoteViews.setTextViewText(R.id.textView, "九電停電情報★\nウィジェット！！！");
       
        for(int id : appWidgetIds) {
        	
            // 起動するActivityのIntentを作成する
            Intent intent = new Intent(context, BlackoutScheduleActivity.class);

	        // PendingIntentを取得する
	        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
	        
	        // クリックイベントが上書き消去されないように、updateAppWidgetの前に記述
	        remoteViews.setOnClickPendingIntent(R.id.textView, pendingIntent);
	        
	        appWidgetManager.updateAppWidget(id, remoteViews);
        }*/
		
//        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Intent serviceIntent = new Intent(context, BlackoutScheduleWidgetService.class);
        serviceIntent.setAction("FIRST_ACTION");
        context.startService(serviceIntent);
	}
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		Log.v("BukkenWidgetProvider", "onDeleted");
		super.onDeleted(context, appWidgetIds);
	}
	
	@Override
	public void onDisabled(Context context) {
		Log.v("BukkenWidgetProvider", "onDisabled");
		super.onDisabled(context);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("BukkenWidgetProvider", "onReceive");
		super.onReceive(context, intent);
	}
}
