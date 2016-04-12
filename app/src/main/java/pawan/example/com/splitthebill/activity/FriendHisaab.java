package pawan.example.com.splitthebill.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import pawan.example.com.splitthebill.dto.Friend;

public class FriendHisaab extends Activity implements View.OnClickListener {

    private EditText etFriendName;
    private EditText etFriendEmailId;
    private SQLiteDatabase db;
    private Cursor c;
    private Toolbar mToolbar;
    private List<Friend> hisaabList = new ArrayList<Friend>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_hisaab);
        hisaabList = getData();
        RecyclerView rvHisaabList = (RecyclerView) findViewById(R.id.rvHisaabList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvHisaabList.setLayoutManager(linearLayoutManager);

        RecyclerViewAdapterForFriendHisaab adapter = new RecyclerViewAdapterForFriendHisaab(hisaabList);
        rvHisaabList.setAdapter(adapter);
        etFriendName = (EditText) findViewById(R.id.etName);
        etFriendEmailId = (EditText) findViewById(R.id.etEmail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("ssssssssssssss");
        //setSupportActionBar(mToolbar);

    }

    @Override
    public void onClick(View v) {

    }

    public List<Friend> getData() {
        Friend friend = new Friend();
        db = openOrCreateDatabase("SplitTheBill", Context.MODE_PRIVATE, null);
        Intent intent = getIntent();
        String friendEmailId=intent.getStringExtra("FriendEmailId");
        c = db.rawQuery("SELECT * FROM FRIENDS WHERE  FRIENDEMAILID ='"+friendEmailId+"' AND DESCRIPTION IS NOT NULL ", null);
        if (c.moveToFirst()) {

            SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            SimpleDateFormat outputDate = new SimpleDateFormat("dd-MMM-yyyy");

            Log.i("pppppppp", c.getString(1) + "\t" + c.getString(2) + "\t" + c.getString(3) + "\t" + c.getString(4) + "\t" + c.getString(5));
            friend.setFriendName(c.getString(1) + "");
            friend.setFriendEmailId(c.getString(2) + "");
            friend.setDescription(c.getString(3) + "");
            friend.setAmount(Integer.parseInt(c.getString(4)));
            try{
                Date parsedDate = sdf.parse(c.getString(5));
                friend.setSpentDate(outputDate.format(parsedDate));
            }catch(Exception e ){
                e.printStackTrace();
            }

            hisaabList.add(friend);
            while (!c.isLast()) {
                c.moveToNext();
                friend = new Friend();
                friend.setFriendName(c.getString(1));
                friend.setFriendEmailId(c.getString(2));
                friend.setDescription(c.getString(3));
                friend.setAmount(Integer.parseInt(c.getString(4)));
                try{
                    Date parsedDate = sdf.parse(c.getString(5));
                    friend.setSpentDate(outputDate.format(parsedDate));
                }catch(Exception e ) {
                    e.printStackTrace();
                }
                hisaabList.add(friend);
            }
        }
        return hisaabList;
    }
}