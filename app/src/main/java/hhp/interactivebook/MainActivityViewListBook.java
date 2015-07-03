package hhp.interactivebook;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


public class MainActivityViewListBook extends ActionBarActivity {
    private OrientationEventListener mOrientationListener;
    private List<String[]> bookList;
    private List<Integer> numOfPages;
    private int currentMode = 1;
    private Button btnVoiceSearch;
    private Button btnSkin1;
    private Button btnSkin2;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private void setBtnVoiceSearchProperties(){
        btnVoiceSearch = (Button) findViewById(R.id.Button_VoiceSearch);
        btnVoiceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookList = getBookNameFromDirective();
        setViewByOrientation();
        setEventForCommonFeatures();
        changeOrientationManagement();
    }

    private void setFont() {
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/FabfeltScript-Bold.otf");
        (btnSkin1).setTypeface(type, Typeface.BOLD);
        btnSkin1.setTextSize(30);
        (btnSkin2).setTypeface(type, Typeface.BOLD);
        btnSkin2.setTextSize(30);
        btnVoiceSearch.setTypeface(type, Typeface.BOLD);
        btnVoiceSearch.setTextSize(30);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentBookTitleSkin", currentMode);
    }
    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        currentMode = inState.getInt("CurrentBookTitleSkin");
    }
    private void changeStateSkinButton(int mode) {
        currentMode = mode;
        if (mode == 1){
            btnSkin1.setVisibility(View.GONE);
            btnSkin2.setVisibility(View.VISIBLE);
        }
        else{
            btnSkin2.setVisibility(View.GONE);
            btnSkin1.setVisibility(View.VISIBLE);
        }
    }
    private void setEventForCommonFeatures() {
        getControl();
        setEventForAlterSkinButton();
        setBtnVoiceSearchProperties();
        setFont();
    }

    private void setEventForAlterSkinButton() {
        findViewById(R.id.Btn_Skin1ForChooseBook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBookTitleListView();
            }
        });
        findViewById(R.id.Btn_Skin2ForChooseBook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStateSkinButton(2);
                setViewByOrientation();
            }
        });
    }

    private void putBookToGridView() {
        GridView gv = (GridView) findViewById(R.id.GridView_BookImages);
        BookTitleAdapter bta = createBookTitleAdapter(R.layout.list_book_grid_layout);
        gv.setAdapter(bta);
    }
    private void setEventForGridView(){
        GridView gv = (GridView) findViewById(R.id.GridView_BookImages);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openListPage(position);
            }
        });

    }

    private void setEventForListView() {
        ListView listView = (ListView) findViewById(R.id.mylist);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openListPage(position);
            }
        });
    }

    private void openListPage(int position) {
        Intent intent = new Intent(this, ViewListOfPages.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("InteractiveBook", bookList.get(position)[0]);
        intent.putExtra("numPages", numOfPages.get(position));
        startActivity(intent);
    }


    private BookTitleAdapter createBookTitleAdapter(int layoutId) {
        final BookTitleAdapter bookTitleAdapter = new BookTitleAdapter(getApplicationContext(), layoutId);
        for (String[] bookData : bookList) {
            String bookImg = bookData[0];
            String bookName = bookData[1];
            int bookImgResId = getResources().getIdentifier(bookImg, "drawable", "hhp.interactivebook");
            InteractiveBook book = new InteractiveBook(bookImgResId, bookName);
            bookTitleAdapter.add(book);
        }
        return bookTitleAdapter;
    }

    private void putBookToListView() {
        ListView list = (ListView) findViewById(R.id.mylist);
        BookTitleAdapter bookTitleAdapter = createBookTitleAdapter(R.layout.list_book_layout);
        list.setAdapter(bookTitleAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_view_list_book, menu);
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

    private List<String[]> getBookNameFromDirective() {
        List<String[]> resultList = new ArrayList<String[]>();
        InputStream ins = getResources().openRawResource(R.raw.listofbookname);
                //getResources().getIdentifier("raw/listofbookname",
                  //      "raw", getPackageName()));
        BufferedReader r = new BufferedReader(new InputStreamReader(ins));
        String line;
        numOfPages = new ArrayList<Integer>();
        try {
            while ((line = r.readLine()) != null) {
                String[] tmp = new String[2];
                tmp[1] = line;// book img name
                line = r.readLine();
                tmp[0] = line;// book name
                line = r.readLine();
                numOfPages.add(Integer.parseInt(line));
                resultList.add(tmp);
            }
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    private void setViewByOrientation() {
        if (currentMode == 1) {
            setBookTitleListView();
            return;
        }
        int orientationStates = getResources().getConfiguration().orientation;
        if (orientationStates== Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_activity_view_images_book_landscape);
        }
        else if (orientationStates==Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_main_activity_view_images_book);
        }
        else return;
        setEventForImagesView();
    }

    private void setBookTitleListView() {
        setContentView(R.layout.activity_main_activity_view_list_book);
        setEventForCommonFeatures();
        putBookToListView();
        changeStateSkinButton(1);
        setEventForListView();
    }

    private void setEventForImagesView() {
        setEventForCommonFeatures();
        putBookToGridView();
        setEventForGridView();
        changeStateSkinButton(2);
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

    public void getControl() {
        btnSkin1 = (Button)findViewById(R.id.Btn_Skin1ForChooseBook);
        btnSkin2 = (Button) findViewById(R.id.Btn_Skin2ForChooseBook);
    }
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    OpenBook(result.get(0));
                }
                break;
            }

        }
    }

    private void OpenBook(String bookName) {
        int index = 0;
        for (String[] bookData : bookList) {
            if (bookName.equals(bookData[1])){
                openListPage(index);
                index = -1;
                break;
            }
            index++;
        }
        if (index == -1)
            Toast.makeText(getApplicationContext(),
                    "Book Not Found",
                    Toast.LENGTH_LONG).show();
    }

}
