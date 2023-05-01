package de.daniel.bactromod.bettersky

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry.SkyRenderer
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.renderer.FogRenderer
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.RandomSource
import net.minecraft.world.level.levelgen.LegacyRandomSource
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

private const val fogColorRed = 0.3061791f * 4
private const val fogColorGreen = 0.2449433f
private const val fogColorBlue = 0.3061791f * 4
private const val fogDensity = 1.2f
private const val blindness = 0f

class BetterSkyRenderer : SkyRenderer {

    private val nebula1Texture: ResourceLocation = ResourceLocation("bactromod", "textures/sky/nebula_2.png")
    private val nebula2Texture: ResourceLocation = ResourceLocation("bactromod", "textures/sky/nebula_3.png")
    private val horizonTexture: ResourceLocation = ResourceLocation("bactromod", "textures/sky/nebula_1.png")
    private val starsTexture: ResourceLocation = ResourceLocation("bactromod", "textures/sky/stars.png")
    private val fogTexture: ResourceLocation = ResourceLocation("bactromod", "textures/sky/fog.png")

    internal fun interface BufferFunction {
        fun make(bufferBuilder: BufferBuilder, minSize: Double, maxSize: Double, count: Int, seed: Long)
    }

    private lateinit var nebula1: VertexBuffer
    private lateinit var nebula2: VertexBuffer
    private lateinit var horizon: VertexBuffer
    private lateinit var stars1: VertexBuffer
    private lateinit var stars2: VertexBuffer
    private lateinit var stars3: VertexBuffer
    private lateinit var stars4: VertexBuffer
    private lateinit var fog: VertexBuffer
    private lateinit var axis1: Vector3f
    private lateinit var axis2: Vector3f
    private lateinit var axis3: Vector3f
    private lateinit var axis4: Vector3f
    private var initialised = false

