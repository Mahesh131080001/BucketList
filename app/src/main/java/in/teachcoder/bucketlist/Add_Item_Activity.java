package in.teachcoder.bucketlist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

import in.teachcoder.bucketlist.models.BucketCategory;
import in.teachcoder.bucketlist.models.ITEMs;

public class Add_Item_Activity extends AppCompatActivity {


    EditText title, date, priority, details;
    Firebase activeCategoryListRef, itemsRef, itemref,newref;
    SharedPreferences sp;
    String owner, MEncodedEmail, item_key;
    String itemtitle, itemdate, itempriority, itemdetails,status;
    Button savebtn;
    String list_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list_id = getIntent().getStringExtra(Constants.KEY_ID);

        setContentView(R.layout.activity_add__item_);
        initialize();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        MEncodedEmail = sp.getString(Constants.KEY_SECOND_ENCODED_EMAIL, null);
        String comingfrom = getIntent().getStringExtra("coming_from");

        itemsRef = new Firebase(Constants.FIREBASE_ITEMS_URL).child(list_id);

        Firebase newRef = itemsRef.push();

        if (comingfrom.equals("Edit_button")) {

            item_key = getIntent().getStringExtra("item_key");
            itemref = new Firebase(Constants.FIREBASE_ITEMS_URL).child(list_id).child(item_key);
            itemref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ITEMs item = dataSnapshot.getValue(ITEMs.class);
                    title.setText(item.getTitle());
                    date.setText(item.getDate());
                    priority.setText(item.getPriority());
                    details.setText(item.getDetails());
                }
            });
            savebtn.setText("UPDATE");
            savebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String update = "update";
                    additem(update);
                    finish();

                }
            });
        } else {

            savebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ADD = "Add";
                     newref = itemsRef.push();
                    final String itemid = newref.getKey();
                    if(title.getText().toString().equals(""))
                    {
//            Toast.makeText(this, "please provide Title", Toast.LENGTH_SHORT).show();
                        title.setError("Title should not be empty");
                        return;
                    }

                    additem(ADD);

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Add_Item_Activity.this);
                    dialogBuilder.setTitle("WANT TO ADD MILESTONES TO IT");
//                    dialogBuilder.setMessage("Enter your category name");
                    dialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Add_Item_Activity.this,Add_Milestone_Activity.class);
                                  intent.putExtra("item_id",itemid);
                            startActivity(intent);
                            finish();      // send to firebase



                              }
                    });
                    dialogBuilder.setNegativeButton("NO",new DialogInterface.OnClickListener()
                    {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    AlertDialog categoryDialog = dialogBuilder.create();
                    categoryDialog.show();






                }
            });

        }

    }


    void initialize() {
        title = (EditText) findViewById(R.id.ADD_edit_title);
        date = (EditText) findViewById(R.id.ADD_date);
        priority = (EditText) findViewById(R.id.ADD_priority);
        details = (EditText) findViewById(R.id.ADD_description);
        savebtn = (Button) findViewById(R.id.save_button);

    }


    public void additem(String todo) {

        HashMap<String, Object> timeStampCreatedAt = new HashMap<String, Object>();
//        timeStampCreatedAt.put(Constants.FIREBASE_TIMESTAMP_PROPERTY, ServerValue.TIMESTAMP);
        owner = MEncodedEmail;
        itemtitle = title.getText().toString();
        itemdate = date.getText().toString();
        itempriority = priority.getText().toString();
        itemdetails = details.getText().toString();


        if(itemdate.equals(""))
        {
            itemdate="Not Provided";
        }
        if(itempriority.equals(""))
        {
            itempriority="Not provided";

        }
        if(itemdetails.equals(""))
        {
            itempriority = "Not provided";
        }
        status="Incomplete";

        ITEMs newItem = new ITEMs(itemtitle, itemdate, itempriority, itemdetails, owner,status);
//
//        String itemId = newRef.getKey();

        if (todo.equals("update")) {
            itemref = new Firebase(Constants.FIREBASE_ITEMS_URL).child(list_id).child(item_key);

            itemref.setValue(newItem);

        } else {

            activeCategoryListRef = new Firebase(Constants.FIREBASE_CATEGORIES_URL).child(list_id);


            newref.setValue(newItem);
        }


    }


}
