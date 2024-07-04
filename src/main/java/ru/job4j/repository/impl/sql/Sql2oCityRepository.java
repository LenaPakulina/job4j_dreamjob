package ru.job4j.repository.impl.sql;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.model.City;
import ru.job4j.repository.CityRepository;

import java.util.Collection;

@Repository
public class Sql2oCityRepository implements CityRepository {
    private final Sql2o sql2o;

    public Sql2oCityRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Collection<City> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM cities");
            return query.executeAndFetch(City.class);
        }
    }
}