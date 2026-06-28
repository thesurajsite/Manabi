package app.dev.manabi

import javax.swing.JOptionPane

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun showToast(message: String) {
    // Show a non-blocking dialog on desktop as a "Toast"
    Thread {
        JOptionPane.showMessageDialog(null, message, "Notification", JOptionPane.INFORMATION_MESSAGE)
    }.start()
}

