-- Seed idempotente para H2 usando MERGE (upsert)
MERGE INTO fabricantes (nome, cnpj, endereco, telefone, contato) KEY (cnpj)
VALUES ('ACME Indústria', '12345678000199', 'Rua A, 100', '(11)9999-0001', 'João');
MERGE INTO fabricantes (nome, cnpj, endereco, telefone, contato) KEY (cnpj)
VALUES ('Beta Ltda', '98765432000188', 'Avenida B, 200', '(11)9888-0002', 'Maria');

-- Produtos: usar KEY (codigo_barras) e resolver fabricante por CNPJ
MERGE INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id) KEY (codigo_barras)
VALUES ('Produto Alpha', '7891234567895', 'Produto alpha descricao', 19.90, 50,
        (SELECT id FROM fabricantes WHERE cnpj = '12345678000199'));
MERGE INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id) KEY (codigo_barras)
VALUES ('Produto Beta', '7891234567896', 'Produto beta descricao', 29.90, 30,
        (SELECT id FROM fabricantes WHERE cnpj = '12345678000199'));
MERGE INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id) KEY (codigo_barras)
VALUES ('Produto Gama', '7891234567897', 'Produto gama descricao', 9.90, 100,
        (SELECT id FROM fabricantes WHERE cnpj = '98765432000188'));
