
# Recordario
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-green)
![React](https://img.shields.io/badge/React-18+-blue)
![Tests](https://img.shields.io/badge/Tests-JUnit%205-brightgreen)

Una aplicación web para estudiar de forma activa mediante el método de repetición espaciada (SM-2). Perfecto para estudiantes y lectores que quieren retener información a largo plazo.

**[🌐 Live Demo](https://recordario.vercel.app)** | **[📚 API Documentation](#api-documentation)** | **[🏗️ Arquitectura](#arquitectura)**

---

## 🎯 ¿Qué es Recordario?

Recordario te permite crear tarjetas de estudio organizadas en libros. El sistema te interroga usando el algoritmo SM-2, que ajusta automáticamente cuándo deberías revisar cada tarjeta basándose en qué tan bien las dominas. Es como tener un tutor personal que sabe exactamente qué necesitás practicar.

**Casos de uso:**
- Estudiantes preparándose para exámenes
- Programadores aprendiendo nuevas tecnologías
- Lectores reteniendο información de libros
- Cualquiera que quiera aprender de forma eficiente

---

## 🛠️ Tech Stack

### Backend
- **Java 17** + **Spring Boot 3.5.10**
- **PostgreSQL** para persistencia
- **JWT** (jjwt) para autenticación stateless
- **Spring Security** para autorización
- **Hibernate/JPA** para ORM

### Frontend
- **React 18+** 
- Comunicación con API mediante REST

### Testing
- **JUnit 5** para tests unitarios
- **Mockito** para mocks
- **MockMvc** para tests de controladores
- **H2 Database** para tests de integración

### DevOps
- **Docker** para contenerización
- **Maven** para build
- **Render** (backend + BD) + **Vercel** (frontend) para deployment

---

## ✨ Características Principales

### 🔐 Autenticación & Seguridad
- **JWT con refresh tokens:** Tokens con expiración de 15 minutos y refresh token de 7 días
- **Cookies seguras:** HttpOnly, Secure y SameSite configurados correctamente
- **Hasheado de contraseñas** con algoritmos seguros
- **Validación de entrada** en todos los endpoints

### 📊 Algoritmo SM-2
Sistema de repetición espaciada que:
- Calcula automáticamente cuándo revisar cada tarjeta
- Ajusta dificultad según tu desempeño
- Optimiza el tiempo de estudio

### ✅ Testing Riguroso
- **100% de cobertura** en servicios y controladores
- **Tests de integración** del flujo completo: signup → crear tarjetas → estudiar → logout
- **Tests en BD aislada** (H2) para reproducibilidad
- Cada método testeado unitariamente

### 📚 Organización Flexible
- Tarjetas agrupadas en libros
- Múltiples relaciones (usuarios → libros → tarjetas)
- Análisis de progreso individual por libro

### 📖 API Documentada
- **OpenAPI 3.0** completa
- Todos los endpoints documentados con modelos
- Validaciones visibles en spec

---

## 🚀 Instalación & Setup

### Requisitos Previos
```
Java 17+
Node.js v24.13.1+
PostgreSQL (o Docker)
Maven 3.8+
```

### Backend

1. **Clonar el repositorio**
```bash
git clone <repo-backend-url>
cd recordario-backend
```

2. **Configurar variables de entorno**
```bash
# Copiar archivo de ejemplo
cp .env.example .env

# Editar con tus valores
# Necesarias:
# - APPLICATION.SECURITY.JWT.CODIGO-SECRETO (clave privada para firmar JWT)
# - APPLICATION.SECURITY.JWT.TIEMPO-EXPIRACION (15 minutos en ms)
# - APPLICATION.SECURITY.JWT.TIEMPO-EXPIRACION-REFRESCO (7 días en ms)
# - SPRING.DATASOURCE.URL (postgresql://host:port/dbname)
# - SPRING.DATASOURCE.PASSWORD
# - SPRING_JPA_HIBERNATE_DDL_AUTO (create-drop para dev, validate para prod)
```

3. **Correr la aplicación**
```bash
mvn clean install
mvn spring-boot:run
```

La API estará disponible en `http://localhost:8080`

### Frontend

1. **Clonar el repositorio**
```bash
git clone <repo-frontend-url>
cd recordario-frontend
```

2. **Instalar dependencias y correr**
```bash
npm install
npm run dev
```

La aplicación estará en `http://localhost:5173`

### Con Docker (Opcional)

```bash
docker-compose up
```

---

## 🧪 Testing

### Correr todos los tests
```bash
mvn test
```

### Ejecutar solo tests unitarios
```bash
mvn test -Dtest=*Service,*Controller
```

**Cobertura actual:**
- Servicios: ~100%
- Controladores: ~100%
- Flujo completo de usuario: Testeado en integración

---

## 📖 API Documentation

### Swagger UI
Una vez corriendo el backend, accedé a:
```
http://localhost:8080/swagger-ui.html
```

### Endpoints Principales
#### Publicos 
````
POST   /api/v1/publico/usuario/register       - Crear cuenta
POST   /api/v1/publico/usuario/login          - Iniciar sesión
````
#### Autenticación
```
POST   /api/v1/autenticacion/usuario/refresh        - Renovar token
POST   /api/v1/autenticacion/usuario/logout         - Cerrar sesión
POST   /api/v1/autenticacion/usuario/contraseña     - Actualizar contraseña
GET    /api/v1/autenticacion/usuario/me             - Obtener información del usuario
GET   /api/v1/autenticacion/usuario/progreso        - Obtener el progreso del usuario. 
```

#### Analisis
```
POST     /api/v1/autenticacion/libros/analizar      - Analizar notas
```

#### Tarjetas
```
GET    /api/v1/autenticacion/tarjetas/obtener      - Listar tarjetas
```

#### Repasos (Estudiar)
```
POST   /api/v1/autenticacion/repaso/iniciar        - Comenzar sesión de estudio
POST   /api/v1/autenticacion/repaso/sesion/responder      - Responder tarjeta (1-5)
GET    /api/v1/autenticacion/repaso/sesion/finalizar        - Finalizar sesión de estudio
```

**Todas las respuestas incluyen validación y manejo de errores global.**

---

## 🏗️ Arquitectura

### Estructura del Proyecto

```
recordario-backend/
├── src/main/java/com/recordario/
│   ├── usuarios/              # Entidad Usuario
│   │   ├── Usuario.java
│   │   ├── UsuarioRepository.java
│   │   ├── UsuarioService.java
│   │   └── UsuarioController.java
│   ├── libros/                # Entidad Libro
│   ├── tarjetas/              # Entidad Tarjeta
│   ├── repasos/               # Entidad Repaso (SM-2)
│   ├── analisis/              # Análisis y estadísticas
│   ├── cartas/                # Cartas (relación tarjeta-repaso)
│   ├── compartido/
│   │   ├── enums/
│   │   └── utilidades/
│   ├── config/
│   │   ├──seguridad/
│   │   │   ├──JwtAthenticationEntryPoint.java
│   │   ├── CorsConfig.java
│   │   ├── JwtFilter.java
│   │   └── SecurityConfig.java
│   └── excepciones/           # Manejo de errores global
├── src/test/java/             # Tests unitarios e integración
├── application.properties      # Config general
└── pom.xml
```

### Decisiones Arquitectónicas

**Por entidad:** Cada entidad es autocontenida (repositorio, servicio, controlador)
- ✅ Escalable: Fácil agregar nuevas entidades
- ✅ Testeable: Cada módulo se prueba independientemente
- ✅ Mantenible: Cambios aislados por dominio

**Inyección por constructor:** Explícita y testeable
- Fácil de mockear en tests
- Dependencias claras en cada clase

---

## 🔐 Seguridad

### JWT Implementation
- **Firma HS256** con clave privada en variables de entorno
- **Refresh token en cookies:** Separado del access token para mayor seguridad
- **Tiempo limitado:** 15 min (access) + 7 días (refresh)

### Contraseñas
- Hasheadas con BCrypt
- Nunca enviadas en respuestas de API

### Cookies Seguras
```java
HttpOnly: true   // No accessible desde JavaScript
Secure: true     // Solo HTTPS en producción
SameSite: None   // Cross-site requests (CORS configurado)
```

### Validación de Entrada
- `@NotNull`, `@Email`, `@Size` en todos los DTOs
- Errores globales devueltos en formato consistente

---

## 📊 Flujo Completo de Usuario

```
1. REGISTRO
   Usuario registra cuenta → Contraseña hasheada → JWT generado

2. ESTUDIO
   Login → Access token (15min) + Refresh token (7 días)
   
3. CREAR CONTENIDO
   Crea libro → Agrega tarjetas con preguntas/respuestas
   
4. ESTUDIAR (SM-2)
   Sistema calcula qué revisar → Usuario responde (1-5)
   → Algoritmo ajusta frecuencia según desempeño
   
5. LOGOUT
   Tokens invalidados → Nueva autenticación requerida
```

---

## 🚀 Deployment

### Backend (Render)
```
Variables de entorno configuradas en Render
Base de datos PostgreSQL en Render
Conectado a: https://recordario-backend.onrender.com
```

### Frontend (Vercel)
```
Deployado automáticamente desde GitHub
URL: https://recordario-frontend.vercel.app
```

---

## 🎓 Lo que Aprendí

### Algoritmos
- Implementación del SM-2 (Spaced Repetition Algorithm)
- Cálculos matemáticos para optimizar revisiones

### Seguridad
- JWT: Generación, validación, refresh tokens
- Hashing de contraseñas con BCrypt
- Transmisión segura de datos y por qué no mandar datos sensibles en tokens

### DevOps
- Contenerización con Docker
- Por qué Docker > Máquinas virtuales (efficencia, portabilidad)
- Deployment en plataformas cloud (Render, Vercel)

### Testing
- Tests unitarios aislados con Mockito
- Tests de integración del flujo completo
- Uso de H2 para tests sin afectar BD real

---

## 🔮 Mejoras Futuras

- [ ] **Token Blacklist:** Invalidar tokens expirados para mayor seguridad
- [ ] **Análisis Avanzado:** Gráficos de progreso y estadísticas detalladas
- [ ] **Generación de Preguntas:** Usar IA para generar tarjetas automáticamente
- [ ] **Sincronización:** Estudiar en múltiples dispositivos
- [ ] **Export/Import:** Descargar/subir tarjetas en formato estándar
- [ ] **Modo colaborativo:** Compartir libros con otros usuarios

---

## 🤝 Contribuciones

Este es un proyecto personal creado como portafolio. Sin embargo, sugerencias y feedback son bienvenidos.

---

## 📄 Licencia

MIT License - Ver LICENSE.md

---

## 📧 Contacto

Para preguntas o feedback sobre el proyecto, contactame en LinkedIn o GitHub.

---

## 🎯 En Resumen

**Recordario demuestra:**
- ✅ Autenticación segura con JWT + Refresh tokens
- ✅ Testing riguroso (unitarios + integración)
- ✅ API documentada y validada
- ✅ Arquitectura escalable por entidades
- ✅ Deployment en producción
- ✅ Decisiones técnicas informadas

Es un proyecto **completo y profesional** que muestra conocimientos reales de backend seguro y testeado.
