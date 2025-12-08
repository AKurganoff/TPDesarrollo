--
-- PostgreSQL database dump
--

\restrict NJfiV4FCK8SthdfcayWCdmuQQYnPG67eZFB7ZffxY1FJGj1l5TP5f8VPL4Nx7XL

-- Dumped from database version 16.10 (Debian 16.10-1.pgdg13+1)
-- Dumped by pg_dump version 16.10

--SET statement_timeout = 0;
--SET lock_timeout = 0;
--SET idle_in_transaction_session_timeout = 0;
--SET client_encoding = 'UTF8';
--SET standard_conforming_strings = on;
--SELECT pg_catalog.set_config('search_path', '', false);
--SET check_function_bodies = false;
--SET xmloption = content;
--SET client_min_messages = warning;
--SET row_security = off;

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA IF NOT EXISTS public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: cheque; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cheque (
    id_mediopago integer NOT NULL,
    nombre_titular character varying(20) NOT NULL,
    apellido_titular character varying(20) NOT NULL,
    CONSTRAINT cheque_id_mediopago_check CHECK (((id_mediopago > 0) AND (id_mediopago < 6)))
);


ALTER TABLE public.cheque OWNER TO postgres;

--
-- Name: consumible; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.consumible (
    id_estadia integer NOT NULL,
    precio double precision NOT NULL,
    id_consumible integer NOT NULL,
    nombre character varying(50) NOT NULL,
    CONSTRAINT consumible_id_consumible_check CHECK ((id_consumible > 0)),
    CONSTRAINT consumible_id_estadia_check CHECK ((id_estadia > 0)),
    CONSTRAINT consumible_precio_check CHECK ((precio > (0)::double precision))
);


ALTER TABLE public.consumible OWNER TO postgres;

--
-- Name: credito; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.credito (
    id_mediopago integer,
    nro_tarjeta integer NOT NULL,
    cuotas integer NOT NULL,
    nombre_titular character varying(20) NOT NULL,
    apellido_titular character varying(20) NOT NULL,
    CONSTRAINT credito_cuotas_check CHECK ((cuotas > 0)),
    CONSTRAINT credito_id_mediopago_check CHECK (((id_mediopago > 0) AND (id_mediopago < 6)))
);


ALTER TABLE public.credito OWNER TO postgres;

--
-- Name: debito; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.debito (
    id_mediopago integer,
    nro_tarjeta integer NOT NULL,
    nombre_titular character varying(20) NOT NULL,
    apellido_titular character varying(20) NOT NULL,
    CONSTRAINT debito_id_mediopago_check CHECK (((id_mediopago > 0) AND (id_mediopago < 6)))
);


ALTER TABLE public.debito OWNER TO postgres;

--
-- Name: direccion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.direccion (
    calle character varying(20) NOT NULL,
    numero integer NOT NULL,
    departamento character varying(5) NOT NULL,
    piso integer NOT NULL,
    cod_postal integer NOT NULL,
    localidad character varying(20) NOT NULL,
    provincia character varying(20) NOT NULL,
    pais character varying(20) NOT NULL,
    CONSTRAINT direccion_cod_postal_check CHECK ((cod_postal > 0)),
    CONSTRAINT direccion_numero_check CHECK ((numero > 0)),
    CONSTRAINT direccion_piso_check CHECK ((piso >= 0))
);


ALTER TABLE public.direccion OWNER TO postgres;

--
-- Name: efectivo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.efectivo (
    id_mediopago integer NOT NULL,
    CONSTRAINT efectivo_id_mediopago_check CHECK (((id_mediopago > 0) AND (id_mediopago < 6)))
);


ALTER TABLE public.efectivo OWNER TO postgres;

--
-- Name: estadia; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.estadia (
    id_estadia integer NOT NULL,
    precio double precision NOT NULL,
    hora_checkin time without time zone NOT NULL,
    hora_checkout time without time zone NOT NULL,
    id_reserva integer,
    CONSTRAINT estadia_id_estadia_check CHECK ((id_estadia > 0)),
    CONSTRAINT estadia_id_reserva_check CHECK ((id_reserva > 0)),
    CONSTRAINT estadia_precio_check CHECK ((precio > (0)::double precision))
);


ALTER TABLE public.estadia OWNER TO postgres;

--
-- Name: factura; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.factura (
    id_factura integer NOT NULL,
    id_responsablepago integer NOT NULL,
    precio_final double precision NOT NULL,
    id_estadia integer NOT NULL,
    nro_factura integer NOT NULL,
    tipo_factura character varying(3) NOT NULL,
    fecha date NOT NULL,
    CONSTRAINT factura_id_estadia_check CHECK ((id_estadia > 0)),
    CONSTRAINT factura_id_factura_check CHECK ((id_factura > 0)),
    CONSTRAINT factura_id_responsablepago_check CHECK ((id_responsablepago > 0)),
    CONSTRAINT factura_nro_factura_check CHECK ((nro_factura > 0))
);


