# 🌾 AzueroMarket v2.0 — Guía de Setup

## Qué hay en esta versión

| Módulo | Descripción |
|---|---|
| **Login / Registro** | Toggle Cliente ↔ Emprendedor, validaciones completas |
| **Home Cliente** | Catálogo en grid, filtros por categoría, badge carrito en nav |
| **Detalle de Producto** | Stock en tiempo real, cargos desglosados, productos relacionados del mismo productor |
| **Carrito** | Control de cantidad, ITBMS 7% + cargo servicio 5% al cliente, confirmación de pedido |
| **Chat** | Lista de conversaciones, mensajes burbuja izq/der, contexto del producto consultado |
| **Home Emprendedor** | Stats (productos, ventas), gestión de productos con stock, nav a mensajes |
| **Modelo de negocio** | El productor recibe el 100% del precio. Los cargos (5% + ITBMS 7%) los paga el cliente |

---

## 1. Abrir el proyecto en Android Studio

1. Descomprime `AzueroMarket_v2.zip`
2. **File → Open** → selecciona la carpeta `AzueroMarket_v2`
3. Espera que Gradle sincronice (puede tardar 2-3 min la primera vez)
4. Ejecuta ▶️ en un emulador API 24+

**Cuentas de prueba:**
```
Emprendedor:  emprendedor@test.com  /  123456
Cliente:      cliente@test.com      /  123456
```

---

## 2. Conectar Supabase (base de datos real)

### Paso 1: Crear cuenta y proyecto
1. Ve a [supabase.com](https://supabase.com) → **Start your project** (gratis)
2. Crea un nuevo proyecto (elige región US East para menos latencia desde Panamá)
3. Guarda la contraseña de la base de datos

### Paso 2: Crear las tablas
1. En el dashboard de Supabase → **SQL Editor** → **New Query**
2. Copia y pega todo el contenido de `SUPABASE_SETUP.sql`
3. Haz clic en **Run**

### Paso 3: Obtener las credenciales
1. **Project Settings** → **API**
2. Copia:
   - **Project URL**: `https://xxxx.supabase.co`
   - **anon / public key**: la clave larga

### Paso 4: Pegar credenciales en la app
Abre `AzueroMarketApp.kt` y reemplaza:
```kotlin
const val SUPABASE_URL   = "https://TU_PROYECTO.supabase.co"
const val SUPABASE_ANON_KEY = "TU_ANON_KEY"
```

### Paso 5: Habilitar Realtime (para chat en vivo)
1. Supabase Dashboard → **Database** → **Replication**
2. En "Supabase Realtime" activa las tablas: `mensajes`, `conversaciones`, `pedidos`

---

## 3. Arquitectura de cargos (modelo de negocio)

```
Precio del producto:    $ 10.00   ← el productor recibe ESTO
+ Cargo de servicio 5%: $  0.50   ← AzueroMarket
+ ITBMS 7%:             $  0.73   ← obligatorio por ley (aplica a subtotal + cargo)
─────────────────────────────────
Total que paga cliente: $ 11.23
```

Los valores están en `AzueroMarketApp.kt`:
```kotlin
const val CARGO_SERVICIO_PORCENT = 0.05   // Cambiar según negocio
const val ITBMS_PORCENT          = 0.07   // No cambiar (ley panameña)
```

---

## 4. Flujo completo del usuario

### Cliente
```
Login → Home (catálogo) → Detalle Producto
           ├── Stock disponible → Agregar Carrito → Confirmar Pedido
           └── Sin stock        → Chat con productor
Mensajes → Lista conversaciones → Chat individual
```

### Emprendedor
```
Login → Mi Tienda (lista productos) → Agregar Producto (con stock)
Mensajes → Ver consultas de clientes → Responder
```

---

## 5. Próximos pasos sugeridos

| Prioridad | Feature | Complejidad |
|---|---|---|
| 🔴 Alta | Reemplazar MockDataSource con llamadas Supabase reales | Media |
| 🔴 Alta | Chat en tiempo real con Supabase Realtime | Media |
| 🟡 Media | Subida de fotos de productos (Supabase Storage) | Media |
| 🟡 Media | Notificaciones push (Firebase FCM) cuando llega un pedido | Alta |
| 🟡 Media | Historial de pedidos para cliente y emprendedor | Baja |
| 🟢 Baja | Búsqueda de texto libre en productos | Baja |
| 🟢 Baja | Calificaciones y reseñas de productos | Media |
| 🟢 Baja | Integración Yappy/PayPal para pagos | Alta |

---

## 6. Estructura del proyecto

```
app/src/main/java/com/azueromarket/
├── AzueroMarketApp.kt          ← Config Supabase + tasas
├── model/
│   └── Models.kt               ← Todos los data class
├── utils/
│   ├── SessionManager.kt       ← SharedPreferences sesión
│   ├── MockDataSource.kt       ← Datos locales (reemplazar con Supabase)
│   └── CarritoManager.kt       ← Estado del carrito en memoria
└── ui/
    ├── login/LoginActivity.kt
    ├── register/RegisterActivity.kt
    ├── home/
    │   ├── HomeClienteActivity.kt
    │   └── HomeEmprendedorActivity.kt
    ├── producto/DetalleProductoActivity.kt
    ├── carrito/
    │   ├── CarritoActivity.kt
    │   └── CarritoAdapter.kt
    ├── chat/
    │   ├── ChatActivity.kt
    │   ├── ChatAdapter.kt
    │   ├── ConversacionesActivity.kt
    │   └── ConversacionAdapter.kt
    ├── cliente/
    │   ├── ProductoClienteAdapter.kt
    │   └── ProductoRelacionadoAdapter.kt
    └── emprendedor/
        ├── ProductoEmprendedorAdapter.kt
        └── AgregarProductoDialog.kt
```
