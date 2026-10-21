package com.example.library.book;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.library.common.BusinessException;
import com.example.library.entity.Book;
import com.example.library.mapper.BookMapper;
import org.springframework.stereotype.Service;

/**
 * 图书查询服务（US-01 列表分页 / US-02 详情）
 */
@Service
public class BookService {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;

    private final BookMapper bookMapper;

    public BookService(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    /**
     * 分页查询图书列表，默认每页 10 条，新书优先
     *
     * @param page 页码，从 1 开始，非法值归一化为 1
     * @param size 每页条数，非法值归一化为 10
     */
    public Page<Book> pageBooks(int page, int size) {
        if (page < 1) {
            page = DEFAULT_PAGE;
        }
        if (size < 1) {
            size = DEFAULT_SIZE;
        }
        Page<Book> p = new Page<>(page, size);
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<Book>()
                .orderByDesc(Book::getCreateTime)
                .orderByDesc(Book::getId);
        return bookMapper.selectPage(p, wrapper);
    }

    /**
     * 查询图书详情，不存在时抛出业务异常
     */
    public Book getBook(Long id) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            throw new BusinessException(400, "图书不存在");
        }
        return book;
    }
}
