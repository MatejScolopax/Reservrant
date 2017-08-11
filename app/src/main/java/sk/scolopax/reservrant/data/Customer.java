package sk.scolopax.reservrant.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by scolopax on 08/08/2017.
 */

public class Customer implements Parcelable {

    public static final String JSON_KEY_ID = "id";
    public static final String JSON_KEY_NAME_FIRST = "customerFirstName";
    public static final String JSON_KEY_NAME_LAST = "customerLastName";

    public final long idCustomer;
    public final String nameFirst;
    public final String nameLast;

    /* Constructors */

    public Customer(long idCustomer, String nameFirst, String nameLast) {
        this.idCustomer = idCustomer;
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;
    }

    public Customer(Cursor cursor) {
        this.idCustomer = cursor.getLong(DatabaseContract.TableCustomers.COL_IDX_ID);
        this.nameFirst = cursor.getString(DatabaseContract.TableCustomers.COL_IDX_NAME_FIRST);
        this.nameLast = cursor.getString(DatabaseContract.TableCustomers.COL_IDX_NAME_LAST);
    }

    protected Customer(Parcel in) {
        this.idCustomer = in.readLong();
        this.nameFirst = in.readString();
        this.nameLast = in.readString();
    }

    /* Parcelable */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idCustomer);
        dest.writeString(nameFirst);
        dest.writeString(nameLast);
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    @Override
    public String toString() {
        return "ID: " + idCustomer + " First Name: " + nameFirst + " Last Name: " + nameLast;
    }
}
