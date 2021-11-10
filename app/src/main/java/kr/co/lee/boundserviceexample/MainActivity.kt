package kr.co.lee.boundserviceexample

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var extendBinderBtn: Button
    lateinit var messengerBinderBtn: Button

    // 서비스와 상호작용하기 위한 메신저
    private var messenger: Messenger? = null
    // 서비스에 bind되었는지 확인하는 변수
    private var messengerBound: Boolean = false

    // Binder를 확장한 클래스를 사용하는 서비스
    private lateinit var mService: ExtendBinderService
    // 서비스에 bind되었는지 확인하는 변수
    private var mBound: Boolean = false

    private val mConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            messenger = Messenger(service)
            messengerBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            messenger = null
            messengerBound = false
        }

    }

    private val connection = object : ServiceConnection {
        // 서비스에 bind 되었을 때 호출되는 콜백 메서드
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            // 이곳에서 Binder를 상속받은 클래스를 받는다
            val binder = service as ExtendBinderService.LocalBinder
            mService = binder.getService()
            mBound = true
        }

        // 서비스와 연결이 끊겼을 때 호출되는 콜백 메서드
        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        extendBinderBtn = findViewById(R.id.btn_extend_binding)
        messengerBinderBtn = findViewById(R.id.btn_messenger_binding)

        extendBinderBtn.setOnClickListener {
            extendBindingService()
        }

        messengerBinderBtn.setOnClickListener {
            messengerBindingService()
        }
    }

    override fun onStart() {
        super.onStart()
        // Service에 bind
        Intent(this, ExtendBinderService::class.java).also {
            intent -> bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        // Service에 bind
        Intent(this, MessengerService::class.java).also {
            intent -> bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false

        // unbind
        if(messengerBound) {
            unbindService(mConnection)
            messengerBound = false
        }
    }

    fun extendBindingService() {
        // 서비스에 bind가 되어있다면
        if(mBound) {
            // ExtendBinderService로부터 메서드를 호출한다
            val num: Int = mService.randomNumber
            Toast.makeText(this, "number : $num", Toast.LENGTH_SHORT).show()
        }
    }

    fun messengerBindingService() {
        if(!messengerBound) return
        // Service에게 전송 할 message를 생성 후 전송
        val msg: Message = Message.obtain(null, MSG_SAY_HELLO, 0, 0)
        try {
            messenger?.send(msg)
        } catch(e: RemoteException) {
            e.printStackTrace()
        }
    }
}