package in.teachcoder.bucketlist.models;

/**
 * Created by Mahesh on 5/22/2016.
 */
public class ITEMs {

    String title,date,priority,details,owner;

    public ITEMs()
    {

    }

    public ITEMs(String title,String date,String priority,String details,String owner)
    {
        this.title=title;
        this.date=date;
        this.priority = priority;
        this.details =details;
        this.owner =owner;


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
