package jp.gr.uchiwa.blackout;

import java.util.ArrayList;
import java.util.List;

import jp.gr.uchiwa.blackout.service.Db;
import jp.gr.uchiwa.blackout.service.ScheduleService;
import jp.gr.uchiwa.blackout.service.ScheduleService.IRefreshTask;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

/**
 * @author jabaraster
 */
public class MainActivity extends ListActivity {

    private final Handler handler = new Handler();

    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final IRefreshTask task = new ScheduleService(this).refreshInBackground(new Runnable() {
            @SuppressWarnings("synthetic-access")
            public void run() {
                MainActivity.this.handler.post(new Runnable() {
                    public void run() {
                        loadDbData();
                    }
                });
            }
        });

        final ArrayAdapter<String> ada = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] { //
                "スケジュール取得中..." + (task.isFirst() ? "(初回起動!)" : "") //  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
                });
        setListAdapter(ada);

        getListView().setBackgroundResource(jp.gr.uchiwa.blackout.R.drawable.app_icon_background);
    }

    private void loadDbData() {
        final Db db = new Db(this);
        final Cursor cursor = db.getDatabase().query(Db.Schedule.TABLE_NAME, Db.Schedule.INSTANCE.getAllColumnNames(), null, null, null, null, null);

        final List<String> rows = new ArrayList<String>();
        for (@SuppressWarnings("unused")
        final Cursor _ : Db.toIterable(cursor)) {
            final String doDate = Db.getString(cursor, Db.Schedule.COL_DO_DATE);
            final String subGroup = Db.getString(cursor, Db.Schedule.COL_SUB_GROUP);
            final int priority = Db.getInteger(cursor, Db.Schedule.COL_PRIORITY);
            rows.add(doDate + ", " + subGroup + ", " + priority); //$NON-NLS-1$//$NON-NLS-2$
        }
        db.getDatabase().close();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                rows.toArray(new String[rows.size()]));
        setListAdapter(adapter);
    }

}
