CREATE TABLE pedido(
    id UUID PRIMARY KEY NOT NULL,
    situacao VARCHAR(255),
    data_emissao TIMESTAMP NOT NULL,
    data_fechamento TIMESTAMP,
    valor_bruto NUMERIC(10,2) ,
    valor_desconto NUMERIC(10,2),
    valor_liquido NUMERIC(10,2),
    ativo BOOLEAN DEFAULT TRUE
);
