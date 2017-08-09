package sk.scolopax.reservrant;

import org.junit.Test;

import sk.scolopax.reservrant.data.Customer;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void read() throws Exception {



        Customer customer = new Customer(0,"Marilyn", "Monroe");




        assertEquals(4, 2 + 2);
    }


}