CREATE TABLE usuarios(
  id BIGINT NOT NULL auto_increment,
  nombreusuario VARCHAR(100) NOT NULL,
  clave VARCHAR(100) NOT NULL,

  PRIMARY KEY(id)
);