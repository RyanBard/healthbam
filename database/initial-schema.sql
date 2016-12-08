-- noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    email TEXT NOT NULL,
    super_admin BOOLEAN NOT NULL DEFAULT FALSE,
    admin BOOLEAN NOT NULL DEFAULT FALSE,
    display_name TEXT,
    image_url TEXT,
    first_name TEXT,
    last_name TEXT,
    middle_name TEXT,
    prefix TEXT,
    suffix TEXT,
    profile_url TEXT,
    created_on DATE NOT NULL,
    created_by TEXT NOT NULL,
    updated_on DATE,
    updated_by TEXT
);

/*
 * Create the unique index to make the unique constraint on emails case insensitive.
 */
CREATE UNIQUE INDEX app_user_lower_email_index ON app_user(LOWER(email));

CREATE TABLE organization (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    contact_phone TEXT,
    contact_email TEXT,
    website_url TEXT,
    facebook_url TEXT,
    created_on DATE NOT NULL,
    created_by TEXT NOT NULL,
    updated_on DATE,
    updated_by TEXT
);

CREATE TABLE county (
    id BIGINT PRIMARY KEY,
    name TEXT NOT NULL,
    state TEXT NOT NULL,
    outer_boundary1 TEXT NOT NULL,
    inner_boundary1 TEXT,
    outer_boundary2 TEXT,
    outer_boundary3 TEXT,
    UNIQUE(name, state)
);

CREATE TABLE program_area (
    id BIGINT PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE program (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    organization_id BIGINT REFERENCES organization(id),
    start_year INT,
    street_address TEXT NOT NULL,
    city TEXT NOT NULL,
    state TEXT NOT NULL,
    zip_code TEXT NOT NULL,
    coordinates TEXT NOT NULL,
    primary_goal1 TEXT NOT NULL,
    primary_goal2 TEXT,
    primary_goal3 TEXT,
    measurable_outcome1 TEXT NOT NULL,
    measurable_outcome2 TEXT,
    measurable_outcome3 TEXT,
    serves_all_counties BOOLEAN NOT NULL DEFAULT FALSE,
    other_program_area_explanation TEXT,
    created_on DATE NOT NULL,
    created_by TEXT NOT NULL,
    updated_on DATE,
    updated_by TEXT,
    UNIQUE(name, organization_id)
);

CREATE TABLE program_county (
    program_id BIGINT REFERENCES program(id),
    county_id BIGINT REFERENCES county(id),
    PRIMARY KEY(program_id, county_id)
);

CREATE TABLE program_program_area (
    program_id BIGINT REFERENCES program(id),
    program_area_id BIGINT REFERENCES program_area(id),
    PRIMARY KEY(program_id, program_area_id)
);
