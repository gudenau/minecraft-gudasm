package net.gudenau.minecraft.asm.api.v0.type;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;

import java.util.Objects;

/**
 * A simple method type that contains the fully qualified type of a method.
 *
 * This contains:
 *  - The owner type
 *  - The method name
 *  - The method descriptor
 */
public final class MethodType{
    @NotNull private final Type owner;
    @NotNull private final String name;
    @NotNull private final Type descriptor;

    /**
     * Creates a new MethodType.
     *
     * @param owner The owner of this method
     * @param name The name of this method
     * @param type The return type of this method
     * @param params The parameters of this method
     */
    public MethodType(@NotNull Type owner, @NotNull String name, @NotNull Type type, @NotNull Type... params){
        this(owner, name, Type.getMethodType(type, params));
    }

    /**
     * Creates a new MethodType.
     *
     * @param owner The owner of this method
     * @param name The name of this method
     * @param descriptor The type with the descriptor
     */
    public MethodType(@NotNull Type owner, @NotNull String name, @NotNull Type descriptor){
        this.owner = owner;
        this.name = name;
        this.descriptor = descriptor;
    }

    /**
     * Gets the owner of this method.
     *
     * @return The owner
     */
    @NotNull
    public Type getOwner(){
        return owner;
    }

    /**
     * Gets the name of this method.
     *
     * @return The name
     */
    @NotNull
    public String getName(){
        return name;
    }

    /**
     * Gets the descriptor of this method.
     *
     * @return The descriptor
     */
    @NotNull
    public Type getDescriptor(){
        return descriptor;
    }

    /**
     * Gets the return type of this method.
     *
     * @return The return type
     */
    @NotNull
    public Type getType(){
        return descriptor.getReturnType();
    }

    /**
     * Gets the parameters of this method.
     *
     * @return The parameters
     */
    @NotNull
    public Type[] getParams(){
        return descriptor.getArgumentTypes();
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        MethodType that = (MethodType)o;
        return Objects.equals(owner, that.owner) &&
            Objects.equals(name, that.name) &&
            Objects.equals(descriptor, that.descriptor);
    }

    @Override
    public int hashCode(){
        return Objects.hash(owner, name, descriptor);
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder()
            .append(owner.getInternalName())
            .append('.')
            .append(name)
            .append('(');

        Type[] params = getParams();
        if(params.length > 0){
            int length = params.length - 1;
            for(int i = 0; i < length; i++){
                builder
                    .append(params[i].getInternalName())
                    .append(',');
            }
            builder.append(params[length].getInternalName());
        }

        return builder
            .append(')')
            .append(getType().getInternalName())
            .toString();
    }
}
