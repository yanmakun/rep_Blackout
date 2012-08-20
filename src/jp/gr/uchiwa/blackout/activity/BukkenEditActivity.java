package jp.gr.uchiwa.blackout.activity;

import java.util.Map;

import jp.gr.uchiwa.blackout.R;
import jp.gr.uchiwa.blackout.service.BukkenEditService;
import jp.gr.uchiwa.blackout.service.Db.Bukken;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BukkenEditActivity extends Activity {

	private EditText            bukkenName;
	private EditText            subGroupName;
	private EditText            urgentContact;
	private EditText            chargeText;
	private Button              bukkenUpdate;
	private Button              cancel;
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
		bukkenName    = (EditText) findViewById(R.id.bukkenName);
		subGroupName  = (EditText) findViewById(R.id.subGroupName);
		urgentContact = (EditText) findViewById(R.id.urgentContact);
		chargeText    = (EditText) findViewById(R.id.chargeText);
		bukkenUpdate  = (Button)   findViewById(R.id.bukkenUpdate);
		cancel        = (Button)   findViewById(R.id.cancel);
	}

	private void addEventHandler() {
		final BukkenEditService service = new BukkenEditService(this);
		final AlertDialog.Builder validateMessage = new AlertDialog.Builder(this);
		bukkenUpdate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				data.put(Bukken.COL_BUKKEN_NAME.getName(), bukkenName.getText().toString());
				data.put(Bukken.COL_SUB_GROUP_NAME.getName(), subGroupName.getText().toString());
				data.put(Bukken.COL_URGENT_CONTACT.getName(), urgentContact.getText().toString());
				data.put(Bukken.COL_CHARGE_NAME.getName(), chargeText.getText().toString());
				data.put(Bukken.COL_REMARKS.getName(), "");
				if (!validateBukkenUpdate(data)) {
					validateMessage.setTitle("入力チェック");
					validateMessage.setMessage("物件名とサブグループは必ず入力して下さい。");
					validateMessage.setPositiveButton("了解", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// 何もしない。
						}
					});
					validateMessage.show();
					return;
				}
				service.editBukken(data);
				final Intent intent = new Intent(BukkenEditActivity.this, BukkenListActivity.class);
				startActivity(intent);
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
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
		subGroupName.setText(data.get(Bukken.COL_SUB_GROUP_NAME.getName()));
		urgentContact.setText(data.get(Bukken.COL_URGENT_CONTACT.getName()));
		chargeText.setText(data.get(Bukken.COL_CHARGE_NAME.getName()));
	}

	private boolean validateBukkenUpdate(Map<String, String> pData) {
		if (isEmpty(pData.get(Bukken.COL_BUKKEN_NAME.getName()))) {
			return false;
		}
		if (isEmpty(pData.get(Bukken.COL_SUB_GROUP_NAME.getName()))) {
			return false;
		}
		return true;
	}

	private boolean isEmpty(String pValue) {
		if (pValue != null && !"".equals(pValue)) {
			return false;
		}
		return true;
	}
}