ALTER TABLE public.factura OWNER TO postgres;

--
-- Name: guarda_una; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.guarda_una (
    id_reserva integer NOT NULL,
    id_habitacion integer NOT NULL,
    CONSTRAINT guarda_una_id_habitacion_check CHECK ((id_habitacion > 0)),
    CONSTRAINT guarda_una_id_reserva_check CHECK ((id_reserva > 0))
);


ALTER TABLE public.guarda_una OWNER TO postgres;

--
-- Name: habitacion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.habitacion (
    id_habitacion integer NOT NULL,
    idtipo integer NOT NULL,
    noches_descuento integer NOT NULL,
    estado character varying(9) NOT NULL,
    CONSTRAINT habitacion_id_habitacion_check CHECK (((id_habitacion > 0) AND (id_habitacion < 49))),
    CONSTRAINT habitacion_idtipo_check CHECK (((idtipo > 0) AND (idtipo < 6)))
);


ALTER TABLE public.habitacion OWNER TO postgres;

--
-- Name: huesped; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.huesped (
    dni integer NOT NULL,
    nombre character varying(20) NOT NULL,
    apellido character varying(20) NOT NULL,
    edad integer NOT NULL,
    fecha_nacimiento date NOT NULL,
    ocupacion character varying(50),
    tipodni character varying(10) NOT NULL,
    mail character varying(20) NOT NULL,
    pos_iva character varying(20) NOT NULL,
    calle character varying(20) NOT NULL,
    numero integer NOT NULL,
    departamento character varying(5) NOT NULL,
    piso integer NOT NULL,
    cod_postal integer NOT NULL,
    CONSTRAINT huesped_cod_postal_check CHECK ((cod_postal > 0)),
    CONSTRAINT huesped_dni_check CHECK ((dni > 0)),
    CONSTRAINT huesped_edad_check CHECK (((edad > 0) AND (edad < 110))),
    CONSTRAINT huesped_numero_check CHECK ((numero > 0)),
    CONSTRAINT huesped_piso_check CHECK ((piso >= 0))
);


ALTER TABLE public.huesped OWNER TO postgres;

--
-- Name: juridica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.juridica (
    cuit integer NOT NULL,
    razon_social character varying NOT NULL,
    calle character varying(20) NOT NULL,
    numero integer NOT NULL,
    departamento character varying(5) NOT NULL,
    piso integer NOT NULL,
    cod_postal integer NOT NULL,
    CONSTRAINT juridica_cod_postal_check CHECK ((cod_postal > 0)),
    CONSTRAINT juridica_cuit_check CHECK ((cuit > 0)),
    CONSTRAINT juridica_numero_check CHECK ((numero > 0)),
    CONSTRAINT juridica_piso_check CHECK ((piso >= 0))
);


ALTER TABLE public.juridica OWNER TO postgres;

--
-- Name: mediopago; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mediopago (
    id_mediopago integer NOT NULL,
    nombre_medio character varying(20) NOT NULL,
    CONSTRAINT mediopago_id_mediopago_check CHECK (((id_mediopago > 0) AND (id_mediopago < 6)))
);


ALTER TABLE public.mediopago OWNER TO postgres;

--
-- Name: monedaextrajera; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.monedaextrajera (
    id_mediopago integer NOT NULL,
    tipo_moneda character varying(20) NOT NULL,
    valor_cambio double precision NOT NULL,
    CONSTRAINT monedaextrajera_id_mediopago_check CHECK (((id_mediopago > 0) AND (id_mediopago < 6)))
);


ALTER TABLE public.monedaextrajera OWNER TO postgres;

--
-- Name: notacredito; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notacredito (
    facturaasociada integer NOT NULL,
    id_notacredito integer NOT NULL,
    monto double precision NOT NULL,
    fecha date NOT NULL,
    CONSTRAINT notacredito_facturaasociada_check CHECK ((facturaasociada > 0)),
    CONSTRAINT notacredito_id_notacredito_check CHECK ((id_notacredito > 0))
);


ALTER TABLE public.notacredito OWNER TO postgres;

--
-- Name: pago; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pago (
    id_pago integer NOT NULL,
    id_mediopago integer NOT NULL,
    id_factura integer NOT NULL,
    id_responsablepago integer NOT NULL,
    monto double precision NOT NULL,
    fecha date NOT NULL,
    CONSTRAINT pago_id_factura_check CHECK ((id_factura > 0)),
    CONSTRAINT pago_id_mediopago_check CHECK (((id_mediopago > 0) AND (id_mediopago < 6))),
    CONSTRAINT pago_id_pago_check CHECK ((id_pago > 0)),
    CONSTRAINT pago_id_responsablepago_check CHECK ((id_responsablepago > 0)),
    CONSTRAINT pago_monto_check CHECK ((monto > (0)::double precision))
);


