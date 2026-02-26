# Práctica healthmvc
En esta aplicación, podremos gestionar una serie de citas médicas realizadas por los pacientes del centro de salud. Podemos registrarnos en la aplicación, y posteriormente, 
editar los datos de nuestro perfil o directamente crear una cita. Los médicos tendrán la posibilidad de ver todas las citas para una gestión de integra de las mismas.

### Ejemplo de uso
![Paciente](./assets/paciente-ejemplo.gif)

## Explicación de seguridad

Spring Security va a controlar seguridad de la aplicación; se encargará de autenticar usuarios, autorizar acciones, controlar qué podemos hacer, 
restringir el acceso a páginas y URLs de nuestra app, así como manejar sesiones y tokens. Trataremos los pacientes y los médicos como roles distintivos
para gestionar la autorización a las distintas partes de la app

### Componentes Principales

#### SecurityConfig
En esta clase de configuración se define:
* URLs públicas.
* URLs protegidas.
* Qué roles pueden acceder a cada URL.
* Funcionamiento del login y logout.
* JWT

#### Usuarios y Roles 
Autenticar al usuario y decidir en qué partes de la aplicación tendrá acceso permitido. 
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
| **JwtUtil** |Se encarga de generar tokens JWT, validarlos y extraer la información que contienen. |
| **Filtro JWT (`JwtFilter`)** | Se ejecuta antes del controlador, comprueba si la petición lleva un token, lo valida y autentica automáticamente al usuario si es correcto. |

#### Flujo JWT

1. El usuario hace login con sus credenciales.
2. El servidor valida la información y genera un token.
3. El token se guarda en el cliente.
4. El cliente envía el token adjunto en cada petición.
5. El filtro intercepta la petición y valida el token.
6. Spring Security reconoce al usuario y permite o deniega el acceso.


