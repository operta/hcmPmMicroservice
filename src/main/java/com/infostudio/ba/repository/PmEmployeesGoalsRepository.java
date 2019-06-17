package com.infostudio.ba.repository;

import com.infostudio.ba.domain.PmEmployeesGoals;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;


/**
 * Spring Data JPA repository for the PmEmployeesGoals entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PmEmployeesGoalsRepository extends JpaRepository<PmEmployeesGoals, Long> {
    List<PmEmployeesGoals> findByIdGoalId(Long id);

    List<PmEmployeesGoals> findByIdEmployeeResponsible(Long id);

    @Query("SELECT pm FROM PmEmployeesGoals pm WHERE (?1 IS NULL OR pm.idEmployeeResponsible = ?1) AND "
        + "(?2 IS NULL OR pm.idGoal.id <= ?2) AND "
        + "(?3 IS NULL OR pm.fromDate >= ?3) AND "
        + "(?4 IS NULL OR pm.toDate <= ?4)")
    Page<PmEmployeesGoals> searchEmployeesGoals(Pageable pageable, Long employeeId, Long goalId, LocalDate dateFrom, LocalDate dateTo);
}
