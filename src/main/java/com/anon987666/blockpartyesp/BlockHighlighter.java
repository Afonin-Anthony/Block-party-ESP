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

import java.util.*;

import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.TickEvent.*;

public final class BlockHighlighter {

    private static BlockHighlighter instance;

    private static final Minecraft MC = Minecraft.getMinecraft();

    private boolean enabled;

    private List<ItemStack> heldItems = new ArrayList<>();

    private List<AxisAlignedBB> boxes = new ArrayList<>();

    public static BlockHighlighter instance() {
	if (instance == null) {
	    instance = new BlockHighlighter();
	}

	return instance;
    }

    private static boolean isItemBlock(ItemStack stack, IBlockState state) {
	final Block block = state.getBlock();
	final int blockMeta = block.getMetaFromState(state);
	final int blockId = Block.getIdFromBlock(block);

	final int itemMeta = stack.getMetadata();
	final int itemId = Item.getIdFromItem(stack.getItem());

	return blockId == itemId && blockMeta == itemMeta;
    }

    private static boolean isPlayerOnGround(EntityPlayerSP player) {
	return player.collidedVertically;
    }

    private BlockHighlighter() {

    }

    public void setEnabled(boolean enabled) {
	if (enabled) {
	    MinecraftForge.EVENT_BUS.register(this);
	} else {
	    MinecraftForge.EVENT_BUS.unregister(this);
	}

	this.enabled = enabled;
    }

    public boolean isEnabled() {
	return enabled;
    }

    private boolean isHighlightableBlock(IBlockState state) {
	for (ItemStack stack : heldItems) {
	    if (isItemBlock(stack, state)) {
		return true;
	    }
	}

	return false;
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
	final EntityPlayerSP player = MC.player;
	final World world = MC.world;

	if (world == null || player == null) {
	    return;
	}

	/* Retrieving blocks that need to be highlighted */

	final int offset = Settings.Common.offset;
	final int count = Settings.Common.count;

	heldItems.clear();
	for (int i = 0; i < count; i++) {
	    ItemStack stack = player.inventory.getStackInSlot(i + offset);
	    if (stack.getItem() instanceof ItemBlock) {
		heldItems.add(stack);
	    }
	}

	/* Scan the blocks under the player */

	if (isPlayerOnGround(player) && !heldItems.isEmpty()) {
	    /*
	     * Equal to player.getPosition().getY() but not allocate unnecessary heap memory
	     */
	    final int xPos = MathHelper.floor(player.posX + 0.5);
	    final int yPos = MathHelper.floor(player.posY + 0.5) - 1;
	    final int zPos = MathHelper.floor(player.posZ + 0.5);
	    final int scanSize = Settings.Common.fieldSize / 2;
	    final MutableBlockPos blockPos = new MutableBlockPos();

	    boxes.clear();
	    for (int z = zPos - scanSize; z < zPos + scanSize; z++) {
		for (int x = xPos - scanSize; x < xPos + scanSize; x++) {
		    blockPos.setPos(x, yPos, z);
		    if (isHighlightableBlock(world.getBlockState(blockPos))) {
			boxes.add(new AxisAlignedBB(blockPos));
		    }
		}
	    }
	}
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
	RenderingUtil.beginDraw();

	final int red = Settings.Color.red;
	final int green = Settings.Color.green;
	final int blue = Settings.Color.blue;
	final int alpha = Settings.Color.alpha;

	RenderingUtil.setColor(red, green, blue, alpha);
	boxes.forEach(RenderingUtil::drawBlockHighlight);

	RenderingUtil.endDraw();
    }

}
