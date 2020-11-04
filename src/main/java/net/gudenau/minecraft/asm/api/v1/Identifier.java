package net.gudenau.minecraft.asm.api.v1;

import java.util.Objects;

/**
 * A simple namespace/path identifier.
 *
 * Similar to Minecraft's but doesn't load any of it's classes.
 * */
public final class Identifier{
    private final String modId;
    private final String name;
    
    /**
     * Creates a new identifier from a mod id and a name.
     *
     * @param modId The mod id
     * @param name The name
     * */
    public Identifier(String modId, String name){
        this.modId = modId;
        this.name = name;
    }

    /**
     * Gets the mod id.
     *
     * @return The mod id
     * */
    public String getModId(){
        return modId;
    }
    
    /**
     * Gets the name.
     *
     * @return The name
     * */
    public String getName(){
        return name;
    }
    
    @Override
    public String toString(){
        return modId + ":" + name;
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o == null || getClass() != o.getClass()){
            return false;
        }
        Identifier that = (Identifier)o;
        return Objects.equals(modId, that.modId) &&
               Objects.equals(name, that.name);
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(modId, name);
    }
}
