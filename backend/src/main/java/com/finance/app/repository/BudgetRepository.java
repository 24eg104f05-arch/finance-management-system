package com.finance.app.repository;

import com.finance.app.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUserIdAndMonthAndYear(Long userId, Integer month, Integer year);
    List<Budget> findByUserIdOrderByYearDescMonthDesc(Long userId);
    Optional<Budget> findByIdAndUserId(Long id, Long userId);
    Optional<Budget> findByUserIdAndCategoryIgnoreCaseAndMonthAndYear(Long userId, String category, Integer month, Integer year);
}
