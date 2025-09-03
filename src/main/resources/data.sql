-- Popula fabricantes
INSERT INTO fabricantes (id, nome, cnpj, endereco, telefone, contato) VALUES (1, 'ACME Indústria', '12345678000199', 'Rua A, 100', '(11)9999-0001', 'João');
INSERT INTO fabricantes (id, nome, cnpj, endereco, telefone, contato) VALUES (2, 'Beta Ltda', '98765432000188', 'Avenida B, 200', '(11)9888-0002', 'Maria');

-- Popula produtos
INSERT INTO produtos (id, nome, codigo_barras, descricao, preco, estoque, fabricante_id) VALUES (1, 'Produto Alpha', '7891234567895', 'Produto alpha descricao', 19.90, 50, 1);
INSERT INTO produtos (id, nome, codigo_barras, descricao, preco, estoque, fabricante_id) VALUES (2, 'Produto Beta', '7891234567896', 'Produto beta descricao', 29.90, 30, 1);
INSERT INTO produtos (id, nome, codigo_barras, descricao, preco, estoque, fabricante_id) VALUES (3, 'Produto Gama', '7891234567897', 'Produto gama descricao', 9.90, 100, 2);

