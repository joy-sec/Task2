package sust.hackathon.helper.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import sust.hackathon.helper.R;

/**
 * Created by joy on 8/5/17.
 */

public class ListAdapter  extends android.widget.CursorAdapter {

    private LayoutInflater cursorInflater;

    public ListAdapter(Context context, Cursor c) {
        super(context, c);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = cursorInflater.inflate(R.layout.custom_layout, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView title, date, msg;
        ImageView imageView;

        title = (TextView) view.findViewById(R.id.title_textview);
        msg = (TextView) view.findViewById(R.id.msg_textview);
        imageView = (ImageView) view.findViewById(R.id.round_image);

        Typeface fonts = Typeface.DEFAULT;
       // title.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Aller_Rg.ttf"));
        msg.setTypeface(fonts);

        title.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        msg.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));
        //date.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3))));

        //ColorGenerator generator = ColorGenerator.MATERIAL;

        //int color1 = generator.getRandomColor();
        String tit = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)));
        String dt = tit.substring(0, 1);
        //TextDrawable textDrawable= TextDrawable.builder().buildRound(dt,color1);
        //imageView.setImageDrawable(textDrawable);

    }
}
