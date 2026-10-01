package com.azueromarket.ui.chat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.azueromarket.databinding.ActivityConversacionesBinding
import com.azueromarket.utils.MockDataSource
import com.azueromarket.utils.SessionManager

class ConversacionesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConversacionesBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversacionesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SessionManager(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply { setDisplayHomeAsUpEnabled(true); title = "Mensajes" }
        binding.toolbar.setNavigationOnClickListener { finish() }

        val conversaciones = MockDataSource.getConversacionesCliente(session.getUserId())
        val adapter = ConversacionAdapter { conv ->
            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra(ChatActivity.EXTRA_CONV_ID,             conv.id)
                putExtra(ChatActivity.EXTRA_OTRO_USUARIO_ID,    conv.otroUsuarioId)
                putExtra(ChatActivity.EXTRA_OTRO_USUARIO_NOMBRE, conv.otroUsuarioNombre)
                putExtra(ChatActivity.EXTRA_PRODUCTO_NOMBRE,     conv.productoContextoNombre)
            }
            startActivity(intent)
        }
        binding.rvConversaciones.layoutManager = LinearLayoutManager(this)
        binding.rvConversaciones.adapter = adapter
        adapter.submitList(conversaciones)
    }
}
