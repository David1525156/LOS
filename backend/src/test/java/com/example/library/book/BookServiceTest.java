package com.example.library.book;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.library.common.BusinessException;
import com.example.library.entity.Book;
import com.example.library.mapper.BookMapper;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * BookService（图书列表分页 / 详情）单元测试
 */
class BookServiceTest {

    private BookMapper bookMapper;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        // 初始化实体表元数据，使 lambda 排序（orderByDesc）可解析列名
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Book.class);
        bookMapper = mock(BookMapper.class);
        bookService = new BookService(bookMapper);
    }

    private Page<Book> mockSelectPage() {
        Page<Book> page = new Page<>(1, 10);
        page.setTotal(25);
        Book book = new Book();
        book.setId(1L);
        book.setTitle("三体");
        page.setRecords(Collections.singletonList(book));
        when(bookMapper.selectPage(any(Page.class), any(Wrapper.class))).thenReturn(page);
        return page;
    }

    @Test
    void pageBooks_returnPageWithRecords() {
        mockSelectPage();
        Page<Book> result = bookService.pageBooks(1, 10);
        assertEquals(25, result.getTotal());
        assertEquals(1, result.getRecords().size());
        assertEquals("三体", result.getRecords().get(0).getTitle());
    }

    @Test
    void pageBooks_orderByCreateTimeDesc() {
        mockSelectPage();
        bookService.pageBooks(1, 10);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Wrapper<Book>> captor = ArgumentCaptor.forClass(Wrapper.class);
        verify(bookMapper).selectPage(any(Page.class), captor.capture());
        LambdaQueryWrapper<Book> wrapper = (LambdaQueryWrapper<Book>) captor.getValue();

        assertFalse(wrapper.getExpression().getOrderBy().isEmpty());
        String orderSql = wrapper.getExpression().getOrderBy().getSqlSegment();
        assertTrue(orderSql.contains("create_time"), "实际SQL: " + orderSql);
        assertTrue(orderSql.contains("DESC"), "实际SQL: " + orderSql);
    }

    @Test
    void pageBooks_invalidPageSize_normalizedToDefaults() {
        mockSelectPage();
        bookService.pageBooks(0, 0);

        ArgumentCaptor<Page<Book>> captor = ArgumentCaptor.forClass(Page.class);
        verify(bookMapper).selectPage(captor.capture(), any(Wrapper.class));
        assertEquals(1, captor.getValue().getCurrent());
        assertEquals(10, captor.getValue().getSize());
    }

    @Test
    void getBook_found_returnBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("三体");
        when(bookMapper.selectById(1L)).thenReturn(book);

        Book result = bookService.getBook(1L);
        assertEquals("三体", result.getTitle());
    }

    @Test
    void getBook_notFound_throwBusinessException() {
        when(bookMapper.selectById(999L)).thenReturn(null);

        BusinessException ex = assertThrows(BusinessException.class, () -> bookService.getBook(999L));
        assertEquals("图书不存在", ex.getMessage());
        assertEquals(400, ex.getCode());
    }
}
