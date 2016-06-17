package in.teachcoder.bucketlist;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import in.teachcoder.bucketlist.models.ITEMs;
import in.teachcoder.bucketlist.models.Milestone;

/**
 * Created by Mahesh on 6/14/2016.
 */
public class MilestoneAdapter extends FirebaseListAdapter<Milestone> {


    public MilestoneAdapter(Activity activity, Class<Milestone> modelClass,  Firebase ref) {
        super(activity, modelClass,android.R.layout.simple_list_item_1, ref);
    }




    protected void populateView(View v, Milestone model) {
        TextView title = (TextView) v.findViewById(android.R.id.text1);
        title.setText(model.getTitle());
    }
}
