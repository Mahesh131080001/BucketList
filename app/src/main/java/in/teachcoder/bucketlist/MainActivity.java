package in.teachcoder.bucketlist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Objects;

import in.teachcoder.bucketlist.models.BucketCategory;
import in.teachcoder.bucketlist.models.Users;

public class MainActivity extends AppCompatActivity {
    ListView categoriesList;
    FloatingActionButton addCategory;
    Firebase firebaseRef, categoriesRef;
    CategoriesListAdapter adapter;
    String owner;
    SharedPreferences sp;
    String mProvider,MEncodedEmail;
    private ValueEventListener mUserRefListener;
    Firebase usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addCategory = (FloatingActionButton) findViewById(R.id.addCategoryBtn);
        categoriesList = (ListView) findViewById(R.id.bucketCategoriesList);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        MEncodedEmail = sp.getString(Constants.KEY_SECOND_ENCODED_EMAIL,null);
        mProvider = sp.getString(Constants.KEY_PROVIDER,null);

        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase(Constants.FIREBASE_BASE_URL);
        categoriesRef = new Firebase(Constants.FIREBASE_CATEGORIES_URL);
        usersRef = new Firebase(Constants.FIREBASE_USER_URL).child(MEncodedEmail);
        Log.d("name= " ,usersRef.toString());
        adapter = new CategoriesListAdapter(this, BucketCategory.class, categoriesRef);
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        categoriesList.setAdapter(adapter);
        mUserRefListener = usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Users user  = dataSnapshot.getValue(Users.class);
                if(user!=null)
                {
                    String firstName = user.getUsername().split("\\s+")[0];
                    Log.d("name= " ,firstName);
                    String title = firstName+"'s Lists";
                    setTitle(title);
                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        listListener();
    }

    public void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_category_dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText categoryName = (EditText) dialogView.findViewById(R.id.category_name_input);

        dialogBuilder.setTitle("Add a Category");
        dialogBuilder.setMessage("Enter your category name");
        dialogBuilder.setPositiveButton("Add Category", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send to firebase
                String category = categoryName.getText().toString();
                owner = MEncodedEmail;
                Firebase newRef = categoriesRef.push();
                HashMap<String, Object> timeStampCreatedAt = new HashMap<String, Object>();
                timeStampCreatedAt.put(Constants.FIREBASE_TIMESTAMP_PROPERTY, ServerValue.TIMESTAMP);
                BucketCategory newCategory = new BucketCategory(owner, category, timeStampCreatedAt);

                String itemId = newRef.getKey();
                newRef.setValue(newCategory);
            }
        });

        AlertDialog categoryDialog = dialogBuilder.create();
        categoryDialog.show();

    }

    public void listListener(){
        categoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = adapter.getRef(position).getKey();
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(Constants.KEY_ID,key );
                startActivity(intent);
            }
        });
    }
    protected void onDestroy()
    {
        super.onDestroy();
        usersRef.removeEventListener(mUserRefListener);
    }
}

