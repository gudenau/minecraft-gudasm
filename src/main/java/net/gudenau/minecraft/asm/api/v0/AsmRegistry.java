package net.gudenau.minecraft.asm.api.v0;

import net.gudenau.minecraft.asm.impl.RegistryImpl;

/**
 * The place to register your gross ASM hacks.
 * */
public interface AsmRegistry{
    /**
     * Gets the instance of the registry.
     *
     * @return The registry
     * */
    static AsmRegistry getInstance(){
        return RegistryImpl.INSTANCE;
    }
    
    /**
     * Registers a class transformer for transforming classes before mixins.
     *
     * This one should not be used unless it is 100% required.
     *
     * @param transformer The transformer to register
     * */
    void registerEarlyTransformer(Transformer transformer);
    
    /**
     * Registers a class transformer for transforming classes after mixins.
     *
     * This is the most compatible one.
     *
     * @param transformer The transformer to register
     * */
    void registerTransformer(Transformer transformer);
    
    /**
     * Registers a class cache.
     *
     * @param cache The class cache
     * */
    void registerClassCache(ClassCache cache);
}
