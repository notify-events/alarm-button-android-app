package events.notify.alarmbutton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Settings settings;

    private EditText field_token;
    private TextView field_token_error;
    private EditText field_message_title;
    private TextView field_message_title_error;
    private EditText field_message_text;
    private TextView field_message_text_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = new Settings(this);

        field_token               = findViewById(R.id.field_token);
        field_token_error         = findViewById(R.id.field_token_error);
        field_message_title       = findViewById(R.id.field_message_title);
        field_message_title_error = findViewById(R.id.field_message_title_error);
        field_message_text        = findViewById(R.id.field_message_text);
        field_message_text_error  = findViewById(R.id.field_message_text_error);

        field_token.setText(settings.token);
        field_token_error.setText("");
        field_token_error.setVisibility(View.GONE);
        field_message_title.setText(settings.message_title);
        field_message_title_error.setText("");
        field_message_title_error.setVisibility(View.GONE);
        field_message_text.setText(settings.message_text);
        field_message_text_error.setText("");
        field_message_text_error.setVisibility(View.GONE);
    }

    /**
     * @return Boolean
     */
    private Boolean validateFieldToken() {
        String value = field_token.getText().toString();

        if (value.length() != 32) {
            field_token_error.setText(R.string.field_token_error_length);
            field_token_error.setVisibility(View.VISIBLE);
            return false;
        }

        field_token_error.setText("");
        field_token_error.setVisibility(View.GONE);
        return true;
    }

    /**
     * @return Boolean
     */
    private Boolean validateFieldMessageText() {
        String value = field_message_text.getText().toString();

        if (value.isEmpty()) {
            field_message_text_error.setText(R.string.field_message_text_error_length);
            field_message_text_error.setVisibility(View.VISIBLE);
            return false;
        }

        field_message_text_error.setText("");
        field_message_text_error.setVisibility(View.GONE);
        return true;
    }

    /**
     * @return Boolean
     */
    private Boolean validate()
    {
        return validateFieldToken()
            && validateFieldMessageText();
    }

    public void onClickSave(View view) {
        if (!validate()) {
            return;
        }

        settings.token         = field_token.getText().toString();
        settings.message_title = field_message_title.getText().toString();
        settings.message_text  = field_message_text.getText().toString();

        settings.Save();

        Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_SHORT).show();
    }
}