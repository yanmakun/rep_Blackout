package jp.gr.uchiwa.blackout.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.gr.uchiwa.blackout.R;
import jp.gr.uchiwa.blackout.service.BukkenListService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BukkenListActivity extends Activity {

	private Button                        moveToBukkenEdit;
	private Button                        moveToBlackoutSchedule;
	private ListView                      bukkenList;
	private List<HashMap<String, String>> dataList;
	private ArrayAdapter<String>          adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bukken_list);
		findView();
		customizeView();
		addEventHandler();
		bindData();
	}

	private void findView() {
		moveToBukkenEdit       = (Button) findViewById(R.id.moveToBukkenEdit);
		moveToBlackoutSchedule = (Button) findViewById(R.id.moveToBlackoutSchedule);
		bukkenList             = (ListView) findViewById(R.id.bukkenList);
	}

	private void customizeView() {
		TextView bukkenListHeader = new TextView(this);
		bukkenListHeader.setText("物件名／サブグループ");
		bukkenList.addHeaderView(bukkenListHeader);
	}

	private void addEventHandler() {
		moveToBukkenEdit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(BukkenListActivity.this, BukkenEditActivity.class);
				startActivity(intent);
			}
		});
		moveToBlackoutSchedule.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(BukkenListActivity.this, BlackoutScheduleActivity.class);
				startActivity(intent);
			}
		});
	}

	private void bindData() {
		final BukkenListService service = new BukkenListService(this);
		final List<String> list = new ArrayList<String>();
//		dataList = service.getBukkenList();
		dataList = service.getBukkenListTest();
		for (int i = 0; i < dataList.size(); i++) {
			final HashMap<String, String> data = dataList.get(i);
			list.add(data.get("HousingName") + "／" + data.get("SubGroupName"));
		}
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		bukkenList.setAdapter(adapter);
	}
}
