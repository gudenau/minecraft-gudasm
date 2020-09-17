package net.gudenau.minecraft.asm.api.v0.type;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;

import java.util.Objects;

/**
 * A simple field type that contains the fully qualified type of a field.
 *
 * This contains:
 *  - The owner type
 *  - The field name
 *  - The field descriptor
 */
public final class FieldType{
    @NotNull private final Type owner;
    @NotNull private final String name;
    @NotNull private final Type descriptor;

    public FieldType(@NotNull Type owner, @NotNull String name, @NotNull Type descriptor){
        this.owner = owner;
        this.name = name;
        this.descriptor = descriptor;
    }

    /**
     * Gets the owner of this field.
     *
     * @return The owner
     */
    @NotNull
    public Type getOwner(){
        return owner;
    }

    /**
     * Gets the name of this field.
     *
     * @return The name
     */
    @NotNull
    public String getName(){
        return name;
    }

    /**
     * Gets the descriptor of this field.
     *
     * @return The descriptor
     */
    @NotNull
    public Type getDescriptor(){
        return descriptor;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        FieldType that = (FieldType)o;
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
        return owner.getInternalName() + '.' + name + ' ' + descriptor.getInternalName();
    }
}
