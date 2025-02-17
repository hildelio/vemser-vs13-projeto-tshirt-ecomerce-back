
-- DROP TABLE 
DROP TABLE ITEM_PEDIDO CASCADE CONSTRAINTS;
DROP TABLE VARIACAO CASCADE CONSTRAINTS;
DROP TABLE PRODUTO CASCADE CONSTRAINTS;
DROP TABLE FOTO CASCADE CONSTRAINTS;
DROP TABLE PEDIDO CASCADE CONSTRAINTS;
DROP TABLE CUPOM_DESCONTO CASCADE CONSTRAINTS;
DROP TABLE CARGO_USUARIO CASCADE CONSTRAINTS;
DROP TABLE CARGO CASCADE CONSTRAINTS;
DROP TABLE USUARIO CASCADE CONSTRAINTS;
DROP TABLE PESSOA CASCADE CONSTRAINTS;
DROP TABLE ENDERECO CASCADE CONSTRAINTS;

-- DROP SEQUENCE 
DROP SEQUENCE SEQ_ITEM_PEDIDO;
DROP SEQUENCE SEQ_VARIACAO;
DROP SEQUENCE SEQ_PRODUTO;
DROP SEQUENCE SEQ_FOTO;
DROP SEQUENCE SEQ_PEDIDO;
DROP SEQUENCE SEQ_CUPOM;
DROP SEQUENCE SEQ_CARGO;
DROP SEQUENCE SEQ_USUARIO;
DROP SEQUENCE SEQ_PESSOA;
DROP SEQUENCE SEQ_ENDERECO;

-- CREATE TABLE 
CREATE TABLE ENDERECO (
                          ID_ENDERECO NUMBER NOT NULL PRIMARY KEY,
                          ID_PESSOA NUMBER NOT NULL,
                          LOGRADOURO VARCHAR2(255) NOT NULL,
                          NUMERO NUMBER(38) NOT NULL,
                          COMPLEMENTO VARCHAR2(255) NOT NULL,
                          REFERENCIA VARCHAR2(255) NOT NULL,
                          BAIRRO VARCHAR2(255) NOT NULL,
                          CIDADE VARCHAR2(255) NOT NULL,
                          CEP VARCHAR2(8) NOT NULL,
                          ESTADO VARCHAR2(255) NOT NULL,
                          PAIS VARCHAR2(255) NOT NULL,
                          CRIADO DATE,
                          EDITADO DATE
);

CREATE TABLE PESSOA (
                        ID_PESSOA NUMBER NOT NULL PRIMARY KEY,
                        ID_USUARIO NUMBER NOT NULL,
                        NOME VARCHAR2(255) NOT NULL,
                        SOBRENOME VARCHAR2(255),
                        CPF VARCHAR2(11) NOT NULL,
                        CELULAR VARCHAR2(11) NOT NULL,
                        DATA_NASCIMENTO DATE,
                        PREFERENCIA VARCHAR2(60) NOT NULL,
                        ATIVO CHAR(1) NOT NULL,
                        CRIADO TIMESTAMP,
                        EDITADO TIMESTAMP,
                        CONSTRAINT CHK_ATIVO CHECK (ATIVO IN ('0', '1'))
);

CREATE TABLE USUARIO (
                         ID_USUARIO NUMBER NOT NULL PRIMARY KEY,
                         EMAIL VARCHAR2(100) UNIQUE NOT NULL,
                         SENHA VARCHAR2(255) NOT NULL,
                         ATIVO CHAR(1) NOT NULL
);

CREATE TABLE CARGO (
                       ID_CARGO NUMBER NOT NULL PRIMARY KEY,
                       DESCRICAO VARCHAR2(255) NOT NULL
);

CREATE TABLE CARGO_USUARIO (
                               ID_CARGO NUMBER NOT NULL,
                               ID_USUARIO NUMBER NOT NULL,
                               CONSTRAINT FK_CARGO FOREIGN KEY (ID_CARGO) REFERENCES CARGO (ID_CARGO),
                               CONSTRAINT FK_USUARIO FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO (ID_USUARIO),
                               PRIMARY KEY (ID_CARGO, ID_USUARIO)
);

CREATE TABLE CUPOM_DESCONTO (
                                ID_CUPOM NUMBER NOT NULL PRIMARY KEY,
                                CODIGO NUMBER NOT NULL,
                                VALOR NUMBER NOT NULL,
                                VALIDADE DATE NOT NULL,
                                LIMITE_USO NUMBER NOT NULL,
                                VALOR_MINIMO NUMBER NOT NULL
);

