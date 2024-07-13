--
-- PostgreSQL database dump
--

-- Dumped from database version 16.3 (Ubuntu 16.3-0ubuntu0.24.04.1)
-- Dumped by pg_dump version 16.3 (Ubuntu 16.3-0ubuntu0.24.04.1)

-- Started on 2024-06-22 18:53:58 CEST

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


ALTER DATABASE sitpass_db OWNER TO postgres;

\connect sitpass_db

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 216 (class 1259 OID 18068)
-- Name: account_request; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.account_request (
    id bigint NOT NULL,
    address character varying(255) NOT NULL,
    created_at date NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    rejection_reason character varying(255),
    request_status character varying(255) NOT NULL,
    CONSTRAINT account_request_request_status_check CHECK (((request_status)::text = ANY ((ARRAY['PENDING'::character varying, 'ACCEPTED'::character varying, 'REJECTED'::character varying])::text[])))
);


ALTER TABLE public.account_request OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 18067)
-- Name: account_request_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.account_request_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.account_request_id_seq OWNER TO postgres;

--
-- TOC entry 3579 (class 0 OID 0)
-- Dependencies: 215
-- Name: account_request_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.account_request_id_seq OWNED BY public.account_request.id;


--
-- TOC entry 235 (class 1259 OID 18141)
-- Name: user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."user" (
    id bigint NOT NULL,
    address character varying(255) NOT NULL,
    birthday date,
    city character varying(255),
    created_at date NOT NULL,
    email character varying(255) NOT NULL,
    is_deleted boolean,
    name character varying(255),
    password character varying(255) NOT NULL,
    phone_number character varying(255),
    surname character varying(255),
    zip_code character varying(255)
);


ALTER TABLE public."user" OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 18140)
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_id_seq OWNER TO postgres;

--
-- TOC entry 3589 (class 0 OID 0)
-- Dependencies: 234
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_id_seq OWNED BY public."user".id;


--
-- TOC entry 237 (class 1259 OID 18150)
-- Name: work_day; Type: TABLE; Schema: public; Owner: postgres
--


--
-- TOC entry 217 (class 1259 OID 18077)
-- Name: administrator; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.administrator (
    id bigint NOT NULL
);


ALTER TABLE public.administrator OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 26471)
-- Name: audit_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.audit_log (
    id bigint NOT NULL,
    action character varying(255),
    created_at timestamp(6) without time zone,
    entity_id bigint,
    error_message character varying(255),
    success boolean NOT NULL,
    username character varying(255)
);


ALTER TABLE public.audit_log OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 26470)
-- Name: audit_log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.audit_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.audit_log_id_seq OWNER TO postgres;

--
-- TOC entry 3580 (class 0 OID 0)
-- Dependencies: 238
-- Name: audit_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.audit_log_id_seq OWNED BY public.audit_log.id;


--
-- TOC entry 219 (class 1259 OID 18083)
-- Name: comment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comment (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    is_deleted boolean,
    text character varying(255) NOT NULL,
    parent_comment_id bigint,
    user_id bigint NOT NULL
);


ALTER TABLE public.comment OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 18082)
-- Name: comment_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.comment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.comment_id_seq OWNER TO postgres;

--
-- TOC entry 3581 (class 0 OID 0)
-- Dependencies: 218
-- Name: comment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.comment_id_seq OWNED BY public.comment.id;


--
-- TOC entry 221 (class 1259 OID 18090)
-- Name: discipline; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.discipline (
    id bigint NOT NULL,
    is_deleted boolean,
    name character varying(255) NOT NULL,
    facility_id bigint
);


ALTER TABLE public.discipline OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 18089)
-- Name: discipline_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.discipline_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.discipline_id_seq OWNER TO postgres;

--
-- TOC entry 3582 (class 0 OID 0)
-- Dependencies: 220
-- Name: discipline_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.discipline_id_seq OWNED BY public.discipline.id;


--
-- TOC entry 225 (class 1259 OID 18104)
-- Name: facility; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.facility (
    id bigint NOT NULL,
    active boolean,
    address character varying(255),
    city character varying(255),
    created_at date,
    description character varying(255),
    is_deleted boolean,
    name character varying(255),
    total_rating double precision
);


ALTER TABLE public.facility OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 18103)
-- Name: facility_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.facility_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.facility_id_seq OWNER TO postgres;

--
-- TOC entry 3584 (class 0 OID 0)
-- Dependencies: 224
-- Name: facility_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.facility_id_seq OWNED BY public.facility.id;




--
-- TOC entry 223 (class 1259 OID 18097)
-- Name: exercise; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.exercise (
    id bigint NOT NULL,
    "from" timestamp(6) without time zone NOT NULL,
    is_deleted boolean,
    until timestamp(6) without time zone NOT NULL,
    facility_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.exercise OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 18096)
-- Name: exercise_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.exercise_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.exercise_id_seq OWNER TO postgres;

--
-- TOC entry 3583 (class 0 OID 0)
-- Dependencies: 222
-- Name: exercise_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.exercise_id_seq OWNED BY public.exercise.id;




--
-- TOC entry 227 (class 1259 OID 18113)
-- Name: image; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.image (
    id bigint NOT NULL,
    is_deleted boolean,
    path character varying(255),
    facility_id bigint,
    user_id bigint
);


ALTER TABLE public.image OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 18112)
-- Name: image_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.image_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.image_id_seq OWNER TO postgres;

--
-- TOC entry 3585 (class 0 OID 0)
-- Dependencies: 226
-- Name: image_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.image_id_seq OWNED BY public.image.id;


--
-- TOC entry 229 (class 1259 OID 18120)
-- Name: manages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.manages (
    id bigint NOT NULL,
    end_date date,
    is_deleted boolean,
    start_date date,
    facility_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.manages OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 18119)
-- Name: manages_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.manages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.manages_id_seq OWNER TO postgres;

--
-- TOC entry 3586 (class 0 OID 0)
-- Dependencies: 228
-- Name: manages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.manages_id_seq OWNED BY public.manages.id;


--
-- TOC entry 231 (class 1259 OID 18127)
-- Name: rate; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rate (
    id bigint NOT NULL,
    equipment integer,
    hygene integer,
    is_deleted boolean,
    space integer,
    staff integer
);


ALTER TABLE public.rate OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 18126)
-- Name: rate_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.rate_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.rate_id_seq OWNER TO postgres;

