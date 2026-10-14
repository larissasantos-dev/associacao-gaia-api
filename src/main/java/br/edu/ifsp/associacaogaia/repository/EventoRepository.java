package br.edu.ifsp.associacaogaia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifsp.associacaogaia.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    
}