package pollub.edu.pl.kolokwium1;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import pollub.edu.pl.kolokwium1.MessageDatabase.MessageDatabaseHelper;

/**
 * Created by Dell on 2017-05-19.
 */

public class MessageAdapter extends CursorAdapter {
    public MessageAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.chat_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvBody = (TextView) view.findViewById(R.id.chatRow);
        StringBuilder body = new StringBuilder("[");
        body.append(cursor.getString(cursor.getColumnIndexOrThrow(MessageDatabaseHelper.TIME_COLUMN_NAME))).append("] ")
                .append(cursor.getString(cursor.getColumnIndexOrThrow(MessageDatabaseHelper.USER_COLUMN_NAME))).append(": ")
                .append(cursor.getString(cursor.getColumnIndexOrThrow(MessageDatabaseHelper.CONTENT_COLUMN_NAME)));
        tvBody.setText(body.toString());
    }
}
