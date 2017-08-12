package sk.scolopax.reservrant;


import android.content.ContentUris;
import android.content.ContentValues;

import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import sk.scolopax.reservrant.data.dbs.DatabaseContract;
import sk.scolopax.reservrant.ui.HomeActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.anything;

/**
 * Created by scolopax on 12/08/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ReservationTest {

    private String mButtonString;

//    @Before
//    public void setUp() throws Exception{
//        setContext(InstrumentationRegistry.getTargetContext());
//    }

    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void demonstrateIntentPrep() {

        long idTable = 1;

        // make sure, that table with id=1 is available
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TableTables.COL_AVAILABLE, 1);
        Uri uri = ContentUris.withAppendedId(DatabaseContract.TableTables.CONTENT_URI, idTable);
        int updated = InstrumentationRegistry.getTargetContext().getContentResolver().update(uri, values, null, null);
        assertEquals(updated,1);

        // Click customer at position 3  (it will be Christopher Columbus, "id": 12 )
        onView(withId(R.id.recycler_view_customers)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        // check if table at position 0 has value 1 - this means next click will be on idTable=1
        onData(anything()).inAdapterView(withId(R.id.gridview)).atPosition(0).onChildView(withId(R.id.txt_idtable)).check(matches(withText("1")));

        // click table at position 0
        onData(anything()).inAdapterView(withId(R.id.gridview)).atPosition(0).onChildView(withId(R.id.txt_idtable)).perform(click());

        Cursor cursor = InstrumentationRegistry.getTargetContext().getContentResolver().query(
                DatabaseContract.TableTables.CONTENT_URI,
                DatabaseContract.TableTables.getProjection(),
                DatabaseContract.TableTables._ID + " = ? ",
                new String[] {"1"},
                null
        );

        assertNotNull(cursor);
        cursor.moveToFirst();
        String idCusomter = cursor.getString(1);
        String available = cursor.getString(3);

        //check if table is now reserved
        assertEquals(available,"0");

        //check if is reserved for Columbus
        assertEquals(idCusomter,"12");

        // go back
        onView(withContentDescription("Navigate up")).perform(click());


        // click customer at position 5  (it will be Albert Einstein, "id": 16)
        onView(withId(R.id.recycler_view_customers)).perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));

        // click table at position 0
        onData(anything()).inAdapterView(withId(R.id.gridview)).atPosition(0).onChildView(withId(R.id.txt_idtable)).perform(click());

        cursor = InstrumentationRegistry.getTargetContext().getContentResolver().query(
                DatabaseContract.TableTables.CONTENT_URI,
                DatabaseContract.TableTables.getProjection(),  // projection
                DatabaseContract.TableTables._ID + " = ? ",    // selection
                new String[] {"1"},                            // selection args
                null                                           // sort order
        );

        assertNotNull(cursor);
        cursor.moveToFirst();
        idCusomter = cursor.getString(1);

        //table should be still reserved for Columbus
        assertEquals(idCusomter,"12");
    }

}
