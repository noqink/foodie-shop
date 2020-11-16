package com.imooc.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.Items;
import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.SearchItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ItemsMapperCustom extends MyMapper<Items> {

    public List<ItemCommentVO> queryItemsComment(@Param("paramsMap")Map<String, Object> paramsMap);

    public List<SearchItemVO> searchItems(@Param("paramsMap")Map<String, Object> paramsMap);

    public List<SearchItemVO> searchItemsByCatId(@Param("paramsMap")Map<String, Object> paramsMap);

}