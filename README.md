# 📌 Taller II - Loan Validation Service

Microservicio desarrollado en el **NTTDATA Bootcamp Microservicios – Taller 02**.  
Este servicio valida solicitudes de préstamos aplicando reglas de negocio definidas y expone un endpoint `POST /loan-validations`.

---

## 🛠️ Tecnologías utilizadas
- **Java 17** (compatible con Java 11)
- **Spring Boot 3 (WebFlux)**
- **Maven** como manejador de dependencias
- **Lombok** para reducir código boilerplate
- **JUnit 5 + Mockito + Reactor Test** para pruebas unitarias reactivas
- **JaCoCo** para cobertura de pruebas
- **Checkstyle** para control de calidad de código
- **OpenAPI 3.0.3** como contrato contract-first

---

## 📋 Funcionalidad del sistema

El microservicio valida solicitudes de préstamos bajo las siguientes reglas:

- **R1 – Antigüedad de préstamos:**  
  El solicitante no debe tener préstamos en los últimos **3 meses** (inclusive).  
  _(Se usa `Clock` inyectable para pruebas)._

- **R2 – Plazo máximo:**  
  `termMonths` debe estar en el rango **1 ≤ termMonths ≤ 36**.

- **R3 – Capacidad de pago:**  
  `monthlyPayment = requestedAmount / termMonths`  
  Debe cumplirse:  
  `monthlyPayment ≤ 0.40 * monthlySalary`

- **R4 – Datos válidos:**  
  `monthlySalary > 0` y `requestedAmount > 0`.

👉 Si **alguna regla falla** → `eligible = false` + lista de razones.  
👉 Si **todas pasan** → `eligible = true`.

---

## 🔗 Endpoint

### `POST /loan-validations`

#### Request Body (JSON)
```json
{
  "monthlySalary": 2500,
  "requestedAmount": 6000,
  "termMonths": 24,
  "lastLoanDate": "2025-04-01"
}

````

## Ejecución del microservicio

1. Compilar y empaquetar: `mvn clean install`
2. Ejecutar la aplicación: `mvn spring-boot:run`
3. Probar con Postman: `http://localhost:8080/loan-validations` 
## Ejecución de pruebas
`mvn clean test`

- Reporte JaCoCo: Se genera en target/site/jacoco/index.html
- Checkstyle: mvn checkstyle:check

### OpenAPI Specification

El contrato está definido en src/main/resources/openapi.yaml.
Para visualizar la documentación con Swagger UI, ejecuta la aplicación y accede a:

👉 http://localhost:8080/swagger-ui.html

## Diagramas UML

### 1. Diagrama de Secuencia

### 2. Diagrama de componentes

## Autores

1. Anaís Bustamante
2. Yesenia Peche
