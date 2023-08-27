package test.foodtosave.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import test.foodtosave.dtos.PersonDto;
import test.foodtosave.records.PersonRecord;
import test.foodtosave.services.PersonService;

import java.util.List;

@RestController
@RequestMapping("person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping()
    public List<PersonRecord> findAll() {
        return this.personService.findAll();
    }

    @GetMapping("{id}")
    public PersonRecord findById(@PathVariable("id") Long id) {
        return this.personService.findById(id);
    }

    @PostMapping()
    public boolean create(@RequestBody PersonDto personDto) {
        return this.personService.create(personDto);
    }

    @DeleteMapping()
    public boolean deleteAll() {
        return this.personService.deleteAll();
    }

    @DeleteMapping("{id}")
    public boolean deleteById(@PathVariable("id") Long id) {
        return this.personService.deleteById(id);
    }
}
