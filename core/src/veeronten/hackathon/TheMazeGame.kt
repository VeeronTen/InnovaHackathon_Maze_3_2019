package veeronten.hackathon

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import veeronten.hackathon.screens.MenuScreen

const val WIDTH = 800F
const val HEIGHT = 480F
const val TILE_SIZE = 5F

class TheMazeGame : Game() {

    lateinit var camera: OrthographicCamera
    lateinit var batch: SpriteBatch
    lateinit var font: BitmapFont
    private lateinit var music: Music
    lateinit var wallVisibleImg: Texture
    lateinit var wallInvisible: Texture
    lateinit var knownFloor: Texture
    lateinit var stragglerImg: Texture
    lateinit var targetImg: Texture

    lateinit var inputMultiplexer: InputMultiplexer
    lateinit var desctopInputProcessor: DesktopInputProcessor

    override fun create() {
        camera = OrthographicCamera()
        camera.setToOrtho(false, WIDTH, HEIGHT)
        batch = SpriteBatch()
        font = BitmapFont()

        wallVisibleImg = Texture("wall_visible.jpg")
        wallInvisible = Texture("wall_invisible.jpg")
        knownFloor = Texture("known_floor.jpg")
        stragglerImg = Texture("hero.png")
        targetImg = Texture("target.png")

        inputMultiplexer = InputMultiplexer()
        desctopInputProcessor = DesktopInputProcessor(camera)
        Gdx.input.inputProcessor = inputMultiplexer

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
