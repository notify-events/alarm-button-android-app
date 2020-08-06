package events.notify.alarmbutton;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
    private static final String SETTINGS_NAME       = "events.notify.alarmbutton.SETTINGS";

    private static final String FIELD_TOKEN         = "token";
    private static final String FIELD_MESSAGE_TITLE = "message_title";
    private static final String FIELD_MESSAGE_TEXT  = "message_text";

    private SharedPreferences _preferences;

    public String token;
    public String message_title;
    public String message_text;

    public Settings(Context context) {
        _preferences = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);

        Load();
    }

    public void Load() {
        token         = _preferences.getString(FIELD_TOKEN, "");
        message_title = _preferences.getString(FIELD_MESSAGE_TITLE, "");
        message_text  = _preferences.getString(FIELD_MESSAGE_TEXT, "");
    }

    public void Save() {
        SharedPreferences.Editor editor = _preferences.edit();

        editor.putString("token", token);
        editor.putString("message_title", message_title);
        editor.putString("message_text", message_text);

        editor.apply();
    }
}