--
-- TOC entry 3587 (class 0 OID 0)
-- Dependencies: 230
-- Name: rate_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rate_id_seq OWNED BY public.rate.id;


--
-- TOC entry 233 (class 1259 OID 18134)
-- Name: review; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.review (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone,
    exercise_count integer,
    hidden boolean,
    is_deleted boolean,
    comment_id bigint,
    facility_id bigint NOT NULL,
    rate_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.review OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 18133)
-- Name: review_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.review_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.review_id_seq OWNER TO postgres;

--
-- TOC entry 3588 (class 0 OID 0)
-- Dependencies: 232
-- Name: review_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.review_id_seq OWNED BY public.review.id;




CREATE TABLE public.work_day (
    id bigint NOT NULL,
    day character varying(255) NOT NULL,
    "from" time(6) without time zone NOT NULL,
    is_deleted boolean,
    until time(6) without time zone NOT NULL,
    valid_from date,
    facility_id bigint,
    CONSTRAINT work_day_day_check CHECK (((day)::text = ANY ((ARRAY['MONDAY'::character varying, 'TUESDAY'::character varying, 'WEDNESDAY'::character varying, 'THURSDAY'::character varying, 'FRIDAY'::character varying, 'SATURDAY'::character varying, 'SUNDAY'::character varying])::text[])))
);


ALTER TABLE public.work_day OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 18149)
-- Name: work_day_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.work_day_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.work_day_id_seq OWNER TO postgres;

--
-- TOC entry 3590 (class 0 OID 0)
-- Dependencies: 236
-- Name: work_day_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.work_day_id_seq OWNED BY public.work_day.id;


--
-- TOC entry 3348 (class 2604 OID 18071)
-- Name: account_request id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.account_request ALTER COLUMN id SET DEFAULT nextval('public.account_request_id_seq'::regclass);


--
-- TOC entry 3359 (class 2604 OID 26474)
-- Name: audit_log id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audit_log ALTER COLUMN id SET DEFAULT nextval('public.audit_log_id_seq'::regclass);


--
-- TOC entry 3349 (class 2604 OID 18086)
-- Name: comment id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comment ALTER COLUMN id SET DEFAULT nextval('public.comment_id_seq'::regclass);


--
-- TOC entry 3350 (class 2604 OID 18093)
-- Name: discipline id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discipline ALTER COLUMN id SET DEFAULT nextval('public.discipline_id_seq'::regclass);


--
-- TOC entry 3351 (class 2604 OID 18100)
-- Name: exercise id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exercise ALTER COLUMN id SET DEFAULT nextval('public.exercise_id_seq'::regclass);


--
-- TOC entry 3352 (class 2604 OID 18107)
-- Name: facility id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.facility ALTER COLUMN id SET DEFAULT nextval('public.facility_id_seq'::regclass);


--
-- TOC entry 3353 (class 2604 OID 18116)
-- Name: image id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.image ALTER COLUMN id SET DEFAULT nextval('public.image_id_seq'::regclass);


--
-- TOC entry 3354 (class 2604 OID 18123)
-- Name: manages id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.manages ALTER COLUMN id SET DEFAULT nextval('public.manages_id_seq'::regclass);


--
-- TOC entry 3355 (class 2604 OID 18130)
-- Name: rate id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rate ALTER COLUMN id SET DEFAULT nextval('public.rate_id_seq'::regclass);


--
-- TOC entry 3356 (class 2604 OID 18137)
-- Name: review id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.review ALTER COLUMN id SET DEFAULT nextval('public.review_id_seq'::regclass);


--
-- TOC entry 3357 (class 2604 OID 18144)
-- Name: user id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user" ALTER COLUMN id SET DEFAULT nextval('public.user_id_seq'::regclass);


--
-- TOC entry 3358 (class 2604 OID 18153)
-- Name: work_day id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.work_day ALTER COLUMN id SET DEFAULT nextval('public.work_day_id_seq'::regclass);


--
-- TOC entry 3549 (class 0 OID 18068)
-- Dependencies: 216
-- Data for Name: account_request; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.account_request (id, address, created_at, email, password, rejection_reason, request_status) FROM stdin;
1	Vojvode Stepe 22	2024-05-25	admin@gmail.com	password	\N	ACCEPTED
2	Vojvode Stepe 22	2024-05-25	manager@gmail.com	password	\N	ACCEPTED
3	Vojvode Mise 22	2024-05-25	user@gmail.com	password	\N	ACCEPTED
4	Vojvode Mise 22	2024-05-25	user2@gmail.com	password	\N	ACCEPTED
5	Vojvode Mise 22	2024-05-25	user3@gmail.com	password	\N	ACCEPTED
6	Vojvode Mise 22	2024-05-25	user4@gmail.com	password	\N	ACCEPTED
7	Vojvode Mise 22	2024-05-25	user5@gmail.com	password	\N	ACCEPTED
9	Dundova 22	2024-05-25	manager2@gmail.com	password	\N	ACCEPTED
10	Brcanska 1	2024-05-25	manager3@gmail.com	password	\N	ACCEPTED
11	Gredice BB	2024-05-27	arsa@gmail.com	password	\N	ACCEPTED
12	Dragise Brasanova 14	2024-06-08	user_ns@gmail.com	password	\N	ACCEPTED
13	Bulevar 41	2024-06-08	user_ns2@gmail.com	password	\N	ACCEPTED
14	Svetog Save 2A	2024-06-08	user_ns3@gmail.com	password	\N	ACCEPTED
8	Ulica Vojvode Stepe 22	2024-05-25	user6@gmail.com	password	\N	ACCEPTED
\.



