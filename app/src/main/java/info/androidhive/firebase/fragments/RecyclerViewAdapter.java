package info.androidhive.firebase.fragments;

/**
 * Created by Samit
 */
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import info.androidhive.firebase.R;

public class RecyclerViewAdapter
        extends RecyclerView.Adapter
        <RecyclerViewAdapter.ListItemViewHolder> {

    private List<DealDetailModel> items;
    private SparseBooleanArray selectedItems;
    Fragment couponListFragment;
    Context context;

    RecyclerViewAdapter(Context context,List<DealDetailModel> dealDetailModelData,Fragment couponListFragment) {
        this.context=context;
        this.couponListFragment =couponListFragment;
        if (dealDetailModelData == null) {
            throw new IllegalArgumentException("dealDetailModelData must not be null");
        }
        items = dealDetailModelData;
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_demo_recyclerview_01, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
        final DealDetailModel dealDetailModel = items.get(position);
        viewHolder.title.setText(String.valueOf(dealDetailModel.getProductTitle()));
        viewHolder.desc.setText(String.valueOf(dealDetailModel.getDescription()));
        viewHolder.itemView.setActivated(selectedItems.get(position, false));

       StorageReference  newStorageReference;
        if(TextUtils.isEmpty(dealDetailModel.getServerImagePath())){
            newStorageReference = FirebaseStorage.getInstance().getReference().child("images/69.jpg");
        }else {
            newStorageReference = FirebaseStorage.getInstance().getReference().child(dealDetailModel.getServerImagePath());
        }

       // Glide.with(context).using(new FirebaseImageLoader()).load(newStorageReference).into(viewHolder.imageView);
        Log.d("selected image",newStorageReference.getDownloadUrl().toString());

        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(newStorageReference)
                .placeholder(R.drawable.no_image_icon)
                .into(viewHolder.imageView);
        viewHolder.viewDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(couponListFragment instanceof CouponListFragment)
                ((CouponListFragment)couponListFragment).startDealActivity(dealDetailModel);
                else if (couponListFragment instanceof StoreListFragment)
                    ((StoreListFragment)couponListFragment).startDealActivity(dealDetailModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView desc;
        ImageView imageView;
        View viewDeal;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txt_name);
            desc = (TextView) itemView.findViewById(R.id.txt_description);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            viewDeal=itemView;
        }
    }
}
