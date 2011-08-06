--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

--
-- Name: seq_country_id; Type: SEQUENCE; Schema: public; Owner: jooq_wicket_example
--

CREATE SEQUENCE seq_country_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

--
-- Name: seq_country_id; Type: SEQUENCE SET; Schema: public; Owner: jooq_wicket_example
--

SELECT pg_catalog.setval('seq_country_id', 1, false);


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: country; Type: TABLE; Schema: public; Owner: jooq_wicket_example; Tablespace: 
--

CREATE TABLE country (
    id bigint DEFAULT nextval('seq_country_id'::regclass) NOT NULL,
    name character varying NOT NULL
);

--
-- Name: seq_person_id; Type: SEQUENCE; Schema: public; Owner: jooq_wicket_example
--

CREATE SEQUENCE seq_person_id
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

--
-- Name: seq_person_id; Type: SEQUENCE SET; Schema: public; Owner: jooq_wicket_example
--

SELECT pg_catalog.setval('seq_person_id', 31, true);


--
-- Name: person; Type: TABLE; Schema: public; Owner: jooq_wicket_example; Tablespace: 
--

CREATE TABLE person (
    id bigint DEFAULT nextval('seq_person_id'::regclass) NOT NULL,
    name character varying NOT NULL,
    city character varying NOT NULL,
    country_id bigint NOT NULL
);

--
-- Name: person_country_view; Type: VIEW; Schema: public; Owner: jooq_wicket_example
--

CREATE VIEW person_country_view AS
    SELECT person.id, person.name, person.city, country.name AS country_name FROM (person JOIN country ON ((person.country_id = country.id)));

--
-- Data for Name: country; Type: TABLE DATA; Schema: public; Owner: jooq_wicket_example
--

