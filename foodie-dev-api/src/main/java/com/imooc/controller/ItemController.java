package com.imooc.controller;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.bo.ShopcartBO;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemInfoVO;
import com.imooc.pojo.vo.ShopcartVO;
import com.imooc.service.ItemService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "商品详情页展示", tags = {"用于商品详情页的相关接口"})
@RestController
@RequestMapping("items")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @RequestMapping("/info/{itemId}")
    public IMOOCJSONResult info(
            @ApiParam(name = "itemId", value = "商品ID", required = true)
            @PathVariable String itemId){

        if (StringUtils.isBlank(itemId)){
            return IMOOCJSONResult.errorMsg("商品不存在！");
        }

        Items item = itemService.queryItemById(itemId);
        List<ItemsImg> itemImgList = itemService.queryItemImgById(itemId);
        List<ItemsSpec> itemSpecList = itemService.queryItemSpecById(itemId);
        ItemsParam itemParams = itemService.queryItemParamById(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemImgList);
        itemInfoVO.setItemSpecList(itemSpecList);
        itemInfoVO.setItemParams(itemParams);

        return IMOOCJSONResult.ok(itemInfoVO);
    }

    @ApiOperation(value = "查询商品评价数", notes = "查询商品评价数", httpMethod = "GET")
    @RequestMapping("/commentLevel")
    public IMOOCJSONResult commentLevel(
            @ApiParam(name = "itemId", value = "商品ID", required = true)
            @RequestParam String itemId){

        if (StringUtils.isBlank(itemId)){
            return IMOOCJSONResult.errorMsg("商品不存在！");
        }

        CommentLevelCountsVO countsVO = itemService.queryCommentCountsById(itemId);

        return IMOOCJSONResult.ok(countsVO);
    }

    @ApiOperation(value = "分页查询评价", notes = "分页查询评价", httpMethod = "GET")
    @RequestMapping("/comments")
    public IMOOCJSONResult comments(
            @ApiParam(name = "itemId", value = "商品ID", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评价等级", required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "当前页数", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页显示数", required = false)
            @RequestParam Integer pageSize){

        if (StringUtils.isBlank(itemId)){
            return IMOOCJSONResult.errorMsg("商品不存在！");
        }

        if (page == null){
            page = 1;
        }

        if (pageSize == null){
            pageSize = BaseController.COMMENT_DEFAULT_COUNT;
        }

        PagedGridResult grid = itemService.queryPagedComment(itemId, level, page, pageSize);

        return IMOOCJSONResult.ok(grid);
    }

    @ApiOperation(value = "分页搜索商品", notes = "分页搜索商品", httpMethod = "GET")
    @RequestMapping("/search")
    public IMOOCJSONResult comments(
            @ApiParam(name = "keywords", value = "搜索关键字", required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序方式", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "当前页数", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页显示数", required = false)
            @RequestParam Integer pageSize){

        if (StringUtils.isBlank(keywords)){
            return IMOOCJSONResult.errorMsg("搜索内容不能为空！");
        }

        if (page == null){
            page = 1;
        }

        if (pageSize == null){
            pageSize = BaseController.ITEAM_DEFAULT_COUNT;
        }

        PagedGridResult grid = itemService.searchPagedItems(keywords, sort, page, pageSize);

        return IMOOCJSONResult.ok(grid);
    }

    @ApiOperation(value = "分页依据catId搜索商品", notes = "分页依据catId搜索商品", httpMethod = "GET")
    @RequestMapping("/catItems")
    public IMOOCJSONResult catItems(
            @ApiParam(name = "catId", value = "分类id", required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序方式", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "当前页数", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页显示数", required = false)
            @RequestParam Integer pageSize){

        if (catId == null){
            return IMOOCJSONResult.errorMsg("商品分类不存在");
        }

        if (page == null){
            page = 1;
        }

        if (pageSize == null){
            pageSize = BaseController.ITEAM_DEFAULT_COUNT;
        }

        PagedGridResult grid = itemService.searchPagedItemsByCatId(catId, sort, page, pageSize);

        return IMOOCJSONResult.ok(grid);
    }

    // 由于用户长时间未登录网站，刷新购物车中的数据（主要预防价格变动）
    @ApiOperation(value = "刷新购物车商品数据", notes = "刷新购物车商品数据", httpMethod = "GET")
    @GetMapping("/refresh")
    public IMOOCJSONResult add(
            @ApiParam(name = "itemSpecIds", value = "购物车商品id拼接的字符串", required = true)
            @RequestParam String itemSpecIds){

        if (StringUtils.isBlank(itemSpecIds)){
            // 购物车无数据
            return IMOOCJSONResult.ok();
        }

        List<ShopcartVO> shopcartVOList = itemService.queryShopcartByIds(itemSpecIds);

        return IMOOCJSONResult.ok(shopcartVOList);
    }

}
