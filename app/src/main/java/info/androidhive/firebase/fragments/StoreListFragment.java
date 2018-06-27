package info.androidhive.firebase.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import info.androidhive.firebase.R;
import info.androidhive.firebase.activity.CreateStoreActivity;
import info.androidhive.firebase.activity.MyDeals;
import info.androidhive.firebase.activity.StoreDetailActivity;
import info.androidhive.firebase.adapters.StoreListRecyclerViewAdapter;
import info.androidhive.firebase.model.UserDemo;
import info.androidhive.firebase.util.ItemDecorationAlbumColumns;
import info.androidhive.firebase.utils.Pref;

/**
 * Created by Samit
 */
public class StoreListFragment extends Fragment {
    public static final String ARG_CATEGORY_NUMBER = "category_number";
    RecyclerView mRecyclerView;

    StoreListRecyclerViewAdapter adapter;

    FloatingActionButton addDealsFloatingButton;
    Button buttonShowMydeals;

    private static List<DealDetailModel> demoDataDeals;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_copon_list, container, false);
        int category = getArguments().getInt(ARG_CATEGORY_NUMBER);
        String planet = getResources().getStringArray(R.array.category_array)[category];


       /* int categoryId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                "drawable", getActivity().getPackageName());
        ((ImageView) rootView.findViewById(R.id.image)).setImageResource(categoryId);*/
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycleList);

        buttonShowMydeals = (Button) rootView.findViewById(R.id.showMyDeals);
        Pref pref=new Pref(getActivity());

        if(pref.getUserType().equals("merchant")){
            buttonShowMydeals.setText("Show Orders");
        }else {
            buttonShowMydeals.setText("Show My Orders");
        }

        buttonShowMydeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyDeals.class));//added by samit parade
            }
        });

        mRecyclerView.setHasFixedSize(true);
        addDealsFloatingButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
        addDealsFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateStoreActivity.class);
                startActivityForResult(intent, 1005);
            }
        });
       /* LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        llm.setOrientation(LinearLayoutManager.VERTICAL);
*/
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        mRecyclerView.addItemDecoration(new ItemDecorationAlbumColumns(
                getResources().getDimensionPixelSize(R.dimen.photos_list_spacing),
                getResources().getInteger(R.integer.photo_list_preview_columns)));

        demoDataDeals = new ArrayList<DealDetailModel>();
        getUserDataFromServer();
       /* char c = 'A';
        for (byte i = 0; i < 3; i++) {
            DealDetailModel dealDetailModel = new DealDetailModel();
            dealDetailModel.name = c++;
            dealDetailModel.age = (byte) (20 + i);
            demoDataDeals.add(dealDetailModel);
        }*/
        adapter = new StoreListRecyclerViewAdapter(getActivity(), demoDataDeals, this);
        mRecyclerView.setAdapter(adapter);

        getActivity().setTitle(planet);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        writeNewUser("10001", "samit", "parade");
        writeNewPost("10002", "amit", "samit");
        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = mRecyclerView.indexOfChild(v);
                Intent detailActIntent = new Intent(getActivity(), StoreDetailActivity.class);
                detailActIntent.putExtra("details_data", demoDataDeals.get(itemPosition));
            }
        });

        manageFloatingActionButtonPermissionVisibility();
        return rootView;

    }

    private void manageFloatingActionButtonPermissionVisibility() {
        if (new Pref(getActivity()).getUserType().equals("customer")) {
            addDealsFloatingButton.setVisibility(View.INVISIBLE);
        }

    }

    DatabaseReference firebaseDatabase;
    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {


    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1005 && resultCode == Activity.RESULT_OK) {
            demoDataDeals.add((DealDetailModel) data.getSerializableExtra("new_deal_response"));
            adapter.notifyDataSetChanged();

        }
    }

    private void writeNewUser(String userId, String name, String email) {
        UserDemo user = new UserDemo(name, email);

        firebaseDatabase.child("userstemp").child(userId).setValue(user);
    }

    private void writeNewPost(String userId, String username, String title) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneo`usly
        String key = firebaseDatabase.child("posts").push().getKey();
        UserDemo post = new UserDemo(username, title);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        firebaseDatabase.updateChildren(childUpdates);
    }

    void getUserDataFromServer() {
        {
            String url = "https://firbasesamit1.firebaseio.com/stores_final.json";
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading...");
            pd.show();

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Log.d("reserved response", "" + s.toString());
                    JSONObject data = null;
                    if (s != null) {
                        try {
                            data = new JSONObject(s.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (data != null) {

                            Iterator<String> it = data.keys();
                            while (it.hasNext()) {
                                String key = it.next();
                                try {
                                    if (data.get(key) instanceof JSONArray) {
                                        JSONArray arry = data.getJSONArray(key);
                                        int size = arry.length();
                                        for (int i = 0; i < size; i++) {
                                            JSONArray jsonObject = (arry.getJSONArray(i));
                                        }
                                    } else if (data.get(key) instanceof JSONObject) {
                                        JSONObject jsonObject = (data.getJSONObject(key));
                                        DealDetailModel dealDetailModel = new DealDetailModel();
                                        dealDetailModel.setIntegerUnigueId(Integer.parseInt(jsonObject.getString("integerUnigueId")));
                                        dealDetailModel.setProductTitle(jsonObject.getString("productTitle"));
                                        dealDetailModel.setDescription(jsonObject.getString("description"));
                                        if (jsonObject.has("price")) {
                                            dealDetailModel.setPrice(jsonObject.getString("price"));
                                        } else {
                                            dealDetailModel.setPrice("100Rs");
                                        }
                                        dealDetailModel.setAddress(jsonObject.getString("address"));
                                        //    dealDetailModel.setmDiscount(jsonObject.getString( "discount"));
                                        dealDetailModel.setServerImagePath(jsonObject.getString("serverImagePath"));
                                        dealDetailModel.setProducts(jsonObject.getString("products"));
                                        dealDetailModel.setContact_no(jsonObject.getString("contact_no"));
                                        if (jsonObject.has("merchant_id")) {
                                            dealDetailModel.setMerchant_id(jsonObject.getString("merchant_id"));
                                        }


                                        demoDataDeals.add(dealDetailModel);
                                    } else {
                                        System.out.println(key + ":" + data.getString(key));
                                    }
                                    pd.dismiss();
                                    adapter.notifyDataSetChanged();
                                } catch (Throwable e) {
                                    try {
                                        System.out.println(key + ":" + data.getString(key));
                                    } catch (Exception ee) {
                                    }
                                    e.printStackTrace();
                                }
                            }
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

            RequestQueue rQueue = Volley.newRequestQueue(getActivity());
            rQueue.add(request);
        }
    }


    public void startDealActivity(DealDetailModel dealDetailModel) {
        Intent detailActIntent = new Intent(getActivity(), StoreDetailActivity.class);
        detailActIntent.putExtra("details_data", dealDetailModel);
        startActivity(detailActIntent);
    }
}

