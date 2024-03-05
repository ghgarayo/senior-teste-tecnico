CREATE TABLE item(
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    tipo_item VARCHAR(255) NOT NULL,
    preco_unitario NUMERIC(10,2) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE
   );