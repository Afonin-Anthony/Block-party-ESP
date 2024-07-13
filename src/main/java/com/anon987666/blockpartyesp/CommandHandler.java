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

import net.minecraft.command.*;
import net.minecraft.server.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;

public final class CommandHandler extends CommandBase {

    private static final String NAME = "blockpartyesp";

    private static final String USAGE = "blockpartyesp <enable|disable>";

    private static final String[] ALIASES = { "bpesp" };

    private static final String ENABLE_ACTION = "enable";

    private static final String DISABLE_ACTION = "disable";

    private static final String TOGGLE_ACTION = "toggle";

    private static final BlockHighlighter HIGHLIGHTER = BlockHighlighter.instance();

    private static void sendMessage(ICommandSender sender, String message) {
	sender.sendMessage(new TextComponentString(message));
    }

    private static void printStatus(ICommandSender sender) {
	sendMessage(sender, "\u00a7a\u00a7lBlock Party ESP " + (HIGHLIGHTER.isEnabled() ? "enabled" : "disabled"));
    }

    @Override
    public String getName() {
	return NAME;
    }

    @Override
    public String getUsage(ICommandSender sender) {
	return USAGE;
    }

    @Override
    public List<String> getAliases() {
	return Arrays.asList(ALIASES);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
	if (args.length != 1) {
	    throw new CommandException("\u00a74\u00a7lBlock Party ESP:\u00a7c Wrong usage");
	}

	final String action = args[0];

	if (action.equalsIgnoreCase(ENABLE_ACTION)) {
	    HIGHLIGHTER.setEnabled(true);
	    printStatus(sender);
	} else if (action.equalsIgnoreCase(DISABLE_ACTION)) {
	    HIGHLIGHTER.setEnabled(false);
	    printStatus(sender);
	} else if (action.equalsIgnoreCase(TOGGLE_ACTION)) {
	    HIGHLIGHTER.setEnabled(!HIGHLIGHTER.isEnabled());
	    printStatus(sender);
	} else {
	    throw new CommandException("\u00a74\u00a7lBlock Party ESP:\u00a7c Unknown action \"" + action + "\"");
	}
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
	return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
	    BlockPos targetPos) {
	return Arrays.asList(ENABLE_ACTION, DISABLE_ACTION, TOGGLE_ACTION);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
	return false;
    }

}
