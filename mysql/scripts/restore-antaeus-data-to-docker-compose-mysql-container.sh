source .env && cat ../mysql-dump/commenter-dump.sql | docker exec -i mysql mysql -u"${MYSQL_USER}" -p"${MYSQL_PASSWORD}"
