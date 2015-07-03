package hhp.interactivebook;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by hhphat on 6/16/2015.
 */
public class twoDSprite {
    private float Left;
    private float Top;
    private float Width;
    private float Height;
    private int nBitmaps;
    private int iBitmap;
    private Bitmap[] Bitmaps;

    public twoDSprite(float left, float top, float width, float height, Bitmap[] bitmap) {
        Left= left;
        Top = top;
        Width = width;
        Height = height;
        Bitmaps = bitmap;
        nBitmaps = bitmap.length;
        iBitmap = 0;
    }


    public void Update()
    {
        iBitmap = (iBitmap +1) % nBitmaps;
    }


    public void Draw(Canvas canvas)
    {
        if (canvas != null) {

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            canvas.drawBitmap(Bitmaps[iBitmap], Left, Top, paint);

        }
    }


}
