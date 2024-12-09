package com.drow.domain.model;

public class ProductModel {
    private String name;
    private Integer stock;

    public ProductModel() {
    }

    public ProductModel(String name, Integer stock) {
        this.name = name;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
