package test.foodtosave.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

import test.foodtosave.model.Person;
import test.foodtosave.records.PersonRecord;
import test.foodtosave.repositories.PersonRepository;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTests {

    @Mock
    PersonRepository mockedPersonRepository;

    @InjectMocks
    PersonService personService = new PersonService();

    @Test()
    public void shouldInvokeRepositoryFindByIdWhenInvokingFindById() {
        doReturn(of(new Person(1L, "Andre", now()))).when(mockedPersonRepository)
                                                    .findById(1L);

        personService.findById(1L);

        verify(mockedPersonRepository).findById(1L);
    }

    @Test()
    public void shouldReturnPersonRecordWhenInvokingFindById() {
        doReturn(of(new Person(1L, "Andre", now()))).when(mockedPersonRepository)
                                                    .findById(1L);

        final PersonRecord personRecord = personService.findById(1L);
        assertEquals(personRecord.id(), Long.valueOf(1));
        assertEquals(personRecord.name(), "Andre");
    }

    @Test()
    public void shouldThrowExceptionWhenNoRecordIsFoundInFindById() {
        doReturn(empty()).when(mockedPersonRepository)
                         .findById(1L);

        assertThrows(ResponseStatusException.class, () -> personService.findById(1L));
    }

    @Test()
    public void shouldInvokeRepositoryFindAllWhenInvokingFindAll() {
        personService.findAll();

        verify(mockedPersonRepository).findAll();
    }

    @Test()
    public void shouldReturnPersonRecordListWhenInvokingFindAll() {
        final List<Person> personList = new ArrayList<>() {
            {
                add(new Person(1L, "Andre", now()));
                add(new Person(2L, "Lucas", now()));
            }
        };

        when(mockedPersonRepository.findAll()).thenReturn(personList);

        final List<PersonRecord> returned = personService.findAll();
        assertNotNull(returned);
        assertEquals(2, returned.size());
        assertEquals(returned.get(0)
                             .id(), Long.valueOf(1));
        assertEquals(returned.get(1)
                             .id(), Long.valueOf(2));
    }

    @Test()
    public void shouldInvokeRepositoryDeleteWhenInvokingDeleteAll() {
        personService.deleteAll();

        verify(mockedPersonRepository).deleteAll();

    }

    @Test()
    public void shouldInvokeRepositoryDeleteByIdWhenInvokingDeleteById() {
        personService.deleteById(1L);

        verify(mockedPersonRepository).deleteById(1L);

    }
}
