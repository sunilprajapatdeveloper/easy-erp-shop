#!/bin/bash
set -e

BACKUP_DIR="/home/deploy/backups"
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/backup_$DATE.tar.gz"

echo "Starting backup at $(date)"

# Create backup directory
mkdir -p $BACKUP_DIR

# Backup PostgreSQL
echo "Backing up PostgreSQL..."
docker exec erp-postgres pg_dumpall -U postgres > $BACKUP_DIR/postgres_$DATE.sql

# Backup Redis
echo "Backing up Redis..."
docker exec erp-redis redis-cli --rdb /data/dump.rdb
docker cp erp-redis:/data/dump.rdb $BACKUP_DIR/redis_$DATE.rdb

# Backup uploads
echo "Backing up uploads..."
docker cp erp-backend:/app/uploads $BACKUP_DIR/uploads_$DATE

# Compress all backups
echo "Compressing backups..."
tar -czf $BACKUP_FILE $BACKUP_DIR/*_$DATE.*

# Remove uncompressed files
rm -rf $BACKUP_DIR/*_$DATE.*

# Keep only last 7 days
find $BACKUP_DIR -name "backup_*.tar.gz" -mtime +7 -delete

echo "Backup completed: $BACKUP_FILE"
echo "Size: $(du -h $BACKUP_FILE | cut -f1)"