package hhp.interactivebook;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Random;


public class ViewListOfPages extends ActionBarActivity {
    private String bookTitle;
    private int bookPages;
    private int columnOfGridView;

    private OrientationEventListener mOrientationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_of_pages);
        getBookInfo();
        changeOrientationManagement();
        setViewByOrientation();
        setGridViewProperties();
        putPagesToGridView();
        setEventForGridView();
        setEventForQuizButton();
    }
    private void setFont(Button btnView) {
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/FabfeltScript-Bold" +
                ".otf");
        btnView.setGravity(Gravity.CENTER);
        (btnView).setTypeface(type, Typeface.BOLD);
        btnView.setTextSize(25);
    }
    private void setEventForQuizButton() {
        Button quiz = (Button) findViewById(R.id.Button_Quiz);
        setFont(quiz);
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuizTest();
            }
        });
    }

    private void openQuizTest() {
        Intent intent = new Intent(this, QuizTestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("BookTitle", bookTitle);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentColumnOfGridView", columnOfGridView);
    }
    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        columnOfGridView = inState.getInt("CurrentColumnOfGridView");
    }
    private void setGridViewProperties() {
        GridView gv = (GridView) findViewById(R.id.GridView_PagesOfBook);
        gv.setNumColumns(columnOfGridView);
    }

    private void changeOrientationManagement() {
        mOrientationListener = new OrientationEventListener(this,
                SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                setViewByOrientation();
            }
        };
        mOrientationListener.enable();
    }
    private void setViewByOrientation() {
        int orientationStates = getResources().getConfiguration().orientation;
        if (orientationStates== Configuration.ORIENTATION_LANDSCAPE) {
            setGridViewColumn(7);
        }
        else if (orientationStates==Configuration.ORIENTATION_PORTRAIT){
            setGridViewColumn(4);
        }
        else return;
    }
    private void setGridViewColumn(int num) {

        int tmp= columnOfGridView;
        while (columnOfGridView == tmp)
            tmp= randomNumber(num-1)+2;
        columnOfGridView = tmp;

    }
    private int randomNumber(int range) {
        Random rand = new Random();
        return rand.nextInt(range);
    }

    private void setEventForGridView() {
        GridView gv = (GridView) findViewById(R.id.GridView_PagesOfBook);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openPage(position);
            }
        });
    }

    private void openPage(int position) {
        Intent intent = new Intent(this, ViewPageOfBook.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("BookImageId",createBookImageId(bookTitle,position));
        intent.putExtra("BookTitle",bookTitle);
        intent.putExtra("Pages", position);
        startActivity(intent);

    }

    private int createBookImageId(String bookTitle, int position) {
        return getResources().getIdentifier(createImageName(bookTitle,position), "drawable", "hhp" +
                ".interactivebook");
    }

    private ListPageViewAdapter createListPageViewAdapter(int layoutId) {
        int tmp=getWeightPerCollumn();
        final ListPageViewAdapter listPageViewAdapter = new ListPageViewAdapter(getApplicationContext()
                , layoutId, tmp, getAniImgHeight(tmp));
        int i = 0;
        while (i< bookPages) {
            int pageImgResId = createBookImageId(bookTitle,i);
            InteractivePage page = new InteractivePage(bookTitle, pageImgResId, i);
            listPageViewAdapter.add(page);
            ++i;
        }
        return listPageViewAdapter;
    }

    private int getAniImgHeight(int w) {
        return w*3/4;
    }

    private int getWeightPerCollumn() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        return width/columnOfGridView;
    }

    private String createImageName(String bookTitle,int num) {
        return bookTitle + "_" + num;
    }

    private void putPagesToGridView() {
        GridView gv = (GridView) findViewById(R.id.GridView_PagesOfBook);
        ListPageViewAdapter lbva = createListPageViewAdapter(R.layout.list_page_grid_layout);
        gv.setAdapter(lbva);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_list_of_pages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void getBookInfo() {
        Intent i = getIntent();
        bookTitle = i.getStringExtra("InteractiveBook");
        bookPages = i.getIntExtra("numPages",0);
    }
}
