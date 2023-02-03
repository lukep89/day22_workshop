# day 22 workshop

spring boot connection string for application.properties
https://docs.spring.io/spring-boot/docs/2.1.13.RELEASE/reference/html/boot-features-sql.html
spring.datasource.url=jdbc:mysql://localhost/test
spring.datasource.username=dbuser
spring.datasource.password=dbpass
spring.datasource.driver-class-name=com.mysql.jdbc.Driver


GET /api/rsvps
GET /api/rsvps?q=fred
POST /api/rsvps
PUT /api/rsvps/{id}
GET /api/rsvps/count