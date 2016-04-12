package pawan.example.com.splitthebill.activity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import pawan.example.com.splitthebill.dto.Friend;

/**
 * Created by Pawan on 29/03/16.
 */
public class SplitTheBillFragment extends Fragment implements View.OnClickListener{

    private SQLiteDatabase db;
    private Cursor c;
    protected List<Friend> friendList=new ArrayList<>();
    String[] mDataset;
    AutoCompleteTextView tvAutocompleteFriendEmail;
    EditText etDescription;
    EditText etMoney;
    Button btnSplitSubmit;
    TreeSet<String> uniqueName;
    List<String> listEmailId=new ArrayList<String>();
    public SplitTheBillFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_split, container, false);
        tvAutocompleteFriendEmail = (AutoCompleteTextView) rootView.findViewById(R.id.tvAutocompleteFriendEmail);
        etDescription = (EditText) rootView.findViewById(R.id.etDescription);
        etMoney = (EditText) rootView.findViewById(R.id.etMoney);
        btnSplitSubmit = (Button) rootView.findViewById(R.id.btnSplitSubmit);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, uniqueName.toArray(new String[uniqueName.size()]));
        tvAutocompleteFriendEmail.setAdapter(adapter);

        btnSplitSubmit.setOnClickListener(this);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    private void initDataset() {
        Log.d("Coming", "x");
        db=getActivity().openOrCreateDatabase("SplitTheBill", Context.MODE_PRIVATE, null);
        c = db.rawQuery("SELECT * FROM FRIENDS", null);
        if (c.moveToFirst()) {
            Friend friend = new Friend();

            friend.setFriendName(c.getString(1));
            friend.setFriendEmailId(c.getString(2));
            friend.setDescription(c.getString(3));
            friend.setAmount(c.getInt(4));
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
                friend.setAmount(Integer.parseInt(c.getString(4)));
                //friend.setSpentDate((Date)c.getString(3));
                //friend.setSign((Character)c.getString(5));
                friendList.add(friend);
                name = c.getString(1);
                //  mDataset[i]=name;
            }
        }
        mDataset=new String[friendList.size()];
        for (int j=0;j<friendList.size();j++){
            mDataset[j]=friendList.get(j).getFriendEmailId();
        }
        listEmailId = Arrays.asList(mDataset);
        uniqueName=new TreeSet<String>(listEmailId);

    }

    @Override
    public void onClick(View v) {
        insertIntoDB();
    }
    protected void insertIntoDB() {
        Date date = Calendar.getInstance().getTime();
        String friendEmail = tvAutocompleteFriendEmail.getText() + "";
        String description = etDescription.getText() + "";
        String money = etMoney.getText() + "";
        if(friendEmail.equals("") || description.equals("")|| money.equals("")){
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_LONG).show();
        }else if (!listEmailId.contains(friendEmail)){
            Toast.makeText(getActivity(), "Friend Name is incorrect", Toast.LENGTH_LONG).show();
        }else {
            String query = "INSERT INTO FRIENDS (FriendName,FriendEmailId,Description,Amount,SpentDate,Sign) VALUES('" + "','" + friendEmail + "', '" + description + "', '" + money + "', '" + Calendar.getInstance().getTime() + "', '" + "-" + "');";
            db.execSQL(query);
            Toast.makeText(getActivity(), "Saved Successfully", Toast.LENGTH_LONG).show();
        }
    }
}