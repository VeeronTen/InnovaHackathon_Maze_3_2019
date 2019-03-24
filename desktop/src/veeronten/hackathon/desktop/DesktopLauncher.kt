package veeronten.hackathon.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import veeronten.hackathon.HEIGHT
import veeronten.hackathon.TheMazeGame
import veeronten.hackathon.WIDTH

object DesktopLauncher {

    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.width = WIDTH.toInt()
        config.height = HEIGHT.toInt()
        config.resizable = false
        LwjglApplication(TheMazeGame(), config)
    }
}
