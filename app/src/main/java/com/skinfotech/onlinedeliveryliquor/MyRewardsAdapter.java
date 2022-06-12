package com.skinfotech.onlinedeliveryliquor;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.ViewHolder> {

    private List<RewardModel>  rewardModelList;
    private Boolean useMiniLayout = false;
    private RecyclerView coupensRecyclerview;
    private LinearLayout selectedCoupen;
    private String productOriginalPrice;
    private TextView selectedCoupenTitle, selectedCoupenExpiryDate,  selectedCoupenBody, discountedPrice;
    private int cartItemPosition =-1;
    private List<CartItemModel> cartItemModelList;

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
    }
    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView coupensRecyclerview, LinearLayout selectedCoupen, String productOriginalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody,TextView discountedPrice) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.coupensRecyclerview =coupensRecyclerview;
        this.selectedCoupen = selectedCoupen;
        this.productOriginalPrice =productOriginalPrice;
        this.selectedCoupenTitle= coupenTitle;
        this.selectedCoupenExpiryDate = coupenExpiryDate;
        this.selectedCoupenBody = coupenBody;
        this.discountedPrice = discountedPrice;
    }
    public MyRewardsAdapter(int cartItemPosition,List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView coupensRecyclerview, LinearLayout selectedCoupen, String productOriginalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody,TextView discountedPrice,List<CartItemModel> cartItemModelList) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.coupensRecyclerview =coupensRecyclerview;
        this.selectedCoupen = selectedCoupen;
        this.productOriginalPrice =productOriginalPrice;
        this.selectedCoupenTitle= coupenTitle;
        this.selectedCoupenExpiryDate = coupenExpiryDate;
        this.selectedCoupenBody = coupenBody;
        this.discountedPrice = discountedPrice;
        this.cartItemPosition = cartItemPosition;
        this.cartItemModelList = cartItemModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (useMiniLayout){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_rewards_item_layout,parent,false);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_item_layout, parent, false);
        }

       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String coupenId = rewardModelList.get(position).getCoupenId();
        String type = rewardModelList.get(position).getType();
        Date validity = rewardModelList.get(position).getTimestamp();
        String body = rewardModelList.get(position).getCoupenBody();
        String lowerLimit = rewardModelList.get(position).getLowerLimit();
        String upperLimit = rewardModelList.get(position).getUpperLimit();
        String discORamt = rewardModelList.get(position).getDiscORamt();
        Boolean alreadyUsed = rewardModelList.get(position).getAlreadyUsed();



        holder.setData(coupenId,type,validity,body,lowerLimit,upperLimit,discORamt,alreadyUsed);
    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView coupenTitle;
        private TextView coupenExpiryDate;
        private TextView coupenBody;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coupenTitle= itemView.findViewById(R.id.coupen_title);
            coupenExpiryDate = itemView.findViewById(R.id.coupen_validity);
            coupenBody = itemView.findViewById(R.id.coupen_body);

        }
        private void setData( final String coupenId,final String type, final Date validity, final String body, final String upperLimit, final String lowerLimit, final String discORamt, final boolean alreadyUsed){
          if (type.equals("Discount")){
              coupenTitle.setText(type);
          }else {
              coupenTitle.setText("FLAT Rs."+discORamt+" OFF");
          }
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM YYYY");
          if (alreadyUsed){
              coupenExpiryDate.setText("Already used");
              coupenExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.BtnBlue));
              coupenBody.setTextColor(Color.parseColor("#50ffffff"));
              coupenTitle.setTextColor(Color.parseColor("#50ffffff"));
          }else {
              coupenBody.setTextColor(Color.parseColor("#ffffff"));
              coupenTitle.setTextColor(Color.parseColor("#ffffff"));
              coupenExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPurple));
              coupenExpiryDate.setText("till "+simpleDateFormat.format(validity));
          }


          coupenBody.setText(body);

            if (useMiniLayout){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!alreadyUsed){
                            ///do nth
                        selectedCoupenTitle.setText(type);
                        selectedCoupenExpiryDate.setText(simpleDateFormat.format(validity));
                        selectedCoupenBody.setText(body);
                        if (Long.valueOf(productOriginalPrice)>Long.valueOf(lowerLimit)&&Long.valueOf(productOriginalPrice)<Long.valueOf(upperLimit)){
                            if (type.equals("Discount")){
                                Long discountAmount = Long.valueOf(productOriginalPrice)*Long.valueOf(discORamt)/100;
                                discountedPrice.setText("Rs."+String.valueOf(Long.valueOf(productOriginalPrice)-discountAmount)+"/-");
                            }else {
                                discountedPrice.setText("Rs."+String.valueOf(Long.valueOf(productOriginalPrice)-Long.valueOf(discORamt)+"/-"));
                            }
                            if (cartItemPosition!=-1){
                                cartItemModelList.get(cartItemPosition).setSelectedCoupenId(coupenId);
                            }

                        }else {
                            if (cartItemPosition!=-1){
                                cartItemModelList.get(cartItemPosition).setSelectedCoupenId(null);
                            }

                            discountedPrice.setText("Invalid");
                            Toast.makeText(itemView.getContext(), "Sorry ! Product doesn't matches the coupen terms.", Toast.LENGTH_SHORT).show();
                        }
                        if (coupensRecyclerview.getVisibility() == View.GONE) {
                            coupensRecyclerview.setVisibility(View.VISIBLE);
                            selectedCoupen.setVisibility(View.GONE);
                        } else {
                            coupensRecyclerview.setVisibility(View.GONE);
                            selectedCoupen.setVisibility(View.VISIBLE);

                        }

                    }}
                });
            }
        }
    }
}
