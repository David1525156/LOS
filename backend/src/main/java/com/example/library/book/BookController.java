package com.example.library.book;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.library.common.Result;
import com.example.library.entity.Book;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 图书查询接口（US-01 列表 / US-02 详情）
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * 图书分页列表：GET /api/books?page=1&size=10
     */
    @GetMapping
    public Result<Page<Book>> list(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        return Result.success(bookService.pageBooks(page, size));
    }

    /**
     * 图书详情：GET /api/books/{id}
     */
    @GetMapping("/{id}")
    public Result<Book> detail(@PathVariable Long id) {
        return Result.success(bookService.getBook(id));
    }
}
