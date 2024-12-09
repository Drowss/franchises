package com.drow.domain.model;

import java.util.List;

public class BranchModel {
    private String name;
    private List<ProductModel> offeredProducts;

    public BranchModel() {
    }

    public BranchModel(String name, List<ProductModel> offeredProducts) {
        this.name = name;
        this.offeredProducts = offeredProducts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductModel> getOfferedProducts() {
        return offeredProducts;
    }

    public void setOfferedProducts(List<ProductModel> offeredProducts) {
        this.offeredProducts = offeredProducts;
    }
}
