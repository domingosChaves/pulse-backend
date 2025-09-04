-- Fabricantes base (presentes no data.sql do H2) também para Docker
INSERT INTO fabricantes (nome, cnpj, endereco, telefone, contato)
VALUES ('ACME Indústria', '12345678000199', 'Rua A, 100', '(11)9999-0001', 'João')
ON CONFLICT (cnpj) DO NOTHING;
INSERT INTO fabricantes (nome, cnpj, endereco, telefone, contato)
VALUES ('Beta Ltda', '98765432000188', 'Avenida B, 200', '(11)9888-0002', 'Maria')
ON CONFLICT (cnpj) DO NOTHING;

-- Dados adicionais para ambiente Docker (PostgreSQL)
-- Observação: usa ON CONFLICT para evitar erro em reinicializações.

-- Fabricantes extras
INSERT INTO fabricantes (nome, cnpj, endereco, telefone, contato)
VALUES ('Gama SA', '11111111000111', 'Rua C, 300', '(11)9000-0003', 'Carlos')
ON CONFLICT (cnpj) DO NOTHING;
INSERT INTO fabricantes (nome, cnpj, endereco, telefone, contato)
VALUES ('Delta SA', '22222222000122', 'Rua D, 400', '(11)9000-0004', 'Paula')
ON CONFLICT (cnpj) DO NOTHING;
INSERT INTO fabricantes (nome, cnpj, endereco, telefone, contato)
VALUES ('Omega Ltda', '33333333000133', 'Rua E, 500', '(11)9000-0005', 'Ricardo')
ON CONFLICT (cnpj) DO NOTHING;
INSERT INTO fabricantes (nome, cnpj, endereco, telefone, contato)
VALUES ('Sigma Indústria', '44444444000144', 'Av. F, 600', '(11)9000-0006', 'Silvia')
ON CONFLICT (cnpj) DO NOTHING;
INSERT INTO fabricantes (nome, cnpj, endereco, telefone, contato)
VALUES ('Epsilon Corp', '55555555000155', 'Av. G, 700', '(11)9000-0007', 'Marcos')
ON CONFLICT (cnpj) DO NOTHING;
INSERT INTO fabricantes (nome, cnpj, endereco, telefone, contato)
VALUES ('Theta Brasil', '66666666000166', 'Rua H, 800', '(11)9000-0008', 'Fernanda')
ON CONFLICT (cnpj) DO NOTHING;
INSERT INTO fabricantes (nome, cnpj, endereco, telefone, contato)
VALUES ('Lambda Tech', '77777777000177', 'Rua I, 900', '(11)9000-0009', 'Helena')
ON CONFLICT (cnpj) DO NOTHING;
INSERT INTO fabricantes (nome, cnpj, endereco, telefone, contato)
VALUES ('Zeta Com', '88888888000188', 'Av. J, 1000', '(11)9000-0010', 'Rafael')
ON CONFLICT (cnpj) DO NOTHING;

-- Produtos em massa (distribuídos entre fabricantes existentes por CNPJ)
-- Referência ACME Indústria (data.sql) cnpj 12345678000199
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('ACME Caneta Azul', '1000000000001', 'Caneta esferográfica azul', 2.50, 500,
        (SELECT id FROM fabricantes WHERE cnpj='12345678000199'))
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('ACME Caneta Preta', '1000000000002', 'Caneta esferográfica preta', 2.50, 450,
        (SELECT id FROM fabricantes WHERE cnpj='12345678000199'))
ON CONFLICT (codigo_barras) DO NOTHING;

-- Referência Beta Ltda (data.sql) cnpj 98765432000188
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('Beta Caderno 100F', '1000000001001', 'Caderno 100 folhas', 12.90, 300,
        (SELECT id FROM fabricantes WHERE cnpj='98765432000188'))
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('Beta Caderno 200F', '1000000001002', 'Caderno 200 folhas', 19.90, 200,
        (SELECT id FROM fabricantes WHERE cnpj='98765432000188'))
ON CONFLICT (codigo_barras) DO NOTHING;

-- Referência Gama SA
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('Gama Lápis HB', '1000000002001', 'Lápis grafite HB', 1.20, 1000,
        (SELECT id FROM fabricantes WHERE cnpj='11111111000111'))
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('Gama Borracha', '1000000002002', 'Borracha branca', 0.90, 800,
        (SELECT id FROM fabricantes WHERE cnpj='11111111000111'))
ON CONFLICT (codigo_barras) DO NOTHING;

-- Referência Delta SA
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('Delta Apontador', '1000000003001', 'Apontador plástico', 1.50, 600,
        (SELECT id FROM fabricantes WHERE cnpj='22222222000122'))
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('Delta Régua 30cm', '1000000003002', 'Régua acrílica 30cm', 3.50, 400,
        (SELECT id FROM fabricantes WHERE cnpj='22222222000122'))
ON CONFLICT (codigo_barras) DO NOTHING;

