# Proyecto Backend: Servicio de Recargas con Spring Boot

Este proyecto es un servicio backend para manejar recargas de proveedores móviles. Está desarrollado en **Spring Boot** y realiza las siguientes funciones principales:
- Autenticación con un API de terceros.
- Obtención de proveedores de recargas disponibles.
- Realización de compras de recargas.
- Almacenamiento de transacciones en una base de datos H2.
- Consulta de las transacciones realizadas.

## Requisitos previos

Asegúrate de tener lo siguiente instalado en tu sistema:
- **Java 17**.
- **Maven** (para construir y ejecutar el proyecto).
- **Azure App Service** (si despliegas en la nube, aunque puedes usar cualquier servicio en la nube o en local).

## Configuración

### 1. Clona el repositorio

```bash
git clone https://github.com/Arbyl/recargas.git
cd recargas
```
### 2. Configura las propiedades de la base de datos
En el archivo src/main/resources/application.properties, se utiliza una base de datos MySQL para el desarrollo. No obstante, puedes cambiar a otra base de datos si lo prefieres.

la configuracion se hace de esta manera asignando variables de entorno para proteger la informacion sensible

```bash
# Configuración de MySQL
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuración de JPA e Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### 3. Configuración de API de terceros
El servicio se conecta a una API externa para manejar las recargas. Las URL y credenciales de autenticación ya están configuradas en el archivo AuthService.java:

URL de autenticación: https://us-central1-puntored-dev.cloudfunctions.net/technicalTest-developer/api/auth
URL para obtener proveedores: https://us-central1-puntored-dev.cloudfunctions.net/technicalTest-developer/api/getSuppliers
URL para realizar la compra: https://us-central1-puntored-dev.cloudfunctions.net/technicalTest-developer/api/buy
Asegúrate de que las credenciales del API estén correctamente configuradas.

### 4. Construir y ejecutar el proyecto
Usa Maven para construir y ejecutar el proyecto localmente:

```bash
mvn clean install
mvn spring-boot:run
```
Esto ejecutará el proyecto en http://localhost:8080.

### 5. Endpoints disponibles

Autenticación: GET /authenticate
Autentica con el API y devuelve un token Bearer.

Obtener proveedores: GET /getSuppliers
Lista los proveedores de recargas disponibles.

Realizar recarga: POST /buy
Realiza una recarga a un número de teléfono.

Cuerpo de la solicitud:
```json
{
  "cellPhone": "3210338900",
  "value": 1000,
  "supplierId": "8753"
}
```

Ver transacciones: GET /transactions
Lista todas las transacciones realizadas.

### 6. Despliegue en Azure
Este backend ha sido desplegado utilizando Azure App Service. Para el despliegue:

Crea un Azure App Service en tu cuenta de Azure.
Configura las variables de entorno para la base de datos y usuario y contraseña de la API.


```bash
az webapp up --resource-group <nombre-del-grupo> --name <nombre-de-la-app> --plan <nombre-del-plan>
```


