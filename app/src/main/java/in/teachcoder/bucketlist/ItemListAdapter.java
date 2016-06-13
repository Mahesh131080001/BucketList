package in.teachcoder.bucketlist;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import in.teachcoder.bucketlist.models.BucketCategory;
import in.teachcoder.bucketlist.models.ITEMs;

/**
 * Created by Mahesh on 5/23/2016.
 */
public class ItemListAdapter  extends  FirebaseListAdapter<ITEMs> {
    public ItemListAdapter(Activity activity, Class<ITEMs> modelClass, Firebase ref) {
        super(activity, modelClass, android.R.layout.simple_list_item_1, ref);
    }


    protected void populateView(View v, ITEMs model) {
        TextView title = (TextView) v.findViewById(android.R.id.text1);
        title.setText(model.getTitle());
    }

}

