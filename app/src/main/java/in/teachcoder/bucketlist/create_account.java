package in.teachcoder.bucketlist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class create_account extends AppCompatActivity {

    EditText name,email,pass;
    Button createaccountBtn;
    Button signin;
 ProgressDialog progressDialog;

    String useremail,userpasss,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        name = (EditText) findViewById(R.id.name_text_create);
        email = (EditText)findViewById(R.id.email_text_create);
        pass = (EditText)findViewById(R.id.pass_tex_create);
        createaccountBtn =(Button)findViewById(R.id.signup_button);
        signin = (Button)findViewById(R.id.signin_go);

        Firebase.setAndroidContext(this);

        final Firebase ref = new Firebase(Constants.FIREBASE_BASE_URL);
        progressDialog = new ProgressDialog(this,android.R.style.Theme_DeviceDefault_Light_Dialog);
        progressDialog.setMessage("creating user in firebase");
        progressDialog.setCancelable(false);

        createaccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                useremail= email.getText().toString();
                username =name.getText().toString();
                userpasss = pass.getText().toString();

               ref.createUser(useremail, userpasss, new Firebase.ResultHandler() {
                   @Override
                   public void onSuccess() {
                       Log.d("CreateAccount","user account created"+name);
                       progressDialog.dismiss();
                       Intent i = new Intent(create_account.this,Login_Activity.class);

                       startActivity(i);



                   }

                   @Override
                   public void onError(FirebaseError firebaseError) {

                       if(firebaseError.getCode()==FirebaseError.EMAIL_TAKEN)
                           email.setError("Email already Exist");

                       Log.e("CreateAccount",firebaseError.toString());
                       progressDialog.dismiss();
                   }
               });
            }
        });



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(create_account.this,Login_Activity.class);
                startActivity(intent);
            }
        });


    }




}