--
-- TOC entry 3568 (class 0 OID 18141)
-- Dependencies: 235
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."user" (id, address, birthday, city, created_at, email, is_deleted, name, password, phone_number, surname, zip_code) FROM stdin;
3	Vojvode Mise 22	2002-02-02	Novi Sad	2024-05-25	user@gmail.com	\N	User	$2a$10$lqQ1PqY9c.BT7QQ9zHMCneDXLJLE8iIszLm14CQQGgaAULDUOgfOW	065510000	User	21001
4	Vojvode Mise 22	1999-09-09	Beograd	2024-05-25	user2@gmail.com	\N	Svetozar	$2a$10$PMGasGbfQhNqBB0ytYkwC.wdj3qEotCsqqsxQ53qlwz8g993JM1ga	065441222	Messi	104104
5	Vojvode Mise 22	2002-02-02	Brcko	2024-05-25	user3@gmail.com	\N	Mikica	$2a$10$Ua0ryRdjoTzG4FeP2xGC6ObrTXsfT8nQ1aNi3zfMYttphhCm3wqvq	066771222	Ronaldinho	76100
8	Dundova 22	2000-01-01	Novi Sad	2024-05-25	manager2@gmail.com	\N	Managerko	$2a$10$9GjttGeUMgWbJmM/SWn0uOm28/UqtDS.226onSjrr4ERk5gWkyZvi	065781234	Managerkovic	21001
9	Brigada 22	1991-01-01	Beograd	2024-05-25	manager3@gmail.com	\N	Vido	$2a$10$btj96DQC/gdzmESG1Bgm2epinpdqG3r0TrfQayvhhN.t9DgyQkonC	066809132	Mido	101801
6	Vojvode Mise 22	2002-02-02	Novi Sad	2024-05-25	user4@gmail.com	\N	Wilfried	$2a$10$BOfGz1ZtuUmJTO6M.JI/E.AoxRZFaBCwjPLaZd9H/HzeM/G.Z24lG	066666660	Saha	21001
7	Vojvode Mise 22	2000-01-01	Novi Sad	2024-05-25	user5@gmail.com	\N	Miso	$2a$10$wk8jDuk6tJxJadDpbZCC3usbgpeIfc4SIOUpGn0NYrAdLES9Z9kOW	066555005	Macak	21001
2	Vojvode Stepe 22	1991-01-01	Novi Sad	2024-05-25	manager@gmail.com	\N	Manager	$2a$10$02M/HBz.mIXUtOJj1z/kQuQQjh7PGBl6utv29I5OEWYnH5KQXI7iO	066000007	Manager	76100
10	Gredice BB	\N	\N	2024-05-27	arsa@gmail.com	\N		$2a$10$AT1/aZekvCmFl8murugsaebmBn2LXcD8n.nYjgZob2H/nVWqU7L3m	066666006		\N
12	Bulevar 41	\N	\N	2024-06-08	user_ns2@gmail.com	\N	\N	$2a$10$JHbRCf3jfyuVbHI3OX1Dzu1BAw4a9NRbZ3oD5b.KYl8PHePkyAxXa	\N	\N	\N
13	Svetog Save 2A	\N	Novi Sad	2024-06-08	user_ns3@gmail.com	\N	Neko	$2a$10$BZUOV/QiRcgQ1cOEwynM..YvYSqiHSsiJRUUjxWJsNmEeejHCMdOS	066123441	Novi	21001
11	Dragise Brasanova 14	\N	Novi Sad	2024-06-08	user_ns@gmail.com	\N	Mirko	$2a$10$AgRS1VtILfFGdgF5/ojaw.3yLrk4tBt1uw0HAApJYB5Bixeu7d4oO	066234432	Mirkovic	21001
14	Ulica Vojvode Stepe 22	\N	\N	2024-06-12	user6@gmail.com	\N	\N	$2a$10$TWhqHZKO2gvnC92goD1KwuA9lYKEptWSMuXw9NE4SFxkzl7mtSiy.	\N	\N	\N
1	Vojvode Stepe 22	2000-02-02	Novi Sad	2024-05-25	admin@gmail.com	\N	Admin	$2a$10$tnoaEgUpIZrJJfxFpYi8MO3E7FOVDDXzyKxfQR5eaOv/NLqHScsS6	066000001	Admin	21001
\.


--
-- TOC entry 3550 (class 0 OID 18077)
-- Dependencies: 217
-- Data for Name: administrator; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.administrator (id) FROM stdin;
1
\.


--
-- TOC entry 3572 (class 0 OID 26471)
-- Dependencies: 239
-- Data for Name: audit_log; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.audit_log (id, action, created_at, entity_id, error_message, success, username) FROM stdin;
1	DELETE	2024-06-21 15:05:07.498903	26	\N	t	admin@gmail.com
\.


--
-- TOC entry 3552 (class 0 OID 18083)
-- Dependencies: 219
-- Data for Name: comment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.comment (id, created_at, is_deleted, text, parent_comment_id, user_id) FROM stdin;
1	2024-05-25 21:55:13.889713	\N	Everything perfect! You need one more WC and hygene would be 10!	\N	3
2	2024-05-25 21:56:43.143722	\N	Not too much space to train and need more equipment.	\N	3
3	2024-05-25 21:57:35.027492	\N	PERFECT GYM!	\N	3
4	2024-05-25 21:58:28.416218	\N	BEST GYM I EVER VISITED	\N	3
5	2024-05-25 22:01:21.838298	\N	No comment. Only equipment was good.	\N	5
6	2024-05-25 22:05:58.838306	\N	WOW!	\N	7
8	2024-05-25 22:21:52.701718	\N	Yes, as a manager I totally agree! :)	4	2
9	2024-05-25 22:34:04.38016	\N	COOL	\N	7
10	2024-05-26 12:07:11.3527	\N	It's ok.	\N	3
11	2024-05-26 12:08:01.76034	\N	Good gym!	\N	3
12	2024-05-29 19:43:18.22626	\N	ok?	1	1
13	2024-06-10 16:24:09.23142	\N	dasd	\N	11
14	2024-06-12 09:06:39.929969	\N	Not bad training	\N	11
15	2024-06-12 11:15:10.874238	\N	Aaskdaskdask	13	1
16	2024-06-13 10:02:46.318327	\N	Reply	15	11
\.


--
-- TOC entry 3554 (class 0 OID 18090)
-- Dependencies: 221
-- Data for Name: discipline; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.discipline (id, is_deleted, name, facility_id) FROM stdin;
1	\N	Box	1
4	\N	Running	3
5	\N	Yoga	3
6	\N	Boxing	4
7	\N	Swimming	4
8	\N	Running	5
9	\N	Jumping	5
10	\N	Cross-Fit	5
11	\N	Kick-Boxing	6
12	\N	Krav Maga	6
13	\N	Boxing	7
14	\N	Strength	7
15	\N	Street Workout	7
16	\N	Strength	8
17	\N	Aerobics	8
18	\N	Swimming	9
19	\N	Strength	10
20	\N	Street Workout	10
21	\N	Running	11
22	\N	Yoga	11
23	\N	Strength	12
24	\N	Running	13
25	\N	Yoga	13
26	\N	Judo	14
27	\N	Boxing	15
28	\N	Running	15
29	\N	Krav Maga	16
30	\N	Swimming	17
31	\N	Football	17
32	\N	Basketball	17
33	\N	Table Tennis	17
34	\N	Street Workout	18
35	\N	Strength	18
36	\N	Cardio	18
37	\N	Jiu-Jitsu	19
38	\N	Cross-Fit	20
39	\N	Strength	20
40	\N	Yoga	20
41	\N	Boxing	21
42	\N	Kick-Boxing	21
47	\N	Brazilian JJ	25
48	t	Neka	26
\.



