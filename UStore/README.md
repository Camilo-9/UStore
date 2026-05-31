# UStore

## Descripción

UStore es una API REST desarrollada con Spring Boot para la gestión de una tienda en línea. El sistema permite administrar clientes, direcciones, categorías, productos, inventario, pedidos y reportes de negocio.

El proyecto fue desarrollado siguiendo una arquitectura por capas y aplicando buenas prácticas de desarrollo como validación de datos, manejo centralizado de excepciones, pruebas automatizadas y pruebas de integración con Testcontainers.

# Tecnologías utilizadas

* Java 21
* Spring Boot 4
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* JUnit 5
* Mockito
* Testcontainer

# Arquitectura

El proyecto está organizado en capas:

* Controller: expone los endpoints REST.
* Service: contiene la lógica de negocio.
* Repository: acceso a datos mediante Spring Data JPA.
* Security: seguridad para la API (omitida para producción).

# Reglas de negocio

## Clientes

* Un cliente puede tener múltiples direcciones.
* El correo electrónico de un cliente debe ser único.
* No se permite registrar clientes con información inválida.

## Categorías

* Las categorías pueden contener múltiples productos.
* Una categoría debe existir antes de asociar productos a ella.

## Productos

* Cada producto pertenece a una única categoría.
* El SKU debe ser único.
* Un producto puede estar activo o inactivo.

## Inventario

* Cada producto posee un único registro de inventario.
* La cantidad disponible no puede ser negativa.
* El inventario se actualiza automáticamente cuando se procesan pedidos.

## Pedidos

### Estados posibles

* CREATED
* PAID
* SHIPPED
* DELIVERED
* CANCELLED

### Flujo de estados

CREATED → PAID → SHIPPED → DELIVERED

También es posible:

CREATED → CANCELLED

### Restricciones

* No se puede enviar un pedido que no haya sido pagado.
* No se puede entregar un pedido que no haya sido enviado.
* Un pedido debe contener al menos un ítem.
* Al pagar un pedido se valida la disponibilidad de inventario.
* Si no existe stock suficiente se genera una excepción de negocio.

## Historial de estados

Cada cambio de estado de un pedido se registra en una tabla de historial para mantener trazabilidad completa del proceso.

# Reportes

La API incluye los siguientes reportes:

* Productos más vendidos por rango de fechas.
* Ingresos mensuales.
* Clientes con mayor volumen de compras.
* Productos con bajo stock.

# Decisiones de diseño

## Arquitectura por capas

Se separó la aplicación en Controller, Service y Repository para mantener bajo acoplamiento y alta cohesión.

## Manejo global de excepciones

Se implementó un GlobalExceptionHandler para centralizar la respuesta de errores y evitar duplicación de código.

## Testcontainers

Las pruebas de integración utilizan PostgreSQL ejecutado dentro de contenedores Docker, garantizando un entorno aislado y reproducible.

# Pruebas automatizadas

El proyecto cuenta con:

* Pruebas unitarias para la capa Service.
* Pruebas de integración para la capa Repository.
* Pruebas WebMvc para la capa Controller.

Las pruebas utilizan:

* JUnit 5
* Mockito
* Spring Boot Test
* Testcontainers

Ejecución:

* Dirigirse a la parte superior derecha y hacer clic en la "m" de Maven.
* Hacer doble clic en "uStore".
* Hacer doble clic en "Lifecycle".
* Hacer doble clic en "clean".
* Hacer doble clic en "test" o en "install".

# Ejecución del proyecto

## Requisitos

* Java 21
* Maven
* Docker Desktop

## Ejecutar la aplicación

Iniciar Docker Desktop.

Ejecutar la clase:

TestUStoreApplication

La aplicación quedará disponible en:

http://localhost:8081
