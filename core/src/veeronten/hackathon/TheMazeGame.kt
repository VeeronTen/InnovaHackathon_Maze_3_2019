package veeronten.hackathon

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch

//todo to float all?
const val WIDTH = 800
const val HEIGHT = 480
const val TILE_SIZE = 5

class TheMazeGame : Game() {


    lateinit var camera: OrthographicCamera
    lateinit var batch: SpriteBatch
    lateinit var font: BitmapFont
    lateinit var music: Music
    lateinit var wallVisibleImg: Texture

    override fun create() {
        camera = OrthographicCamera()
        camera.setToOrtho(false, WIDTH.toFloat(), HEIGHT.toFloat())
        batch = SpriteBatch()
        font = BitmapFont()
        Gdx.input.inputProcessor = DesktopInputProcessor(camera)

        wallVisibleImg = Texture("wall_visible.jpg")

        music = Gdx.audio.newMusic(Gdx.files.internal("OST.mp3"))
        music.isLooping = true
        music.play()
        this.setScreen(MenuScreen(this))
    }

    override fun dispose() {
        super.dispose()
        batch.dispose()
        font.dispose()
        music.dispose()
        wallVisibleImg.dispose()
    }
}
