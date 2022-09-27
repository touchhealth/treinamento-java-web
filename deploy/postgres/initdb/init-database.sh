#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE USER treinamento WITH PASSWORD 'treinamento';
	CREATE DATABASE treinamento OWNER treinamento;
	GRANT ALL PRIVILEGES ON DATABASE treinamento TO treinamento;
EOSQL