    private fun initialise() {
        if (!initialised) {
            initStars()
            val random: RandomSource = LegacyRandomSource(131)
            axis1 = Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat())
            axis2 = Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat())
            axis3 = Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat())
            axis4 = Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat())
            axis1.normalize()
            axis2.normalize()
            axis3.normalize()
            axis4.normalize()
            initialised = true
        }
    }

    override fun render(context: WorldRenderContext) {
        if (context.world() == null || context.matrixStack() == null) {
            return
        }
        initialise()
        val projectionMatrix = context.projectionMatrix()
        val matrices = context.matrixStack()
        val time = (context.world().dayTime + context.tickDelta()) % 360000 * 0.000017453292f
        val time2 = time * 2
        val time3 = time * 3
        FogRenderer.levelFogColor()
        RenderSystem.depthMask(false)
        RenderSystem.enableBlend()
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)
        val blindA: Float = 1f - blindness
        val blind02 = blindA * 0.2f
        val blind06 = blindA * 0.6f
        if (blindA > 0) {
            matrices.pushPose()
            matrices.mulPose(Quaternionf().rotationXYZ(0f, time, 0f))
            RenderSystem.setShaderTexture(0, horizonTexture)
            renderBuffer(
                matrices,
                projectionMatrix,
                horizon,
                DefaultVertexFormat.POSITION_TEX,
                0.77f,
                0.31f,
                0.73f,
                0.7f * blindA
            )
            matrices.popPose()
            matrices.pushPose()
            matrices.mulPose(Quaternionf().rotationXYZ(0f, -time, 0f))
            RenderSystem.setShaderTexture(0, nebula1Texture)
            renderBuffer(
                matrices, projectionMatrix, nebula1, DefaultVertexFormat.POSITION_TEX, 0.77f, 0.31f, 0.73f, blind02
            )
            matrices.popPose()
            matrices.pushPose()
            matrices.mulPose(Quaternionf().rotationXYZ(0f, time2, 0f))
            RenderSystem.setShaderTexture(0, nebula2Texture)
            renderBuffer(
                matrices, projectionMatrix, nebula2, DefaultVertexFormat.POSITION_TEX, 0.77f, 0.31f, 0.73f, blind02
            )
            matrices.popPose()
            RenderSystem.setShaderTexture(0, starsTexture)
            matrices.pushPose()
            matrices.mulPose(Quaternionf().setAngleAxis(time, axis3.x, axis3.y, axis3.z))
            renderBuffer(
                matrices, projectionMatrix, stars3, DefaultVertexFormat.POSITION_TEX, 0.77f, 0.31f, 0.73f, blind06
            )
            matrices.popPose()
            matrices.pushPose()
            matrices.mulPose(Quaternionf().setAngleAxis(time2, axis4.x, axis4.y, axis4.z))
            renderBuffer(matrices, projectionMatrix, stars4, DefaultVertexFormat.POSITION_TEX, 1f, 1f, 1f, blind06)
            matrices.popPose()
        }
        var a: Float = fogDensity - 1f
        if (a > 0) {
            if (a > 1) a = 1f
            RenderSystem.setShaderTexture(0, fogTexture)
            renderBuffer(
                matrices,
                projectionMatrix,
                fog,
                DefaultVertexFormat.POSITION_TEX,
                fogColorRed,
                fogColorGreen,
                fogColorBlue,
                a
            )
        }
        if (blindA > 0) {
            matrices.pushPose()
            matrices.mulPose(Quaternionf().setAngleAxis(time3, axis1.x, axis1.y, axis1.z))
            renderBuffer(matrices, projectionMatrix, stars1, DefaultVertexFormat.POSITION, 1f, 1f, 1f, blind06)
            matrices.popPose()
            matrices.pushPose()
            matrices.mulPose(Quaternionf().setAngleAxis(time2, axis2.x, axis2.y, axis2.z))
            renderBuffer(
                matrices, projectionMatrix, stars2, DefaultVertexFormat.POSITION, 0.95f, 0.64f, 0.93f, blind06
            )
            matrices.popPose()
        }
        RenderSystem.depthMask(true)
        RenderSystem.defaultBlendFunc()
        RenderSystem.disableBlend()
    }

    private fun renderBuffer(
        matrices: PoseStack,
        matrix4f: Matrix4f,
        buffer: VertexBuffer?,
        format: VertexFormat,
        r: Float,
        g: Float,
        b: Float,
        a: Float
    ) {
        RenderSystem.setShaderColor(r, g, b, a)
        buffer!!.bind()
        if (format === DefaultVertexFormat.POSITION) {
            GameRenderer.getPositionShader()?.let { buffer.drawWithShader(matrices.last().pose(), matrix4f, it) }
        } else {
            GameRenderer.getPositionTexShader()?.let { buffer.drawWithShader(matrices.last().pose(), matrix4f, it) }
        }
        VertexBuffer.unbind()
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
    }

    private fun initStars() {
        val buffer = Tesselator.getInstance().builder
        stars1 = buildBuffer(
            buffer, stars1, 0.1, 0.30, 3500, 41315
        ) { buffer1: BufferBuilder, minSize: Double, maxSize: Double, count: Int, seed: Long ->
            makeStars(
                buffer1, minSize, maxSize, count, seed
            )
        }
        stars2 = buildBuffer(
            buffer, stars2, 0.1, 0.35, 2000, 35151
        ) { buffer1: BufferBuilder, minSize: Double, maxSize: Double, count: Int, seed: Long ->
            makeStars(
                buffer1, minSize, maxSize, count, seed
            )
        }
        stars3 = buildBuffer(
            buffer, stars3, 0.4, 1.2, 1000, 61354
        ) { buffer1: BufferBuilder, minSize: Double, maxSize: Double, count: Int, seed: Long ->
            makeUVStars(
                buffer1, minSize, maxSize, count, seed
            )
        }
        stars4 = buildBuffer(
            buffer, stars4, 0.4, 1.2, 1000, 61355
        ) { buffer1: BufferBuilder, minSize: Double, maxSize: Double, count: Int, seed: Long ->
            makeUVStars(
                buffer1, minSize, maxSize, count, seed
            )
        }
        nebula1 = buildBuffer(
            buffer, nebula1, 40.0, 60.0, 30, 11515
        ) { buffer1: BufferBuilder, minSize: Double, maxSize: Double, count: Int, seed: Long ->
            makeFarFog(
                buffer1, minSize, maxSize, count, seed
            )
        }
        nebula2 = buildBuffer(
            buffer, nebula2, 40.0, 60.0, 10, 14151
        ) { buffer1: BufferBuilder, minSize: Double, maxSize: Double, count: Int, seed: Long ->
            makeFarFog(
                buffer1, minSize, maxSize, count, seed
            )
        }
        horizon = buildBufferHorizon(buffer, horizon)
        fog = buildBufferFog(buffer, fog)
    }

    private fun buildBuffer(
        bufferBuilder: BufferBuilder,
        buffer: VertexBuffer?,
        minSize: Double,
        maxSize: Double,
        count: Int,
        seed: Long,
        fkt: BufferFunction
    ): VertexBuffer {
        var buffer1 = buffer
        buffer1?.close()
        buffer1 = VertexBuffer()
        fkt.make(bufferBuilder, minSize, maxSize, count, seed)
        val renderedBuffer = bufferBuilder.end()
        buffer1.bind()
        buffer1.upload(renderedBuffer)
        return buffer1
    }

    private fun buildBufferHorizon(bufferBuilder: BufferBuilder, buffer: VertexBuffer?): VertexBuffer {
        return buildBuffer(
            bufferBuilder, buffer, 0.0, 0.0, 0, 0
        ) { builder: BufferBuilder, _: Double, _: Double, _: Int, _: Long ->
            makeCylinder(
                builder, 100.0
            )
        }
    }

    private fun buildBufferFog(bufferBuilder: BufferBuilder, buffer: VertexBuffer?): VertexBuffer {
        return buildBuffer(
            bufferBuilder, buffer, 0.0, 0.0, 0, 0
        ) { builder: BufferBuilder, _: Double, _: Double, _: Int, _: Long ->
            makeCylinder(
                builder, 70.0
            )
        }
    }

    private fun makeStars(buffer: BufferBuilder, minSize: Double, maxSize: Double, count: Int, seed: Long) {
        val random: RandomSource = LegacyRandomSource(seed)
        RenderSystem.setShader { GameRenderer.getPositionShader() }
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION)
        for (i in 0 until count) {
            var posX = random.nextDouble() * 2.0 - 1.0
            var posY = random.nextDouble() * 2.0 - 1.0
            var posZ = random.nextDouble() * 2.0 - 1.0
            val size: Double = randRange(minSize, maxSize, random)
            var length = posX * posX + posY * posY + posZ * posZ
            if (length < 1.0 && length > 0.001) {
                length = 1.0 / sqrt(length)
                posX *= length
                posY *= length
                posZ *= length
                val px = posX * 100.0
                val py = posY * 100.0
                val pz = posZ * 100.0
                var angle = atan2(posX, posZ)
                val sin1 = sin(angle)
                val cos1 = cos(angle)
                angle = atan2(sqrt(posX * posX + posZ * posZ), posY)
                val sin2 = sin(angle)
                val cos2 = cos(angle)
                angle = random.nextDouble() * Math.PI * 2.0
                val sin3 = sin(angle)
                val cos3 = cos(angle)
                for (index in 0..3) {
                    val x = ((index and 2) - 1).toDouble() * size
                    val y = ((index + 1 and 2) - 1).toDouble() * size
                    val aa = x * cos3 - y * sin3
                    val ab = y * cos3 + x * sin3
                    val dy = aa * sin2 + 0.0 * cos2
                    val ae = 0.0 * sin2 - aa * cos2
                    val dx = ae * sin1 - ab * cos1
                    val dz = ab * sin1 + ae * cos1
                    buffer.vertex(px + dx, py + dy, pz + dz).endVertex()
                }
            }
        }
    }

    private fun makeUVStars(buffer: BufferBuilder, minSize: Double, maxSize: Double, count: Int, seed: Long) {
        val random: RandomSource = LegacyRandomSource(seed)
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)
        for (i in 0 until count) {
            var posX = random.nextDouble() * 2.0 - 1.0
            var posY = random.nextDouble() * 2.0 - 1.0
            var posZ = random.nextDouble() * 2.0 - 1.0
            val size: Double = randRange(minSize, maxSize, random)
            var length = posX * posX + posY * posY + posZ * posZ
            if (length < 1.0 && length > 0.001) {
                length = 1.0 / sqrt(length)
                posX *= length
                posY *= length
                posZ *= length
                val px = posX * 100.0
                val py = posY * 100.0
                val pz = posZ * 100.0
                var angle = atan2(posX, posZ)
                val sin1 = sin(angle)
                val cos1 = cos(angle)
                angle = atan2(sqrt(posX * posX + posZ * posZ), posY)
                val sin2 = sin(angle)
                val cos2 = cos(angle)
                angle = random.nextDouble() * Math.PI * 2.0
                val sin3 = sin(angle)
                val cos3 = cos(angle)
                val minV = random.nextInt(4) / 4f
                for (index in 0..3) {
                    val x = ((index and 2) - 1).toDouble() * size
                    val y = ((index + 1 and 2) - 1).toDouble() * size
                    val aa = x * cos3 - y * sin3
                    val ab = y * cos3 + x * sin3
                    val dy = aa * sin2 + 0.0 * cos2
                    val ae = 0.0 * sin2 - aa * cos2
                    val dx = ae * sin1 - ab * cos1
                    val dz = ab * sin1 + ae * cos1
                    val texU = (index shr 1 and 1).toFloat()
                    val texV = (index + 1 shr 1 and 1) / 4f + minV
                    buffer.vertex(px + dx, py + dy, pz + dz).uv(texU, texV).endVertex()
                }
            }
        }
    }

    private fun randRange(min: Double, max: Double, random: RandomSource): Double {
        return min + random.nextDouble() * (max - min)
    }

    private fun makeFarFog(buffer: BufferBuilder, minSize: Double, maxSize: Double, count: Int, seed: Long) {
        val random: RandomSource = LegacyRandomSource(seed)
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)
        for (i in 0 until count) {
            var posX = random.nextDouble() * 2.0 - 1.0
            var posY = random.nextDouble() - 0.5
            var posZ = random.nextDouble() * 2.0 - 1.0
            var size: Double = randRange(minSize, maxSize, random)
            var length = posX * posX + posY * posY + posZ * posZ
            val distance = 2.0
            if (length < 1.0 && length > 0.001) {
                length = distance / sqrt(length)
                size *= distance
                posX *= length
                posY *= length
                posZ *= length
                val px = posX * 100.0
                val py = posY * 100.0
                val pz = posZ * 100.0
                var angle = atan2(posX, posZ)
                val sin1 = sin(angle)
                val cos1 = cos(angle)
                angle = atan2(sqrt(posX * posX + posZ * posZ), posY)
                val sin2 = sin(angle)
                val cos2 = cos(angle)
                angle = random.nextDouble() * Math.PI * 2.0
                val sin3 = sin(angle)
                val cos3 = cos(angle)
                for (index in 0..3) {
                    val x = ((index and 2) - 1).toDouble() * size
                    val y = ((index + 1 and 2) - 1).toDouble() * size
                    val aa = x * cos3 - y * sin3
                    val ab = y * cos3 + x * sin3
                    val dy = aa * sin2 + 0.0 * cos2
                    val ae = 0.0 * sin2 - aa * cos2
                    val dx = ae * sin1 - ab * cos1
                    val dz = ab * sin1 + ae * cos1
                    val texU = (index shr 1 and 1).toFloat()
                    val texV = (index + 1 shr 1 and 1).toFloat()
                    buffer.vertex(px + dx, py + dy, pz + dz).uv(texU, texV).endVertex()
                }
            }
        }
    }

    private fun makeCylinder(buffer: BufferBuilder, radius: Double) {
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)
        for (i in 0 until 16) {
            val a1 = i.toDouble() * Math.PI * 2.0 / 16.0
            val a2 = (i + 1).toDouble() * Math.PI * 2.0 / 16.0
            val px1 = sin(a1) * radius
            val pz1 = cos(a1) * radius
            val px2 = sin(a2) * radius
            val pz2 = cos(a2) * radius
            val u0 = i.toFloat() / 16.0f
            val u1 = (i + 1).toFloat() / 16.0f
            buffer.vertex(px1, -50.0, pz1).uv(u0, 0f).endVertex()
            buffer.vertex(px1, 50.0, pz1).uv(u0, 1f).endVertex()
            buffer.vertex(px2, 50.0, pz2).uv(u1, 1f).endVertex()
            buffer.vertex(px2, -50.0, pz2).uv(u1, 0f).endVertex()
        }
    }
}