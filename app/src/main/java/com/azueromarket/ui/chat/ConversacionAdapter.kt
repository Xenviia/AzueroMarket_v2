package com.azueromarket.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azueromarket.databinding.ItemConversacionBinding
import com.azueromarket.model.Conversacion
import java.text.SimpleDateFormat
import java.util.*

class ConversacionAdapter(
    private val onClick: (Conversacion) -> Unit
) : ListAdapter<Conversacion, ConversacionAdapter.VH>(Diff()) {

    inner class VH(private val b: ItemConversacionBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(c: Conversacion) {
            b.tvNombreUsuario.text   = c.otroUsuarioNombre
            b.tvUltimoMensaje.text   = c.ultimoMensaje
            b.tvContextoProducto.text = if (c.productoContextoNombre.isNotEmpty())
                "Sobre: ${c.productoContextoNombre}" else ""
            b.tvContextoProducto.visibility = if (c.productoContextoNombre.isNotEmpty()) View.VISIBLE else View.GONE
            b.tvHora.text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(c.timestamp))
            b.tvAvatar.text = c.otroUsuarioNombre.first().toString()
            if (c.noLeidos > 0) {
                b.tvBadgeNoLeidos.visibility = View.VISIBLE
                b.tvBadgeNoLeidos.text = c.noLeidos.toString()
                b.tvUltimoMensaje.setTypeface(null, android.graphics.Typeface.BOLD)
            } else {
                b.tvBadgeNoLeidos.visibility = View.GONE
                b.tvUltimoMensaje.setTypeface(null, android.graphics.Typeface.NORMAL)
            }
            b.root.setOnClickListener { onClick(c) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, vt: Int) =
        VH(ItemConversacionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))

    class Diff : DiffUtil.ItemCallback<Conversacion>() {
        override fun areItemsTheSame(a: Conversacion, b: Conversacion) = a.id == b.id
        override fun areContentsTheSame(a: Conversacion, b: Conversacion) = a == b
    }
}
