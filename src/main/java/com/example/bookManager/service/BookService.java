package com.example.bookManager.service;

import com.example.bookManager.dao.BookDAO;
import com.example.bookManager.model.Book;
import com.example.bookManager.model.enums.BookStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookDAO bookDAO;

    /**
     * 封装 bookDAO, 使用 bookService 代理
     * 好处:
     *  1. 分层，将一些功能统一处理, 例如异常处理可以设置在 service 层, 不用再关心底层的异常
     *  2. 使用代理, 可以实现: 多态、封装、重载(不同层关心的功能不一样, 例如,DAO 层只关心和数据库打交道, 而 service 层关心的是功能)
     * @return
     */
    public List<Book> getAllBooks() {
        return bookDAO.selectAll();
    }

    public int addBooks(Book book) {
        return bookDAO.addBook(book);
    }

    public void deleteBooks(int id) {
        bookDAO.updateBookStatus(id, BookStatusEnum.DELETE.getValue());
    }

    public void recoverBooks(int id) {
        bookDAO.updateBookStatus(id, BookStatusEnum.NORMAL.getValue());
    }
}
