package hhp.interactivebook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


public class LogUpAcivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_up_acivity);
        findViewById(R.id.btnDoneLogUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewProfile(getNewUserProfile());
                BackToCover();
            }
        });
    }

    private void saveNewProfile(UserProfile newUserProfile) {
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File
                ("/sdcard/save_object.bin"))); //Select where you wish to save the file...
            oos.writeObject(newUserProfile); // write the class as an 'object'
            oos.flush(); // flush the stream to insure all of the information was written to 'save_object.bin'
            oos.close();// close the stream
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }

    private UserProfile getNewUserProfile() {
        EditText tmp1 = (EditText) findViewById(R.id.username);
        EditText tmp2 = (EditText) findViewById(R.id.email);
        EditText tmp3 = (EditText) findViewById(R.id.fb);
        return new UserProfile(tmp1.getText().toString()
                            ,tmp2.getText().toString(),tmp3.getText().toString());
    }

    private void BackToCover() {
        Intent intent = new Intent(this,InteractiveBookCover.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_up_acivity, menu);
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
