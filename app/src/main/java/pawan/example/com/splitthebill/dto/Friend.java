package pawan.example.com.splitthebill.dto;

/**
 * Created by Pawan on 4/9/2016.
 */
public class Friend {

    private String friendName;
    private String friendEmailId;
    private String description;
    private Integer totalAmount;
    private Integer splittedAmount;
    private String spentDate;
    private String paidBy;

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendEmailId() {
        return friendEmailId;
    }

    public void setFriendEmailId(String friendEmailId) {
        this.friendEmailId = friendEmailId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getSplittedAmount() {
        return splittedAmount;
    }

    public void setSplittedAmount(Integer splittedAmount) {
        this.splittedAmount = splittedAmount;
    }

    public String getSpentDate() {
        return spentDate;
    }

    public void setSpentDate(String spentDate) {
        this.spentDate = spentDate;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if(obj instanceof Friend)
        {
            Friend temp = (Friend) obj;
            if(this.friendEmailId .equals(temp.friendEmailId))
                return true;
        }
        return false;

    }
    @Override
    public int hashCode() {
        // TODO Auto-generated method stub

        return (this.friendEmailId.hashCode());
    }
}
