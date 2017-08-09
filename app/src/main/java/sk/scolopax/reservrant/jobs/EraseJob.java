package sk.scolopax.reservrant.jobs;

import android.app.AlarmManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;



/**
 * Created by scolopax on 09/08/2017.
 */

public class EraseJob {

    private static final String TAG = EraseJob.class.getSimpleName();
    private static final int JOB_ID = 123;

    public static void startStopEraser(Context ctx, boolean start)
    {
        JobScheduler jobScheduler = (JobScheduler) ctx.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        if (!start)
        {
            jobScheduler.cancel(JOB_ID);
            Log.d(TAG, "cancelling scheduled job");
        }
        else
        {
            long interval = 600000; //TEN_MINUTES

            JobInfo job = new JobInfo.Builder(JOB_ID, new ComponentName(ctx.getPackageName(), JobSchedulerService.class.getName()))
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
