package barqsoft.footballscores.widget;/*
 * Created by soehler on 25/10/15 20:32.
 */

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;

public class TodayScoresIntentService extends IntentService {

    public TodayScoresIntentService() {
       super ("TodayScoresIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        int matchesToday;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendar = new GregorianCalendar();
        String dateToday[]= {simpleDateFormat.format(calendar.getTime())};

        RemoteViews remoteViews;
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ScoresTodayWidget.class));

        //get data using existing provider
        Cursor cursor = getApplicationContext().getContentResolver().query(
                DatabaseContract.scores_table.buildScoreWithDate(), null, null , dateToday, null);

        //count today matches
        matchesToday = cursor.getCount();

        //release cursor
        cursor.close();

        for (int appWidgetId : appWidgetIds) {

            //Get remote views reference and set its contents
            remoteViews = new RemoteViews(getPackageName(),  R.layout.scores_today_widget);
            remoteViews.setTextViewText(R.id.appwidget_scores, String.valueOf(matchesToday));

            //make pending intent to open MainActivity when Widget is clicked
            makePendingIntent(remoteViews);

            //Very important to update widget
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        }

    }

    private void makePendingIntent(RemoteViews remoteViews){
        Intent intent = new Intent (this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        remoteViews.setOnClickPendingIntent(R.id.widget_today_scores, pendingIntent);
    }


}
