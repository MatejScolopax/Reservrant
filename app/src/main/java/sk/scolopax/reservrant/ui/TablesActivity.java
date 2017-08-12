package sk.scolopax.reservrant.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;


import sk.scolopax.reservrant.R;
import sk.scolopax.reservrant.data.Customer;
import sk.scolopax.reservrant.data.dbs.DatabaseContract;
import sk.scolopax.reservrant.data.ReserveTableTask;
import sk.scolopax.reservrant.data.TablesAdapter;


/**
 * Created by scolopax on 09/08/2017.
 */

public class TablesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Customer selectedCustomer;
    private TablesAdapter mTableAdapter;
    private static final int TABLES_LOADER_ID = 10;
    private static final String TAG = TablesActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            selectedCustomer = bundle.getParcelable(HomeActivity.PARCELABLE_EXTRA);

            if (selectedCustomer!=null) {

                getSupportActionBar().setTitle(selectedCustomer.nameFirst + " " + selectedCustomer.nameLast);
            }
        }
        GridView gridview = (GridView) findViewById(R.id.gridview);

        mTableAdapter = new TablesAdapter(TablesActivity.this);
        gridview.setAdapter(mTableAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long idTable) {

                LinearLayout ll = (LinearLayout) view.findViewById(R.id.fl_table);
                ll.setBackground(TablesActivity.this.getDrawable(R.drawable.table_unavailable));

                Long params[] = {idTable, selectedCustomer.idCustomer };
                new MakeReservation(TablesActivity.this).execute(params);
            }
        });
        getSupportLoaderManager().initLoader(TABLES_LOADER_ID, null, this );
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = DatabaseContract.TableTables.getProjection();
        return new CursorLoader(TablesActivity.this, DatabaseContract.TableTables.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mTableAdapter.refreshCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTableAdapter.refreshCursor(null);
    }

    /* Reservation task on worker thread */
    private class MakeReservation extends ReserveTableTask
    {

        public MakeReservation(Context context) {
            super(context);
        }

        @Override
        protected void onPostExecute(Long result) {
            Log.v(TAG, "inserted: " + result);
        }
    }

}
