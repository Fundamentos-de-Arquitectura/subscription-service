# Subscription Service - FoodFlow

## Descripción general
El módulo Subscription gestiona los planes y suscripciones de usuarios dentro de la plataforma FoodFlow.  
Incluye el flujo completo de alta, cancelación y consulta de suscripciones, así como la integración con un cliente de pago externo (actualmente preparado para Izipay).

Desarrollado con Spring Boot 3, Java 21 y arquitectura basada en Domain-Driven Design (DDD).

## Arquitectura

### Estructura de paquetes
subscription/
 ├── application/
 │   ├── dto/
 │   │   ├── PaymentData.java
 │   │   └── PaymentResult.java
 │   └── service/
 │       └── SubscriptionService.java
 ├── domain/
 │   ├── model/
 │   │   ├── Subscription.java
 │   │   ├── SubscriptionPlan.java
 │   │   ├── SubscriptionStatus.java
 │   │   └── BillingPeriod.java
 │   ├── repository/
 │   │   ├── SubscriptionRepository.java
 │   │   └── PlanRepository.java
 │   └── service/
 │       └── PaymentClient.java
 ├── infrastructure/
 │   ├── persistence/
 │   │   └── JPA repositories
 │   └── payment/
 │       └── IzipayPaymentClient.java
 └── interfaces/
     └── controller/
         └── SubscriptionController.java

### Capas
- Domain: Entidades, enumeraciones y contratos (PaymentClient).
- Application: Casos de uso y lógica de orquestación (SubscriptionService).
- Infrastructure: Implementaciones concretas (persistencia y pasarelas de pago).
- Interfaces: Controladores REST expuestos al cliente o frontend.

## Entidades principales

### SubscriptionPlan
Representa un plan de suscripción disponible (Básico, Premium, Enterprise).  
Atributos principales:
- price
- billingPeriod (mensual, trimestral, anual)
- description
- active

### Subscription
Representa una suscripción activa o pasada de un usuario.  
Atributos:
- userId
- plan
- status (PENDING_PAYMENT, ACTIVE, CANCELLED, EXPIRED)
- startDate, endDate
- paymentTransactionId

### PaymentClient
Interfaz que define cómo el dominio espera procesar pagos.  
Permite integrar cualquier pasarela sin modificar el núcleo del sistema.

## Casos de uso

| Operación | Descripción | Endpoint | Método |
|------------|--------------|----------|--------|
| Listar planes | Retorna todos los planes activos | /api/v1/subscriptions/plans | GET |
| Suscribir usuario | Procesa un pago y activa una suscripción | /api/v1/subscriptions/subscribe/{userId}?planName={name} | POST |
| Consultar suscripción | Muestra la última suscripción del usuario (activa o cancelada) | /api/v1/subscriptions/{userId} | GET |
| Cancelar suscripción | Cambia estado a CANCELLED | /api/v1/subscriptions/cancel/{userId} | DELETE |
| Historial | Lista todas las suscripciones del usuario | /api/v1/subscriptions/history/{userId} | GET |

## Integración con pagos

### Contrato
```java
public interface PaymentClient {
    PaymentResult processPayment(PaymentData data, BigDecimal amount);
}
```

## Pruebas locales

### Swagger
URL: http://localhost:8080/swagger-ui/index.html  
Configuración en application.properties:
```
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### Ejemplos con Postman

Suscribir usuario:
```
POST http://localhost:8080/api/v1/subscriptions/subscribe/1?planName=BASIC
Content-Type: application/json

{
  "cardNumber": "4111111111111111",
  "cardHolder": "Juan Perez",
  "expirationDate": "12/27",
  "cvv": "123"
}
```

Cancelar suscripción:
```
DELETE http://localhost:8080/api/v1/subscriptions/cancel/1
```

## Próximos pasos
- Conectar IzipayPaymentClient con API REST real (sandbox).
- Desplegar backend en entorno HTTPS.
- Integrar frontend con tokenización de tarjetas (Izipay JS SDK).
- Añadir autenticación JWT al módulo de suscripciones.

## Estado actual
- Servicio completo y funcional localmente  
- Arquitectura DDD aplicada  
- En espera de integración real con Izipay
