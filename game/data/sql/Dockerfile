# Tell Docker to pull a Postgres 11 image running on Alpine
FROM postgres:11-alpine

# And then copy over the apollo.sql script into the container's entrypoint
COPY apollo.sql /docker-entrypoint-initdb.d/init.sql