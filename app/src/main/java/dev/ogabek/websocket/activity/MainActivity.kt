package dev.ogabek.websocket.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import dev.ogabek.websocket.model.DataForRespond
import dev.ogabek.websocket.R
import dev.ogabek.websocket.manager.WebSocketHandler
import dev.ogabek.websocket.manager.WebSocketManager
import dev.ogabek.websocket.model.Data
import dev.ogabek.websocket.model.Respond
import dev.ogabek.websocket.model.Send
import okhttp3.*
import okio.ByteString

class MainActivity : AppCompatActivity() {

    lateinit var tvSocket: TextView
    lateinit var loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

    }

    private fun initViews() {
        tvSocket = findViewById(R.id.tvSocket)
        tvSocket.visibility = View.GONE
        loading = findViewById(R.id.progress)
        loading.visibility = View.GONE
        connectToSocket()
    }

    private fun connectToSocket() {

        val toSend = Send("bts:subscribe", Data("live_trades_btcusd"))

        WebSocketManager.connectToSocket(Gson().toJson(toSend), object: WebSocketHandler {
            @SuppressLint("SetTextI18n")
            override fun onMessage(text: String) {
                Log.d("WebSocket", "onMessage : $text")
                val respond: Respond = Gson().fromJson(text, Respond::class.java)
                runOnUiThread {
                    loading.visibility = View.VISIBLE
                    if (respond.data != null) {
                        Log.d("WebSocket", "onRespond : ${respond.data}")
                        if (respond.data.price_str.isNullOrEmpty()) {
                            loading.visibility = View.VISIBLE
                            tvSocket.visibility = View.GONE
                        } else {
                            loading.visibility = View.GONE
                            tvSocket.visibility = View.VISIBLE
                            tvSocket.text = "1 BTC = ${respond.data.price_str}"
                        }
                    } else {
                        Log.d("WebSocket", "onNullRespond : ${respond.data}")
                    }
                }
            }

            override fun onMessage(byte: ByteString) {
                Log.d("WebSocket", "onMessage : $byte")
            }

            override fun onClosing(code: Int, reason: String) {
                Log.d("WebSocket","onClosing : $code / $reason")
            }

            override fun onFailure(t: Throwable, respond: Response?) {
                Log.d("WebSocket", "OnFailure : ${t.message} / $respond")
            }

        })
    }

}