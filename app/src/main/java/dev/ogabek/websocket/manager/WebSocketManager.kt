package dev.ogabek.websocket.manager

import android.util.Log
import okhttp3.*
import okio.ByteString

class WebSocketManager: WebSocketListener() {

    companion object {

        private var mWebSocket: WebSocket? = null

        private val url: String =  "wss://ws.bitstamp.net"

        fun connectToSocket(toSend: String, handler: WebSocketHandler) {
            val client = OkHttpClient()

            val request: Request = Request.Builder().url(url).build()
            client.newWebSocket(request, object: WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    mWebSocket = webSocket
                    webSocket.send(toSend)
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    handler.onMessage(text)
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    handler.onMessage(bytes)
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    handler.onClosing(code, reason)
//                    webSocket.close(1000 ,null)
//                    webSocket.cancel()
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    handler.onFailure(t, response)
                }

            })

            client.dispatcher().executorService().shutdown()

        }

    }

}