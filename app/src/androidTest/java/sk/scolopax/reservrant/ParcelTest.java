package sk.scolopax.reservrant;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import sk.scolopax.reservrant.data.Customer;
import static org.junit.Assert.assertEquals;

/**
 * Created by Matej Suluka on 12/08/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ParcelTest {

    @Test
    public void parcelTest() {

        // create customer
        Customer customer = new Customer(0L,"Marilyn", "Monroe");

        // write  to parcel object
        Parcel parcel = Parcel.obtain();
        customer.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);

        // read from parcel
        Customer createdFromParcel = Customer.CREATOR.createFromParcel(parcel);

        //check
        assertEquals(createdFromParcel.idCustomer, 0L);
        assertEquals(createdFromParcel.nameFirst, "Marilyn");
        assertEquals(createdFromParcel.nameLast, "Monroe");
    }
}
