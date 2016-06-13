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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Objects;

import in.teachcoder.bucketlist.models.BucketCategory;
import in.teachcoder.bucketlist.models.Users;



public class MainActivity extends AppCompatActivity  implements GoogleApiClient.OnConnectionFailedListener {
    ListView categoriesList;
    FloatingActionButton addCategory;
    Firebase firebaseRef, categoriesRef;
    CategoriesListAdapter adapter;
    String owner;
    FirebaseInstanceIdService firebaseInstanceIdService;

    SharedPreferences sp;
    String mProvider,MEncodedEmail;
    private ValueEventListener mUserRefListener;
    Firebase usersRef;
GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addCategory = (FloatingActionButton) findViewById(R.id.addCategoryBtn);
        categoriesList = (ListView) findViewById(R.id.bucketCategoriesList);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        MEncodedEmail = sp.getString(Constants.KEY_SECOND_ENCODED_EMAIL,null);
        mProvider = sp.getString(Constants.KEY_PROVIDER,null);




       GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase(Constants.FIREBASE_BASE_URL);



        Query queryref;

        categoriesRef = new Firebase(Constants.FIREBASE_CATEGORIES_URL);

//        queryref = categoriesRef.orderByChild("owner").equalTo(MEncodedEmail);
        queryref = categoriesRef.child(MEncodedEmail);

        usersRef = new Firebase(Constants.FIREBASE_USER_URL).child(MEncodedEmail);
        adapter = new CategoriesListAdapter(this, BucketCategory.class, queryref);





        Log.d("name= " ,usersRef.toString());

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
                Firebase newRef = categoriesRef.child(MEncodedEmail).push();
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
                intent.putExtra("user",MEncodedEmail);
                startActivity(intent);
            }
        });
    }
    protected void onDestroy()
    {

        super.onDestroy();
        usersRef.removeEventListener(mUserRefListener);
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout_button) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    private void logout()
    {

        if(mProvider!=null)
        {
            firebaseRef.unauth();
        }

        if(mProvider.equals(Constants.GOOGLE_PROVIDER))
        {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {

                        }
                    }
            );
        }

        Intent startIntent = new Intent(this,Login_Activity.class);
          startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(startIntent);
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}

