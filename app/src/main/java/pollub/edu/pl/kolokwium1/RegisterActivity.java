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

import pollub.edu.pl.kolokwium1.UserDatabase.UserDatabaseHelper;
import pollub.edu.pl.kolokwium1.UserDatabase.UserDatabaseProvider;

/**
 * Created by Dell on 2017-05-18.
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText yearInput;
    private EditText countryInput;
    private EditText passwordInput;
    private EditText passwordConfirmInput;

    private Button createAccountButton;

    private UserDatabaseProvider provider=new UserDatabaseProvider();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameInput= (EditText) findViewById(R.id.usernameInput);
        yearInput= (EditText) findViewById(R.id.yearInput);
        countryInput= (EditText) findViewById(R.id.countryInput);
        passwordInput= (EditText) findViewById(R.id.passwordInput);
        passwordConfirmInput= (EditText) findViewById(R.id.passwordConfirmInput);

        createAccountButton= (Button) findViewById(R.id.createAccountButton);

        createAccountButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String error=validateElements();
                if(error.equals("")){
                    ContentValues gameContentValues=new ContentValues();
                    gameContentValues.put(UserDatabaseHelper.USERNAME_COLUMN_NAME,usernameInput.getText().toString());
                    gameContentValues.put(UserDatabaseHelper.PASSWORD_COLUMN_NAME,passwordInput.getText().toString());
                    gameContentValues.put(UserDatabaseHelper.COUNTRY_COLUMN_NAME,countryInput.getText().toString());
                    gameContentValues.put(UserDatabaseHelper.AGE_COLUMN_NAME, Calendar.getInstance().get(Calendar.YEAR)-Integer.parseInt(yearInput.getText().toString()));

                    getContentResolver().insert(UserDatabaseProvider.CONTENT_URI,gameContentValues);

                    Intent intent = new Intent();
                    setResult(0, intent);
                    finish();
                }else{
                    Toast toast=Toast.makeText(RegisterActivity.this,error,Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    private String validateElements(){
        String message="";
        if(!(usernameInput.getText().toString().length()>0 && usernameInput.getText().toString().length()<20)){
            message+="nazwa uzytkownika musi miec od 0 do 20 znakow\n";
        }
        if(!(provider.checkUserExistByUsername(usernameInput.getText().toString()))){
            message+="uzytkownik o tym imieniu juz istnieje\n";
        }
        if(!(countryInput.getText().toString().length()>0 && countryInput.getText().toString().length()<30)){
            message+="nazwa kraju musi miec od 0 do 30 znakow\n";
        }
        if(!(yearInput.getText().toString().length()>0 && yearInput.getText().toString().matches("^(19|20)[0-9]{2}$"))){
            message+="nieprawidlowy format roku\n";
        }
        if(!(passwordInput.getText().toString().length()>0 && passwordInput.getText().toString().length()<20)){
            message+="haslo musi miec od 0 do 20 znakow\n";
        }
        if(!(passwordInput.getText().toString().equals(passwordConfirmInput.getText().toString()))){
            message+="powtorzone haslo nie pasuje do podanego\n";
        }
        message.trim();
        return message;
    }
}

