package com.azueromarket.utils

import com.azueromarket.AzueroMarketApp
import com.azueromarket.model.ItemCarrito
import com.azueromarket.model.Producto
import com.azueromarket.model.ResumenCarrito

object CarritoManager {

    private val items = mutableListOf<ItemCarrito>()

    fun agregarProducto(producto: Producto, cantidad: Int = 1) {
        val existente = items.find { it.producto.id == producto.id }
        if (existente != null) {
            existente.cantidad += cantidad
        } else {
            items.add(ItemCarrito(producto, cantidad))
        }
    }

    fun quitarProducto(productoId: String) {
        items.removeAll { it.producto.id == productoId }
    }

    fun actualizarCantidad(productoId: String, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) {
            quitarProducto(productoId)
            return
        }
        items.find { it.producto.id == productoId }?.let { it.cantidad = nuevaCantidad }
    }

    fun getItems(): List<ItemCarrito> = items.toList()

    fun getConteo(): Int = items.sumOf { it.cantidad }

    fun estaEnCarrito(productoId: String) = items.any { it.producto.id == productoId }

    fun calcularResumen(): ResumenCarrito {
        val subtotal      = items.sumOf { it.subtotal }
        val cargoServicio = subtotal * AzueroMarketApp.CARGO_SERVICIO_PORCENT
        val baseItbms     = subtotal + cargoServicio
        val itbms         = baseItbms * AzueroMarketApp.ITBMS_PORCENT
        val total         = subtotal + cargoServicio + itbms
        return ResumenCarrito(items.toList(), subtotal, cargoServicio, itbms, total)
    }

    fun limpiarCarrito() = items.clear()

    fun isEmpty() = items.isEmpty()
}
