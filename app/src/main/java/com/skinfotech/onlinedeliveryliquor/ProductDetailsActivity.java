package com.skinfotech.onlinedeliveryliquor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.skinfotech.onlinedeliveryliquor.MainActivity.showCart;
import static com.skinfotech.onlinedeliveryliquor.RegisterActivity.setSignUpFragment;

public class ProductDetailsActivity extends AppCompatActivity {


    public static boolean running_wishlist_query = false;
    public static boolean running_cart_query = false;
    public static boolean running_rating_quary = false;

    public static boolean fromSerch = false;

    private ViewPager productImagesViewPager;
    private TextView productTitle, productPrice, cuttedPrice;
    private String productOriginalPrice;
    private TextView averageRatingMiniview, totalRatingMiniView;
    private TabLayout viewPagerIndicator;
    private ImageView codIndicator;
    private TextView tvCodIndicator;
    public static MenuItem cartItem;
    private Dialog signInDialog;
    private Dialog loadingDialog;
    private TextView rewardTitle;
    private TextView badgeCount;
    private TextView rewardBody;
    private LinearLayout coupenRedeemLayout;
    private Button coupenRedeemBtn;
    private Button BuyNowBtn;
    private LinearLayout addToCartButton;
    private boolean inStock = false;

    ///////product description
    private ConstraintLayout productDetailsOnlyContainer;
    private ConstraintLayout productDetailsTabsContainer;
    private ViewPager productDetailsViewpager;
    private TabLayout productDetailsTablayout;
    private TextView productOnlyDescriptionBody;
    private String productDescription;
    private String productOtherDetails;
    public static Activity productDetailsActivity;

    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();
    ///////product description
    private FirebaseFirestore firebaseFirestore;
    public static FloatingActionButton addtowishListBtn;
    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static boolean ALREADY_ADDED_TO_CART = false;


    ///////////coupen dialog
    private TextView coupenTitle;
    private TextView coupenExpiryDate;
    private TextView coupenBody;
    private RecyclerView coupensRecyclerview;
    private LinearLayout selectedCoupen;
    private TextView discountedPrice, originalPrice;
    /////////coupen dialog

    private FirebaseUser firebaseUser;
    public static String productID;
    private DocumentSnapshot documentSnapshot;

    ////////rating layout
    public static int initialRating;
    public static LinearLayout rateNowContainer;
    private TextView totalRatings;
    private LinearLayout ratingsNoContainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressbarContainer;
    private TextView averageRating;
    ////////rating layout


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productTitle = findViewById(R.id.product_title);
        averageRatingMiniview = findViewById(R.id.tv_product_rating_miniview);
        totalRatingMiniView = findViewById(R.id.total_ratings_miniview);
        productImagesViewPager = findViewById(R.id.product_images_viewpager);
        productPrice = findViewById(R.id.product_price);
        rewardTitle = findViewById(R.id.reward_title);

        rewardBody = findViewById(R.id.reward_detail);
        addToCartButton = findViewById(R.id.add_to_cart_button);
        tvCodIndicator = findViewById(R.id.cod_indicator_text);
        codIndicator = findViewById(R.id.cod_indicator_image);
        cuttedPrice = findViewById(R.id.cutted_price);
        viewPagerIndicator = findViewById(R.id.viewpager_indicator);
        addtowishListBtn = findViewById(R.id.add_to_wishlist_btn);
        productDetailsViewpager = findViewById(R.id.product_details_viewpager);
        productDetailsTablayout = findViewById(R.id.product_details_tablayout);
        coupenRedeemBtn = findViewById(R.id.coupon_redeem_button);
        productDetailsTabsContainer = findViewById(R.id.product_details_tabs_container);
        productDetailsOnlyContainer = findViewById(R.id.product_details_container);
        productOnlyDescriptionBody = findViewById(R.id.product_details_body);
        coupenRedeemLayout = findViewById(R.id.coupon_redeem_layout);
        totalRatings = findViewById(R.id.total_ratings);
        ratingsNoContainer = findViewById(R.id.ratings_number_container);
        totalRatingsFigure = findViewById(R.id.total_ratings_figure);
        ratingsProgressbarContainer = findViewById(R.id.ratings_progressbar_container);
        averageRating = findViewById(R.id.average_rating);
        initialRating = -1;

        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
///////////coupen dialog
        final Dialog checkCoupenPriceDialog = new Dialog(ProductDetailsActivity.this);
        checkCoupenPriceDialog.setContentView(R.layout.coupen_redeem_dialog);
        checkCoupenPriceDialog.setCancelable(true);
        checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toogleRecyclerview = checkCoupenPriceDialog.findViewById(R.id.toogle_recyclerview);
        coupensRecyclerview = checkCoupenPriceDialog.findViewById(R.id.coupens_recyclerview);
        selectedCoupen = checkCoupenPriceDialog.findViewById(R.id.selected_coupen);
        coupenTitle = checkCoupenPriceDialog.findViewById(R.id.coupen_title);
        coupenExpiryDate = checkCoupenPriceDialog.findViewById(R.id.coupen_validity);
        coupenBody = checkCoupenPriceDialog.findViewById(R.id.coupen_body);

