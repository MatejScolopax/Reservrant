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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import sk.scolopax.reservrant.R;
import sk.scolopax.reservrant.data.Customer;
import sk.scolopax.reservrant.data.CustomerAdapter;
import sk.scolopax.reservrant.data.DatabaseContract;
import sk.scolopax.reservrant.jobs.EraseJob;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private String mCurFilter;
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

                //Toast.makeText(HomeActivity.this, customer.toString() ,Toast.LENGTH_LONG).show();

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
                //onQueryTextChange(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EraseJob.startStopEraser(this);
        getSupportLoaderManager().initLoader(CUSTOMERS_LOADER_ID, null, this );
    }

    public void onQueryTextChange(String newText) {
        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        getSupportLoaderManager().restartLoader(CUSTOMERS_LOADER_ID, null, this);
    }


    /* LoaderManager.LoaderCallbacks<Cursor> */


//    @NonNull
//    public static android.content.Loader<Cursor> createSearchStationsLoader(@NonNull Context context, @NonNull String nameLike) {
//        return new CursorLoaderBuilder(StationsEntity.class)
//                .selection(new Selection(StationsEntity.Columns.NAME_FULL_NORMALIZED + " LIKE ?")
//                        .groupAnd( 	StationsEntity.Columns.TYPE + "=? OR " + StationsEntity.Columns.TICKETS_SELLING + "=1")
//                )
//                .selectionArgs(new String[]{ "%" + TextUtils.normalizeText(nameLike) + "%", Integer.toString(Station.TYPE_HIGH_TATRAS)
//                })
//                .sortOrder("(CASE WHEN name_full_normalized = '"+TextUtils.normalizeText(nameLike)+"' THEN 1 WHEN name_full_normalized LIKE '"+TextUtils.normalizeText(nameLike)+"%' THEN 2 ELSE 3 END)")
//                .build(context); //DONE by Matej - doplneny SORT - aby ked sa hlada "tur" boli "Turany" pred "Stara Tura"
//    }

//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//
//        Uri baseUri;
//        if (mCurFilter != null)
//            baseUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(mCurFilter));
//        else
//            baseUri = DatabaseContract.CONTENT_URI;
//
//
//        String select = "((" + ContactsContract.Contacts.DISPLAY_NAME     + " NOTNULL) AND ("
//                + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
//                + ContactsContract.Contacts.DISPLAY_NAME     + " != '' ))";
//
//        return new android.content.CursorLoader(this, baseUri, CONTACTS_SUMMARY_PROJECTION, select, null, ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = DatabaseContract.TableCustomers.getProjection();
        return new CursorLoader(HomeActivity.this, DatabaseContract.TableCustomers.CONTENT_URI, projection, null, null, null);
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
