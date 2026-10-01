package com.azueromarket.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.azueromarket.databinding.ItemChatDerechaBinding
import com.azueromarket.databinding.ItemChatIzquierdaBinding
import com.azueromarket.model.Mensaje
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter : ListAdapter<Mensaje, ChatAdapter.MessageViewHolder>(DiffCallback()) {

    private val VIEW_TYPE_ME    = 1
    private val VIEW_TYPE_OTHER = 2

    override fun getItemViewType(position: Int) =
        if (getItem(position).esMio) VIEW_TYPE_ME else VIEW_TYPE_OTHER

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_ME)
            SentVH(ItemChatDerechaBinding.inflate(inflater, parent, false))
        else
            ReceivedVH(ItemChatIzquierdaBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) =
        holder.bind(getItem(position))

    abstract class MessageViewHolder(b: ViewBinding) : RecyclerView.ViewHolder(b.root) {
        abstract fun bind(m: Mensaje)
        protected fun fmt(ts: Long): String =
            SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(ts))
    }

    class SentVH(private val b: ItemChatDerechaBinding) : MessageViewHolder(b) {
        override fun bind(m: Mensaje) { b.tvMensaje.text = m.contenido; b.tvHora.text = fmt(m.timestamp) }
    }

    class ReceivedVH(private val b: ItemChatIzquierdaBinding) : MessageViewHolder(b) {
        override fun bind(m: Mensaje) { b.tvMensaje.text = m.contenido; b.tvHora.text = fmt(m.timestamp) }
    }

    class DiffCallback : DiffUtil.ItemCallback<Mensaje>() {
        override fun areItemsTheSame(a: Mensaje, b: Mensaje) = a.timestamp == b.timestamp
        override fun areContentsTheSame(a: Mensaje, b: Mensaje) = a == b
    }
}
