# Documentação de Funcionalidades — Associação Gaia

**Versão:** 1.1  
**Marco de referência:** 30/09/2026  
**Arquivo principal:** `index.html`

## Escopo

Esta documentação registra o estado funcional dos repositórios ao final de setembro de 2026. Foram usados snapshots dos branches principais:

- Backend: `520be4c498a760e2c0ab7fef011a16a3699d94b5`
- Frontend: `2cbc762a911a0343755cc84a473f06969b67f59b`

O conteúdo diferencia:
1. funcionalidades disponíveis no website;
2. recursos existentes somente no backend;
3. recursos existentes na API, mas sem integração comprovada no frontend;
4. funcionalidades pendentes ou não disponíveis em 30/09/2026;
5. funcionalidades que só foram integradas depois da data de corte.

## Conteúdo

O `index.html` reúne a documentação funcional em uma página navegável:
- site institucional e navegação;
- cadastro, login e sessão JWT;
- consulta e atualização de usuário;
- painel administrativo de usuários;
- perfil de artesão e trabalhos disponíveis na API;
- regras de segurança e referência de endpoints;
- quadro de pendências com marcadores **EM BREVE**, **NÃO DISPONÍVEL** e **NÃO INCLUÍDA**.

## Como visualizar

Abra `index.html` em um navegador. O arquivo é autocontido e não depende de bibliotecas externas para navegação e estilo.

## Observação sobre os status

“Em breve” identifica uma funcionalidade prevista/pendente no trabalho da equipe, mas ainda não concluída no marco. “Não disponível” ou “não incluída” é usado quando o snapshot não comprova sua implementação; esses rótulos não prometem entrega futura.

A documentação é funcional. A documentação de classes Java permanece separada em `docs/javadoc`.
