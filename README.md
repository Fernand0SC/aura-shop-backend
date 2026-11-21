# Aura Shop – Backend

API REST de la tienda online Aura Shop, hecha con Spring Boot. Incluye autenticación con JWT y control de acceso por roles.

## Tecnologías
- Java 21 y Spring Boot 4
- Spring Data JPA con base de datos H2 (archivo local en `./data`)
- Spring Security con JWT (jjwt)
- Swagger (springdoc-openapi) para documentar los endpoints

## Cómo ejecutarlo
1. Instalar Java 21.
2. Abrir el proyecto en IntelliJ IDEA y ejecutar `AurashopApplication`, o desde la terminal:
   ```
   .\mvnw.cmd spring-boot:run
   ```
3. La API queda en `http://localhost:8080`.
4. Swagger: `http://localhost:8080/swagger-ui.html`

Al primer arranque se crean automáticamente los usuarios de demostración, las 3 categorías y los 12 productos.

## Variables opcionales
| Variable | Descripción | Valor por defecto |
|---|---|---|
| `JWT_SECRET` | Clave para firmar los tokens (mínimo 32 caracteres) | valor de desarrollo |
| `SEED_PASSWORD` | Contraseña de los usuarios de demostración | `Aura#2026demo` |

## Usuarios de demostración
Todos usan la contraseña `Aura#2026demo`.

| Correo | Rol |
|---|---|
| fernando@duoc.cl | ADMINISTRADOR |
| editor@duoc.cl | EDITOR_PRODUCTOS |
| soporte@duoc.cl | SOPORTE |
| cliente@gmail.com | CLIENTE |

## Autenticación
1. `POST /api/auth/login` devuelve un `accessToken` (15 minutos) y un `refreshToken` (7 días).
2. Las peticiones protegidas envían el header `Authorization: Bearer <accessToken>`.
3. `POST /api/auth/refresh` entrega tokens nuevos usando el `refreshToken`.
4. En Swagger, el botón **Authorize** permite pegar el `accessToken`.

## Endpoints principales y permisos
| Recurso | Operación | Quién puede |
|---|---|---|
| `/api/productos`, `/api/categorias` | GET | Público |
| `/api/productos`, `/api/categorias` | POST, PUT, DELETE | ADMINISTRADOR, EDITOR_PRODUCTOS |
| `/api/usuarios` | GET | ADMINISTRADOR, SOPORTE |
| `/api/usuarios` | POST, PUT, DELETE | ADMINISTRADOR |
| `/api/ordenes` | POST, GET `/mias` | Usuario autenticado |
| `/api/ordenes` | GET (todas) | ADMINISTRADOR, SOPORTE |
| `/api/auth/*` | login, refresh, registro | Público (`/me` requiere token) |

## Lógica de negocio
- Al crear una orden, el servidor valida que haya stock, lo descuenta y calcula el total. El cliente nunca envía precios.
- El registro público siempre crea usuarios con rol `CLIENTE`.
- Las contraseñas se guardan con hash BCrypt.