--
-- TOC entry 3558 (class 0 OID 18104)
-- Dependencies: 225
-- Data for Name: facility; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.facility (id, active, address, city, created_at, description, is_deleted, name, total_rating) FROM stdin;
16	t	Jevrejska 2	Novi Sad	2024-05-29	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Express Gym	\N
20	t	Bulevar 92	Novi Sad	2024-05-29	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Ultra Flex Gym	9.833333333333334
15	t	Vojvode Stepe 86	Novi Sad	2024-05-29	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	ATP Gym	\N
21	t	Bulevar Patrijarha Pavla 8A	Novi Sad	2024-05-29	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Super Gym	\N
25	t	Bulevar 12345	Novi Sad	2024-06-12	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	WONDERFUL GYM	8.75
1	t	Dragise Brasanova 12	Novi Sad	2024-05-25	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Best Practices	9.083333333333334
3	t	Address	Novi Sad	2024-05-25	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Silver Gym	9
4	t	Dragise Brasanova 123	Novi Sad	2024-05-25	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Black Gym	9.125
5	t	Bulevar 1 	Novi Sad	2024-05-25	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Calvin Gym	10
6	t	Bulevar 45	Novi Sad	2024-05-25	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Effective Gym	9.25
7	t	Ulica Vojvode Stepe 22	Novi Sad	2024-05-25	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Green Gym	9.625
8	f	Dragise Brasanova 1	Novi Sad	2024-05-25	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Home Gym	\N
9	f	Bulevar 4	Novi Sad	2024-05-25	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Old School Gym	\N
10	t	Gredice BB	Brcko	2024-05-25	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Orange Gym	5.75
11	t	Stolin	Brcko	2024-05-25	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Pink Gym	10
12	f	Gredice BB	Brcko	2024-05-25	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Pitbull Gym	\N
13	t	Svetog Save 2A	Beograd	2024-05-25	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Purple Gym	8.875
14	t	Bulevar 4	Beograd	2024-05-25	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Yellow Gym	10
19	t	Novosadska 22	Novi Sad	2024-05-29	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Inicio Gym	\N
18	t	Detelinara 4	Novi Sad	2024-05-29	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Hussle Gym	\N
17	t	Ulica Narodnog Fronta 22	Novi Sad	2024-05-29	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	\N	Holiday Gym	\N
26	f	Bulevar 12312312	Novi Sad	2024-06-12	Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget velit turpis. Sed quis egestas est, non egestas lectus. Sed nulla tellus, varius vitae congue non, sagittis vitae lacus.	t	New Gym	\N
\.



--
-- TOC entry 3556 (class 0 OID 18097)
-- Dependencies: 223
-- Data for Name: exercise; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.exercise (id, "from", is_deleted, until, facility_id, user_id) FROM stdin;
1	2024-05-06 19:50:00	\N	2024-05-06 21:00:00	1	3
2	2024-05-08 15:45:00	\N	2024-05-08 17:00:00	4	3
3	2024-05-17 17:50:00	\N	2024-05-17 18:55:00	6	3
4	2024-05-06 00:00:00	\N	2024-05-06 00:45:00	7	3
5	2024-05-06 17:40:00	\N	2024-05-06 20:59:00	13	4
6	2024-05-01 17:00:00	\N	2024-05-01 20:00:00	10	5
7	2024-05-13 14:00:00	\N	2024-05-13 15:05:00	7	6
8	2024-05-14 17:05:00	\N	2024-05-14 19:05:00	7	7
9	2024-04-15 16:25:00	\N	2024-04-15 17:50:00	3	3
10	2024-05-16 10:27:00	\N	2024-05-16 10:27:00	5	3
11	2024-04-29 10:28:00	\N	2024-04-29 11:28:00	1	3
12	2024-05-24 10:28:00	\N	2024-05-24 10:59:00	4	3
13	2024-03-11 08:29:00	\N	2024-03-11 08:59:00	6	3
14	2024-05-18 10:29:00	\N	2024-05-18 11:29:00	7	3
15	2024-05-03 18:30:00	\N	2024-05-03 18:59:00	3	3
16	2024-05-06 17:30:00	\N	2024-05-06 19:30:00	14	4
17	2024-02-05 19:31:00	\N	2024-02-05 20:40:00	13	4
18	2023-12-25 17:30:00	\N	2023-12-25 17:30:00	5	7
19	2024-05-21 14:30:00	\N	2024-05-21 14:30:00	5	7
20	2024-05-24 14:34:00	\N	2024-05-24 14:45:00	6	7
21	2024-05-07 10:35:00	\N	2024-05-07 11:45:00	7	7
22	2024-05-09 10:35:00	\N	2024-05-09 11:25:00	7	7
23	2024-05-24 07:15:00	\N	2024-05-24 08:15:00	3	7
24	2024-05-09 10:36:00	\N	2024-05-09 11:45:00	1	7
25	2024-05-13 12:06:00	\N	2024-05-13 13:06:00	6	3
26	2024-05-15 12:06:00	\N	2024-05-15 14:06:00	6	3
27	2024-05-08 12:07:00	\N	2024-05-08 13:07:00	1	3
28	2024-05-03 12:07:00	\N	2024-05-03 14:07:00	1	3
29	2024-05-13 12:07:00	\N	2024-05-13 14:07:00	1	3
30	2024-05-03 12:08:00	\N	2024-05-03 15:08:00	4	3
31	2024-05-10 12:08:00	\N	2024-05-10 13:08:00	4	3
32	2024-05-01 12:08:00	\N	2024-05-01 13:08:00	4	3
33	2024-05-08 12:08:00	\N	2024-05-08 14:08:00	4	3
34	2024-05-19 12:09:00	\N	2024-05-19 13:09:00	7	3
41	2024-05-27 21:36:00	\N	2024-05-27 22:36:00	11	1
42	2024-05-17 12:36:00	\N	2024-05-17 13:36:00	11	1
43	2024-05-29 18:27:00	\N	2024-05-29 19:27:00	20	1
44	2024-06-01 18:14:00	\N	2024-06-01 19:14:00	20	1
45	2024-06-02 20:14:00	\N	2024-06-02 21:14:00	20	1
46	2024-06-02 18:14:00	\N	2024-06-02 20:14:00	20	1
47	2024-06-03 13:14:00	\N	2024-06-03 14:14:00	20	1
48	2024-06-05 09:14:00	\N	2024-06-05 11:15:00	20	1
49	2024-06-07 18:15:00	\N	2024-06-07 19:15:00	20	1
50	2024-06-01 16:16:00	\N	2024-06-01 18:16:00	20	3
51	2024-06-02 16:16:00	\N	2024-06-02 17:16:00	20	3
52	2024-06-03 18:16:00	\N	2024-06-03 21:16:00	20	3
53	2024-06-06 19:16:00	\N	2024-06-06 20:16:00	20	3
54	2024-06-07 11:01:00	\N	2024-06-07 11:57:00	20	1
55	2024-06-07 11:19:00	\N	2024-06-07 11:54:00	20	1
56	2024-05-08 16:12:00	\N	2024-05-08 18:12:00	20	1
57	2024-05-08 12:15:00	\N	2024-05-08 13:15:00	20	11
58	2024-06-01 12:16:00	\N	2024-06-01 13:16:00	20	12
59	2024-03-06 12:16:00	\N	2024-03-06 14:16:00	20	13
60	2024-06-07 16:15:00	\N	2024-06-07 17:16:00	20	1
61	2024-06-08 17:46:00	\N	2024-06-08 18:46:00	7	1
62	2024-06-08 16:16:00	\N	2024-06-08 18:16:00	20	1
63	2024-06-10 16:23:00	\N	2024-06-10 18:23:00	20	1
64	2024-06-12 07:41:00	\N	2024-06-12 08:41:00	20	1
65	2024-06-10 08:06:00	\N	2024-06-10 10:06:00	25	11
\.


