package org.example.services;

import org.example.models.Book;
import org.example.models.Person;
import org.example.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(Person person){
        peopleRepository.delete(person);
    }

    @Transactional
    public void update(int id, Person person){
        person.setId(id);
        peopleRepository.save(person);
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    public Person findById(int id){
        return peopleRepository.findById(id).orElse(null);
    }

    public Person findByName(String name) {
        return peopleRepository.findByName(name);
    }

    @Transactional
    public List<Book> getBooksByPersonId(int id){
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()){
            Hibernate.initialize(person.get().getBooks());


            person.get().getBooks().forEach(book -> {

                if (book.getTakenAt() == null){
                    book.setTakenAt(new Date());
                }

                long diffInMillis = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                if (diffInMillis > 864000000){
                    book.setExpired(true);
                }
            });
            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }

    }


}
