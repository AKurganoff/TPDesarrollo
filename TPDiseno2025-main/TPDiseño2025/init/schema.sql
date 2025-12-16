--
-- PostgreSQL database dump
--

\restrict hdw7weSHhfWgWebAaO5uNGJtHDab25oUFuq2v21ON3jUdhIYTRJ29o7PYLLYGCX

-- Dumped from database version 16.10 (Debian 16.10-1.pgdg13+1)
-- Dumped by pg_dump version 16.10

-- Started on 2025-12-09 15:19:51 UTC

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA IF NOT EXISTS public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 3634 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 16384)
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
-- TOC entry 216 (class 1259 OID 16388)
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
-- TOC entry 217 (class 1259 OID 16394)
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
-- TOC entry 218 (class 1259 OID 16399)
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
-- TOC entry 243 (class 1259 OID 16595)
-- Name: detalle_estadia; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.detalle_estadia (
    id_detalleestadia integer NOT NULL,
    id_estadia integer NOT NULL,
    dni_huesped integer NOT NULL
);


ALTER TABLE public.detalle_estadia OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 16615)
-- Name: detalle_estadia_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.detalle_estadia_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.detalle_estadia_id_seq OWNER TO postgres;

--
-- TOC entry 3635 (class 0 OID 0)
-- Dependencies: 245
-- Name: detalle_estadia_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.detalle_estadia_id_seq OWNED BY public.detalle_estadia.id_detalleestadia;


--
-- TOC entry 237 (class 1259 OID 16532)
-- Name: detalle_notacredito; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.detalle_notacredito (
    id_detallenota integer NOT NULL,
    id_notacredito integer NOT NULL,
    id_factura integer NOT NULL,
    monto_aplicado double precision NOT NULL
);


ALTER TABLE public.detalle_notacredito OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 16558)
-- Name: detalle_notacredito_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.detalle_notacredito_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.detalle_notacredito_id_seq OWNER TO postgres;

--
-- TOC entry 3636 (class 0 OID 0)
-- Dependencies: 242
-- Name: detalle_notacredito_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.detalle_notacredito_id_seq OWNED BY public.detalle_notacredito.id_notacredito;


--
-- TOC entry 235 (class 1259 OID 16509)
-- Name: detalle_reserva; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.detalle_reserva (
    id_detalle integer NOT NULL,
    id_reserva integer NOT NULL,
    id_habitacion integer NOT NULL,
    precio double precision NOT NULL,
    cantidad_noches integer NOT NULL
);


ALTER TABLE public.detalle_reserva OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 16552)
-- Name: detalle_reserva_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.detalle_reserva_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.detalle_reserva_id_seq OWNER TO postgres;

--
-- TOC entry 3637 (class 0 OID 0)
-- Dependencies: 239
-- Name: detalle_reserva_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.detalle_reserva_id_seq OWNED BY public.detalle_reserva.id_detalle;


--
-- TOC entry 219 (class 1259 OID 16403)
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
-- TOC entry 220 (class 1259 OID 16409)
-- Name: efectivo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.efectivo (
    id_mediopago integer NOT NULL,
    CONSTRAINT efectivo_id_mediopago_check CHECK (((id_mediopago > 0) AND (id_mediopago < 6)))
);


ALTER TABLE public.efectivo OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16413)
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
-- TOC entry 244 (class 1259 OID 16612)
-- Name: estadia_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.estadia_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.estadia_id_seq OWNER TO postgres;

--
-- TOC entry 3638 (class 0 OID 0)
-- Dependencies: 244
-- Name: estadia_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.estadia_id_seq OWNED BY public.estadia.id_estadia;


--
-- TOC entry 222 (class 1259 OID 16419)
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
-- TOC entry 241 (class 1259 OID 16556)
-- Name: factura_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.factura_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.factura_id_seq OWNER TO postgres;

--
-- TOC entry 3639 (class 0 OID 0)
-- Dependencies: 241
-- Name: factura_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.factura_id_seq OWNED BY public.factura.id_factura;


--
-- TOC entry 223 (class 1259 OID 16426)
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
-- TOC entry 224 (class 1259 OID 16431)
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
-- TOC entry 225 (class 1259 OID 16436)
-- Name: huesped; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.huesped (
    dni_huesped integer NOT NULL,
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
    CONSTRAINT huesped_dni_check CHECK ((dni_huesped > 0)),
    CONSTRAINT huesped_edad_check CHECK (((edad > 0) AND (edad < 110))),
    CONSTRAINT huesped_numero_check CHECK ((numero > 0)),
    CONSTRAINT huesped_piso_check CHECK ((piso >= 0))
);


