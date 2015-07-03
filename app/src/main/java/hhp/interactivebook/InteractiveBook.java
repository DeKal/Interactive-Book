package hhp.interactivebook;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hhphat on 6/29/2015.
 */
public class InteractiveBook implements Parcelable {

    private int bookImg;
    private String bookName;
    private int pageNum;

    public InteractiveBook(int bookImg, String bookName) {
        super();
        this.setBookImg(bookImg);
        this.setBookName(bookName);
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getBookImg() {
        return bookImg;
    }

    public void setBookImg(int bookImg) {
        this.bookImg = bookImg;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getBookImg());
        dest.writeString(getBookName());
        dest.writeInt(getPageNum());
    }
    private InteractiveBook(Parcel in) {
        setBookImg(in.readInt());
        setBookName(in.readString());
        setPageNum(in.readInt());
    }
    public static final Parcelable.Creator<InteractiveBook> CREATOR = new Parcelable.Creator<InteractiveBook>() {
        public InteractiveBook createFromParcel(Parcel in) {
            return new InteractiveBook(in);
        }

        public InteractiveBook[] newArray(int size) {
            return new InteractiveBook[size];
        }
    };


}
