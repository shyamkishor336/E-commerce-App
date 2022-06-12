package com.skinfotech.onlinedeliveryliquor;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.skinfotech.onlinedeliveryliquor.DeliveryActivity.SELECT_ADDRESS;
import static com.skinfotech.onlinedeliveryliquor.MyAccountFragment.MANAGE_ADDRESS;
import static com.skinfotech.onlinedeliveryliquor.MyAddressActivity.refreshItem;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private List<AddressModel> addressModelList;
    private int MODE;
    private int preSelectedPosition;
    private boolean refresh = false;
    private Dialog loadingDialog;

    public AddressAdapter(List<AddressModel> addressModelList, int MODE, Dialog loadingDialog) {
        this.addressModelList = addressModelList;
        this.MODE = MODE;
        preSelectedPosition = DBquerries.selectedAddress;
        this.loadingDialog = loadingDialog;

    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {

        String city = addressModelList.get(position).getCity();
        String locality = addressModelList.get(position).getLocality();
        String flatNo = addressModelList.get(position).getFlatNo();
        String pincode = addressModelList.get(position).getPinCode();
        String landmark = addressModelList.get(position).getLandmark();
        String name = addressModelList.get(position).getName();
        String mobileNo = addressModelList.get(position).getMobileNo();
        String alternateMobileNo = addressModelList.get(position).getAlternateMobileNo();
        String state = addressModelList.get(position).getState();
        boolean selected = addressModelList.get(position).getSelected();

        holder.setData(name, city, pincode, selected, position, mobileNo, flatNo, locality, landmark, state, alternateMobileNo);
    }

    @Override
    public int getItemCount() {
        return addressModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView fullName, address, pincode;
        private ImageView icon;
        private LinearLayout optionContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            pincode = itemView.findViewById(R.id.pincode);
            icon = itemView.findViewById(R.id.icon_view);
            optionContainer = itemView.findViewById(R.id.option_container);
        }

        private void setData(String username, String city, String userPincode, Boolean selected, final int position, String mobileNo, String flatNo, String locality, String landmark, String state, String alternateMobileNo) {
            if (alternateMobileNo.equals("")) {

                fullName.setText(username + " - " + mobileNo);
            } else {
                fullName.setText(username + " - " + mobileNo + " or " + alternateMobileNo);

            }
            if (landmark.equals("")) {
                address.setText(flatNo + " " + locality + " " + " " + city + " " + state);
            } else {
                address.setText(flatNo + " " + locality + " " + landmark + " " + city + " " + state);
            }
            pincode.setText(userPincode);
            if (MODE == SELECT_ADDRESS) {
                icon.setImageResource(R.drawable.check);
                if (selected) {
                    icon.setVisibility(View.VISIBLE);
                    preSelectedPosition = position;
                } else {
                    icon.setVisibility(View.INVISIBLE);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (preSelectedPosition != position) {
                            addressModelList.get(position).setSelected(true);
                            addressModelList.get(preSelectedPosition).setSelected(false);
                            refreshItem(preSelectedPosition, position);
                            preSelectedPosition = position;
                            DBquerries.selectedAddress = position;
                        }
                    }
                });
            } else if (MODE == MANAGE_ADDRESS) {

                optionContainer.setVisibility(View.GONE);
                optionContainer.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//////////edit address
                        Intent addAddressIntent = new Intent(itemView.getContext(), AddAddressActivity.class);
                        addAddressIntent.putExtra("INTENT", "update_address");
                        addAddressIntent.putExtra("index", position);
                        itemView.getContext().startActivity(addAddressIntent);
                        refresh = false;
                    }
                });
                optionContainer.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//////remove address
                        loadingDialog.show();
                        Map<String, Object> addresses = new HashMap<>();
                        int x = 0;
                        int selected = -1;

                        for (int i = 0; i < addressModelList.size(); i++) {
                            if (i != position) {
                                x++;
                                addresses.put("city_" + x, addressModelList.get(i).getCity());
                                addresses.put("locality_" + x, addressModelList.get(i).getLocality());
                                addresses.put("flat_no_" + x, addressModelList.get(i).getFlatNo());
                                addresses.put("pincode_" + x, addressModelList.get(i).getPinCode());
                                addresses.put("landmark_" + x, addressModelList.get(i).getLandmark());
                                addresses.put("name_" + x, addressModelList.get(i).getName());
                                addresses.put("mobile_no_" + x, addressModelList.get(i).getMobileNo());
                                addresses.put("alternate_mobile_no_" + x, addressModelList.get(i).getAlternateMobileNo());
                                addresses.put("state_" + x, addressModelList.get(i).getState());
                                if (addressModelList.get(position).getSelected()) {
                                    if (position - 1 >= 0) {
                                        if (x == position) {
                                            addresses.put("selected_" + x, true);
                                            selected = x;
                                        }else {
                                            addresses.put("selected_" + x, addressModelList.get(i).getSelected());

                                        }
                                    } else {
                                        if (x == 1) {
                                            addresses.put("selected_" + x, true);
                                            selected = x;
                                        }else {
                                            addresses.put("selected_" + x, addressModelList.get(i).getSelected());

                                        }
                                    }
                                } else {
                                    addresses.put("selected_" + x, addressModelList.get(i).getSelected());
                                        if (addressModelList.get(i).getSelected()){
                                            selected =x;
                                        }
                                }
                            }
                        }
                        addresses.put("list_size", x);
                        final int finalSelected = selected;
                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES")
                                .set(addresses).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    DBquerries.addressModelList.remove(position);
                                    if (finalSelected != -1) {
                                        DBquerries.selectedAddress = finalSelected - 1;
                                        DBquerries.addressModelList.get(finalSelected - 1).setSelected(true);
                                    }else if (DBquerries.addressModelList.size()==0){
                                        DBquerries.selectedAddress =-1;
                                    }
                                    notifyDataSetChanged();
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                }
                                loadingDialog.dismiss();
                            }
                        });

                        refresh = false;
                    }
                });
                icon.setImageResource(R.drawable.vertical_dots);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        optionContainer.setVisibility(View.VISIBLE);
                        if (refresh) {

                            refreshItem(preSelectedPosition, preSelectedPosition);
                        } else {
                            refresh = true;
                        }
                        preSelectedPosition = position;

                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        refreshItem(preSelectedPosition, preSelectedPosition);
                        preSelectedPosition = -1;
                    }
                });
            }

        }
    }
}