CREATE TABLE PEDIDO (
                        ID_PEDIDO NUMBER NOT NULL,
                        ID_PESSOA NUMBER NOT NULL,
                        ID_CUPOM NUMBER NOT NULL,
                        METODO_PAGAMENTO VARCHAR2(60) NOT NULL,
                        STATUS VARCHAR2(60) NOT NULL,
                        TOTAL_BRUTO NUMBER NOT NULL,
                        DESCONTO NUMBER NOT NULL,
                        TOTAL_LIQUIDO NUMBER NOT NULL,
                        CRIADO TIMESTAMP,
                        EDITADO TIMESTAMP,
                        PRIMARY KEY (ID_PEDIDO),
                        CONSTRAINT FK_PESSOA_NO_PEDIDO FOREIGN KEY (ID_PESSOA) REFERENCES PESSOA (ID_PESSOA),
                        CONSTRAINT FK_CUPOM_NO_PEDIDO FOREIGN KEY (ID_CUPOM) REFERENCES CUPOM_DESCONTO (ID_CUPOM)
);

CREATE TABLE FOTO (
                      ID_FOTO NUMBER NOT NULL PRIMARY KEY,
                      ID_VARIACAO NUMBER NOT NULL,
                      ARQUIVO BLOB
);

CREATE TABLE PRODUTO (
                         ID_PRODUTO NUMBER NOT NULL PRIMARY KEY,
                         CATEGORIA VARCHAR2(60) NOT NULL,
                         TITULO VARCHAR2(255) NOT NULL,
                         DESCRICAO VARCHAR2(255) NOT NULL,
                         TECIDO VARCHAR2(60) NOT NULL,
                         ATIVO CHAR(1) NOT NULL,
                         CRIADO TIMESTAMP,
                         EDITADO TIMESTAMP,
                         CONSTRAINT CHK_ATIVO_PRODUTO CHECK (ATIVO IN ('0', '1'))
);

CREATE TABLE VARIACAO (
                          ID_VARIACAO NUMBER NOT NULL PRIMARY KEY,
                          ID_PRODUTO NUMBER NOT NULL,
                          ID_FOTO NUMBER NOT NULL,
                          SKU VARCHAR2(255) NOT NULL,
                          COR VARCHAR2(255) NOT NULL,
                          TAMANHO VARCHAR2(255) NOT NULL,
                          PRECO NUMBER NOT NULL,
                          TAXA_DESCONTO NUMBER NOT NULL,
                          ATIVO CHAR(1) NOT NULL,
                          CRIADO TIMESTAMP,
                          EDITADO TIMESTAMP,
                          CONSTRAINT FK_PRODUTO_NA_VARIACAO FOREIGN KEY (ID_PRODUTO) REFERENCES PRODUTO (ID_PRODUTO),
                          CONSTRAINT FK_FOTO_NA_VARIACAO FOREIGN KEY (ID_FOTO) REFERENCES FOTO(ID_FOTO),
                          CONSTRAINT CHK_ATIVO_NA_VARIACAO CHECK (ATIVO IN ('0', '1'))
);

CREATE TABLE ITEM_PEDIDO (
                             ID_ITEM_PEDIDO NUMBER NOT NULL PRIMARY KEY,
                             ID_PEDIDO NUMBER NOT NULL,
                             ID_VARIACAO NUMBER NOT NULL,
                             QUANTIDADE NUMBER NOT NULL,
                             SUB_TOTAL NUMBER NOT NULL
);

CREATE TABLE ITEM_CARRINHO  (
                                ID_ITEM NUMBER NOT NULL,
                                ID_CARRINHO NUMBER NOT NULL,
                                PRIMARY KEY (ID_ITEM, ID_CARRINHO)
);

CREATE TABLE CARRINHO  (
                           ID_CARRINHO NUMBER NOT NULL PRIMARY KEY,
                           ID_USUARIO NUMBER NOT NULL,
                           TOTAL NUMBER NOT NULL
);

