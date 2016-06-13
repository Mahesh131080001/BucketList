package in.teachcoder.bucketlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

import in.teachcoder.bucketlist.models.Milestone;

public class Add_Milestone_Activity extends AppCompatActivity {


    EditText title,deadline,details;
    Button savebtn,addanotherbtn;
    Firebase milestoneref,milestone;
    String itemkey,title1,deadline1,details1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__milestone_);
        initialize();
        itemkey = getIntent().getStringExtra("item_id");


savebtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        additem();
    }
});








    }


    void initialize()
    {
        title = (EditText) findViewById(R.id.milestone_ADD_edit_title);
        deadline = (EditText)findViewById(R.id.milestone_ADD_date);
        details = (EditText)findViewById(R.id.milestone_ADD_description);
        savebtn = (Button) findViewById(R.id.milestone_save_button);
        addanotherbtn =(Button)findViewById(R.id._milestone_Add_another_milestone_button);

    }

    void additem()
    {

        title1 = title.getText().toString();
        deadline1 =deadline.getText().toString();
        details1 = details.getText().toString();


        Milestone mlstn = new Milestone(title1,deadline1,details1);

        milestoneref =new Firebase(Constants.FIREBASE_BASE_URL).child(Constants.FIREBASE_LOCATION_BUCKETLIST_MILESTONES).child(itemkey);
        milestone = milestoneref.push();
        milestone.setValue(mlstn);
        finish();
    }

}
