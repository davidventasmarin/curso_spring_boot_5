/* Poblar tabla cliente */
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('John', 'Doe', 'john.doe@gmail.com', '2017-08-02', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Linus', 'Torvalds', 'linus.torvalds@gmail.com', '2017-08-03', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Jane', 'Doe', 'jane.doe@gmail.com', '2017-08-04', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Rasmus', 'Lerdorf', 'rasmus.lerdorf@gmail.com', '2017-08-05', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Erich', 'Gamma', 'erich.gamma@gmail.com', '2017-08-06', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Richard', 'Helm', 'richard.helm@gmail.com', '2017-08-07', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Ralph', 'Johnson', 'ralph.johnson@gmail.com', '2017-08-08', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('John', 'Vlissides', 'john.vlissides@gmail.com', '2017-08-09', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('James', 'Gosling', 'james.gosling@gmail.com', '2017-08-010', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Bruce', 'Lee', 'bruce.lee@gmail.com', '2017-08-11', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Johnny', 'Doe', 'johnny.doe@gmail.com', '2017-08-12', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('John', 'Roe', 'john.roe@gmail.com', '2017-08-13', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Jane', 'Roe', 'jane.roe@gmail.com', '2017-08-14', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Richard', 'Doe', 'richard.doe@gmail.com', '2017-08-15', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Janie', 'Doe', 'janie.doe@gmail.com', '2017-08-16', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Phillip', 'Webb', 'phillip.webb@gmail.com', '2017-08-17', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Stephane', 'Nicoll', 'stephane.nicoll@gmail.com', '2017-08-18', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Sam', 'Brannen', 'sam.brannen@gmail.com', '2017-08-19', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Juergen', 'Hoeller', 'juergen.Hoeller@gmail.com', '2017-08-20', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Janie', 'Roe', 'janie.roe@gmail.com', '2017-08-21', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('John', 'Smith', 'john.smith@gmail.com', '2017-08-22', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Joe', 'Bloggs', 'joe.bloggs@gmail.com', '2017-08-23', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('John', 'Stiles', 'john.stiles@gmail.com', '2017-08-24', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Richard', 'Roe', 'stiles.roe@gmail.com', '2017-08-25', '');
INSERT INTO clientes (nombre, apellidos, email, create_at, foto) VALUES('Javi', 'Fraile', 'stiles.roe@gmail.com', '2017-08-25', '');

/* Poblar tabla productos */
INSERT INTO productos (nombre, precio, create_at) VALUES ('Thinkpad W530', 430, now());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Thinkpad x260', 230, now());
INSERT INTO productos (nombre, precio, create_at) VALUES ('PlayStation 5', 500, now());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Colección de juegos Assasins Creed', 19, now());
INSERT INTO productos (nombre, precio, create_at) VALUES ('King Kong 3', 350, now());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Monitor philips 24"', 100, now());
INSERT INTO productos (nombre, precio, create_at) VALUES ('Raspberry Pi 4 4GB', 130, now());

/* Poblar tabla facturas e itemfacturas */
INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES ('Ordenadores portatiles', null, 1, now());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (3,1,1);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (2,1,4);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (4,1,5);

INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES ('Juegos', null, 1, now());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (3,2,4);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (3,2,3);