insert into emprestimos (id, usuario_id, data_emprestimo, data_esperada_devolucao, data_devolucao, multa, exemplar_id) values
    (1, 1, date '2025-06-01', date '2025-06-15', date '2025-06-15', 0.00, 1),
    (2, 2, date '2025-06-05', date '2025-06-20', null, 5.50, 3),
    (3, 3, date '2025-06-10', date '2025-06-25', date '2025-06-20', 0.00, 4),
    (4, 4, date '2025-06-12', date '2025-06-27', null, 12.75, 5),
    (5, 5, date '2025-06-02', date '2025-06-18', date '2025-06-18', 0.00, 6);

alter table emprestimos alter column id restart with 6;
