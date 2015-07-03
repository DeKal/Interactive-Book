package hhp.interactivebook;

/**
 * Created by hhphat on 6/30/2015.
 */
public class InteractivePage {
    private String bookName;
    private int pageImg;
    private int pageNo;
    public InteractivePage(String name, int pageImg, int pageNo){
        setBookName(name);
        setPageImg(pageImg);
        setPageNo(pageNo);
    }
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getPageImg() {
        return pageImg;
    }

    public void setPageImg(int pageImg) {
        this.pageImg = pageImg;
    }

    public String getPageNo() {
        return pageNo+"";
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
