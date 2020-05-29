# Apollo - Database

Contains the database schema for the Apollo project. Start-up and tear-down scripts are included as well as Docker related files. These scripts rely on Docker so it is required to have Docker installed on your machine. This means that you may require an operating system that supports Hyper-V, which include (but are not limited to):
- Windows 10 Professional+
- Linux distributions
- MacOS distributions

Windows 10 Home is not supported as is specified [here](https://forums.docker.com/t/installing-docker-on-windows-10-home/11722).

## Running it

First permit your operating system to run these scripts:

```
chmod a+x ./start-db.sh
chmod a+x ./stop-db.sh
```

And then to start your PostgreSQL container with your database setup:

```
./start-db.sh
```

And to stop it:

```
./stop-db.sh
```

## Updating the schema
You can modify the [apollo.sql](apollo.sql) script to add dummy data to your database. Note however that you are required to delete the associated volume after modifying the script to make it work:

```
docker volume rm database_data
```

The `start.sh` and `stop.sh` scripts do this for you as well.