package net.gudenau.minecraft.asm;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;

public class GudAsm implements ModInitializer{
    @Override
    public void onInitialize(){
        LogManager.getLogger("gud_asm").fatal("Welcome to the wacky world of gudASM, things might break in weird and wonderful ways!");
    }
}
