package com.imooc.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.SixNewItemsVO;

import java.util.List;

public interface CategoryService {

    /**
     * 查询一级分类列表
     * @return
     */
    public List<Category> queryRootLevelCat();

    /**
     * 根据一级分类id查询子级分类
     * @param rootCatId
     * @return
     */
    public List<CategoryVO> querySubLevelCat(Integer rootCatId);

    /**
     * 通过懒加载查询最新六个商品
     * @param rootCatId
     * @return
     */
    public List<SixNewItemsVO> querySixNewItemsLazy(Integer rootCatId);
}
