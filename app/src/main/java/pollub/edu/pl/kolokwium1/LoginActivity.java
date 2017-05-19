package pollub.edu.pl.kolokwium1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pollub.edu.pl.kolokwium1.UserDatabase.UserDatabaseProvider;

public class LoginActivity extends AppCompatActivity {

    public final static String USER_AGE = "user age";
    public final static String USER_NAME = "user name";

    private Button loginButton;
    private Button registerButton;
    private Button viewListOfUsersButton;

    private EditText loginInput;
    private EditText passwordInput;

    private UserDatabaseProvider provider=new UserDatabaseProvider();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton= (Button) findViewById(R.id.loginButton);
        registerButton= (Button) findViewById(R.id.registerButton);
        viewListOfUsersButton= (Button) findViewById(R.id.viewListOfUserButton);

        loginInput= (EditText) findViewById(R.id.loginEditText);
        passwordInput= (EditText) findViewById(R.id.passwordEditText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login=loginInput.getText().toString();
                String password=passwordInput.getText().toString();
                try {
                    int age = provider.getUserAgeByLoginAndPassword(login, password);
                    Intent loginToChatIntent = new Intent(LoginActivity.this, ChatActivity.class);
                    loginToChatIntent.putExtra(USER_NAME, login);
                    loginToChatIntent.putExtra(USER_AGE, age);
                    startActivityForResult(loginToChatIntent, 0);
                }
                catch(Exception e){
                    Toast toast = Toast.makeText(LoginActivity.this, e.getMessage() , Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        StringBuilder registerButtonText = new StringBuilder("ZAREJESTRUJ SIE (OBECNIE: ");
        registerButtonText.append(provider.getProfilesCount()).append("  OSOB)");
        registerButton.setText(registerButtonText.toString());
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(registerIntent, 0);
            }
        });

        viewListOfUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listOfUsersIntent = new Intent(LoginActivity.this, ListOfUsersActivity.class);
                startActivityForResult(listOfUsersIntent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            Toast toast = Toast.makeText(LoginActivity.this, "Uzytkownik zostal dodany" , Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
