package spring.Project2Boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.Project2Boot.models.Book;
import spring.Project2Boot.repositories.BookRepository;


import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    public Book findOne(int id){
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Book book){
        book.setCreatedAt(new Date());
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook){
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id){
        bookRepository.deleteById(id);
    }

    public List<Book> findAll(Pageable pageable){
        return bookRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize())).getContent();
    }
    
    public List<Book> findAll(Sort var1){
        return bookRepository.findAll(var1);
    }

    public List<Book> findAll(Pageable pageable, Sort var1){
        return bookRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), var1)).getContent();
    }

    public List<Book> findByTitleStartingWith(String title){
        return bookRepository.findByTitleStartingWith(title);
    }

}
