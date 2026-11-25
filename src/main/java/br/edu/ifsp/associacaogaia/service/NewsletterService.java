package br.edu.ifsp.associacaogaia.service;

import br.edu.ifsp.associacaogaia.exception.InscricaoNaoEncontradaException;
import br.edu.ifsp.associacaogaia.model.NewsletterInscricao;
import br.edu.ifsp.associacaogaia.repository.NewsletterInscricaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Regras da newsletter da Associação Gaia.
 *
 * Decisão do grupo: as inscrições ficam no nosso próprio banco (tabela
 * newsletter_inscricao). O administrador consulta e exporta a lista no painel
 * e usa o e-mail que preferir para enviar as novidades.
 *
 * A inscrição é idempotente: se o e-mail já estiver ativo, nada muda e a
 * resposta é a mesma (não revela se o e-mail já existia).
 */
@Service
public class NewsletterService {

    private final NewsletterInscricaoRepository repository;

    public NewsletterService(NewsletterInscricaoRepository repository) {
        this.repository = repository;
    }

    /**
     * Inscreve um e-mail na newsletter. Se ele já estava cadastrado e ativo,
     * nada é alterado; se havia cancelado, a inscrição é reativada.
     *
     * @return a inscrição ativa
     */
    public NewsletterInscricao inscrever(String emailInformado) {
        String email = normalizar(emailInformado);

        Optional<NewsletterInscricao> existente = repository.findByEmail(email);

        if (existente.isPresent()) {
            NewsletterInscricao inscricao = existente.get();

            if (!inscricao.isAtivo()) {
                inscricao.reativar();
                return repository.save(inscricao);
            }
            return inscricao;
        }

        return repository.save(new NewsletterInscricao(email));
    }

    /**
     * Cancela a inscrição de um e-mail. Se o e-mail não existe, não faz nada
     * (a resposta pública é a mesma nos dois casos).
     */
    public void cancelar(String emailInformado) {
        String email = normalizar(emailInformado);

        repository.findByEmail(email).ifPresent(inscricao -> {
            if (inscricao.isAtivo()) {
                inscricao.cancelar();
                repository.save(inscricao);
            }
        });
    }

    /** Lista todas as inscrições (ativas e canceladas) para o painel administrativo. */
    public List<NewsletterInscricao> listar() {
        return repository.findAllByOrderByDataInscricaoDesc();
    }

    /**
     * Remove definitivamente uma inscrição (administrador).
     *
     * @throws InscricaoNaoEncontradaException se o ID não existir
     */
    public void remover(Long idInscricao) {
        NewsletterInscricao inscricao = repository.findById(idInscricao)
                .orElseThrow(InscricaoNaoEncontradaException::new);

        repository.delete(inscricao);
    }

    private String normalizar(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
