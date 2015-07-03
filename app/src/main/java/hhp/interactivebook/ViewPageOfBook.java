package hhp.interactivebook;

import android.content.Intent;
import android.graphics.Typeface;
import android.renderscript.Sampler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;
import java.lang.reflect.Field;
import java.util.Locale;


public class ViewPageOfBook extends ActionBarActivity implements
        TextToSpeech.OnInitListener {
    private String bookTitle;
    private int pageNo;
    private int pageImageId;
    private Button speak;
    private TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page_of_book);
        tts = new TextToSpeech(this, this);
        speak = (Button) findViewById(R.id.Btn_Speak);
        setFont(speak);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakOut(getPageContent());
            }
        });
        getPageInfo();
        displayPageContent();
    }

    private String getPageContent() {
        TextView tmp = (TextView)findViewById(R.id.TextView_PageContent);
        setFont(tmp);
        int id = this.getResources().getIdentifier(createStringName(bookTitle,pageNo), "string",getPackageName());
        tmp.setText(Html.fromHtml(getString(id)));
        return tmp.getText().toString();
    }

    private void setFont(TextView txtView) {
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/EBGaramond08-Regular" +
                ".ttf");
        (txtView).setTypeface(type);
    }
    private void displayPageContent() {
        TextView tmp = (TextView)findViewById(R.id.TextView_PageContent);
        setFont(tmp);
        int id = this.getResources().getIdentifier(createStringName(bookTitle,pageNo), "string",getPackageName());
        tmp.setText(Html.fromHtml(getString(id)));
        tmp.setMovementMethod(new ScrollingMovementMethod());
        ImageView temp = (ImageView) findViewById(R.id.ImageView_PageImage);
        temp.setImageResource(pageImageId);
    }

    private String createStringName(String bookTitle, int pageNo) {
        return bookTitle + "_page_" + pageNo + "_html";
    }
    private void speakOut(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_page_of_book, menu);
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

    public void getPageInfo() {
        Intent i = getIntent();
        bookTitle = i.getStringExtra("BookTitle");
        pageNo = i.getIntExtra("Pages", 0);
        pageImageId = i.getIntExtra("BookImageId",0);
    }
    //http://www.androidhive.info/2012/01/android-text-to-speech-tutorial/
    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
                speak.setEnabled(false);
            } else {
                speak.setEnabled(true);
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }
}
