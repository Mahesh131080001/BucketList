package in.teachcoder.bucketlist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

import in.teachcoder.bucketlist.models.BucketCategory;
import in.teachcoder.bucketlist.models.BucketCategoryItem;
import in.teachcoder.bucketlist.models.ITEMs;

public class DetailActivity extends AppCompatActivity {
    ListView itemsList;
    FloatingActionButton addBucketItem;
    Firebase activeCategoryListRef, itemsRef;
    String owner,listId,user;
    ValueEventListener eventListener;
//    CategoriesListAdapter adapter;
    ItemListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initializeViews();
        Firebase.setAndroidContext(this);
         listId = getIntent().getStringExtra(Constants.KEY_ID);
         user = getIntent().getStringExtra("user");

        activeCategoryListRef = new Firebase(Constants.FIREBASE_CATEGORIES_URL).child(listId);
        itemsRef = new Firebase(Constants.FIREBASE_ITEMS_URL).child(listId);
//        adapter = new CategoriesListAdapter(this, BucketCategory.class, itemsRef);
          adapter = new ItemListAdapter(this, ITEMs.class,itemsRef);
        eventListener = activeCategoryListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BucketCategory category = dataSnapshot.getValue(BucketCategory.class);
                if (category != null && category.getOwner().equals(user))
                    setTitle(category.getTitle());
            }




            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        addBucketItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDialog();

                Intent i = new Intent(DetailActivity.this,Add_Item_Activity.class);
                i.putExtra(Constants.KEY_ID,listId );
                i.putExtra("coming_from","DetailActivity");


                startActivity(i);
            }
        });

        itemsList.setAdapter(adapter);

        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String key =   adapter.getRef(position).getKey();
             Intent i =new Intent(DetailActivity.this,Item_Details_Activity.class);
                i.putExtra(Constants.KEY_ID,listId );
                i.putExtra("Item_key",key);
             startActivity(i);



            }
        });

    }

    public void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_category_dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText itemName = (EditText) dialogView.findViewById(R.id.category_name_input);

        dialogBuilder.setTitle("Add a item");
        dialogBuilder.setMessage("Enter your Item name");
        dialogBuilder.setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send to firebase
                String item = itemName.getText().toString();
                owner = user;
                Firebase newRef = itemsRef.push();
                HashMap<String, Object> timeStampCreatedAt = new HashMap<String, Object>();
                timeStampCreatedAt.put(Constants.FIREBASE_TIMESTAMP_PROPERTY, ServerValue.TIMESTAMP);
                BucketCategoryItem newCategory = new BucketCategoryItem(owner, item, timeStampCreatedAt);

                String itemId = newRef.getKey();
                newRef.setValue(newCategory);
            }
        });

        AlertDialog categoryDialog = dialogBuilder.create();
        categoryDialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activeCategoryListRef.removeEventListener(eventListener);
    }


    public void initializeViews() {
        addBucketItem = (FloatingActionButton) findViewById(R.id.addbucketItemBtn);
        itemsList = (ListView) findViewById(R.id.bucketItemsList);
    }
}
