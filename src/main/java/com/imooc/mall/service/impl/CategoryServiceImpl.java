package com.imooc.mall.service.impl;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CategoryMapper;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.reuqest.AddCategoryReq;
import com.imooc.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述：  CategoryService 实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    CategoryMapper categoryMapper;

    @Override
    public void add(AddCategoryReq addCategoryReq) throws ImoocMallException {
        Category category = new Category();

        // 同名属性拷贝
        BeanUtils.copyProperties(addCategoryReq, category);

        // 查询是否有重名
        Category categoryOld = categoryMapper.selectByName(addCategoryReq.getName());

        if (categoryOld != null) { // 有重名
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }

        // 插入
        int count = categoryMapper.insertSelective(category);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CREATE_FAILED);
        }


    }

    /**
     * 更新后台目录
     * @param updateCategory
     */
    @Override
    public void update(Category updateCategory) {

        // 检查名称,首先传进来参数不能为空
        if (updateCategory.getName() != null) {

            // 根据名称查找数据库，分2 情况，数据库中的数据相对于查询的数据，id相同，名称不同（修改名称+其他）; id相同，名称相同（修改其他）
            Category categoryOld = categoryMapper.selectByName(updateCategory.getName());

            // 数据库返回的不能已经存在，（区别 id），修改的话会返回同名称的数据
            /**
             * 17 冰淇淋 2
             * 30 鸭货 3
             * 30 鸭货 2
             */
            if (categoryOld != null && !categoryOld.getId().equals(updateCategory.getId())) {// id在数据库中不存在，名称存在
                    throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
            }
            int count = categoryMapper.updateByPrimaryKeySelective(updateCategory);

            if (count == 0) { // id 不存在，目录名称不存在，更新失败（找不到 id）
                throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
            }
        } else {
            throw new ImoocMallException(ImoocMallExceptionEnum.PARA_NOT_NULL);
        }
    }

}