CREATE TABLE FAVORITOS (
                           ID_FAVORITOS NUMBER PRIMARY KEY,
                           ID_USUARIO NUMBER,
                           ID_VARIACAO NUMBER,
                           Criado TIMESTAMP,
                           Editado TIMESTAMP,
                           CONSTRAINT fk_favoritos_usuarios
                               FOREIGN KEY (ID_USUARIO)
                                   REFERENCES USUARIO(ID_USUARIO),
                           CONSTRAINT fk_favoritos_variacao
                               FOREIGN KEY (ID_VARIACAO)
                                   REFERENCES VARIACAO(ID_VARIACAO)
);

ALTER TABLE FOTO ADD CONSTRAINT FK_VARIACAO_EM_FOTOS FOREIGN KEY (ID_VARIACAO) REFERENCES VARIACAO (ID_VARIACAO);
ALTER TABLE ITEM_PEDIDO ADD CONSTRAINT FK_PEDIDO FOREIGN KEY (ID_PEDIDO) REFERENCES PEDIDO (ID_PEDIDO);
ALTER TABLE ITEM_PEDIDO ADD CONSTRAINT FK_VARIACAO FOREIGN KEY (ID_VARIACAO) REFERENCES VARIACAO (ID_VARIACAO);
ALTER TABLE ENDERECO ADD CONSTRAINT FK_PESSOA FOREIGN KEY (ID_PESSOA) REFERENCES PESSOA (ID_PESSOA);

-- CREATE SEQUENCE
CREATE SEQUENCE SEQ_ENDERECO START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE SEQ_PESSOA START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE SEQ_USUARIO START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE SEQ_CARGO START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE SEQ_CUPOM START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE SEQ_PEDIDO START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE SEQ_FOTO START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE SEQ_PRODUTO START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE SEQ_VARIACAO START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE SEQ_ITEM_PEDIDO START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE SEQ_FAVORITOS START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- Inserir usuários
INSERT INTO USUARIO (ID_USUARIO, EMAIL, SENHA, ATIVO) VALUES (SEQ_USUARIO.NEXTVAL, 'admin@email.com', '$2a$10$PuFWf.7OIvuc3k0jaz.ZC.hb7swUWn39JqrWde72u6qdKG.aCvtoa', 1);
INSERT INTO USUARIO (ID_USUARIO, EMAIL, SENHA, ATIVO) VALUES (SEQ_USUARIO.NEXTVAL, 'cliente@email.com', '$2a$10$PuFWf.7OIvuc3k0jaz.ZC.hb7swUWn39JqrWde72u6qdKG.aCvtoa', 1);
INSERT INTO USUARIO (ID_USUARIO, EMAIL, SENHA, ATIVO) VALUES (SEQ_USUARIO.NEXTVAL, 'funcionario@email.com', '$2a$10$PuFWf.7OIvuc3k0jaz.ZC.hb7swUWn39JqrWde72u6qdKG.aCvtoa', 1);


INSERT ALL
    INTO CARGO (ID_CARGO, DESCRICAO) VALUES (1, 'ROLE_ADMIN')
INTO CARGO (ID_CARGO, DESCRICAO) VALUES (2, 'ROLE_CLIENTE')
INTO CARGO (ID_CARGO, DESCRICAO) VALUES (3, 'ROLE_FUNCIONARIO')
INTO CARGO (ID_CARGO, DESCRICAO) VALUES (4, 'ROLE_FORNECEDOR')
SELECT * FROM DUAL;

INSERT ALL
    INTO CARGO_USUARIO (ID_CARGO, ID_USUARIO) VALUES (1, 1)
INTO CARGO_USUARIO (ID_CARGO, ID_USUARIO) VALUES (2, 2)
INTO CARGO_USUARIO (ID_CARGO, ID_USUARIO) VALUES (3, 3)
SELECT * FROM DUAL;

INSERT INTO VS_13_PROJETO_TSHIRT.PRODUTO
(ID_PRODUTO, CATEGORIA, TITULO, DESCRICAO, TECIDO, ATIVO, CRIADO, EDITADO)
VALUES(1, 'Camiseta', 'Camiseta Branca', 'Camiseta branca básica', 'Algodão', '1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

SELECT * FROM USUARIO;
SELECT * FROM PESSOA;
SELECT * FROM CARGO;
SELECT * FROM CARGO_USUARIO;
SELECT * FROM PRODUTO;
SELECT * FROM PEDIDO;
SELECT * FROM ITEM_PEDIDO;
SELECT * FROM ITEM_CARRINHO;
SELECT * FROM CARGO_USUARIO;
SELECT * FROM VARIACAO;
SELECT * FROM CUPOM_DESCONTO;
SELECT * FROM CARRINHO;
