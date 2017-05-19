package pollub.edu.pl.kolokwium1;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import pollub.edu.pl.kolokwium1.MessageDatabase.MessageDatabaseHelper;
import pollub.edu.pl.kolokwium1.UserDatabase.UserDatabaseHelper;

/**
 * Created by Dell on 2017-05-19.
 */

public class UsersAdapter extends CursorAdapter {
    public UsersAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.user_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvBody = (TextView) view.findViewById(R.id.userRow);
        StringBuilder body = new StringBuilder("[");
        body.append(cursor.getPosition()+1).append(". ")
                .append(cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.USERNAME_COLUMN_NAME))).append("(")
                .append(cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.AGE_COLUMN_NAME))).append(", ")
                .append(cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseHelper.COUNTRY_COLUMN_NAME))).append(")");
        tvBody.setText(body.toString());
    }
}
