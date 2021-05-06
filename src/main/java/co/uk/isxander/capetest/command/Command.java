package co.uk.isxander.capetest.command;

import club.sk1er.mods.core.ModCore;
import co.uk.isxander.capetest.CapeTest;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class Command extends CommandBase {

    @Override
    public String getCommandName() {
        return "capetest";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/captest";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        ModCore.getInstance().getGuiHandler().open(CapeTest.getInstance().getConfig().gui());
    }

}
