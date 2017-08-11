package sk.scolopax.reservrant.jobs;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;



/**
 * Created by scolopax on 09/08/2017.
 */

public class EraseJob {

    private static final String TAG = EraseJob.class.getSimpleName();
    private static final String SHARED_T = EraseJob.class.getSimpleName();
    private static final int JOB_ID = 123;

    public static void startStopEraser(Activity activity)
    {

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        boolean start = sharedPref.getBoolean(SHARED_T, true);

        JobScheduler jobScheduler = (JobScheduler) activity.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        if (start)
        {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(SHARED_T, false);
            editor.commit();

            long interval = 600000; //TEN_MINUTES

            JobInfo job = new JobInfo.Builder(JOB_ID, new ComponentName(activity.getPackageName(), JobSchedulerService.class.getName()))
                    .setPersisted(true)
                    .setPeriodic(interval)
                    .build();

            int result = jobScheduler.schedule(job);

            if (result == JobScheduler.RESULT_SUCCESS)
                Log.d(TAG, "setting scheduled job for: " + interval);
            else
                Log.d(TAG, "error code: " + result);
        }
    }


}
