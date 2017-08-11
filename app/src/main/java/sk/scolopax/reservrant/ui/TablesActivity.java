package sk.scolopax.reservrant.ui;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import sk.scolopax.reservrant.R;
import sk.scolopax.reservrant.data.Customer;
import sk.scolopax.reservrant.data.DatabaseContract;
import sk.scolopax.reservrant.data.TablesAdapter;


/**
 * Created by scolopax on 09/08/2017.
 */

public class TablesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Customer selectedCustomer;
    private TablesAdapter mTableAdapter;
    private static final int TABLES_LOADER_ID = 10;

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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                ContentValues values = new ContentValues();
                values.clear();
                values.put(DatabaseContract.TableTables.COL_AVAILABLE, 0);
                values.put(DatabaseContract.TableTables.COL_ID_CUSTOMER,selectedCustomer.idCustomer);
                Uri uri = ContentUris.withAppendedId(DatabaseContract.TableTables.CONTENT_URI, id);
                ContentResolver resolver = getContentResolver();
                long noUpdated = resolver.update(uri, values, null, null);
                //Toast.makeText(TablesActivity.this,"updated:" + noUpdated,Toast.LENGTH_SHORT ).show();
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
}
