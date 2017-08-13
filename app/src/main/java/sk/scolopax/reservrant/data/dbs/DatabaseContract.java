package sk.scolopax.reservrant.data.dbs;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Matej Sluka on 08/08/2017.
 */

public class DatabaseContract {

    /* Database schema information */
    public static final String TABLE_NAME_CUSTOMERS = "customers";
    public static final String TABLE_NAME_TABLES = "tables";
    public static final String CONTENT_AUTHORITY_CUSTOMER = "sk.scolopax.reservrant.customer";
    public static final String CONTENT_AUTHORITY_TABLE = "sk.scolopax.reservrant.table";

    /*  Customers  */
    public static final class TableCustomers implements BaseColumns
    {
        public static final String COL_ID = "_id";
        public static final String COL_NAME_FIRST = "firstname";
        public static final String COL_NAME_LAST = "lastname";

        public static final int COL_IDX_ID = 0;
        public static final int COL_IDX_NAME_FIRST = 1;
        public static final int COL_IDX_NAME_LAST = 2;

        public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
                .authority(CONTENT_AUTHORITY_CUSTOMER)
                .appendPath(TABLE_NAME_CUSTOMERS)
                .build();

        public static Uri buildCustomerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String[] getProjection()
        {
            return new String[] { TableCustomers.COL_ID,TableCustomers.COL_NAME_FIRST,TableCustomers.COL_NAME_LAST };
        }
    }

    /* Tables */
    public static final class TableTables implements BaseColumns
    {
        public static final String COL_ID = "_id";
        public static final String COL_ID_CUSTOMER = "idcustomer";
        public static final String COL_AVAILABLE = "available";
        public static final String COL_RESERVATION_TIME = "reservationtime";

        public static final int COL_IDX_ID = 0;
        public static final int COL_IDX_ID_CUSTOMER = 1;
        public static final int COL_IDX_RESERVATION_TIME = 2;
        public static final int COL_IDX_AVAILABLE = 3;

        public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
                .authority(CONTENT_AUTHORITY_TABLE)
                .appendPath(TABLE_NAME_TABLES)
                .build();

        public static String[] getProjection()
        {
            return new String[] { TableTables.COL_ID,TableTables.COL_ID_CUSTOMER, TableTables.COL_RESERVATION_TIME,TableTables.COL_AVAILABLE };
        }

        public static Uri buildTablesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
