package com.imooc.service.impl;

import com.imooc.mapper.CategoryMapper;
import com.imooc.mapper.CategoryMapperCustom;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryMapperCustom categoryMapperCustom;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queryRootLevelCat() {

        Example categoryExample = new Example(Category.class);
        Example.Criteria criteria = categoryExample.createCriteria();
        criteria.andEqualTo("type", 1);

        List<Category> categories = categoryMapper.selectByExample(categoryExample);

        return categories;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> querySubLevelCat(Integer rootCatId) {
        List<CategoryVO> categoryVOList = categoryMapperCustom.querySubCat(rootCatId);
        return categoryVOList;
    }
}
