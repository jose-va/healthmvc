# Práctica final healthmvc
## Explicación de seguridad

Spring Security va a controlar seguridad de la aplicación, se encargará de autenticar usuarios, autorizar acciones, controlar qué podemos hacer, 
restringir el acceso a páginas y URLs  de nuestra app y manejo de sesiones y tokens

### Componentes Principales

#### SecurityConfig
En esta clase de configuración se define:
* URLs públicas.
* URLs protegidas.
* Qué roles pueden acceder a cada URL.
* Funcionamiento del login y logout.
* JWT

#### Usuarios y Roles 
Usuarios del sistema. Se usta información para autenticar al usuario y decidir a qué partes de la aplicación tiene acceso permitido. 
* Nombre del usuario.
* Contraseña encriptada.
* Rol de paciente o médico.

#### UserDetails
Se trata de una interfaz de Spring Security que representa a un usuario autenticado. 
* Usuario.
* Contraseña.
* Roles.
* Estado.

#### CustomUserDetailsService
Encargada de cargar los usuarios.

#### Codificador de Contraseñas
Encriptar contraseñas antes de guardarlas en la base de datos y comprobarlas  en el momento del login.

---

### JWT (Autenticación por Token)

Se suele utilizar JWT (JSON Web Tokens) en lugar de sesiones tradicionales para una mayor seguridad. Está formado por: 

| Componente | Responsabilidad |
| :--- | :--- |
| **JwtUtil** | Es una clase de utilidad (no toma decisiones). Se encarga de generar tokens JWT, validarlos y extraer la información que contienen. |
| **Filtro JWT (`JwtFilter`)** | Es el vigilante que revisa cada petición. Se ejecuta antes del controlador, comprueba si la petición lleva un token, lo valida y autentica automáticamente al usuario si es correcto. |

#### Flujo JWT

1. El usuario hace login con sus credenciales.
2. El servidor valida la información y genera un token.
3. El token se guarda en el cliente.
4. El cliente envía el token adjunto en cada petición.
5. El filtro intercepta la petición y valida el token.
6. Spring Security reconoce al usuario y permite o deniega el acceso.
