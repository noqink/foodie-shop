package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.enums.CommentLevel;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.SearchItemVO;
import com.imooc.pojo.vo.ShopcartVO;
import com.imooc.service.ItemService;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private ItemsImgMapper itemsImgMapper;
    @Autowired
    private ItemsSpecMapper itemsSpecMapper;
    @Autowired
    private ItemsParamMapper itemsParamMapper;
    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;
    @Autowired
    private ItemsMapperCustom itemsMapperCustom;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsSpec queryItemsSpecById(String itemsSpecId) {
        return itemsSpecMapper.selectByPrimaryKey(itemsSpecId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgById(String itemsId) {

        Example itemsImgExample = new Example(ItemsImg.class);
        Example.Criteria criteria = itemsImgExample.createCriteria();
        criteria.andEqualTo("itemId", itemsId);

        return itemsImgMapper.selectByExample(itemsImgExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecById(String itemsId) {

        Example itemsSpecExample = new Example(ItemsSpec.class);
        Example.Criteria criteria = itemsSpecExample.createCriteria();
        criteria.andEqualTo("itemId", itemsId);

        return itemsSpecMapper.selectByExample(itemsSpecExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParamById(String itemsId) {

        Example itemsParamExample = new Example(ItemsParam.class);
        Example.Criteria criteria = itemsParamExample.createCriteria();
        criteria.andEqualTo("itemId", itemsId);

        return itemsParamMapper.selectOneByExample(itemsParamExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO queryCommentCountsById(String itemsId) {
        Integer goodCounts = getCountsByLevel(itemsId, CommentLevel.GOOD.type);
        Integer normalCounts = getCountsByLevel(itemsId, CommentLevel.NORMAL.type);
        Integer badCounts = getCountsByLevel(itemsId, CommentLevel.BAD.type);

        CommentLevelCountsVO countsVO = new CommentLevelCountsVO();
        countsVO.setGoodCounts(goodCounts);
        countsVO.setNormalCounts(normalCounts);
        countsVO.setBadCounts(badCounts);
        countsVO.setTotalCounts(goodCounts + normalCounts + badCounts);

        return countsVO;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryPagedComment(String itemsId, Integer level,
                                                 Integer page, Integer pageSize) {

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("itemId", itemsId);
        paramsMap.put("level", level);

        // mybatis-pagehelper
        // 拦截sql 开启分页
        PageHelper.startPage(page, pageSize);

        List<ItemCommentVO> list = itemsMapperCustom.queryItemsComment(paramsMap);
        for (ItemCommentVO vo : list){
            vo.setNickname(DesensitizationUtil.commonDisplay(vo.getNickname()));
        }

        return getGrid(list, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchPagedItems(String keywords, String sort,
                                            Integer page, Integer pageSize) {

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("keywords", keywords);
        paramsMap.put("sort", sort);

        // mybatis-pagehelper
        // 拦截sql 开启分页
        PageHelper.startPage(page, pageSize);
        List<SearchItemVO> list = itemsMapperCustom.searchItems(paramsMap);

        return getGrid(list, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchPagedItemsByCatId(Integer catId, String sort, Integer page, Integer pageSize) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("catId", catId);
        paramsMap.put("sort", sort);

        // mybatis-pagehelper
        // 拦截sql 开启分页
        PageHelper.startPage(page, pageSize);
        List<SearchItemVO> list = itemsMapperCustom.searchItemsByCatId(paramsMap);

        return getGrid(list, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopcartVO> queryShopcartByIds(String specIds) {

        String[] ids = specIds.split(",");
        List<String> specIdList = new ArrayList<>();
        Collections.addAll(specIdList, ids);

        return itemsMapperCustom.queryShopcartByIds(specIdList);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public String queryItemImgUrl(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setIsMain(YesOrNo.YES.type);
        itemsImg.setItemId(itemId);

        ItemsImg img = itemsImgMapper.selectOne(itemsImg);
        return img.getUrl();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemSpecStock(String specId, Integer buyCounts) {
        // synchronized 不推荐使用，集群下无用，性能低下
        // 锁数据库: 不推荐，导致数据库性能低下
        // 分布式锁 zookeeper redis

        // lockUtil.getLock(); -- 加锁

        // 1. 查询库存
//        int stock = 10;

        // 2. 判断库存，是否能够减少到0以下
//        if (stock - buyCounts < 0) {
        // 提示用户库存不够
//            10 - 3 -3 - 5 = -1
//        }

        // lockUtil.unLock(); -- 解锁


        int result = itemsMapperCustom.decreaseItemSpecStock(specId, buyCounts);
        if (result != 1) {
            throw new RuntimeException("订单创建失败，原因：库存不足!");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    Integer getCountsByLevel(String itemsId, Integer level){
        ItemsComments condition = new ItemsComments();
        condition.setItemId(itemsId);
        condition.setCommentLevel(level);

        return itemsCommentsMapper.selectCount(condition);
    }

    private PagedGridResult getGrid(List<?> list, Integer page){
        // 分页设置
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setPage(page);
        gridResult.setRows(list);
        gridResult.setTotal(pageList.getPages());
        gridResult.setRecords(pageList.getTotal());

        return gridResult;
    }
}
