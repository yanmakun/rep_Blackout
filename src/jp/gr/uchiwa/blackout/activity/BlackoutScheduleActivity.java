package jp.gr.uchiwa.blackout.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.gr.uchiwa.blackout.model.TimeZone;
import jp.gr.uchiwa.blackout.model.TimeZoneDetail;
import jp.gr.uchiwa.blackout.service.BlackoutScheduleService;
import jp.gr.uchiwa.blackout.service.Db;
import jp.gr.uchiwa.blackout.service.ScheduleService;
import jp.gr.uchiwa.blackout.R;
import jp.gr.uchiwa.blackout.R.id;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

/**
 * 
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

    // 日付
    private String picDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 九電から最新の計画停電データを取得
        //new ScheduleService(this).refreshInBackground();

        // 画面設定
        setContentView(R.layout.activity_blackout_schedule);
        DatePicker datePicker = (DatePicker) findViewById(id.datePicker1); // 日付選択ボックス
        ExpandableListView exListView1 = (ExpandableListView) findViewById(id.expandableListView1); // 時間帯リスト

        // 日付選択ボックスに現在日付の設定
        Calendar c = Calendar.getInstance();
        datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),this);
        
        // 日付選択ボックス用日付形式
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/m/d");
        setPicDate(sf.format(c.getTime()));
        
        // 時間帯リスト用ノード
        List<Map<String, Object>> parentList = new ArrayList<Map<String,Object>>(); // 親ノードリスト
        List<List<Map<String, Object>>> allChildList = new ArrayList<List<Map<String,Object>>>(); // 子ノードリスト
        
        // 九電スケジュールから取得
        List<TimeZone> timeZoneList = makeTimeZoneTestList();

        // 時間割を設定        
        for(TimeZone timeZone : timeZoneList){
        	
        	Map<String, Object> parentData = new HashMap<String, Object>();
        	List<TimeZoneDetail> detailList = timeZone.getTimeZoneDetailList();

        	parentData.put(KEY_TIMEZONE,timeZone.getTimeZoneName());
        	parentList.add(parentData);

        	List<Map<String, Object>> childList = new ArrayList<Map<String,Object>>();

            for(TimeZoneDetail timeZoneDetail : detailList){

                Map<String, Object> childData = new HashMap<String, Object>();

                childData.put(KEY_BUKKEN_NAME, timeZoneDetail.getNo() + ":" + timeZoneDetail.getBukkenName());
                childData.put(KEY_SUBGROUP_NAME, timeZoneDetail.getSubGroupName());
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
                new String []{KEY_BUKKEN_NAME, KEY_SUBGROUP_NAME},
                new int []{android.R.id.text1, android.R.id.text2}
        );
        exListView1.setAdapter(adapter);

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

                return false;
            }
        });
    }

    // 表示日付が変更されたら
    public void onDateChanged(DatePicker view, int year, int monthOfYear,
            int dayOfMonth) {
        view.updateDate(year, monthOfYear, dayOfMonth);
        setPicDate("" + year + "/" + monthOfYear + "/" + dayOfMonth);
    }
    
    /**
     * 時間帯ごとに取得したスケジュールを分割する
     * @return 時間帯データリスト
     */
    public List<TimeZone> makeTimeZoneTestList(){
    
    	List<TimeZone> resultTimeZoneList = new ArrayList<TimeZone>();
    	BlackoutScheduleService blackoutScheduleService = new BlackoutScheduleService();

    	//ここで一覧をタイムゾーンごとに分割して再度リストに突っ込む。
    	ArrayList<TimeZoneDetail> scheduleListOfDate = (ArrayList<TimeZoneDetail>)blackoutScheduleService.getTimeZoneListForSQLite(this, "2012/08/01");
    	
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
    	
    	resultTimeZoneList.add(new TimeZone("01","8:30～11:00",timeZoneList1));
    	resultTimeZoneList.add(new TimeZone("02","10:30～13:00",timeZoneList2));
    	resultTimeZoneList.add(new TimeZone("03","12:30～15:00",timeZoneList3));
    	resultTimeZoneList.add(new TimeZone("04","14:30～17:00",timeZoneList4));
    	resultTimeZoneList.add(new TimeZone("05","16:30～19:00",timeZoneList5));
    	resultTimeZoneList.add(new TimeZone("06","18:30～21:00",timeZoneList6));
    	
    	return resultTimeZoneList;
    }

	public String getPicDate() {
		return picDate;
	}

	public void setPicDate(String picDate) {
		this.picDate = picDate;
	}
}
