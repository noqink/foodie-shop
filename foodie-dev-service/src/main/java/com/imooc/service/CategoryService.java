package com.imooc.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;

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

}
