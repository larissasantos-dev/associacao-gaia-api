package br.edu.ifsp.associacaogaia.repository;

import br.edu.ifsp.associacaogaia.model.HistoricoPreco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoPrecoRepository extends JpaRepository<HistoricoPreco, Long> {

    /** Histórico de preços de um trabalho, do mais recente para o mais antigo. */
    List<HistoricoPreco> findByTrabalho_IdTrabalhoOrderByDataAlteracaoDesc(Long idTrabalho);
}
