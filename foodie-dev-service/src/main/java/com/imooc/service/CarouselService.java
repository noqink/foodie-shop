package com.imooc.service;

import com.imooc.pojo.Carousel;

import java.util.List;

public interface CarouselService {

    /**
     * 查询轮播图列表
     * @param isShow
     * @return
     */
    public List<Carousel> queryAll(int isShow);

}
