package info.androidhive.firebase.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.firebase.R;
import info.androidhive.firebase.activity.StoreDetailActivity;
import info.androidhive.firebase.model.ProductModel;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<ProductModel> productList;
    List<ProductModel> selectedProductModel;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, price, productUnit, productCost;
        public EditText quantitySelected;
        CheckBox checkBoxPurchase;

        public MyViewHolder(View view) {
            super(view);

            productName = (TextView) view.findViewById(R.id.productName);
            price = (TextView) view.findViewById(R.id.productPrice);
            productUnit = (TextView) view.findViewById(R.id.productUnit);
            quantitySelected = (EditText) view.findViewById(R.id.quantitySelected);
            checkBoxPurchase = (CheckBox) view.findViewById(R.id.checkBoxPurchase);

            productCost = (TextView) view.findViewById(R.id.productCost);
        }

    }


    public ProductAdapter(Context context,List<ProductModel> productList) {
        this.productList = productList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ProductModel item = productList.get(position);
        holder.productName.setText(item.getProductName());
        holder.price.setText(item.getPrice() + "");
        holder.productUnit.setText(item.getUnit() + "");
        holder.checkBoxPurchase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!holder.quantitySelected.getText().toString().isEmpty()) {
                        Double productQuantity = (Double.valueOf(holder.quantitySelected.getText().toString()));
                        double productCost = (productQuantity * (double) item.getPrice());
                        holder.productCost.setText(productCost + "");
                        item.setProductCost(productCost);
                        item.setProductQuantity(productQuantity);
                        item.setIschecked(true);
                    } else {
                        holder.quantitySelected.setError("select Quantity");
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public void finalCostForSelectedProduct() {
       selectedProductModel = new ArrayList<>();
        double finalCost=0;
        for (ProductModel productModel : productList) {
            if (productModel.ischecked() == true) {
                selectedProductModel.add(productModel);
                finalCost += productModel.getProductCost();
            }

        }
        if(selectedProductModel.size() != 0){


            final double finalCost1 = finalCost;
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle("Plese confirm order")
                    .setMessage("Final bill for this order is Rs. "+finalCost )
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Order", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(context, "Yaay", Toast.LENGTH_SHORT).show();
                            ((StoreDetailActivity)context).createOrder(selectedProductModel, finalCost1);
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }else{
            Toast.makeText(context, "Please select any product", Toast.LENGTH_SHORT).show();
        }

    }

}