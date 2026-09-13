USE associacao_gaia;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE trabalho;
TRUNCATE TABLE artesao;
TRUNCATE TABLE usuario;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO usuario (nome, email, senha, telefone, tipo_usuario, data_cadastro) VALUES
  ('Ana Souza',      'ana.souza@ateliergaia.com',      'senha_hash_ana',    '(11) 99876-5432', 'ARTESAO',       NOW()),
  ('Helena Martins', 'helena.martins@ateliergaia.com', 'senha_hash_helena', '(15) 99111-2233', 'ARTESAO',       NOW()),
  ('Lucas Eduardo',  'lucas.eduardo@ateliergaia.com',  'senha_hash_lucas',  '(15) 98800-4411', 'ARTESAO',       NOW()),
  ('Jonas Pereira',  'jonas.pereira@ateliergaia.com',  'senha_hash_jonas',  '(14) 99700-3322', 'ARTESAO',       NOW()),
  ('Lucia Santos',   'lucia.santos@ateliergaia.com',   'senha_hash_lucia',  '(15) 99600-5544', 'ARTESAO',       NOW()),
  ('Admin Gaia',     'diretoriagaia1@gmail.com',        'senha_hash_admin',  '(11) 9999-9999',  'ADMINISTRADOR', NOW());

INSERT INTO artesao (nome_exibicao, titulo_curto, biografia, `lead`, cidade, instagram, facebook_url, whatsapp_url, avatar_url, locais_atendimento, membro_desde, id_usuario) VALUES
('Ana Souza', 'Ceramista Autoral',
 'Trabalho com argila de alta temperatura, formas utilitarias e pecas decorativas. Meu processo valoriza textura, acabamento limpo e a historia de cada colecao.',
 'Ceramica autoral com argila, queima e esmalte.',
 'Campinas, SP', '@ana.souza.ceramica', 'https://www.facebook.com/', 'https://wa.me/5511998765432',
 'https://images.pexels.com/photos/28867382/pexels-photo-28867382.jpeg',
 'Oficinas abertas, Encomendas autorais, Feiras e mostras', 'Marco 2023',
 (SELECT id_usuario FROM usuario WHERE email = 'ana.souza@ateliergaia.com')),
('Helena Martins', 'Ceramista - Ceramica e Queima',
 'Apaixonada pela ceramica artesanal, Helena trabalha com tecnicas tradicionais de queima adaptadas ao cotidiano contemporaneo.',
 'Ceramica artesanal com alma: queima tradicional em Itu, SP.',
 'Itu, SP', '@helena.martins.ceramica', 'https://www.facebook.com/', 'https://wa.me/5515991112233',
 'https://images.pexels.com/photos/10157816/pexels-photo-10157816.jpeg',
 'Atelie em Itu, Feiras regionais', 'Junho 2023',
 (SELECT id_usuario FROM usuario WHERE email = 'helena.martins@ateliergaia.com')),
('Lucas Eduardo', 'Artesao - Porcelana e Pintura',
 'Lucas combina a delicadeza da porcelana com tecnicas de pintura a mao livre, criando pecas unicas para decoracao.',
 'Porcelana pintada a mao com olhar de designer.',
 'Itapetininga, SP', '@lucas.eduardo.porcelana', 'https://www.facebook.com/', 'https://wa.me/5515988004411',
 'https://images.pexels.com/photos/30124371/pexels-photo-30124371.jpeg',
 'Atelie em Itapetininga, Encomendas online', 'Agosto 2023',
 (SELECT id_usuario FROM usuario WHERE email = 'lucas.eduardo@ateliergaia.com')),
('Jonas Pereira', 'Marceneiro Artesanal',
 'Com mais de 15 anos de experiencia, Jonas transforma madeira em pecas funcionais e esculturicas.',
 'Marcenaria artesanal com alma.',
 'Botucatu, SP', '@jonas.pereira.madeira', 'https://www.facebook.com/', 'https://wa.me/5514997003322',
 'https://images.pexels.com/photos/32508763/pexels-photo-32508763.jpeg',
 'Atelie em Botucatu, Feiras do interior SP', 'Janeiro 2023',
 (SELECT id_usuario FROM usuario WHERE email = 'jonas.pereira@ateliergaia.com')),
