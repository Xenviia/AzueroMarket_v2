package com.azueromarket.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.azueromarket.databinding.ActivityChatBinding
import com.azueromarket.model.Mensaje
import com.azueromarket.utils.MockDataSource
import com.azueromarket.utils.SessionManager

class ChatActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CONV_ID             = "conv_id"
        const val EXTRA_OTRO_USUARIO_ID     = "otro_usuario_id"
        const val EXTRA_OTRO_USUARIO_NOMBRE = "otro_usuario_nombre"
        const val EXTRA_PRODUCTO_ID         = "producto_id"
        const val EXTRA_PRODUCTO_NOMBRE     = "producto_nombre"
    }

    private lateinit var binding: ActivityChatBinding
    private lateinit var session: SessionManager
    private lateinit var adapter: ChatAdapter
    private lateinit var convId: String
    private val mensajes = mutableListOf<Mensaje>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SessionManager(this)

        convId = intent.getStringExtra(EXTRA_CONV_ID) ?: "conv_demo"
        val nombreOtro     = intent.getStringExtra(EXTRA_OTRO_USUARIO_NOMBRE) ?: "Productor"
        val productoNombre = intent.getStringExtra(EXTRA_PRODUCTO_NOMBRE) ?: ""

        setSupportActionBar(binding.toolbarChat)
        supportActionBar?.apply { setDisplayHomeAsUpEnabled(true); title = nombreOtro }
        binding.toolbarChat.setNavigationOnClickListener { finish() }

        if (productoNombre.isNotEmpty())
            binding.tvContextoProducto.text = "💬 Consultando sobre: $productoNombre"
        else binding.tvContextoProducto.visibility = android.view.View.GONE

        setupRecyclerView()
        loadMensajes()
        setupEnviar()
    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter()
        binding.rvMensajes.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity).apply { stackFromEnd = true }
            adapter = this@ChatActivity.adapter
        }
    }

    private fun loadMensajes() {
        mensajes.clear()
        mensajes.addAll(MockDataSource.getMensajes(convId).map { msg ->
            msg.copy(esMio = msg.remitenteId == session.getUserId())
        })
        adapter.submitList(mensajes.toList())
        if (mensajes.isNotEmpty())
            binding.rvMensajes.scrollToPosition(mensajes.size - 1)
    }

    private fun setupEnviar() {
        binding.btnEnviar.setOnClickListener {
            val texto = binding.etMensaje.text.toString().trim()
            if (texto.isEmpty()) return@setOnClickListener
            MockDataSource.enviarMensaje(convId, session.getUserId(), texto)
            binding.etMensaje.text.clear()
            loadMensajes()
        }
    }
}
