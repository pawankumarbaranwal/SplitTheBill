package pawan.example.com.splitthebill.dto;

/**
 * Created by Pawan on 4/9/2016.
 */
public class Friend {

    private String friendName;
    private String friendEmailId;
    private String description;
    private Integer amount;
    private String spentDate;
    private Character sign;

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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getSpentDate() {
        return spentDate;
    }

    public void setSpentDate(String spentDate) {
        this.spentDate = spentDate;
    }

    public Character getSign() {
        return sign;
    }

    public void setSign(Character sign) {
        this.sign = sign;
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
