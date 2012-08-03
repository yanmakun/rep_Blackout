package jp.gr.uchiwa.blackout.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BukkenEditService {

	private final Context context;

	public BukkenEditService(final Context pContext) {
		this.context = pContext;
	}

	public void editBukken(final int pNo, final ContentValues values) {
		final Db db = new Db(this.context);
		final SQLiteDatabase sqlite = db.getDatabase();
		try {
			sqlite.beginTransaction();
			if (pNo != 0) {
				sqlite.update
					(
						"",
						values,
						"no = ?",
						new String[]{ Integer.toString(pNo) }
					);
			} else {
				values.put("no", getNextNo(sqlite));
				sqlite.insert
					(
						"",
						null,
						values
					);
			}
			sqlite.setTransactionSuccessful();
		} catch (Exception e) {
			// どうしよう・・・。
		} finally {
			sqlite.endTransaction();
			db.close();
		}
	}

	public Cursor getBukken(final int pNo) {
		final Db db = new Db(this.context);
		final SQLiteDatabase sqlite = db.getDatabase();
		Cursor cursor = null;
		try {
			sqlite.beginTransaction();
			cursor = sqlite.query
				(
					"",
					new String[]{},
					"no = ?",
					new String[]{ Integer.toString(pNo) },
					null,
					null,
					null
				);
			cursor.moveToFirst();
			sqlite.setTransactionSuccessful();
		} catch (Exception e) {
			// どうしよう・・・。
		} finally {
			sqlite.endTransaction();
			db.close();
		}
		return cursor;
	}

	private int getNextNo(final SQLiteDatabase sqlite) {
		Cursor cursor = sqlite.query
			(
				"",
				new String[]{ "no" },
				null,
				null,
				null,
				"no desc",
				"1"
			);
		cursor.moveToFirst();
		return cursor.getInt(0) + 1;
	}
}
