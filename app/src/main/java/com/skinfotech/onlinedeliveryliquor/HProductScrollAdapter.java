package com.skinfotech.onlinedeliveryliquor;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class HProductScrollAdapter extends RecyclerView.Adapter<HProductScrollAdapter.ViewHolder> {

    private List<HProductScrollModel> hProductScrollModelList;

    public HProductScrollAdapter(List<HProductScrollModel> hProductScrollModelList) {
        this.hProductScrollModelList = hProductScrollModelList;
    }

    @NonNull
    @Override
    public HProductScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HProductScrollAdapter.ViewHolder holder, int position) {
        String  resource = hProductScrollModelList.get(position).getProductImage();
        String  title = hProductScrollModelList.get(position).getPrductTitle();
        String  detail = hProductScrollModelList.get(position).getProductDetail();
        String  price = hProductScrollModelList.get(position).getProductPrice();
        String productId = hProductScrollModelList.get(position).getProductID();

        holder.setData(resource,productId,title,detail,price);





    }

    @Override
    public int getItemCount() {
        if (hProductScrollModelList.size()>8){
            return 8;
        }else {
            return hProductScrollModelList.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle, productDetail, productPrice;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.hs_product_image);
            productTitle = itemView.findViewById(R.id.hs_prduct_title);
            productDetail = itemView.findViewById(R.id.hs_prduct_detail);
            productPrice = itemView.findViewById(R.id.hs_product_price);



        }
        private void setData(final String productID, String resource, String title, String detail, String price){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.icon_placeholder)).into(productImage);
            productTitle.setText(title);
            productDetail.setText(detail);
            productPrice.setText("Rs."+price+"/-");

            if (!title.equals("")){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent productdetailIntent = new Intent(itemView.getContext(),ProductDetailsActivity.class);
                    productdetailIntent.putExtra("PRODUCT_ID",productID);
                    itemView.getContext().startActivity(productdetailIntent);
                }
            });
        }}

    }
}