ALTER TABLE public.pago OWNER TO postgres;

--
-- Name: reserva; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reserva (
    id_reserva integer NOT NULL,
    dni_huesped integer NOT NULL,
    cant_huesped integer NOT NULL,
    fecha_inicio date NOT NULL,
    cant_noches integer NOT NULL,
    id_habitacion integer,
    descuento boolean NOT NULL,
    CONSTRAINT reserva_cant_huesped_check CHECK ((cant_huesped > 0)),
    CONSTRAINT reserva_cant_noches_check CHECK ((cant_noches > 0)),
    CONSTRAINT reserva_dni_huesped_check CHECK ((dni_huesped > 0)),
    CONSTRAINT reserva_id_habitacion_check CHECK (((id_habitacion > 0) AND (id_habitacion < 48))),
    CONSTRAINT reserva_id_reserva_check CHECK ((id_reserva > 0))
);


ALTER TABLE public.reserva OWNER TO postgres;

--
-- Name: responsablepago; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.responsablepago (
    id_responsablepago integer NOT NULL,
    cuit integer,
    razon_social character varying NOT NULL,
    CONSTRAINT responsablepago_cuit_check CHECK ((cuit > 0)),
    CONSTRAINT responsablepago_id_responsablepago_check CHECK ((id_responsablepago > 0))
);


ALTER TABLE public.responsablepago OWNER TO postgres;

--
-- Name: telefono; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.telefono (
    dni integer NOT NULL,
    telefono integer NOT NULL,
    CONSTRAINT telefono_dni_check CHECK ((dni > 0))
);


ALTER TABLE public.telefono OWNER TO postgres;

--
-- Name: tipohabitacion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tipohabitacion (
    idtipo integer NOT NULL,
    nombretipo character varying(20) NOT NULL,
    cantidaddisponible integer NOT NULL,
    precio_x_noche integer NOT NULL,
    CONSTRAINT tipohabitacion_idtipo_check CHECK (((idtipo > 0) AND (idtipo < 6))),
    CONSTRAINT tipohabitacion_precio_x_noche_check CHECK ((precio_x_noche > 0))
);


ALTER TABLE public.tipohabitacion OWNER TO postgres;

--
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    nombre character varying(20) NOT NULL,
    psw character varying(20) NOT NULL
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- Data for Name: cheque; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cheque (id_mediopago, nombre_titular, apellido_titular) FROM stdin;
\.


--
-- Data for Name: consumible; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.consumible (id_estadia, precio, id_consumible, nombre) FROM stdin;
\.


--
-- Data for Name: credito; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.credito (id_mediopago, nro_tarjeta, cuotas, nombre_titular, apellido_titular) FROM stdin;
\.


--
-- Data for Name: debito; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.debito (id_mediopago, nro_tarjeta, nombre_titular, apellido_titular) FROM stdin;
\.


--
-- Data for Name: direccion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.direccion (calle, numero, departamento, piso, cod_postal, localidad, provincia, pais) FROM stdin;
Corrientes	1530	B	5	1042	Buenos Aires	CABA	Argentina
General Paz	345	A	2	5000	Córdoba	Córdoba	Argentina
Bv. Gálvez	1335	C	8	3000	Santa Fe	Santa Fe	Argentina
San Martín	890	D	1	5500	Mendoza	Mendoza	Argentina
Av. Argentina	150	E	3	8300	Neuquén	Neuquén	Argentina
Zapiola	850	PB	0	9400	Río Gallegos	Santa Cruz	Argentina
San Martín	1200	F	6	9410	Ushuaia	Tierra del Fuego	Argentina
24 de Septiembre	700	1A	1	4000	Tucumán	Tucumán	Argentina
Caseros	921	C	4	4400	Salta	Salta	Argentina
Roque Sáenz Peña	101	7B	7	3300	Posadas	Misiones	Argentina
\.


--
-- Data for Name: efectivo; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.efectivo (id_mediopago) FROM stdin;
\.


--
-- Data for Name: estadia; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.estadia (id_estadia, precio, hora_checkin, hora_checkout, id_reserva) FROM stdin;
\.


--
-- Data for Name: factura; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.factura (id_factura, id_responsablepago, precio_final, id_estadia, nro_factura, tipo_factura, fecha) FROM stdin;
\.


--
-- Data for Name: guarda_una; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.guarda_una (id_reserva, id_habitacion) FROM stdin;
\.


