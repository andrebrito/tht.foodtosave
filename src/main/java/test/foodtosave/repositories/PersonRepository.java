package test.foodtosave.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import test.foodtosave.dtos.PersonDto;
import test.foodtosave.model.Person;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component
public class PersonRepository {

    private final NamedParameterJdbcTemplate JDBC_TEMPLATE;

    private final RowMapper<Person> MAPPER =
            (resultSet, i) ->
                    new Person(resultSet.getLong("id"),
                               resultSet.getString("name"),
                               resultSet.getTimestamp("created_at")
                                        .toLocalDateTime());

    @Autowired
    public PersonRepository(DataSource dataSource) {
        this.JDBC_TEMPLATE = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Person> findAll() {
        String FIND_ALL = "SELECT * FROM person";
        return this.JDBC_TEMPLATE.query(FIND_ALL, MAPPER);
    }

    public Optional<Person> findById(Long id) {
        SqlParameterSource namedParameter = new MapSqlParameterSource().addValue("id", id);

        String FIND_BY_ID = "SELECT * FROM person WHERE id = :id";
        List<Person> personList = this.JDBC_TEMPLATE.query(FIND_BY_ID, namedParameter, MAPPER);

        if (personList.isEmpty()) {
            return Optional.empty();
        }

        if (personList.size() == 1) {
            return personList.stream()
                             .findFirst();
        }

        throw new IncorrectResultSizeDataAccessException(1, personList.size());
    }

    public boolean create(PersonDto personDto) {
        SqlParameterSource namedParameter =
                new MapSqlParameterSource().addValue("name", personDto.name());

        String INSERT = "INSERT INTO person (name) VALUES(:name)";
        return this.JDBC_TEMPLATE.update(INSERT, namedParameter) == 1;
    }

    public boolean deleteById(Long id) {
        SqlParameterSource namedParameter = new MapSqlParameterSource().addValue("id", id);

        String DELETE = "DELETE FROM person WHERE id = :id";
        return this.JDBC_TEMPLATE.update(DELETE, namedParameter) >= 0;
    }

    public boolean deleteAll() {
        String DELETE = "DELETE FROM person";
        return this.JDBC_TEMPLATE.update(DELETE, new MapSqlParameterSource()) >= 0;
    }

}