INSERT INTO country (id, name) VALUES
(460, 'AFGHANISTAN'),
(461, 'ÅLAND ISLANDS'),
(462, 'ALBANIA'),
(463, 'ALGERIA'),
(464, 'AMERICAN SAMOA'),
(465, 'ANDORRA'),
(466, 'ANGOLA'),
(467, 'ANGUILLA'),
(468, 'ANTARCTICA'),
(469, 'ANTIGUA AND BARBUDA'),
(470, 'ARGENTINA'),
(471, 'ARMENIA'),
(472, 'ARUBA'),
(473, 'AUSTRALIA'),
(475, 'AZERBAIJAN'),
(476, 'BAHAMAS'),
(477, 'BAHRAIN'),
(478, 'BANGLADESH'),
(479, 'BARBADOS'),
(480, 'BELARUS'),
(482, 'BELIZE'),
(483, 'BENIN'),
(484, 'BERMUDA'),
(485, 'BHUTAN'),
(486, 'BOLIVIA, PLURINATIONAL STATE OF'),
(487, 'BONAIRE, SINT EUSTATIUS AND SABA'),
(488, 'BOSNIA AND HERZEGOVINA'),
(489, 'BOTSWANA'),
(490, 'BOUVET ISLAND'),
(491, 'BRAZIL'),
(492, 'BRITISH INDIAN OCEAN TERRITORY'),
(493, 'BRUNEI DARUSSALAM'),
(495, 'BURKINA FASO'),
(496, 'BURUNDI'),
(497, 'CAMBODIA'),
(498, 'CAMEROON'),
(500, 'CAPE VERDE'),
(501, 'CAYMAN ISLANDS'),
(502, 'CENTRAL AFRICAN REPUBLIC'),
(503, 'CHAD'),
(504, 'CHILE'),
(505, 'CHINA'),
(506, 'CHRISTMAS ISLAND'),
(507, 'COCOS (KEELING) ISLANDS'),
(508, 'COLOMBIA'),
(509, 'COMOROS'),
(510, 'CONGO'),
(511, 'CONGO, THE DEMOCRATIC REPUBLIC OF THE'),
(512, 'COOK ISLANDS'),
(513, 'COSTA RICA'),
(514, 'CÔTE D''IVOIRE'),
(515, 'CROATIA'),
(516, 'CUBA'),
(517, 'CURAÇAO'),
(521, 'DJIBOUTI'),
(522, 'DOMINICA'),
(523, 'DOMINICAN REPUBLIC'),
(524, 'ECUADOR'),
(525, 'EGYPT'),
(526, 'EL SALVADOR'),
(527, 'EQUATORIAL GUINEA'),
(528, 'ERITREA'),
(530, 'ETHIOPIA'),
(531, 'FALKLAND ISLANDS (MALVINAS)'),
(532, 'FAROE ISLANDS'),
(533, 'FIJI'),
(536, 'FRENCH GUIANA'),
(537, 'FRENCH POLYNESIA'),
(538, 'FRENCH SOUTHERN TERRITORIES'),
(539, 'GABON'),
(540, 'GAMBIA'),
(541, 'GEORGIA'),
(543, 'GHANA'),
(544, 'GIBRALTAR'),
(546, 'GREENLAND'),
(547, 'GRENADA'),
(548, 'GUADELOUPE'),
(549, 'GUAM'),
(550, 'GUATEMALA'),
(551, 'GUERNSEY'),
(552, 'GUINEA'),
(553, 'GUINEA-BISSAU'),
(554, 'GUYANA'),
(555, 'HAITI'),
(556, 'HEARD ISLAND AND MCDONALD ISLANDS'),
(557, 'HOLY SEE (VATICAN CITY STATE)'),
(558, 'HONDURAS'),
(559, 'HONG KONG'),
(561, 'ICELAND'),
(562, 'INDIA'),
(563, 'INDONESIA'),
(564, 'IRAN, ISLAMIC REPUBLIC OF'),
(565, 'IRAQ'),
(567, 'ISLE OF MAN'),
(568, 'ISRAEL'),
(570, 'JAMAICA'),
(571, 'JAPAN'),
(572, 'JERSEY'),
(573, 'JORDAN'),
(574, 'KAZAKHSTAN'),
(575, 'KENYA'),
(576, 'KIRIBATI'),
(577, 'KOREA, DEMOCRATIC PEOPLE''S REPUBLIC OF'),
(578, 'KOREA, REPUBLIC OF'),
(579, 'KUWAIT'),
(580, 'KYRGYZSTAN'),
(581, 'LAO PEOPLE''S DEMOCRATIC REPUBLIC'),
(583, 'LEBANON'),
(584, 'LESOTHO'),
(585, 'LIBERIA'),
(586, 'LIBYAN ARAB JAMAHIRIYA'),
(587, 'LIECHTENSTEIN'),
(590, 'MACAO'),
(591, 'MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF'),
(592, 'MADAGASCAR'),
(593, 'MALAWI'),
(594, 'MALAYSIA'),
(595, 'MALDIVES'),
(596, 'MALI'),
(598, 'MARSHALL ISLANDS'),
(599, 'MARTINIQUE'),
(600, 'MAURITANIA'),
(601, 'MAURITIUS'),
(602, 'MAYOTTE'),
(603, 'MEXICO'),
(604, 'MICRONESIA, FEDERATED STATES OF'),
(605, 'MOLDOVA, REPUBLIC OF'),
(606, 'MONACO'),
(607, 'MONGOLIA'),
(608, 'MONTENEGRO'),
(609, 'MONTSERRAT'),
(610, 'MOROCCO'),
(611, 'MOZAMBIQUE'),
(612, 'MYANMAR'),
(613, 'NAMIBIA'),
(614, 'NAURU'),
(615, 'NEPAL'),
(617, 'NEW CALEDONIA'),
(618, 'NEW ZEALAND'),
(619, 'NICARAGUA'),
(620, 'NIGER'),
(621, 'NIGERIA'),
(622, 'NIUE'),
(623, 'NORFOLK ISLAND'),
(624, 'NORTHERN MARIANA ISLANDS'),
(625, 'NORWAY'),
(626, 'OMAN'),
(627, 'PAKISTAN'),
(628, 'PALAU'),
(629, 'PALESTINIAN TERRITORY, OCCUPIED'),
(630, 'PANAMA'),
(631, 'PAPUA NEW GUINEA'),
(632, 'PARAGUAY'),
(633, 'PERU'),
(634, 'PHILIPPINES'),
(635, 'PITCAIRN'),
(638, 'PUERTO RICO'),
(639, 'QATAR'),
(640, 'RÉUNION'),
(642, 'RUSSIAN FEDERATION'),
(643, 'RWANDA'),
(644, 'SAINT BARTHÉLEMY'),
(645, 'SAINT HELENA, ASCENSION AND TRISTAN DA CUNHA'),
(646, 'SAINT KITTS AND NEVIS'),
(647, 'SAINT LUCIA'),
(648, 'SAINT MARTIN (FRENCH PART)'),
(649, 'SAINT PIERRE AND MIQUELON'),
(650, 'SAINT VINCENT AND THE GRENADINES'),
(651, 'SAMOA'),
(652, 'SAN MARINO'),
(653, 'SAO TOME AND PRINCIPE'),
(654, 'SAUDI ARABIA'),
(655, 'SENEGAL'),
(656, 'SERBIA'),
(657, 'SEYCHELLES'),
(658, 'SIERRA LEONE'),
(659, 'SINGAPORE'),
(660, 'SINT MAARTEN (DUTCH PART)'),
(663, 'SOLOMON ISLANDS'),
(664, 'SOMALIA'),
(665, 'SOUTH AFRICA'),
(666, 'SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS'),
(668, 'SRI LANKA'),
(669, 'SUDAN'),
(670, 'SURINAME'),
(671, 'SVALBARD AND JAN MAYEN'),
(672, 'SWAZILAND'),
(674, 'SWITZERLAND'),
(675, 'SYRIAN ARAB REPUBLIC'),
(676, 'TAIWAN, PROVINCE OF CHINA'),
(677, 'TAJIKISTAN'),
(678, 'TANZANIA, UNITED REPUBLIC OF'),
(679, 'THAILAND'),
(680, 'TIMOR-LESTE'),
(681, 'TOGO'),
(682, 'TOKELAU'),
(683, 'TONGA'),
(684, 'TRINIDAD AND TOBAGO'),
(685, 'TUNISIA'),
(686, 'TURKEY'),
(687, 'TURKMENISTAN'),
(688, 'TURKS AND CAICOS ISLANDS'),
(689, 'TUVALU'),
(690, 'UGANDA'),
(691, 'UKRAINE'),
(692, 'UNITED ARAB EMIRATES'),
(695, 'UNITED STATES MINOR OUTLYING ISLANDS'),
(696, 'URUGUAY'),
(697, 'UZBEKISTAN'),
(698, 'VANUATU'),
(699, 'VENEZUELA, BOLIVARIAN REPUBLIC OF'),
(700, 'VIET NAM'),
(701, 'VIRGIN ISLANDS, BRITISH'),
(702, 'VIRGIN ISLANDS, U.S.'),
(703, 'WALLIS AND FUTUNA'),
(704, 'WESTERN SAHARA'),
(705, 'YEMEN'),
(706, 'ZAMBIA'),
(707, 'ZIMBABWE'),
(694, 'UNITED STATES'),
(499, 'CANADA'),
(474, 'AUSTRIA'),
(481, 'BELGIUM'),
(494, 'BULGARIA'),
(518, 'CYPRUS'),
(519, 'CZECH REPUBLIC'),
(520, 'DENMARK'),
(529, 'ESTONIA'),
(534, 'FINLAND'),
(535, 'FRANCE'),
(542, 'GERMANY'),
(545, 'GREECE'),
(560, 'HUNGARY'),
(566, 'IRELAND'),
(569, 'ITALY'),
(582, 'LATVIA'),
(588, 'LITHUANIA'),
(589, 'LUXEMBOURG'),
(597, 'MALTA'),
(636, 'POLAND'),
(637, 'PORTUGAL'),
(641, 'ROMANIA'),
(661, 'SLOVAKIA'),
(662, 'SLOVENIA'),
(667, 'SPAIN'),
(673, 'SWEDEN'),
(693, 'UNITED KINGDOM'),
(616, 'NETHERLANDS');


