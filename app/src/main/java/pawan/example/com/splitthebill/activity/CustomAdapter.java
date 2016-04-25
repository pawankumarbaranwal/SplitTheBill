package pawan.example.com.splitthebill.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pawan.example.com.splitthebill.dto.Friend;

/**
 * Created by Pawan on 4/9/2016.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private List<Friend> friendList = new ArrayList<>();
    Context context;


    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvFriendName;
        private final TextView tvTotalAmount;
        private final RelativeLayout rlElement;

        public ViewHolder(View v) {
            super(v);
            tvFriendName = (TextView) v.findViewById(R.id.tvFriendName);
            tvTotalAmount = (TextView) v.findViewById(R.id.tvTotalAmount);
            rlElement = (RelativeLayout) v.findViewById(R.id.rlElement);
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     */
    public CustomAdapter(List<Friend> friendList, Context context) {
        this.friendList = friendList;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.tvFriendName.setText(friendList.get(position).getFriendEmailId());
        //getTotalAmount(friendList.get(position).getFriendEmailId());
        //viewHolder.tvTotalAmount.setText(getTotalAmount(friendList.get(position).getFriendEmailId()) + "");
        viewHolder.tvTotalAmount.setText(friendList.get(position).getSplittedAmount() + "");
        viewHolder.rlElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "" + friendList.get(position).getFriendEmailId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, FriendHisaab.class);
                intent.putExtra("FriendEmailId",friendList.get(position).getFriendEmailId());

                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(context, R.anim.animation, R.anim.animation2).toBundle();
                context.startActivity(intent, bndlanimation);
                //context.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return friendList.size();
        //return 0;
    }

    public Integer getTotalAmount(String emailId) {
        int sum = 0;
        for (int i = 0; i < friendList.size(); i++) {
            if (friendList.get(i).getFriendEmailId().equals(emailId)) {
                sum = sum + friendList.get(i).getSplittedAmount();
            }
        }
        return sum;
    }
}
