USE associacao_gaia;

INSERT INTO `evento`
    (`imagem_url`, `titulo`, `descricao`, `local`, `data`, `hora`)
VALUES
    (
        '../assets/fashion-day.jpg',
        'Artesanal Fashion Day',
        'Moda, arte e inovação unidas para celebrar o talento manual. Conheça o evento que marcou o início de uma nova era para a moda artesanal e sustentável.',
        'Praça Central',
        '2026-05-10',
        '00:00:00'
    ),
    (
        '../assets/maos-e-mentes.jpg',
        'Mega Artesanato',
        'Uma celebração da criatividade e do talento manual. Vivenciamos momentos de troca e conexão que provam que o artesanato é pura memória, cultura e emoção.',
        'Museu Municipal',
        '2026-05-15',
        '00:00:00'
    ),
    (
        '../assets/maos-criativas.jpg',
        'Mãos Criativas',
        'Uma parceria entre Associação Gaia e Melissa Barth para valorizar o bordado de Itapetininga.',
        'Praça Peixoto Gomide',
        '2026-05-21',
        '00:00:00'
    ),
    (
        '../assets/Natal-maos-criativas.jpg',
        'Edição Natal - 2ª Vitrine Mãos Criativas',
        'A temporada natalina chegou com o lançamento de uma vitrine inédita de bordado livre. Valorize o artesanato de Itapetininga e garanta peças exclusivas feitas por nossas artesãs locais.',
        'Praça Mal.Deodoro da Fonseca',
        '2026-08-08',
        '00:00:00'
    );

SELECT 'eventos' AS tabela, COUNT(*) AS total FROM evento;