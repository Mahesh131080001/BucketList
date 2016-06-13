package in.teachcoder.bucketlist.models;

/**
 * Created by Mahesh on 6/13/2016.
 */
public class Milestone {
    String title,deadline,details;

public Milestone()
{

}
    public  Milestone(String title,String deadline,String details)
    {
        this.title = title;
        this.deadline = deadline;
        this.details = details;


    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
