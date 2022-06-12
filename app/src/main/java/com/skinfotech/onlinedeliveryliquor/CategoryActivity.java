package com.skinfotech.onlinedeliveryliquor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static com.skinfotech.onlinedeliveryliquor.DBquerries.lists;
import static com.skinfotech.onlinedeliveryliquor.DBquerries.loadFragmentData;
import static com.skinfotech.onlinedeliveryliquor.DBquerries.loadedCategoriesNames;

public class CategoryActivity extends AppCompatActivity {


    private RecyclerView categoryRecyclerView;
    private HomePageAdapter adapter ;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getIntent().getStringExtra("categoryName");
        getSupportActionBar().setTitle(title);
        categoryRecyclerView = findViewById(R.id.category_recyclerView);
        ////////////////home page fake list

        List<SliderModel> sliderModelFakeList = new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));


        List<HProductScrollModel> hProductScrollModelFakeList = new ArrayList<>();
        hProductScrollModelFakeList.add(new HProductScrollModel("","","","",""));
        hProductScrollModelFakeList.add(new HProductScrollModel("","","","",""));
        hProductScrollModelFakeList.add(new HProductScrollModel("","","","",""));
        hProductScrollModelFakeList.add(new HProductScrollModel("","","","",""));
        hProductScrollModelFakeList.add(new HProductScrollModel("","","","",""));
        hProductScrollModelFakeList.add(new HProductScrollModel("","","","",""));
        hProductScrollModelFakeList.add(new HProductScrollModel("","","","",""));

        homePageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1,"","#ffffff"));
        homePageModelFakeList.add(new HomePageModel(2,"","#ffffff",hProductScrollModelFakeList,new ArrayList<WishListModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"","#ffffff",hProductScrollModelFakeList));

        /////////home page fake list

        LinearLayoutManager testingLayoutMangager = new LinearLayoutManager(this);
        adapter = new HomePageAdapter(homePageModelFakeList);

        testingLayoutMangager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(testingLayoutMangager);

        int listPosition = 0;
        for (int x = 0;x< loadedCategoriesNames.size();x++){
            if (loadedCategoriesNames.get(x).equals(title.toUpperCase())){
                listPosition =x;
            }
        }if (listPosition==0){
            loadedCategoriesNames.add(title.toUpperCase());
            lists.add(new ArrayList<HomePageModel>());

            loadFragmentData(categoryRecyclerView,this,loadedCategoriesNames.size()-1,title);
        }else {
            adapter = new HomePageAdapter(lists.get(listPosition));
        }


        categoryRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
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
            finish();
            //search icon
            return true;
        }
        else if (id==R.id.main_search_icon){
            //cart icon
            Intent searchIntent = new Intent(this,SearchActivity.class);
            startActivity(searchIntent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}