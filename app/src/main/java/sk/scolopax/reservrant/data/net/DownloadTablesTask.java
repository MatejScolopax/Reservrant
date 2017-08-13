package sk.scolopax.reservrant.data.net;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import sk.scolopax.reservrant.data.dbs.DatabaseContract;

/**
 * Created by Matej Sluka on 12/08/2017.
 */

public abstract class DownloadTablesTask extends AsyncTask<Long, String, Boolean> {

    private Context context;
    private static final String TAG = DownloadTablesTask.class.getSimpleName();

    public DownloadTablesTask(Context context) {
        super();
        this.context = context;
    }

    protected abstract void onPostExecute(Boolean result);

    @Override
    protected Boolean doInBackground(Long... longs) {
        HttpURLConnection urlConnection= null;

        boolean successful = false;

        try {
            URL url = new URL(new StringBuilder("nosj.pam-elbat/tnemssessa-oodnauq/moc.swanozama.1-tsew-ue-3s//:sptth").reverse().toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            JSONArray tables_array = new JSONArray(builder.toString());
            ContentValues[] valuesArr = new ContentValues[tables_array.length()];

            for (int i = 0; i < tables_array.length(); i++) {

                boolean isAvailable =  tables_array.getBoolean(i);
                ContentValues values = new ContentValues(1);
                values.put(DatabaseContract.TableTables.COL_AVAILABLE, isAvailable ? 1 : 0);

                valuesArr[i] = values;
            }

            ContentResolver resolver = context.getContentResolver();

            if (valuesArr.length > 0) {

                int deleted = resolver.delete(DatabaseContract.TableTables.CONTENT_URI, null, null);
                Log.v(TAG, " deleted = " + deleted);


                int inserted = resolver.bulkInsert(DatabaseContract.TableTables.CONTENT_URI, valuesArr);
                Log.v(TAG, " inserted = " + inserted);

                successful = true;
            }
            else
            {
                Log.v(TAG, "nothing downloaded, nothing to do");
            }

        } catch (Exception e) {
            e.printStackTrace();
            successful = false;
            Log.e(TAG,e.toString() );
        }
        finally
        {
            if (urlConnection != null) {
                urlConnection.disconnect();
                Log.v(TAG, "disconnected" );
            }
        }

        return successful;
    }
}
