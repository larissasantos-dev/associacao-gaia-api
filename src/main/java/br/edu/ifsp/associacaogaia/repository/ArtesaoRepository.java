package br.edu.ifsp.associacaogaia.repository;

import br.edu.ifsp.associacaogaia.model.Artesao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtesaoRepository extends JpaRepository<Artesao, Long> {

    /**
     * Busca o perfil de artesão vinculado ao usuário de ID informado.
     * Usado pelo TrabalhoService para identificar o artesão autenticado a partir do token JWT.
     *
     * Spring Data JPA gera o SQL automaticamente pelo nome do método:
     *   SELECT * FROM artesao WHERE id_usuario = :idUsuario
     */
    Optional<Artesao> findByUsuario_IdUsuario(Long idUsuario);
}
