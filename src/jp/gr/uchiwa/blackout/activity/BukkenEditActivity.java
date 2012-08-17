package jp.gr.uchiwa.blackout.activity;

import java.util.Map;

import jp.gr.uchiwa.blackout.R;
import jp.gr.uchiwa.blackout.service.BukkenEditService;
import jp.gr.uchiwa.blackout.service.Db.Bukken;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BukkenEditActivity extends Activity {

	private EditText            bukkenName;
	private EditText            subGroup;
	private EditText            contactAddress;
	private EditText            contactPerson;
	private Button              bukkenEntry;
	private Map<String, String> data;

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
		final BukkenEditService service = new BukkenEditService(this);
		bukkenEntry.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				data.put(Bukken.COL_BUKKEN_NAME.getName(), bukkenName.getText().toString());
				data.put(Bukken.COL_SUB_GROUP_NAME.getName(), subGroup.getText().toString());
				data.put(Bukken.COL_CHARGE_NAME.getName(), contactPerson.getText().toString());
				data.put(Bukken.COL_URGENT_CONTACT.getName(), contactAddress.getText().toString());
				data.put(Bukken.COL_REMARKS.getName(), null);
				service.editBukken(data);
				final Intent intent = new Intent(BukkenEditActivity.this, BukkenListActivity.class);
				startActivity(intent);
			}
		});
	}

	private void bindData() {
		final BukkenEditService service = new BukkenEditService(this);
		final Intent intent = getIntent();
		final int no = intent.getIntExtra(Bukken.COL_NO.getName(), 0);
		data = service.getBukken(no);
		bukkenName.setText(data.get(Bukken.COL_BUKKEN_NAME.getName()));
		subGroup.setText(data.get(Bukken.COL_SUB_GROUP_NAME.getName()));
		contactPerson.setText(data.get(Bukken.COL_CHARGE_NAME.getName()));
		contactAddress.setText(data.get(Bukken.COL_URGENT_CONTACT.getName()));
//		remarks.setText(data.get(Bukken.COL_REMARKS.getName()));
	}
}
