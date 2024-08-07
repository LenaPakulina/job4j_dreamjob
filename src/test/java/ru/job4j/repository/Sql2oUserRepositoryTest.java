package ru.job4j.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.configuration.DatasourceConfiguration;
import ru.job4j.model.User;
import ru.job4j.repository.impl.sql.Sql2oUserRepository;

import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oUserRepositoryTest {
    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        sql2oUserRepository.deleteAll();
    }

    @Test
    public void whenSaveThenGetSame() {
        User user = sql2oUserRepository.save(new User(0, "dog@mail.ru", "login", "password")).get();
        User savedUser = sql2oUserRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()).get();
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void whenSaveCopyEmails() {
        String email = "dog@mail.ru";
        User user1 = new User(0, email, "login", "password");
        User user2 = new User(0, email, "login1", "password1");
        sql2oUserRepository.save(user1);
        User savedUser1 = sql2oUserRepository.findByEmailAndPassword(user1.getEmail(), user1.getPassword()).get();
        assertThat(savedUser1).usingRecursiveComparison().isEqualTo(user1);
        assertThat(sql2oUserRepository.save(user2).isEmpty()).isTrue();
    }
}