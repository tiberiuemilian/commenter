source .env && docker exec mysql mysqldump -u"${MYSQL_USER}" -p"${MYSQL_PASSWORD}" --databases commenter > ../mysql-dump/commenter-dump.sql
