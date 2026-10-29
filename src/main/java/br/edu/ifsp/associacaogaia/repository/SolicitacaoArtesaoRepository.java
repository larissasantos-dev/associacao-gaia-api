package br.edu.ifsp.associacaogaia.repository;

import br.edu.ifsp.associacaogaia.model.SolicitacaoArtesao;
import br.edu.ifsp.associacaogaia.model.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SolicitacaoArtesaoRepository extends JpaRepository<SolicitacaoArtesao, Long> {

    boolean existsByUsuario_IdUsuarioAndStatus(Long idUsuario, StatusSolicitacao status);

    Optional<SolicitacaoArtesao> findFirstByUsuario_IdUsuarioOrderByDataSolicitacaoDesc(Long idUsuario);
}