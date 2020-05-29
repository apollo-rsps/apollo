#!/bin/bash

docker volume rm database_data

docker-compose up --build -d