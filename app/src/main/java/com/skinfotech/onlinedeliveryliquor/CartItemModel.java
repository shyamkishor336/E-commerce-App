package com.skinfotech.onlinedeliveryliquor;

import java.util.ArrayList;
import java.util.List;

public class CartItemModel {
    public static final int CART_ITEM =0;
    public static final int TOTAL_AMOUNT =1;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /////////cart item
    private String productID;
    private String  productImage;
    private String productTitle;
    private Long freeCoupons;
    private String productPrice;
    private String cuttedPrice;
    private Long productQty;
    private Long maxQty;
    private Long stockQuantity;
    private Long offersApplied;
    private Long couponsApplied;
    private boolean inStock;
    private List<String > qutyIDs;
    private boolean qtyError;
    private String selectedCoupenId;
    private String discountedPrice;
    private boolean COD;

    public CartItemModel(boolean COD,int type,String productID, String productImage, String productTitle, Long freeCoupons, String productPrice, String cuttedPrice, Long productQty, Long offersApplied, Long couponsApplied,boolean inStock,Long maxQty,Long stockQuantity) {
        this.type = type;
        this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.freeCoupons = freeCoupons;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;
        this.productQty = productQty;
        this.maxQty = maxQty;
        this.stockQuantity = stockQuantity;
        this.offersApplied = offersApplied;
        this.couponsApplied = couponsApplied;
        this.inStock = inStock;
        qutyIDs = new ArrayList<>();
        qtyError = false;
        this.COD = COD;

    }

    public boolean isCOD() {
        return COD;
    }

    public void setCOD(boolean COD) {
        this.COD = COD;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getSelectedCoupenId() {
        return selectedCoupenId;
    }

    public void setSelectedCoupenId(String selectedCoupenId) {
        this.selectedCoupenId = selectedCoupenId;
    }

    public boolean isQtyError() {
        return qtyError;
    }

    public void setQtyError(boolean qtyError) {
        this.qtyError = qtyError;
    }

    public Long getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Long stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<String> getQutyIDs() {
        return qutyIDs;
    }

    public void setQutyIDs(List<String> qutyIDs) {
        this.qutyIDs = qutyIDs;
    }

    public Long getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(Long maxQty) {
        this.maxQty = maxQty;
    }

    public boolean isInStock() {
        return inStock;
    }
    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }
    public String getProductID() {
        return productID;
    }
    public void setProductID(String productID) {
        this.productID = productID;
    }
    public String getProductImage() {
        return productImage;
    }
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
    public String getProductTitle() {
        return productTitle;
    }
    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }
    public Long getFreeCoupons() {
        return freeCoupons;
    }
    public void setFreeCoupons(Long freeCoupons) {
        this.freeCoupons = freeCoupons;
    }
    public String getProductPrice() {
        return productPrice;
    }
    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
    public String getCuttedPrice() {
        return cuttedPrice;
    }
    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }
    public Long getProductQty() {
        return productQty;
    }
    public void setProductQty(Long productQty) {
        this.productQty = productQty;
    }
    public Long getOffersApplied() {
        return offersApplied;
    }
    public void setOffersApplied(Long offersApplied) {
        this.offersApplied = offersApplied;
    }
    public Long getCouponsApplied() {
        return couponsApplied;
    }
    public void setCouponsApplied(Long couponsApplied) {
        this.couponsApplied = couponsApplied;
    }

    /////cart item

    //////////cart total
    private int totalItems, totalItemPrice,totalAmount,savedAmount;
    private String deliveryPrice;
    public CartItemModel(int type) {
        this.type = type;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(int totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(int savedAmount) {
        this.savedAmount = savedAmount;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }
////////cart total



}
