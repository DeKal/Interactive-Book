package hhp.interactivebook;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;


public class InteractiveBookCover extends ActionBarActivity {
    private Button buttonRead;
    private Button buttonNewUser;
    private TextView txtViewAppTitle1 ;
    private TextView txtViewAppTitle2 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interactive_book_cover);
        findView();
        setFeature();
        setEvent();
    }

    private void findView() {
        buttonRead = (Button) findViewById(R.id.Button_Read);
        buttonNewUser = (Button) findViewById(R.id.Button_NewUser);
        txtViewAppTitle1 = (TextView) findViewById(R.id.TextView_AppTitle_1);
        txtViewAppTitle2 = (TextView) findViewById(R.id.TextView_AppTitle_2);
    }

    private void setFeature() {
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/FabfeltScript-Bold.otf");
        txtViewAppTitle1.setTypeface(type);
        txtViewAppTitle1.setTextSize(55);
        txtViewAppTitle2.setTypeface(type);
        txtViewAppTitle2.setTextSize(55);
        buttonNewUser.setTypeface(type);
        buttonNewUser.setTextSize(30);
        buttonRead.setTypeface((type));
        buttonRead.setTextSize(30);
    }

    private void setEvent() {
        buttonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewBook();
            }
        });
        buttonNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsUserExist() == 0)
                    logUpUser();
                else
                    DisplayUserInfo();
            }
        });
    }

    private void DisplayUserInfo() {
        Intent intent = new Intent(this,DisplayUserInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public Object loadSerializedObject(File f)
    {
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            Object o = ois.readObject();
            return o;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    private short IsUserExist() {
        UserProfile person = (UserProfile)loadSerializedObject(new File("/sdcard/save_object.bin"));
        if (person == null)
            return 0;
        else
            return 1;
    }

    private void logUpUser() {
        Intent intent = new Intent(this,LogUpAcivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void viewBook(){
        Intent intent = new Intent(this, MainActivityViewListBook.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interactive_book_cover, menu);
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
}
