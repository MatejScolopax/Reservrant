package sk.scolopax.reservrant.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import sk.scolopax.reservrant.R;

/**
 * Created by scolopax on 08/08/2017.
 */

public class ReservrantDBHelper extends SQLiteOpenHelper {

    private static final String TAG = ReservrantDBHelper.class.getSimpleName();

    public static final String DB_NAME = "customers.db";
    public static final int DB_VERSION = 1;
    private Resources mResources;


    private static final String SQL_CREATE_TABLE_CUSTOMERS= "CREATE TABLE " +
            DatabaseContract.TABLE_CUSTOMERS + " (" +
            DatabaseContract.TableCustomers._ID + " INTEGER PRIMARY KEY ," +
            DatabaseContract.TableCustomers.COL_NAME_FIRST + " TEXT NOT NULL," +
            DatabaseContract.TableCustomers.COL_NAME_LAST  + " TEXT NOT NULL )";

    private static final String SQL_CREATE_TABLE_TABLES = "CREATE TABLE " +
            DatabaseContract.TABLE_TABLES + " (" +
            DatabaseContract.TableTables._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DatabaseContract.TableTables.COL_ID_CUSTOMER     + " INTEGER NULL," +
            DatabaseContract.TableTables.COL_RESERVATION_TIME+ " INTEGER NULL," +
            DatabaseContract.TableTables.COL_AVAILABLE       + " INTEGER NOT NULL," +
            "FOREIGN KEY ("+DatabaseContract.TableTables.COL_ID_CUSTOMER+") REFERENCES "+ DatabaseContract.TABLE_CUSTOMERS +"("+DatabaseContract.TableCustomers._ID+") )";

    /* Constructor */

    public ReservrantDBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
        mResources = context.getResources();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_CUSTOMERS);
        db.execSQL(SQL_CREATE_TABLE_TABLES);
        try{
            readCustomersFromResources(db);
            readTablesFromResources(db);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "JSON import ERROR: " + e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_CUSTOMERS);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_TABLES);
        onCreate(db);
    }


    /**
     * reads JSON file from resources and stores data in database
     * @param db
     * @throws IOException
     * @throws JSONException
     */
    private void readCustomersFromResources(SQLiteDatabase db) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.customer_list);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        JSONArray customers_array = new JSONArray(builder.toString());

        for (int i = 0; i < customers_array.length(); i++) {
            JSONObject item = customers_array.getJSONObject(i);
            ContentValues values = new ContentValues(3);

            values.put(DatabaseContract.TableCustomers.COL_ID, item.getString(Customer.JSON_KEY_ID));
            values.put(DatabaseContract.TableCustomers.COL_NAME_FIRST, item.getString(Customer.JSON_KEY_NAME_FIRST));
            values.put(DatabaseContract.TableCustomers.COL_NAME_LAST, item.getString(Customer.JSON_KEY_NAME_LAST));

            db.insert(DatabaseContract.TABLE_CUSTOMERS, null, values);
        }
    }


    private void readTablesFromResources(SQLiteDatabase db) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.table_map);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        JSONArray tables_array = new JSONArray(builder.toString());

        for (int i = 0; i < tables_array.length(); i++) {

            boolean isAvailable =  tables_array.getBoolean(i);

            ContentValues values = new ContentValues(1);

            values.put(DatabaseContract.TableTables.COL_AVAILABLE, isAvailable ? 1 : 0);

            db.insert(DatabaseContract.TABLE_TABLES, null, values);
        }
    }



}
