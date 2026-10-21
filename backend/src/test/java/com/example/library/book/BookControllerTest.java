package com.example.library.book;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.library.auth.TokenStore;
import com.example.library.common.BusinessException;
import com.example.library.entity.Book;
import com.example.library.mapper.AdminMapper;
import com.example.library.mapper.BookMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * BookController（GET /api/books、GET /api/books/{id}）Web 层测试
 */
@WebMvcTest(BookController.class)
@Import(TokenStore.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookMapper bookMapper;

    @MockBean
    private AdminMapper adminMapper;

    @Test
    void list_success() throws Exception {
        Page<Book> page = new Page<>(1, 10);
        page.setTotal(25);
        Book book = new Book();
        book.setId(1L);
        book.setTitle("三体");
        book.setAuthor("刘慈欣");
        book.setStock(5);
        page.setRecords(Collections.singletonList(book));
        when(bookService.pageBooks(anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/api/books").param("page", "1").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(25))
                .andExpect(jsonPath("$.data.records[0].title").value("三体"))
                .andExpect(jsonPath("$.data.records[0].stock").value(5));
    }

    @Test
    void detail_success() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("三体");
        book.setAuthor("刘慈欣");
        book.setDescription("地球文明与三体文明之间的星际战争。");
        book.setStock(5);
        when(bookService.getBook(1L)).thenReturn(book);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.title").value("三体"))
                .andExpect(jsonPath("$.data.author").value("刘慈欣"))
                .andExpect(jsonPath("$.data.stock").value(5));
    }

    @Test
    void detail_notFound_returnFriendlyError() throws Exception {
        when(bookService.getBook(999L)).thenThrow(new BusinessException(400, "图书不存在"));

        mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("图书不存在"));
    }
}
