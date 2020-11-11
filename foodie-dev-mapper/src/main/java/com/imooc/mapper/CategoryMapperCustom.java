package com.imooc.mapper;

import com.imooc.pojo.vo.CategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryMapperCustom {

    public List<CategoryVO> querySubCat(Integer rootCatId);

}
