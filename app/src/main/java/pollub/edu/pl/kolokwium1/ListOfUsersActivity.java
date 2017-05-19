package pollub.edu.pl.kolokwium1;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pollub.edu.pl.kolokwium1.UserDatabase.UserDatabaseHelper;
import pollub.edu.pl.kolokwium1.UserDatabase.UserDatabaseProvider;

/**
 * Created by Dell on 2017-05-18.
 */

public class ListOfUsersActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private UsersAdapter listAdapter;
    private ListView listView;

    int sortPointer = 0;
    ArrayList<String> kindSort = new ArrayList<String>(){{
        add(UserDatabaseHelper.ID);
        add(UserDatabaseHelper.USERNAME_COLUMN_NAME);
        add(UserDatabaseHelper.AGE_COLUMN_NAME);
        add(UserDatabaseHelper.COUNTRY_COLUMN_NAME);
    }};

    Button sortButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_users);

        listView= (ListView) findViewById(R.id.usersList);
        runLoader();

        sortButton= (Button) findViewById(R.id.sortButton);
        sortButton.setText("SORTOWANIE PO: "+kindSort.get(sortPointer));
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sortPointer+1>=kindSort.size())
                    sortPointer=0;
                else
                    sortPointer+=1;

                sortButton.setText("SORTOWANIE PO: "+kindSort.get(sortPointer));
                runLoader();
            }
        });
    }

    private void runLoader(){
        getLoaderManager().initLoader(0,null,this);
        String[] columnNames={UserDatabaseHelper.USERNAME_COLUMN_NAME,UserDatabaseHelper.AGE_COLUMN_NAME,UserDatabaseHelper.COUNTRY_COLUMN_NAME};
        Cursor cursor=getContentResolver().query(UserDatabaseProvider.CONTENT_URI,columnNames,null,null,kindSort.get(sortPointer));
        listAdapter=new UsersAdapter(this,cursor);
        listView.setAdapter(listAdapter);
    }


    @Override
    protected void onResume() {
        listAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection={UserDatabaseHelper.USERNAME_COLUMN_NAME,UserDatabaseHelper.AGE_COLUMN_NAME,UserDatabaseHelper.COUNTRY_COLUMN_NAME};
        CursorLoader cursorLoader=null;
        cursorLoader = new CursorLoader(this, UserDatabaseProvider.CONTENT_URI, projection, null, null, kindSort.get(sortPointer));
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
