package com.imooc.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.Stu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

public interface StuMapper extends MyMapper<Stu> {
}