ALTER TABLE public.huesped OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16444)
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
-- TOC entry 227 (class 1259 OID 16453)
-- Name: mediopago; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mediopago (
    id_mediopago integer NOT NULL,
    nombre_medio character varying(20) NOT NULL,
    CONSTRAINT mediopago_id_mediopago_check CHECK (((id_mediopago > 0) AND (id_mediopago < 6)))
);


ALTER TABLE public.mediopago OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 16457)
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
-- TOC entry 236 (class 1259 OID 16526)
-- Name: notacredito; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notacredito (
    id_notacredito integer NOT NULL,
    fecha date NOT NULL,
    monto double precision NOT NULL,
    motivo character varying(200),
    CONSTRAINT notacredito_monto_check CHECK ((monto > (0)::double precision))
);


ALTER TABLE public.notacredito OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 16554)
-- Name: nota_credito_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.nota_credito_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.nota_credito_id_seq OWNER TO postgres;

--
-- TOC entry 3640 (class 0 OID 0)
-- Dependencies: 240
-- Name: nota_credito_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.nota_credito_id_seq OWNED BY public.notacredito.id_notacredito;


--
-- TOC entry 229 (class 1259 OID 16466)
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
-- TOC entry 230 (class 1259 OID 16474)
-- Name: reserva; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reserva (
    id_reserva integer NOT NULL,
    dni_huesped integer NOT NULL,
    cant_huesped integer NOT NULL,
    fecha_inicio date NOT NULL,
    cant_noches integer NOT NULL,
    descuento boolean NOT NULL,
    estado character varying(10) NOT NULL,
    CONSTRAINT reserva_cant_huesped_check CHECK ((cant_huesped > 0)),
    CONSTRAINT reserva_cant_noches_check CHECK ((cant_noches > 0)),
    CONSTRAINT reserva_dni_huesped_check CHECK ((dni_huesped > 0)),
    CONSTRAINT reserva_id_reserva_check CHECK ((id_reserva > 0))
);


ALTER TABLE public.reserva OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 16550)
-- Name: reserva_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.reserva_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.reserva_id_seq OWNER TO postgres;

--
-- TOC entry 3641 (class 0 OID 0)
-- Dependencies: 238
-- Name: reserva_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.reserva_id_seq OWNED BY public.reserva.id_reserva;


--
-- TOC entry 231 (class 1259 OID 16482)
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
-- TOC entry 246 (class 1259 OID 16625)
-- Name: telefono_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.telefono_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.telefono_id_seq OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 16489)
-- Name: telefono; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.telefono (
    dni integer NOT NULL,
    telefono character varying(20) NOT NULL,
    id_telefono integer DEFAULT nextval('public.telefono_id_seq'::regclass) NOT NULL,
    CONSTRAINT telefono_dni_check CHECK ((dni > 0))
);


ALTER TABLE public.telefono OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 16493)
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
-- TOC entry 234 (class 1259 OID 16498)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    nombre character varying(20) NOT NULL,
    psw character varying(20) NOT NULL
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 3373 (class 2604 OID 16616)
-- Name: detalle_estadia id_detalleestadia; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_estadia ALTER COLUMN id_detalleestadia SET DEFAULT nextval('public.detalle_estadia_id_seq'::regclass);


--
-- TOC entry 3372 (class 2604 OID 16559)
-- Name: detalle_notacredito id_detallenota; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_notacredito ALTER COLUMN id_detallenota SET DEFAULT nextval('public.detalle_notacredito_id_seq'::regclass);


--
-- TOC entry 3370 (class 2604 OID 16553)
-- Name: detalle_reserva id_detalle; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detalle_reserva ALTER COLUMN id_detalle SET DEFAULT nextval('public.detalle_reserva_id_seq'::regclass);


--
-- TOC entry 3366 (class 2604 OID 16614)
-- Name: estadia id_estadia; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.estadia ALTER COLUMN id_estadia SET DEFAULT nextval('public.estadia_id_seq'::regclass);


--
-- TOC entry 3367 (class 2604 OID 16557)
-- Name: factura id_factura; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.factura ALTER COLUMN id_factura SET DEFAULT nextval('public.factura_id_seq'::regclass);


--
-- TOC entry 3371 (class 2604 OID 16555)
-- Name: notacredito id_notacredito; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notacredito ALTER COLUMN id_notacredito SET DEFAULT nextval('public.nota_credito_id_seq'::regclass);


