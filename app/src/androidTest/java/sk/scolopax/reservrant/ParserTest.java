package sk.scolopax.reservrant;

import android.content.ContentValues;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import sk.scolopax.reservrant.data.Customer;

import static org.junit.Assert.assertEquals;

/**
 * Created by Matej Sluka on 12/08/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ParserTest {

    @Test
    public void customerParserTest() throws JSONException {
        String inputARR = "[\n" +
                "  {\n" +
                "    \"customerFirstName\": \"Marilyn\",\n" +
                "    \"customerLastName\": \"Monroe\",\n" +
                "    \"id\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"customerFirstName\": \"Abraham\",\n" +
                "    \"customerLastName\": \"Lincoln\",\n" +
                "    \"id\": 1\n" +
                "  }]";

        Customer[] customers;
        JSONArray customers_array = new JSONArray(inputARR);
        customers = new Customer[customers_array.length()];

        for (int i = 0; i < customers_array.length(); i++) {
            JSONObject item = customers_array.getJSONObject(i);
            Customer customer = new Customer(item.getLong(Customer.JSON_KEY_ID),item.getString(Customer.JSON_KEY_NAME_FIRST),item.getString(Customer.JSON_KEY_NAME_LAST));
            customers[i] = customer;
        }

        assertEquals(customers[0].idCustomer,0L);
        assertEquals(customers[0].nameFirst,"Marilyn");
        assertEquals(customers[0].nameLast,"Monroe");

        assertEquals(customers[1].idCustomer, 1L);
        assertEquals(customers[1].nameFirst,"Abraham");
        assertEquals(customers[1].nameLast,"Lincoln");
    }

}