('Lucia Santos', 'Artesa - Porcelana e Decoracao',
 'Lucia cria pecas de porcelana voltadas a decoracao de ambientes, explorando formas minimalistas e paletas neutras.',
 'Porcelana decorativa minimalista.',
 'Itapetininga, SP', '@lucia.santos.porcelana', 'https://www.facebook.com/', 'https://wa.me/5515996005544',
 'https://images.pexels.com/photos/33220961/pexels-photo-33220961.jpeg',
 'Atelie em Itapetininga, Lojas parceiras', 'Outubro 2022',
 (SELECT id_usuario FROM usuario WHERE email = 'lucia.santos@ateliergaia.com'));

INSERT INTO trabalho (legenda, descricao, imagem_url, categoria, data_publicacao, id_artesao) VALUES
('Vaso utilitario',    'Vaso em argila de alta temperatura, acabamento esmaltado em tons terrosos.',
 'https://images.pexels.com/photos/28867382/pexels-photo-28867382.jpeg', 'Ceramica', NOW(),
 (SELECT id_artesao FROM artesao WHERE nome_exibicao = 'Ana Souza')),
('Conjunto de cha',    'Conjunto de xicaras e bule em ceramica com esmalte azul artesanal.',
 'https://images.pexels.com/photos/33393570/pexels-photo-33393570.jpeg', 'Ceramica', NOW(),
 (SELECT id_artesao FROM artesao WHERE nome_exibicao = 'Ana Souza')),
('Tigela esmaltada',   'Tigela em argila com esmalte artesanal de queima unica.',
 'https://images.pexels.com/photos/27674625/pexels-photo-27674625.jpeg', 'Ceramica', NOW(),
 (SELECT id_artesao FROM artesao WHERE nome_exibicao = 'Ana Souza')),
('Colecao decorativa', 'Serie de pecas decorativas inspiradas em formas da natureza.',
 'https://images.pexels.com/photos/15250508/pexels-photo-15250508.jpeg', 'Ceramica', NOW(),
 (SELECT id_artesao FROM artesao WHERE nome_exibicao = 'Ana Souza')),
('Pote de queima raku','Peca em tecnica Raku com craquele natural e acabamento fumado.',
 'https://images.pexels.com/photos/10157816/pexels-photo-10157816.jpeg', 'Ceramica', NOW(),
 (SELECT id_artesao FROM artesao WHERE nome_exibicao = 'Helena Martins')),
('Prato pintado a mao','Prato de porcelana com ilustracao floral pintada a mao livre.',
 'https://images.pexels.com/photos/30124371/pexels-photo-30124371.jpeg', 'Porcelana', NOW(),
 (SELECT id_artesao FROM artesao WHERE nome_exibicao = 'Lucas Eduardo')),
('Banco escultural',   'Banco em madeira macica com formas organicas esculpidas manualmente.',
 'https://images.pexels.com/photos/32508763/pexels-photo-32508763.jpeg', 'Madeira', NOW(),
 (SELECT id_artesao FROM artesao WHERE nome_exibicao = 'Jonas Pereira')),
('Vaso minimalista',   'Vaso de porcelana branca com linhas retas e inspiracao escandinava.',
 'https://images.pexels.com/photos/33220961/pexels-photo-33220961.jpeg', 'Porcelana', NOW(),
 (SELECT id_artesao FROM artesao WHERE nome_exibicao = 'Lucia Santos'));

SELECT 'usuarios' AS tabela, COUNT(*) AS total FROM usuario
UNION ALL SELECT 'artesaos', COUNT(*) FROM artesao
UNION ALL SELECT 'trabalhos', COUNT(*) FROM trabalho;
