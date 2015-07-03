package hhp.interactivebook;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;


public class DisplayUserInfoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_info);
        showInfo(loadUserInfo());
        setButtonEvent();
    }

    private void setButtonEvent() {
        findViewById(R.id.btnBackInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackToBookCover();
            }
        });
        findViewById(R.id.Button_NewUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logUpUser();
            }
        });
    }

    private void BackToBookCover() {
        Intent intent = new Intent(this, InteractiveBookCover.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }

    private void logUpUser() {
        Intent intent = new Intent(this,LogUpAcivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private UserProfile loadUserInfo() {
        return (UserProfile)loadSerializedObject(new File("/sdcard/save_object.bin"));
    }

    private void showInfo(UserProfile up){
        EditText tmp = (EditText) findViewById(R.id.username);
        tmp.setText(up.getName());
        tmp = (EditText) findViewById(R.id.email);
        tmp.setText(up.getEmail());
        tmp = (EditText) findViewById(R.id.fb);
        tmp.setText(up.getFb());
        tmp = (EditText) findViewById(R.id.EditText_Points);
        tmp.setText(""+ up.getPoints());
        tmp = (EditText) findViewById(R.id.EditText_Levels);
        tmp.setText("" + up.getLevel());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_user_info, menu);
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
}
