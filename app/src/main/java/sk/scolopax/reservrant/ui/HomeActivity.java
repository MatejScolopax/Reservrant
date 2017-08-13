package sk.scolopax.reservrant.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import sk.scolopax.reservrant.R;
import sk.scolopax.reservrant.data.Customer;
import sk.scolopax.reservrant.data.adapter.CustomerAdapter;
import sk.scolopax.reservrant.data.dbs.DatabaseContract;
import sk.scolopax.reservrant.data.net.DownloadCustomersTask;
import sk.scolopax.reservrant.data.net.DownloadTablesTask;
import sk.scolopax.reservrant.jobs.EraseJob;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private CustomerAdapter customerAdapter;
    private RecyclerView customerRecyclerView;
    private EditText edtSearch;
    private SharedPreferences sharedPref;

    private static final int CUSTOMERS_LOADER_ID = 0;
    private static final String SHARED_DOWNLOAD = "SHARED_PREF_KEY_DOWNLOAD";
    public static final String PARCELABLE_EXTRA = "sk.scolopax.Customer.parcelable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        edtSearch = (EditText) findViewById(R.id.edt_search);
        customerRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_customers);

        customerRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager cardLayoutManager = new LinearLayoutManager(this);
        customerRecyclerView.setLayoutManager(cardLayoutManager);

        customerAdapter = new CustomerAdapter(new CustomerAdapter.CustomerAdapterOnClickHandler() {
            @Override
            public void onClick(Long id, Customer customer) {
                Intent answerIntent = new Intent(HomeActivity.this, TablesActivity.class);
                answerIntent.putExtra(PARCELABLE_EXTRA,customer);
                HomeActivity.this.startActivity(answerIntent);
            }
        });

        customerRecyclerView.setAdapter(customerAdapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getSupportLoaderManager().restartLoader(CUSTOMERS_LOADER_ID, null, HomeActivity.this );
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // start job that removes every 10 minutes all reservations
        EraseJob.startEraser(this);
        getSupportLoaderManager().initLoader(CUSTOMERS_LOADER_ID, null, this );


        /* app from the very beginning contains data, but those are replaced from downloaded data */

        this.sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        boolean isDownloaded = sharedPref.getBoolean(SHARED_DOWNLOAD, false);

        // when device is connected to internet and data were not downloaded
        if (!isDownloaded && isConnected(this))
        {
            //download data from web
            Log.v(TAG,"downloading data");
            new DownloadTask(this).execute();
        }
        else
        {
            Log.v(TAG,"not downloading, downloaded"+ isDownloaded + " connected:" +isConnected(this));
        }


    }

    /**
     * Check if device is connected
     * @param context
     * @return
     */
    public static boolean isConnected(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork)
        {
            return (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE);
        }
        return false;
    }


    /* LoaderManager.LoaderCallbacks<Cursor> */

    private Loader<Cursor> getAllCustmoers()
    {
        String[] projection = DatabaseContract.TableCustomers.getProjection();
        String order = DatabaseContract.TableCustomers.COL_NAME_LAST + " ASC ";
        return new CursorLoader(HomeActivity.this, DatabaseContract.TableCustomers.CONTENT_URI, projection, null, null, order);
    }

    private Loader<Cursor> getLikeCustmoers(String like)
    {
        String[] projection = DatabaseContract.TableCustomers.getProjection();
        String selection = DatabaseContract.TableCustomers.COL_NAME_FIRST + " LIKE ? OR " + DatabaseContract.TableCustomers.COL_NAME_LAST + " LIKE ? ";
        String[] selectionArgs = {"%" + like + "%","%" + like + "%"};
        String order = DatabaseContract.TableCustomers.COL_NAME_LAST + " ASC ";
        return new CursorLoader(HomeActivity.this, DatabaseContract.TableCustomers.CONTENT_URI, projection, selection, selectionArgs, order);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (edtSearch.getText().toString().length()==0){
            return getAllCustmoers();
        }
        else {
            return getLikeCustmoers(edtSearch.getText().toString());
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        customerAdapter.refreshCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        customerAdapter.refreshCursor(null);
    }

    /* Data download tasks*/

    private class DownloadTask extends DownloadCustomersTask
    {

        Context context;

        public DownloadTask(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result)
            {
                new DownloadTables(context).execute();
            }
        }
    }

    private class DownloadTables extends DownloadTablesTask
    {

        public DownloadTables(Context context) {
            super(context);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result)
            {
                // when successful, save to shared preferences
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(SHARED_DOWNLOAD, true);
                editor.commit();
            }
        }
    }

}
