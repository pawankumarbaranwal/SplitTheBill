package pawan.example.com.splitthebill.activity;

/**
 * Created by Pawan on 29/07/15.
 */


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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pawan.example.com.splitthebill.dto.Friend;


public class ActivitiesFragment extends Fragment {

    private SQLiteDatabase db;
    private Cursor c;
    private List<Friend> activityList = new ArrayList<Friend>();
    RecyclerView mRecyclerView;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_activities, container, false);
        FloatingActionButton fabNewActivity = (FloatingActionButton) rootView.findViewById(R.id.fabNewActivity);

        activityList = getData();


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewForActivites);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        if (savedInstanceState != null) {
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }

        RecyclerViewAdapterForActivities mAdapter = new RecyclerViewAdapterForActivities(activityList, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        fabNewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SplitTheBillActivity.class);
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getActivity(), R.anim.shifttotop_enter, R.anim.shifttotop_exit).toBundle();
                getActivity().startActivity(intent, bndlanimation);

            }
        });
        return rootView;
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
                mLayoutManager = new GridLayoutManager(getActivity(), 2);
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

    public List<Friend> getData() {
        Friend friend = new Friend();
        db = getActivity().openOrCreateDatabase("SplitTheBill", Context.MODE_PRIVATE, null);
        c = db.rawQuery("SELECT * FROM FRIENDS WHERE DESCRIPTION IS NOT NULL ORDER BY SPENTDATE DESC", null);
        if (c.moveToFirst()) {
            DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");// \n hh:mm:ss");

            long milliSeconds = Long.parseLong(c.getString(6));
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

            activityList.add(friend);
            while (!c.isLast()) {
                c.moveToNext();
                friend = new Friend();
                friend.setFriendName(c.getString(1));
                friend.setFriendEmailId(c.getString(2));
                friend.setDescription(c.getString(3));
                friend.setSplittedAmount(Integer.parseInt(c.getString(4)));
                friend.setTotalAmount(Integer.parseInt(c.getString(5)));
                friend.setPaidBy(c.getString(7));
                milliSeconds = Long.parseLong(c.getString(6));
                calendar.setTimeInMillis(milliSeconds);
                try {
                    friend.setSpentDate(formatter.format(calendar.getTime()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                activityList.add(friend);
            }
        }
        return activityList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}