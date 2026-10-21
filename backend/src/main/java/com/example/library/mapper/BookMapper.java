package com.example.library.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.library.entity.Book;
import org.apache.ibatis.annotations.Mapper;

/**
 * 图书 Mapper
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
