package com.skinfotech.onlinedeliveryliquor;

import java.util.List;

public class HomePageModel {

    public static final int BANNER_SLIDER =0;
    public static final int STRIP_AD_BANNER =1;
    public static final int HORIZONTAL_PRODUCT_VIEW=2;
    public static final int GRID_PRODUCT_VIEW=3;
    private int type;
    private String backgroundColor;
    ////////////banner slider
    private List<SliderModel> sliderModelList;
    public HomePageModel(int type, List<SliderModel> sliderModelList) {
        this.type = type;
        this.sliderModelList = sliderModelList;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public List<SliderModel> getSliderModelList() {
        return sliderModelList;
    }
    public void setSliderModelList(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }
    ///////banner slider

    ////////Strip Ad
    private String resource;


    public HomePageModel(int type, String resource, String backgroundColor) {
        this.type = type;
        this.resource = resource;
        this.backgroundColor = backgroundColor;
    }
    public String getResource() {
        return resource;
    }
    public void setResource(String resource) {
        this.resource = resource;
    }
    public String getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    /////Strip Ad


    private String title;
    private List<HProductScrollModel> hProductScrollModelList;
    //////////HORIZONTAL VIEW PRODUCT
    private List<WishListModel> viewAllProductList;
    public HomePageModel(int type, String title,String backgroundColor, List<HProductScrollModel> hProductScrollModelList,List<WishListModel> viewAllProductList) {
        this.type = type;
        this.title = title;
        this.backgroundColor=backgroundColor;
        this.hProductScrollModelList = hProductScrollModelList;
        this.viewAllProductList = viewAllProductList;
    }

    public List<WishListModel> getViewAllProductList() {
        return viewAllProductList;
    }

    public void setViewAllProductList(List<WishListModel> viewAllProductList) {
        this.viewAllProductList = viewAllProductList;
    }
    //////////HORIZONTAL VIEW PRODUCT
    //////////GRID VIEW PRODUCT
    public HomePageModel(int type, String title, String backgroundColor, List<HProductScrollModel> hProductScrollModelList) {
        this.type = type;
        this.title = title;
        this.backgroundColor=backgroundColor;
        this.hProductScrollModelList = hProductScrollModelList;
    }//////////GRID VIEW PRODUCT
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<HProductScrollModel> gethProductScrollModelList() {
        return hProductScrollModelList;
    }
    public void sethProductScrollModelList(List<HProductScrollModel> hProductScrollModelList) {
        this.hProductScrollModelList = hProductScrollModelList;
    }


}
