package pawan.example.com.splitthebill.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import pawan.example.com.splitthebill.dto.Friend;

public class FriendHisaab extends Activity implements View.OnClickListener {

    private EditText etFriendName;
    private EditText etFriendEmailId;
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

        RecyclerViewAdapterForFriendHisaab adapter = new RecyclerViewAdapterForFriendHisaab(hisaabList);
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

        //setSupportActionBar(mToolbar);

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
        if (c.moveToFirst()) {

            SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            SimpleDateFormat outputDate = new SimpleDateFormat("dd-MMM-yyyy");

            Log.i("pppppppp", c.getString(1) + "\t" + c.getString(2) + "\t" + c.getString(3) + "\t" + c.getString(4) + "\t" + c.getString(5));
            friend.setFriendName(c.getString(1) + "");
            friend.setFriendEmailId(c.getString(2) + "");
            friend.setDescription(c.getString(3) + "");
            friend.setSplittedAmount(Integer.parseInt(c.getString(4)));
            friend.setTotalAmount(Integer.parseInt(c.getString(5)));
            friend.setPaidBy(c.getString(7));
            try {
                Date parsedDate = sdf.parse(c.getString(6));
                friend.setSpentDate(outputDate.format(parsedDate));
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
                try {
                    Date parsedDate = sdf.parse(c.getString(6));
                    friend.setSpentDate(outputDate.format(parsedDate));
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

   /* private void sendMail(String email, String subject, String messageBody) {

        String to = "pawankumarbaranwal@gmail.com";//change accordingly

        //Get the session object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("pawankumar910@gmail.com", "ddcdsfs");//change accordingly
                    }
                });

        //compose message
        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("pawankumar910@gmail.com"));//change accordingly
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Hello");
            message.setText("No Testing.......");

            //send message
            Transport.send(message);

            System.out.println("message sent successfully");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }*/
}
