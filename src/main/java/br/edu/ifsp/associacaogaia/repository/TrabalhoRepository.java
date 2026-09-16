package br.edu.ifsp.associacaogaia.repository;

import br.edu.ifsp.associacaogaia.model.Trabalho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrabalhoRepository extends JpaRepository<Trabalho, Long> {

    /**
     * Lista os trabalhos de um artesão, do mais recente para o mais antigo.
     * Usado para montar o portfólio do artesão autenticado (GET /api/trabalhos/meus).
     */
    List<Trabalho> findByArtesao_IdArtesaoOrderByDataPublicacaoDesc(Long idArtesao);
}