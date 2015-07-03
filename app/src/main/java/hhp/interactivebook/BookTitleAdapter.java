package hhp.interactivebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import hhp.interactivebook.R;

/**
 * Created by hhphat on 6/28/2015.
 */
public class BookTitleAdapter extends ArrayAdapter<InteractiveBook> {
    private int textViewResourceId;
    private List<InteractiveBook> bookList = new ArrayList<InteractiveBook>();

    public BookTitleAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.textViewResourceId = textViewResourceId;
    }
    static class BookViewHolder {
//      ImageView bookImg;
        Animation bookImgAni;
        TextView bookName;
    }
    @Override
    public View getView(int index, View row, ViewGroup parent) {

        BookViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(textViewResourceId, parent, false);
            viewHolder = new BookViewHolder();
            viewHolder.bookImgAni = (Animation) row.findViewById(R.id.bookAni);
            //viewHolder.bookImg = (ImageView) row.findViewById(R.id.bookImg);
            viewHolder.bookName = (TextView) row.findViewById(R.id.bookName);
            row.setTag(viewHolder);
        } else {
            viewHolder = (BookViewHolder)row.getTag();
        }
        InteractiveBook book = getItem(index);
        //viewHolder.bookImg.setImageResource(book.getBookImg());

        Bitmap[] bm = new Bitmap[2];
        bm[0] = BitmapFactory.decodeResource(getContext().getResources(),book.getBookImg() );
        bm[1] = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.blacken )   ;
        viewHolder.bookImgAni.setSize(220,180);
        viewHolder.bookImgAni.setBitmap(bm);
        viewHolder.bookImgAni.enableAnimated();

        viewHolder.bookName.setText(book.getBookName());
        setFont(viewHolder.bookName);
        return row;


    }
    private void setFont(TextView txtView) {
        Typeface type = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/FabfeltScript-Bold" +
                ".otf");
        txtView.setGravity(Gravity.CENTER);
        (txtView).setTypeface(type);
        txtView.setTextSize(30);

    }
    @Override
    public InteractiveBook getItem(int index) {
        return this.bookList.get(index);
    }
    @Override
    public void add(InteractiveBook object) {
        bookList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.bookList.size();
    }


}
