package com.drow.application.handler.impl;

import com.drow.application.handler.IProductsHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductsHandler implements IProductsHandler {
    @Override
    public void createProduct() {

    }
}
