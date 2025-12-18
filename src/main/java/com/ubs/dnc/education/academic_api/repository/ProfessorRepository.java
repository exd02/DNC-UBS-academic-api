package com.ubs.dnc.education.academic_api.repository;

import com.ubs.dnc.education.academic_api.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

}