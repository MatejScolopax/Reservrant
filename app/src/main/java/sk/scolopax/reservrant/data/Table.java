package sk.scolopax.reservrant.data;

import android.database.Cursor;

import sk.scolopax.reservrant.data.dbs.DatabaseContract;

/**
 * Created by scolopax on 09/08/2017.
 */

public class Table{

    public final int idTable;
    public final int idCustomer;
    public boolean isAvailable;
    public final long reservationTime;

    /* Constructors */

    public Table(int idCustomer, int idTable, long reservationTime, boolean isAvailable) {
        this.idTable = idTable;
        this.idCustomer = idCustomer;
        this.isAvailable = isAvailable;
        this.reservationTime = reservationTime;
    }

    public Table(Cursor cursor) {
        this.idTable = cursor.getInt(DatabaseContract.TableTables.COL_IDX_ID);
        this.idCustomer = cursor.getInt(DatabaseContract.TableTables.COL_IDX_ID_CUSTOMER);
        this.isAvailable =  cursor.getInt(DatabaseContract.TableTables.COL_IDX_AVAILABLE ) == 1 ? true : false;
        this.reservationTime = cursor.getInt(DatabaseContract.TableTables.COL_IDX_RESERVATION_TIME);
    }

}
