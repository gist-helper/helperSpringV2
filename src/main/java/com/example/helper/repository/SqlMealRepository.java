package com.example.helper.repository;

import com.example.helper.entity.Meal;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SqlMealRepository implements MealRepository {
    private final EntityManager em;
    public SqlMealRepository(EntityManager em) {
        this.em = em;
    }
    @Override
    public Meal save(Meal meal) {
        em.persist(meal);
        return meal;
    }

    @Override
    public Optional<Meal> findByPk(Integer bldgType, Integer langType, Integer dateType, Integer kindType, String date) {
        List<Meal> result = em.createQuery("select m from Meal m where " +
                        "m.bldgType = :bldgType and m.langType = :langType and m.dateType = :dateType " +
                        "and m.kindType = :kindType and m.date = :date", Meal.class)
                .setParameter("bldgType", bldgType)
                .setParameter("langType", langType)
                .setParameter("dateType", dateType)
                .setParameter("kindType", kindType)
                .setParameter("date", date)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public Optional<Meal> findById(Long mealId) {
        Meal meal = em.find(Meal.class, mealId);
        return Optional.ofNullable(meal);
    }

    @Override
    public Long delete(Long mealId) {
        return null;
    }
}