--
-- TOC entry 3560 (class 0 OID 18113)
-- Dependencies: 227
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.image (id, is_deleted, path, facility_id, user_id) FROM stdin;
1	\N	me.jpeg	\N	1
2	\N	manager.jpg	\N	2
3	\N	user.jpg	\N	3
4	\N	user2.jpg	\N	4
5	\N	user3.jpg	\N	5
6	\N	best_practices_gym1-min.jpg	1	\N
56	t	manager2-min.jpeg	\N	8
64	t	manager2-min.jpeg	\N	8
65	t	orange_gym1-min.jpg	\N	8
66	\N	manager2-min.jpeg	\N	8
67	\N	manager3-min.jpg	\N	9
14	\N	best_practices_gym2-min.jpg	1	\N
68	t	arsa.png	\N	10
69	\N	arsa.png	\N	10
17	\N	silver_gym1-min.jpg	3	\N
18	\N	black_gym1-min.jpg	4	\N
19	\N	black_gym2-min.jpg	4	\N
20	\N	black_gym3-min.jpg	4	\N
21	\N	calvin_gym1-min.jpeg	5	\N
70	\N	atp_gym1-min.jpg	15	\N
71	\N	atp_gym2-min.jpg	15	\N
25	\N	calvin_gym2-min.jpeg	5	\N
74	\N	express_gym1-min.jpeg	16	\N
27	\N	effective_gym1-min.jpg	6	\N
75	\N	express_gym2-min.jpeg	16	\N
78	\N	ultra_flex_gym1-min.jpg	20	\N
79	\N	ultra_flex_gym2-min.jpg	20	\N
82	\N	super_gym1-min.jpg	21	\N
36	\N	home_gym1-min.jpg	8	\N
37	\N	old_school_gym1-min.jpeg	9	\N
83	\N	super_gym2-min.jpg	21	\N
39	\N	pink_gym1-min.jpg	11	\N
85	\N	inicio_gym1-min.jpeg	19	\N
43	\N	orange_gym1-min.jpg	10	\N
44	\N	orange_gym2-min.jpg	10	\N
45	\N	orange_gym3-min.jpg	10	\N
87	\N	hussle_gym1-min.jpg	18	\N
47	\N	pitbull_gym1-min.jpg	12	\N
89	\N	holiday_gym1-min.jpg	17	\N
50	\N	purple_gym1-min.jpg	13	\N
51	\N	purple_gym2-min.jpg	13	\N
53	\N	yellow_gym2-min.jpg	14	\N
55	\N	yellow_gym3-min.jpg	14	\N
52	\N	yellow_gym1-min.jpg	14	\N
93	\N	green_gym1-min.jpg	7	\N
94	\N	green_gym2-min.jpg	7	\N
95	\N	green_gym3-min.jpg	7	\N
104	\N	wonderful_gym.jpg	25	\N
105	t	atp_gym1-min.jpg	26	\N
106	t	atp_gym2-min.jpg	26	\N
\.


--
-- TOC entry 3562 (class 0 OID 18120)
-- Dependencies: 229
-- Data for Name: manages; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.manages (id, end_date, is_deleted, start_date, facility_id, user_id) FROM stdin;
1	2030-01-01	\N	2024-01-01	1	2
2	2025-01-01	\N	2024-01-01	3	2
3	2030-01-01	\N	2024-01-01	4	2
4	2030-01-01	\N	2024-01-01	5	2
5	2030-01-01	\N	2024-01-01	6	2
6	2030-01-01	\N	2024-01-01	7	2
7	2030-01-01	\N	2024-01-01	10	8
8	2030-01-01	\N	2024-01-01	11	8
9	2030-01-01	\N	2024-01-01	13	9
10	2030-01-01	\N	2024-01-01	14	9
11	2030-01-01	\N	2024-01-01	1	1
12	2030-01-01	\N	2024-01-01	15	2
13	2030-01-01	\N	2024-01-01	20	2
14	2030-01-01	\N	2024-01-01	21	2
15	2030-01-01	\N	2024-01-01	19	2
16	2030-01-01	\N	2024-01-01	18	1
17	2030-01-01	\N	2024-01-01	17	1
18	2030-01-01	\N	2024-01-01	16	1
19	2030-01-01	\N	2024-01-01	20	1
21	2030-01-01	t	2024-01-01	25	1
22	2030-01-01	\N	2024-01-01	25	2
23	2028-01-01	t	2022-01-01	25	5
\.


