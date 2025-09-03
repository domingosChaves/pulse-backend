-- Exemplo de consultas para validar dados no H2 Console
-- Listar todos os fabricantes
SELECT * FROM fabricantes;

-- Listar todos os produtos
SELECT * FROM produtos;

-- Produtos do fabricante com id = 1
SELECT * FROM produtos WHERE fabricante_id = 1;

-- Contagem de produtos por fabricante
SELECT fabricante_id, COUNT(*) AS total_produtos FROM produtos GROUP BY fabricante_id;

-- Join para ver nome do fabricante com seus produtos
SELECT p.id AS produto_id, p.nome AS produto, p.codigo_barras, p.preco, f.id AS fabricante_id, f.nome AS fabricante
FROM produtos p
JOIN fabricantes f ON p.fabricante_id = f.id
ORDER BY f.nome, p.nome;

