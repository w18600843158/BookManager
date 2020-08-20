package com.example.bookManager.controllers;

import com.example.bookManager.model.Book;
import com.example.bookManager.model.User;
import com.example.bookManager.service.BookService;
import com.example.bookManager.service.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private HostHolder hostHolder;

    @GetMapping(path = {"/index"})
    public String bookList(Model model) {
        User host = hostHolder.getUser();
        if (host != null) {
            model.addAttribute("host", host);
        }
        return "book/books";
    }


    @RequestMapping(path = {"/books/add"}, method = {RequestMethod.GET})
    public String addBook() {
        return "book/addbook";
    }

    @RequestMapping(path = {"/books/add/do"}, method = {RequestMethod.POST})
    public String doAddBook(
            @RequestParam("name") String name,
            @RequestParam("author") String author,
            @RequestParam("price") String price
    ) {

        Book book = new Book();
        book.setName(name);
        book.setAuthor(author);
        book.setPrice(price);
        bookService.addBooks(book);

        return "redirect:/index";

    }


    @RequestMapping(path = {"/books/{bookId:[0-9]+}/delete"}, method = {RequestMethod.GET})
    public String deleteBook(
            @PathVariable("bookId") int bookId
    ) {

        bookService.deleteBooks(bookId);
        return "redirect:/index";

    }


    @RequestMapping(path = {"/books/{bookId:[0-9]+}/recover"}, method = {RequestMethod.GET})
    public String recoverBook(
            @PathVariable("bookId") int bookId
    ) {

        bookService.recoverBooks(bookId);
        return "redirect:/index";

    }


    /**
     * 为model加载所有的book
     */
    private void loadAllBooksView(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
    }
}