--
-- TOC entry 3368 (class 2604 OID 16551)
-- Name: reserva id_reserva; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reserva ALTER COLUMN id_reserva SET DEFAULT nextval('public.reserva_id_seq'::regclass);


--
-- TOC entry 3597 (class 0 OID 16384)
-- Dependencies: 215
-- Data for Name: cheque; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cheque (id_mediopago, nombre_titular, apellido_titular) FROM stdin;
\.


--
-- TOC entry 3598 (class 0 OID 16388)
-- Dependencies: 216
-- Data for Name: consumible; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.consumible (id_estadia, precio, id_consumible, nombre) FROM stdin;
\.


--
-- TOC entry 3599 (class 0 OID 16394)
-- Dependencies: 217
-- Data for Name: credito; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.credito (id_mediopago, nro_tarjeta, cuotas, nombre_titular, apellido_titular) FROM stdin;
\.


--
-- TOC entry 3600 (class 0 OID 16399)
-- Dependencies: 218
-- Data for Name: debito; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.debito (id_mediopago, nro_tarjeta, nombre_titular, apellido_titular) FROM stdin;
\.


--
-- TOC entry 3625 (class 0 OID 16595)
-- Dependencies: 243
-- Data for Name: detalle_estadia; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.detalle_estadia (id_detalleestadia, id_estadia, dni_huesped) FROM stdin;
1	1	40987654
2	1	234567822
3	1	43434343
4	1	45456789
5	2	18567345
6	2	33456789
7	2	25123456
8	3	43434343
9	3	45456789
10	3	234567822
11	4	25123456
12	4	43001998
13	4	33456789
14	5	40987654
15	5	234567822
16	5	43434343
17	6	45456789
18	7	40987654
19	8	234567822
20	8	18567345
\.


--
-- TOC entry 3619 (class 0 OID 16532)
-- Dependencies: 237
-- Data for Name: detalle_notacredito; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.detalle_notacredito (id_detallenota, id_notacredito, id_factura, monto_aplicado) FROM stdin;
\.


--
-- TOC entry 3617 (class 0 OID 16509)
-- Dependencies: 235
-- Data for Name: detalle_reserva; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.detalle_reserva (id_detalle, id_reserva, id_habitacion, precio, cantidad_noches) FROM stdin;
1	1	1	1066800	21
2	2	47	385800	3
3	3	1	254000	5
4	4	11	2317590	33
5	5	1	304800	6
6	6	2	304800	6
7	7	48	643000	5
8	8	4	203200	4
9	9	7	355600	7
10	10	8	101600	2
11	11	1	304800	6
12	12	1	406400	8
13	13	47	128600	1
14	14	4	254000	5
\.


--
-- TOC entry 3601 (class 0 OID 16403)
-- Dependencies: 219
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
cordoba	46	a	1	2447	Santa Fe	Santa Fe	Paraguay
Sarmiento	2342	a	3	3000	Santafe	Santa Fe	Argentina
San Martin	1612	-	0	3000	Santa fe	Santa Fe	Argentina
San Lorenzo	2334	a	1	2556	Cordoba	Cordoba	Argentino
Cordoba	46	a	1	3000	Santa Fe	Santa Fe	Argentino
Lavaise	2212	a	1	3001	Santa Fe	Santa Fe	Argentino
Las heras	23		0	3000	Santa Fe	Buenos Aires	Argentina
Roque Sáenz Peña	2332	B	7	2444	San Lorenzo	Buenos Aires	Estados Unidos
Hogwarts	12	t	5	2222	Santa Fe	Santa Fe	Argentina
Sarmiento	1232	c	5	3000	Santa Fe	Santa Fe	Argentina
Zapiola	850	PB	3	9400	Río Gallegos	Santa Cruz	Argentina
Alvear	2344		0	3000	Santa Fe	Santa Fe	Argentina
Candioti	3322	a	5	3001	San Cristobal	Santa Fe	Argentina
calle	2443	a	1	3000	Santa fe	Santa Fe	Argentina
\.


--
-- TOC entry 3602 (class 0 OID 16409)
-- Dependencies: 220
-- Data for Name: efectivo; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.efectivo (id_mediopago) FROM stdin;
\.


--
-- TOC entry 3603 (class 0 OID 16413)
-- Dependencies: 221
-- Data for Name: estadia; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.estadia (id_estadia, precio, hora_checkin, hora_checkout, id_reserva) FROM stdin;
1	50800	04:00:20.808	10:00:00	5
2	50800	04:00:48.852	10:00:00	6
3	128600	04:03:32.431	10:00:00	7
4	50800	04:17:12.276	10:00:00	9
5	128600	23:51:03.945	10:00:00	13
6	50800	19:38:43.04	10:00:00	1
7	50800	19:40:37.645	10:00:00	3
8	70230	21:34:33.274	10:00:00	4
\.


