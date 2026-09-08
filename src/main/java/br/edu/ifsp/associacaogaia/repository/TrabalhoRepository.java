package br.edu.ifsp.associacaogaia.repository;

import br.edu.ifsp.associacaogaia.model.Trabalho;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrabalhoRepository extends JpaRepository<Trabalho, Integer> {
}