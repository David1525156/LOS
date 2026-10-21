package com.example.library.book;

import com.example.library.book.dto.BookCreateRequest;
import com.example.library.entity.Book;
import com.example.library.mapper.BookMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 新增图书服务（US-03）
 */
@Service
public class BookCreateService {

    private final BookMapper bookMapper;

    public BookCreateService(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    /**
     * 新增图书，书名/作者去除首尾空格，成功后返回含 id 的完整记录
     */
    public Book create(BookCreateRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle().trim());
        book.setAuthor(request.getAuthor().trim());
        book.setDescription(request.getDescription());
        book.setStock(request.getStock());
        book.setCreateTime(LocalDateTime.now());
        bookMapper.insert(book);
        return book;
    }
}
