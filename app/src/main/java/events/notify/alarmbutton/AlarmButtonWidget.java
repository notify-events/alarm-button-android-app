package events.notify.alarmbutton;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AlarmButtonWidget extends AppWidgetProvider {

    private static final String ALARM_BUTTON_ACTION = "alarm_button_action";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.alarm_button_widget);

        Intent intent = new Intent(context, AlarmButtonWidget.class);
        intent.setAction(ALARM_BUTTON_ACTION);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.alarm_button, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (!ALARM_BUTTON_ACTION.equals(intent.getAction())) {
            return;
        }

        Settings settings = new Settings(context);

        new AsyncRequest(context).execute(settings);
    }

    public static void post(String url, String json) throws Exception {
        String charset = "UTF-8";

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty("Content-Type", "application/json; charset=" + charset);

        try (OutputStream output = connection.getOutputStream()) {
            output.write(json.getBytes(charset));
            output.flush();
        }

        InputStream response = connection.getInputStream();
    }

    class AsyncRequest extends AsyncTask<Settings, Void, Boolean> {

        private Context _context;

        public AsyncRequest (Context context) {
            _context = context;
        }

        @Override
        protected Boolean doInBackground(Settings... params) {
            boolean result = false;

            try {
                JSONObject data = new JSONObject();
                data.put("title", params[0].message_title);
                data.put("text",  params[0].message_text);

                String url = String.format("https://notify.events/api/v1/channel/source/%s/execute", params[0].token);

                post(url, data.toString());

                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                Toast.makeText(_context, R.string.alarm_button_submit, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(_context, "Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }
}

