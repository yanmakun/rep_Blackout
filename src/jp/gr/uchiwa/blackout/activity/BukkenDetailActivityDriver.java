package jp.gr.uchiwa.blackout.activity;

import java.util.Arrays;
import java.util.regex.Pattern;

import jp.gr.uchiwa.blackout.R;
import jp.gr.uchiwa.blackout.service.Db;
import jp.gr.uchiwa.blackout.service.IntentKey;
import android.R.integer;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @author usagichoco
 *
 */
public class BukkenDetailActivityDriver extends Activity {

	private EditText bukkenNo;
	private Button openDetail;

    @Override 
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
		setContentView(R.layout.activity_bukken_detail_driver);
		
		bukkenNo	= (EditText) findViewById(R.id.bukkenNo);
		openDetail	= (Button) findViewById(R.id.openDetail);

		openDetail.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(BukkenDetailActivityDriver.this, BukkenDetailActivity.class);
				intent.putExtra(IntentKey.BUKKEN_NO, Integer.parseInt(bukkenNo.getText().toString()));
				startActivity(intent);
			}
		});

		// サンプルデータ作成
		setData();
    }
    
    private void setData() {
    	//初期投入サンプルデータ	  
    	String[][] datas = new String[][]{  
    			{"1", "test1", "A10", "00000000000", "担当者A", "あああ"},  
    			{"2", "test2", "A20", "11111111111", "部長", "びこう"},  
    			{"3", "test3", "A30", "22222222222", "課長", "コメント"},  
    			{"4", "test4", "B10", "33333333333", "担当者B", "てすと"},  
    			{"5", "test5", "B20", "44444444444", "社長", "テスト"}  
    			};
    	
		SQLiteDatabase database = new Db(BukkenDetailActivityDriver.this).getDatabase();
    	
		for( String[] data: datas){

			ContentValues values = new ContentValues();
			values.put(Db.Bukken.COL_NO.getName(), Integer.getInteger(data[0].toString()));
			values.put(Db.Bukken.COL_BUKKEN_NAME.getName(), data[1]);
			values.put(Db.Bukken.COL_SUB_GROUP_NAME.getName(), data[2]);
			values.put(Db.Bukken.COL_URGENT_CONTACT.getName(), data[3]);
			values.put(Db.Bukken.COL_CHARGE_NAME.getName(), data[4]);
			values.put(Db.Bukken.COL_REMARKS.getName(), data[5]);
			database.insert(Db.Bukken.TABLE_NAME, null, values);			

		}
    }
}
