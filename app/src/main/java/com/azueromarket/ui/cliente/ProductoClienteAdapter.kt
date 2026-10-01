package com.azueromarket.ui.cliente

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azueromarket.databinding.ItemProductoClienteBinding
import com.azueromarket.model.Producto

class ProductoClienteAdapter(
    private val onClick: (Producto) -> Unit
) : ListAdapter<Producto, ProductoClienteAdapter.VH>(Diff()) {

    inner class VH(private val b: ItemProductoClienteBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(p: Producto) {
            b.tvEmoji.text        = p.categoria.emoji
            b.tvNombre.text       = p.nombre
            b.tvPrecio.text       = "$ %.2f".format(p.precio)
            b.tvEmprendedor.text  = "por ${p.emprendedorNombre}"
            b.tvUbicacion.text    = "📍 ${p.ubicacion}"
            b.tvCategoria.text    = p.categoria.nombreMostrar

            // Stock badge
            when {
                p.stock == 0   -> { b.tvStockBadge.text = "Sin stock"; b.tvStockBadge.setBackgroundResource(com.azueromarket.R.drawable.bg_badge_rojo) }
                p.stock <= 5   -> { b.tvStockBadge.text = "Últimas ${p.stock}"; b.tvStockBadge.setBackgroundResource(com.azueromarket.R.drawable.bg_badge_naranja) }
                else           -> { b.tvStockBadge.text = "Disponible"; b.tvStockBadge.setBackgroundResource(com.azueromarket.R.drawable.bg_badge_verde) }
            }

            b.root.setOnClickListener { onClick(p) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, vt: Int) =
        VH(ItemProductoClienteBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))

    class Diff : DiffUtil.ItemCallback<Producto>() {
        override fun areItemsTheSame(a: Producto, b: Producto) = a.id == b.id
        override fun areContentsTheSame(a: Producto, b: Producto) = a == b
    }
}
