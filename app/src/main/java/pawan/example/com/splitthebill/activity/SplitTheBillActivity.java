package pawan.example.com.splitthebill.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

import pawan.example.com.splitthebill.dto.Friend;

/**
 * Created by Pawan on 29/03/16.
 */
public class SplitTheBillActivity extends AppCompatActivity implements View.OnClickListener {

    private SQLiteDatabase db;
    private Cursor c;
    protected List<Friend> friendList = new ArrayList<>();
    String[] mDataset;
    AutoCompleteTextView tvAutocompleteFriendEmail;
    EditText etDescription;
    EditText etMoney;
    Button btnSplitSubmit;
    TreeSet<String> uniqueName;
    List<String> listEmailId = new ArrayList<String>();
    TextView tvCalculation;

    public SplitTheBillActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();

        setContentView(R.layout.activity_split_the_bill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Split The Bill");

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        tvAutocompleteFriendEmail = (AutoCompleteTextView) findViewById(R.id.tvAutocompleteFriendEmail);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etMoney = (EditText) findViewById(R.id.etMoney);
        tvCalculation = (TextView) findViewById(R.id.tvCalculation);
        btnSplitSubmit = (Button) findViewById(R.id.btnSplitSubmit);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, uniqueName.toArray(new String[uniqueName.size()]));
        tvAutocompleteFriendEmail.setAdapter(adapter);

        tvCalculation.setOnClickListener(this);
        btnSplitSubmit.setOnClickListener(this);

    }

    private void initDataset() {
        Log.d("Coming", "x");
        db = openOrCreateDatabase("SplitTheBill", Context.MODE_PRIVATE, null);
        c = db.rawQuery("SELECT * FROM FRIENDS", null);
        if (c.moveToFirst()) {
            Friend friend = new Friend();

            friend.setFriendName(c.getString(1));
            friend.setFriendEmailId(c.getString(2));
            friend.setDescription(c.getString(3));
            friend.setSplittedAmount(c.getInt(4));
            //friend.setSpentDate((Date)c.getString(3));
            //friend.setSign((Character)c.getString(5));
            friendList.add(friend);
            String name = c.getString(1);
            //mDataset[i]=name;
            while (!c.isLast()) {
                c.moveToNext();
                friend = new Friend();

                friend.setFriendName(c.getString(1));
                friend.setFriendEmailId(c.getString(2));
                friend.setDescription(c.getString(3));
                friend.setSplittedAmount(Integer.parseInt(c.getString(4)));
                //friend.setSpentDate((Date)c.getString(3));
                //friend.setSign((Character)c.getString(5));
                friendList.add(friend);
                name = c.getString(1);
                //  mDataset[i]=name;
            }
        }
        mDataset = new String[friendList.size()];
        for (int j = 0; j < friendList.size(); j++) {
            mDataset[j] = friendList.get(j).getFriendEmailId();
        }
        listEmailId = Arrays.asList(mDataset);
        uniqueName = new TreeSet<String>(listEmailId);

    }

    @Override
    public void onClick(View view) {
        final Context context = this;
        if (view == tvCalculation) {
            final CharSequence[] scienarios = {
                    "Paid by You and Split Equally",
                    "Paid by You and Other Owe the full amount",
                    "Paid by Other and Split Equally",
                    "Paid by Other and You Owe the full amount"};
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Split the Bill");
            builder.setItems(scienarios, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context, "You have selected  " + scienarios[which], Toast.LENGTH_LONG).show();
                    tvCalculation.setText(scienarios[which]);
                }
            });
            builder.setInverseBackgroundForced(true);
            builder.create();
            builder.show();
        }
        if (view == btnSplitSubmit) {
            String friendEmail = tvAutocompleteFriendEmail.getText() + "";
            String description = etDescription.getText() + "";
            String money = etMoney.getText() + "";
            if ((friendEmail.equals("")) || (description.equals("")) || (money.equals(""))) {
                Toast.makeText(this, "Please enter the fields", Toast.LENGTH_LONG).show();
            } else if (!listEmailId.contains(friendEmail)) {
                Toast.makeText(this, "Friend Name is incorrect", Toast.LENGTH_LONG).show();
            } else {
                insertIntoDB(tvCalculation.getText() + "", friendEmail, description, money);

                final Toast toast = Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 500);
                NewMainActivity.ROW = 0;
                Intent intent = new Intent(this, NewMainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    protected void insertIntoDB(String splitScienario, String friendEmail, String description, String money) {
        String query = "";

        String friendName = getFriendName(friendEmail);

        if (splitScienario.equals("Paid by You and Split Equally")) {
            query = "INSERT INTO FRIENDS (FriendName,FriendEmailId,Description,SplittedAmount,TotalAmount,SpentDate,PaidBy) " +
                    "VALUES('" + friendName + "','" + friendEmail + "', '" + description + "', '" + Integer.parseInt(money) / 2 + "', '" + money + "', '" + Calendar.getInstance().getTimeInMillis() + "', '" + "You" + "');";
        } else if (splitScienario.equals("Paid by You and Other Owe the full amount")) {
            query = "INSERT INTO FRIENDS (FriendName,FriendEmailId,Description,SplittedAmount,TotalAmount,SpentDate,PaidBy)" +
                    " VALUES('" + friendName + "','" + friendEmail + "', '" + description + "', '" + money + "', '" + money + "', '" + Calendar.getInstance().getTimeInMillis() + "', '" + "You" + "');";
        } else if (splitScienario.equals("Paid by Other and Split Equally")) {
            query = "INSERT INTO FRIENDS (FriendName,FriendEmailId,Description,SplittedAmount,TotalAmount,SpentDate,PaidBy)" +
                    " VALUES('" + friendName + "','" + friendEmail + "', '" + description + "', '" + "-" + Integer.parseInt(money) / 2 + "', '" + money + "', '" + Calendar.getInstance().getTimeInMillis() + "', '" + friendEmail + "');";
        } else if (splitScienario.equals("Paid by Other and You Owe the full amount")) {
            query = "INSERT INTO FRIENDS (FriendName,FriendEmailId,Description,SplittedAmount,TotalAmount,SpentDate,PaidBy) " +
                    "VALUES('" + friendName + "','" + friendEmail + "', '" + description + "', '" + "-" + Integer.parseInt(money) + "', '" + money + "', '" + Calendar.getInstance().getTimeInMillis() + "', '" + friendEmail + "');";
        } else {
            query = "INSERT INTO FRIENDS (FriendName,FriendEmailId,Description,SplittedAmount,TotalAmount,SpentDate,PaidBy)" +
                    " VALUES('" + friendName + "','" + friendEmail + "', '" + description + "', '" + Integer.parseInt(money) / 2 + "', '" + money + "', '" + Calendar.getInstance().getTimeInMillis() + "', '" + "You" + "');";
        }
        db.execSQL(query);
    }

    private String getFriendName(String friendEmailId) {
        c = db.rawQuery("SELECT * FROM FRIENDS WHERE  FRIENDEMAILID ='" + friendEmailId + "' AND DESCRIPTION IS NULL ", null);
        Log.i("testttttt", "11");
        if (c.moveToFirst()) {
            Log.i("testttttt", "11" + c.getString(1));
            return c.getString(1) + "";

        } else {
            Log.i("testttttt", "11" + c.getString(1));
            return null;
        }
    }

    /**
     * react to the user tapping the back/up icon in the action bar
     */
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
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
            overridePendingTransition(R.anim.shifttobottom_enter, R.anim.shifttobottom_exit);
        } else {
            getFragmentManager().popBackStack();
        }
    }
}

