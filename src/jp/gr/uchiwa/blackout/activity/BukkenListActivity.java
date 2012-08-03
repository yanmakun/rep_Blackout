package jp.gr.uchiwa.blackout.activity;

import jp.gr.uchiwa.blackout.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BukkenListActivity extends Activity {

	private Button   moveToBukkenEdit;
	private Button   moveToBlackoutSchedule;
	private ListView bukkenList;

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
			}
		});
	}

	private void bindData() {
		
	}
}
