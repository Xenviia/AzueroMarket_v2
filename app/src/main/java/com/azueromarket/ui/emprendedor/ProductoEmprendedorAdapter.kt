package com.azueromarket.ui.emprendedor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azueromarket.databinding.ItemProductoEmprendedorBinding
import com.azueromarket.model.Producto

class ProductoEmprendedorAdapter(
    private val onEditClick:   (Producto) -> Unit,
    private val onDeleteClick: (Producto) -> Unit
) : ListAdapter<Producto, ProductoEmprendedorAdapter.VH>(Diff()) {

    inner class VH(private val b: ItemProductoEmprendedorBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(p: Producto) {
            b.tvEmoji.text       = p.categoria.emoji
            b.tvNombre.text      = p.nombre
            b.tvPrecio.text      = "$ %.2f".format(p.precio)
            b.tvCategoria.text   = p.categoria.nombreMostrar
            b.tvDescripcion.text = p.descripcion
            b.tvStock.text = when {
                p.stock == 0  -> "❌ Sin stock"
                p.stock <= 5  -> "⚠️ Stock: ${p.stock}"
                else          -> "✅ Stock: ${p.stock}"
            }
            b.chipDisponible.text = if (p.disponible) "Activo" else "Inactivo"
            b.btnEditar.setOnClickListener   { onEditClick(p) }
            b.btnEliminar.setOnClickListener { onDeleteClick(p) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, vt: Int) =
        VH(ItemProductoEmprendedorBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))

    class Diff : DiffUtil.ItemCallback<Producto>() {
        override fun areItemsTheSame(a: Producto, b: Producto) = a.id == b.id
        override fun areContentsTheSame(a: Producto, b: Producto) = a == b
    }
}
