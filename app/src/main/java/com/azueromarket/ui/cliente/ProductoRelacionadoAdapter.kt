package com.azueromarket.ui.cliente

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azueromarket.databinding.ItemProductoRelacionadoBinding
import com.azueromarket.model.Producto

class ProductoRelacionadoAdapter(
    private val onClick: (Producto) -> Unit
) : ListAdapter<Producto, ProductoRelacionadoAdapter.VH>(Diff()) {

    inner class VH(private val b: ItemProductoRelacionadoBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(p: Producto) {
            b.tvEmoji.text  = p.categoria.emoji
            b.tvNombre.text = p.nombre
            b.tvPrecio.text = "$ %.2f".format(p.precio)
            b.root.setOnClickListener { onClick(p) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, vt: Int) =
        VH(ItemProductoRelacionadoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))

    class Diff : DiffUtil.ItemCallback<Producto>() {
        override fun areItemsTheSame(a: Producto, b: Producto) = a.id == b.id
        override fun areContentsTheSame(a: Producto, b: Producto) = a == b
    }
}
