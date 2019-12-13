package com.example.finalproject_draft1;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.RemoteViews;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class RecordingWidget extends AppWidgetProvider {
    private static final String TAG = "Recording Widget";
    public static final String ACTION_RECORD = "com.example.finalproject_draft1.record";
    public static final String ACTION_STOP = "com.example.finalproject_draft1.stop";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recording_widget);

        PendingIntent pendingIntentStart = getPendingIntent(context, RecordingWidget.ACTION_RECORD);
        views.setOnClickPendingIntent(R.id.imageButton, pendingIntentStart);

        PendingIntent pendingIntentStop = getPendingIntent(context, RecordingWidget.ACTION_STOP);
        views.setOnClickPendingIntent(R.id.imageButton3,pendingIntentStop);


        appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    public static PendingIntent getPendingIntent(Context context, String action) {
        Intent intent = new Intent(context, RecordingWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

