package sk.scolopax.reservrant.data.net;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import sk.scolopax.reservrant.data.Customer;
import sk.scolopax.reservrant.data.dbs.DatabaseContract;

/**
 * Created by Matej Sluka on 12/08/2017.
 */

public abstract class DownloadCustomersTask extends AsyncTask<Long, String, Boolean> {

    private Context context;
    private static final String TAG = DownloadCustomersTask.class.getSimpleName();

    public DownloadCustomersTask(Context context) {
        super();
        this.context = context;
    }

    protected abstract void onPostExecute(Boolean result);

    @Override
    protected Boolean doInBackground(Long... longs) {

        HttpURLConnection urlConnection= null;

        boolean successful = false;

        try {
            URL url = new URL(new StringBuilder("nosj.tsil-remotsuc/tnemssessa-oodnauq/moc.swanozama.1-tsew-ue-3s//:sptth").reverse().toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);


            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            JSONArray customers_array = new JSONArray(builder.toString());
            ContentValues[] valuesArr = new ContentValues[customers_array.length()];

            for (int i = 0; i < customers_array.length(); i++) {
                JSONObject item = customers_array.getJSONObject(i);
                ContentValues values = new ContentValues(3);

                values.put(DatabaseContract.TableCustomers.COL_ID, item.getString(Customer.JSON_KEY_ID));
                values.put(DatabaseContract.TableCustomers.COL_NAME_FIRST, item.getString(Customer.JSON_KEY_NAME_FIRST));
                values.put(DatabaseContract.TableCustomers.COL_NAME_LAST, item.getString(Customer.JSON_KEY_NAME_LAST));

                valuesArr[i] = values;
            }

            ContentResolver resolver = context.getContentResolver();

            if (valuesArr.length > 0) {

                int deleted = resolver.delete(DatabaseContract.TableCustomers.CONTENT_URI, null, null);
                Log.v(TAG, " deleted = " + deleted);


                int inserted = resolver.bulkInsert(DatabaseContract.TableCustomers.CONTENT_URI, valuesArr);
                Log.v(TAG, " inserted = " + inserted);

                successful = true;
            }
            else
            {
                Log.v(TAG, "nothing downloaded, nothing to do");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,e.toString() );
            successful = false;
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
