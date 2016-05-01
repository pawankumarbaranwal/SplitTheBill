package pawan.example.com.splitthebill.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AddFriend extends AppCompatActivity implements View.OnClickListener{

    EditText etFriendName;
    EditText etFriendEmailId;
    Button submit;
    private SQLiteDatabase db;
    private Cursor c;
    private Toolbar mToolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        etFriendName=(EditText)findViewById(R.id.etName);
        etFriendEmailId=(EditText)findViewById(R.id.etEmail);
        submit=(Button)findViewById(R.id.btnSubmitFriendName);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("Add a Friend");


        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        createDatabase();
        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v==submit) {
            insertIntoDB();

        }
    }
    protected void createDatabase(){
        db=openOrCreateDatabase("SplitTheBill", Context.MODE_PRIVATE, null);
       /* db.execSQL("CREATE TABLE IF NOT EXISTS FRIENDS (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
                "FriendName VARCHAR, " +
                "FriendEmailId VARCHAR," +
                "Description VARCHAR," +
                "Amount VARCHAR, " +
                "SpentDate DATE," +
                "Sign VARCHAR);");*/
    }
    protected void insertIntoDB(){
        String friendName = etFriendName.getText().toString();
        String friendEmailId = etFriendEmailId.getText().toString();
        if(friendName.equals("") || friendEmailId.equals("")){
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }else {
            String query = "INSERT INTO FRIENDS (FriendName,FriendEmailId,SplittedAmount,TotalAmount,SpentDate,PaidBy) " +
                    "VALUES('" + friendName + "', '" + friendEmailId + "', '" + "0" + "', '" + "0" + "', '" + Calendar.getInstance().getTimeInMillis() + "', '" + "-" + "');";
            db.execSQL(query);
           final Toast toast= Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT);
            toast.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 500);


            MainActivity.ROW=1;
            Intent intent =new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                this.onBackPressed();
                overridePendingTransition(R.anim.shifttobottom_enter, R.anim.shifttobottom_exit);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
            overridePendingTransition(R.anim.shifttobottom_enter, R.anim.shifttobottom_exit);
        }
        else {
            getFragmentManager().popBackStack();
        }
    }
}
