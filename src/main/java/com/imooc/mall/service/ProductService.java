package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.reuqest.AddProductReq;
import com.imooc.mall.model.reuqest.ProductListReq;

/**
 * 描述：
 */
public interface ProductService {

    void add(AddProductReq addProductReq);

    void update(Product updateProduct);

    void delete(Integer id);

    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    PageInfo<Product> listForAdmin(Integer pageNum, Integer pageSize);

    Product detail(Integer id);

    PageInfo<Product> list(ProductListReq productListReq);
}
