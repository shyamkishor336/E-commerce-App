package com.skinfotech.onlinedeliveryliquor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.skinfotech.onlinedeliveryliquor.DBquerries.categoryModelList;
import static com.skinfotech.onlinedeliveryliquor.DBquerries.firebaseFirestore;
import static com.skinfotech.onlinedeliveryliquor.DBquerries.lists;
import static com.skinfotech.onlinedeliveryliquor.DBquerries.loadCategories;
import static com.skinfotech.onlinedeliveryliquor.DBquerries.loadFragmentData;
import static com.skinfotech.onlinedeliveryliquor.DBquerries.loadedCategoriesNames;

/**
 * A simple {@link Fragment} subclass.

 */
public class HomeFragment extends Fragment {



    public HomeFragment() {
        // Required empty public constructor
    }

    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
  private RecyclerView categoryRecyclerview;
    private CategoryAdapter categoryAdapter;
    private List<CategoryModel> categoryModelFakeList = new ArrayList<>();
    private RecyclerView homePageRecyclerview;
    private ImageView noInternetConnection;
    private Button retryBtn;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    public static SwipeRefreshLayout swipeRefreshLayout;

    private HomePageAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        noInternetConnection = view.findViewById(R.id.no_internet_connection);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        homePageRecyclerview = view.findViewById(R.id.home_page_recyclerview);
        categoryRecyclerview = view.findViewById(R.id.category_recyclerview);
        retryBtn = view.findViewById(R.id.retry_btn);
        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.BtnBlue),getContext().getResources().getColor(R.color.BtnBlue),getContext().getResources().getColor(R.color.BtnBlue));


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerview.setLayoutManager(layoutManager);

        LinearLayoutManager testingLayoutMangager = new LinearLayoutManager(getContext());
        testingLayoutMangager.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerview.setLayoutManager(testingLayoutMangager);

        ////////////////category fake list
        categoryModelFakeList.add(new CategoryModel("null",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
////////////////category fake list

        ////////////////home page fake list

        List<SliderModel> sliderModelFakeList = new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new SliderModel("null","#dfdfdf"));


        List<HProductScrollModel> hProductScrollModelFakeList = new ArrayList<>();
        hProductScrollModelFakeList.add(new HProductScrollModel("","","","",""));
        hProductScrollModelFakeList.add(new HProductScrollModel("","","","",""));
        hProductScrollModelFakeList.add(new HProductScrollModel("","","","",""));
        hProductScrollModelFakeList.add(new HProductScrollModel("","","","",""));
        hProductScrollModelFakeList.add(new HProductScrollModel("","","","",""));
        hProductScrollModelFakeList.add(new HProductScrollModel("","","","",""));
        hProductScrollModelFakeList.add(new HProductScrollModel("","","","",""));

        homePageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1,"","#dfdfdf"));
        homePageModelFakeList.add(new HomePageModel(2,"","#dfdfdf",hProductScrollModelFakeList,new ArrayList<WishListModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"","#dfdfdf",hProductScrollModelFakeList));

        /////////home page fake list





        categoryAdapter = new CategoryAdapter(categoryModelFakeList);

        adapter = new HomePageAdapter(homePageModelFakeList);


        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()==true){
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);
            categoryRecyclerview.setVisibility(View.VISIBLE);
            homePageRecyclerview.setVisibility(View.VISIBLE);


            if (categoryModelList.size() == 0){
                loadCategories(categoryRecyclerview,getContext());

            }else {
                categoryAdapter = new CategoryAdapter(categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            }categoryRecyclerview.setAdapter(categoryAdapter);





            //////////


            if (lists.size() == 0){
                loadedCategoriesNames.add("HOME");
                lists.add(new ArrayList<HomePageModel>());
                loadFragmentData(homePageRecyclerview,getContext(),0,"Home");

            }else {
                adapter = new HomePageAdapter(lists.get(0));
                adapter.notifyDataSetChanged();
            }
            homePageRecyclerview.setAdapter(adapter);

        }else {
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            categoryRecyclerview.setVisibility(View.GONE);
            homePageRecyclerview.setVisibility(View.GONE);
            Glide.with(this).load(R.mipmap.no_internet).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "No Internet Connection!!", Toast.LENGTH_LONG).show();

        }



        //////////////swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
               reloadPage();

            }
        });
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadPage();
            }
        });
        return view;
    }

private void reloadPage(){
    networkInfo = connectivityManager.getActiveNetworkInfo();
//    categoryModelList.clear();
//    lists.clear();
//    loadedCategoriesNames.clear();
    DBquerries.clearData();
    if (networkInfo != null && networkInfo.isConnected()==true) {
        MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        noInternetConnection.setVisibility(View.GONE);
        categoryRecyclerview.setVisibility(View.VISIBLE);
        homePageRecyclerview.setVisibility(View.VISIBLE);
        retryBtn.setVisibility(View.GONE);
        categoryAdapter = new CategoryAdapter(categoryModelFakeList);
        adapter= new HomePageAdapter(homePageModelFakeList);
        categoryRecyclerview.setAdapter(categoryAdapter);
        homePageRecyclerview.setAdapter(adapter);
        loadCategories(categoryRecyclerview,getContext());
        loadedCategoriesNames.add("HOME");
        lists.add(new ArrayList<HomePageModel>());
        loadFragmentData( homePageRecyclerview,getContext(),0,"Home");


    }else {
        MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        Glide.with(getContext()).load(R.mipmap.no_internet).into(noInternetConnection);
        noInternetConnection.setVisibility(View.VISIBLE);
        categoryRecyclerview.setVisibility(View.GONE);
        homePageRecyclerview.setVisibility(View.GONE);
        retryBtn.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), "Please check your Internet Connection!!", Toast.LENGTH_LONG).show();
        swipeRefreshLayout.setRefreshing(false);

    }
}

}