--
-- TOC entry 3564 (class 0 OID 18127)
-- Dependencies: 231
-- Data for Name: rate; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rate (id, equipment, hygene, is_deleted, space, staff) FROM stdin;
1	10	9	\N	10	10
2	5	10	\N	5	10
3	10	10	\N	10	10
4	10	10	\N	10	10
5	10	10	\N	10	10
6	10	4	\N	4	5
7	10	10	\N	10	10
8	10	10	\N	10	10
9	8	10	\N	10	10
10	10	10	\N	10	4
11	10	10	\N	10	10
12	10	10	\N	10	10
13	10	10	\N	10	10
14	10	10	\N	10	1
15	10	10	\N	10	10
16	10	7	\N	10	8
17	7	10	\N	7	10
18	10	10	\N	6	8
19	10	10	\N	6	10
20	10	10	\N	8	10
21	10	8	\N	10	10
22	9	10	\N	8	10
23	9	10	\N	10	10
24	10	10	\N	10	10
25	10	10	\N	10	10
26	10	10	\N	8	10
27	10	10	\N	10	10
28	10	10	\N	10	10
29	8	10	\N	7	10
\.


--
-- TOC entry 3566 (class 0 OID 18134)
-- Dependencies: 233
-- Data for Name: review; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.review (id, created_at, exercise_count, hidden, is_deleted, comment_id, facility_id, rate_id, user_id) FROM stdin;
1	2024-05-25 21:55:13.737788	1	\N	\N	1	1	1	3
2	2024-05-25 21:56:43.089394	1	\N	\N	2	4	2	3
3	2024-05-25 21:57:34.977834	1	\N	\N	3	6	3	3
4	2024-05-25 21:58:28.361465	1	\N	\N	4	7	4	3
5	2024-05-25 22:00:03.061568	1	\N	\N	\N	13	5	4
6	2024-05-25 22:01:21.790717	1	\N	\N	5	10	6	5
7	2024-05-25 22:04:38.582979	1	\N	\N	\N	7	7	6
9	2024-05-25 22:23:17.328025	1	\N	\N	\N	3	9	3
10	2024-05-25 22:28:12.59634	2	\N	\N	\N	1	10	3
11	2024-05-25 22:29:09.282857	2	\N	\N	\N	4	11	3
12	2024-05-25 22:30:10.223886	2	\N	\N	\N	7	12	3
13	2024-05-25 22:31:46.917595	1	\N	\N	\N	14	13	4
14	2024-05-25 22:32:18.982921	2	\N	\N	\N	13	14	4
15	2024-05-25 22:34:04.338623	2	\N	\N	9	5	15	7
16	2024-05-25 22:35:42.17302	3	\N	\N	\N	7	16	7
17	2024-05-25 22:36:24.685236	1	\N	\N	\N	3	17	7
18	2024-05-26 12:07:11.26558	4	\N	\N	10	6	18	3
19	2024-05-26 12:08:01.698221	5	\N	\N	11	1	19	3
20	2024-05-26 12:08:16.509569	3	\N	\N	\N	4	20	3
21	2024-05-26 12:09:04.682035	6	\N	\N	\N	4	21	3
22	2024-05-26 12:09:21.739215	3	\N	\N	\N	7	22	3
23	2024-05-26 12:09:27.308302	3	\N	\N	\N	7	23	3
24	2024-05-27 12:36:37.016826	1	\N	\N	\N	11	24	1
25	2024-05-30 18:27:40.445667	1	\N	\N	\N	20	25	1
26	2024-06-08 00:16:52.770966	1	\N	\N	\N	20	26	13
27	2024-06-10 16:24:08.91876	1	\N	\N	13	20	28	11
28	2024-06-12 09:06:39.839295	1	\N	\N	14	25	29	11
\.



