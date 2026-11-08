package br.edu.ifsp.associacaogaia.repository;

import br.edu.ifsp.associacaogaia.model.SolicitacaoArtesao;
import br.edu.ifsp.associacaogaia.model.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SolicitacaoArtesaoRepository extends JpaRepository<SolicitacaoArtesao, Long> {

    boolean existsByUsuario_IdUsuarioAndStatus(Long idUsuario, StatusSolicitacao status);

    Optional<SolicitacaoArtesao> findFirstByUsuario_IdUsuarioOrderByDataSolicitacaoDesc(Long idUsuario);

    // Fila do administrador: as mais antigas primeiro
    List<SolicitacaoArtesao> findByStatusOrderByDataSolicitacaoAsc(StatusSolicitacao status);

    // Sem filtro de status: as mais recentes primeiro
    List<SolicitacaoArtesao> findAllByOrderByDataSolicitacaoDesc();
}