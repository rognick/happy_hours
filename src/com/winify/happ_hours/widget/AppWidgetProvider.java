package com.winify.happ_hours.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import com.winify.happy_hours.ApplicationPreferences;
import com.winify.happy_hours.R;
import com.winify.happy_hours.activities.MainActivity;
import com.winify.happy_hours.controller.ServiceGateway;
import com.winify.happy_hours.listeners.ServiceListener;
import com.winify.happy_hours.models.Time;
import com.winify.happy_hours.models.Token;
import com.winify.happy_hours.service.TrackerService;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {
    private ApplicationPreferences preferences;
    private TrackerService service;
    private static final String LOG_TAG = "ExampleWidget";
    private static int counter;
    /**
     * Custom Intent name that is used by the AlarmManager to tell us to update the clock once per second.
     */
    public static String CLOCK_WIDGET_UPDATE = "com.winify.happ_hours.widget.8BITCLOCK_WIDGET_UPDATE";
    private static long currentTime;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (CLOCK_WIDGET_UPDATE.equals(intent.getAction())) {

            Log.d(LOG_TAG, "count " + counter);
            Log.d(LOG_TAG, "Clock update");
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int appWidgetID : ids) {
                updateAppWidget(context, appWidgetManager, appWidgetID);
            }
        }
    }

    private PendingIntent createClockTickIntent(Context context) {
        Intent intent = new Intent(CLOCK_WIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "Widget Provider disabled. Turning off timer");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createClockTickIntent(context));
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "Widget Provider enabled.  Starting timer to update widget every second");

        preferences = new ApplicationPreferences(context.getApplicationContext());
        ServiceGateway serviceGateway = new ServiceGateway(context);
        service = serviceGateway.getService();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, getWorkedTime(), 60000, createClockTickIntent(context));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        Log.d(LOG_TAG, "Updating Example Widgets.");

        // provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget1);
            views.setOnClickPendingIntent(R.id.widget1label, pendingIntent);
            // Tell the AppWidgetManager to perform an update on the current app
            // widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            // Update The clock label using a shared method
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        preferences = new ApplicationPreferences(context.getApplicationContext());

        ServiceGateway serviceGateway = new ServiceGateway(context);
        service = serviceGateway.getService();

        if (preferences.isTimerSet()) {
            if (counter == 30) {
                counter = 0;
                getWorkedTime();
            } else if (counter == 0) {
                counter++;
                getWorkedTime();
            } else {
                currentTime += 60000;
                counter++;
            }
            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget1);
            updateViews.setTextViewText(R.id.widget1label, convertTime(currentTime));
            appWidgetManager.updateAppWidget(appWidgetId, updateViews);
        }
    }

    private long getWorkedTime() {
        Token token = new Token(preferences.getKeyToken());
        service.getWorkedTime(token, new ServiceListener<Time>() {
            @Override
            public void success(Time time, Response response) {
                currentTime = Integer.parseInt(time.getDaily());
                Log.d(LOG_TAG, "TIME.");
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(LOG_TAG, "No TIME.");
                currentTime = 0;
            }
        });
        return currentTime;
    }

    private String convertTime(long time) {
        long minutes = ((time / (1000 * 60)) % 60);
        long hours = ((time / (1000 * 60 * 60)));
        return hours + "h : " + minutes + "m";
    }
}
