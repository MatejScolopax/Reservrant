package sk.scolopax.reservrant.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

/**
 * Created by scolopax on 11/08/2017.
 */

public abstract class ReserveTableTask extends AsyncTask<Long, String, Long> {

    private final String TAG = "ReserveTableTask- ";

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
        values.put(DatabaseContract.TableTables.COL_AVAILABLE, 0);
        values.put(DatabaseContract.TableTables.COL_ID_CUSTOMER,idCustomer);
        Uri uri = ContentUris.withAppendedId(DatabaseContract.TableTables.CONTENT_URI, idTable);
        ContentResolver resolver = context.getContentResolver();
        String where = DatabaseContract.TableTables.COL_AVAILABLE+ "=1";
        long noUpdated = resolver.update(uri, values, where, null);

        return noUpdated;
    }

}
