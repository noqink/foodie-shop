package com.imooc.controller;

import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.SixNewItemsVO;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "首页展示", tags = {"用于首页展示的相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "查询轮播图列表", notes = "查询轮播图列表", httpMethod = "GET")
    @RequestMapping("/carousel")
    public IMOOCJSONResult carousel(){

        List<Carousel> list = carouselService.queryAll(YesOrNo.YES.type);

        return IMOOCJSONResult.ok(list);
    }

    @ApiOperation(value = "查询一级分类列表", notes = "查询一级分类列表", httpMethod = "GET")
    @RequestMapping("/cats")
    public IMOOCJSONResult categoryRoot(){

        List<Category> categorieRoots = categoryService.queryRootLevelCat();

        return IMOOCJSONResult.ok(categorieRoots);
    }

    @ApiOperation(value = "根据一级分类id查询子级分类列表", notes = "根据一级分类id查询子级分类列表", httpMethod = "GET")
    @RequestMapping("/subCat/{rootCatId}")
    public IMOOCJSONResult subcat(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId){

        if (rootCatId == null){
            return IMOOCJSONResult.errorMsg("子级分类不存在");
        }
        List<CategoryVO> subCats = categoryService.querySubLevelCat(rootCatId);

        return IMOOCJSONResult.ok(subCats);
    }

    @ApiOperation(value = "通过懒加载查询六个一级id下的商品", notes = "通过懒加载查询六个一级id下的商品", httpMethod = "GET")
    @RequestMapping("/sixNewItems/{rootCatId}")
    public IMOOCJSONResult sixNewItemsLazy(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId){

        if (rootCatId == null){
            return IMOOCJSONResult.errorMsg("子级分类不存在");
        }
        List<SixNewItemsVO> itemsVOList = categoryService.querySixNewItemsLazy(rootCatId);

        return IMOOCJSONResult.ok(itemsVOList);
    }

}
