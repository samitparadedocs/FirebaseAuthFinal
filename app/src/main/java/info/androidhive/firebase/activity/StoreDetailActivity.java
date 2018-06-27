package info.androidhive.firebase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.androidhive.firebase.R;
import info.androidhive.firebase.adapters.ProductAdapter;
import info.androidhive.firebase.fragments.DealDetailModel;
import info.androidhive.firebase.model.ProductModel;
import info.androidhive.firebase.utils.Pref;
import info.androidhive.firebase.utils.VerticalSpaceItemDecoration;

/**
 * Created by Samit
 */
public class StoreDetailActivity extends AppCompatActivity {
    TextView title;
    TextView desc;
    TextView address;
    ImageView imageView;
    DealDetailModel dealDetailModel;
    RecyclerView recyclerView;
    List<ProductModel> productModels = new ArrayList<>();
    ProductAdapter mAdapter;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_detail_activity);

        title = (TextView) findViewById(R.id.txt_name);
        desc = (TextView) findViewById(R.id.txt_description);
        imageView = (ImageView) findViewById(R.id.imageView);
        address = (TextView) findViewById(R.id.txt_add);

        Bundle extras = getIntent().getExtras();
        init();
        if (extras != null && extras.containsKey("details_data")) {
            dealDetailModel = (DealDetailModel) extras.getSerializable("details_data");
            initialiseData();
        }
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.reclyclerView);
        mAdapter = new ProductAdapter(this,productModels);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

       /* DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());*/
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(5));

        recyclerView.setAdapter(mAdapter);
    }

    private void initialiseData() {
        title.setText(dealDetailModel.getProductTitle());
        desc.setText(dealDetailModel.getDescription());
        address.setText(dealDetailModel.getAddress());
        StorageReference newStorageReference;

        if (TextUtils.isEmpty(dealDetailModel.getServerImagePath())) {
            newStorageReference = FirebaseStorage.getInstance().getReference().child("images/98.jpg");
        } else {
            newStorageReference = FirebaseStorage.getInstance().getReference().child(dealDetailModel.getServerImagePath());
        }
        Log.d("selected image", newStorageReference.getDownloadUrl().toString());

        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(newStorageReference)
                .placeholder(R.drawable.no_image_icon)
                .into(imageView);
        ProductModel[] models = new Gson().fromJson(dealDetailModel.getProducts(), ProductModel[].class);
        if (models == null) {

        } else {
            productModels.clear();
            productModels.addAll(Arrays.asList(models));
            mAdapter.notifyDataSetChanged();
        }
    }

    public void callToPurchase(View view) {
        mAdapter.finalCostForSelectedProduct();
      /* String posted_by = "111-333-222-4";

        String uri = "tel:" +dealDetailModel.getContact_no() ;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);*/
    }
  /*  user_purchase_order*/
    public void createOrder(List<ProductModel> selectedProductModel, double finalCost1) {
        String uniqueId = ""+System.currentTimeMillis();
        DatabaseReference parent_orders = mDatabaseReference.child("user_purchase_order");
        DatabaseReference current_user_db = parent_orders.child(new Pref(getApplication()).getUserUniqueId());
        DatabaseReference current_user_array = current_user_db.child(uniqueId);
        current_user_array.child("orders").setValue(new Gson().toJson(selectedProductModel));
        current_user_array.child("merchant_id").setValue(dealDetailModel.getMerchant_id());
        current_user_array.child("address").setValue(dealDetailModel.getAddress());
        current_user_array.child("store_name").setValue(dealDetailModel.getProductTitle());

        DatabaseReference merchantorders = mDatabaseReference.child("merchant_purchase_order");

        DatabaseReference current_merchant_db = merchantorders.child(dealDetailModel.getMerchant_id());

        DatabaseReference current_merchant_array = current_merchant_db.child(uniqueId);

        current_merchant_array.child("orders").setValue(new Gson().toJson(selectedProductModel));
        current_merchant_array.child("user_id").setValue(new Pref(getApplication()).getUserUniqueId());
        current_merchant_array.child("user_name").setValue(new Pref(getApplication()).getUserName());
        current_merchant_array.child("user_contact_number").setValue(new Pref(getApplication()).getcontact_number());
        current_merchant_array.child("user_address").setValue(new Pref(getApplication()).getaddress());
        current_merchant_array.child("store_name").setValue(dealDetailModel.getProductTitle());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_store_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_myorders:
                Intent detailActIntent = new Intent(this, MyDeals.class);
                detailActIntent.putExtra("details_data", dealDetailModel);
                startActivity(detailActIntent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
