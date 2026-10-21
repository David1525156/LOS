package com.example.library.book;

import com.example.library.book.dto.BookCreateRequest;
import com.example.library.common.Result;
import com.example.library.entity.Book;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 图书修改 / 删除接口（需管理员登录，AuthInterceptor 校验 Bearer Token）
 */
@RestController
@RequestMapping("/api/books")
public class BookManageController {

    private final BookManageService bookManageService;

    public BookManageController(BookManageService bookManageService) {
        this.bookManageService = bookManageService;
    }

    /**
     * 修改图书：PUT /api/books/{id}
     */
    @PutMapping("/{id}")
    public Result<Book> update(@PathVariable Long id,
                               @RequestBody @Validated @Valid BookCreateRequest request) {
        return Result.success(bookManageService.update(id, request));
    }

    /**
     * 删除图书：DELETE /api/books/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        bookManageService.delete(id);
        return Result.success(null);
    }
}
