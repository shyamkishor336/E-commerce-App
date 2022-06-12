package com.skinfotech.onlinedeliveryliquor;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {

    private List<HomePageModel> homePageModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private int lastPosition = -1;

    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()) {
            case 0:
                return HomePageModel.BANNER_SLIDER;
            case 1:
                return HomePageModel.STRIP_AD_BANNER;
            case 2:
                return HomePageModel.HORIZONTAL_PRODUCT_VIEW;
            case 3:
                return HomePageModel.GRID_PRODUCT_VIEW;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case HomePageModel.BANNER_SLIDER:
                View bannerSliderview = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_ad_layout, parent, false);
                return new bannerSliderViewHolder(bannerSliderview);
            case HomePageModel.STRIP_AD_BANNER:
                View stripadview = LayoutInflater.from(parent.getContext()).inflate(R.layout.strip_ad_layout, parent, false);
                return new StripAdBannerViewHolder(stripadview);
            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                View hproductview = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_layout, parent, false);
                return new HorizontalProductViewHolder(hproductview);
            case HomePageModel.GRID_PRODUCT_VIEW:
                View gridproductview = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_layout, parent, false);
                return new GridProductViewHolder(gridproductview);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (homePageModelList.get(position).getType()) {
            case HomePageModel.BANNER_SLIDER:
                String bannercolor = homePageModelList.get(position).getBackgroundColor();
                List<SliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
                ((bannerSliderViewHolder) holder).setBannerSliderViewPager(sliderModelList);

                break;
            case HomePageModel.STRIP_AD_BANNER:
                String resource = homePageModelList.get(position).getResource();
                String color = homePageModelList.get(position).getBackgroundColor();
                ((StripAdBannerViewHolder) holder).setStripAd(resource, color);
                break;

            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                String layoutcolor = homePageModelList.get(position).getBackgroundColor();
                String horizontalprtitle = homePageModelList.get(position).getTitle();
                List<WishListModel> viewAllProductList = homePageModelList.get(position).getViewAllProductList();
                List<HProductScrollModel> hProductScrollModelList = homePageModelList.get(position).gethProductScrollModelList();
                ((HorizontalProductViewHolder) holder).setHorizontalProductLayout(hProductScrollModelList,horizontalprtitle,layoutcolor,viewAllProductList);
                break;
            case HomePageModel.GRID_PRODUCT_VIEW:
                String gridcolor = homePageModelList.get(position).getBackgroundColor();
                String gridtitle = homePageModelList.get(position).getTitle();
                List<HProductScrollModel> gridProductScrollModelist = homePageModelList.get(position).gethProductScrollModelList();
                ((GridProductViewHolder) holder).setGridProductLayout(gridProductScrollModelist, gridtitle, gridcolor);
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
        return homePageModelList.size();
    }

    public class bannerSliderViewHolder extends RecyclerView.ViewHolder {
        private ViewPager bannerSliderViewPager;
        private List<SliderModel> sliderModelList;
        private int currentPage;
        private Timer timer;
        final private long DELAY_TIME = 3000;
        final private long PERIOD_TIME = 3000;
        private List<SliderModel> arrangeList;

        public bannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerSliderViewPager = itemView.findViewById(R.id.banner_slider_viewPager);


        }

        private void pageLooper(List<SliderModel> sliderModelList) {
            if (currentPage == sliderModelList.size() - 2) {
                currentPage = 2;
                bannerSliderViewPager.setCurrentItem(currentPage, false);
            }
            if (currentPage == 1) {
                currentPage = sliderModelList.size() - 3;
                bannerSliderViewPager.setCurrentItem(currentPage, false);
            }
        }

        private void startBannerSlideShow(final List<SliderModel> sliderModelList) {
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentPage >= sliderModelList.size()) {
                        currentPage = 1;
                    }
                    bannerSliderViewPager.setCurrentItem(currentPage++, true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, DELAY_TIME, PERIOD_TIME);
        }

        private void stopBannerSlideShow() {
            timer.cancel();
        }

        private void setBannerSliderViewPager(final List<SliderModel> sliderModelList) {
            currentPage = 2;
            if (timer != null) {
                timer.cancel();
            }
            arrangeList = new ArrayList<>();
            for (int x = 0; x < sliderModelList.size(); x++) {
                arrangeList.add(x, sliderModelList.get(x));

            }
            arrangeList.add(0, sliderModelList.get(sliderModelList.size() - 2));
            arrangeList.add(1, sliderModelList.get(sliderModelList.size() - 1));

            arrangeList.add(sliderModelList.get(0));
            arrangeList.add(sliderModelList.get(1));


            SliderAdapter sliderAdapter = new SliderAdapter(arrangeList);
            bannerSliderViewPager.setAdapter(sliderAdapter);
            bannerSliderViewPager.setClipToPadding(false);
            bannerSliderViewPager.setPageMargin(20);
            bannerSliderViewPager.setCurrentItem(currentPage);

            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        pageLooper(arrangeList);
                    }
                }
            };
            bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);
