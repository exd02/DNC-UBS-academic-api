package com.ubs.dnc.education.academic_api.repository;

import com.ubs.dnc.education.academic_api.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

}