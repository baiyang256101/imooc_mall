package com.imooc.mall.service.impl;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.ProductMapper;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.reuqest.AddProductReq;
import com.imooc.mall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    ProductMapper productMapper;

    @Override
    public void add(AddProductReq addProductReq){
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq,product);

        // 检测重名
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if (productOld != null){
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.insertSelective(product);
        if (count == 0){
            throw new ImoocMallException(ImoocMallExceptionEnum.CREATE_FAILED);
        }
    }
}
