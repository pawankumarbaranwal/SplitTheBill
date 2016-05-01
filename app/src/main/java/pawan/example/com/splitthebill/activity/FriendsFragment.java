package pawan.example.com.splitthebill.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pawan.example.com.splitthebill.dto.Friend;


/**
 * Created by Pawan on 29/05/16.
 */
public class FriendsFragment extends Fragment implements View.OnClickListener {


    private FloatingActionButton fab;
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
    private SQLiteDatabase db;
    private Cursor c;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;
    protected List<Friend> friendList = new ArrayList<>();
    protected List<Friend> finalFriendList = new ArrayList<>();

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new CustomAdapter(finalFriendList, getActivity());
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);

        // Inflate the layout for this fragment
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddFriend.class);
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getActivity(), R.anim.shifttotop_enter, R.anim.shifttotop_exit).toBundle();
                getActivity().startActivity(intent, bndlanimation);

            }
        });
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

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "SHOW", Toast.LENGTH_SHORT).show();
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void initDataset() {
        Log.d("Coming", "x");
        int i = 0;
        String name;
        Friend friend = new Friend();
        mDataset = new String[DATASET_COUNT];
        db = getActivity().openOrCreateDatabase("SplitTheBill", Context.MODE_PRIVATE, null);
        c = db.rawQuery("SELECT * FROM FRIENDS", null);
        if (c.moveToFirst()) {
            Log.i("pppppppp", c.getString(1) + "\t" + c.getString(2) + "\t" + c.getString(3) + "\t" + c.getString(4) + "\t" + c.getString(5));
                friend.setFriendName(c.getString(1) + "");
                friend.setFriendEmailId(c.getString(2) + "");
                friend.setDescription(c.getString(3) + "");
                friend.setSplittedAmount(Integer.parseInt(c.getString(4)));
                //friend.setSpentDate((Date)c.getString(3));
                //friend.setSign((Character)c.getString(5));
                friendList.add(friend);
                name = c.getString(1);
                mDataset[i] = name;
            while (!c.isLast()) {
                i++;
                c.moveToNext();
                friend = new Friend();
                    friend.setFriendName(c.getString(1));
                    friend.setFriendEmailId(c.getString(2));
                    friend.setDescription(c.getString(3));
                    friend.setSplittedAmount(Integer.parseInt(c.getString(4)));
                    //friend.setSpentDate((Date)c.getString(3));
                    //friend.setSign((Character)c.getString(5));
                    friendList.add(friend);
                    name = c.getString(2);
                    mDataset[i] = name;
            }

            for (int x=0;x<friendList.size();x++){
                getTotalAmount(friendList.get(x).getFriendEmailId());
            }
            Set<Friend> s= new HashSet<Friend>();
            s.addAll(finalFriendList);
            finalFriendList = new ArrayList<Friend>();
            finalFriendList.addAll(s);
        }
    }
    public void getTotalAmount(String emailId) {
        int sum = 0;
        Friend friend =new Friend();
        for (int i = 0; i < friendList.size(); i++) {
            if (friendList.get(i).getFriendEmailId().equals(emailId)) {
                sum = sum + friendList.get(i).getSplittedAmount();
            }
        }
        friend.setFriendEmailId(emailId);
        friend.setSplittedAmount(sum);
        finalFriendList.add(friend);
        //return sum;
    }
    /*@Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
            overridePendingTransition(R.anim.shifttoright_enter, R.anim.shifttoright_exit);
        }
        else {
            getFragmentManager().popBackStack();
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
