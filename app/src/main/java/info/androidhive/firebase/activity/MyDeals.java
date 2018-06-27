package info.androidhive.firebase.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import info.androidhive.firebase.R;
import info.androidhive.firebase.adapters.MyOrdersAdapter;
import info.androidhive.firebase.fragments.DealDetailModel;
import info.androidhive.firebase.model.MyOrdersModel;
import info.androidhive.firebase.utils.Pref;
import info.androidhive.firebase.utils.VerticalSpaceItemDecoration;

/**
 * Created by Samit
 */
public class MyDeals extends AppCompatActivity {
    DealDetailModel dealDetailModel;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_purchase_deal);
        recyclerView = (RecyclerView) findViewById(R.id.orders);

        Bundle extras = getIntent().getExtras();
        Log.d("in my deal page", "Smait");
      /*  if (extras != null && extras.containsKey("details_data")) {
            dealDetailModel = (DealDetailModel) extras.getSerializable("details_data");

        }*/
        //  getUserDataFromServer();
        Pref pref = new Pref(this);
        if (pref.getUserType().equals("merchant")) {
            getMerchantFromServer();
        } else {
            getUserDataFromServer();
        }

       /* DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("user_purchase_order").child(new Pref(this).getUserUniqueId());
        mRef.keepSynced(true);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("Child Event,Verse", dataSnapshot.getKey());

                // dailyVerse = dataSnapshot.getValue(DailyVerse.class);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("Child Event,Verse", "onChildChanged:" + dataSnapshot.getKey());

                //   dailyVerse = dataSnapshot.getValue(DailyVerse.class);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "onCancelled", databaseError.toException());
                *//*Toast.makeText(context, "Failed to load verse",
                        Toast.LENGTH_SHORT).show();*//*
            }
        };

        mRef.addChildEventListener(childEventListener);*/


    }

    void getUserDataFromServer() {
        {
            String url = "https://firbasesamit1.firebaseio.com/user_purchase_order/" + (new Pref(this).getUserUniqueId() + ".json");
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Loading...");
            pd.show();

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    pd.dismiss();
                    Log.d("reserved response", "" + s.toString());
                    JSONObject data = null;
                    if (s != null) {
                                try {
                            data = new JSONObject(s.toString());
                            if (data != null) {


                                List<MyOrdersModel> myOrdersModels = new ArrayList<>();
                                Iterator<String> it = data.keys();
                                while (it.hasNext()) {
                                    String key = it.next();

                                    if (data.get(key) instanceof JSONArray) {
                                        JSONArray arry = data.getJSONArray(key);
                                        int size = arry.length();
                                        for (int i = 0; i < size; i++) {
                                            JSONArray jsonObject = (arry.getJSONArray(i));
                                        }
                                    } else if (data.get(key) instanceof JSONObject) {
                                        JSONObject jsonObject = (data.getJSONObject(key));

                                        MyOrdersModel myOrdersModel = new MyOrdersModel();
                                        if (jsonObject.has("merchant_id"))
                                            myOrdersModel.merchant_id = jsonObject.getString("merchant_id");
                                        if (jsonObject.has("orders"))
                                            myOrdersModel.orders = jsonObject.getString("orders");
                                        if (jsonObject.has("address"))
                                            myOrdersModel.user_id = jsonObject.getString("address");
                                        if (jsonObject.has("store_name"))
                                            myOrdersModel.store_name = jsonObject.getString("store_name");
                                        myOrdersModels.add(myOrdersModel);

                                    } else {
                                        System.out.println(key + ":" + data.getString(key));
                                    }
//
                                }

                                MyOrdersAdapter myOrdersAdapter = new MyOrdersAdapter(myOrdersModels, MyDeals.this);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(5));
                                recyclerView.setAdapter(myOrdersAdapter);

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("" + volleyError);
                    pd.dismiss();
                }
            });

            RequestQueue rQueue = Volley.newRequestQueue(this);
            rQueue.add(request);
        }
    }

    void getMerchantFromServer() {
        {
            String url = "https://firbasesamit1.firebaseio.com/merchant_purchase_order/" + /*dealDetailModel.getMerchant_id() */new Pref(MyDeals.this).getUserUniqueId() + ".json";
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Loading...");
            pd.show();

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    pd.dismiss();
                    Log.d("reserved response", "" + s.toString());
                    JSONObject data = null;
                    if (s != null) {
                        try {
                            data = new JSONObject(s.toString());
                            if (data != null) {


                                List<MyOrdersModel> myOrdersModels = new ArrayList<>();
                                Iterator<String> it = data.keys();
                                while (it.hasNext()) {
                                    String key = it.next();

                                    if (data.get(key) instanceof JSONArray) {
                                        JSONArray arry = data.getJSONArray(key);
                                        int size = arry.length();
                                        for (int i = 0; i < size; i++) {
                                            JSONArray jsonObject = (arry.getJSONArray(i));
                                        }
                                    } else if (data.get(key) instanceof JSONObject) {
                                        JSONObject jsonObject = (data.getJSONObject(key));

                                        MyOrdersModel myOrdersModel = new MyOrdersModel();
                                        if (jsonObject.has("user_id"))
                                            myOrdersModel.merchant_id = jsonObject.getString("user_id");
                                        if (jsonObject.has("orders"))
                                            myOrdersModel.orders = jsonObject.getString("orders");
                                        if (jsonObject.has("user_id"))
                                            myOrdersModel.user_id = jsonObject.getString("user_id");
                                        if (jsonObject.has("store_name"))
                                            myOrdersModel.store_name = jsonObject.getString("store_name");
                                        if (jsonObject.has("user_contact_number"))
                                            myOrdersModel.user_contact_number = jsonObject.getString("user_contact_number");
                                        if (jsonObject.has("user_address"))
                                            myOrdersModel.user_address = jsonObject.getString("user_address");
                                        if (jsonObject.has("user_name"))
                                            myOrdersModel.user_name = jsonObject.getString("user_name");

                                        myOrdersModels.add(myOrdersModel);

                                    } else {
                                        System.out.println(key + ":" + data.getString(key));
                                    }

                                }
                                MyOrdersAdapter myOrdersAdapter = new MyOrdersAdapter(myOrdersModels, MyDeals.this);
                                // recyclerView.setAdapter(myOrdersAdapter);
                                //  myOrdersAdapter.notifyDataSetChanged();
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());

       /* DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());*/
                                recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(5));

                                recyclerView.setAdapter(myOrdersAdapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("" + volleyError);
                    pd.dismiss();
                }
            });

            RequestQueue rQueue = Volley.newRequestQueue(this);
            rQueue.add(request);
        }
    }
}
