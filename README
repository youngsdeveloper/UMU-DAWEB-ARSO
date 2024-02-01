![logo](./docs/images/logo.svg) 

# Proyecto DaWEB + Arso (pideREST)

pideREST es una web de gestión de restaurantes, programada en el contexto de las asignaturas "Desarrollo de Aplicaciones WEB" y "Arquitecturas del Software".

Casa asignatura se centra en una de las partes de la aplicación.

- DaWeb (Frontend)
- ArSo (Backend)

La aplicación simular un sistema de pedir comida a domicilio, al estilo JustEat o Glovo.

Al restaurante le permite gestionar su información básica, platos disponibles, incidencias...

Al cliente le permite realizar pedidos, enviar valoraciones, incidencias, etc.

![screenshot](./docs/images/01.jpeg) 


# Arquitectura del Sistema
![arquitectura](./docs/images/arquitectura.png) 

En este diagrama se puede consultar el escenario de la arquitectura del sistema utilizada para el
proyecto, separada principalmente en dos grandes bloques, el que corresponde a la asignatura de
ArSo y la otra que corresponde a DaWEB.
En ArSo, principalmente destacan dos servicios:
- Servicio Restaurantes: Gestionar restaurantes, platos y sitios turísticos cercanos a
restaurantes. Esta parte se almacena en la instancia de MongoDB de Atlas.

- Servicio Opiniones: Gestionar restaurantes, platos y sitios turísticos cercanos a
restaurantes. Este servicio almacena la información en una instancia de MongoDB local
que habrá que iniciar con Docker.

La comunicación entre Restaurantes y Opiniones es síncrona (mediante llamadas a la API REST)
y la comunicación entre Opiniones y Restaurantes es asíncrona a través de Rabbit MQ.

Nota: Hemos detectado problemas con Rabbit MQ debido a su límite de conexiones por la versión
gratuita, esto puede hacer que el modelo no se actualice siempre con la media de valoraciones
actual.

Existe una pasarela de la API que unifica estos dos servicios y además añade la capa de
autentificación mediante JWT.

En cuanto a la autentificación, cómo todos los servicios se ejecutan bajo localhost, la pasarela API
guarda en una cookie el token JWT (cookie con nombre ’jwt’) y por tanto puede ser accedida por
el resto de servicios.


En cuanto a la parte de DaWEB destacan principalmente otros dos bloques:
- Express + HBS: En esta parte se encuentran algunas de las funcionalidades de la
aplicación web. Además, todas las peticiones a la API desde DaWEB se han centralizado
aquí.

- React.JS: Aquí se ha implementado otra parte de las funcionalidades, en este caso no se
comunica con la pasarela de la API (ArSO) sino que consume la información haciendo
llamadas a Express+HBS. Por temas de seguridad, en esta parte no se tiene acceso al
token JWT, así que se hacen llamadas con fetch() usando la opción {credentials:
'include'} además se ha configurado en Express los CORS para permitir acceso desde
este servicio. En React.js además se hace uso de LocalStorage para guardar el usuario
actual. En caso de que las peticiones a Express devuelvan un 401, limpia el localStorage y
redirige al login.


Durante toda la aplicación web, se va redirigiendo de un servicio a otro según quien implemente la
funcionalidad en concreto.

Se ha creado una instancia local de MySQL de incidencias que habrá que desplegar con Docker
(incluyendo un servicio de phpMyAdmin)

# Despliegue


El proyecto se compone de los siguientes directorios.

| Directorio | Descripción |
| ------ | ------ |
| /proyecto-arso | Directorio principal ArSO |
| /proyecto-daweb | Directorio principal DaWEB|
| /proyecto-daweb/react-daweb  | Aplicación React |
| /proyecto-daweb/express-daweb  | Aplicación Express |


Para desplegar el proyecto tendremos que tener preparados los archivos con todo el código.

Comencemos con la parte de ArSo. Entramos en el directorio de ArSo y ejecutamos
docker-compose.

```console
alumno@um: ~/$ cd proyecto-arso
alumno@um: ~/proyecto-arso$ docker-compose up --build
```

De esta forma se han iniciado todos los servicios necesarios para ArSo.

Volvamos ahora a la carpeta de DaWEB e iniciamos el servicio de MySQL.

```console
alumno@um: ~/$ cd proyecto-daweb
alumno@um: ~/proyecto-daweb$ docker-compose up --build
```

Haciendo esto ya habremos lanzado el servicio de MySQL. También se ha creado un servicio de phpMyAdmin para gestionar rápidamente la base de datos.

Entramos al servicio de phpMyAdmin usando http://localhost:9000

Las credenciales de acceso a MySQL son las siguientes:

| Servidor | Usuario | Contraseña
| ------ | ------ | ------
| db| daweb | daweb 

En MySQL deberá estar creada la base de datos “daweb”, entramos en ella y debemos importar la
tabla incidencias.

Usa el fichero “daweb.sql” para importar la tabla de incidencias.

Ahora, iniciamos el servicio de Express


```console
alumno@um: ~/proyecto-daweb$ cd express-daweb
alumno@um: ~/proyecto-daweb/express-daweb$ npm run start
```

Por último, iniciamos el servicio de React.js

```console
alumno@um: ~/proyecto-daweb$ cd react-daweb
alumno@um: ~/proyecto-daweb/react-daweb$ npm start
```

De esta forma, ya podremos utilizar los servicios, se podrá acceder a la aplicaciones desde las
siguientes URLs

| Express | React.js
| ------ | ------
| http://localhost:3000/| http://localhost:3001/  


# Servicios (Backend)


Se han integrado en Docker de los siguientes servicios:


| Servicio | Puerto
| ------ | ------
| Restaurantes-REST | 8080
| Opiniones-REST | 5000
| Opiniones-GraphQL | 8070
| Pasarela API | 8090
| MongoDB | -
| MongoExpress | 8081

Se ha utilizado la herramienta Docker Compose para integrar todos estos servicios.

Para cada servicio correspondiente al desarrollo del proyecto se ha creado un Dockerfile que lo
configura y estos son llamados desde Docker Compose.

Se han creado dos redes: arso y mongo-opiniones:

- arso: red principal para los servicios que necesitan conexión entre ellos (por ejemplo, la
pasarela o restaurantes->opiniones)
- mongo-opiniones: red exclusiva para la instancia local de MongoDB y el servicio opiniones

Además, se ha creado un volúmen para MongoDB con el objetivo de asegurar la persistencia de
datos.

Se ha modificado el proyecto para incluir variables de entorno, de manera que las URL de
MongoDB son configurables para todos los proyectos.

También ha sido necesario usar una variable de entorno para pasarle la URL del servicio
opiniones a restaurantes-rest para poder realizar la comunicación sincrona entre restaurantes y
opiniones.

# Pasarela

En cuanto a la pasarela, se ha utilizado Zuul (Netflix)

Se ha implementado un inicio de sesión OAuth 2.0 a través de GitHub, la pasarela, si no tiene una sesión activa re-dirigue al inicio de sesión, si tiene sesión activa simplemente redirecciona a la API correspondiente.

La gestión de las sesión se hace a través de tokens JWT almacenadas en cookies del navegador.

