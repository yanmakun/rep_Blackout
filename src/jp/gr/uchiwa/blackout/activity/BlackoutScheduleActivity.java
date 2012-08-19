package jp.gr.uchiwa.blackout.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.gr.uchiwa.blackout.model.TimeZone;
import jp.gr.uchiwa.blackout.model.TimeZoneDetail;
import jp.gr.uchiwa.blackout.service.BlackoutScheduleService;
import jp.gr.uchiwa.blackout.service.IntentKey;
import jp.gr.uchiwa.blackout.service.ScheduleService;
import jp.gr.uchiwa.blackout.service.ScheduleService.IRefreshTask;
import jp.gr.uchiwa.blackout.MainActivity;
import jp.gr.uchiwa.blackout.R;
import jp.gr.uchiwa.blackout.R.id;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

/**
 * 計画停電スケジュール画面
 * @author takuro
 *
 */
public class BlackoutScheduleActivity extends Activity implements OnDateChangedListener{    
	
	// 時間帯リストキー
	/** 物件Noキー */
    public static final String KEY_BUKKEN_NO = "BUKKEN_NO";
    /** 物件名キー */
	public static final String KEY_BUKKEN_NAME= "BUKKEN_NAME";
	/** 時間帯キー */
    public static final String KEY_TIMEZONE = "TIMEZONE";
    /** サブグループキー */
    public static final String KEY_SUBGROUP_NAME = "SUBGROUPNAME";
    /** 優先度キー */
    public static final String KEY_PRIORITY = "PRIORITY";
    /** 優先度 + サブグループキー */
    public static final String KEY_PRIORITY_AND_SUBGROUP = "PRIORITY_AND_SUBGROUP";

    /** メニューID1 */
    private static final int MENU_ID_MENU1 = (Menu.FIRST + 1);
    /** メニューID2 */
    private static final int MENU_ID_MENU2 = (Menu.FIRST + 2);
    
    // 日付
    private String picDate;
    SimpleDateFormat sf;
    
    ExpandableListView exListView1;
    List<Map<String, Object>> parentList;
    List<List<Map<String, Object>>> allChildList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 九電から最新の計画停電データを取得
        new ScheduleService(this).refreshInBackground();

        // 画面設定
        setContentView(R.layout.activity_blackout_schedule);
        DatePicker datePicker = (DatePicker) findViewById(id.datePicker1); // 日付選択ボックス
        exListView1 = (ExpandableListView) findViewById(id.expandableListView1); // 時間帯リスト

