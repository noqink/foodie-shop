package com.imooc.mapper;

import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.SixNewItemsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

//@Mapper
//@Repository
public interface CategoryMapperCustom {

    /**
     * 查询一级分类下的子级分类
     * @param rootCatId
     * @return
     */
    public List<CategoryVO> querySubCat(Integer rootCatId);

    /**
     * 运用懒加载 加载最新的六个商品
     * @param map
     * @return
     */
    public List<SixNewItemsVO> querySixNewItemsLazy(@Param("paramsMap") Map<String, Object> map);

}
