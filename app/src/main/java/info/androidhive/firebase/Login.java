package info.androidhive.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

import info.androidhive.firebase.activity.StoreDashBoardActivity;
import info.androidhive.firebase.model.Note;
import info.androidhive.firebase.model.UserDemo;
import info.androidhive.firebase.utils.Pref;

public class Login extends AppCompatActivity {
    TextView registerUser;
    EditText username, password;
    Button loginButton;
    String user, pass;
    Firebase mRef;
    ArrayAdapter<String> stringArrayAdapter;
    ArrayList<String> mUsername = new ArrayList<>();
    Firebase mRefDb;
    ListView listView;

    FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth mAuth;
    DatabaseReference firebaseDatabaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_1);
        Firebase.setAndroidContext(this);
      /*  listView = (ListView) findViewById(R.id.listview);
        stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mUsername);
        listView.setAdapter(stringArrayAdapter);*/
       /* mRef=new Firebase("https://firbasesamit1.firebaseio.com/Users");


        Firebase mRefChild=mRef.child("Name");
        mRefChild.setValue("samit");*/

        mRefDb = new Firebase("https://firbasesamit1.firebaseio.com/");
        mRefDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("resulted change value", dataSnapshot.toString());
                Map<String, String> stringStringMap = dataSnapshot.getValue(Map.class);
                Log.d("values are", "pro" + stringStringMap.get("profession") + stringStringMap.get("age"));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("resulted change value", firebaseError.toString());
            }
        });

        mRef = new Firebase("https://firbasesamit1.firebaseio.com/users1");
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                mUsername.add(value);
            //    stringArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        registerUser = (TextView) findViewById(R.id.register);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCounter=0;
                if (validateInput()) {
                    doSignIn();
                }else {
                    AlertDialog.Builder  alertDialog=new AlertDialog.Builder(Login.this);
                    alertDialog.setTitle("Gocerry App");
                    alertDialog.setMessage("Please Enter valid Inputs...");
                    alertDialog.show();

                }
            }


        });
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpPage();
            }
        });
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    if (firebaseAuth.getCurrentUser().getEmail().equals(username.getText().toString()))
                        manageSignPermission(firebaseAuth.getCurrentUser().getUid());

                }
            }
        };
        firebaseDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseDatabaseUser.keepSynced(true);


    }

    private boolean validateInput() {
        boolean flag = true;
        if (TextUtils.isEmpty(username.getText().toString())) {
            flag = false;
        } else {
            flag = android.util.Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches();
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            flag = false;
        }

        return flag;
    }

    private void openSignUpPage() {
        startActivity(new Intent(Login.this, RegisterActivity.class));
    }

    private void addNotes() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Note note = new Note();
        note.setUid(database.child("notes").push().getKey());
        note.setTitle("samit 1");
        note.setDecription("Description 1");
        database.child("notes").child(note.getUid()).setValue(note);
        finish();
    }


    private void doSignIn() {
        mAuth = FirebaseAuth.getInstance();
        String email = username.getText().toString();
        String pw = password.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(email)) {
            Toast.makeText(Login.this, "Fields are not empty", Toast.LENGTH_SHORT);
            return;
        }
        mAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                } else {
                    Toast.makeText(Login.this, "Problem with login", Toast.LENGTH_SHORT);
                }
            }
        });
        mAuth.addAuthStateListener(authStateListener);
    }
int mCounter=0;
    void manageSignPermission(final String userId){
        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users_final/"+userId);

// Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                UserDemo s= dataSnapshot.getValue(UserDemo.class);
                Log.d("samitp",""+s);
               Pref pref =new Pref(getApplicationContext());
                if(s!=null) {
                    pref.putUserName(s.username);
                    pref.putUserType(s.user_type);
                    pref.putUserUniqueId(userId+"");
                    pref.putcontact_number(s.contact_number);
                    pref.putaddress(s.address);
                }
                if(mCounter==0)
                  startActivity(new Intent(Login.this, StoreDashBoardActivity.class));
                mCounter+=1;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