-- Referência Omega Ltda
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('Omega Marca-texto', '1000000004001', 'Marca-texto amarelo', 4.90, 350,
        (SELECT id FROM fabricantes WHERE cnpj='33333333000133'))
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('Omega Clips', '1000000004002', 'Clips metálico 100 un', 5.50, 250,
        (SELECT id FROM fabricantes WHERE cnpj='33333333000133'))
ON CONFLICT (codigo_barras) DO NOTHING;

-- Referência Sigma Indústria
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('Sigma Post-it', '1000000005001', 'Bloco adesivo 76x76', 6.90, 220,
        (SELECT id FROM fabricantes WHERE cnpj='44444444000144'))
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('Sigma Pasta A4', '1000000005002', 'Pasta catálogo A4', 7.90, 180,
        (SELECT id FROM fabricantes WHERE cnpj='44444444000144'))
ON CONFLICT (codigo_barras) DO NOTHING;

-- Referência Epsilon Corp
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('Epsilon Envelope', '1000000006001', 'Envelope pardo A4', 1.10, 900,
        (SELECT id FROM fabricantes WHERE cnpj='55555555000155'))
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('Epsilon Etiquetas', '1000000006002', 'Etiquetas auto-adesivas', 8.90, 160,
        (SELECT id FROM fabricantes WHERE cnpj='55555555000155'))
ON CONFLICT (codigo_barras) DO NOTHING;

-- Referência Theta Brasil
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('Theta Planner', '1000000007001', 'Planner semanal', 24.90, 140,
        (SELECT id FROM fabricantes WHERE cnpj='66666666000166'))
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('Theta Agenda', '1000000007002', 'Agenda diária', 29.90, 120,
        (SELECT id FROM fabricantes WHERE cnpj='66666666000166'))
ON CONFLICT (codigo_barras) DO NOTHING;

-- Referência Lambda Tech
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('Lambda Mousepad', '1000000008001', 'Mousepad tecido', 15.90, 260,
        (SELECT id FROM fabricantes WHERE cnpj='77777777000177'))
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('Lambda Suporte Notebook', '1000000008002', 'Suporte ajustável', 59.90, 80,
        (SELECT id FROM fabricantes WHERE cnpj='77777777000177'))
ON CONFLICT (codigo_barras) DO NOTHING;

-- Referência Zeta Com
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('Zeta Teclado', '1000000009001', 'Teclado USB', 79.90, 70,
        (SELECT id FROM fabricantes WHERE cnpj='88888888000188'))
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
VALUES ('Zeta Mouse', '1000000009002', 'Mouse USB', 49.90, 90,
        (SELECT id FROM fabricantes WHERE cnpj='88888888000188'))
ON CONFLICT (codigo_barras) DO NOTHING;

-- Mais variações para encher base
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
SELECT 'ACME Caderno Capa Dura', '1000000010001', 'Caderno capa dura', 21.90, 210, id FROM fabricantes WHERE cnpj='12345678000199'
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
SELECT 'Beta Lápis 2B', '1000000010002', 'Lápis 2B', 1.40, 950, id FROM fabricantes WHERE cnpj='98765432000188'
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
SELECT 'Gama Marca-Texto Verde', '1000000010003', 'Marca-texto verde', 4.90, 330, id FROM fabricantes WHERE cnpj='11111111000111'
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
SELECT 'Delta Classificador', '1000000010004', 'Classificador 50 plásticos', 18.90, 150, id FROM fabricantes WHERE cnpj='22222222000122'
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
SELECT 'Omega Pasta Suspensa', '1000000010005', 'Pasta suspensa kraft', 2.90, 600, id FROM fabricantes WHERE cnpj='33333333000133'
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
SELECT 'Sigma Caderno Espiral', '1000000010006', 'Caderno espiral 96F', 16.90, 230, id FROM fabricantes WHERE cnpj='44444444000144'
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
SELECT 'Epsilon Fichário', '1000000010007', 'Fichário universitário', 39.90, 110, id FROM fabricantes WHERE cnpj='55555555000155'
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
SELECT 'Theta Planner Mensal', '1000000010008', 'Planner mensal', 27.90, 130, id FROM fabricantes WHERE cnpj='66666666000166'
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
SELECT 'Lambda Hub USB', '1000000010009', 'Hub USB 4 portas', 39.90, 85, id FROM fabricantes WHERE cnpj='77777777000177'
ON CONFLICT (codigo_barras) DO NOTHING;
INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
SELECT 'Zeta Headset', '1000000010010', 'Headset estéreo', 119.90, 60, id FROM fabricantes WHERE cnpj='88888888000188'
ON CONFLICT (codigo_barras) DO NOTHING;

-- Lote adicional para testar paginação (20 itens rápidos)
DO $$
BEGIN
  FOR i IN 1..20 LOOP
    INSERT INTO produtos (nome, codigo_barras, descricao, preco, estoque, fabricante_id)
    SELECT 'Item Paginado ' || i, '20000000' || lpad(i::text, 4, '0'), 'Gerado para testes', 9.90 + i, 10 + i,
           (SELECT id FROM fabricantes WHERE cnpj='12345678000199')
    ON CONFLICT (codigo_barras) DO NOTHING;
  END LOOP;
END$$;
