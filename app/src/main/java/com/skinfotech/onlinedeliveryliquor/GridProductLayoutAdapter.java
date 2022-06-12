package com.skinfotech.onlinedeliveryliquor;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {

    List<HProductScrollModel> hProductScrollModelList;

    public GridProductLayoutAdapter(List<HProductScrollModel> hProductScrollModelList) {
        this.hProductScrollModelList = hProductScrollModelList;
    }

    @Override
    public int getCount() {
        return hProductScrollModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertview, final ViewGroup viewGroup) {
        View view;
        if (convertview == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_item_layout, null);
            view.setElevation(0);
            view.setBackgroundColor(Color.parseColor("#ffffff"));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent productdetailsIntent = new Intent(viewGroup.getContext(), ProductDetailsActivity.class);
                    productdetailsIntent.putExtra("PRODUCT_ID", hProductScrollModelList.get(position).getProductID());
                    view.getContext().startActivity(productdetailsIntent);
                }
            });

            ImageView productImage = view.findViewById(R.id.hs_product_image);
            TextView productTitle = view.findViewById(R.id.hs_prduct_title);
            TextView productDetail = view.findViewById(R.id.hs_prduct_detail);
            TextView productPrice = view.findViewById(R.id.hs_product_price);

            Glide.with(view.getContext()).load(hProductScrollModelList.get(position).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.icon_placeholder)).into(productImage);
            productTitle.setText(hProductScrollModelList.get(position).getPrductTitle());
            productDetail.setText(hProductScrollModelList.get(position).getProductDetail());
            productPrice.setText("Rs." + hProductScrollModelList.get(position).getProductPrice() + "/-");


        } else {
            view = convertview;
        }


        return view;
    }
}