--
-- Data for Name: habitacion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.habitacion (id_habitacion, idtipo, noches_descuento, estado) FROM stdin;
1	1	5	Libre
2	1	5	Libre
3	1	5	Libre
4	1	5	Libre
5	1	5	Libre
6	1	5	Libre
7	1	5	Libre
8	1	5	Libre
9	1	5	Libre
10	1	5	Libre
11	2	6	Libre
12	2	6	Libre
13	2	6	Libre
14	2	6	Libre
15	2	6	Libre
16	2	6	Libre
17	2	6	Libre
18	2	6	Libre
19	2	6	Libre
20	2	6	Libre
21	2	6	Libre
22	2	6	Libre
23	2	6	Libre
24	2	6	Libre
25	2	6	Libre
26	2	6	Libre
27	2	6	Libre
28	2	6	Libre
29	3	5	Libre
30	3	5	Libre
31	3	5	Libre
32	3	5	Libre
33	3	5	Libre
34	3	5	Libre
35	3	5	Libre
36	3	5	Libre
37	4	6	Libre
38	4	6	Libre
39	4	6	Libre
40	4	6	Libre
41	4	6	Libre
42	4	6	Libre
43	4	6	Libre
44	4	6	Libre
45	4	6	Libre
46	4	6	Libre
47	5	3	Libre
48	5	3	Libre
\.


--
-- Data for Name: huesped; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.huesped (dni, nombre, apellido, edad, fecha_nacimiento, ocupacion, tipodni, mail, pos_iva, calle, numero, departamento, piso, cod_postal) FROM stdin;
18567345	Ricardo	Giménez	55	1970-05-15	Empresario	DNI	ricardo.gim@mail.com	Resp. Inscripto	Zapiola	850	PB	0	9400
40987654	Florencia	Díaz	28	1997-12-01	Diseñadora	DNI	flordiaz@mail.com	Consumidor Final	San Martín	1200	F	6	9410
33456789	Javier	Rodríguez	38	1987-03-20	Comerciante	DNI	javi.rod@mail.com	Monotributista	24 de Septiembre	700	1A	1	4000
25123456	María	López	49	1976-08-10	Abogada	DNI	mlopez@mail.com	Resp. Inscripto	Caseros	921	C	4	4400
43001998	Lucas	Pérez	24	2001-01-25	Estudiante	DNI	lucas.perez@mail.com	Consumidor Final	Roque Sáenz Peña	101	7B	7	3300
\.


--
-- Data for Name: juridica; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.juridica (cuit, razon_social, calle, numero, departamento, piso, cod_postal) FROM stdin;
301111111	Servicios Digitales S.A.	Corrientes	1530	B	5	1042
302222222	Consultora del Sur SRL	General Paz	345	A	2	5000
303333333	Logística Integral E.S.	Bv. Gálvez	1335	C	8	3000
\.


--
-- Data for Name: mediopago; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.mediopago (id_mediopago, nombre_medio) FROM stdin;
1	efectivo
2	credito
3	debito
4	cheque
5	moneda extranjera
\.


--
-- Data for Name: monedaextrajera; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.monedaextrajera (id_mediopago, tipo_moneda, valor_cambio) FROM stdin;
\.


--
-- Data for Name: notacredito; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.notacredito (facturaasociada, id_notacredito, monto, fecha) FROM stdin;
\.


--
-- Data for Name: pago; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pago (id_pago, id_mediopago, id_factura, id_responsablepago, monto, fecha) FROM stdin;
\.


--
-- Data for Name: reserva; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.reserva (id_reserva, dni_huesped, cant_huesped, fecha_inicio, cant_noches, id_habitacion, descuento) FROM stdin;
\.


--
-- Data for Name: responsablepago; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.responsablepago (id_responsablepago, cuit, razon_social) FROM stdin;
1	301111111	Servicios Digitales S.A.
2	302222222	Consultora del Sur SRL
3	303333333	Logística Integral E.S.
\.


--
-- Data for Name: telefono; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.telefono (dni, telefono) FROM stdin;
18567345	115555
40987654	351444
33456789	381666
25123456	261333
43001998	376222
\.


--
-- Data for Name: tipohabitacion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tipohabitacion (idtipo, nombretipo, cantidaddisponible, precio_x_noche) FROM stdin;
1	Individual Estandar	10	50800
2	Doble Estandar	18	70230
3	Doble Superior	8	90560
4	Superior Family Plan	10	110500
5	Suite Doble	2	128600
\.


--
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuario (nombre, psw) FROM stdin;
conserje	admin123
\.


--
-- PostgreSQL database dump complete
--

\unrestrict NJfiV4FCK8SthdfcayWCdmuQQYnPG67eZFB7ZffxY1FJGj1l5TP5f8VPL4Nx7XL

