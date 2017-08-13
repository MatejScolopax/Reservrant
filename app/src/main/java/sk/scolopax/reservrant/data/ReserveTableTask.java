package sk.scolopax.reservrant.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import sk.scolopax.reservrant.data.dbs.DatabaseContract;

/**
 * Created by Matej Sluka on 11/08/2017.
 */

public abstract class ReserveTableTask extends AsyncTask<Long, String, Long> {

    private Context context;

    public ReserveTableTask(Context context) {
        super();
        this.context = context;
    }

    protected abstract void onPostExecute(Long result);

    @Override
    protected Long doInBackground(Long... params) {

        long idTable = params[0];
        long idCustomer = params[1];

        ContentValues values = new ContentValues();
        values.clear();
        values.put(DatabaseContract.TableTables.COL_AVAILABLE, 0L);
        values.put(DatabaseContract.TableTables.COL_ID_CUSTOMER,idCustomer);
        Uri uri = ContentUris.withAppendedId(DatabaseContract.TableTables.CONTENT_URI, idTable);
        ContentResolver resolver = context.getContentResolver();
        String where = DatabaseContract.TableTables.COL_AVAILABLE + "=1";
        long updated = resolver.update(uri, values, where, null);

        return updated;
    }
}
