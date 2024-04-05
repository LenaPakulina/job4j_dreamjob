package ru.job4j.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemoryVacancyRepository implements VacancyRepository {

    private int nextId = 1;

    private final Map<Integer, Vacancy> vacancies = new HashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "Desc1", LocalDateTime.now()));
        save(new Vacancy(0, "Junior Java Developer", "Desc2", LocalDateTime.now()));
        save(new Vacancy(0, "Junior+ Java Developer", "Desc3", LocalDateTime.now()));
        save(new Vacancy(0, "Middle Java Developer", "Desc4", LocalDateTime.now()));
        save(new Vacancy(0, "Middle+ Java Developer", "Desc5", LocalDateTime.now()));
        save(new Vacancy(0, "Senior Java Developer", "Desc6", LocalDateTime.now()));
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId++);
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public Vacancy deleteById(int id) {
        return vacancies.remove(id);
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(),
                (id, oldVacancy) -> new Vacancy(oldVacancy.getId(),
                        vacancy.getTitle(),
                        vacancy.getDescription(),
                        vacancy.getCreationDate())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}
