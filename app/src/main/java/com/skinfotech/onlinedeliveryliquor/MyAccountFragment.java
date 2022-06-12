package com.skinfotech.onlinedeliveryliquor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {


    public MyAccountFragment() {
        // Required empty public constructor
    }

    private Button viewAllAddressBtn, signOutBtn;
    public static final int MANAGE_ADDRESS = 1;
    private CircleImageView profileView, currentOrderImage;
    private TextView name, email;
    private LinearLayout layoutContainer, recentOrderContainer;
    private Dialog loadingDialog;
    private TextView tvCurrentOrderStatus;
    private ImageView orderedIndicator, packedIndicator, shippedIndicator, deliveredIndicator;
    private ProgressBar O_P_progress, P_S_progress, S_D_progress;
    private TextView yourRecentOrderTitle;
    private TextView addressName, address, pincode;
    private FloatingActionButton settingsBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);


        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //loadingDialog.show();

        viewAllAddressBtn = view.findViewById(R.id.view_all_addresses_btn);
        profileView = view.findViewById(R.id.current_profile_image);
        name = view.findViewById(R.id.user_name);
        email = view.findViewById(R.id.user_email);
        layoutContainer = view.findViewById(R.id.layout_container);
        currentOrderImage = view.findViewById(R.id.current_order_image);
        tvCurrentOrderStatus = view.findViewById(R.id.current_order_status);
        orderedIndicator = view.findViewById(R.id.ordered_indicatored);
        packedIndicator = view.findViewById(R.id.packed_indicatored);
        shippedIndicator = view.findViewById(R.id.shipped_indicatored);
        deliveredIndicator = view.findViewById(R.id.delivered_indicatored);
        O_P_progress = view.findViewById(R.id.order_packed_progress);
        P_S_progress = view.findViewById(R.id.packed_shipped_progress);
        S_D_progress = view.findViewById(R.id.shipping_delivered_progress);
        yourRecentOrderTitle = view.findViewById(R.id.your_recent_orders_title);
        recentOrderContainer = view.findViewById(R.id.recent_order_container);
        addressName = view.findViewById(R.id.ac_address_fullname);
        address = view.findViewById(R.id.adddress);
        pincode = view.findViewById(R.id.address_pincodee);
        signOutBtn = view.findViewById(R.id.sign_out_btn);
        settingsBtn = view.findViewById(R.id.setting_btn);

        layoutContainer.getChildAt(1).setVisibility(View.GONE);
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                for (MyOrderItemModel orderItemModel : DBquerries.myOrderItemModelList) {
                    if (!orderItemModel.isCancellationRequested()) {
                        if (!orderItemModel.getDelveryStatus().equals("Delivered") && !orderItemModel.getDelveryStatus().equals("Cancelled")) {
                            layoutContainer.getChildAt(1).setVisibility(View.VISIBLE);
                            Glide.with(getContext()).load(orderItemModel.getProductImage()).apply(new RequestOptions().placeholder(R.drawable.icon_placeholder)).into(currentOrderImage);
                            tvCurrentOrderStatus.setText(orderItemModel.getDelveryStatus());
                            switch (orderItemModel.getDelveryStatus()) {
                                case "Ordered":
                                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                    break;
                                case "Packed":
                                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                    O_P_progress.setProgress(100);
                                    break;
                                case "Shipped":
                                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                    shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                    O_P_progress.setProgress(100);
                                    P_S_progress.setProgress(100);
                                    break;
                                case "Out for Delivery":
                                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                    shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                    deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.SuccessGreen)));
                                    O_P_progress.setProgress(100);
                                    P_S_progress.setProgress(100);
                                    S_D_progress.setProgress(100);
                                    break;

                            }
                        }
                    }
                }
                int i = 0;
                for (MyOrderItemModel myOrderItemModel : DBquerries.myOrderItemModelList) {
                    if (i < 4) {
                        if (myOrderItemModel.getDelveryStatus().equals("Delivered")) {
                            Glide.with(getContext()).load(myOrderItemModel.getProductImage()).apply(new RequestOptions().placeholder(R.drawable.icon_placeholder)).into((CircleImageView) recentOrderContainer.getChildAt(i));
                            i++;
                        }
                    } else {
                        break;
                    }
                }
                if (i == 0) {
                    yourRecentOrderTitle.setText("No recent orders");
                }
                if (i < 3) {
                    for (int x = i; x < 4; x++) {
                        recentOrderContainer.getChildAt(x).setVisibility(View.GONE);

                    }
                }
                loadingDialog.show();
                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        loadingDialog.setOnDismissListener(null);
                        if (DBquerries.addressModelList.size() == 0) {
                            addressName.setText("No Address");
                            address.setText("-");
                            pincode.setText("-");
                        } else {
                            setAddress();
                        }
                    }
                });
                DBquerries.loadAddresses(getContext(), loadingDialog, false);

            }
        });
        DBquerries.loadOrders(getContext(), null, loadingDialog);
        viewAllAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myaddressIntent = new Intent(getContext(), MyAddressActivity.class);
                myaddressIntent.putExtra("MODE", MANAGE_ADDRESS);
                startActivity(myaddressIntent);
            }
        });
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                DBquerries.clearData();
                Intent registerIntent = new Intent(getContext(), RegisterActivity.class);
                startActivity(registerIntent);
                getActivity().finish();
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateUserInfo = new Intent(getContext(), UpdateUserInfoActivity.class);
                updateUserInfo.putExtra("Name", name.getText());
                updateUserInfo.putExtra("Email", email.getText());
                updateUserInfo.putExtra("Photo", DBquerries.profile);
                startActivity(updateUserInfo);

            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        name.setText(DBquerries.fullName);
        email.setText(DBquerries.email);

        if (!DBquerries.profile.equals("")) {
            Glide.with(getContext()).load(DBquerries.profile).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(profileView);
        }else {
            profileView.setImageResource(R.drawable.icon_placeholder);
        }
        if (!loadingDialog.isShowing()) {
            if (DBquerries.addressModelList.size() == 0) {
                addressName.setText("No Address");
                address.setText("-");
                pincode.setText("-");
            } else {
                setAddress();
            }
        }
    }

    private void setAddress() {
        String nametext, mobileNo;
        nametext = DBquerries.addressModelList.get(DBquerries.selectedAddress).getName();
        mobileNo = DBquerries.addressModelList.get(DBquerries.selectedAddress).getMobileNo();
        if (DBquerries.addressModelList.get(DBquerries.selectedAddress).getAlternateMobileNo().equals("")) {

            addressName.setText(nametext + " - " + mobileNo);
        } else {
            addressName.setText(nametext + " - " + mobileNo + " or " + (DBquerries.addressModelList.get(DBquerries.selectedAddress).getAlternateMobileNo()));

        }
        String flatNo = DBquerries.addressModelList.get(DBquerries.selectedAddress).getFlatNo();
        String locality = DBquerries.addressModelList.get(DBquerries.selectedAddress).getLocality();
        String landmark = DBquerries.addressModelList.get(DBquerries.selectedAddress).getLandmark();
        String city = DBquerries.addressModelList.get(DBquerries.selectedAddress).getMobileNo();
        String state = DBquerries.addressModelList.get(DBquerries.selectedAddress).getCity();
        if (landmark.equals("")) {
            address.setText(flatNo + " " + locality + " " + landmark + " " + city + " " + state);
        } else {
            address.setText(flatNo + " " + locality + " " + landmark + " " + city + " " + state);
        }
        pincode.setText(DBquerries.addressModelList.get(DBquerries.selectedAddress).getPinCode());
    }
}