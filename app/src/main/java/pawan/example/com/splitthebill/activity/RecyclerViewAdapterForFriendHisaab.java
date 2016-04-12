package pawan.example.com.splitthebill.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pawan.example.com.splitthebill.dto.Friend;

/**
 * Created by Pawan on 4/12/2016.
 */
public class RecyclerViewAdapterForFriendHisaab extends RecyclerView.Adapter<RecyclerViewAdapterForFriendHisaab.FriendHisaabViewHolder> {



    private List<Friend> hisaabList=new ArrayList<Friend>();

    public RecyclerViewAdapterForFriendHisaab(List<Friend> hisaabList) {
        this.hisaabList = hisaabList;
    }

    @Override
    public int getItemCount() {
        return hisaabList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterForFriendHisaab.FriendHisaabViewHolder holder, int position) {
        Friend friend= hisaabList.get(position);
        holder.tvItemDescription.setText(friend.getDescription());
        holder.tvItemAmount.setText(friend.getAmount()+"");
        holder.tvItemPurchaseDate.setText(friend.getSpentDate()+"");
    }
    @Override
    public RecyclerViewAdapterForFriendHisaab.FriendHisaabViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.activity_friend_hisaab_row, viewGroup, false);
        return new FriendHisaabViewHolder(itemView);
    }



    public static class FriendHisaabViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItemDescription;
        private TextView tvItemAmount;
        private TextView tvItemPurchaseDate;

        public FriendHisaabViewHolder(View itemView) {
            super(itemView);
            tvItemDescription=(TextView)itemView.findViewById(R.id.tvItemDescription);
            tvItemAmount=(TextView)itemView.findViewById(R.id.tvItemAmount);
            tvItemPurchaseDate=(TextView)itemView.findViewById(R.id.tvItemPurchaseDate);
        }
    }
}
