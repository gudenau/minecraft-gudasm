package net.gudenau.minecraft.asm.api.v0;

import net.gudenau.minecraft.asm.util.TypeCacheImpl;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;

/**
 * Type cache to try and keep less instances floating around.
 * */
@Deprecated // This is just annoying
public interface TypeCache{
    /**
     * Get the handle to the Type cache.
     *
     * @return The handle to the type cache
     * */
    @Deprecated
    @NotNull
    static TypeCache getTypeCache(){
        return TypeCacheImpl.INSTANCE;
    }

    /**
     * Gets a Type from a descriptor.
     *
     * I.E.: "Ljava/lang/Object;"
     *
     * @param descriptor The descriptor
     *
     * @return The type
     * */
    @Deprecated
    Type getType(String descriptor);

    /**
     * Gets a Type from a Class.
     *
     * I.E.: Object.class
     *
     * Do not use this for anything outside the standard lib, it will cause things to get loaded early!
     *
     * @param klass The class type
     *
     * @return The type
     * */
    @Deprecated
    Type getType(Class<?> klass);
    
    /**
     * Gets an Type from an internal name.
     *
     * I.E.: "java/lang/Object"
     *
     * @param name The internal name
     *
     * @return The type
     * */
    @Deprecated
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
    @Deprecated
    Type getMethodType(String descriptor);
    
    /**
     * Gets a method type from individual types.
     *
     * @param returnType The return type
     * @param arguments The argument types
     *
     * @return The method type
     * */
    @Deprecated
    Type getMethodType(Type returnType, Type... arguments);
}
