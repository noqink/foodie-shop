package com.imooc.service;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.ShopcartVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public interface ItemService {

    /**
     * 通过itemId查询商品
     * @param itemId
     * @return
     */
    public Items queryItemById(String itemId);

    /**
     * 通过itemsSpecId查询商品规格
     * @param itemsSpecId
     * @return
     */
    public ItemsSpec queryItemsSpecById(String itemsSpecId);

    /**
     * 通过itemId查询商品图片
     * @param itemsId
     * @return
     */
    public List<ItemsImg> queryItemImgById(String itemsId);

    /**
     * 通过itemId查询商品规格
     * @param itemsId
     * @return
     */
    public List<ItemsSpec> queryItemSpecById(String itemsId);

    /**
     * 通过itemId查询商品参数
     * @param itemsId
     * @return
     */
    public ItemsParam queryItemParamById(String itemsId);

    /**
     * 通过itemId查询商品评价数量VO
     * @param itemsId
     * @return
     */
    public CommentLevelCountsVO queryCommentCountsById(String itemsId);

    /**
     * 分页查询对应评论等级的评论VO
     * @param itemsId
     * @param level
     * @return
     */
    public PagedGridResult queryPagedComment(String itemsId, Integer level,
                                             Integer page, Integer pageSize);

    /**
     * 分页搜索商品VO
     * @param keywords
     * @param sort
     * @return
     */
    public PagedGridResult searchPagedItems(String keywords, String sort,
                                             Integer page, Integer pageSize);

    /**
     * 分页搜索商品VO
     * @param catId
     * @param sort
     * @return
     */
    public PagedGridResult searchPagedItemsByCatId(Integer catId, String sort,
                                            Integer page, Integer pageSize);

    /**
     * 根据规格specIds查询最新的购物车中的商品数据（用于刷新渲染购物车中的商品数据）
     * @param specIds
     * @return
     */
    public List<ShopcartVO> queryShopcartByIds(String specIds);

    /**
     * 通过itemId查询商品主图
     * @param itemId
     * @return
     */
    public String queryItemImgUrl(String itemId);

    /**
     * 减库存
     * @param specId
     * @param buyCounts
     * @return
     */
    public void decreaseItemSpecStock(String specId, Integer buyCounts);
}
