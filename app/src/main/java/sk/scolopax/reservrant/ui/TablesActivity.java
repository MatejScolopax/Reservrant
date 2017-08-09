package sk.scolopax.reservrant.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import sk.scolopax.reservrant.R;
import sk.scolopax.reservrant.data.Customer;


/**
 * Created by scolopax on 09/08/2017.
 */

public class TablesActivity extends AppCompatActivity {

    private Customer selectedCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            selectedCustomer = bundle.getParcelable(HomeActivity.PARCELABLE_EXTRA);

            if (selectedCustomer!=null) {

                getSupportActionBar().setTitle(selectedCustomer.nameFirst + " " + selectedCustomer.nameLast);
            }
        }
    }

}
