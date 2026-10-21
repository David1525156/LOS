package com.example.library.book;

import com.example.library.book.dto.BookCreateRequest;
import com.example.library.entity.Book;
import com.example.library.mapper.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * BookCreateService（新增图书 US-03）单元测试
 */
class BookCreateServiceTest {

    private BookMapper bookMapper;
    private BookCreateService bookCreateService;

    @BeforeEach
    void setUp() {
        bookMapper = mock(BookMapper.class);
        bookCreateService = new BookCreateService(bookMapper);
    }

    @Test
    void create_trimTitleAndAuthor_insertBook() {
        when(bookMapper.insert(any(Book.class))).thenReturn(1);

        BookCreateRequest request = new BookCreateRequest();
        request.setTitle("  三体全集  ");
        request.setAuthor("  刘慈欣  ");
        request.setDescription("经典科幻");
        request.setStock(3);

        Book result = bookCreateService.create(request);

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookMapper).insert(captor.capture());
        Book saved = captor.getValue();
        assertEquals("三体全集", saved.getTitle());
        assertEquals("刘慈欣", saved.getAuthor());
        assertEquals("经典科幻", saved.getDescription());
        assertEquals(3, saved.getStock());
        assertNotNull(saved.getCreateTime());
        assertTrue(result == saved);
    }

    @Test
    void create_zeroStock_allowed() {
        when(bookMapper.insert(any(Book.class))).thenReturn(1);

        BookCreateRequest request = new BookCreateRequest();
        request.setTitle("绝版书");
        request.setAuthor("作者");
        request.setStock(0);

        bookCreateService.create(request);

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookMapper).insert(captor.capture());
        assertEquals(0, captor.getValue().getStock());
    }
}
