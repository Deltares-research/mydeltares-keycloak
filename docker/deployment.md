### Start Keycloak
To start Keycloak run the following command in your project ./docker folder.
```
docker-compose up
```
This command will create the container for Liferay, MariaDB and ElasticSearch.
```
docker ps -a
f1609a61da15        quay.io/keycloak/keycloak:9.0.2   "/opt/jboss/tools/do…"   19 minutes ago      Up 19 minutes             0.0.0.0:8080->8080/tcp, 0.0.0.0:8443->8443/tcp, 0.0.0.0:8787->8787/tcp, 0.0.0.0:9990->9990/tcp   keycloak
9fd9ef0b1889        mariadb:10.2.25                   "docker-entrypoint.s…"   19 minutes ago      Up 19 minutes (healthy)   0.0.0.0:3307->3306/tcp                                                                           keycloak-m
ariadb


docker volume ls
DRIVER              VOLUME NAME
local               docker_mysql_data
```

### Checking the logs
The logs are available though docker.
```
docker logs -f keycloak-mariadb
docker logs -f keycloak
```

### Stop Liferay
To stop Keycloak run the following command in your project docker folder.
```
docker-compose down
```

### Deploy modules
To deploy the Theme and Keycloak modules run the following command in your project root folder.
```
mvn clean package
```

### Cleaning database and document library
If you need to restore your environment from a backup first clean the docker volumes.
```
docker volume rm docker_mysql_data
```
