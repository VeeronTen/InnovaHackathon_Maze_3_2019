package veeronten.hackathon

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2

class DesktopInputProcessor(private val camera: OrthographicCamera) : InputProcessor {

    private var lastTouch = Vector2()

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        lastTouch.set(screenX.toFloat(), screenY.toFloat())
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        val newTouch = Vector2(screenX.toFloat(), screenY.toFloat())
        val delta = newTouch.cpy().sub(lastTouch)
        lastTouch = newTouch
        camera.position.x -= delta.x * camera.zoom
        camera.position.y += delta.y * camera.zoom
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        camera.zoom += amount * camera.zoom * 0.1f
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int) = false
    override fun keyTyped(character: Char) = false
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) = false
    override fun keyUp(keycode: Int) = false
    override fun keyDown(keycode: Int) = false
}