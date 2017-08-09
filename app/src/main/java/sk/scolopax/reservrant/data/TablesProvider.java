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
import android.text.TextUtils;

/**
 * Created by scolopax on 09/08/2017.
 */

public class TablesProvider extends ContentProvider {


    private static final String TAG = TablesProvider.class.getSimpleName();

    private static final int TABLES = 300;
    private static final int TABLES_WITH_ID = 301;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static
    {
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.TABLE_TABLES, TABLES);

        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.TABLE_TABLES + "/#", TABLES_WITH_ID);
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
        queryBuilder.setTables(DatabaseContract.TABLE_TABLES);

        switch (sUriMatcher.match(uri))
        {
            case TABLES:
            {
                break;
            }
            case TABLES_WITH_ID: {
                queryBuilder.appendWhere(DatabaseContract.TableTables.COL_ID + "=" + uri.getLastPathSegment());
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
        Uri returnUri;
        SQLiteDatabase db = mReservrantDBHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case TABLES:
            {
                long id = db.insert(DatabaseContract.TABLE_CUSTOMERS, null, contentValues);
                if ( id > 0 )
                    returnUri = DatabaseContract.TableTables.buildTablesUri(id);
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
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase db = mReservrantDBHelper.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case TABLES:
                rowsDeleted = db.delete(DatabaseContract.TABLE_TABLES, selection, selectionArgs);
                break;
            case TABLES_WITH_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                {
                    rowsDeleted = db.delete(DatabaseContract.TABLE_TABLES, DatabaseContract.TableTables.COL_ID + "=" + id, null);
                }
                else
                {
                    rowsDeleted = db.delete( DatabaseContract.TABLE_TABLES, DatabaseContract.TableTables.COL_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase db = mReservrantDBHelper.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType) {
            case TABLES:
                rowsUpdated = db.update(DatabaseContract.TABLE_TABLES,contentValues, selection, selectionArgs);
                break;
            case TABLES_WITH_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                {
                    rowsUpdated = db.update( DatabaseContract.TABLE_TABLES, contentValues, DatabaseContract.TableTables.COL_ID + "=" + id, null);
                }
                else
                {
                    rowsUpdated = db.update( DatabaseContract.TABLE_TABLES,contentValues, DatabaseContract.TableTables.COL_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }
}
