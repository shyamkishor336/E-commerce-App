package com.skinfotech.onlinedeliveryliquor;

public class HProductScrollModel {

    private String productID;
    private String  productImage;
    private String prductTitle, productDetail, productPrice;

    public HProductScrollModel(String productID,String  productImage, String prductTitle, String productDetail, String productPrice) {
        this.productImage = productImage;
        this.productID = productID;
        this.prductTitle = prductTitle;
        this.productDetail = productDetail;
        this.productPrice = productPrice;
    }

    public String getProductID() {
        return this.productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String  getProductImage() {
        return productImage;
    }

    public void setProductImage(String  productImage) {
        this.productImage = productImage;
    }

    public String getPrductTitle() {
        return prductTitle;
    }

    public void setPrductTitle(String prductTitle) {
        this.prductTitle = prductTitle;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
