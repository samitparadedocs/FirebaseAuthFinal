package info.androidhive.firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText mUsername, mPassword, mEmail,conatctNumber, address;
    Button mRegister;
    ProgressDialog mProgress;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference;
    String userType="merchant";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_1);
        mUsername = (EditText) findViewById(R.id.username);
        mEmail = (EditText) findViewById(R.id.emailId);
        mEmail = (EditText) findViewById(R.id.emailId);

        conatctNumber = (EditText) findViewById(R.id.contactNumber);
        address = (EditText) findViewById(R.id.userAddress);

        mPassword = (EditText) findViewById(R.id.password);
        mRegister = (Button) findViewById(R.id.registerButton);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    startRegister();
                }
            }

        });
        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users_final");


    }

    private boolean validateInput() {

        String errorMesage = "";
        if (TextUtils.isEmpty(mUsername.getText().toString())) {

            errorMesage = "Please enter name";
            showALertDialog(errorMesage);
            return false;
        }
        if (TextUtils.isEmpty(mEmail.getText().toString())) {
            errorMesage = "Please enter email";
            showALertDialog(errorMesage);
            return false;
        } else if(!(android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches())) {
            errorMesage = "Please enter valid email";
            showALertDialog(errorMesage);
            return false;
        }

        if (TextUtils.isEmpty(mPassword.getText().toString())) {
            errorMesage = "Please enter password";
            showALertDialog(errorMesage);
            return false;
        }
        return true;
    }

    void showALertDialog(String errorMessage) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
        alertDialog.setTitle("Gocerry App");
        alertDialog.setMessage(errorMessage);
        alertDialog.show();
    }

    private void startRegister() {
        mProgress.show();
        mProgress.setMessage("..Signing Up..");
        final String username = mUsername.getText().toString();
        final String email = mEmail.getText().toString();

        final String pass = mPassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//when task is complete we can add it into the dATABASE
                if (task.isSuccessful()) {
                    String user_id = mAuth.getCurrentUser().getUid();
                    DatabaseReference current_user_db = mDatabaseReference.child(user_id);
                    current_user_db.child("username").setValue(username);
                    current_user_db.child("email").setValue(email);
                    current_user_db.child("password").setValue(pass);
                    current_user_db.child("image").setValue("default");
                    current_user_db.child("user_type").setValue(userType);
                    current_user_db.child("contact_number").setValue(conatctNumber.getText().toString());
                    current_user_db.child("address").setValue(address.getText().toString().trim());

                    Log.d("Current user", mAuth.getCurrentUser().toString());
                    mProgress.dismiss();

                    Intent loginIntent = new Intent(RegisterActivity.this, Login.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(loginIntent);
                }else {
                    showALertDialog("Enter strong password more than 6 characters");
                    mProgress.dismiss();
                }
            }
        });

    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_customer:
                if (checked)
                    userType="customer";
                    break;
            case R.id.radio_merchant:
                if (checked)
                    userType="merchant";
                    break;
        }
    }
}
