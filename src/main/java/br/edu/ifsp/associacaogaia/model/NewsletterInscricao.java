package br.edu.ifsp.associacaogaia.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Inscrição de um visitante na newsletter da Associação Gaia.
 *
 * O e-mail é único. Quando a pessoa cancela, a inscrição não é apagada:
 * fica com ativo = false e a data de cancelamento preenchida, e pode ser
 * reativada se ela se inscrever novamente.
 */
@Entity
@Table(name = "newsletter_inscricao")
public class NewsletterInscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscricao")
    private Long idInscricao;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    @Column(name = "data_inscricao", nullable = false)
    private LocalDateTime dataInscricao;

    @Column(name = "data_cancelamento")
    private LocalDateTime dataCancelamento;

    protected NewsletterInscricao() {
    }

    public NewsletterInscricao(String email) {
        this.email = email;
        this.ativo = true;
        this.dataInscricao = LocalDateTime.now();
    }

    /** Cancela a inscrição (mantém o registro). */
    public void cancelar() {
        this.ativo = false;
        this.dataCancelamento = LocalDateTime.now();
    }

    /** Reativa uma inscrição que havia sido cancelada. */
    public void reativar() {
        this.ativo = true;
        this.dataInscricao = LocalDateTime.now();
        this.dataCancelamento = null;
    }

    public Long getIdInscricao() {
        return idInscricao;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }

    public LocalDateTime getDataCancelamento() {
        return dataCancelamento;
    }
}
