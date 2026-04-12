package hiddencore.ddasum.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hiddencore.ddasum.backend.domain.MealPlan;

@Repository
public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {

    List<MealPlan> findByMealDateOrderByMealType(LocalDate mealDate);

    List<MealPlan> findByMealDateBetweenOrderByMealDate(LocalDate startDate, LocalDate endDate);

    Optional<MealPlan> findByMealDateAndMealTypeAndDietType(
            LocalDate mealDate,
            MealPlan.Mealtype mealType,
            MealPlan.Diettype dietType
    );
}
