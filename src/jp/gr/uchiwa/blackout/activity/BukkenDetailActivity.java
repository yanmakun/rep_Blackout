package jp.gr.uchiwa.blackout.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.gr.uchiwa.blackout.R;
import jp.gr.uchiwa.blackout.service.Db;
import jp.gr.uchiwa.blackout.service.IntentKey;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.TextView;

/**
 * 
 * @author usagichoco
 *
 */
public class BukkenDetailActivity extends Activity {

	private TextView bukkenName;
	private TextView subGroup;
	private TextView contactAddress;
	private TextView contactPerson;
	private TextView linkHomePage;

    @Override 
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
		setContentView(R.layout.activity_bukken_detail);
		findView();
		// データ表示
		setData();
		// リンク情報作成
		addLink();
    }
	
	private void findView() {
		bukkenName     	= (TextView) findViewById(R.id.bukkenName);
		subGroup       	= (TextView) findViewById(R.id.subGroup);
		contactAddress 	= (TextView) findViewById(R.id.contactAddress);
		contactPerson  	= (TextView) findViewById(R.id.contactPerson);
		linkHomePage	= (TextView) findViewById(R.id.linkHomePage);
	}

	private void setData() {
		// 物件№取得
		int bukkenNo = getIntent().getIntExtra(IntentKey.BUKKEN_NO, Integer.MIN_VALUE);

		String[] cols = {Db.Bukken.COL_NO.getName()
        				,Db.Bukken.COL_BUKKEN_NAME.getName()
        				,Db.Bukken.COL_SUB_GROUP_NAME.getName()
        				,Db.Bukken.COL_URGENT_CONTACT.getName()
        				,Db.Bukken.COL_CHARGE_NAME.getName()
        				,Db.Bukken.COL_REMARKS.getName()};   
        String selection = Db.Bukken.COL_NO.getName() + " = ?";   
        String[] selectionArgs = {String.valueOf(bukkenNo)};   

		SQLiteDatabase database = new Db(BukkenDetailActivity.this).getDatabase();

        Cursor cursor = database.query(Db.Bukken.TABLE_NAME, cols, selection, selectionArgs, null, null, null);   

        //TextViewに表示   
        while (cursor.moveToNext()){   

        	bukkenName.setText(cursor.getString(1));
        	subGroup.setText(cursor.getString(2));
        	contactAddress.setText(cursor.getString(3));
        	contactPerson.setText(cursor.getString(4));
        }   
	}
	
	private void addLink() {
		String[] alphabet = {	"A","B","C","D","E",
								"F","G","H","I","J",
								"K","L","M","N","O",
								"P","Q","R","S","T",
								"U","V","W","X","Y","Z"};

		// 九州電力ホームページ［TOP］URL
		final String topScheme		= "http://www.kyuden.co.jp/";
		// 九州電力ホームページ［計画停電月間カレンダー］URL
		final String calendarScheme;
		String calendarScheme1	= "http://www2.kyuden.co.jp/kt_search/index.php/blackout_group/calendar/";
		String calendarScheme2	= "/0";
		StringBuilder sb = new StringBuilder();

		// 1桁目の英字を数値に変換（A⇒0、B⇒1）
		int subAlphabet = 0;
		int subNumber	= Integer.valueOf(subGroup.getText().toString().substring(1)).intValue();

		String alp = subGroup.getText().toString().substring(0, 1);
		for (int i = 0; i < alphabet.length; i++) {
			if (alphabet[i].toString().equals(alp)) {
				subAlphabet = i;
				break;
			}
		}
		
		// 計画停電月間カレンダーURL組立
		sb.append(calendarScheme1.toString());
		sb.append(String.valueOf(subNumber));
		sb.append(String.valueOf(subAlphabet));
		sb.append(calendarScheme2);
		calendarScheme = sb.toString();
		
		Pattern topPattern 		= Pattern.compile(linkHomePage.getText().toString());		
		Pattern calendarPattern	= Pattern.compile(subGroup.getText().toString());		

		Linkify.TransformFilter topFilter = new Linkify.TransformFilter() {			
			public String transformUrl(Matcher match, String url) {
				return topScheme;
			}
		};
		Linkify.TransformFilter calendarFilter = new Linkify.TransformFilter() {			
			public String transformUrl(Matcher match, String url) {
				return calendarScheme;
			}
		};
		
		Linkify.MatchFilter topMatchFilter = new Linkify.MatchFilter() {
			public boolean acceptMatch(CharSequence s, int start, int end) {
				return true;
			}
		};
		Linkify.MatchFilter calendarMatchFilter = new Linkify.MatchFilter() {
			public boolean acceptMatch(CharSequence s, int start, int end) {
				return true;
			}
		};

		// リンク作成
		Linkify.addLinks(linkHomePage, topPattern, topScheme, topMatchFilter, topFilter);
		Linkify.addLinks(subGroup, calendarPattern, calendarScheme, calendarMatchFilter, calendarFilter);
		
	}
	
}
