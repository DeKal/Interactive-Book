package hhp.interactivebook;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;


public class QuizTestActivity extends ActionBarActivity implements
        TextToSpeech.OnInitListener{
    private String bookTitle;
    private Button nextQuestion;
    private UserProfile user;
    private TextToSpeech tts;
    private int numQuest;
    private int currentQuest = 0;
    private int currentRightQuest;
    private boolean checkAns = false;
    private RadioGroup rg ;
    private Toast congratImgToast;
    private MediaPlayer mp = null;
    private void getBookInfo() {
        Intent i = getIntent();
        bookTitle = i.getStringExtra("BookTitle");
    }
    @Override
    protected void onPause(){
        super.onPause();
        if (mp != null) mp.stop();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_test);
        tts = new TextToSpeech(this, this);
        congratImgToast = createCongratsImage();
        setEventForButton();
        getBookInfo();
        getNumOfQuest();
        InitData();
        setEventForRadioButton();
    }


    private void setEventForRadioButton() {
        rg = (RadioGroup) findViewById(R.id.radioGroup_ChooseAnswer);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int[] radioButtionId = new int[]{R.id.radioButton_ans1, R.id.radioButton_ans2, R.id
                        .radioButton_ans3, R.id.radioButton_ans4};
                if (!checkAns) {
                    nextQuestion.setEnabled(true);
                    checkAns = true;
                    if (checkedId == radioButtionId[currentRightQuest]) {
                        user = loadUserInfo();
                        user.raisePoint(1);

                        saveNewProfile(user);
                        ShowCongrats();

                    } else {
                        speakOut("You're wrong");
                        rg.check(radioButtionId[currentRightQuest]);
                    }
                    setEnabledRadioButton(false);

                }
            }
        });
    }

    private void ShowCongrats() {
        if (user.isLevelUp()) {
            displayCongratsImage();
            Toast.makeText(getApplicationContext(), "Congratulation! Your level is up",
                    Toast.LENGTH_LONG).show();
        }
        else
            speakOut("You're right");
    }
    private Toast createCongratsImage() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.congrats_image,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id));
        ImageView image = (ImageView) layout.findViewById(R.id.imageCongrats);
        image.setImageResource(R.drawable.congrat);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        return toast;
    }
    private void displayCongratsImage() {
        congratImgToast.show();
        mp = MediaPlayer.create(this, R.raw.hammerfall_last_man_standing);
        mp.start();
    }

    private void setEnabledRadioButton(boolean enable) {
        int[] radioButtionId = new int[]{R.id.radioButton_ans1,R.id.radioButton_ans2,R.id
                .radioButton_ans3,R.id.radioButton_ans4};
        int i=0;
        while (i<4) {
            int id = this.getResources().getIdentifier(createAnsName(bookTitle, i), "string",
                    getPackageName());
            RadioButton tmp = (RadioButton) findViewById(radioButtionId[i]);
            tmp.setEnabled(enable);
            ++i;
        }
    }

    private void saveNewProfile(UserProfile newUserProfile) {
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File
                    ("/sdcard/save_object.bin")));
            oos.writeObject(newUserProfile);
            oos.flush();
            oos.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }
    private UserProfile loadUserInfo() {
        return (UserProfile)loadSerializedObject(new File("/sdcard/save_object.bin"));
    }

    public Object loadSerializedObject(File f)
    {
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            return ois.readObject();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    private void InitData() {
        setQuizQuestion();
        setQuizAnswer();
        setRightAnswer();
    }

    private void setRightAnswer() {
        int id = this.getResources().getIdentifier(createRightAnsName(bookTitle), "integer",
                getPackageName());
        currentRightQuest = Integer.parseInt(getString(id));
    }

    private String createRightAnsName(String bookTitle) {
        return bookTitle+"_"+currentQuest+"_right_ans";
    }

    private void setQuizAnswer() {
        int[] radioButtionId = new int[]{R.id.radioButton_ans1,R.id.radioButton_ans2,R.id
                .radioButton_ans3,R.id.radioButton_ans4};
        int i=0;
        while (i<4){
            int id = this.getResources().getIdentifier(createAnsName(bookTitle,i), "string",
                    getPackageName());
            RadioButton tmp = (RadioButton) findViewById(radioButtionId[i]);
            tmp.setText(id);
            ++i;
        }
    }

    private String createAnsName(String bookTitle, int index) {
        return bookTitle+ "_ans_" + currentQuest +"_" + index;
    }

    private void setQuizQuestion() {
        int id = this.getResources().getIdentifier(createQuestName(bookTitle), "string",
                getPackageName());
        TextView quest = (TextView) findViewById(R.id.TextView_Question);
        quest.setText(id);
    }

    private String createQuestName(String bookTitle) {
        return bookTitle+ "_quest_" + currentQuest;
    }

    private void getNumOfQuest() {
        int id = this.getResources().getIdentifier(createNumOfQuestName(bookTitle), "integer",
                getPackageName());
        numQuest = Integer.parseInt(getString(id));
    }

    private String createNumOfQuestName(String bookTitle) {
        return bookTitle+ "_num_quest";
    }

    private void setEventForButton() {
        nextQuestion = (Button) findViewById(R.id.Button_NextQuestion);
        nextQuestion.setEnabled(false);
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuest < numQuest-1) UpdateQuest();
                else {
                    Toast.makeText(getApplicationContext(), "OUT OF QUESTION !!! Please go back !",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void UpdateQuest() {
            currentQuest++;
            nextQuestion.setEnabled(false);
            InitData();
            rg.clearCheck();
            setEnabledRadioButton(true);
            checkAns = false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz_test, menu);
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
    private void speakOut(String text) {
        tts.setPitch(1.3f);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
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
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }
}
