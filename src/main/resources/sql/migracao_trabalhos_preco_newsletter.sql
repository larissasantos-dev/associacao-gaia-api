-- ============================================================================
-- Associação Gaia — migração: foto/preço dos trabalhos + newsletter
-- Autora: Lana
--
-- ATENÇÃO: com spring.jpa.hibernate.ddl-auto=update o Spring cria/ajusta isto
-- sozinho ao subir a API. Este arquivo serve para quem prefere aplicar à mão
-- (ex.: banco de produção) e como documentação do modelo de dados.
-- ============================================================================
USE associacao_gaia;

-- 1) Novos campos na tabela de trabalhos (cards dos artesãos)
ALTER TABLE trabalho
    ADD COLUMN imagem_public_id     VARCHAR(255)  NULL COMMENT 'id da foto no Cloudinary (para apagar ao excluir/trocar)',
    ADD COLUMN preco                DECIMAL(10,2) NULL COMMENT 'NULL = sob consulta',
    ADD COLUMN preco_atualizado_em  DATETIME(6)   NULL;

-- 2) Histórico de preços: cada mudança de preço vira uma linha
CREATE TABLE IF NOT EXISTS historico_preco (
    id_historico    BIGINT        NOT NULL AUTO_INCREMENT,
    id_trabalho     BIGINT        NOT NULL,
    preco_anterior  DECIMAL(10,2) NULL,
    preco_novo      DECIMAL(10,2) NULL,
    data_alteracao  DATETIME(6)   NOT NULL,
    PRIMARY KEY (id_historico),
    KEY idx_historico_trabalho (id_trabalho),
    CONSTRAINT fk_historico_trabalho FOREIGN KEY (id_trabalho)
        REFERENCES trabalho (id_trabalho) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3) Inscrições da newsletter
CREATE TABLE IF NOT EXISTS newsletter_inscricao (
    id_inscricao       BIGINT       NOT NULL AUTO_INCREMENT,
    email              VARCHAR(150) NOT NULL,
    ativo              BIT(1)       NOT NULL DEFAULT b'1',
    data_inscricao     DATETIME(6)  NOT NULL,
    data_cancelamento  DATETIME(6)  NULL,
    PRIMARY KEY (id_inscricao),
    UNIQUE KEY uk_newsletter_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
