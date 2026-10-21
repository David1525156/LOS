package com.example.library.book;

import com.example.library.book.dto.BookCreateRequest;
import com.example.library.common.BusinessException;
import com.example.library.entity.Book;
import com.example.library.mapper.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * BookManageService（修改 / 删除图书）单元测试
 */
class BookManageServiceTest {

    private BookMapper bookMapper;
    private BookManageService bookManageService;

    @BeforeEach
    void setUp() {
        bookMapper = mock(BookMapper.class);
        bookManageService = new BookManageService(bookMapper);
    }

    @Test
    void update_exist_trimAndSave() {
        Book exist = new Book();
        exist.setId(1L);
        exist.setTitle("三体");
        when(bookMapper.selectById(1L)).thenReturn(exist);
        when(bookMapper.updateById(any(Book.class))).thenReturn(1);

        BookCreateRequest request = new BookCreateRequest();
        request.setTitle("  三体全集  ");
        request.setAuthor("  刘慈欣  ");
        request.setDescription("修订版");
        request.setStock(9);

        Book result = bookManageService.update(1L, request);

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookMapper).updateById(captor.capture());
        Book saved = captor.getValue();
        assertEquals("三体全集", saved.getTitle());
        assertEquals("刘慈欣", saved.getAuthor());
        assertEquals("修订版", saved.getDescription());
        assertEquals(9, saved.getStock());
        assertEquals(1L, result.getId());
    }

    @Test
    void update_notExist_throwBusinessException() {
        when(bookMapper.selectById(999L)).thenReturn(null);

        BookCreateRequest request = new BookCreateRequest();
        request.setTitle("x");
        request.setAuthor("y");
        request.setStock(1);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> bookManageService.update(999L, request));
        assertEquals("图书不存在", ex.getMessage());
        verify(bookMapper).selectById(999L);
    }

    @Test
    void delete_exist_deleteById() {
        Book exist = new Book();
        exist.setId(5L);
        when(bookMapper.selectById(5L)).thenReturn(exist);

        bookManageService.delete(5L);

        verify(bookMapper).deleteById(5L);
    }

    @Test
    void delete_notExist_throwBusinessException() {
        when(bookMapper.selectById(999L)).thenReturn(null);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> bookManageService.delete(999L));
        assertEquals("图书不存在", ex.getMessage());
        verify(bookMapper).selectById(999L);
    }
}
