FROM mariadb:lts

# 데이터베이스 초기화 스크립트 복사
COPY member_database.sql /docker-entrypoint-initdb.d/

# 환경 변수 설정
ENV MYSQL_ROOT_PASSWORD root
ENV MYSQL_DATABASE member_database
ENV MYSQL_USER root
ENV MYSQL_PASSWORD root