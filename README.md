# 🐶 Woof! - API Solutions

API REST desarrollada con Spring Boot para **Woof!**, una plataforma de gamificación orientada a mascotas.

Este proyecto gestiona la lógica de negocio, autenticación y persistencia de datos utilizando PostgreSQL en la nube.

---

## 🚀 Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Maven
- PostgreSQL (Supabase)
- Variables de entorno (.env)

---

## 🧠 ¿Qué es Woof!?

Woof! es una plataforma de gamificación para mascotas donde los usuarios pueden interactuar, registrar actividades y participar en dinámicas diseñadas para incentivar el cuidado y bienestar animal.

Esta API se encarga de:

- Gestión de usuarios
- Autenticación
- Persistencia de datos
- Lógica del sistema

---

## 🗄 Base de datos

Se utiliza **PostgreSQL** desplegado en la nube mediante Supabase.

La conexión se maneja mediante variables de entorno para proteger credenciales.

Archivo de ejemplo:

DB_URL=jdbc:postgresql://db.<>.supabase.co:5432/postgres
DB_USER=postgres
DB_PASS=

JWT_SECRET=
JWT_EXPIRATION_MS=

MAIL_USERNAME=woof.berlincompany@gmail.com
MAIL_PASSWORD=tu_contraseña_de_aplicacion
