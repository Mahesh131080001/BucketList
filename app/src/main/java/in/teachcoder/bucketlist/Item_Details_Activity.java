package in.teachcoder.bucketlist;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

import in.teachcoder.bucketlist.models.BucketCategory;
import in.teachcoder.bucketlist.models.ITEMs;
import in.teachcoder.bucketlist.models.Milestone;

public class Item_Details_Activity extends AppCompatActivity {



    TextView title,due_date,priority,details,status;
    ListView listView;
    MilestoneAdapter adapter;
//    String detail_title,detaildate,detailpriority,detaildetails,key;
    Firebase itemref,milestoneref;
    String item_key,list_id,mil_key;
    Button compltBtn;


    TextView mtitle,mdeadline,mdetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item__details_);
        InitializeView();
        item_key = getIntent().getStringExtra("Item_key");
         list_id = getIntent().getStringExtra(Constants.KEY_ID);
         milestoneref = new Firebase(Constants.FIREBASE_BASE_URL).child(Constants.FIREBASE_LOCATION_BUCKETLIST_MILESTONES).child(item_key);
        itemref = new Firebase(Constants.FIREBASE_ITEMS_URL).child(list_id).child(item_key);

         adapter = new MilestoneAdapter(Item_Details_Activity.this, Milestone.class,milestoneref);
        listView.setAdapter(adapter);


    compltBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            itemref.child("status").setValue("Completed");
        }
    });

//        itemref.child("status").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                ITEMs item =  dataSnapshot.getValue(ITEMs.class);
//                title.setText(item.getTitle());
//                due_date.setText(item.getDate());
//                priority.setText(item.getPriority());
//                details.setText(item.getDetails());
//                status.setText(item.getStatus());
//
//            });

            itemref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ITEMs item =  dataSnapshot.getValue(ITEMs.class);
                title.setText(item.getTitle());
                due_date.setText(item.getDate());
                priority.setText(item.getPriority());
                details.setText(item.getDetails());
                status.setText(item.getStatus());

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        listlistener();
    }




public void InitializeView()
{
    title = (TextView) findViewById(R.id.Title_text);
    due_date = (TextView) findViewById(R.id.date);
    priority = (TextView) findViewById(R.id.priority);
    details = (TextView) findViewById(R.id.description);
    listView = (ListView)findViewById(R.id.list_milestones);
status = (TextView)findViewById(R.id.item_details_status);
compltBtn=(Button)findViewById(R.id.complete_button);

}




    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_delete_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id)
        {
            case R.id.EDit_button:
               Intent i = new Intent(Item_Details_Activity.this,Add_Item_Activity.class);
                i.putExtra("coming_from","Edit_button");
                i.putExtra(Constants.KEY_ID,list_id);
                i.putExtra("item_key",item_key);
                startActivity(i);

                return true;

            case R.id.delete_button:
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();

                dialogBuilder.setTitle("Delete This Entry");
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                          itemref.removeValue();
                        finish();

                        // send to firebase
                    }
                });

                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                AlertDialog categoryDialog = dialogBuilder.create();
                categoryDialog.show();


                return true;

        }

//        Intent i = new Intent(MainActivity.this,Login_Activity.class);
        return true;
        //        startActivity(i);

    }


    public void listlistener()
    {
           listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    mil_key = adapter.getRef(position).getKey();
//

showDialog();

               }
           });
    }


    public void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.milestone_details, null);
        dialogBuilder.setView(dialogView);

        Firebase mileref = new Firebase(Constants.FIREBASE_BASE_URL).child(item_key).child(mil_key);
        mtitle = (TextView)findViewById(R.id.milestone_detail_title);
        mdeadline = (TextView) findViewById(R.id.milestone_deadline);
        mdetails = (TextView) findViewById(R.id.milestone_details);


        mileref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Milestone ML = dataSnapshot.getValue(Milestone.class);
                mtitle.setText(ML.getTitle());
                mdeadline.setText(ML.getDeadline());
                mdetails.setText(ML.getDetails());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


//          dialogBuilder.setTitle("Add a Category");
//        dialogBuilder.setMessage("Enter your category name");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send to firebase




            }
        });

        AlertDialog categoryDialog = dialogBuilder.create();
        categoryDialog.show();

    }


}
