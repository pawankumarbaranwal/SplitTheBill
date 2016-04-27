package pawan.example.com.splitthebill.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


import pawan.example.com.splitthebill.dto.Friend;

public class FriendHisaab extends AppCompatActivity implements View.OnClickListener {

    private SQLiteDatabase db;
    private Cursor c;
    private Toolbar mToolbar;
    private List<Friend> hisaabList = new ArrayList<Friend>();
    String friendEmailId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_hisaab);
        hisaabList = getData();
        RecyclerView rvHisaabList = (RecyclerView) findViewById(R.id.rvHisaabList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvHisaabList.setLayoutManager(linearLayoutManager);

        RecyclerViewAdapterForFriendHisaab adapter = new RecyclerViewAdapterForFriendHisaab(hisaabList, this);
        rvHisaabList.setAdapter(adapter);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);

        FriendsFragment friendsFragment = new FriendsFragment();
        friendsFragment.getTotalAmount(friendEmailId);
        saveAsText();
        Integer totalAmount = getTotalAmount(friendEmailId);
        if (totalAmount > 0) {
            mToolbar.setTitle(friendEmailId + " will return " + totalAmount + "");
        } else if (totalAmount < 0) {
            mToolbar.setTitle("You will pay " + (totalAmount * -1) + "");
        } else {
            mToolbar.setTitle("Settled ");
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private Integer getTotalAmount(String friendEmailId) {
        c = db.rawQuery("SELECT sum(splittedAmount) FROM FRIENDS WHERE  FRIENDEMAILID ='" + friendEmailId + "' AND DESCRIPTION IS NOT NULL ", null);
        if ((c.moveToFirst() && (c.getString(0) != null))) {
            return Integer.parseInt(c.getString(0));
        } else {
            return 0;
        }
    }

    @Override
    public void onClick(View v) {

    }

    public List<Friend> getData() {
        Friend friend = new Friend();
        db = openOrCreateDatabase("SplitTheBill", Context.MODE_PRIVATE, null);
        Intent intent = getIntent();
        friendEmailId = intent.getStringExtra("FriendEmailId");
        c = db.rawQuery("SELECT * FROM FRIENDS WHERE  FRIENDEMAILID ='" + friendEmailId + "' AND DESCRIPTION IS NOT NULL ", null);
        if (c.moveToFirst()) {/*
            SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            SimpleDateFormat outputDate = new SimpleDateFormat("dd-MMM-yyyy");*/


            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy : hh/mm/ss");

            long milliSeconds= Long.parseLong(c.getString(6));
            Log.i("pppppppp", milliSeconds + "");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);

            Log.i("pppppppp", c.getString(1) + "\t" + c.getString(2) + "\t" + c.getString(3) + "\t" + c.getString(4) + "\t" + c.getString(5));
            Log.i("pppppppp", c.getString(6));
            friend.setFriendName(c.getString(1) + "");
            friend.setFriendEmailId(c.getString(2) + "");
            friend.setDescription(c.getString(3) + "");
            friend.setSplittedAmount(Integer.parseInt(c.getString(4)));
            friend.setTotalAmount(Integer.parseInt(c.getString(5)));
            friend.setPaidBy(c.getString(7));
            try {
                friend.setSpentDate(formatter.format(calendar.getTime()));

            } catch (Exception e) {
                e.printStackTrace();
            }

            hisaabList.add(friend);
            while (!c.isLast()) {
                c.moveToNext();
                friend = new Friend();
                friend.setFriendName(c.getString(1));
                friend.setFriendEmailId(c.getString(2));
                friend.setDescription(c.getString(3));
                friend.setSplittedAmount(Integer.parseInt(c.getString(4)));
                friend.setTotalAmount(Integer.parseInt(c.getString(5)));
                friend.setPaidBy(c.getString(7));
                milliSeconds= Long.parseLong(c.getString(6));
                calendar.setTimeInMillis(milliSeconds);
                try {
//                    Date parsedDate = sdf.parse("Mon Apr 25 21:33:53 IST 2016");
                    //friend.setSpentDate(outputDate.format(parsedDate));
                    friend.setSpentDate(formatter.format(calendar.getTime()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                hisaabList.add(friend);
            }
        }
        return hisaabList;
    }

    public void saveAsText() {

        try {
            if (hisaabList.size() != 0) {
                FileOutputStream fos = new FileOutputStream("/sdcard/SplitTheBill/" + hisaabList.get(0).getFriendEmailId() + ".txt");

                for (int i = 0; i < hisaabList.size(); i++) {

                    fos.write((hisaabList.get(i).getDescription() + "\t\t" + hisaabList.get(i).getSpentDate() + "\n").getBytes());
                    if (hisaabList.get(i).getPaidBy().equals("You")) {
                        Log.i("mamamamam", hisaabList.get(i).getPaidBy() + " Paid " + hisaabList.get(i).getTotalAmount());
                        Log.i("mamamamam", "You will get back " + hisaabList.get(i).getSplittedAmount());
                        fos.write((hisaabList.get(i).getPaidBy() + " Paid " + hisaabList.get(i).getTotalAmount()).getBytes());
                        fos.write(("\nYou will get back " + hisaabList.get(i).getSplittedAmount() + "\n\n\n").getBytes());
                    } else {
                        Log.i("mamamamam", hisaabList.get(i).getFriendName() + " Paid " + hisaabList.get(i).getTotalAmount());
                        Log.i("mamamamam", hisaabList.get(i).getFriendName() + " will get back " + (hisaabList.get(i).getSplittedAmount() * -1));
                        fos.write((hisaabList.get(i).getFriendName() + " Paid " + hisaabList.get(i).getTotalAmount()).getBytes());
                        fos.write(("\n" + hisaabList.get(i).getFriendName() + " will get back " + (hisaabList.get(i).getSplittedAmount() * -1) + "\n\n\n").getBytes());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.friend_data_modification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_modify_friend) {
            Toast.makeText(this,"action_modify_friend"+id,Toast.LENGTH_LONG).show();
            /*intent =new Intent(this,ModifyFriendDetails.class);
            startActivity(intent);*/
            return true;
        }else if (id == R.id.action_delete_friend){
            Toast.makeText(this,"action_delete_friend"+id,Toast.LENGTH_LONG).show();
            displayError(this,"Are you sure you want to delete this friend");
            /*intent =new Intent(this,MainActivity.class);
            startActivity(intent);*/
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void displayError(Context context, String message) {
        final Dialog dialog = new Dialog(context, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_delete_friend);
        final TextView error = (TextView) dialog.findViewById(R.id.tvErrorMessage);
        final Button ok = (Button) dialog.findViewById(R.id.btnOK);
        final Button cancel = (Button) dialog.findViewById(R.id.btnCancel);
        error.setText(message);

        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("setOnClickListener", "OK");
                dialog.dismiss();
                deleteFriend();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("setOnClickListener","Cancel");
                dialog.dismiss();
            }
        });
    }
    private void deleteFriend() {

        String[] whereArgs = new String[] { friendEmailId };
        String whereClause = " FriendEmailId" + "=?";
        db.delete("FRIENDS", whereClause, whereArgs);
        Intent intent =new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}