///////////
            startBannerSlideShow(arrangeList);
            bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    pageLooper(arrangeList);
                    stopBannerSlideShow();
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        startBannerSlideShow(arrangeList);
                    }
                    return false;
                }
            });
        }
    }

    public class StripAdBannerViewHolder extends RecyclerView.ViewHolder {
        private ImageView stripAdImage;
        private LinearLayout stripadContainer;

        public StripAdBannerViewHolder(@NonNull View itemView) {
            super(itemView);
            stripAdImage = itemView.findViewById(R.id.strip_ad_image);
            stripadContainer = itemView.findViewById(R.id.strip_ad_container);
        }

        private void setStripAd(String resource, String color) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.icon_placeholder)).into(stripAdImage);

            stripadContainer.setBackgroundColor(Color.parseColor(color));
        }
    }

    public class HorizontalProductViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout container;
        private TextView horizontalLayoutTitle;
        private Button horizontalViewallBtn;
        private RecyclerView horizontalRecyclerView;

        public HorizontalProductViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            horizontalLayoutTitle = itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizontalViewallBtn = itemView.findViewById(R.id.horizontal_viewall_button);
            horizontalRecyclerView = itemView.findViewById(R.id.horizontal_recycler);
            horizontalRecyclerView.setRecycledViewPool(recycledViewPool);
        }

        private void setHorizontalProductLayout(final List<HProductScrollModel> hProductScrollModelList, final String title, String color, final List<WishListModel> viewAllProductList) {
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            horizontalLayoutTitle.setText(title);

            for (final HProductScrollModel model : hProductScrollModelList) {
                if (!model.getProductID().isEmpty() && !model.getPrductTitle().isEmpty()) {
                    firebaseFirestore.collection("PRODUCTS")
                            .document(model.getProductID())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                model.setPrductTitle(task.getResult().getString("product_title"));
                                model.setProductImage(task.getResult().getString("product_image_1"));
                                model.setProductPrice(task.getResult().getString("product_price"));

                                WishListModel wishListModel = viewAllProductList.get(hProductScrollModelList.indexOf(model));

                                wishListModel.setTotalRatings(task.getResult().getLong("total_ratings"));
                                wishListModel.setRating(task.getResult().getString("average_rating"));
                                wishListModel.setProductTitle(task.getResult().getString("product_title"));
                                wishListModel.setProductPrice(task.getResult().getString("product_price"));
                                wishListModel.setProductImage(task.getResult().getString("product_image_1"));
                                wishListModel.setFreeCoupons(task.getResult().getLong("free_coupens"));
                                wishListModel.setCuttedPrice(task.getResult().getString("cutted_price"));
                                wishListModel.setCOD(task.getResult().getBoolean("COD"));

                                wishListModel.setInStock((task.getResult().getLong("stock_quantity")>0));

                                if (hProductScrollModelList.indexOf(model) == hProductScrollModelList.size() - 1) {
                                    if (horizontalRecyclerView.getAdapter() != null) {
                                        horizontalRecyclerView.getAdapter().notifyDataSetChanged();
                                    }
                                }
                            } else {
                                /////do nothing
                            }
                        }
                    });
                }
            }
            if (hProductScrollModelList.size() > 8) {
                horizontalViewallBtn.setVisibility(View.VISIBLE);

                horizontalViewallBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewAllActivity.wishListModelList = viewAllProductList;
                        Intent viewallIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        viewallIntent.putExtra("layout_code", 0);
                        viewallIntent.putExtra("title", title);
                        itemView.getContext().startActivity(viewallIntent);
                    }
                });
            } else {
                horizontalViewallBtn.setVisibility(View.INVISIBLE);
            }

            HProductScrollAdapter hProductScrollAdapter = new HProductScrollAdapter(hProductScrollModelList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontalRecyclerView.setLayoutManager(linearLayoutManager);
            horizontalRecyclerView.setAdapter(hProductScrollAdapter);
            hProductScrollAdapter.notifyDataSetChanged();
        }
    }

    public class GridProductViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout container;
        private TextView gridLayoutTitle;
        private Button gridLayoutViewAllBtn;
        private GridLayout gridProductLayout;

        public GridProductViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            gridLayoutTitle = itemView.findViewById(R.id.grid_product_layout_title);
            gridLayoutViewAllBtn = itemView.findViewById(R.id.grid_product_viewall_btn);
            gridProductLayout = itemView.findViewById(R.id.grid_layout);


        }

        private void setGridProductLayout(final List<HProductScrollModel> hProductScrollModelList, final String title, String color) {
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            gridLayoutTitle.setText(title);
            for (final HProductScrollModel model : hProductScrollModelList) {
                if (!model.getProductID().isEmpty() && model.getPrductTitle().isEmpty()) {
                    firebaseFirestore.collection("PRODUCTS").document(model.getProductID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                model.setPrductTitle(task.getResult().getString("product_title"));
                                model.setProductImage(task.getResult().getString("product_image_1"));
                                model.setProductPrice(task.getResult().getString("product_price"));

                                if (hProductScrollModelList.indexOf(model) == hProductScrollModelList.size() - 1) {
                                    setGridData(title, hProductScrollModelList);
                                    if (!title.equals("")) {
                                        gridLayoutViewAllBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                ViewAllActivity.hProductScrollModelList = hProductScrollModelList;
                                                Intent viewallIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                                                viewallIntent.putExtra("layout_code", 1);
                                                viewallIntent.putExtra("title", title);
                                                itemView.getContext().startActivity(viewallIntent);
                                            }
                                        });
                                    }
                                }
                            } else {
                                /////do nothing
                            }
                        }
                    });
                }
            }
            setGridData(title, hProductScrollModelList);
        }

        private void setGridData(final String title, final List<HProductScrollModel> hProductScrollModelList) {
            for (int x = 0; x < 4; x++) {
                ImageView productImage = gridProductLayout.getChildAt(x).findViewById(R.id.hs_product_image);
                TextView productTitle = gridProductLayout.getChildAt(x).findViewById(R.id.hs_prduct_title);
                TextView productDescription = gridProductLayout.getChildAt(x).findViewById(R.id.hs_prduct_detail);
                TextView productPrice = gridProductLayout.getChildAt(x).findViewById(R.id.hs_product_price);

                Glide.with(itemView.getContext()).load(hProductScrollModelList.get(x).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.icon_placeholder)).into(productImage);
                productTitle.setText(hProductScrollModelList.get(x).getPrductTitle());
                productDescription.setText(hProductScrollModelList.get(x).getProductDetail());
                productPrice.setText("Rs." + hProductScrollModelList.get(x).getProductPrice() + "/-");
                gridProductLayout.getChildAt(x).setBackgroundColor(Color.parseColor("#ffffff"));

                if (!title.equals("")) {


                    final int finalX = x;
                    gridProductLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent productDetailIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                            productDetailIntent.putExtra("PRODUCT_ID", hProductScrollModelList.get(finalX).getProductID());
                            itemView.getContext().startActivity(productDetailIntent);
                        }
                    });
                }
            }

        }
    }
}
