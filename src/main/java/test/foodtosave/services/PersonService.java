package test.foodtosave.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import test.foodtosave.dtos.PersonDto;
import test.foodtosave.model.Person;
import test.foodtosave.records.PersonRecord;
import test.foodtosave.repositories.PersonRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final Logger LOGGER = Logger.getLogger(PersonService.class.getName());

    @Autowired
    private PersonRepository personRepository;

    @Cacheable(value = "person")
    public List<PersonRecord> findAll() {
        this.LOGGER.info("Querying database...");

        final List<Person> records = this.personRepository.findAll();
        return records.stream()
                      .map(record -> new PersonRecord(record.getId(),
                                                      record.getName(),
                                                      this.formatDateTime(record.getCreatedAt())))
                      .collect(Collectors.toList());
    }

    public PersonRecord findById(Long id) {
        final Optional<Person> optionalPerson = this.personRepository.findById(id);

        if (optionalPerson.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found.");
        }

        final Person person = optionalPerson.get();
        return new PersonRecord(person.getId(), person.getName(), this.formatDateTime(person.getCreatedAt()));
    }

    @CacheEvict(value = "person", allEntries = true)
    public boolean create(PersonDto personDto) {
        return this.personRepository.create(personDto);
    }

    @CacheEvict(value = "person", allEntries = true)
    public boolean deleteAll() {
        return this.personRepository.deleteAll();
    }

    @CacheEvict(value = "person", allEntries = true)
    public boolean deleteById(Long id) {
        return this.personRepository.deleteById(id);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(this.FORMATTER);
    }

}