--
-- TOC entry 3570 (class 0 OID 18150)
-- Dependencies: 237
-- Data for Name: work_day; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.work_day (id, day, "from", is_deleted, until, valid_from, facility_id) FROM stdin;
1	MONDAY	07:00:00	\N	19:00:00	2024-05-25	1
2	TUESDAY	07:00:00	\N	19:00:00	2024-05-25	1
3	WEDNESDAY	09:00:00	\N	21:00:00	2024-05-25	1
4	THURSDAY	09:00:00	\N	21:00:00	2024-05-25	1
5	FRIDAY	09:00:00	\N	21:00:00	2024-05-25	1
48	MONDAY	08:00:00	\N	20:00:00	2024-05-25	12
49	TUESDAY	08:00:00	\N	20:00:00	2024-05-25	12
50	WEDNESDAY	08:00:00	\N	20:00:00	2024-05-25	12
51	THURSDAY	08:00:00	\N	20:00:00	2024-05-25	12
52	FRIDAY	08:00:00	\N	20:00:00	2024-05-25	12
11	MONDAY	07:00:00	\N	19:00:00	2024-05-25	3
12	TUESDAY	07:00:00	\N	19:00:00	2024-05-25	3
13	WEDNESDAY	07:00:00	\N	19:00:00	2024-05-25	3
14	THURSDAY	07:00:00	\N	19:00:00	2024-05-25	3
15	FRIDAY	09:00:00	\N	21:00:00	2024-05-25	3
16	MONDAY	07:00:00	\N	19:00:00	2024-05-25	4
17	WEDNESDAY	07:00:00	\N	19:00:00	2024-05-25	4
18	FRIDAY	07:00:00	\N	19:00:00	2024-05-25	4
19	MONDAY	07:00:00	\N	19:00:00	2024-05-25	5
20	TUESDAY	07:00:00	\N	19:00:00	2024-05-25	5
21	WEDNESDAY	07:00:00	\N	19:00:00	2024-05-25	5
22	THURSDAY	07:00:00	\N	19:00:00	2024-05-25	5
23	FRIDAY	07:00:00	\N	19:00:00	2024-05-25	5
24	MONDAY	07:00:00	\N	19:00:00	2024-05-25	6
25	WEDNESDAY	07:00:00	\N	19:00:00	2024-05-25	6
26	FRIDAY	07:00:00	\N	19:00:00	2024-05-25	6
27	MONDAY	07:00:00	\N	19:00:00	2024-05-25	7
28	TUESDAY	07:00:00	\N	19:00:00	2024-05-25	7
29	WEDNESDAY	07:00:00	\N	19:00:00	2024-05-25	7
30	THURSDAY	07:00:00	\N	19:00:00	2024-05-25	7
31	FRIDAY	07:00:00	\N	19:00:00	2024-05-25	7
32	SATURDAY	07:00:00	\N	19:00:00	2024-05-25	7
33	MONDAY	08:00:00	\N	20:00:00	2024-05-25	8
34	WEDNESDAY	08:00:00	\N	20:00:00	2024-05-25	8
35	THURSDAY	08:00:00	\N	20:00:00	2024-05-25	8
36	SATURDAY	08:00:00	\N	20:00:00	2024-05-25	8
37	MONDAY	08:00:00	\N	20:00:00	2024-05-25	9
38	MONDAY	09:00:00	\N	21:00:00	2024-05-25	10
39	TUESDAY	09:00:00	\N	21:00:00	2024-05-25	10
40	WEDNESDAY	09:00:00	\N	21:00:00	2024-05-25	10
41	THURSDAY	09:00:00	\N	21:00:00	2024-05-25	10
42	FRIDAY	09:00:00	\N	21:00:00	2024-05-25	10
43	MONDAY	09:00:00	\N	21:00:00	2024-05-25	11
44	TUESDAY	09:00:00	\N	21:00:00	2024-05-25	11
45	WEDNESDAY	09:00:00	\N	21:00:00	2024-05-25	11
46	THURSDAY	09:00:00	\N	22:00:00	2024-05-25	11
47	FRIDAY	11:00:00	\N	22:00:00	2024-05-25	11
53	SATURDAY	08:00:00	\N	20:00:00	2024-05-25	12
54	MONDAY	07:00:00	\N	21:00:00	2024-05-25	13
55	FRIDAY	08:00:00	\N	20:00:00	2024-05-25	13
56	MONDAY	08:00:00	\N	20:00:00	2024-05-25	14
57	WEDNESDAY	08:00:00	\N	20:00:00	2024-05-25	14
58	FRIDAY	08:00:00	\N	20:00:00	2024-05-25	14
59	MONDAY	07:00:00	\N	21:00:00	2024-05-29	15
60	TUESDAY	07:00:00	\N	21:00:00	2024-05-29	15
61	THURSDAY	07:00:00	\N	21:00:00	2024-05-29	15
62	FRIDAY	07:00:00	\N	21:00:00	2024-05-29	15
63	MONDAY	07:00:00	\N	19:00:00	2024-05-29	16
64	WEDNESDAY	09:00:00	\N	21:00:00	2024-05-29	16
65	FRIDAY	09:00:00	\N	21:00:00	2024-05-29	16
66	WEDNESDAY	08:00:00	\N	19:00:00	2024-05-29	17
67	SATURDAY	08:00:00	\N	20:00:00	2024-05-29	17
68	MONDAY	07:00:00	\N	19:00:00	2024-05-29	18
69	TUESDAY	07:00:00	\N	19:00:00	2024-05-29	18
70	WEDNESDAY	07:00:00	\N	19:00:00	2024-05-29	18
71	THURSDAY	07:00:00	\N	19:00:00	2024-05-29	18
72	MONDAY	07:00:00	\N	19:00:00	2024-05-29	19
73	WEDNESDAY	07:00:00	\N	19:00:00	2024-05-29	19
74	FRIDAY	07:00:00	\N	19:00:00	2024-05-29	19
75	MONDAY	07:00:00	\N	19:00:00	2024-05-29	20
76	TUESDAY	07:00:00	\N	19:00:00	2024-05-29	20
77	WEDNESDAY	07:00:00	\N	19:00:00	2024-05-29	20
78	THURSDAY	07:00:00	\N	19:00:00	2024-05-29	20
79	FRIDAY	07:00:00	\N	19:00:00	2024-05-29	20
80	SATURDAY	07:00:00	\N	19:00:00	2024-05-29	20
81	SUNDAY	07:00:00	\N	19:00:00	2024-05-29	20
82	TUESDAY	07:00:00	\N	19:00:00	2024-05-29	21
83	THURSDAY	07:00:00	\N	19:00:00	2024-05-29	21
84	SATURDAY	07:00:00	\N	19:00:00	2024-05-29	21
93	THURSDAY	07:00:00	t	19:00:00	2024-06-12	26
91	MONDAY	08:00:00	\N	20:00:00	2024-06-12	25
92	WEDNESDAY	08:00:00	\N	20:00:00	2024-06-12	25
\.


--
-- TOC entry 3591 (class 0 OID 0)
-- Dependencies: 215
-- Name: account_request_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.account_request_id_seq', 14, true);


--
-- TOC entry 3592 (class 0 OID 0)
-- Dependencies: 238
-- Name: audit_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.audit_log_id_seq', 1, true);


--
-- TOC entry 3593 (class 0 OID 0)
-- Dependencies: 218
-- Name: comment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.comment_id_seq', 16, true);


--
-- TOC entry 3594 (class 0 OID 0)
-- Dependencies: 220
-- Name: discipline_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.discipline_id_seq', 48, true);


--
-- TOC entry 3595 (class 0 OID 0)
-- Dependencies: 222
-- Name: exercise_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.exercise_id_seq', 65, true);


--
-- TOC entry 3596 (class 0 OID 0)
-- Dependencies: 224
-- Name: facility_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.facility_id_seq', 26, true);


--
-- TOC entry 3597 (class 0 OID 0)
-- Dependencies: 226
-- Name: image_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.image_id_seq', 106, true);


--
-- TOC entry 3598 (class 0 OID 0)
-- Dependencies: 228
-- Name: manages_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.manages_id_seq', 23, true);


--
-- TOC entry 3599 (class 0 OID 0)
-- Dependencies: 230
-- Name: rate_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.rate_id_seq', 29, true);


--
-- TOC entry 3600 (class 0 OID 0)
-- Dependencies: 232
-- Name: review_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.review_id_seq', 28, true);


--
-- TOC entry 3601 (class 0 OID 0)
-- Dependencies: 234
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_id_seq', 14, true);


--
-- TOC entry 3602 (class 0 OID 0)
-- Dependencies: 236
-- Name: work_day_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.work_day_id_seq', 93, true);


--
-- TOC entry 3363 (class 2606 OID 18076)
-- Name: account_request account_request_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.account_request
    ADD CONSTRAINT account_request_pkey PRIMARY KEY (id);


--
-- TOC entry 3365 (class 2606 OID 18081)
-- Name: administrator administrator_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.administrator
    ADD CONSTRAINT administrator_pkey PRIMARY KEY (id);


--
-- TOC entry 3389 (class 2606 OID 26478)
-- Name: audit_log audit_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audit_log
    ADD CONSTRAINT audit_log_pkey PRIMARY KEY (id);


