package jp.gr.uchiwa.blackout.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BukkenListService {

	private final Context context;

	public BukkenListService(final Context pContext) {
		this.context = pContext;
	}

	public Cursor getBukkenList() {
		final Db db = new Db(this.context);
		final SQLiteDatabase sqlite = db.getDatabase();
		Cursor cursor = null;
		try {
			sqlite.beginTransaction();
			cursor = sqlite.query
				(
					"",
					new String[]{},
					null,
					new String[]{},
					null,
					"no desc",
					null
				);
			sqlite.setTransactionSuccessful();
		} catch (Exception e) {
			// どうしよう・・・。
		} finally {
			sqlite.endTransaction();
			db.close();
		}
		return cursor;
	}
}
