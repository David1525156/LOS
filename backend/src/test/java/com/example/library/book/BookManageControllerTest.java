package com.example.library.book;

import com.example.library.auth.TokenStore;
import com.example.library.book.dto.BookCreateRequest;
import com.example.library.common.BusinessException;
import com.example.library.entity.Book;
import com.example.library.mapper.AdminMapper;
import com.example.library.mapper.BookMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * BookManageController（PUT/DELETE /api/books/{id}）Web 层测试
 */
@WebMvcTest(BookManageController.class)
@Import(TokenStore.class)
class BookManageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenStore tokenStore;

    @MockBean
    private BookManageService bookManageService;

    @MockBean
    private BookMapper bookMapper;

    @MockBean
    private AdminMapper adminMapper;

    private String validToken() {
        return tokenStore.create("admin");
    }

    @Test
    void update_withValidToken_success() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("三体全集");
        book.setAuthor("刘慈欣");
        book.setStock(9);
        when(bookManageService.update(eq(1L), any(BookCreateRequest.class))).thenReturn(book);

        mockMvc.perform(put("/api/books/1")
                        .header("Authorization", "Bearer " + validToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"三体全集\",\"author\":\"刘慈欣\",\"stock\":9}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.title").value("三体全集"));
    }

    @Test
    void update_withoutToken_return401() throws Exception {
        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"x\",\"author\":\"y\",\"stock\":1}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401));
    }

    @Test
    void update_missingTitle_returnValidationError() throws Exception {
        mockMvc.perform(put("/api/books/1")
                        .header("Authorization", "Bearer " + validToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"author\":\"刘慈欣\",\"stock\":9}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("书名不能为空"));
    }

    @Test
    void update_notExist_returnFriendlyError() throws Exception {
        when(bookManageService.update(eq(999L), any(BookCreateRequest.class)))
                .thenThrow(new BusinessException(400, "图书不存在"));

        mockMvc.perform(put("/api/books/999")
                        .header("Authorization", "Bearer " + validToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"x\",\"author\":\"y\",\"stock\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("图书不存在"));
    }

    @Test
    void delete_withValidToken_success() throws Exception {
        mockMvc.perform(delete("/api/books/5")
                        .header("Authorization", "Bearer " + validToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void delete_withoutToken_return401() throws Exception {
        mockMvc.perform(delete("/api/books/5"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401));
    }

    @Test
    void delete_notExist_returnFriendlyError() throws Exception {
        doThrow(new BusinessException(400, "图书不存在"))
                .when(bookManageService).delete(999L);

        mockMvc.perform(delete("/api/books/999")
                        .header("Authorization", "Bearer " + validToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("图书不存在"));
    }
}