--
-- Data for Name: person; Type: TABLE DATA; Schema: public; Owner: jooq_wicket_example
--

INSERT INTO person (id, name, city, country_id) VALUES
(18, 'Pietje Puk', 'Haarlem', 616),
(19, 'Barack Obama', 'Washington', 694),
(20, 'Angela Merkel', 'Berlin', 542),
(21, 'Bill Gates', 'Seattle', 694),
(22, 'Larry Ellison', 'Redwood Shores', 694),
(23, 'John Doe', 'New York', 694),
(24, 'Goodluck Jonathan', 'Abuja', 621),
(25, 'Susilo Bambang Yudhoyono', 'Jakarta', 563),
(27, 'Mark Rutte', 'Den Haag', 616),
(16, 'Sander Plas', 'Amsterdam', 616),
(17, 'Lukas Eder', 'Zürich', 674),
(26, 'Micheline Calmy-Rey', 'Zürich', 674);

--
-- Name: country_pkey; Type: CONSTRAINT; Schema: public; Owner: jooq_wicket_example; Tablespace: 
--

ALTER TABLE ONLY country
    ADD CONSTRAINT country_pkey PRIMARY KEY (id);


--
-- Name: person_pkey; Type: CONSTRAINT; Schema: public; Owner: jooq_wicket_example; Tablespace: 
--

ALTER TABLE ONLY person
    ADD CONSTRAINT person_pkey PRIMARY KEY (id);


--
-- Name: person_country_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: jooq_wicket_example
--

ALTER TABLE ONLY person
    ADD CONSTRAINT person_country_id_fkey FOREIGN KEY (country_id) REFERENCES country(id);

--
-- PostgreSQL database dump complete
--

