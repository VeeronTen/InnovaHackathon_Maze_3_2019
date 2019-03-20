package veeronten.hackathon.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration

import veeronten.hackathon.TheMazeGame

object DesktopLauncher {

    const val DESKTOP_WIDTH = 800
    const val DESKTOP_HEIGHT = 480

    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.width = DESKTOP_WIDTH
        config.height = DESKTOP_HEIGHT
        config.resizable = false
        LwjglApplication(TheMazeGame(), config)
    }
}
