package veeronten.hackathon

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxApplicationAdapter


class TheMazeGame : KtxApplicationAdapter {
    lateinit var camera: OrthographicCamera

    lateinit var batch: SpriteBatch
    lateinit var img: Texture

    override fun create() {
        camera = OrthographicCamera()
        camera.setToOrtho(false, 800F, 480F)

        batch = SpriteBatch()
        img = Texture("badlogic.jpg")
        Gdx.input.inputProcessor = DesktopInputProcessor(camera)
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()

        batch.projectionMatrix = camera.combined
        batch.begin()

        for (i in 0 until MazeSource.mazeArray.size) {
            for (j in 0 until MazeSource.mazeArray[i].size) {
                if (MazeSource.mazeArray[i][j] == WALL) {
                    batch.draw(img, 5F * j, 480 - 5F * (i + 1), 5f, 5f)

//                    batch.draw(img, 0F, 0F)
                }
            }
        }

        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
    }
}


//class Drop : ApplicationAdapter() {
//    internal var camera: OrthographicCamera
//    internal var batch: SpriteBatch
//    internal var dropImage: Texture
//    internal var bucketImage: Texture
//    internal var dropSound: Sound
//    internal var rainMusic: Music
//    internal var bucket: Rectangle
//    internal var touchPos: Vector3
//    internal var raindrops: Array<Rectangle>
//    internal var lastDropTime: Long = 0
//
//
//
//    override fun render() {
//        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f)
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
//
//        camera.update()
//
//        batch.projectionMatrix = camera.combined
//        batch.begin()
//        batch.draw(bucketImage, bucket.x, bucket.y)
//        for (raindrop in raindrops) {
//            batch.draw(dropImage, raindrop.x, raindrop.y)
//        }
//        batch.end()
//
//        if (Gdx.input.isTouched) {
//            touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
//            camera.unproject(touchPos)
//            bucket.x = (touchPos.x - 64 / 2).toInt()
//        }
//
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.deltaTime
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.deltaTime
//
//        if (bucket.x < 0) bucket.x = 0
//        if (bucket.x > 800 - 64) bucket.x = 800 - 64
//
//        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop()
//
//        val iter = raindrops.iterator()
//        while (iter.hasNext()) {
//            val raindrop = iter.next()
//            raindrop.y -= 200 * Gdx.graphics.deltaTime
//            if (raindrop.y + 64 < 0) iter.remove()
//            if (raindrop.overlaps(bucket)) {
//                dropSound.play()
//                iter.remove()
//            }
//        }
//    }
//
//    override fun dispose() {
//        super.dispose()
//        dropImage.dispose()
//        bucketImage.dispose()
//        dropSound.dispose()
//        rainMusic.dispose()
//        batch.dispose()
//    }
//}