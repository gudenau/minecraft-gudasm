package net.gudenau.minecraft.asm.api.v0;

import org.objectweb.asm.Type;

/**
 * Type cache to try and keep less instances floating around.
 * */
public interface TypeCache{
    /**
     * Gets a Type from a descriptor.
     *
     * I.E.: "Ljava/lang/Object;"
     *
     * @param descriptor The descriptor
     *
     * @return The type
     * */
    Type getType(String descriptor);
    
    /**
     * Gets an Type from an internal name.
     *
     * I.E.: "java/lang/Object"
     *
     * @param name The internal name
     *
     * @return The type
     * */
    Type getObjectType(String name);
    
    /**
     * Gets a method type from a descriptor.
     *
     * I.E.: "(Ljava/lang/Object;)V"
     *
     * @param descriptor The method descriptor
     *
     * @return The method type
     * */
    Type getMethodType(String descriptor);
    
    /**
     * Gets a method type from individual types.
     *
     * @param returnType The return type
     * @param arguments The argument types
     *
     * @return The method type
     * */
    Type getMethodType(Type returnType, Type... arguments);
}
