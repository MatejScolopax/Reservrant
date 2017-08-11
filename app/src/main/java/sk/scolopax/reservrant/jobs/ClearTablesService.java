package sk.scolopax.reservrant.jobs;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;

import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import sk.scolopax.reservrant.R;
import sk.scolopax.reservrant.data.DatabaseContract;
import sk.scolopax.reservrant.ui.HomeActivity;


/**
 * Created by scolopax on 11/08/2017.
 */

public class ClearTablesService extends IntentService {

    public static final int NOTIFICATION_ID = 12;
    private static final String TAG = ClearTablesService.class.getSimpleName();
    private NotificationManager mNotificationManager;

    public ClearTablesService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Log.v(TAG, "NotificationManager created");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.v(TAG, "Executing jobs");
        createNotification();
    }

    private long clearTables()
    {
        ContentValues values = new ContentValues();
        values.clear();
        values.put(DatabaseContract.TableTables.COL_AVAILABLE, 1);

        Uri uri =DatabaseContract.TableTables.CONTENT_URI;
        ContentResolver resolver = getContentResolver();
        return resolver.update(uri, values, null, null);
    }

    private void createNotification()
    {

        long cleared =  clearTables();


        // if no resrvation was removed, do not notify
        if (cleared> 0) {

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.sym_action_email)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(getResources().getString(R.string.noti_name));

            Intent resultIntent = new Intent(this, HomeActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(HomeActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            Notification notification = mBuilder.build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;

            mNotificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

}
