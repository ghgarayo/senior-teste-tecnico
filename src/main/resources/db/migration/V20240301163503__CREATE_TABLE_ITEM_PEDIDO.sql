CREATE TABLE item_pedido(
    id UUID PRIMARY KEY NOT NULL,
    pedido_id uuid,
    item_id uuid,
    tipo_item VARCHAR(255) NOT NULL,
    quantidade INTEGER NOT NULL,
    preco_unitario NUMERIC(10,2) NOT NULL,
    valor_bruto NUMERIC(10,2) NOT NULL,
    valor_desconto NUMERIC(10,2),
    valor_liquido NUMERIC(10,2) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,

    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    FOREIGN KEY (item_id) REFERENCES item(id),

    CONSTRAINT fk_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    CONSTRAINT fk_produto FOREIGN KEY (item_id) REFERENCES item(id)
);