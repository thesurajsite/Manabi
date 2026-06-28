package app.dev.manabi

import android.os.Build
import android.widget.Toast
import org.koin.java.KoinJavaComponent.get

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun showToast(message: String) {
    val context: android.content.Context = get(android.content.Context::class.java)
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

