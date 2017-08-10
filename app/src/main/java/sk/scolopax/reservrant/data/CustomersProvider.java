package sk.scolopax.reservrant.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by scolopax on 08/08/2017.
 */

public class CustomersProvider extends ContentProvider {

    private static final String TAG = CustomersProvider.class.getSimpleName();

    private static final int CUSTOMERS = 200;
    private static final int CUSTOMERS_WITH_ID = 201;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static
    {
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY_CUSTOMER, DatabaseContract.TABLE_CUSTOMERS, CUSTOMERS);

        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY_CUSTOMER, DatabaseContract.TABLE_CUSTOMERS + "/#", CUSTOMERS_WITH_ID);
    }

    private ReservrantDBHelper mReservrantDBHelper;

    @Override
    public boolean onCreate() {
        mReservrantDBHelper = new ReservrantDBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseContract.TABLE_CUSTOMERS);

        switch (sUriMatcher.match(uri))
        {
            case CUSTOMERS:
            {
                break;
            }
            case CUSTOMERS_WITH_ID: {
                queryBuilder.appendWhere(DatabaseContract.TableCustomers.COL_ID + "=" + uri.getLastPathSegment());
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        SQLiteDatabase db = mReservrantDBHelper.getWritableDatabase();
        cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mReservrantDBHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case CUSTOMERS:
            {
                long id = db.insert(DatabaseContract.TABLE_CUSTOMERS, null, contentValues);
                if ( id > 0 )
                    returnUri = DatabaseContract.TableCustomers.buildCustomerUri(id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("This provider does not support deletion");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("This provider does not support updates");
    }
}
