package com.example.library.book;

import com.example.library.book.dto.BookCreateRequest;
import com.example.library.common.Result;
import com.example.library.entity.Book;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 新增图书接口（US-03，需管理员登录，AuthInterceptor 校验 Bearer Token）
 */
@RestController
@RequestMapping("/api/books")
public class BookAdminController {

    private final BookCreateService bookCreateService;

    public BookAdminController(BookCreateService bookCreateService) {
        this.bookCreateService = bookCreateService;
    }

    /**
     * 新增图书：POST /api/books
     */
    @PostMapping
    public Result<Book> create(@RequestBody @Validated @Valid BookCreateRequest request) {
        return Result.success(bookCreateService.create(request));
    }
}
