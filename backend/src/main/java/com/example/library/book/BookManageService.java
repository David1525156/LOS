package com.example.library.book;

import com.example.library.book.dto.BookCreateRequest;
import com.example.library.common.BusinessException;
import com.example.library.entity.Book;
import com.example.library.mapper.BookMapper;
import org.springframework.stereotype.Service;

/**
 * 图书修改 / 删除服务
 */
@Service
public class BookManageService {

    private final BookMapper bookMapper;

    public BookManageService(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    /**
     * 修改图书：不存在时抛业务异常
     */
    public Book update(Long id, BookCreateRequest request) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            throw new BusinessException(400, "图书不存在");
        }
        book.setTitle(request.getTitle().trim());
        book.setAuthor(request.getAuthor().trim());
        book.setDescription(request.getDescription());
        book.setStock(request.getStock());
        bookMapper.updateById(book);
        return book;
    }

    /**
     * 删除图书：不存在时抛业务异常
     */
    public void delete(Long id) {
        Book book = bookMapper.selectById(id);
        if (book == null) {
            throw new BusinessException(400, "图书不存在");
        }
        bookMapper.deleteById(id);
    }
}
