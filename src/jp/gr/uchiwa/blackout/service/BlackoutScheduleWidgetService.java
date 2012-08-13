package jp.gr.uchiwa.blackout.service;

import jp.gr.uchiwa.blackout.R;
import jp.gr.uchiwa.blackout.activity.BlackoutScheduleActivity;
import jp.gr.uchiwa.blackout.widget.BlackoutScheduleWidget;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class BlackoutScheduleWidgetService extends Service {

	@Override
	public void onStart(Intent intent, int startId) {
		
		// TODO 自動生成されたメソッド・スタブ
		super.onStart(intent, startId);
		
        Context context = this.getApplicationContext();

        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        RemoteViews view = new RemoteViews(getPackageName(), R.layout.widget_blackout);

        if ("FIRST_ACTION".equals( intent.getAction() )) {
            //ウィジェットを画面に貼り付けたとき

            //ボタンをクリックしたらPendingIntentによりサービスが発動するよう設定する。
            Intent newintent = new Intent(context, BlackoutScheduleActivity.class);
            newintent.setAction("BUTTON_CLICK_ACTION");
            PendingIntent pending = PendingIntent.getService(context, 0, newintent, 0);

            //widgetのボタンクリックイベントに呼び出したいIntentを設定する
            view.setOnClickPendingIntent(R.id.btnWidget, pending);
        }
        else if("BUTTON_CLICK_ACTION".equals( intent.getAction() )) {
            //ウィジェットのボタンを押下したとき

            //ダイアログの呼び出し
//            Intent intentActivity = new Intent(context, BlackoutScheduleActivity.class);
//            context.startActivity(intentActivity);
        }
       
        view.setTextViewText(R.id.btnWidget, "九電停電情報☆");
        view.setTextViewText(R.id.textView, "九電停電情報★\nウィジェット！！！");

        // widgetの更新
        ComponentName widget = new ComponentName(context, BlackoutScheduleWidget.class);
        manager.updateAppWidget(widget, view);
        
		/*
        super.onStart(intent, startId);
        
        Context context = this.getApplicationContext();
        Intent buttonIntent = new Intent();

        buttonIntent.setAction("BUTTON_CLICK_ACTION");
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, buttonIntent, 0);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_blackout);
        remoteViews.setOnClickPendingIntent(R.id.btnWidget, pendingIntent);

        if ("BUTTON_CLICK_ACTION".equals(intent.getAction())) {
                remoteViews.setTextViewText(R.id.textView, "九電停電情報★\nウィジェット！！！");
        }
        
        ComponentName thisWidget = new ComponentName(context, BlackoutScheduleWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);*/
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
