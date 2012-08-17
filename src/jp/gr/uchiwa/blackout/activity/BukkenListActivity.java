package jp.gr.uchiwa.blackout.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.gr.uchiwa.blackout.R;
import jp.gr.uchiwa.blackout.service.BukkenListService;
import jp.gr.uchiwa.blackout.service.Db.Bukken;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
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
		final TextView bukkenListHeader = new TextView(this);
		bukkenListHeader.setText("物件名／サブグループ");
		bukkenList.addHeaderView(bukkenListHeader);
		registerForContextMenu(bukkenList);
	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("操作");
		menu.add("編集");
		menu.add("削除");
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		String title = (String) item.getTitle();
		if ("編集".equals(title)) {
			Intent intent = new Intent(BukkenListActivity.this, BukkenEditActivity.class);
			final Map<String, String> data = dataList.get(info.position);
			final int no = Integer.parseInt(data.get(Bukken.COL_NO.getName()));
			intent.putExtra(Bukken.COL_NO.getName(), no);
			startActivity(intent);
		} else {
			dataList.remove(info.position);
		}
		return true;
	}

	private void addEventHandler() {
		moveToBukkenEdit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final Intent intent = new Intent(BukkenListActivity.this, BukkenEditActivity.class);
				intent.putExtra(Bukken.COL_NO.getName(), 0);
				startActivity(intent);
			}
		});
		moveToBlackoutSchedule.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final Intent intent = new Intent(BukkenListActivity.this, BlackoutScheduleActivity.class);
				startActivity(intent);
			}
		});
	}

	private void bindData() {
		final BukkenListService service = new BukkenListService(this);
		final List<String> list = new ArrayList<String>();
		dataList = service.getBukkenList();
		for (int i = 0; i < dataList.size(); i++) {
			final HashMap<String, String> data = dataList.get(i);
			list.add(data.get(Bukken.COL_BUKKEN_NAME.getName()) + "／" + data.get(Bukken.COL_SUB_GROUP_NAME.getName()));
		}
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		bukkenList.setAdapter(adapter);
	}
}
