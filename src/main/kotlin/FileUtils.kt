
import java.io.File
import java.util.*
import javax.imageio.ImageIO

object FileUtils {

    fun isImage(file: File): Boolean {
        try {
            if (ImageIO.read(file) == null) {
                return false
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }


    fun createLauncherFile(name: String, iconPath: String, execPath: String) {
        val username = System.getProperty("user.name")
        val content =
"""[Desktop Entry]
Exec="${execPath}" %f
Icon=${iconPath}
Version=1.0
Type=Application
Name=${name}
Terminal=false"""
        val applicationsPath = "/home/$username/.local/share/applications"
        val uuid = UUID.randomUUID().toString().slice(0..10)
        val file = File(applicationsPath + File.separator + "$name-$uuid.desktop")
        if(file.exists()){
            file.delete()
        }
        if(file.createNewFile()) {
            file.writeText(content)
        }

    }

}
