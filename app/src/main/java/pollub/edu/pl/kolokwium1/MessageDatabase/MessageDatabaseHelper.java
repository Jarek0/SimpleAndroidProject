package pollub.edu.pl.kolokwium1.MessageDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dell on 2017-05-18.
 */

public class MessageDatabaseHelper extends SQLiteOpenHelper {

    public final static int DB_VERSION = 1;
    public final static String DB_NAME = "chat_db";
    public final static String TABLE_NAME = "messages";
    public final static String ID = "_id";
    public final static String CONTENT_COLUMN_NAME  = "content";
    public final static String USER_COLUMN_NAME = "user";
    public final static String TIME_COLUMN_NAME = "time";
    public final static String ONLY_FOR_ADULT_COLUMN_NAME = "only_for_adult";

    public final static String TABLE_CREATING_SCRIPT = "CREATE TABLE " + TABLE_NAME +
            "("+ ID +" integer primary key autoincrement, " +
            CONTENT_COLUMN_NAME +" text not null,"+
            USER_COLUMN_NAME+" text not null,"+
            TIME_COLUMN_NAME+" text not null,"+
            ONLY_FOR_ADULT_COLUMN_NAME+" text not null);";
    private static final String TABLE_DELETING_SCRIPT = "DROP TABLE IF EXISTS "+TABLE_NAME;

    public MessageDatabaseHelper(Context context)  {
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