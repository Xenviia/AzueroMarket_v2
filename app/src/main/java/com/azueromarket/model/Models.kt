package com.azueromarket.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ─────────────────────────────────────────
//  ENUMS
// ─────────────────────────────────────────

enum class TipoUsuario { EMPRENDEDOR, CLIENTE }

enum class CategoriaProducto(val nombreMostrar: String, val emoji: String) {
    ARTESANIAS("Artesanías", "🧶"),
    ALIMENTOS("Alimentos Típicos", "🌽"),
    TEXTILES("Tejidos y Textiles", "🪡"),
    CERAMICA("Cerámica", "🏺"),
    MIEL_DULCES("Miel y Dulces", "🍯"),
    QUESOS_EMBUTIDOS("Quesos y Embutidos", "🧀"),
    AGRICULTURA("Agricultura", "🌱"),
    OTROS("Otros", "📦")
}

enum class EstadoPedido(val label: String) {
    PENDIENTE("Pendiente"),
    CONFIRMADO("Confirmado"),
    EN_PROCESO("En proceso"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado")
}

// ─────────────────────────────────────────
//  USUARIO
// ─────────────────────────────────────────

@Parcelize
data class Usuario(
    val id: String = "",
    val nombre: String = "",
    val email: String = "",
    val tipoUsuario: TipoUsuario = TipoUsuario.CLIENTE,
    val telefono: String = "",
    val ubicacion: String = "",
    val descripcion: String = ""
) : Parcelable

// ─────────────────────────────────────────
//  PRODUCTO
// ─────────────────────────────────────────

@Parcelize
data class Producto(
    val id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val categoria: CategoriaProducto = CategoriaProducto.OTROS,
    val emprendedorId: String = "",
    val emprendedorNombre: String = "",
    val emprendedorTelefono: String = "",
    val ubicacion: String = "",
    val imagenUrl: String = "",
    val stock: Int = 0,           // 0 = sin stock / consultar
    val disponible: Boolean = true
) : Parcelable

// ─────────────────────────────────────────
//  CARRITO
// ─────────────────────────────────────────

@Parcelize
data class ItemCarrito(
    val producto: Producto,
    var cantidad: Int = 1
) : Parcelable {
    val subtotal: Double get() = producto.precio * cantidad
}

data class ResumenCarrito(
    val items: List<ItemCarrito>,
    val subtotal: Double,
    val cargoServicio: Double,   // 5% al cliente
    val itbms: Double,           // 7% al cliente
    val total: Double
)

// ─────────────────────────────────────────
//  PEDIDO / ORDEN
// ─────────────────────────────────────────

data class Pedido(
    val id: String = "",
    val clienteId: String = "",
    val clienteNombre: String = "",
    val emprendedorId: String = "",
    val emprendedorNombre: String = "",
    val items: List<ItemCarrito> = emptyList(),
    val subtotal: Double = 0.0,
    val cargoServicio: Double = 0.0,
    val itbms: Double = 0.0,
    val total: Double = 0.0,
    val estado: EstadoPedido = EstadoPedido.PENDIENTE,
    val timestamp: Long = System.currentTimeMillis(),
    val nota: String = ""
)

// ─────────────────────────────────────────
//  CHAT / MENSAJES
// ─────────────────────────────────────────

data class Conversacion(
    val id: String = "",
    val otroUsuarioId: String = "",
    val otroUsuarioNombre: String = "",
    val ultimoMensaje: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val noLeidos: Int = 0,
    val productoContextoId: String = "",
    val productoContextoNombre: String = ""
)

data class Mensaje(
    val id: String = "",
    val conversacionId: String = "",
    val remitenteId: String = "",
    val contenido: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val esMio: Boolean = false,
    val tipoMensaje: TipoMensaje = TipoMensaje.TEXTO
)

enum class TipoMensaje { TEXTO, PRODUCTO_COMPARTIDO, SOLICITUD_PEDIDO }