--
-- TOC entry 3604 (class 0 OID 16419)
-- Dependencies: 222
-- Data for Name: factura; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.factura (id_factura, id_responsablepago, precio_final, id_estadia, nro_factura, tipo_factura, fecha) FROM stdin;
\.


--
-- TOC entry 3605 (class 0 OID 16426)
-- Dependencies: 223
-- Data for Name: guarda_una; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.guarda_una (id_reserva, id_habitacion) FROM stdin;
\.


--
-- TOC entry 3606 (class 0 OID 16431)
-- Dependencies: 224
-- Data for Name: habitacion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.habitacion (id_habitacion, idtipo, noches_descuento, estado) FROM stdin;
3	1	5	Libre
4	1	5	Libre
5	1	5	Libre
6	1	5	Libre
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
1	1	5	Libre
2	1	5	Libre
48	5	3	Libre
7	1	5	Libre
\.


--
-- TOC entry 3607 (class 0 OID 16436)
-- Dependencies: 225
-- Data for Name: huesped; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.huesped (dni_huesped, nombre, apellido, edad, fecha_nacimiento, ocupacion, tipodni, mail, pos_iva, calle, numero, departamento, piso, cod_postal) FROM stdin;
25123456	María	López	49	1976-08-10	Abogada	DNI	mlopez@mail.com	Resp. Inscripto	Caseros	921	C	4	4400
43001998	Lucas	Pérez	24	2001-01-25	Estudiante	DNI	lucas.perez@mail.com	Consumidor Final	Roque Sáenz Peña	101	7B	7	3300
45456789	Javier	Altamirano	25	2000-04-05	Estudiante	DNI	javier@mail.com	Consumidor Final	Sarmiento	2342	a	3	3000
43434343	Matias	Altamirano	25	2000-03-03	Estudiante	DNI	mati@mail.com	Consumidor Final	San Martin	1612	-	0	3000
234567822	Jose	Mastrangelo	35	1990-05-03	Comerciante	DNI	jose@mail.mail	Consumidor Final	San Lorenzo	2334	a	1	2556
40987654	Ignacio	Mazzoni	22	2003-01-01	Estudiante	DNI	nachomazzoni@mail	Consumidor Final	Cordoba	46	a	1	3000
23456632	Ricardo	Beltran	17	2008-02-05	Empleado 	DNI	rick@mail.com	Monotributista	Lavaise	2212	a	1	3001
35446355	Mario	Martinez	21	2003-12-23	Estudiante	DNI	mario@mail.com	Consumidor Final	Las heras	23		0	3000
33344444	Alejo	Carter	16	2009-05-31	Basquetbolista	DNI	ac@mail.com	Consumidor Final	Roque Sáenz Peña	2332	B	7	2444
77747743	Ariel	Santos	36	1989-04-13	Cocinero	DNI	ariel@mail.com	Monotributista	Sarmiento	1232	c	5	3000
18567345	Ricardo	Giménez	55	1970-05-15	Empresario	DNI	ricardo.gim@mail.com	Resp. Inscripto	Zapiola	850	PB	3	9400
33456789	Javier	Rodríguez	38	1987-03-20	Comerciante	DNI	javiernuevo@mail.com	Consumidor Final	24 de Septiembre	700	1A	1	4000
44556533	Agusitin	Colli	22	2003-02-19	Estudiante	DNI	coli@mail.com	Consumidor Final	Alvear	2344		0	3000
9876543	mabel	mer	29	1996-06-27	Abogada	DNI	mabel@mail.com	Consumidor Final	Candioti	3322	a	5	3001
111	bautista	beltran	1	2024-03-13	Estudiante	DNI	mail@mail	Consumidor Final	calle	2443	a	1	3000
\.


--
-- TOC entry 3608 (class 0 OID 16444)
-- Dependencies: 226
-- Data for Name: juridica; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.juridica (cuit, razon_social, calle, numero, departamento, piso, cod_postal) FROM stdin;
301111111	Servicios Digitales S.A.	Corrientes	1530	B	5	1042
302222222	Consultora del Sur SRL	General Paz	345	A	2	5000
303333333	Logística Integral E.S.	Bv. Gálvez	1335	C	8	3000
\.


