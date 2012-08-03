package jp.gr.uchiwa.blackout.activity;

import jp.gr.uchiwa.blackout.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BukkenEditActivity extends Activity {

	private EditText bukkenName;
	private EditText subGroup;
	private EditText contactAddress;
	private EditText contactPerson;
	private Button   bukkenEntry;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bukken_edit);
		findView();
		addEventHandler();
		bindData();
	}

	private void findView() {
		bukkenName     = (EditText) findViewById(R.id.bukkenName);
		subGroup       = (EditText) findViewById(R.id.subGroup);
		contactAddress = (EditText) findViewById(R.id.contactAddress);
		contactPerson  = (EditText) findViewById(R.id.contactPerson);
		bukkenEntry    = (Button) findViewById(R.id.bukkenUpdate);
	}

	private void addEventHandler() {
		bukkenEntry.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			}
		});
	}

	private void bindData() {
	}
}