        originalPrice = checkCoupenPriceDialog.findViewById(R.id.original_price);
        discountedPrice = checkCoupenPriceDialog.findViewById(R.id.discounted_price);


        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        coupensRecyclerview.setLayoutManager(layoutManager);
//


        toogleRecyclerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogRecyclerView();
            }
        });
        ///////////coupen dialog

        BuyNowBtn = findViewById(R.id.buy_now_button);
        firebaseFirestore = FirebaseFirestore.getInstance();

        final List<String> productImages = new ArrayList<>();

        productID = getIntent().getStringExtra("PRODUCT_ID");
        firebaseFirestore.collection("PRODUCTS").document(productID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();
                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY")
                            .orderBy("time", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (int x = 1; x < (Long) documentSnapshot.get("no_of_product_images")+1; x++) {
                                    productImages.add(documentSnapshot.get("product_image_" + x).toString());
                                }
                                ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                                productImagesViewPager.setAdapter(productImagesAdapter);
                                productTitle.setText(documentSnapshot.get("product_title").toString());
                                averageRatingMiniview.setText(documentSnapshot.get("average_rating").toString());
                                totalRatingMiniView.setText("(" + (long) documentSnapshot.get("total_ratings") + ") Ratings");
                                productPrice.setText("Rs." + documentSnapshot.get("product_price").toString() + "/-");
                                ///////for coupen dialog
                                originalPrice.setText(productPrice.getText());
                                productOriginalPrice = documentSnapshot.get("product_price").toString();
                                MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(DBquerries.rewardModelList, true, coupensRecyclerview, selectedCoupen, productOriginalPrice, coupenTitle, coupenExpiryDate, coupenBody, discountedPrice);
                                coupensRecyclerview.setAdapter(myRewardsAdapter);
                                myRewardsAdapter.notifyDataSetChanged();
                                ////////for coupen dialog
                                cuttedPrice.setText("Rs." + documentSnapshot.get("cutted_price").toString() + "/-");
                                if ((boolean) documentSnapshot.get("COD")) {
                                    codIndicator.setVisibility(View.VISIBLE);
                                    tvCodIndicator.setVisibility(View.VISIBLE);
                                } else {
                                    codIndicator.setVisibility(View.INVISIBLE);
                                    tvCodIndicator.setVisibility(View.INVISIBLE);
                                }
                                rewardTitle.setText((long) documentSnapshot.get("free_coupens") + " " + documentSnapshot.get("free_coupen_title").toString());
                                rewardBody.setText(documentSnapshot.get("free_coupen_body").toString());

                                if ((boolean) documentSnapshot.get("use_tab_layout")) {
                                    productDetailsTabsContainer.setVisibility(View.VISIBLE);
                                    productDetailsOnlyContainer.setVisibility(View.GONE);
                                    productDescription = documentSnapshot.get("product_description").toString();

                                    productOtherDetails = documentSnapshot.get("product_other_details").toString();

                                    for (long x = 1; x < (long) documentSnapshot.get("total_spec_titles") + 1; x++) {
                                        productSpecificationModelList.add(new ProductSpecificationModel(0, documentSnapshot.get("spec_title_" + x).toString()));
                                        for (long y = 1; y < (long) documentSnapshot.get("spec_title_" + x + "_total_fields") + 1; y++) {
                                            productSpecificationModelList.add(new ProductSpecificationModel(1, documentSnapshot.get("spec_title_" + x + "_field_" + y + "_name").toString(), documentSnapshot.get("spec_title_" + x + "_field_" + y + "_value").toString()));
                                        }
                                    }
                                } else {
                                    productDetailsTabsContainer.setVisibility(View.GONE);
                                    productDetailsOnlyContainer.setVisibility(View.VISIBLE);
                                    productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
                                }
                                totalRatings.setText((long) documentSnapshot.get("total_ratings") + " ratings");
                                for (int x = 0; x < 5; x++) {
                                    TextView rating = (TextView) ratingsNoContainer.getChildAt(x);
                                    rating.setText(String.valueOf((long) documentSnapshot.get((5 - x) + "_star")));
                                    ProgressBar progressBar = (ProgressBar) ratingsProgressbarContainer.getChildAt(x);
                                    int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")));
                                    progressBar.setMax(maxProgress);
                                    progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - x) + "_star"))));
                                }
                                totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings")));
                                averageRating.setText(documentSnapshot.get("average_rating").toString());
                                productDetailsViewpager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(),
                                        productDetailsTablayout.getTabCount(), productDescription, productOtherDetails, productSpecificationModelList));


                                if (firebaseUser != null) {
                                    if (DBquerries.myRating.size() == 0) {
                                        DBquerries.loadRatingList(ProductDetailsActivity.this);
                                    }
                                    if (DBquerries.cartList.size() == 0) {
                                        DBquerries.loadCartList(ProductDetailsActivity.this, loadingDialog, false, badgeCount, new TextView(ProductDetailsActivity.this));
                                    }
                                    if (DBquerries.wishList.size() == 0) {
                                        DBquerries.loadWishList(ProductDetailsActivity.this, loadingDialog, false);
                                    }
                                    if (DBquerries.rewardModelList.size() == 0) {
                                        DBquerries.loadRewards(ProductDetailsActivity.this, loadingDialog, false);
                                    }
                                    if (DBquerries.cartList.size() != 0 && DBquerries.wishList.size() != 0 && DBquerries.rewardModelList.size() != 0) {
                                        loadingDialog.dismiss();
                                    }
                                } else {
                                    loadingDialog.dismiss();
                                }

                                if (DBquerries.myRatedIds.contains(productID)) {
                                    int index = DBquerries.myRatedIds.indexOf(productID);
                                    initialRating = Integer.parseInt(String.valueOf(DBquerries.myRating.get(index))) - 1;
                                    setRating(initialRating);
                                }
                                if (DBquerries.cartList.contains(productID)) {
                                    ALREADY_ADDED_TO_CART = true;

                                } else {
                                    ALREADY_ADDED_TO_CART = false;
                                }
                                if (DBquerries.wishList.contains(productID)) {
                                    ALREADY_ADDED_TO_WISHLIST = true;
                                    addtowishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorOrange));

                                } else {
                                    addtowishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                    ALREADY_ADDED_TO_WISHLIST = false;
                                }
                                if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {
                                    inStock = true;
                                    BuyNowBtn.setVisibility(View.VISIBLE);
                                    addToCartButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (firebaseUser == null) {
                                                signInDialog.show();
                                            } else {
                                                //////add to cart
                                                if (!running_cart_query) {
                                                    running_cart_query = true;
                                                    if (ALREADY_ADDED_TO_CART) {
                                                        running_cart_query = false;
                                                        Toast.makeText(ProductDetailsActivity.this, "Already Added to cart!!", Toast.LENGTH_SHORT).show();
                                                    } else {

                                                        Map<String, Object> addProduct = new HashMap<>();
                                                        addProduct.put("product_ID_" + String.valueOf(DBquerries.cartList.size()), productID);
                                                        addProduct.put("list_size", (long) (DBquerries.cartList.size() + 1));

                                                        firebaseFirestore.collection("USERS").document(firebaseUser.getUid()).collection("USER_DATA").document("MY_CART")
                                                                .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    if (DBquerries.cartItemModelList.size() != 0) {
                                                                        DBquerries.cartItemModelList.add(0, new CartItemModel(documentSnapshot.getBoolean("COD"), CartItemModel.CART_ITEM, productID,
                                                                                documentSnapshot.get("product_image_1").toString()
                                                                                , documentSnapshot.get("product_title").toString(),
                                                                                (long) documentSnapshot.get("free_coupens"),
                                                                                documentSnapshot.get("product_price").toString(),
                                                                                documentSnapshot.get("cutted_price").toString(),
                                                                                (long) 1,
                                                                                (long) documentSnapshot.get("offers_applied"),
                                                                                (long) 0,
                                                                                inStock,
                                                                                (long) documentSnapshot.get("max-quantity"),
                                                                                (long) documentSnapshot.get("stock_quantity")));
                                                                    }
                                                                    ALREADY_ADDED_TO_CART = true;
                                                                    DBquerries.cartList.add(productID);
                                                                    Toast.makeText(ProductDetailsActivity.this, "Product added to cart successfully!", Toast.LENGTH_SHORT).show();
                                                                    invalidateOptionsMenu();
                                                                    running_cart_query = false;
                                                                } else {
                                                                    running_cart_query = false;
                                                                    String error = task.getException().getMessage();
                                                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                    }
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    inStock = false;
                                    BuyNowBtn.setVisibility(View.GONE);
                                    TextView outOfStock = (TextView) addToCartButton.getChildAt(0);
                                    outOfStock.setText("Out of Stock");
                                    outOfStock.setTextColor(getResources().getColor(R.color.BtnBlue));
                                    outOfStock.setCompoundDrawables(null, null, null, null);
                                }
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewPagerIndicator.setupWithViewPager(productImagesViewPager, true);

        addtowishListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseUser == null) {
                    signInDialog.show();
                } else {
                    if (!running_wishlist_query) {
                        running_wishlist_query = true;
                        if (ALREADY_ADDED_TO_WISHLIST) {
                            int index = DBquerries.wishList.indexOf(productID);
                            DBquerries.removeFromWishList(index, ProductDetailsActivity.this);
                            addtowishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#B6B5B5")));
                        } else {
                            addtowishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.BtnBlue));

                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_" + String.valueOf(DBquerries.wishList.size()), productID);
                            addProduct.put("list_size", (long) (DBquerries.wishList.size() + 1));


                            firebaseFirestore.collection("USERS").document(firebaseUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        if (DBquerries.wishListModelList.size() != 0) {
                                            DBquerries.wishListModelList.add(new WishListModel(productID, documentSnapshot.get("product_image_1").toString()
                                                    , documentSnapshot.get("product_title").toString(),
                                                    (Long) documentSnapshot.get("free_coupens"),
                                                    documentSnapshot.get("average_rating").toString(),
                                                    (Long) documentSnapshot.get("total_ratings"),
                                                    documentSnapshot.get("product_price").toString(),
                                                    documentSnapshot.get("cutted_price").toString(),
                                                    (boolean) documentSnapshot.get("COD"),
                                                    inStock));
                                        }
                                        ALREADY_ADDED_TO_WISHLIST = true;
                                        addtowishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorOrange));
                                        DBquerries.wishList.add(productID);
                                        Toast.makeText(ProductDetailsActivity.this, "Product added to wishlist successfully!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        addtowishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));


                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    running_wishlist_query = false;
                                }
                            });

                        }
                    }
                }
            }
        });
        productDetailsViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTablayout));
        productDetailsTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        BuyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (firebaseUser == null) {
                    signInDialog.show();
                } else {
                    DeliveryActivity.fromCart = false;
                    loadingDialog.show();
                    productDetailsActivity = ProductDetailsActivity.this;
                    DeliveryActivity.cartItemModelList = new ArrayList<>();
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(documentSnapshot.getBoolean("COD"), CartItemModel.CART_ITEM, productID,
                            documentSnapshot.get("product_image_1").toString()
                            , documentSnapshot.get("product_title").toString(),
                            (long) documentSnapshot.get("free_coupens"),
                            documentSnapshot.get("product_price").toString(),
                            documentSnapshot.get("cutted_price").toString(),
                            (long) 1,
                            (long) documentSnapshot.get("offers_applied"),
                            (long) 0,
                            inStock,
                            (long) documentSnapshot.get("max-quantity"),
                            (long) documentSnapshot.get("stock_quantity")));
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));


                    if (DBquerries.addressModelList.size() == 0) {
                        DBquerries.loadAddresses(ProductDetailsActivity.this, loadingDialog, true);
                    } else {
                        loadingDialog.dismiss();
                        Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                        startActivity(deliveryIntent);
                    }
                }
            }
        });


        coupenRedeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkCoupenPriceDialog.show();

            }
        });
