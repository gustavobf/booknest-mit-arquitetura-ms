insert into autores (id, nome, nacionalidade, ano_nascimento) values
    (1, 'Machado de Assis', 'Brasileiro', 1839),
    (2, 'Clarice Lispector', 'Brasileira', 1920),
    (3, 'Paulo Coelho', 'Brasileiro', 1947),
    (4, 'Jorge Amado', 'Brasileiro', 1912);

insert into categorias (id, nome, descricao) values
    (1, 'Ficção', 'Narrativas de ficção literária'),
    (2, 'Romance', 'Romances clássicos e contemporâneos'),
    (3, 'Aventura', 'Histórias de aventura e ação'),
    (4, 'Poesia', 'Obras poéticas em geral');

insert into editoras (id, nome, cidade, email_contato, ativa) values
    (1, 'Companhia das Letras', 'São Paulo', 'contato@companhiadasletras.com.br', true),
    (2, 'Record', 'Rio de Janeiro', 'vendas@record.com.br', true),
    (3, 'Rocco', 'Rio de Janeiro', 'info@rocco.com.br', true),
    (5, 'Aleph', 'São Paulo', 'contato@aleph.com.br', false);

insert into usuarios (id, nome, email, matricula, ativo) values
    (1, 'João Silva', 'joao.silva@biblioteca.com', 'MAT2025001', true),
    (2, 'Maria Santos', 'maria.santos@biblioteca.com', 'MAT2025002', true),
    (3, 'Pedro Oliveira', 'pedro.oliveira@biblioteca.com', 'MAT2025003', true),
    (4, 'Ana Costa', 'ana.costa@biblioteca.com', 'MAT2025004', true),
    (5, 'Carlos Lima', 'carlos.lima@biblioteca.com', 'MAT2025005', false);

insert into livros (id, titulo, isbn, autor_id, categoria_id, editora_id) values
    (1, 'Dom Casmurro', '978-8535905571', 1, 2, 1),
    (2, 'A Hora da Estrela', '978-8526018395', 2, 1, 1),
    (3, 'O Alquimista', '978-8504006032', 3, 3, 2),
    (4, 'Capitães da Areia', '978-8535914924', 4, 1, 1),
    (5, 'Memórias Póstumas de Brás Cubas', '978-8526004565', 1, 2, 3);

insert into exemplares (id, codigo, estado_conservacao, disponivel, livro_id) values
    (1, 'DOM-001', 'BOM', true, 1),
    (2, 'DOM-002', 'REGULAR', true, 1),
    (3, 'EST-001', 'BOM', false, 2),
    (4, 'ALQ-001', 'EXCELENTE', true, 3),
    (5, 'CAP-001', 'REGULAR', false, 4),
    (6, 'MEM-001', 'BOM', true, 5),
    (7, 'EST-002', 'EXCELENTE', true, 2),
    (8, 'ALQ-002', 'BOM', true, 3);

alter table autores alter column id restart with 5;
alter table categorias alter column id restart with 5;
alter table editoras alter column id restart with 6;
alter table usuarios alter column id restart with 6;
alter table livros alter column id restart with 6;
alter table exemplares alter column id restart with 9;
