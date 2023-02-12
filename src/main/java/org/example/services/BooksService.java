package org.example.services;

import org.example.models.Book;
import org.example.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll(int page, int books_per_page, boolean sort_by_year){
        if (sort_by_year){
            return booksRepository.findAll(PageRequest.of(page, books_per_page, Sort.by("year"))).getContent();
        } else {
            return booksRepository.findAll(PageRequest.of(page, books_per_page)).getContent();
        }
    }

    public List<Book> findAll(boolean sort_by_year){
        if (sort_by_year){
            return booksRepository.findAll(Sort.by("year"));
        } else {
            return booksRepository.findAll();
        }
    }

    public Book findById(int id){
        return booksRepository.findById(id);
    }

    @Transactional
    public void save(Book book){
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book){
        book.setId(id);
        booksRepository.save(book);
    }

    @Transactional
    public void releaseBook(int id) {
        Book book = booksRepository.findById(id);
        book.setPerson(null);
        booksRepository.save(book);
    }


    public List<Book> findByTitleStartingWith(String searchString){
        return booksRepository.findByTitleStartingWith(searchString);
    }
}
