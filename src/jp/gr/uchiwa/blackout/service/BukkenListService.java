package jp.gr.uchiwa.blackout.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.gr.uchiwa.blackout.service.Db.Bukken;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BukkenListService {

	private final Context context;

	public BukkenListService(final Context pContext) {
		this.context = pContext;
	}

	public List<HashMap<String, String>> getBukkenList() {
		final Db db = new Db(this.context);
		final SQLiteDatabase sqlite = db.getDatabase();
		List<HashMap<String, String>> dataList = null;
		try {
			sqlite.beginTransaction();
			final Cursor cursor = sqlite.query
				(
					Bukken.TABLE_NAME,
					new String[]{
							Bukken.COL_NO.getName(),
							Bukken.COL_BUKKEN_NAME.getName(),
							Bukken.COL_SUB_GROUP_NAME.getName()
					},
					null,
					new String[]{},
					null,
					Bukken.COL_NO.getName().concat(" desc"),
					null
				);
			dataList = convertCursorToList(cursor);
			cursor.close();
			sqlite.setTransactionSuccessful();
		} catch (Exception e) {
			// どうしよう・・・。
		} finally {
			sqlite.endTransaction();
			db.close();
		}
		return dataList;
	}

	private List<HashMap<String, String>> convertCursorToList(Cursor pCursor) {
		final List<HashMap<String, String>> dataList = new ArrayList<HashMap<String,String>>();
		pCursor.moveToFirst();
		for (int i = 0; i < pCursor.getCount() - 1; i++) {
			final HashMap<String, String> data = new HashMap<String, String>();
			for (int j = 0; j < pCursor.getColumnCount() - 1; j++) {
				data.put(pCursor.getColumnName(0), pCursor.getString(0));
			}
			dataList.add(data);
			pCursor.moveToNext();
		}
		return dataList;
	}

	public List<HashMap<String, String>> getBukkenListTest() {
		final List<HashMap<String, String>> dataList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("No", "1");
		data.put("HousingName", "物件名1");
		data.put("SubGroupName", "サブグループ1");
		dataList.add(data);
		data = new HashMap<String, String>();
		data.put("No", "2");
		data.put("HousingName", "物件名2");
		data.put("SubGroupName", "サブグループ2");
		dataList.add(data);
		data = new HashMap<String, String>();
		data.put("No", "3");
		data.put("HousingName", "物件名3");
		data.put("SubGroupName", "サブグループ3");
		dataList.add(data);
		data = new HashMap<String, String>();
		data.put("No", "4");
		data.put("HousingName", "物件名4");
		data.put("SubGroupName", "サブグループ4");
		dataList.add(data);
		data = new HashMap<String, String>();
		data.put("No", "5");
		data.put("HousingName", "物件名5");
		data.put("SubGroupName", "サブグループ5");
		dataList.add(data);
		data = new HashMap<String, String>();
		data.put("No", "6");
		data.put("HousingName", "物件名6");
		data.put("SubGroupName", "サブグループ6");
		dataList.add(data);
		data = new HashMap<String, String>();
		data.put("No", "7");
		data.put("HousingName", "物件名7");
		data.put("SubGroupName", "サブグループ7");
		dataList.add(data);
		data = new HashMap<String, String>();
		data.put("No", "8");
		data.put("HousingName", "物件名8");
		data.put("SubGroupName", "サブグループ8");
		dataList.add(data);
		data = new HashMap<String, String>();
		data.put("No", "9");
		data.put("HousingName", "物件名9");
		data.put("SubGroupName", "サブグループ9");
		dataList.add(data);
		return dataList;
	}
}
