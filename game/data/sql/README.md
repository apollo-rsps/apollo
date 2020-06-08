# Apollo - SQL Database

For local development, a PostgreSQL database can be started using Docker and Docker-Compose, with the following command:

```
docker-compose up
```

You can modify the [apollo.sql](./apollo.sql) script to add dummy data to your database. Note however that you are required to delete the associated volume after modifying the script to make it work:

```
docker volume rm database_data
```