package veeronten.hackathon

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

fun chance(chance: Int) = MathUtils.randomBoolean(chance.toFloat() / 100)

val textButtonStyle = TextButton.TextButtonStyle().apply { font = BitmapFont() }

fun createTextBtn(text: String, onClick: () -> Unit) =
        TextButton(text, textButtonStyle).apply {
            addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    super.clicked(event, x, y)
                    onClick.invoke()
                }
            })
        }

fun createSlider(min: Float, max:Float, defValue: Float, step: Float, skin: Skin, onClick: (Float) -> Unit) = Slider(min, max, step, false, skin).apply {
    addListener {
        onClick.invoke(value)
        return@addListener false
    }
    value = defValue
}

fun Batch.drawByCoord(img: Texture, point: Point) = draw(img, TILE_SIZE * point.x, HEIGHT - TILE_SIZE * (point.y + 1), TILE_SIZE, TILE_SIZE)

fun lerp(start: Float, end: Float, t: Float) = start + t * (end - start)
