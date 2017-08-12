package sk.scolopax.reservrant.ui;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import sk.scolopax.reservrant.R;
import sk.scolopax.reservrant.data.Customer;
import sk.scolopax.reservrant.data.CustomerAdapter;
import sk.scolopax.reservrant.data.dbs.DatabaseContract;
import sk.scolopax.reservrant.data.net.DownloadCustomersTask;
import sk.scolopax.reservrant.data.net.DownloadTablesTask;
import sk.scolopax.reservrant.jobs.EraseJob;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private CustomerAdapter mCustomerAdapter;
    private RecyclerView mCustomerRecyclerView;
    private EditText edtSearch;
    private static final int CUSTOMERS_LOADER_ID = 0;

    public static final String PARCELABLE_EXTRA = "sk.scolopax.Customer.parcelable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        edtSearch = (EditText) findViewById(R.id.edt_search);
        mCustomerRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_customers);

        mCustomerRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager cardLayoutManager = new LinearLayoutManager(this);
        mCustomerRecyclerView.setLayoutManager(cardLayoutManager);

        mCustomerAdapter = new CustomerAdapter(this, new CustomerAdapter.CustomerAdapterOnClickHandler() {
            @Override
            public void onClick(Long id, Customer customer) {
                Intent answerIntent = new Intent(HomeActivity.this, TablesActivity.class);
                answerIntent.putExtra(PARCELABLE_EXTRA,customer);
                HomeActivity.this.startActivity(answerIntent);
            }
        });

        mCustomerRecyclerView.setAdapter(mCustomerAdapter);

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

        EraseJob.startStopEraser(this);
        getSupportLoaderManager().initLoader(CUSTOMERS_LOADER_ID, null, this );

        //TODO call these at the first start:
       // new DownloadCustomersTask(HomeActivity.this).execute();
       // new DownloadTablesTask(HomeActivity.this).execute();
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
        mCustomerAdapter.refreshCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCustomerAdapter.refreshCursor(null);
    }
}
