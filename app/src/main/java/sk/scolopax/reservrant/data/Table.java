package sk.scolopax.reservrant.data;

/**
 * Created by scolopax on 09/08/2017.
 */

public class Table{

    public final int idTable;
    public final int idCustomer;
    public boolean isFree;
    public final long reservationTime;

    /* Constructors */

    public Table(int idCustomer, int idTable, long reservationTime, boolean isFree) {
        this.idTable = idTable;
        this.idCustomer = idCustomer;
        this.isFree = isFree;
        this.reservationTime = reservationTime;
    }

}
