package pollub.edu.pl.kolokwium1;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import pollub.edu.pl.kolokwium1.MessageDatabase.MessageDatabaseHelper;
import pollub.edu.pl.kolokwium1.UserDatabase.UserDatabaseHelper;
import pollub.edu.pl.kolokwium1.UserDatabase.UserDatabaseProvider;

/**
 * Created by Dell on 2017-05-18.
 */

public class WriteMessageActivity extends AppCompatActivity {

    private EditText messageInput;

    private Button onlyForAdultButton;
    private Button sendMessageButton;

    boolean onlyForAdult=false;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_message);

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            userName=bundle.getString(LoginActivity.USER_NAME);

        } else {
            userName=savedInstanceState.getString(LoginActivity.USER_NAME);
        }

        messageInput= (EditText) findViewById(R.id.messageInput);

        onlyForAdultButton= (Button) findViewById(R.id.onlyForAdultButton);
        if(!onlyForAdult)
        onlyForAdultButton.setText("TRYB: TYLKO DLA PELNOLETNICH");
        else
        onlyForAdultButton.setText("TRYB: DLA WSZYSTKICH");

        onlyForAdultButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                onlyForAdult=!onlyForAdult;
                if(!onlyForAdult)
                    onlyForAdultButton.setText("TRYB: TYLKO DLA PELNOLETNICH");
                else
                    onlyForAdultButton.setText("TRYB: DLA WSZYSTKICH");
            }
        });

        sendMessageButton= (Button) findViewById(R.id.writeMessageButton);

        sendMessageButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String error=validateElements();
                if(error.equals("")){
                    ContentValues gameContentValues=new ContentValues();
                    gameContentValues.put(MessageDatabaseHelper.CONTENT_COLUMN_NAME,messageInput.getText().toString());
                    Calendar now = Calendar.getInstance();
                    gameContentValues.put(MessageDatabaseHelper.TIME_COLUMN_NAME,now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE));
                    gameContentValues.put(UserDatabaseHelper.USERNAME_COLUMN_NAME,userName);
                    gameContentValues.put(MessageDatabaseHelper.ONLY_FOR_ADULT_COLUMN_NAME,onlyForAdult);

                    getContentResolver().insert(UserDatabaseProvider.CONTENT_URI,gameContentValues);

                    Intent intent = new Intent();
                    setResult(0, intent);
                    finish();
                }else{
                    Toast toast=Toast.makeText(WriteMessageActivity.this,error,Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    private String validateElements(){
        String message="";
        if(!(messageInput.getText().toString().length()>0 && messageInput.getText().toString().length()<50)){
            message+="wiadomosc musi miec od 0 do 50 znakow\n";
        }
        message.trim();
        return message;
    }
}
