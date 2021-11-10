package kr.co.lee.boundserviceexample

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.widget.Toast

const val MSG_SAY_HELLO = 1

class MessengerService : Service() {

    // 서비스 안의 핸들러로 컴포넌트가 메세지를 전달하기 위한 타겟
    private lateinit var mMessenger: Messenger

    internal class IncomingHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext
    ) : Handler() {
        override fun handleMessage(msg: Message) {
            when(msg.what) {
                MSG_SAY_HELLO -> {
                    Toast.makeText(applicationContext, "Hello!!!", Toast.LENGTH_SHORT).show()
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    // 다른 컴포넌트에서 서비스에 bind 한다면 서비스에 메세지를 보내기 위한 인터페이스를 메신저에 반환
    override fun onBind(intent: Intent): IBinder {
        Toast.makeText(applicationContext, "Binding", Toast.LENGTH_SHORT).show()
        mMessenger = Messenger(IncomingHandler(this))
        return mMessenger.binder
    }
}