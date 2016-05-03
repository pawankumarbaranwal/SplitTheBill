package pawan.example.com.splitthebill.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Pawan on 29/03/16.
 */
public class ExportAndEmailFragment extends Fragment implements View.OnClickListener {

    private SQLiteDatabase db;
    private Cursor c;
    Button btnExport;
    Button btnEmail;
    AutoCompleteTextView actvFriendsName;
    List<String> listFriendName;

    public ExportAndEmailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_export_email, container, false);

        actvFriendsName = (AutoCompleteTextView) rootView.findViewById(R.id.actvFriendsName);
        btnExport = (Button) rootView.findViewById(R.id.btnExport);
        btnEmail = (Button) rootView.findViewById(R.id.btnEmail);
        listFriendName = retieveFriendName();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listFriendName.toArray(new String[listFriendName.size()]));
        actvFriendsName.setAdapter(adapter);

        btnExport.setOnClickListener(this);
        btnEmail.setOnClickListener(this);

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
        db = getActivity().openOrCreateDatabase("SplitTheBill", Context.MODE_PRIVATE, null);
        c = db.rawQuery("SELECT * FROM FRIENDS", null);

    }

    @Override
    public void onClick(View view) {
        if (view == btnExport) {

            for (int i = 0; i < listFriendName.size(); i++) {
                saveAsText(listFriendName.get(i));
            }
        }
        if (view == btnEmail) {
            if ((actvFriendsName.getText() + "").equals("")){
                Toast.makeText(getActivity(),"Please Enter Friend Name",Toast.LENGTH_LONG).show();
                return;
            }else if (!listFriendName.contains(actvFriendsName.getText() + "")){
                Toast.makeText(getActivity(),"Please Enter Correct Friend Name",Toast.LENGTH_LONG).show();
                return;
            }
            saveAsText(actvFriendsName.getText() + "");
            Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            emailIntent.setType("text/plain");
/*
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                    new String[]{"pawankumarbaranwal@gmail.com"});
            emailIntent.putExtra(android.content.Intent.EXTRA_CC,
                    new String[]{"pawankumarbaranwal@gmail.com"});
*/
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Details of "+actvFriendsName.getText()+"");
            //emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            //has to be an ArrayList
            ArrayList<Uri> uris = new ArrayList<Uri>();
            //convert from paths to Android friendly Parcelable Uri's

            File fileIn = new File("/sdcard/SplitTheBill/"+actvFriendsName.getText()+".txt");
            Uri u = Uri.fromFile(fileIn);
            uris.add(u);
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }
    }

    protected List<String> retieveFriendName() {
        List<String> listFriendName = new ArrayList<String>();
        c = db.rawQuery("SELECT DISTINCT(FriendName) FROM FRIENDS", null);

        if (c.moveToFirst()) {
            listFriendName.add(c.getString(0));
            while (!c.isLast()) {
                c.moveToNext();
                listFriendName.add(c.getString(0));
            }
        }
        return listFriendName;
    }

    public void saveAsText(String friendName) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy : hh/mm/ss");

        try {
            FileOutputStream fos = new FileOutputStream("/sdcard/SplitTheBill/" + friendName + ".txt");
            c = db.rawQuery("SELECT * FROM FRIENDS WHERE  FRIENDNAME ='" + friendName + "' AND DESCRIPTION IS NOT NULL ", null);

            if (c.moveToFirst()) {
                c.getString(3);
                long milliSeconds = Long.parseLong(c.getString(6));
                //try {
                if (c.getString(7).equals("You")) {
                    Log.i("mamamamam", c.getString(7) + " Paid " + Integer.parseInt(c.getString(5)));
                    Log.i("mamamamam", "You will get back " + Integer.parseInt(c.getString(4)));
                    fos.write((c.getString(7) + " Paid " + Integer.parseInt(c.getString(5))).getBytes());

                    fos.write(("\nYou will get back " + Integer.parseInt(c.getString(4)) + "\n\n\n").getBytes());

                } else {/*
                        Log.i("mamamamam", hisaabList.get(i).getFriendName() + " Paid " + hisaabList.get(i).getTotalAmount());
                        Log.i("mamamamam", hisaabList.get(i).getFriendName() + " will get back " + (hisaabList.get(i).getSplittedAmount() * -1));*/
                    fos.write((c.getString(1) + " Paid " + Integer.parseInt(c.getString(5))).getBytes());
                    fos.write(("\n" + c.getString(1) + " will get back " + (Integer.parseInt(c.getString(4)) * -1) + "\n\n\n").getBytes());
                }
            }
            while (c.moveToNext()) {

                formatter = new SimpleDateFormat("dd/MM/yyyy : hh/mm/ss");
                c.getString(5);

                if (c.getString(7).equals("You")) {
                    Log.i("mamamamam", c.getString(7) + " Paid " + Integer.parseInt(c.getString(5)));
                    Log.i("mamamamam", "You will get back " + Integer.parseInt(c.getString(4)));
                    fos.write((c.getString(7) + " Paid " + Integer.parseInt(c.getString(5))).getBytes());
                    fos.write(("\nYou will get back " + Integer.parseInt(c.getString(4)) + "\n\n\n").getBytes());
                } else {/*
                        Log.i("mamamamam", hisaabList.get(i).getFriendName() + " Paid " + hisaabList.get(i).getTotalAmount());
                        Log.i("mamamamam", hisaabList.get(i).getFriendName() + " will get back " + (hisaabList.get(i).getSplittedAmount() * -1));*/
                    fos.write((c.getString(1) + " Paid " + Integer.parseInt(c.getString(5))).getBytes());
                    fos.write(("\n" + c.getString(1) + " will get back " + (Integer.parseInt(c.getString(4)) * -1) + "\n\n\n").getBytes());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


