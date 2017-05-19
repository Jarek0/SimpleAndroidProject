package pollub.edu.pl.kolokwium1.UserDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dell on 2017-05-18.
 */

public class UserDatabaseHelper extends SQLiteOpenHelper {

    public final static int DB_VERSION = 1;
    public final static String DB_NAME = "chat_db";
    public final static String TABLE_NAME = "users";
    public final static String ID = "_id";
    public final static String USERNAME_COLUMN_NAME  = "username";
    public final static String AGE_COLUMN_NAME = "age";
    public final static String COUNTRY_COLUMN_NAME = "country";
    public final static String PASSWORD_COLUMN_NAME = "password";

    public final static String TABLE_CREATING_SCRIPT = "CREATE TABLE " + TABLE_NAME +
            "("+ ID +" integer primary key autoincrement, " +
            USERNAME_COLUMN_NAME +" text not null,"+
            AGE_COLUMN_NAME+" text not null,"+
            COUNTRY_COLUMN_NAME+" text not null,"+
            PASSWORD_COLUMN_NAME+" text not null);";
    private static final String TABLE_DELETING_SCRIPT = "DROP TABLE IF EXISTS "+TABLE_NAME;

    public UserDatabaseHelper(Context context)  {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATING_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
