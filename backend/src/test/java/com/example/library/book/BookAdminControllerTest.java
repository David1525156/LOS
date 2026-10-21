package com.example.library.book;

import com.example.library.auth.TokenStore;
import com.example.library.book.dto.BookCreateRequest;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * BookAdminController（POST /api/books 新增图书 US-03）Web 层测试
 */
@WebMvcTest(BookAdminController.class)
@Import(TokenStore.class)
class BookAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenStore tokenStore;

    @MockBean
    private BookCreateService bookCreateService;

    @MockBean
    private BookMapper bookMapper;

    @MockBean
    private AdminMapper adminMapper;

    private String validToken() {
        return tokenStore.create("admin");
    }

    @Test
    void create_withValidToken_success() throws Exception {
        Book book = new Book();
        book.setId(26L);
        book.setTitle("三体全集");
        book.setAuthor("刘慈欣");
        book.setDescription("经典科幻");
        book.setStock(3);
        when(bookCreateService.create(any(BookCreateRequest.class))).thenReturn(book);

        mockMvc.perform(post("/api/books")
                        .header("Authorization", "Bearer " + validToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"三体全集\",\"author\":\"刘慈欣\",\"description\":\"经典科幻\",\"stock\":3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.title").value("三体全集"))
                .andExpect(jsonPath("$.data.stock").value(3));

        verify(bookCreateService).create(any(BookCreateRequest.class));
    }

    @Test
    void create_withoutToken_return401() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"三体全集\",\"author\":\"刘慈欣\",\"stock\":3}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("未登录或登录已过期"));
    }

    @Test
    void create_withInvalidToken_return401() throws Exception {
        mockMvc.perform(post("/api/books")
                        .header("Authorization", "Bearer invalid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"三体全集\",\"author\":\"刘慈欣\",\"stock\":3}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401));
    }

    @Test
    void create_missingTitle_returnValidationError() throws Exception {
        mockMvc.perform(post("/api/books")
                        .header("Authorization", "Bearer " + validToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"author\":\"刘慈欣\",\"stock\":3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("书名不能为空"));
    }

    @Test
    void create_missingStock_returnValidationError() throws Exception {
        mockMvc.perform(post("/api/books")
                        .header("Authorization", "Bearer " + validToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"三体全集\",\"author\":\"刘慈欣\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("库存不能为空"));
    }

    @Test
    void create_negativeStock_returnValidationError() throws Exception {
        mockMvc.perform(post("/api/books")
                        .header("Authorization", "Bearer " + validToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"三体全集\",\"author\":\"刘慈欣\",\"stock\":-1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("库存不能小于0"));
    }
}
