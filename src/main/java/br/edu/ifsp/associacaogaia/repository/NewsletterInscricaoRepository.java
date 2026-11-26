package br.edu.ifsp.associacaogaia.repository;

import br.edu.ifsp.associacaogaia.model.NewsletterInscricao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewsletterInscricaoRepository extends JpaRepository<NewsletterInscricao, Long> {

    Optional<NewsletterInscricao> findByEmail(String email);

    /** Todas as inscrições, da mais recente para a mais antiga. */
    List<NewsletterInscricao> findAllByOrderByDataInscricaoDesc();
}
