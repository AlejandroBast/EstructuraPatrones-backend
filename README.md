# EstructuraPatrones Backend

Sistema de autenticación y gestión financiera desarrollado con **Spring Boot 3.3.4** y **Java 17**, implementando múltiples patrones de diseño de software.

## 📋 Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Patrones de Diseño Implementados](#patrones-de-diseño-implementados)
  - [Patrones Creacionales](#patrones-creacionales)
  - [Patrones Estructurales](#patrones-estructurales)
  - [Patrones de Comportamiento](#patrones-de-comportamiento)
- [Estructuras de Datos](#estructuras-de-datos)
- [Tecnologías](#tecnologías)
- [Instalación](#instalación)

---

## 📖 Descripción General

Este proyecto es un backend completo que integra:
- **Módulo de Autenticación**: Registro y login con JWT o Supabase
- **Módulo Financiero**: Gestión de ingresos, gastos y microexpensas con recomendaciones basadas en IA

El sistema demuestra la aplicación práctica de patrones de diseño GoF (Gang of Four) y patrones arquitectónicos modernos.

---

## 🏗️ Patrones de Diseño Implementados

### Patrones Creacionales

#### 1. **Singleton** 
*Garantiza que una clase tenga una única instancia y proporciona un punto de acceso global.*

**Ubicación**: `com.example.auth.datastruct.UserStoreSingleton`

**Implementación**:
```java
public class UserStoreSingleton {
  private static volatile UserStoreSingleton INSTANCE;
  
  private UserStoreSingleton() {}
  
  public static UserStoreSingleton getInstance() {
    if (INSTANCE == null) {
      synchronized (UserStoreSingleton.class) {
        if (INSTANCE == null) {
          INSTANCE = new UserStoreSingleton();
        }
      }
    }
    return INSTANCE;
  }
}
```

**Propósito**: Mantener un almacén centralizado de usuarios en memoria, asegurando que todos los componentes accedan a la misma instancia.

**Ventajas**:
- ✅ Punto único de acceso a datos de usuarios
- ✅ Thread-safe mediante doble verificación de bloqueo
- ✅ Inicialización perezosa (lazy initialization)

---

#### 2. **Factory Method** 
*Define una interfaz para crear objetos, permitiendo a las subclases decidir qué clase instanciar.*

**Ubicación**: `com.example.auth.factory.AuthServiceFactory` y `JwtAuthServiceFactory`

**Implementación**:
```java
// Clase abstracta
public abstract class AuthServiceFactory {
  public abstract AuthService createAuthService();
}

// Implementación concreta
public class JwtAuthServiceFactory extends AuthServiceFactory {
  private final UserStoreSingleton store;
  private final BCryptPasswordEncoder encoder;
  private final JwtUtil jwtUtil;
  
  @Override
  public AuthService createAuthService() {
    return new JwtAuthService(store, encoder, jwtUtil);
  }
}
```

**Propósito**: Crear diferentes implementaciones de servicios de autenticación (JWT local vs Supabase) sin acoplar el código cliente a clases concretas.

**Ventajas**:
- ✅ Facilita agregar nuevos tipos de autenticación
- ✅ Desacopla la creación de objetos de su uso
- ✅ Cumple con el principio Open/Closed

---

### Patrones Estructurales

#### 3. **Adapter** 
*Permite que interfaces incompatibles trabajen juntas, actuando como puente entre dos interfaces.*

**Ubicación**: `com.example.auth.service.SupabaseAuthAdapter`

**Implementación**:
```java
public class SupabaseAuthAdapter implements AuthService {
  private final SupabaseAuthService supabase;
  private final SupabaseAdminService admin;
  
  @Override
  public AuthResponse register(AuthRequestRegister req) {
    // Adapta la respuesta de Supabase al formato interno AuthResponse
    SupabaseAuthService.Result res = supabase.signupWithMeta(...);
    JsonNode root = mapper.readTree(res.getBody());
    return new AuthResponse(null, username, null);
  }
}
```

**Propósito**: Adaptar la API de Supabase para que sea compatible con la interfaz `AuthService` del sistema, permitiendo intercambiar entre autenticación local y Supabase sin cambiar el código cliente.

**Ventajas**:
- ✅ Reutiliza código existente de Supabase
- ✅ Permite cambiar entre proveedores de autenticación fácilmente
- ✅ Oculta la complejidad de la integración externa

---

#### 4. **Facade** 
*Proporciona una interfaz unificada y simplificada para un conjunto de interfaces en un subsistema.*

**Ubicación**: `com.example.auth.facade.AuthFacade` y `com.example.finance.service.FinanceFacade`

**Implementación en Auth**:
```java
public class AuthFacade {
  private final RequestQueue queue = new RequestQueue();
  private final HistoryStack history = new HistoryStack();
  private final AuthService service;
  private final SupabaseAdminService admin;
  
  public AuthResponse register(AuthRequestRegister req) {
    queue.push("register:" + req.getEmail());
    var res = service.register(req);
    history.push("registered:" + req.getEmail());
    return res;
  }
}
```

**Implementación en Finance**:
```java
public class FinanceFacade {
  private final TransactionRepository<Income> incomeRepo;
  private final TransactionRepository<Expense> expenseRepo;
  private final TransactionRepository<MicroExpense> microRepo;
  private final ExpenseNotifier notifier;
  private final DailyLimitStrategy limitStrategy;
  private final AiAdvisor aiAdvisor;
  
  public Expense addExpense(...) {
    // Coordina múltiples subsistemas
    var saved = expenseRepo.save(e);
    notifier.notifyAdded(userId, amount);
    return saved;
  }
}
```

**Propósito**: 
- **AuthFacade**: Simplifica la interacción con autenticación, repositorios, colas y historial
- **FinanceFacade**: Unifica el acceso a repositorios de transacciones, notificaciones, estrategias y servicios de IA

**Ventajas**:
- ✅ Reduce la complejidad para los clientes (Controllers)
- ✅ Desacopla el código cliente de los subsistemas
- ✅ Centraliza la lógica de coordinación

---

#### 5. **Composite** 
*Compone objetos en estructuras de árbol para representar jerarquías parte-todo. Permite tratar objetos individuales y composiciones de manera uniforme.*

**Ubicación**: `com.example.finance.patterns.composite.*`

**Implementación**:
```java
// Componente base
public interface TransactionComponent {
  BigDecimal total();
}

// Hoja (elemento individual)
public class TransactionLeaf implements TransactionComponent {
  private final BigDecimal amount;
  public BigDecimal total() { return amount; }
}

// Compuesto (grupo)
public class TransactionGroup implements TransactionComponent {
  private final List<TransactionComponent> children = new ArrayList<>();
  
  public TransactionGroup add(TransactionComponent c) {
    children.add(c);
    return this;
  }
  
  public BigDecimal total() {
    return children.stream()
      .map(TransactionComponent::total)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
```

**Ejemplo de uso**:
```java
// Crear grupo de gastos mensuales
TransactionGroup monthly = new TransactionGroup();
monthly.add(new TransactionLeaf(new BigDecimal("100"))); // Gasto individual
monthly.add(new TransactionLeaf(new BigDecimal("200"))); // Otro gasto

// Crear grupo anual que contiene grupos mensuales
TransactionGroup annual = new TransactionGroup();
annual.add(monthly);
annual.add(new TransactionLeaf(new BigDecimal("50")));

// Calcular total de manera uniforme
BigDecimal total = annual.total(); // 350
```

**Propósito**: Calcular totales de transacciones de manera jerárquica (diarias → semanales → mensuales → anuales).

**Ventajas**:
- ✅ Trata objetos individuales y grupos de manera uniforme
- ✅ Facilita agregar nuevos tipos de componentes
- ✅ Simplifica el cálculo de totales recursivos

---

#### 6. **Decorator** 
*Añade responsabilidades adicionales a un objeto dinámicamente sin alterar su estructura.*

**Ubicación**: `com.example.finance.patterns.decorator.*`

**Implementación**:
```java
// Decorador base
public abstract class RecommendationDecorator {
  protected final Recommendation base;
  
  public RecommendationDecorator(Recommendation base) {
    this.base = base;
  }
  
  public Recommendation build() {
    return base;
  }
}

// Decorador concreto
public class PotentialSavingsDecorator extends RecommendationDecorator {
  public PotentialSavingsDecorator(Recommendation base) {
    super(base);
  }
  
  public Recommendation build() {
    // Enriquece la recomendación con cálculo de ahorros potenciales
    return base;
  }
}
```

**Propósito**: Enriquecer dinámicamente las recomendaciones financieras con información adicional (ahorros potenciales, prioridad, categorías, etc.).

**Ventajas**:
- ✅ Más flexible que la herencia
- ✅ Permite combinar múltiples decoradores
- ✅ Cumple con el principio de responsabilidad única

---

### Patrones de Comportamiento

#### 7. **Strategy** 
*Define una familia de algoritmos, encapsula cada uno y los hace intercambiables.*

**Ubicación**: `com.example.finance.patterns.strategy.*`

**Implementación**:
```java
// Interfaz de estrategia
public interface DailyLimitStrategy {
  BigDecimal limitFor(String userId);
}

// Estrategia concreta
public class FixedDailyLimit implements DailyLimitStrategy {
  private final BigDecimal limit;
  
  public FixedDailyLimit(BigDecimal limit) {
    this.limit = limit;
  }
  
  public BigDecimal limitFor(String userId) {
    return limit;
  }
}
```

**Uso**:
```java
public class FinanceFacade {
  private final DailyLimitStrategy limitStrategy = 
    new FixedDailyLimit(new BigDecimal("20"));
  
  public MicroExpense addMicroExpense(...) {
    m.setDailyLimit(limitStrategy.limitFor(userId).intValue());
    return microRepo.save(m);
  }
}
```

**Propósito**: Definir diferentes estrategias para calcular límites diarios de gasto (fijo, dinámico basado en ingresos, personalizado por usuario, etc.).

**Ventajas**:
- ✅ Facilita cambiar el algoritmo en tiempo de ejecución
- ✅ Elimina condicionales complejos
- ✅ Permite agregar nuevas estrategias sin modificar código existente

**Posibles estrategias futuras**:
- `PercentageBasedLimit`: Límite basado en % de ingresos
- `UserCustomLimit`: Límite personalizado por usuario
- `AdaptiveLimit`: Límite que se ajusta según comportamiento

---

#### 8. **Observer** 
*Define una dependencia uno-a-muchos entre objetos, de modo que cuando un objeto cambia de estado, todos sus dependientes son notificados.*

**Ubicación**: `com.example.finance.patterns.observer.*`

**Implementación**:
```java
// Observador
public interface ExpenseObserver {
  void onExpenseAdded(String userId, BigDecimal amount);
}

// Sujeto
public class ExpenseNotifier {
  private final List<ExpenseObserver> observers = new ArrayList<>();
  
  public void subscribe(ExpenseObserver o) {
    observers.add(o);
  }
  
  public void notifyAdded(String userId, BigDecimal amount) {
    for (var o : observers) {
      o.onExpenseAdded(userId, amount);
    }
  }
}
```

**Uso**:
```java
public class FinanceFacade {
  private final ExpenseNotifier notifier = new ExpenseNotifier();
  
  public Expense addExpense(...) {
    var saved = expenseRepo.save(e);
    notifier.notifyAdded(userId, amount); // Notifica a todos los observadores
    return saved;
  }
}
```

**Propósito**: Notificar a múltiples componentes cuando se añade un gasto (alertas, estadísticas en tiempo real, disparadores de límites, logs, etc.).

**Ventajas**:
- ✅ Bajo acoplamiento entre sujeto y observadores
- ✅ Permite agregar/eliminar observadores dinámicamente
- ✅ Soporte para broadcast de eventos

**Casos de uso**:
- Enviar notificaciones push cuando se excede un límite
- Actualizar dashboards en tiempo real
- Registrar auditoría de transacciones
- Disparar recomendaciones automáticas

---

## 📊 Estructuras de Datos

El proyecto implementa estructuras de datos clásicas para casos de uso específicos:

### Stack (Pila)
**Ubicación**: `com.example.auth.datastruct.HistoryStack`

```java
public class HistoryStack {
  private final Stack<String> stack = new Stack<>();
  
  public synchronized void push(String info) { stack.push(info); }
  public synchronized String pop() { return stack.isEmpty() ? null : stack.pop(); }
}
```

**Uso**: Mantener un historial de acciones de autenticación (LIFO - Last In, First Out).

---

### Queue (Cola)
**Ubicación**: `com.example.auth.datastruct.RequestQueue`

```java
public class RequestQueue {
  private final Queue<String> queue = new ArrayDeque<>();
  
  public synchronized void push(String info) { queue.add(info); }
  public synchronized String poll() { return queue.poll(); }
}
```

**Uso**: Gestionar solicitudes de autenticación en orden (FIFO - First In, First Out).

---

### Tree (Árbol)
**Ubicación**: `com.example.auth.datastruct.RoleHierarchyTree`

```java
public class RoleHierarchyTree {
  public static class Node {
    private final String role;
    private final List<Node> children = new ArrayList<>();
    
    public Node addChild(String role) {
      Node n = new Node(role);
      children.add(n);
      return n;
    }
  }
  
  private final Node root = new Node("ROOT");
  
  public RoleHierarchyTree() {
    Node user = root.addChild("USER");
    user.addChild("ADMIN");
  }
}
```

**Uso**: Representar jerarquía de roles (ROOT → USER → ADMIN) para control de acceso basado en roles (RBAC).

---

## 🛠️ Tecnologías

- **Framework**: Spring Boot 3.3.4
- **Lenguaje**: Java 17
- **Seguridad**: Spring Security (BCrypt), JWT (jjwt 0.11.5)
- **Base de Datos**: Supabase (PostgreSQL) o In-Memory
- **Contenedores**: Docker + Docker Compose
- **Build Tool**: Maven
- **Testing**: JUnit

---

## 🚀 Instalación

### Prerrequisitos
- Java 17 o superior
- Maven 3.6+
- Docker (opcional, para Supabase)

### Configuración

1. **Clonar el repositorio**
```bash
git clone https://github.com/AlejandroBast/EstructuraPatrones-backend.git
cd EstructuraPatrones-backend
```

2. **Configurar variables de entorno** (opcional, para Supabase)

Editar `src/main/resources/application.yml`:
```yaml
supabase:
  url: https://tu-proyecto.supabase.co
  anon-key: tu-anon-key
  service-role-key: tu-service-role-key
```

3. **Compilar el proyecto**
```bash
./mvnw clean package
```

4. **Ejecutar la aplicación**
```bash
./mvnw spring-boot:run
```

O con Docker:
```bash
docker-compose up
```

La API estará disponible en `http://localhost:8080`

---

## 📚 Endpoints Principales

### Autenticación
- `POST /api/auth/register` - Registro de usuario
- `POST /api/auth/login` - Inicio de sesión
- `GET /api/auth/users` - Listar usuarios (admin)

### Finanzas
- `POST /api/finance/income` - Agregar ingreso
- `POST /api/finance/expense` - Agregar gasto
- `POST /api/finance/micro` - Agregar microexpensa
- `GET /api/finance/dashboard?year={y}&month={m}` - Dashboard financiero

---

## 📖 Diagrama de Patrones

```
┌─────────────────────────────────────────────────────────────┐
│                    CREACIONALES                              │
├─────────────────────────────────────────────────────────────┤
│ Singleton        → UserStoreSingleton                        │
│ Factory Method   → AuthServiceFactory → JwtAuthServiceFactory│
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                    ESTRUCTURALES                             │
├─────────────────────────────────────────────────────────────┤
│ Adapter          → SupabaseAuthAdapter                       │
│ Facade           → AuthFacade, FinanceFacade                 │
│ Composite        → TransactionComponent → Group/Leaf         │
│ Decorator        → RecommendationDecorator                   │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                  COMPORTAMIENTO                              │
├─────────────────────────────────────────────────────────────┤
│ Strategy         → DailyLimitStrategy                        │
│ Observer         → ExpenseNotifier → ExpenseObserver         │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎯 Principios SOLID Aplicados

- **Single Responsibility**: Cada clase tiene una única responsabilidad bien definida
- **Open/Closed**: Extendible mediante patrones Factory, Strategy y Decorator
- **Liskov Substitution**: Interfaces permiten intercambiar implementaciones
- **Interface Segregation**: Interfaces específicas y cohesivas
- **Dependency Inversion**: Dependencia de abstracciones, no de implementaciones concretas

---

## 👥 Contribuciones

Las contribuciones son bienvenidas. Por favor:
1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

---

## ✨ Autores

**AlejandroBast**
- GitHub: [@AlejandroBast](https://github.com/AlejandroBast)

**miikeepp**
- GitHub: [@miikeepp](https://github.com/miikeepp)

**benavides17**
- GitHub: [@benavides17](https://github.com/benavides17)

---

*Proyecto desarrollado con fines educativos para demostrar la implementación práctica de patrones de diseño en aplicaciones empresariales.*
