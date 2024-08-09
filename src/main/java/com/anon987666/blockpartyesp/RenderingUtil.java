/*
 * The MIT License
 *
 * Copyright (c) 2024 Anthony Afonin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.anon987666.blockpartyesp;

import org.lwjgl.opengl.*;

import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.GlStateManager.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.util.math.*;

public class RenderingUtil {

	private static final Tessellator TESSELATOR = Tessellator.getInstance();

	private static final BufferBuilder BUILDER = TESSELATOR.getBuffer();

	private RenderingUtil() {
		throw new AssertionError("No RenderingUtil instances for you!");
	}

	public static void beginDraw() {
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

		GlStateManager.disableTexture2D();
		GlStateManager.enableCull();
		GlStateManager.disableDepth();
		GlStateManager.pushMatrix();

		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

		GlStateManager.translate(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ);
	}

	public static void endDraw() {
		GlStateManager.popMatrix();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.enableDepth();
	}

	public static void setColor(int red, int green, int blue, int alpha) {
		GlStateManager.color(red / 255f, green / 255f, blue / 255f, alpha / 255f);
	}

	public static void drawBlockHighlight(AxisAlignedBB aabb) {
		drawBlockHighlight(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
	}

	public static void drawBlockHighlight(double minX, double minY, double minZ, double maxX, double maxY,
			double maxZ) {

		BUILDER.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

		BUILDER.pos(minX, maxY, minZ).endVertex();
		BUILDER.pos(minX, maxY, maxZ).endVertex();
		BUILDER.pos(maxX, maxY, maxZ).endVertex();
		BUILDER.pos(maxX, maxY, minZ).endVertex();

		TESSELATOR.draw();
	}
}
