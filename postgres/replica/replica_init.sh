#!/bin/sh
set -e

PGDATA=${PGDATA:-/var/lib/postgresql/data}
PRIMARY_HOST=${PRIMARY_HOST:-db-primary}
PRIMARY_PORT=${PRIMARY_PORT:-5432}
REPL_USER=${REPL_USER:-replicator}
REPL_PASSWORD=${REPL_PASSWORD:-replica_pass}

# Если PGDATA уже инициализирован — стартуем postgres под postgres-пользователем
if [ -s "$PGDATA/PG_VERSION" ]; then
  echo "PGDATA not empty — starting postgres as user 'postgres'"
  # Попытка безопасно переключиться на postgres-пользователя
  exec su postgres -c "postgres -c config_file=/etc/postgresql/postgresql.conf"
fi

# Обычная инициализация реплики
echo "Waiting for primary to accept connections..."
export PGPASSWORD="$REPL_PASSWORD"
until pg_isready -h "$PRIMARY_HOST" -p "$PRIMARY_PORT" -U "$REPL_USER" >/dev/null 2>&1; do
  echo "Waiting for primary at $PRIMARY_HOST:$PRIMARY_PORT..."
  sleep 1
done

echo "Performing base backup from primary..."
pg_basebackup -h "$PRIMARY_HOST" -p "$PRIMARY_PORT" -D "$PGDATA" -U "$REPL_USER" -v -P --wal-method=stream

# Создаем standby.signal для Postgres 12+
touch "$PGDATA/standby.signal"

# Записываем primary_conninfo
cat >> "$PGDATA/postgresql.auto.conf" <<EOF
primary_conninfo = 'host=$PRIMARY_HOST port=$PRIMARY_PORT user=$REPL_USER password=$REPL_PASSWORD application_name=db-replica'
primary_slot_name = ''
EOF

# Обеспечиваем правильные права
chown -R postgres:postgres "$PGDATA"
chmod 700 "$PGDATA"

echo "Replica setup done — starting postgres as user 'postgres'"
exec su postgres -c "postgres -c config_file=/etc/postgresql/postgresql.conf"
