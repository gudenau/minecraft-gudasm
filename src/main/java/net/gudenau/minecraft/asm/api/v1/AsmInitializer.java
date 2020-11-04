package net.gudenau.minecraft.asm.api.v1;

/**
 * An ASM mod initializer.
 *
 * Load as few classes as you can get away with here!
 * */
public interface AsmInitializer{
    /**
     * Called to initialize this asm mod.
     * */
    void onInitializeAsm();
}
