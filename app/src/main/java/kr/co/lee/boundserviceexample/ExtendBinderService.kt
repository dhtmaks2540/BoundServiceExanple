package kr.co.lee.boundserviceexample

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.util.*

class ExtendBinderService : Service() {
    // 다른 컴포넌트에게 리턴할 Binder를 상속받는 클래스
    private val binder = LocalBinder()

    // Random 숫자를 만들기 위한 클래스
    private val mRandomGenerator = Random()

    // 다른 컴포넌트에게 제공되는 메서드
    val randomNumber: Int
        get() = mRandomGenerator.nextInt(100)

    
    // onBind에서 binder 클래스를 리턴
    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    // 다른 컴포넌트에게 제공되는 Binder 클래스
    inner class LocalBinder: Binder() {
        // Service 객체를 리턴
        fun getService(): ExtendBinderService = this@ExtendBinderService
    }
}