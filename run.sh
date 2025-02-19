#!/bin/bash

DB_CONTAINER_NAME="minesweeper-db"
DB_PORT=5432
POSTGRES_VERSION="17"
POSTGRES_USER="postgres"
POSTGRES_PASSWORD="password"
POSTGRES_DB="minesweeper"

if [ "$(docker ps -q -f name=$DB_CONTAINER_NAME)" ]; then
    echo "PostgreSQL уже запущен."
else
    echo "Запуск PostgreSQL $POSTGRES_VERSION..."
    docker run -d --name $DB_CONTAINER_NAME \
        -e POSTGRES_USER=$POSTGRES_USER \
        -e POSTGRES_PASSWORD=$POSTGRES_PASSWORD \
        -e POSTGRES_DB=$POSTGRES_DB \
        -p $DB_PORT:5432 \
        --restart unless-stopped \
        postgres:$POSTGRES_VERSION

    echo "Ожидание запуска PostgreSQL..."
    sleep 10

    # Обновление конфигурации pg_hba.conf (замена scram-sha-256 на md5)
    docker exec -u root $DB_CONTAINER_NAME bash -c "
        sed -i 's/scram-sha-256/md5/g' /var/lib/postgresql/data/pg_hba.conf
        pg_ctl reload
    "
fi

if [ "$(docker ps -q -f name=$DB_CONTAINER_NAME)" ]; then
    echo "PostgreSQL успешно запущен!"
else
    echo "Ошибка запуска PostgreSQL!"
    exit 1
fi

export SPRING_R2DBC_URL="r2dbc:postgresql://localhost:5432/minesweeper"
export SPRING_R2DBC_USERNAME="postgres"
export SPRING_R2DBC_PASSWORD="password"
export SPRING_FLYWAY_URL="jdbc:postgresql://localhost:5432/minesweeper"
export SPRING_FLYWAY_USER="postgres"
export SPRING_FLYWAY_PASSWORD="password"

echo "Сборка проекта..."
./gradlew clean bootJar

echo "Запуск Spring Boot приложения..."
java -jar build/libs/*.jar

read -p "Нажмите Enter для выхода..."