        // 日付選択ボックスに現在日付の設定
        Calendar c = Calendar.getInstance();
        datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),this);
        
        // 日付選択ボックス用日付形式
        sf = new SimpleDateFormat("yyyy/MM/dd");
        setPicDate(sf.format(c.getTime()));
        
        // 時間帯リスト用ノード
        parentList = new ArrayList<Map<String,Object>>(); // 親ノードリスト
        allChildList = new ArrayList<List<Map<String,Object>>>(); // 子ノードリスト        

        // 時間帯リストを更新
        updateTimeZoneList();

        // 時間帯リストリスナー
        exListView1.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){

            // 子が選択された時
            @SuppressWarnings("unchecked")
            //@Override
            public boolean onChildClick(
                    ExpandableListView parent,
                    View v,
                    int groupPosition,
                    int childPosition,
                    long id)
            {
                // まずはAdapterを取得
                ExpandableListAdapter adapter = parent.getExpandableListAdapter();

                // Adapterから子のデータMapを取得
                Map<String, Object> childMap = (Map<String, Object>)adapter.getChild(
                        groupPosition,
                        childPosition
                );
                
                Log.v("ChildClick",(childMap.get(KEY_BUKKEN_NO)).toString());
                
                moveBukkenDetailActivity(childMap.get(KEY_BUKKEN_NO).toString());

                return false;
            }
        });
    }
    
    /**
     * メニューボタンの作成
     */
    public boolean onCreateOptionsMenu(Menu menu) {
		boolean ret = super.onCreateOptionsMenu(menu);
		menu.add(0 , Menu.FIRST+1 , Menu.NONE ,"対象物件一覧");
		//menu.add(0 , Menu.FIRST+2 , Menu.NONE ,"メニュー1");

		return ret;
    }
    
    // オプションメニューが表示される度に呼び出されます
    @Override
   public boolean onPrepareOptionsMenu(Menu menu) {

        //menu.findItem(MENU_ID_MENU2).setVisible(visible);
        //visible = !visible;
        return super.onPrepareOptionsMenu(menu);
    }

    // オプションメニューアイテムが選択された時に呼び出されます
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = true;
        switch (item.getItemId()) {
	        default:
	            ret = super.onOptionsItemSelected(item);
	            break;
	        case MENU_ID_MENU1:
	        	// インテントへのインスタンス生成 
	        	Intent intent = new Intent(this,BukkenListActivity.class);
	            // 物件詳細画面へ渡す物件No.を設定
	            //setIntent(intent.putExtra(IntentKey.BUKKEN_NO, Integer.valueOf(no)));
	            // 物件詳細画面へ移動
	            startActivity(intent);        	
	        	
	            //ret = true;
	            break;
	        case MENU_ID_MENU2:
	            //ret = true;
	            break;
        }
        return ret;
    }

    /**
     * 日付選択ボックスの日付変更
     */
    public void onDateChanged(DatePicker view, int year, int monthOfYear,
            int dayOfMonth) {

        view.updateDate(year, monthOfYear, dayOfMonth);
        Date date  = toDate(year+"/"+(monthOfYear+1) +"/" + dayOfMonth);
        setPicDate(sf.format(date));
        
        // 計画停電スケジュールを更新
        updateTimeZoneList();
    }
    
    /**
     * 日付選択リスト-日付取得
     * @return
     */
	public String getPicDate() {
		return picDate;
	}

	/**
	 * 日付選択リスト-日付設定
	 * @param picDate
	 */
	public void setPicDate(String picDate) {
		this.picDate = picDate;
	}

    /**
     * 日付文字列"yyyy/MM/dd"をjava.util.Date型へ変換します。
     * @param str 変換対象の文字列
     * @return 変換後のjava.util.Dateオブジェクト
     * @throws ParseException 日付文字列が"yyyy/MM/dd"以外の場合 
     */
    public static Date toDate(String str) {
    	Date  date = new Date();
    	try{
    		date = DateFormat.getDateInstance().parse(str);
    	}catch (ParseException e) {
    		Log.v("ERR",e.toString());
    	}
        return date;
    }
    /**
     * 時間帯ごとに取得したスケジュールを分割する
     * @return 時間帯データリスト
     */
    private List<TimeZone> makeTimeZoneList(){
    
    	List<TimeZone> resultTimeZoneList = new ArrayList<TimeZone>();
    	BlackoutScheduleService blackoutScheduleService = new BlackoutScheduleService();

    	//ここで一覧をタイムゾーンごとに分割して再度リストに突っ込む。
    	ArrayList<TimeZoneDetail> scheduleListOfDate 
    		= (ArrayList<TimeZoneDetail>)blackoutScheduleService.getTimeZoneListForSQLite(this, getPicDate());
    	
    	// 時間帯リスト
    	List<TimeZoneDetail> timeZoneList1 = new ArrayList<TimeZoneDetail>(); // 8:30開始
    	List<TimeZoneDetail> timeZoneList2 = new ArrayList<TimeZoneDetail>(); // 10:30開始
    	List<TimeZoneDetail> timeZoneList3 = new ArrayList<TimeZoneDetail>(); // 12:30開始
    	List<TimeZoneDetail> timeZoneList4 = new ArrayList<TimeZoneDetail>(); // 14:30開始
    	List<TimeZoneDetail> timeZoneList5 = new ArrayList<TimeZoneDetail>(); // 16:30開始
    	List<TimeZoneDetail> timeZoneList6 = new ArrayList<TimeZoneDetail>(); // 17:30開始	
    	
    	for (TimeZoneDetail timeZoneDetail : scheduleListOfDate) {
        	
        	if( "8:30".equals(timeZoneDetail.getStartTime())){
        		timeZoneList1.add(timeZoneDetail);
        		continue;
        	}
 
        	if( "10:30".equals(timeZoneDetail.getStartTime())){
        		timeZoneList2.add(timeZoneDetail);
        		continue;
        	}

        	if( "12:30".equals(timeZoneDetail.getStartTime())){
        		timeZoneList3.add(timeZoneDetail);
        		continue;
        	}
        	if( "14:30".equals(timeZoneDetail.getStartTime())){
        		timeZoneList4.add(timeZoneDetail);
        		continue;
        	}
 
        	if( "16:30".equals(timeZoneDetail.getStartTime())){
        		timeZoneList5.add(timeZoneDetail);
        		continue;
        	}

        	if( "18:30".equals(timeZoneDetail.getStartTime())){
        		timeZoneList6.add(timeZoneDetail);
        		continue;
        	}
            
        }   	
    	
    	// 時間帯ごとに対象物件が存在している場合に表示するリストへ追加
    	if(timeZoneList1.size() > 0){
    		resultTimeZoneList.add(new TimeZone("01","8:30～11:00",timeZoneList1));
    	}
    	if(timeZoneList2.size() > 0){
    		resultTimeZoneList.add(new TimeZone("02","10:30～13:00",timeZoneList2));
    	}
    	if(timeZoneList3.size() > 0){
    		resultTimeZoneList.add(new TimeZone("03","12:30～15:00",timeZoneList3));
    	}
    	if(timeZoneList4.size() > 0){
    		resultTimeZoneList.add(new TimeZone("04","14:30～17:00",timeZoneList4));
    	}
    	if(timeZoneList5.size() > 0){
    		resultTimeZoneList.add(new TimeZone("05","16:30～19:00",timeZoneList5));
    	}
    	if(timeZoneList6.size() > 0){
    		resultTimeZoneList.add(new TimeZone("06","18:30～21:00",timeZoneList6));
    	}
    	
    	return resultTimeZoneList;
    }
    
    /**
     * 物件No.に対する物件詳細画面へ遷移する
     * @param no:物件No.
     */
    private void moveBukkenDetailActivity(String no){

    	// インテントへのインスタンス生成 
    	Intent intent = new Intent(this,BukkenDetailActivity.class);
        // 物件詳細画面へ渡す物件No.を設定
        setIntent(intent.putExtra(IntentKey.BUKKEN_NO, Integer.valueOf(no)));
        // 物件詳細画面へ移動
        startActivity(intent);
    }
     
	/**
	 * 表示スケジュールを更新
	 */
	private void updateTimeZoneList(){
		
        // 九電スケジュールから取得
        List<TimeZone> timeZoneList = makeTimeZoneList();
        
        // リストをクリア
        parentList.clear();
        allChildList.clear();

        // 時間割を設定        
        for(TimeZone timeZone : timeZoneList){
        	
        	Map<String, Object> parentData = new HashMap<String, Object>();
        	List<TimeZoneDetail> detailList = timeZone.getTimeZoneDetailList();

        	parentData.put(KEY_TIMEZONE,timeZone.getTimeZoneName());
        	parentList.add(parentData);

        	List<Map<String, Object>> childList = new ArrayList<Map<String,Object>>();

            for(TimeZoneDetail timeZoneDetail : detailList){

                Map<String, Object> childData = new HashMap<String, Object>();

                childData.put(KEY_BUKKEN_NAME,timeZoneDetail.getBukkenName());
                childData.put(KEY_SUBGROUP_NAME, timeZoneDetail.getSubGroupName());
                childData.put(KEY_BUKKEN_NO, timeZoneDetail.getNo());
                childData.put(KEY_PRIORITY, timeZoneDetail.getPriority());
                childData.put(KEY_PRIORITY_AND_SUBGROUP, "優先順位：" + timeZoneDetail.getPriority() 
                		+ " 【" + timeZoneDetail.getSubGroupName() + "】");
                childList.add(childData);
            }
            allChildList.add(childList);
        }	

        // 時間帯リストに詰め込み
        ExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                this,
                parentList,
                android.R.layout.simple_expandable_list_item_1,
                new String []{KEY_TIMEZONE},
                new int []{android.R.id.text1},
                allChildList,
                android.R.layout.simple_expandable_list_item_2,
                new String []{KEY_BUKKEN_NAME, KEY_PRIORITY_AND_SUBGROUP},
                new int []{android.R.id.text1, android.R.id.text2}         
                
        );
        
        // 計画停電がない場合
        if(adapter.isEmpty()){
        	Toast.makeText(this, "対象物件の計画停電はありません。",Toast.LENGTH_SHORT).show();
        }
        
        exListView1.setAdapter(adapter);
  
	}
}