--
-- TOC entry 3367 (class 2606 OID 18088)
-- Name: comment comment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comment
    ADD CONSTRAINT comment_pkey PRIMARY KEY (id);


--
-- TOC entry 3369 (class 2606 OID 18095)
-- Name: discipline discipline_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discipline
    ADD CONSTRAINT discipline_pkey PRIMARY KEY (id);


--
-- TOC entry 3371 (class 2606 OID 18102)
-- Name: exercise exercise_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exercise
    ADD CONSTRAINT exercise_pkey PRIMARY KEY (id);


--
-- TOC entry 3373 (class 2606 OID 18111)
-- Name: facility facility_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.facility
    ADD CONSTRAINT facility_pkey PRIMARY KEY (id);


--
-- TOC entry 3375 (class 2606 OID 18118)
-- Name: image image_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.image
    ADD CONSTRAINT image_pkey PRIMARY KEY (id);


--
-- TOC entry 3377 (class 2606 OID 18125)
-- Name: manages manages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.manages
    ADD CONSTRAINT manages_pkey PRIMARY KEY (id);


--
-- TOC entry 3379 (class 2606 OID 18132)
-- Name: rate rate_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rate
    ADD CONSTRAINT rate_pkey PRIMARY KEY (id);


--
-- TOC entry 3381 (class 2606 OID 18139)
-- Name: review review_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.review
    ADD CONSTRAINT review_pkey PRIMARY KEY (id);


--
-- TOC entry 3383 (class 2606 OID 18158)
-- Name: review uk_jqo6yhi7i1wj5mtbdthrkk44q; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.review
    ADD CONSTRAINT uk_jqo6yhi7i1wj5mtbdthrkk44q UNIQUE (comment_id);


--
-- TOC entry 3385 (class 2606 OID 18148)
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- TOC entry 3387 (class 2606 OID 18156)
-- Name: work_day work_day_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.work_day
    ADD CONSTRAINT work_day_pkey PRIMARY KEY (id);


--
-- TOC entry 3404 (class 2606 OID 18229)
-- Name: work_day fk171y75uohwhvaxg3ypb6o0w7q; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.work_day
    ADD CONSTRAINT fk171y75uohwhvaxg3ypb6o0w7q FOREIGN KEY (facility_id) REFERENCES public.facility(id);


--
-- TOC entry 3393 (class 2606 OID 18174)
-- Name: discipline fk6mvnotlu4lt19uttp5iru0lgj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discipline
    ADD CONSTRAINT fk6mvnotlu4lt19uttp5iru0lgj FOREIGN KEY (facility_id) REFERENCES public.facility(id);


--
-- TOC entry 3396 (class 2606 OID 18189)
-- Name: image fk70bgcd0aa8jlwfm2rtjigca8e; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.image
    ADD CONSTRAINT fk70bgcd0aa8jlwfm2rtjigca8e FOREIGN KEY (facility_id) REFERENCES public.facility(id);


--
-- TOC entry 3397 (class 2606 OID 18194)
-- Name: image fk7gs4kxtatqefslx5mdrqpics4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.image
    ADD CONSTRAINT fk7gs4kxtatqefslx5mdrqpics4 FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- TOC entry 3400 (class 2606 OID 18214)
-- Name: review fk7x0v65q7pqiv81hkp0d9n83pt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.review
    ADD CONSTRAINT fk7x0v65q7pqiv81hkp0d9n83pt FOREIGN KEY (facility_id) REFERENCES public.facility(id);


--
-- TOC entry 3394 (class 2606 OID 18184)
-- Name: exercise fkdbjju56fhtaalf6swtj8clx1w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exercise
    ADD CONSTRAINT fkdbjju56fhtaalf6swtj8clx1w FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- TOC entry 3390 (class 2606 OID 18159)
-- Name: administrator fkfnik5dprkxunmt7vlo4py1e4p; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.administrator
    ADD CONSTRAINT fkfnik5dprkxunmt7vlo4py1e4p FOREIGN KEY (id) REFERENCES public."user"(id);


--
-- TOC entry 3391 (class 2606 OID 18164)
-- Name: comment fkhvh0e2ybgg16bpu229a5teje7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comment
    ADD CONSTRAINT fkhvh0e2ybgg16bpu229a5teje7 FOREIGN KEY (parent_comment_id) REFERENCES public.comment(id);


--
-- TOC entry 3401 (class 2606 OID 18224)
-- Name: review fkj8m0asijw8lfl4jxhcps8tf4y; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.review
    ADD CONSTRAINT fkj8m0asijw8lfl4jxhcps8tf4y FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- TOC entry 3395 (class 2606 OID 18179)
-- Name: exercise fkk2u1gmn5ewedy9un5y77u9k9f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exercise
    ADD CONSTRAINT fkk2u1gmn5ewedy9un5y77u9k9f FOREIGN KEY (facility_id) REFERENCES public.facility(id);


--
-- TOC entry 3402 (class 2606 OID 18219)
-- Name: review fkkwrav77pt60ilo3klp6gjn4sh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.review
    ADD CONSTRAINT fkkwrav77pt60ilo3klp6gjn4sh FOREIGN KEY (rate_id) REFERENCES public.rate(id);


--
-- TOC entry 3403 (class 2606 OID 18209)
-- Name: review fkmy5p87ip70r5win0vwrjgfw1r; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.review
    ADD CONSTRAINT fkmy5p87ip70r5win0vwrjgfw1r FOREIGN KEY (comment_id) REFERENCES public.comment(id);


--
-- TOC entry 3392 (class 2606 OID 18169)
-- Name: comment fkn6xssinlrtfnm61lwi0ywn71q; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comment
    ADD CONSTRAINT fkn6xssinlrtfnm61lwi0ywn71q FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- TOC entry 3398 (class 2606 OID 18204)
-- Name: manages fkngkcvmhaqwfb6l1oxomf982ae; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.manages
    ADD CONSTRAINT fkngkcvmhaqwfb6l1oxomf982ae FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- TOC entry 3399 (class 2606 OID 18199)
-- Name: manages fkprv78y853m9d7rom5kr2ld2hp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.manages
    ADD CONSTRAINT fkprv78y853m9d7rom5kr2ld2hp FOREIGN KEY (facility_id) REFERENCES public.facility(id);


-- Completed on 2024-06-22 18:53:58 CEST

--
-- PostgreSQL database dump complete
--

