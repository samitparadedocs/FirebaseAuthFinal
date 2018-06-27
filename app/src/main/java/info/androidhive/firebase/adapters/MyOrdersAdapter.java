package info.androidhive.firebase.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import info.androidhive.firebase.R;
import info.androidhive.firebase.model.MyOrdersModel;
import info.androidhive.firebase.model.ProductModel;

/**
 * Created by Samit
 */
public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyViewHolder> {
    List<MyOrdersModel> myOrdersModels;
    private Context mContext;

    public MyOrdersAdapter(List<MyOrdersModel> myOrdersModels, Context mContext) {
        this.myOrdersModels = myOrdersModels;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflateView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_adapter, parent, false);
        return new MyViewHolder(inflateView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyOrdersModel myOrdersModel = myOrdersModels.get(position);
        List<ProductModel> selectedProductModel = new Gson().fromJson(myOrdersModel.orders, new TypeToken<List<ProductModel>>() {
        }.getType());
        String orederedProducts = "";
        for (int i = 0; i < selectedProductModel.size(); i++) {
            orederedProducts += selectedProductModel.get(i).getProductName() + " " + selectedProductModel.get(i).getProductCost() + " " + selectedProductModel.get(i).getProductQuantity() + "\n";
        }
        holder.orderedProductList.setText(orederedProducts);
        holder.customerDetail.setText(myOrdersModel.user_name + "\n" + myOrdersModel.user_address + "\n" );
    }

    @Override
    public int getItemCount() {
        return myOrdersModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView orderedProductList, customerDetail;

        public MyViewHolder(View itemView) {
            super(itemView);
            orderedProductList = (TextView) itemView.findViewById(R.id.orderProductLists);
            customerDetail = (TextView) itemView.findViewById(R.id.customerDetails);

        }
    }
}
