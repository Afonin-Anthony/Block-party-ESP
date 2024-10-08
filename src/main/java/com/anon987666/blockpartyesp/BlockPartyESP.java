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

import net.minecraftforge.client.*;
import net.minecraftforge.common.*;
import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.*;

@Mod(modid = BlockPartyESP.MODID, name = BlockPartyESP.NAME, version = BlockPartyESP.VERSION)
public class BlockPartyESP {

	public static final String MODID = "blockpartyesp";

	public static final String NAME = "Block Party ESP";

	public static final String VERSION = "1.0.1";

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		ClientCommandHandler.instance.registerCommand(new CommandHandler());
	}

	@SubscribeEvent
	public void onConfigChangedEvent(OnConfigChangedEvent event) {
		if (event.getModID().equals(MODID)) {
			ConfigManager.sync(MODID, Config.Type.INSTANCE);
		}
	}

}