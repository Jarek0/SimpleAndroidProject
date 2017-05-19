package pollub.edu.pl.kolokwium1;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import pollub.edu.pl.kolokwium1.MessageDatabase.MessageDatabaseHelper;
import pollub.edu.pl.kolokwium1.MessageDatabase.MessageDatabaseProvider;

/**
 * Created by Dell on 2017-05-18.
 */

public class ChatActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private MessageAdapter listAdapter;
    private ListView listView;

    boolean accessToAdult = false;
    String userName;

    Button wrtieMessageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final int userAge;
        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            userAge=bundle.getInt(LoginActivity.USER_AGE);
            userName=bundle.getString(LoginActivity.USER_NAME);

        } else {
            userAge = savedInstanceState.getInt(LoginActivity.USER_AGE);
            userName=savedInstanceState.getString(LoginActivity.USER_NAME);
        }
        if(userAge>=18)
        accessToAdult = true;

        listView= (ListView) findViewById(R.id.messagesList);
        runLoader();

        wrtieMessageButton= (Button) findViewById(R.id.writeMessageButton);
        wrtieMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent writeMessageIntent = new Intent(ChatActivity.this, WriteMessageActivity.class);
                writeMessageIntent.putExtra(LoginActivity.USER_NAME, userName);
                startActivityForResult(writeMessageIntent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast toast;
        toast= Toast.makeText(ChatActivity.this, "Wiadomosc wyslana" , Toast.LENGTH_LONG);
        runLoader();
        toast.show();
    }

    private void runLoader(){
        getLoaderManager().initLoader(0,null,this);
        String[] columnNames={MessageDatabaseHelper.TIME_COLUMN_NAME,MessageDatabaseHelper.USER_COLUMN_NAME,MessageDatabaseHelper.CONTENT_COLUMN_NAME};
        Cursor cursor=getContentResolver().query(MessageDatabaseProvider.CONTENT_URI,columnNames,null,null,MessageDatabaseHelper.TIME_COLUMN_NAME);
        listAdapter=new MessageAdapter(this,cursor);
        listView.setAdapter(listAdapter);
    }


    @Override
    protected void onResume() {
        listAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection={MessageDatabaseHelper.TIME_COLUMN_NAME,MessageDatabaseHelper.USER_COLUMN_NAME,MessageDatabaseHelper.CONTENT_COLUMN_NAME};
        CursorLoader cursorLoader=null;
        if(!accessToAdult) {
            String selection = MessageDatabaseHelper.ONLY_FOR_ADULT_COLUMN_NAME + "!=";
            String[] selectionArgs = {"true"};
            cursorLoader = new CursorLoader(this, MessageDatabaseProvider.CONTENT_URI, projection, selection, selectionArgs, MessageDatabaseHelper.TIME_COLUMN_NAME);
        }
        else
        {
            cursorLoader = new CursorLoader(this, MessageDatabaseProvider.CONTENT_URI, projection, null, null, MessageDatabaseHelper.TIME_COLUMN_NAME);
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        listAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        listAdapter.swapCursor(null);

    }
}
