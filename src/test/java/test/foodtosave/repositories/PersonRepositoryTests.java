package test.foodtosave.repositories;

/* Had problems using @JdbcTest annotation.
 Apparently, Spring was creating 1 datasource, 1 database and running Flyway against such.

 But right after, Spring was creating a different database and not using the same datasource / Flyway.
 That would force me to write sql data and not run the migrations.

 So I left it here, just in case I wonder why I did not use this annotation. */
// @JdbcTest()

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import test.foodtosave.dtos.PersonDto;
import test.foodtosave.model.Person;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.fail;

@SpringBootTest()
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PersonRepositoryTests {

    @Autowired
    PersonRepository personRepository;

    @Test
    public void shouldReturnEmptyListWhenInvokingFindAllWithoutCreatingData() {
        final List<Person> personList = personRepository.findAll();
        assertTrue(personList.isEmpty());
    }

    @Test
    public void shouldReturnSingleElementInListWhenInvokingFindAllAfterCreate() {
        personRepository.create(new PersonDto("Andrew"));

        final List<Person> personList = personRepository.findAll();
        assertEquals(1, personList.size());

        personList.stream()
                  .findFirst()
                  .ifPresentOrElse(person -> {
                      assertEquals(1L, person.getId());
                      assertEquals("Andrew", person.getName());
                  }, () -> fail("Should have returned person data."));
    }

    @Test
    public void shouldReturnSingleElementWhenInvokingGetById() {
        personRepository.create(new PersonDto("Andrew"));

        final Optional<Person> optionalPerson = personRepository.findById(1L);
        optionalPerson.ifPresentOrElse(person -> {
            assertEquals(1L, person.getId());
            assertEquals("Andrew", person.getName());
        }, () -> fail("Should have returned person data."));
    }

    @Test
    public void shouldReturnEmptyListAfterInvokingDeleteAll() {
        personRepository.create(new PersonDto("Andrew"));
        personRepository.create(new PersonDto("James"));
        personRepository.create(new PersonDto("Cristina"));

        final List<Person> personListBeforeDelete = personRepository.findAll();
        assertFalse(personListBeforeDelete.isEmpty());

        personRepository.deleteAll();

        final List<Person> personListAfterDelete = personRepository.findAll();
        assertTrue(personListAfterDelete.isEmpty());
    }

    @Test
    public void shouldReturnOptionalNotPresentAfterInvokingDeleteById() {
        personRepository.create(new PersonDto("Andrew"));
        personRepository.create(new PersonDto("James"));
        personRepository.create(new PersonDto("Cristina"));

        final Optional<Person> personBeforeDelete = personRepository.findById(1L);
        assertTrue(personBeforeDelete.isPresent());

        personRepository.deleteById(1L);

        final Optional<Person> personAfterDelete = personRepository.findById(1L);
        assertFalse(personAfterDelete.isPresent());
    }

    @Test
    public void shouldDeleteSpecificRecordWhenInvokingDeleteById() {
        personRepository.create(new PersonDto("Andrew"));
        personRepository.create(new PersonDto("James"));
        personRepository.create(new PersonDto("Cristina"));

        final List<Person> personListBeforeDelete = personRepository.findAll();
        assertEquals(3, personListBeforeDelete.size());

        personRepository.deleteById(1L);

        final List<Person> personListAfterDelete = personRepository.findAll();
        assertEquals(2, personListAfterDelete.size());

    }
}
