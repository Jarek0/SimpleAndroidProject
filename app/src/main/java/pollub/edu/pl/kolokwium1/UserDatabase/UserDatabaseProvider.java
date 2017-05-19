package pollub.edu.pl.kolokwium1.UserDatabase;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import pollub.edu.pl.kolokwium1.Exceptions.UserNotFoundException;
import pollub.edu.pl.kolokwium1.MessageDatabase.MessageDatabaseHelper;

/**
 * Created by Dell on 2017-05-18.
 */

public class UserDatabaseProvider extends ContentProvider {

    private static UserDatabaseHelper openHelper;

    private static final String PROVIDER_ID =
            "pollub.edu.pl.kolokwium1.UserDatabase.UserDatabaseProvider";

    public static final Uri CONTENT_URI = Uri.parse("content://"
            + PROVIDER_ID + "/" + openHelper.TABLE_NAME);

    private static final int FULL_TABLE = 1;
    private static final int SELECTED_ROW = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);
    static {

        uriMatcher.addURI(PROVIDER_ID, openHelper.TABLE_NAME,
                FULL_TABLE);
        uriMatcher.addURI(PROVIDER_ID, openHelper.TABLE_NAME +
                "/#",SELECTED_ROW);
    }

    @Override
    public boolean onCreate() {
        openHelper=new UserDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int typUri = uriMatcher.match(uri);

        SQLiteDatabase readableDatabase = openHelper.getReadableDatabase();
        Cursor kursor = null;
        switch (typUri) {
            case FULL_TABLE:
                kursor =readableDatabase.query(true,openHelper.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder,null);
                break;
            case SELECTED_ROW:
                kursor =readableDatabase.query(true,openHelper.TABLE_NAME,projection,addIdToSelectionClausule(selection,uri),selectionArgs,null,null,sortOrder,null);
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI: " + uri);
        }

        kursor.setNotificationUri(getContext().
                getContentResolver(), uri);
        return kursor;
    }

    private String addIdToSelectionClausule(String selectionClausule,Uri uri){
        if(selectionClausule!=null && !selectionClausule.equals(""))
            selectionClausule+=" and "+MessageDatabaseHelper.ID +"="+uri.getLastPathSegment();
        else
            selectionClausule=MessageDatabaseHelper.ID +"="+uri.getLastPathSegment();
        return selectionClausule;
    }

    public long getProfilesCount() {
        SQLiteDatabase db = openHelper.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, openHelper.TABLE_NAME);
        db.close();
        return cnt;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = uriMatcher.match(uri);

        SQLiteDatabase writableDatabase = openHelper.getWritableDatabase();
        long addedElementId = 0;
        switch (uriType) {
            case FULL_TABLE:
                addedElementId=writableDatabase.insertOrThrow(openHelper.TABLE_NAME,null,values);
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI: " +
                        uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(openHelper.TABLE_NAME + "/" + addedElementId);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType=uriMatcher.match(uri);
        SQLiteDatabase writableDatabase = openHelper.getWritableDatabase();
        int deletedElements=0;
        switch(uriType){
            case FULL_TABLE:
                deletedElements=writableDatabase.delete(MessageDatabaseHelper.TABLE_NAME,selection,selectionArgs);
                break;
            case SELECTED_ROW:
                deletedElements=writableDatabase.delete(MessageDatabaseHelper.TABLE_NAME,addIdToSelectionClausule(selection,uri),selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI: " + uri);
        }
        return deletedElements;

    }

    public void deleteAll(){
        SQLiteDatabase writableDatabase = openHelper.getWritableDatabase();
        writableDatabase.execSQL("delete from "+ MessageDatabaseHelper.TABLE_NAME);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int typUri = uriMatcher.match(uri);
        SQLiteDatabase writableDatabase = openHelper.getWritableDatabase();
        int updatedRecords;
        switch (typUri) {
            case FULL_TABLE:
                updatedRecords=writableDatabase.update(openHelper.TABLE_NAME,values,selection,selectionArgs);
                break;
            case SELECTED_ROW:
                updatedRecords=writableDatabase.update(openHelper.TABLE_NAME,values,addIdToSelectionClausule(selection,uri),selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI: " + uri);
        }
        return updatedRecords;
    }

    public int getUserAgeByLoginAndPassword(String login, String password) throws UserNotFoundException {
        SQLiteDatabase sqldb = openHelper.getWritableDatabase();
        StringBuilder query = new StringBuilder("Select ");
        query.append(openHelper.AGE_COLUMN_NAME).append(" from ").append(openHelper.TABLE_NAME).append(" where ").append(openHelper.USERNAME_COLUMN_NAME).append("like").append(login).append(" and ").append(openHelper.PASSWORD_COLUMN_NAME).append(" like ").append(password);
        Cursor cursor = sqldb.rawQuery(query.toString(), null);
        if(cursor.getCount()==0)
            throw new UserNotFoundException("Nieprawidlowy login lub haslo");
        return cursor.getInt(0);
    }

    public boolean checkUserExistByUsername(String username){
        SQLiteDatabase sqldb = openHelper.getWritableDatabase();
        StringBuilder query = new StringBuilder("Select ");
        query.append(openHelper.AGE_COLUMN_NAME).append(" from ").append(openHelper.TABLE_NAME).append(" where ").append(openHelper.USERNAME_COLUMN_NAME).append("like").append(username);
        Cursor cursor = sqldb.rawQuery(query.toString(), null);
        if(cursor.getCount()==0)
            return false;

        return true;
    }
}