--
-- TOC entry 3609 (class 0 OID 16453)
-- Dependencies: 227
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
-- TOC entry 3610 (class 0 OID 16457)
-- Dependencies: 228
-- Data for Name: monedaextrajera; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.monedaextrajera (id_mediopago, tipo_moneda, valor_cambio) FROM stdin;
\.


--
-- TOC entry 3618 (class 0 OID 16526)
-- Dependencies: 236
-- Data for Name: notacredito; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.notacredito (id_notacredito, fecha, monto, motivo) FROM stdin;
\.


--
-- TOC entry 3611 (class 0 OID 16466)
-- Dependencies: 229
-- Data for Name: pago; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pago (id_pago, id_mediopago, id_factura, id_responsablepago, monto, fecha) FROM stdin;
\.


--
-- TOC entry 3612 (class 0 OID 16474)
-- Dependencies: 230
-- Data for Name: reserva; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.reserva (id_reserva, dni_huesped, cant_huesped, fecha_inicio, cant_noches, descuento, estado) FROM stdin;
2	40987654	1	2026-01-02	3	f	Confirmada
5	40987654	4	2026-01-31	6	t	Confirmada
6	18567345	3	2026-01-31	6	t	Confirmada
7	43434343	3	2026-01-01	5	t	Confirmada
8	18567345	1	2026-01-01	4	f	Confirmada
9	25123456	3	2026-01-03	7	t	Confirmada
10	25123456	1	2026-01-02	2	f	Confirmada
11	25123456	1	2026-03-02	6	f	Reservada
12	33456789	1	2026-07-01	8	f	Confirmada
13	40987654	3	2026-01-01	1	t	Ocupada
1	45456789	1	2025-12-01	21	f	Ocupada
3	40987654	1	2026-01-02	5	f	Ocupada
14	43001998	1	2026-01-07	5	f	Confirmada
4	234567822	1	2026-01-02	33	f	Ocupada
\.


--
-- TOC entry 3613 (class 0 OID 16482)
-- Dependencies: 231
-- Data for Name: responsablepago; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.responsablepago (id_responsablepago, cuit, razon_social) FROM stdin;
1	301111111	Servicios Digitales S.A.
2	302222222	Consultora del Sur SRL
3	303333333	Logística Integral E.S.
\.


--
-- TOC entry 3614 (class 0 OID 16489)
-- Dependencies: 232
-- Data for Name: telefono; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.telefono (dni, telefono, id_telefono) FROM stdin;
77747743	+ 54 7 434342111	3
45456789	-	4
43001998	-	5
18567345	-	6
33456789	-	7
44556533	+ 54 9 342 553420	8
9876543	+ 54 7766 243322	9
111	+ 343433223243	10
\.


--
-- TOC entry 3615 (class 0 OID 16493)
-- Dependencies: 233
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
-- TOC entry 3616 (class 0 OID 16498)
-- Dependencies: 234
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuario (nombre, psw) FROM stdin;
conserje	admin123
nacho	12345678
\.


--
-- TOC entry 3642 (class 0 OID 0)
-- Dependencies: 245
-- Name: detalle_estadia_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.detalle_estadia_id_seq', 20, true);


--
-- TOC entry 3643 (class 0 OID 0)
-- Dependencies: 242
-- Name: detalle_notacredito_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.detalle_notacredito_id_seq', 1, false);


--
-- TOC entry 3644 (class 0 OID 0)
-- Dependencies: 239
-- Name: detalle_reserva_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.detalle_reserva_id_seq', 14, true);


--
-- TOC entry 3645 (class 0 OID 0)
-- Dependencies: 244
-- Name: estadia_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.estadia_id_seq', 8, true);


--
-- TOC entry 3646 (class 0 OID 0)
-- Dependencies: 241
-- Name: factura_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.factura_id_seq', 1, false);


--
-- TOC entry 3647 (class 0 OID 0)
-- Dependencies: 240
-- Name: nota_credito_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.nota_credito_id_seq', 1, false);


--
-- TOC entry 3648 (class 0 OID 0)
-- Dependencies: 238
-- Name: reserva_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.reserva_id_seq', 14, true);


--
-- TOC entry 3649 (class 0 OID 0)
-- Dependencies: 246
-- Name: telefono_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.telefono_id_seq', 10, true);


-- Completed on 2025-12-09 15:19:53 UTC

--
-- PostgreSQL database dump complete
--

\unrestrict hdw7weSHhfWgWebAaO5uNGJtHDab25oUFuq2v21ON3jUdhIYTRJ29o7PYLLYGCX