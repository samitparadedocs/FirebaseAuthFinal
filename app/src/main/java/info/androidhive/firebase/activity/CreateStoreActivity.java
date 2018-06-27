package info.androidhive.firebase.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import info.androidhive.firebase.R;
import info.androidhive.firebase.adapters.ProductAdapter;
import info.androidhive.firebase.fragments.DealDetailModel;
import info.androidhive.firebase.model.ProductModel;
import info.androidhive.firebase.utils.Pref;
import info.androidhive.firebase.utils.VerticalSpaceItemDecoration;

/**
 * Created by Samit
 */
public class CreateStoreActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST_CODE = 1000;
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    Button chooseImage, uploadImage;
    ImageView imageView;
    StorageReference storageReference;
    int integerUnigueId;
    EditText mTitle, mDescription, mAddress, item, price, unit,contactNumber;
    String storagePath = "";
    String uniqueKey = "";
    List<ProductModel> productModels = new ArrayList<>();
    RecyclerView recyclerView;
    ProductAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_store);
        Log.d("Activity ", "onCreate");
        Random rand = new Random();
        integerUnigueId = rand.nextInt(100);

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    //startActivity(new Intent(AcountActivity.this, Login.class));
                }
            }
        };
        mAuth.addAuthStateListener(authStateListener);
        storageReference = FirebaseStorage.getInstance().getReference();
        chooseImage = (Button) findViewById(R.id.selectImage);
        uploadImage = (Button) findViewById(R.id.uploadImage);
        imageView = (ImageView) findViewById(R.id.imageView);

        mTitle = (EditText) findViewById(R.id.storetTitle);
        mDescription = (EditText) findViewById(R.id.storeDescription);
        //  mPrice = (EditText) findViewById(R.id.txtPrice);
        mAddress = (EditText) findViewById(R.id.storeAddress);
        contactNumber = (EditText) findViewById(R.id.contactNumber);
        item = (EditText) findViewById(R.id.item);
        price = (EditText) findViewById(R.id.price);
        unit = (EditText) findViewById(R.id.unit);


        //  mDiscount = (EditText) findViewById(R.id.txtDiscount);


   /*     chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFilechooser();
            }
        });*/

       /* uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadFile();
            }
        });*/
        init();
    }

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.reclyclerView);
        productModels.clear();
        mAdapter = new ProductAdapter(this,productModels);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

       /* DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());*/
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(5));

        recyclerView.setAdapter(mAdapter);
    }

    //a Uri object to store file path
    private Uri filePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_deal_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            getFormData();
            addDealToStorage();
        }
        return super.onOptionsItemSelected(item);
    }


    private void doLogout() {
        mAuth.signOut();
    }

    public void startFilechooser(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST_CODE);

    }

    //this method will upload the file
    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            storagePath = "images/" + integerUnigueId + ".jpg";
            StorageReference riversRef = storageReference.child(storagePath);
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            Log.d("uploaded path", taskSnapshot.getDownloadUrl().getPath());
                            getFormData();
                            addDealToStorage();
                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }

    DealDetailModel dealDetailModel;

    private void getFormData() {
        dealDetailModel = new DealDetailModel();
        dealDetailModel.setIntegerUnigueId(integerUnigueId);
        dealDetailModel.setProductTitle(mTitle.getText().toString());
        dealDetailModel.setDescription(mDescription.getText().toString());
        dealDetailModel.setAddress(mAddress.getText().toString());
        dealDetailModel.setServerImagePath(storagePath);
        dealDetailModel.setProducts(new Gson().toJson(productModels));

        dealDetailModel.setContact_no(contactNumber.getText().toString().trim());
        dealDetailModel.setMerchant_id(new Pref(this).getUserUniqueId());

    }

    private void addDealToStorage() {

        if (dealDetailModel != null) {
            writeNewUser();
            Intent intent = new Intent();
            intent.putExtra("new_deal_response", dealDetailModel);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();

    private void writeNewUser() {


      /*  firebaseDatabase.child("deals").child(""+dealDetailModel.getIntegerUnigueId()).setValue(dealDetailModel);
      */
        String key = firebaseDatabase.child("posts").push().getKey();
        // DealDetailModel post = new UserDemo( username, title);
        Map<String, Object> postValues = dealDetailModel.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/stores_final/" + dealDetailModel.getIntegerUnigueId(), postValues);
        //  childUpdates.put("/user-posts/" + dealDetailModel.getIntegerUnigueId() + "/" + key, postValues);

        firebaseDatabase.updateChildren(childUpdates);
    }


    public void onSaveClick(View view) {
        if(isValidStore()) {
            uploadFile();
        }

    }

    public void onAddProduct(View view) {
        if (isValidProduct()) {
            productModels.add(new ProductModel(
                    item.getText().toString().trim(),
                    Double.valueOf(price.getText().toString().trim())
                    ,
                    unit.getText().toString().trim()));
            mAdapter.notifyDataSetChanged();

            item.setText("");
            price.setText("");
            unit.setText("");


        }
    }

    public boolean isValidProduct() {
        if (item.getText().toString().trim().isEmpty()) {
            item.setError("Please Enter product name");
            return false;
        } else if (price.getText().toString().trim().isEmpty()) {
            price.setError("Please Enter product price");
            return false;
        } else if (unit.getText().toString().trim().isEmpty()) {
            unit.setError("Please Enter product unit");
            return false;
        }
        return true;
    }

    public boolean isValidStore() {

        if (mTitle.getText().toString().trim().isEmpty()) {
            mTitle.setError("Please Enter store name");
            return false;
        } else if (mDescription.getText().toString().trim().isEmpty()) {
            mDescription.setError("Please Enter short desc");
            return false;
        } else if (mAddress.getText().toString().trim().isEmpty()) {
            mAddress.setError("Please Enter address");
            return false;
        }
        else if (contactNumber.getText().toString().trim().isEmpty()) {
            contactNumber.setError("Please Enter contact Number");
            return false;
        }

        else if (productModels==null || productModels.size()==0) {

            return false;
        }
        return true;
    }
}
