package net.gudenau.minecraft.asm.api.v1;

import org.objectweb.asm.tree.ClassNode;

/**
 * The entry point for bytecode transformers.
 * */
public interface Transformer{
    /**
     * The name of this transformer.
     *
     * @return The identifier of this transformer
     * */
    Identifier getName();
    
    /**
     * A quick check to see if this transformer might handle a class.
     *
     * @param name The name of the class
     * @param transformedName The transformed name of the class
     *
     * @return true if the class might get transformed
     * */
    boolean handlesClass(String name, String transformedName);
    
    /**
     * Transforms a class.
     *
     * @param classNode The class that is being transformer
     * @param flags Various flags that might be useful
     *
     * @return True if the class was transformer, false otherwise
     * */
    boolean transform(ClassNode classNode, Flags flags);
    
    /**
     * Various flags that might be useful.
     * */
    interface Flags{
        /**
         * Request that ASM calculate maxes when writing the modified class.
         * */
        void requestMaxes();
        
        /**
         * Request that ASM calculate frames when writing the modified class.
         * */
        void requestFrames();
    }
}
