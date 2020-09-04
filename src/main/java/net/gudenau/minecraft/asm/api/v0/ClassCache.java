package net.gudenau.minecraft.asm.api.v0;

import java.io.IOException;
import java.util.Optional;

/**
 * A way to cache classes to speed up class loading.
 * */
public interface ClassCache{
    /**
     * The identifier of the cache.
     * */
    Identifier getName();
    
    /**
     * Load the contents of the cache.
     * */
    void load() throws IOException;
    
    /**
     * Save the contents of the cache.
     * */
    void save() throws IOException;
    
    /**
     * Get a cached class entry.
     *
     * @param original The original class
     *
     * @return The stored class
     * */
    Optional<byte[]> getEntry(byte[] original);
    
    /**
     * Creates an entry in the cache.
     *
     * @param original The original class
     * @param modified The modified class
     * */
    void putEntry(byte[] original, byte[] modified);
}
