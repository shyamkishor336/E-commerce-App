package com.skinfotech.onlinedeliveryliquor;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;
    private int lastPosition = -1;
    private TextView cartTotalAmount;
    private Boolean showDeleteBtn;

    public CartAdapter(List<CartItemModel> cartItemModelList, TextView cartTotalAmount, boolean showDeleteBtn) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteBtn = showDeleteBtn;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()) {
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case CartItemModel.CART_ITEM:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                return new cartItemViewHolder(view);
            case CartItemModel.TOTAL_AMOUNT:
                View cart = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout, parent, false);
                return new cartTotalAmountViewHolder(cart);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (cartItemModelList.get(position).getType()) {
            case CartItemModel.CART_ITEM:
                String productID = cartItemModelList.get(position).getProductID();
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                Long freecoupons = cartItemModelList.get(position).getFreeCoupons();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                String cuttedPrice = cartItemModelList.get(position).getCuttedPrice();
                Long offersApplied = cartItemModelList.get(position).getOffersApplied();
                boolean inStock = cartItemModelList.get(position).isInStock();
                Long productQty = cartItemModelList.get(position).getProductQty();
                Long maxQty = cartItemModelList.get(position).getMaxQty();
                boolean qtyError = cartItemModelList.get(position).isQtyError();
                List<String> qtyIds = cartItemModelList.get(position).getQutyIDs();
                long stockQty = cartItemModelList.get(position).getStockQuantity();
                boolean COD = cartItemModelList.get(position).isCOD();

                ((cartItemViewHolder) holder).setItemDetails(productID, resource, title, freecoupons, productPrice, cuttedPrice, offersApplied, position, inStock, String.valueOf(productQty), maxQty, qtyError, qtyIds, stockQty,COD);

                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemPrice = 0;
                String deliveryPrice;
                int totalAmount;
                int savedAmount = 0;
                for (int x = 0; x < cartItemModelList.size(); x++) {
                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).isInStock()) {
                        int quantity = Integer.parseInt(String.valueOf(cartItemModelList.get(x).getProductQty()));
                        totalItems = totalItems + quantity;
                        if (TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                            totalItemPrice = totalItemPrice + Integer.parseInt(cartItemModelList.get(x).getProductPrice()) * quantity;
                        } else {
                            totalItemPrice = totalItemPrice + Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice()) * quantity;
                        }
                        if (!TextUtils.isEmpty(cartItemModelList.get(x).getCuttedPrice())) {
                            savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getCuttedPrice()) - Integer.parseInt(cartItemModelList.get(x).getProductPrice())) * quantity;
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                                savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getProductPrice()) - Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice())) * quantity;
                            }
                        } else {
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCoupenId())) {
                                savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getProductPrice()) - Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice())) * quantity;

                            }

                        }
                    }
                }
                if (totalItemPrice > 500) {
                    deliveryPrice = "FREE";
                    totalAmount = totalItemPrice;
                } else {
                    deliveryPrice = "60";
                    totalAmount = totalItemPrice + 60;
                }

                cartItemModelList.get(position).setTotalAmount(totalItems);
                cartItemModelList.get(position).setTotalItemPrice(totalItemPrice);
                cartItemModelList.get(position).setDeliveryPrice(deliveryPrice);
                cartItemModelList.get(position).setTotalAmount(totalAmount);
                cartItemModelList.get(position).setSavedAmount(savedAmount);

                ((cartTotalAmountViewHolder) holder).setTotalAmount(totalItems, totalItemPrice, deliveryPrice, totalAmount, savedAmount);
                break;
            default:
                return;
        }
        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class cartItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage, freecouponIcon;
        private TextView productTitle, freeCoupons, productPrice, cuttedPrice, offerApplied, couponsApplied, productQty;
        private LinearLayout removeBtn;
        private TextView coupenRedeemBody;
        private LinearLayout coupenRedeemLayout;
        private Button redeemBtn;
        private ImageView codIndicator;


        ///////////coupen dialog
        private TextView coupenTitle;
        private TextView coupenExpiryDate;
        private TextView coupenBody;
        private RecyclerView coupensRecyclerview;
        private LinearLayout selectedCoupen;
        private TextView discountedPrice, originalPrice;
        private Button removerCoupenBtn, applyCoupenBtn;
        private LinearLayout applyORremoveBtnContainer;
        private TextView footerText;
        private String productOriginalPrice;
        /////////coupen dialog


        public cartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            freecouponIcon = itemView.findViewById(R.id.free_coupon_icon);
            freeCoupons = itemView.findViewById(R.id.tv_free_coupon);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            offerApplied = itemView.findViewById(R.id.offers_applied);
            couponsApplied = itemView.findViewById(R.id.coupons_applied);
            productQty = itemView.findViewById(R.id.product_quantity);
            removeBtn = itemView.findViewById(R.id.remove_item_btn);
            redeemBtn = itemView.findViewById(R.id.coupon_redeem_button);
            coupenRedeemLayout = itemView.findViewById(R.id.coupon_redeem_layout);
            coupenRedeemBody = itemView.findViewById(R.id.coupon_redeem_text);
            codIndicator = itemView.findViewById(R.id.cod_indicatorr);


        }

        private void setItemDetails(final String productID, String resource, String title, Long freeCouponsNo, final String productpricetext, String cuttedpricetext, Long offersAppliedNo, final int position, boolean inStock, final String proQty, final Long maxQty, boolean QtyError, final List<String> qtyIds, final long stockQty,boolean COD) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.icon_placeholder)).into(productImage);
            productTitle.setText(title);
            final Dialog checkCoupenPriceDialog = new Dialog(itemView.getContext());
            checkCoupenPriceDialog.setContentView(R.layout.coupen_redeem_dialog);
            checkCoupenPriceDialog.setCancelable(true);
            checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (COD){
                codIndicator.setVisibility(View.VISIBLE);
            }else {
                codIndicator.setVisibility(View.INVISIBLE);
            }
            if (inStock) {

                if (freeCouponsNo > 0) {
                    freecouponIcon.setVisibility(View.VISIBLE);
                    freeCoupons.setVisibility(View.VISIBLE);
                    if (freeCouponsNo == 1) {
                        freeCoupons.setText("free " + freeCouponsNo + " coupon");
                    } else {
                        freeCoupons.setText("free " + freeCouponsNo + " coupons");
                    }
                } else {
                    freecouponIcon.setVisibility(View.INVISIBLE);
                    freeCoupons.setVisibility(View.INVISIBLE);
                }
                productPrice.setText("Rs." + productpricetext + "/-");
                productPrice.setTextColor(Color.parseColor("#000000"));
                cuttedPrice.setText("Rs." + cuttedpricetext + "/-");
                coupenRedeemLayout.setVisibility(View.VISIBLE);
                //////////coupen dialog

                ImageView toogleRecyclerview = checkCoupenPriceDialog.findViewById(R.id.toogle_recyclerview);
                coupensRecyclerview = checkCoupenPriceDialog.findViewById(R.id.coupens_recyclerview);
                selectedCoupen = checkCoupenPriceDialog.findViewById(R.id.selected_coupen);
                coupenTitle = checkCoupenPriceDialog.findViewById(R.id.coupen_title);
                coupenExpiryDate = checkCoupenPriceDialog.findViewById(R.id.coupen_validity);
                coupenBody = checkCoupenPriceDialog.findViewById(R.id.coupen_body);
                removerCoupenBtn = checkCoupenPriceDialog.findViewById(R.id.remove_btnn);
                applyCoupenBtn = checkCoupenPriceDialog.findViewById(R.id.apply_btnn);
                footerText = checkCoupenPriceDialog.findViewById(R.id.footer_text);
                applyORremoveBtnContainer = checkCoupenPriceDialog.findViewById(R.id.apply_or_remove_btns_container);


                originalPrice = checkCoupenPriceDialog.findViewById(R.id.original_price);
                discountedPrice = checkCoupenPriceDialog.findViewById(R.id.discounted_price);


                LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                coupensRecyclerview.setLayoutManager(layoutManager);

                originalPrice.setText(productPrice.getText());
                productOriginalPrice = productpricetext;
                MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(position, DBquerries.rewardModelList, true, coupensRecyclerview, selectedCoupen, productOriginalPrice, coupenTitle, coupenExpiryDate, coupenBody, discountedPrice, cartItemModelList);
                coupensRecyclerview.setAdapter(myRewardsAdapter);
                myRewardsAdapter.notifyDataSetChanged();

//

                applyCoupenBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCoupenId())) {
                            for (RewardModel rewardModel : DBquerries.rewardModelList) {
                                if (rewardModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                                    rewardModel.setAlreadyUsed(true);


                                    coupenRedeemLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reward_gradient));
                                    coupenRedeemBody.setText(rewardModel.getCoupenBody());
                                    redeemBtn.setText("Coupen");
                                }
                            }
                            couponsApplied.setVisibility(View.VISIBLE);
                            cartItemModelList.get(position).setDiscountedPrice(discountedPrice.getText().toString().substring(3, discountedPrice.getText().length() - 2));
                            productPrice.setText(discountedPrice.getText());
                            String offerDiscountedAmt = String.valueOf(Long.valueOf(productpricetext) - Long.valueOf(discountedPrice.getText().toString().substring(3, discountedPrice.getText().length() - 2)));
                            couponsApplied.setText("Coupen applied -Rs." + offerDiscountedAmt + "/-");
                            notifyItemChanged(cartItemModelList.size() - 1);
                            checkCoupenPriceDialog.dismiss();
                        }
                    }
                });
                removerCoupenBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (RewardModel rewardModel : DBquerries.rewardModelList) {
                            if (rewardModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                                rewardModel.setAlreadyUsed(false);
                            }
                        }
                        coupenTitle.setText("Coupen");
                        coupenExpiryDate.setText("validity");
                        coupenBody.setText("Tap the icon on the top right corner to select your coupen.");
                        couponsApplied.setVisibility(View.INVISIBLE);
                        coupenRedeemLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorOrange));
                        coupenRedeemBody.setText("Apply your coupen here.");
                        redeemBtn.setText("Redeem");
                        cartItemModelList.get(position).setSelectedCoupenId(null);
                        productPrice.setText("Rs." + productpricetext + "/-");
                        notifyItemChanged(cartItemModelList.size() - 1);
                        checkCoupenPriceDialog.dismiss();
                    }
                });
                toogleRecyclerview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialogRecyclerView();
                    }
                });


                if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCoupenId())) {
                    for (RewardModel rewardModel : DBquerries.rewardModelList) {
                        if (rewardModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                            coupenRedeemLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reward_gradient));
                            coupenRedeemBody.setText(rewardModel.getCoupenBody());
                            redeemBtn.setText("Coupen");
                            coupenBody.setText(rewardModel.getCoupenBody());
                            coupenTitle.setText(rewardModel.getType());

                            if (rewardModel.getType().equals("Discount")) {
                                coupenTitle.setText(rewardModel.getType());
                            } else {
                                coupenTitle.setText("FLAT Rs." + rewardModel.getDiscORamt() + " OFF");
                            }
                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM YYYY");
                            coupenExpiryDate.setText("till " + simpleDateFormat.format(rewardModel.getTimestamp()));
                        }
                    }
                    discountedPrice.setText("Rs." + cartItemModelList.get(position).getDiscountedPrice() + "/-");
                    couponsApplied.setVisibility(View.VISIBLE);
                    productPrice.setText("Rs." + cartItemModelList.get(position).getDiscountedPrice() + "/-");
                    String offerDiscountedAmt = String.valueOf(Long.valueOf(productpricetext) - Long.valueOf(cartItemModelList.get(position).getDiscountedPrice()));
                    couponsApplied.setText("Coupen applied -Rs." + offerDiscountedAmt + "/-");

                } else {
                    couponsApplied.setVisibility(View.INVISIBLE);
                    coupenRedeemLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorOrange));
                    coupenRedeemBody.setText("Apply your coupen here.");
                    redeemBtn.setText("Redeem");
                }
                ///////////coupen dialog
                productQty.setText("Qty: " + proQty);
                if (!showDeleteBtn) {
                    if (QtyError) {
                        productQty.setTextColor(itemView.getContext().getResources().getColor(R.color.BtnBlue));
                        productQty.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.BtnBlue)));

                    } else {
                        productQty.setTextColor(itemView.getContext().getResources().getColor(R.color.colorBlack));
                        productQty.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorBlack)));
                    }
                }

                productQty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog quantityDialog = new Dialog(itemView.getContext());
                        quantityDialog.setContentView(R.layout.quantity_dialog);
                        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        quantityDialog.setCancelable(false);
                        final EditText quantityNo = quantityDialog.findViewById(R.id.quantity_no);
                        Button cancelbtn = quantityDialog.findViewById(R.id.cancel_btn);
                        Button okbtn = quantityDialog.findViewById(R.id.ok_btn);
                        quantityNo.setHint("Max " + String.valueOf(maxQty));

                        cancelbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                quantityDialog.dismiss();
                            }
                        });
                        okbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!TextUtils.isEmpty(quantityNo.getText())) {
                                    if (Long.valueOf(quantityNo.getText().toString()) <= maxQty && Long.valueOf(quantityNo.getText().toString()) != 0) {
                                        if (itemView.getContext() instanceof MainActivity) {
                                            cartItemModelList.get(position).setProductQty(Long.valueOf(quantityNo.getText().toString()));

                                        } else {


                                            if (DeliveryActivity.fromCart) {
                                                cartItemModelList.get(position).setProductQty(Long.valueOf(quantityNo.getText().toString()));
                                            } else {
                                                DeliveryActivity.cartItemModelList.get(position).setProductQty(Long.valueOf(quantityNo.getText().toString()));
                                            }
                                            productQty.setText("Qty: " + quantityNo.getText());
                                            notifyItemChanged(cartItemModelList.size() - 1);

                                            if (!showDeleteBtn) {
                                                DeliveryActivity.loadingDialog.show();
                                                DeliveryActivity.cartItemModelList.get(position).setQtyError(false);
                                                final int initialQty = Integer.parseInt(proQty);
                                                final int finalQty = Integer.parseInt(quantityNo.getText().toString());
                                                final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                                if (finalQty > initialQty) {
                                                    for (int y = 0; y < (finalQty - initialQty); y++) {
                                                        final String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);
                                                        Map<String, Object> timestamp = new HashMap<>();
                                                        timestamp.put("time", FieldValue.serverTimestamp());
                                                        final int finalY = y;
                                                        firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(quantityDocumentName).
                                                                set(timestamp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                qtyIds.add(quantityDocumentName);

                                                                if (finalY + 1 == finalQty - initialQty) {
                                                                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY")
                                                                            .orderBy("time", Query.Direction.ASCENDING).limit(stockQty).get()
                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        List<String> serverQuantity = new ArrayList<>();
                                                                                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                                            serverQuantity.add(queryDocumentSnapshot.getId());
                                                                                        }
                                                                                        long availableQty = 0;
                                                                                        for (String qtyId : qtyIds) {

                                                                                            if (!serverQuantity.contains(qtyId)) {
                                                                                                DeliveryActivity.cartItemModelList.get(position).setQtyError(true);
                                                                                                DeliveryActivity.cartItemModelList.get(position).setMaxQty(availableQty);
                                                                                                Toast.makeText(itemView.getContext(), "Sorry ! all products may not be available in required quantity...", Toast.LENGTH_SHORT).show();

                                                                                            } else {
                                                                                                availableQty++;
                                                                                            }

                                                                                        }
                                                                                        DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                                    } else {
                                                                                        /////////error
                                                                                        String error = task.getException().getMessage();
                                                                                        Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                    DeliveryActivity.loadingDialog.dismiss();
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                                    }
                                                } else if (initialQty > finalQty) {
                                                    for (int x = 0; x < initialQty - finalQty; x++) {
                                                        final String qtyId = qtyIds.get(qtyIds.size() - 1 - x);
                                                        final int finalX = x;
                                                        firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(qtyId).delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        qtyIds.remove(qtyId);
                                                                        DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                        if (finalX + 1 == initialQty - finalQty) {
                                                                            DeliveryActivity.loadingDialog.dismiss();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }

                                            }
                                        }
                                    } else {
                                        Toast.makeText(itemView.getContext(), "Max quantity :" + maxQty.toString(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                                quantityDialog.dismiss();
                            }
                        });
                        quantityDialog.show();
                    }
                });
                if (offersAppliedNo > 0) {
                    offerApplied.setVisibility(View.VISIBLE);
                    String offerDiscountedAmt = String.valueOf(Long.valueOf(cuttedpricetext) - Long.valueOf(productpricetext));
                    offerApplied.setText("Offer applied -Rs." + offerDiscountedAmt + "/-");
                } else {
                    offerApplied.setVisibility(View.INVISIBLE);
                }

            } else {
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.BtnBlue));
                cuttedPrice.setText(" ");
                coupenRedeemLayout.setVisibility(View.GONE);
                freeCoupons.setVisibility(View.INVISIBLE);
                productQty.setVisibility(View.INVISIBLE);
                couponsApplied.setVisibility(View.GONE);
                offerApplied.setVisibility(View.GONE);
                freecouponIcon.setVisibility(View.INVISIBLE);
            }


            if (showDeleteBtn) {
                removeBtn.setVisibility(View.VISIBLE);
            } else {
                removeBtn.setVisibility(View.GONE);
            }

            redeemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (RewardModel rewardModel : DBquerries.rewardModelList) {
                        if (rewardModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                            rewardModel.setAlreadyUsed(false);
                        }
                    }
                    checkCoupenPriceDialog.show();
                }
            });
            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCoupenId())) {
                        for (RewardModel rewardModel : DBquerries.rewardModelList) {
                            if (rewardModel.getCoupenId().equals(cartItemModelList.get(position).getSelectedCoupenId())) {
                                rewardModel.setAlreadyUsed(false);
                            }
                        }
                    }
                    if (!ProductDetailsActivity.running_cart_query) {
                        ProductDetailsActivity.running_cart_query = true;
                        DBquerries.removeFromCart(position, itemView.getContext(), cartTotalAmount);
                    }
                }
            });


        }

        private void showDialogRecyclerView() {
            if (coupensRecyclerview.getVisibility() == View.GONE) {
                coupensRecyclerview.setVisibility(View.VISIBLE);
                selectedCoupen.setVisibility(View.GONE);
            } else {
                coupensRecyclerview.setVisibility(View.GONE);
                selectedCoupen.setVisibility(View.VISIBLE);

            }
        }
    }

    private TextView totalItems, totalItemsPrice, deliveryPrice, totalAmount, savedAmount;

    class cartTotalAmountViewHolder extends RecyclerView.ViewHolder {
        public cartTotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);
            totalItems = itemView.findViewById(R.id.total_items);
            totalItemsPrice = itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
            savedAmount = itemView.findViewById(R.id.saved_amount);

        }

        private void setTotalAmount(int totalItemText, int totalItemPriceText, String deliverypriceText, int totalAmountText, int savedAmountText) {
            totalItems.setText("Price(" + totalItemText + ")");
            totalItemsPrice.setText("Rs." + totalItemPriceText + "/-");
            if (deliverypriceText.equals("FREE")) {
                deliveryPrice.setText(deliverypriceText);
            } else {
                deliveryPrice.setText("Rs." + deliverypriceText + "/-");
            }

            totalAmount.setText("Rs." + totalAmountText + "/-");
            savedAmount.setText("You saved Rs." + savedAmountText + "/- on this order.");
            cartTotalAmount.setText("Rs." + totalAmountText + "/-");

            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
            if (totalItemPriceText == 0) {
                if (DeliveryActivity.fromCart) {
                    cartItemModelList.remove(cartItemModelList.size() - 1);
                    DeliveryActivity.cartItemModelList.remove(DeliveryActivity.cartItemModelList.size() - 1);
                }
                if (showDeleteBtn) {
                    cartItemModelList.remove(cartItemModelList.size() - 1);
                }
                parent.setVisibility(View.GONE);
            } else {
                parent.setVisibility(View.VISIBLE);
            }
        }
    }
}
