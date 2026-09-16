package br.edu.ifsp.associacaogaia.service;

import br.edu.ifsp.associacaogaia.dto.TrabalhoCadastroDTO;
import br.edu.ifsp.associacaogaia.exception.ArtesaoNaoEncontradoException;
import br.edu.ifsp.associacaogaia.exception.TrabalhoNaoEncontradoException;
import br.edu.ifsp.associacaogaia.model.Artesao;
import br.edu.ifsp.associacaogaia.model.Trabalho;
import br.edu.ifsp.associacaogaia.repository.ArtesaoRepository;
import br.edu.ifsp.associacaogaia.repository.TrabalhoRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service responsável pela lógica de negócio de cadastro de trabalhos.
 *
 * Fluxo principal de cadastrarTrabalho():
 *   1. Recebe o idUsuario autenticado (extraído do token JWT pelo controller)
 *   2. Busca o Artesao vinculado a esse usuario no banco de dados
 *   3. Se não encontrar, lança ArtesaoNaoEncontradoException (→ HTTP 404)
 *   4. Cria a entidade Trabalho preenchendo todos os campos:
 *      - imagemUrl, legenda, descricao, categoria  → vêm do DTO (formulário)
 *      - artesao                                   → resolvido pelo idUsuario (segurança)
 *      - dataPublicacao                            → gerado automaticamente agora
 *   5. Persiste via TrabalhoRepository e retorna o objeto salvo
 */
@Service
public class TrabalhoService {

    private final TrabalhoRepository trabalhoRepository;
    private final ArtesaoRepository artesaoRepository;

    public TrabalhoService(TrabalhoRepository trabalhoRepository,
                           ArtesaoRepository artesaoRepository) {
        this.trabalhoRepository = trabalhoRepository;
        this.artesaoRepository = artesaoRepository;
    }

    /**
     * Cadastra um novo trabalho no portfólio do artesão autenticado.
     *
     * @param dto       dados do formulário (imagemUrl, legenda, descricao, categoria)
     * @param idUsuario ID do usuário autenticado (vem do token JWT, não do cliente)
     * @return a entidade Trabalho persistida com ID gerado
     * @throws ArtesaoNaoEncontradoException se o usuário não possui perfil de artesão
     */
    public Trabalho cadastrarTrabalho(TrabalhoCadastroDTO dto, Long idUsuario) {

        // 1. Busca o artesão pelo usuário autenticado
        Artesao artesao = artesaoRepository
                .findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(ArtesaoNaoEncontradoException::new);

        // 2. Cria a entidade Trabalho e preenche pelos setters
        Trabalho trabalho = new Trabalho();
        trabalho.setArtesao(artesao);
        trabalho.setImagemUrl(dto.getImagemUrl());
        trabalho.setLegenda(dto.getLegenda());
        trabalho.setDescricao(dto.getDescricao());       // pode ser null (campo opcional)
        trabalho.setCategoria(dto.getCategoria());       // pode ser null (campo opcional)
        trabalho.setDataPublicacao(LocalDateTime.now()); // gerado automaticamente

        // 3. Persiste e retorna o trabalho com ID preenchido pelo banco
        return trabalhoRepository.save(trabalho);
    }

    /**
     * Lista os trabalhos do portfólio do artesão autenticado, do mais recente
     * para o mais antigo. Usado pela tela "Meus Trabalhos" do front-end.
     *
     * @param idUsuario ID do usuário autenticado (vem do token JWT)
     * @throws ArtesaoNaoEncontradoException se o usuário não possui perfil de artesão
     */
    public List<Trabalho> listarTrabalhosDoArtesao(Long idUsuario) {
        Artesao artesao = artesaoRepository
                .findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(ArtesaoNaoEncontradoException::new);

        return trabalhoRepository.findByArtesao_IdArtesaoOrderByDataPublicacaoDesc(artesao.getIdArtesao());
    }

    /**
     * Atualiza os dados de um trabalho já existente no portfólio.
     *
     * A data de publicação e o artesão dono NÃO são alterados aqui — apenas os
     * campos de conteúdo do formulário (mesmos campos do cadastro).
     *
     * @param idTrabalho ID do trabalho a ser atualizado (vem da URL)
     * @param dto        novos dados do formulário
     * @param idUsuario  ID do usuário autenticado (vem do token JWT)
     * @throws TrabalhoNaoEncontradoException se não existir trabalho com esse ID
     * @throws AccessDeniedException          se o trabalho não pertencer ao artesão autenticado (→ HTTP 403)
     */
    public Trabalho atualizarTrabalho(Long idTrabalho, TrabalhoCadastroDTO dto, Long idUsuario) {
        Trabalho trabalho = trabalhoRepository.findById(idTrabalho)
                .orElseThrow(TrabalhoNaoEncontradoException::new);

        validarDono(trabalho, idUsuario);

        trabalho.setImagemUrl(dto.getImagemUrl());
        trabalho.setLegenda(dto.getLegenda());
        trabalho.setDescricao(dto.getDescricao());
        trabalho.setCategoria(dto.getCategoria());

        return trabalhoRepository.save(trabalho);
    }

    /**
     * Exclui um trabalho do portfólio do artesão autenticado.
     *
     * @param idTrabalho ID do trabalho a ser excluído (vem da URL)
     * @param idUsuario  ID do usuário autenticado (vem do token JWT)
     * @throws TrabalhoNaoEncontradoException se não existir trabalho com esse ID
     * @throws AccessDeniedException          se o trabalho não pertencer ao artesão autenticado (→ HTTP 403)
     */
    public void excluirTrabalho(Long idTrabalho, Long idUsuario) {
        Trabalho trabalho = trabalhoRepository.findById(idTrabalho)
                .orElseThrow(TrabalhoNaoEncontradoException::new);

        validarDono(trabalho, idUsuario);

        trabalhoRepository.delete(trabalho);
    }

    /**
     * Garante que o trabalho pertence ao artesão vinculado ao usuário autenticado.
     * Sem essa checagem, qualquer artesão logado poderia editar/excluir trabalhos
     * de outro artesão apenas trocando o ID na URL (falha de autorização — IDOR).
     */
    private void validarDono(Trabalho trabalho, Long idUsuario) {
        Artesao artesao = artesaoRepository
                .findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(ArtesaoNaoEncontradoException::new);

        if (!trabalho.getArtesao().getIdArtesao().equals(artesao.getIdArtesao())) {
            throw new AccessDeniedException("Você não tem permissão para alterar este trabalho.");
        }
    }
}
