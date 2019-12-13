package com.example.finalproject_draft1;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class RecordingWidget extends AppWidgetProvider {
    private static final String TAG = "Recording Widget";
    public static final String ACTION_RECORD = "com.example.finalproject_draft1.record";
    public static final String ACTION_STOP = "com.example.finalproject_draft1.stop";

    public static RemoteViews getRemoteViews(Context context){

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recording_widget);

        PendingIntent pendingIntentStart = getPendingIntent(context, RecordingWidget.ACTION_RECORD);
        views.setOnClickPendingIntent(R.id.imageButton, pendingIntentStart);


//        views.setTextViewText(R.id.textView, "Recording...");

//        views.setChronometer(R.id.chronometerWidget, SystemClock.elapsedRealtime(), null, true);

        PendingIntent pendingIntentStop = getPendingIntent(context, RecordingWidget.ACTION_STOP);
        views.setOnClickPendingIntent(R.id.imageButton3,pendingIntentStop);


        return views;

    }

    private void associateIntents(Context context) {

        try {
            RemoteViews remoteViews = getRemoteViews(context);

            // Push update for this widget to the home screen
            ComponentName thisWidget = new ComponentName(context, RecordingWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            manager.updateAppWidget(thisWidget, remoteViews);
        }
        catch (Exception e)
        {}
    }

    public static PendingIntent getPendingIntent(Context context, String action) {
        Intent intent = new Intent(context, RecordingWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//
//        }
        associateIntents(context);
        Log.d(TAG, "Widget's onUpdate()");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        super.onDeleted(context, appWidgetIds);
        Intent oService = new Intent(context, RecordingAudio.class);
        context.stopService(oService);
        Log.d(TAG, "Deleting widget");
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public void onReceive(Context context, Intent intent)
    {
        final String action = intent.getAction();
        Log.d(TAG, "Widget received action: " + action);

        if ((action.equals(ACTION_RECORD)
                || action.equals(ACTION_STOP)))
        {
            Intent serviceIntent = new Intent(context, RecordingAudio.class);
            serviceIntent.setAction(action);
            context.startService(serviceIntent);
        }
        else
        {
            super.onReceive(context, intent);
        }
    }
}

