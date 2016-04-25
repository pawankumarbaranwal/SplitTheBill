package pawan.example.com.splitthebill.activity;

import android.content.Context;
import android.content.Intent;
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
 * Created by Pawan on 4/12/2016.
 */
public class RecyclerViewAdapterForFriendHisaab extends RecyclerView.Adapter<RecyclerViewAdapterForFriendHisaab.FriendHisaabViewHolder> {


    private List<Friend> hisaabList = new ArrayList<Friend>();
    Context context;
    public RecyclerViewAdapterForFriendHisaab(List<Friend> hisaabList,Context context) {
        this.context=context;
        this.hisaabList = hisaabList;
    }


    public class FriendHisaabViewHolder extends RecyclerView.ViewHolder{

        private TextView tvItemDescription;
        private TextView tvItemAmount;
        private TextView tvItemPurchaseDate;
        private TextView tvTotalAmountAndPaidBy;

        public FriendHisaabViewHolder(View itemView) {
            super(itemView);
            tvItemDescription = (TextView) itemView.findViewById(R.id.tvItemDescription);
            tvItemAmount = (TextView) itemView.findViewById(R.id.tvItemAmount);
            tvItemPurchaseDate = (TextView) itemView.findViewById(R.id.tvItemPurchaseDate);
            tvTotalAmountAndPaidBy = (TextView) itemView.findViewById(R.id.tvTotalAmountAndPaidBy);
        }
    }


    @Override
    public RecyclerViewAdapterForFriendHisaab.FriendHisaabViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.activity_friend_hisaab_row, viewGroup, false);
        return new FriendHisaabViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterForFriendHisaab.FriendHisaabViewHolder holder, int position) {
        final Friend friend = hisaabList.get(position);
        holder.tvItemDescription.setText(friend.getDescription());
        holder.tvItemPurchaseDate.setText(friend.getSpentDate() + "");
        if (friend.getPaidBy().equals("You")) {
            holder.tvTotalAmountAndPaidBy.setText(friend.getPaidBy() + " Paid " + friend.getTotalAmount());
            holder.tvItemAmount.setText("You will get back " + friend.getSplittedAmount() + "");
        } else {
            holder.tvTotalAmountAndPaidBy.setText(friend.getFriendName() + " Paid " + friend.getTotalAmount());
            holder.tvItemAmount.setText(friend.getFriendName() + " will get back " + (friend.getSplittedAmount() * -1) + "");
        }holder.tvItemDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "" + friend.getFriendEmailId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, FriendHisaab.class);
                intent.putExtra("FriendEmailId",friend.getFriendEmailId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hisaabList.size();
    }
}
