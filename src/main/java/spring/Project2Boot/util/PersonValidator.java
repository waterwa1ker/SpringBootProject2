package spring.Project2Boot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import spring.Project2Boot.models.Person;
import spring.Project2Boot.services.PeopleService;


import java.util.Optional;


@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;
        Optional<Person> optionalPerson = Optional.ofNullable(peopleService.findByFullName(person.getFullName()));
        if (optionalPerson.isPresent())
            errors.rejectValue("fullName", "", "Человек с таким ФИО уже существует");
    }
}
