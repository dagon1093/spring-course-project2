package org.example.repositories;

import org.example.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {

    public List<Book> findByTitle(String title);
    public List<Book> findByTitleStartingWith(String searchString);
    public Book findById(int id);
}