//////////sign in dialog
        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);
        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SigninFragment.disableCloseBtn = true;
                SignupFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                Intent registerIntent = new Intent(ProductDetailsActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupFragment.disableCloseBtn = true;
                SigninFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                Intent registerIntent = new Intent(ProductDetailsActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
        ////////sign in dialog

        ///////rating layout


        rateNowContainer = findViewById(R.id.rate_now_container);
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            final int starPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (firebaseUser == null) {
                        signInDialog.show();
                    } else {
                        if (starPosition != initialRating) {
                            if (!running_rating_quary) {
                                running_rating_quary = true;
                                setRating(starPosition);
                                Map<String, Object> updateRating = new HashMap<>();
                                if (DBquerries.myRatedIds.contains(productID)) {
                                    TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                    TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);

                                    updateRating.put(initialRating + 1 + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                                    updateRating.put(starPosition + 1 + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) starPosition - initialRating, true));
                                } else {
                                    updateRating.put(starPosition + 1 + "_star", (long) documentSnapshot.get(starPosition + 1 + "_star") + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) starPosition + 1, false));
                                    updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);
                                }
                                firebaseFirestore.collection("PRODUCTS").document(productID)
                                        .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Map<String, Object> myRating = new HashMap<>();
                                            if (DBquerries.myRatedIds.contains(productID)) {

                                                myRating.put("rating_" + DBquerries.myRatedIds.indexOf(productID), (long) starPosition + 1);

                                            } else {

                                                myRating.put("list_size", (long) DBquerries.myRatedIds.size() + 1);
                                                myRating.put("product_ID_" + DBquerries.myRatedIds.size(), productID);
                                                myRating.put("rating_" + DBquerries.myRatedIds.size(), (long) starPosition + 1);
                                            }

                                            firebaseFirestore.collection("USERS").document(firebaseUser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                    .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        if (DBquerries.myRatedIds.contains(productID)) {

                                                            DBquerries.myRating.set(DBquerries.myRatedIds.indexOf(productID), (long) starPosition + 1);
                                                            TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                                            TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                            oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                            finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));


                                                        } else {
                                                            DBquerries.myRatedIds.add(productID);
                                                            DBquerries.myRating.add((long) starPosition + 1);

                                                            TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                            totalRatingMiniView.setText("(" + ((long) documentSnapshot.get("total_ratings") + 1) + ") ratings");
                                                            totalRatings.setText((long) documentSnapshot.get("total_ratings") + 1 + " ratings");
                                                            totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));


                                                            Toast.makeText(ProductDetailsActivity.this, "Thank you for rating...", Toast.LENGTH_SHORT).show();
                                                        }

                                                        for (int x = 0; x < 5; x++) {
                                                            TextView ratingfigures = (TextView) ratingsNoContainer.getChildAt(x);
                                                            ProgressBar progressBar = (ProgressBar) ratingsProgressbarContainer.getChildAt(x);
                                                            int maxProgress = Integer.parseInt(totalRatingsFigure.getText().toString());
                                                            progressBar.setMax(maxProgress);
                                                            progressBar.setProgress(Integer.parseInt(ratingfigures.getText().toString()));
                                                        }
                                                        initialRating = starPosition;
                                                        averageRating.setText(calculateAverageRating((long) 0, true));
                                                        averageRatingMiniview.setText(calculateAverageRating((long) 0, true));

                                                        if (DBquerries.wishList.contains(productID) && DBquerries.wishListModelList.size() != 0) {
                                                            int index = DBquerries.wishList.indexOf(productID);
                                                            DBquerries.wishListModelList.get(index).setRating(averageRating.getText().toString());
                                                            DBquerries.wishListModelList.get(index).setTotalRatings(Long.valueOf(totalRatingsFigure.getText().toString()));
                                                        }
                                                    } else {
                                                        setRating(initialRating);
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                    running_rating_quary = false;
                                                }
                                            });
                                        } else {
                                            running_rating_quary = false;
                                            setRating(initialRating);
                                            String error = task.getException().getMessage();
                                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }

        /////////rating layout


    }

    public static void setRating(int starPosition) {

        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starPosition) {
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FF9800")));
            }

        }
    }

    private String calculateAverageRating(Long currentUserRating, boolean update) {
        Double totalStars = Double.valueOf(0);
        for (int x = 1; x < 6; x++) {
            TextView ratingNo = (TextView) ratingsNoContainer.getChildAt(5 - x);
            totalStars = totalStars + (Long.parseLong(ratingNo.getText().toString()) * x);
        }
        totalStars = totalStars + currentUserRating;
        if (update) {

            return String.valueOf(totalStars / Long.parseLong(totalRatingsFigure.getText().toString())).substring(0, 3);
        } else {
            return String.valueOf(totalStars / (Long.parseLong(totalRatingsFigure.getText().toString()) + 1)).substring(0, 3);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            coupenRedeemLayout.setVisibility(View.GONE);
        } else {
            coupenRedeemLayout.setVisibility(View.VISIBLE);
        }
        if (firebaseUser != null) {
            if (DBquerries.myRating.size() == 0) {
                DBquerries.loadRatingList(ProductDetailsActivity.this);
            }
            if (DBquerries.wishList.size() == 0) {
                DBquerries.loadWishList(ProductDetailsActivity.this, loadingDialog, false);
            }
            if (DBquerries.rewardModelList.size() == 0) {
                DBquerries.loadRewards(ProductDetailsActivity.this, loadingDialog, false);
            }
            if (DBquerries.cartList.size() != 0 && DBquerries.wishList.size() != 0 && DBquerries.rewardModelList.size() != 0) {
                loadingDialog.dismiss();
            }
        } else {
            loadingDialog.dismiss();
        }
        if (DBquerries.myRatedIds.contains(productID)) {
            int index = DBquerries.myRatedIds.indexOf(productID);
            initialRating = Integer.parseInt(String.valueOf(DBquerries.myRating.get(index))) - 1;
            setRating(initialRating);
        }
        if (DBquerries.cartList.contains(productID)) {
            ALREADY_ADDED_TO_CART = true;

        } else {
            ALREADY_ADDED_TO_CART = false;
        }
        if (DBquerries.wishList.contains(productID)) {
            ALREADY_ADDED_TO_WISHLIST = true;
            addtowishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorOrange));

        } else {
            addtowishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
            ALREADY_ADDED_TO_WISHLIST = false;
        }
        invalidateOptionsMenu();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        cartItem = menu.findItem(R.id.main_cart_icon);


        cartItem.setActionView(R.layout.badgelayout);
        ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
        badgeIcon.setImageResource(R.drawable.carted);
        badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);
        if (firebaseUser != null) {
            if (DBquerries.cartList.size() == 0) {
                DBquerries.loadCartList(ProductDetailsActivity.this, loadingDialog, false, badgeCount, new TextView(ProductDetailsActivity.this));
            } else {
                badgeCount.setVisibility(View.VISIBLE);
                if (DBquerries.cartList.size() < 99) {
                    badgeCount.setText(String.valueOf(DBquerries.cartList.size()));
                } else {
                    badgeCount.setText("99+");
                }
            }
        }
        cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseUser == null) {
                    signInDialog.show();
                } else {
                    Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                    showCart = true;
                    startActivity(cartIntent);
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            //search icon
            productDetailsActivity = null;
            finish();
            return true;

        } else if (id == R.id.main_search_icon) {
            //cart icon
            if (fromSerch) {
                finish();
            } else {
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
            }
            return true;

        } else if (id == R.id.main_cart_icon) {
            //cart icon
            if (firebaseUser == null) {
                signInDialog.show();
            } else {
                Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                showCart = true;
                startActivity(cartIntent);
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fromSerch = false;
    }

    @Override
    public void onBackPressed() {
        productDetailsActivity = null;
        super.onBackPressed();
    }
}