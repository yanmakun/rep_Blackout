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
					null,
					Bukken.COL_NO.getName().concat(" desc")
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
				data.put(pCursor.getColumnName(j), pCursor.getString(j));
			}
			dataList.add(data);
			pCursor.moveToNext();
		}
		return dataList;
	}
}
