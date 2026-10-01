package com.azueromarket.ui.carrito

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azueromarket.databinding.ItemCarritoBinding
import com.azueromarket.model.ItemCarrito

class CarritoAdapter(
    private val onCantidadChange: (ItemCarrito, Int) -> Unit,
    private val onEliminar: (ItemCarrito) -> Unit
) : ListAdapter<ItemCarrito, CarritoAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val b: ItemCarritoBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: ItemCarrito) {
            b.tvNombre.text     = item.producto.nombre
            b.tvCategoria.text  = item.producto.categoria.emoji
            b.tvPrecioUnit.text = "$ %.2f c/u".format(item.producto.precio)
            b.tvSubtotal.text   = "$ %.2f".format(item.subtotal)
            b.tvCantidad.text   = item.cantidad.toString()

            b.btnMenos.setOnClickListener  { onCantidadChange(item, -1) }
            b.btnMas.setOnClickListener    { onCantidadChange(item, +1) }
            b.btnEliminar.setOnClickListener { onEliminar(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemCarritoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<ItemCarrito>() {
        override fun areItemsTheSame(a: ItemCarrito, b: ItemCarrito) = a.producto.id == b.producto.id
        override fun areContentsTheSame(a: ItemCarrito, b: ItemCarrito) = a == b
    }
}
