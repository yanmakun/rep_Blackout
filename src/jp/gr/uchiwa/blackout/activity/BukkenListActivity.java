package jp.gr.uchiwa.blackout.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.gr.uchiwa.blackout.R;
import jp.gr.uchiwa.blackout.service.BukkenListService;
import jp.gr.uchiwa.blackout.service.Db.Bukken;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BukkenListActivity extends Activity {

    private static final int REQUEST_CODE_DUMMY = 0;
    
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
		moveToBukkenEditWhenBukkenNothing();
	}
	
	@Override
	protected void onActivityResult(int pRequestCode, int pResultCode, Intent pData) {
	    if (pResultCode != RESULT_OK) {
	        return;
	    }
	    bindData();
	}

	private void findView() {
		moveToBukkenEdit       = (Button)   findViewById(R.id.moveToBukkenEdit);
		moveToBlackoutSchedule = (Button)   findViewById(R.id.moveToBlackoutSchedule);
		bukkenList             = (ListView) findViewById(R.id.bukkenList);
	}

	private void customizeView() {
		final TextView bukkenListHeader = new TextView(this);
		bukkenListHeader.setText("物件名／サブグループ");
		bukkenListHeader.setTextSize(16);
		bukkenList.addHeaderView(bukkenListHeader);
		registerForContextMenu(bukkenList);
	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("操作");
		menu.add("編集");
		menu.add("削除");
		menu.add("閉じる");
	}

	public boolean onContextItemSelected(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final String title = (String) item.getTitle();
		final Intent intent = new Intent(BukkenListActivity.this, BukkenEditActivity.class);
		final Map<String, String> data = dataList.get(info.position - 1);
		final int no = Integer.parseInt(data.get(Bukken.COL_NO.getName()));
		final BukkenListService service = new BukkenListService(this);
		if ("編集".equals(title)) {
			intent.putExtra(Bukken.COL_NO.getName(), no);
			startActivityForResult(intent, REQUEST_CODE_DUMMY);
		} else if ("削除".equals(title)) {
			service.deleteBukken(no);
			adapter.remove(data.get(Bukken.COL_BUKKEN_NAME.getName()) + "／" + data.get(Bukken.COL_SUB_GROUP_NAME.getName()));
			dataList.remove(info.position - 1);
			adapter.notifyDataSetChanged();
		}
		return true;
	}

	private void addEventHandler() {
		moveToBukkenEdit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final Intent intent = new Intent(BukkenListActivity.this, BukkenEditActivity.class);
				intent.putExtra(Bukken.COL_NO.getName(), 0);
				startActivityForResult(intent, REQUEST_CODE_DUMMY);
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

	private void moveToBukkenEditWhenBukkenNothing() {
		if (adapter.getCount() != 0) {
			return;
		}
		final AlertDialog.Builder message = new AlertDialog.Builder(this);
		message.setTitle("物件情報チェック");
		message.setMessage("物件情報が未登録です。");
		message.setPositiveButton("登録する", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				final Intent intent = new Intent(BukkenListActivity.this, BukkenEditActivity.class);
				intent.putExtra(Bukken.COL_NO.getName(), 0);
				startActivityForResult(intent, REQUEST_CODE_DUMMY);
			}
		});
		message.setNegativeButton("閉じる", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 何もしない。
			}
		});
		message.show();
	}
}
