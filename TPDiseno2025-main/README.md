# Como ejecutar el proyecto
Se debe utilizar un Docker
Y se deben ejecutar los siguientes comandos en estos pasos, para esto se necesitan dos terminales.
Terminal 1:
- cd .\TPDiseno2025-main\TPDiseño2025\
- docker compose up -d (Ejecuta la seed y permite el funcionamiento de la base de datos)
- mvn spring-boot:run
Terminal 2 (abrir nueva terminal):
- cd .\TPDiseno2025-main\frontend_next\
- npm install
- nom run dev

# Como correr pruebas unitarias

Se deben ejecutar los siguientes pasos:

- cd .\TPDiseno2025-main\TPDiseño2025\
- mvn clean test

Luego, dentro de src los siguientes pasos:
src -> target -> site -> Copy Path de index.html

Copiar link en el navegador y buscar la clase Testeada para ver la cobertura

# Endpoints para cada CU
Para CU4:
GET /tiposhabitacion, GET /reservas/disponibilidad, GET /huespedes/getByDni, POST /reservas/crear

Para CU5:
GET /tiposhabitacion, GET /reservas/disponibilidad

Para CU6:
GET /reservas/apellido, DELETE /reservas/{id}

Para CU11:
GET /huespedes/getByDni, PUT /huespedes/{tipoDni}/{dni}, DELETE /huespedes/{tipoDni}/{dni}, 