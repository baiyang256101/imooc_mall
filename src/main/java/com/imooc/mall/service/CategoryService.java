package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.reuqest.AddCategoryReq;
import com.imooc.mall.model.vo.CategoryVO;

import java.util.List;

/**
 * 描述：
 */
public interface CategoryService {

    void add(AddCategoryReq addCategoryReq) throws ImoocMallException;

    void update(Category updateCategory);

    void delete(Integer id);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    List<CategoryVO> listCategoryForCustomer();
}
