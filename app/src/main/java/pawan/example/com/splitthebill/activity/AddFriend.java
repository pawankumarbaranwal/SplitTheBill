/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package pawan.example.com.splitthebill.activity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/*
 * MainActivity class that loads MainFragment
 */
public class AddFriend extends Activity implements View.OnClickListener{

    EditText etFriendName;
    EditText etFriendEmailId;
    Button submit;
    private SQLiteDatabase db;
    private Cursor c;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        etFriendName=(EditText)findViewById(R.id.etName);
        etFriendEmailId=(EditText)findViewById(R.id.etEmail);
        submit=(Button)findViewById(R.id.btnSubmitFriendName);
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
        db.execSQL("CREATE TABLE IF NOT EXISTS FRIENDS (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
                "FriendName VARCHAR, " +
                "FriendEmailId VARCHAR," +
                "Amount VARCHAR, " +
                "SpentDate DATE," +
                "Sign VARCHAR);");
    }
    protected void insertIntoDB(){
        Date date=Calendar.getInstance().getTime();
        String friendName = etFriendName.getText().toString();
        String friendEmailId = etFriendEmailId.getText().toString();
        if(friendName.equals("") || friendEmailId.equals("")){
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
        }
        String query = "INSERT INTO FRIENDS (FriendName,FriendEmailId,Amount,SpentDate,Sign) VALUES('"+friendName+"', '"+friendEmailId+"', '"+"100"+"', '"+ Calendar.getInstance().getTime()+"', '"+"-"+"');";
        db.execSQL(query);
        Toast.makeText(getApplicationContext(),"Saved Successfully", Toast.LENGTH_LONG).show();

        c = db.rawQuery("SELECT * FROM FRIENDS", null);
        c.moveToFirst();
        showRecords();
    }
    protected void showRecords() {
        String id = c.getString(0);
        String friendName = c.getString(1);
        String friendEmailId= c.getString(2);
        Log.i("Test",id+"\t"+friendName+"\t"+friendEmailId);
    }

}
