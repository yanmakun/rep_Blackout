package jp.gr.uchiwa.blackout.widget;

import jp.gr.uchiwa.blackout.service.BlackoutScheduleWidgetService;
import jp.gr.uchiwa.blackout.service.ScheduleService;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * 
 * @author matsumoto
 *
 */
public class BlackoutScheduleWidget extends AppWidgetProvider {

	public static final Uri CONTENT_URI = Uri.parse("content://jp.gr.uchiwa.blackout.activity");
	  
	/**
	 * AppWidgetが作成される際に呼ばれます。
	 */
	@Override
	public void onEnabled(Context context) {
		Log.v("BukkenWidgetProvider", "onEnabled");
		
		super.onEnabled(context);
		
		// DB読み込み
        new ScheduleService(context).refreshInBackground();
        
        // データ作成時に使用
        // ウィジェット初回作成時にサービス起動
        Intent serviceIntent = new Intent(context, BlackoutScheduleWidgetService.class);
        serviceIntent.setAction(BlackoutScheduleWidgetService.FIRST_ACTION);
        context.startService(serviceIntent);
        
	}
	
	/**
	 * AppWidgetが更新される際に呼ばれます。
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
	 * AppWidgetが削除された際に呼ばれます。
	 */
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		Log.v("BukkenWidgetProvider", "onDeleted");
		super.onDeleted(context, appWidgetIds);
	}
	
	/**
	 * AppWidgetが全て削除された際に呼ばれます。
	 */
	@Override
	public void onDisabled(Context context) {
		Log.v("BukkenWidgetProvider", "onDisabled");
		super.onDisabled(context);
	}
	
	/**
	 * アクションを受け取り、AppWidgetProviderの各メソッドの呼び出しを処理します。
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("BukkenWidgetProvider", "onReceive");
		super.onReceive(context, intent);
	}
}
