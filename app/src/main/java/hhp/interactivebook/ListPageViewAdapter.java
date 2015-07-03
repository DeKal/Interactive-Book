package hhp.interactivebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhphat on 6/30/2015.
 */
public class ListPageViewAdapter extends ArrayAdapter<InteractivePage> {
    private int textViewResourceId;
    private List<InteractivePage> pageList = new ArrayList<InteractivePage>();
    private int width;
    private int height;
    public ListPageViewAdapter(Context context, int textViewResourceId, int w, int h) {
        super(context, textViewResourceId);
        this.textViewResourceId = textViewResourceId;
        setAniImgWidth(w);
        setAniImgHeight(h);
    }

    static class PageViewHolder {
        Animation AniImg;
        TextView pageNo;
    }
    public void setAniImgWidth(int w ){
        width = w;
    }
    public void setAniImgHeight(int h){
        height = h;
    }

    @Override
    public View getView(int index, View row, ViewGroup parent) {
        PageViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(textViewResourceId, parent, false);
            viewHolder = new PageViewHolder();
            viewHolder.AniImg = (Animation) row.findViewById(R.id.pageAni);
            viewHolder.AniImg.setLayoutParams(new LinearLayout.LayoutParams(width, height));
            viewHolder.pageNo = (TextView) row.findViewById(R.id.pageNo);
            row.setTag(viewHolder);
        } else {
            viewHolder = (PageViewHolder)row.getTag();
        }
        InteractivePage page = getItem(index);

        Bitmap[] bm = new Bitmap[2];
        bm[0] = BitmapFactory.decodeResource(getContext().getResources(), page.getPageImg());
        bm[1] = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.blacken )   ;
        viewHolder.AniImg.setSize(width,height);
        viewHolder.AniImg.setBitmap(bm);
        viewHolder.AniImg.enableAnimated();
        viewHolder.pageNo.setText(page.getPageNo());
        setFont(viewHolder.pageNo);
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
    public InteractivePage getItem(int index) {
        return this.pageList.get(index);
    }
    @Override
    public void add(InteractivePage object) {
        pageList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.pageList.size();
    }


}

