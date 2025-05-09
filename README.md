
## Arquitetura

### Banco de Dados:
Uso de convenção de prefixos para tabelas e colunas, tais quais:

1. Tabelas:
- `tbl_` para tabelas
---
Para possível futuro poder-se utilizar:
- `vw_` para views
- `sp_` para stored procedures
- `ix_` para índices
---
2. Colunas:
- `id_` para identificadores
- `txt_` para textos
- `num_` para números inteiros
- `flt_` para números de ponto flutuante
- `dt_` para datas
- `bol_` para booleanos

### Lembre-se que isso não é um padrão definido, dá pra usar outras nomenclaturas com semânticas e propostas totalmente diferentes, vai do costume do programador e da equipe que trabalhará no projeto! Fiz assim pois é do meu costume \o/
