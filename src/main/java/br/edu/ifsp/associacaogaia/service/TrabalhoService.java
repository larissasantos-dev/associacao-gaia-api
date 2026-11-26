package br.edu.ifsp.associacaogaia.service;

import br.edu.ifsp.associacaogaia.dto.TrabalhoCadastroDTO;
import br.edu.ifsp.associacaogaia.dto.TrabalhoUploadDTO;
import br.edu.ifsp.associacaogaia.exception.ArtesaoNaoEncontradoException;
import br.edu.ifsp.associacaogaia.exception.LimiteTrabalhosException;
import br.edu.ifsp.associacaogaia.exception.TrabalhoNaoEncontradoException;
import br.edu.ifsp.associacaogaia.model.Artesao;
import br.edu.ifsp.associacaogaia.model.HistoricoPreco;
import br.edu.ifsp.associacaogaia.model.Trabalho;
import br.edu.ifsp.associacaogaia.repository.ArtesaoRepository;
import br.edu.ifsp.associacaogaia.repository.HistoricoPrecoRepository;
import br.edu.ifsp.associacaogaia.repository.TrabalhoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service responsável pela lógica de negócio dos trabalhos (cards) dos artesãos.
 *
 * Fluxo principal de cadastro:
 *   1. Recebe o idUsuario autenticado (extraído do token JWT pelo controller)
 *   2. Busca o Artesao vinculado a esse usuario no banco de dados
 *   3. Se não encontrar, lança ArtesaoNaoEncontradoException (→ HTTP 404)
 *   4. Confere o limite de trabalhos por artesão (→ HTTP 409 se já estiver no limite)
 *   5. Envia a foto ao Cloudinary (cadastro com imagem) e cria a entidade Trabalho:
 *      - imagem, legenda, descricao, categoria, preco → vêm do formulário
 *      - artesao                                      → resolvido pelo idUsuario (segurança)
 *      - dataPublicacao                               → gerado automaticamente agora
 *   6. Persiste via TrabalhoRepository e retorna o objeto salvo
 *
 * Toda mudança de preço é registrada em historico_preco (ver {@link Trabalho#alterarPreco}).
 */
@Service
public class TrabalhoService {

    private static final Logger log = LoggerFactory.getLogger(TrabalhoService.class);

    private final TrabalhoRepository trabalhoRepository;
    private final ArtesaoRepository artesaoRepository;
    private final HistoricoPrecoRepository historicoPrecoRepository;
    private final ImagemStorageService imagemStorageService;

    /**
     * Quantidade máxima de trabalhos (cards) por artesão.
     * Configurável em application.properties (gaia.trabalho.limite-por-artesao);
     * se a propriedade não existir, vale 8.
     */
    @Value("${gaia.trabalho.limite-por-artesao:8}")
    private int limitePorArtesao = 8;

    public TrabalhoService(TrabalhoRepository trabalhoRepository,
                           ArtesaoRepository artesaoRepository,
                           HistoricoPrecoRepository historicoPrecoRepository,
                           ImagemStorageService imagemStorageService) {
        this.trabalhoRepository = trabalhoRepository;
        this.artesaoRepository = artesaoRepository;
        this.historicoPrecoRepository = historicoPrecoRepository;
        this.imagemStorageService = imagemStorageService;
    }

    /**
     * Cadastra um novo trabalho (com link de imagem) no portfólio do artesão autenticado.
     *
     * @param dto       dados do formulário (imagemUrl, legenda, descricao, categoria, preco)
     * @param idUsuario ID do usuário autenticado (vem do token JWT, não do cliente)
     * @return a entidade Trabalho persistida com ID gerado
     * @throws ArtesaoNaoEncontradoException se o usuário não possui perfil de artesão
     * @throws LimiteTrabalhosException      se o artesão já atingiu o limite de trabalhos
     */
    public Trabalho cadastrarTrabalho(TrabalhoCadastroDTO dto, Long idUsuario) {

        // 1. Busca o artesão pelo usuário autenticado
        Artesao artesao = buscarArtesao(idUsuario);

        // 1.1 Regra do grupo: um artesão não pode ultrapassar o limite de trabalhos
        validarLimite(artesao);

        // 2. Cria a entidade Trabalho e preenche pelos setters
        Trabalho trabalho = new Trabalho();
        trabalho.setArtesao(artesao);
        trabalho.setImagemUrl(dto.getImagemUrl());
        trabalho.setLegenda(dto.getLegenda());
        trabalho.setDescricao(dto.getDescricao());       // pode ser null (campo opcional)
        trabalho.setCategoria(dto.getCategoria());       // pode ser null (campo opcional)
        trabalho.setDataPublicacao(LocalDateTime.now()); // gerado automaticamente
        trabalho.alterarPreco(dto.getPreco());           // pode ser null (sob consulta)

        // 3. Persiste e retorna o trabalho com ID preenchido pelo banco
        return trabalhoRepository.save(trabalho);
    }

    /**
     * Cadastra um novo trabalho enviando a FOTO (arquivo) para o Cloudinary.
     * É o fluxo usado pela página "Meus trabalhos" do artesão.
     *
     * Se o banco falhar depois do upload, a foto recém-enviada é apagada
     * para não deixar arquivo órfão no Cloudinary.
     *
     * @throws ArtesaoNaoEncontradoException se o usuário não possui perfil de artesão
     * @throws LimiteTrabalhosException      se o artesão já atingiu o limite de trabalhos
     */
    public Trabalho cadastrarTrabalhoComImagem(TrabalhoUploadDTO dto, Long idUsuario) {
        Artesao artesao = buscarArtesao(idUsuario);
        validarLimite(artesao);

        ImagemStorageService.ResultadoUpload imagem =
                imagemStorageService.uploadTrabalho(dto.getImagem());

        try {
            Trabalho trabalho = new Trabalho();
            trabalho.setArtesao(artesao);
            trabalho.setImagemUrl(imagem.url());
            trabalho.setImagemPublicId(imagem.publicId());
            trabalho.setLegenda(dto.getLegenda());
            trabalho.setDescricao(dto.getDescricao());
            trabalho.setCategoria(dto.getCategoria());
            trabalho.setDataPublicacao(LocalDateTime.now());
            trabalho.alterarPreco(dto.getPreco());

            return trabalhoRepository.save(trabalho);
        } catch (RuntimeException e) {
            excluirImagemComSeguranca(imagem.publicId());
            throw e;
        }
    }

    /**
     * Lista os trabalhos do portfólio do artesão autenticado, do mais recente
     * para o mais antigo. Usado pela tela "Meus Trabalhos" do front-end.
     *
     * @param idUsuario ID do usuário autenticado (vem do token JWT)
     * @throws ArtesaoNaoEncontradoException se o usuário não possui perfil de artesão
     */
    public List<Trabalho> listarTrabalhosDoArtesao(Long idUsuario) {
        Artesao artesao = buscarArtesao(idUsuario);

        return trabalhoRepository.findByArtesao_IdArtesaoOrderByDataPublicacaoDesc(artesao.getIdArtesao());
    }

    /**
     * Atualiza os dados de um trabalho já existente no portfólio (versão com link de imagem).
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
        Trabalho trabalho = buscarTrabalho(idTrabalho);

        validarDono(trabalho, idUsuario);

        trabalho.setImagemUrl(dto.getImagemUrl());
        trabalho.setLegenda(dto.getLegenda());
        trabalho.setDescricao(dto.getDescricao());
        trabalho.setCategoria(dto.getCategoria());
        trabalho.alterarPreco(dto.getPreco());

        return trabalhoRepository.save(trabalho);
    }

    /**
     * Atualiza um trabalho com a possibilidade de trocar a foto (arquivo).
     * Se nenhuma imagem for enviada, a foto atual é mantida. A foto antiga só é
     * apagada do Cloudinary depois que o banco já guardou a nova.
     *
     * @throws TrabalhoNaoEncontradoException se não existir trabalho com esse ID
     * @throws AccessDeniedException          se o trabalho não pertencer ao artesão autenticado
     */
    public Trabalho atualizarTrabalhoComImagem(Long idTrabalho, TrabalhoUploadDTO dto, Long idUsuario) {
        Trabalho trabalho = buscarTrabalho(idTrabalho);

        validarDono(trabalho, idUsuario);

        String publicIdAntigo = trabalho.getImagemPublicId();
        ImagemStorageService.ResultadoUpload novaImagem = null;

        if (dto.getImagem() != null && !dto.getImagem().isEmpty()) {
            novaImagem = imagemStorageService.uploadTrabalho(dto.getImagem());
        }

        try {
            if (novaImagem != null) {
                trabalho.setImagemUrl(novaImagem.url());
                trabalho.setImagemPublicId(novaImagem.publicId());
            }
            trabalho.setLegenda(dto.getLegenda());
            trabalho.setDescricao(dto.getDescricao());
            trabalho.setCategoria(dto.getCategoria());
            trabalho.alterarPreco(dto.getPreco());

            Trabalho salvo = trabalhoRepository.save(trabalho);

            if (novaImagem != null) {
                excluirImagemComSeguranca(publicIdAntigo);
            }
            return salvo;
        } catch (RuntimeException e) {
            if (novaImagem != null) {
                excluirImagemComSeguranca(novaImagem.publicId());
            }
            throw e;
        }
    }

    /**
     * Atualiza somente o preço de um trabalho e registra a mudança no histórico.
     *
     * @param preco novo preço em reais (nulo = sob consulta)
     * @throws TrabalhoNaoEncontradoException se não existir trabalho com esse ID
     * @throws AccessDeniedException          se o trabalho não pertencer ao artesão autenticado
     */
    public Trabalho atualizarPreco(Long idTrabalho, BigDecimal preco, Long idUsuario) {
        Trabalho trabalho = buscarTrabalho(idTrabalho);

        validarDono(trabalho, idUsuario);

        trabalho.alterarPreco(preco);

        return trabalhoRepository.save(trabalho);
    }

    /**
     * Lista as mudanças de preço de um trabalho, da mais recente para a mais antiga.
     *
     * @throws TrabalhoNaoEncontradoException se não existir trabalho com esse ID
     * @throws AccessDeniedException          se o trabalho não pertencer ao artesão autenticado
     */
    public List<HistoricoPreco> listarHistoricoPreco(Long idTrabalho, Long idUsuario) {
        Trabalho trabalho = buscarTrabalho(idTrabalho);

        validarDono(trabalho, idUsuario);

        return historicoPrecoRepository.findByTrabalho_IdTrabalhoOrderByDataAlteracaoDesc(idTrabalho);
    }

    /**
     * Exclui um trabalho do portfólio do artesão autenticado
     * (e a foto correspondente no Cloudinary, se houver).
     *
     * @param idTrabalho ID do trabalho a ser excluído (vem da URL)
     * @param idUsuario  ID do usuário autenticado (vem do token JWT)
     * @throws TrabalhoNaoEncontradoException se não existir trabalho com esse ID
     * @throws AccessDeniedException          se o trabalho não pertencer ao artesão autenticado (→ HTTP 403)
     */
    public void excluirTrabalho(Long idTrabalho, Long idUsuario) {
        Trabalho trabalho = buscarTrabalho(idTrabalho);

        validarDono(trabalho, idUsuario);

        String publicId = trabalho.getImagemPublicId();
        trabalhoRepository.delete(trabalho);
        excluirImagemComSeguranca(publicId);
    }

    /**
     * Exclusão feita pelo ADMINISTRADOR (moderação): não exige ser o dono do trabalho.
     *
     * @throws TrabalhoNaoEncontradoException se não existir trabalho com esse ID
     */
    public void excluirTrabalhoComoAdministrador(Long idTrabalho) {
        Trabalho trabalho = buscarTrabalho(idTrabalho);

        String publicId = trabalho.getImagemPublicId();
        trabalhoRepository.delete(trabalho);
        excluirImagemComSeguranca(publicId);
    }

    private Artesao buscarArtesao(Long idUsuario) {
        return artesaoRepository
                .findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(ArtesaoNaoEncontradoException::new);
    }

    private Trabalho buscarTrabalho(Long idTrabalho) {
        return trabalhoRepository.findById(idTrabalho)
                .orElseThrow(TrabalhoNaoEncontradoException::new);
    }

    /** Impede que o artesão publique mais trabalhos do que o limite permitido. */
    private void validarLimite(Artesao artesao) {
        long total = trabalhoRepository.countByArtesao_IdArtesao(artesao.getIdArtesao());

        if (total >= limitePorArtesao) {
            throw new LimiteTrabalhosException(limitePorArtesao);
        }
    }

    /**
     * Garante que o trabalho pertence ao artesão vinculado ao usuário autenticado.
     * Sem essa checagem, qualquer artesão logado poderia editar/excluir trabalhos
     * de outro artesão apenas trocando o ID na URL (falha de autorização — IDOR).
     */
    private void validarDono(Trabalho trabalho, Long idUsuario) {
        Artesao artesao = buscarArtesao(idUsuario);

        if (!trabalho.getArtesao().getIdArtesao().equals(artesao.getIdArtesao())) {
            throw new AccessDeniedException("Você não tem permissão para alterar este trabalho.");
        }
    }

    /** Apaga a foto no Cloudinary sem derrubar a operação principal se o serviço falhar. */
    private void excluirImagemComSeguranca(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            return;
        }

        try {
            imagemStorageService.excluir(publicId);
        } catch (RuntimeException e) {
            log.warn("Não foi possível excluir a imagem {} do Cloudinary.", publicId, e);
        }
    }
}
