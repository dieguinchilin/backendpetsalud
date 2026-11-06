Clínica PETSALUD – Seguridad Backend

Descripción del proyecto

Proyecto académico desarrollado con Java, Spring Boot y MySQL para la gestión de una clínica veterinaria. Esta actividad se enfoca en implementar mecanismos de autenticación y autorización para proteger los recursos del backend.

Tecnologías utilizadas

Java y Spring Boot

Spring Security

JSON Web Token (JWT)

Spring Data JPA

MySQL

Maven

Postman

Seguridad del sistema

El proyecto incorpora componentes para la autenticación mediante JWT y el control de acceso a los endpoints.

Los componentes principales son:

SecurityConfig.java: configuración de seguridad.

JwtService.java: generación y validación de tokens.

JwtAuthenticationFilter.java: procesamiento del token recibido en las solicitudes.

UsuarioDetailsService.java: carga de usuarios para la autenticación.

Roles del sistema

Se consideran tres roles con diferentes niveles de acceso:

CLIENTE: acceso a las funcionalidades autorizadas para clientes.

EMPLEADO: acceso a las operaciones permitidas al personal.

GERENTE: acceso a las funciones administrativas autorizadas.

La aplicación debe comprobar los permisos antes de permitir el acceso a los recursos protegidos.

Riesgos de seguridad analizados

Acceso no autorizado: protección mediante autenticación y autorización por roles.

Inyección SQL: uso de repositorios JPA y consultas parametrizadas.

XSS: validación y tratamiento seguro de los datos recibidos.

CSRF: revisión de la configuración de seguridad según el mecanismo de autenticación utilizado.

Pruebas

Las pruebas de seguridad deben comprobar:

Autenticación con credenciales válidas e inválidas.

Acceso a endpoints con y sin token JWT.

Rechazo de tokens inválidos o vencidos.

Restricciones de acceso según el rol.

Resistencia frente a entradas maliciosas relacionadas con SQL y XSS.

Los resultados y evidencias de estas pruebas se documentan en el informe técnico de la actividad.

Ejecución del proyecto

Tener instalados Java, MySQL y Maven.

Configurar la base de datos petsalud_db.

Definir la variable de entorno DB_PASSWORD.

Configurar de manera segura el secreto JWT mediante JWT_SECRET.

Ejecutar el proyecto desde su carpeta principal:

.\mvnw.cmd spring-boot:run

Propósito académico

Actividad Sumativa Semana 3: Integrando seguridad en aplicaciones Backend.

El objetivo es analizar amenazas, configurar Spring Security y JWT, implementar permisos por roles y documentar las pruebas realizadas.
