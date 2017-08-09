package sk.scolopax.reservrant.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by scolopax on 08/08/2017.
 */

public class DatabaseContract {

    // Database schema information
    public static final String TABLE_CUSTOMERS = "customers";
    public static final String TABLE_TABLES = "tables";
    public static final String CONTENT_AUTHORITY = "sk.scolopax.reservrant";


    /*  Customers  */
    public static final class TableCustomers implements BaseColumns
    {
        public static final String COL_ID = "_id";
        public static final String COL_NAME_FIRST = "firstname";
        public static final String COL_NAME_LAST = "lastname";

        static final int COL_IDX_ID = 0;
        static final int COL_IDX_NAME_FIRST = 1;
        static final int COL_IDX_NAME_LAST = 2;

        public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
                .authority(CONTENT_AUTHORITY)
                .appendPath(TABLE_CUSTOMERS)
                .build();


        public static Uri buildCustomerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String[] getProjection()
        {
            return new String[] { TableCustomers.COL_ID,TableCustomers.COL_NAME_FIRST,TableCustomers.COL_NAME_LAST };
        }

        public static final String DEFAULT_SORT_CUSTOMERS = TableCustomers.COL_ID;

    }

    /* Tables */
    public static final class TableTables implements BaseColumns
    {
        public static final String COL_ID = "_id";
        public static final String COL_ID_CUSTOMER = "idcustomer";
        public static final String COL_AVAILABLE = "available";
        public static final String COL_RESERVATION_TIME = "reservationtime";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
                .authority(CONTENT_AUTHORITY)
                .appendPath(TABLE_TABLES)
                .build();

        public static Uri buildTablesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }










}
