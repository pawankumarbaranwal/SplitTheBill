package pawan.example.com.splitthebill.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pawan.example.com.splitthebill.dto.Friend;

/**
 * Created by Pawan on 4/27/2016.
 */
public class RecyclerViewAdapterForActivities extends RecyclerView.Adapter<RecyclerViewAdapterForActivities.ActivitiesViewHolder> {

    private List<Friend> activityList = new ArrayList<Friend>();
    Context context;

    public RecyclerViewAdapterForActivities(List<Friend> activityList, Context context) {
        this.context = context;
        this.activityList = activityList;
    }

    public class ActivitiesViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItemDescription;
        private TextView tvItemPurchaseDate;
        private TextView tvTotalAmountAndDescription;

        public ActivitiesViewHolder(View itemView) {
            super(itemView);
            tvItemDescription = (TextView) itemView.findViewById(R.id.tvItemDescription);
            tvItemPurchaseDate = (TextView) itemView.findViewById(R.id.tvItemPurchaseDate);
            tvTotalAmountAndDescription = (TextView) itemView.findViewById(R.id.tvTotalAmountAndDescription);
        }
    }

    @Override
    public RecyclerViewAdapterForActivities.ActivitiesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.row_activities, viewGroup, false);
        return new ActivitiesViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RecyclerViewAdapterForActivities.ActivitiesViewHolder holder, int position) {
        final Friend friend = activityList.get(position);
        holder.tvItemDescription.setText(friend.getDescription());
        holder.tvItemPurchaseDate.setText(friend.getSpentDate() + "");
        if (friend.getPaidBy().equals("You")) {
            holder.tvTotalAmountAndDescription.setText(friend.getPaidBy() + " Paid " + friend.getTotalAmount()+ " and You will get back " + friend.getSplittedAmount() + "");
        } else {
            holder.tvTotalAmountAndDescription.setText(friend.getFriendName() + " Paid " + friend.getTotalAmount()+  " and You owe " + (friend.getSplittedAmount()*-1) + "");
        }
      /*  holder.tvItemDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "" + friend.getFriendEmailId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, FriendHisaab.class);
                intent.putExtra("FriendEmailId", friend.getFriendEmailId());
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }
}
