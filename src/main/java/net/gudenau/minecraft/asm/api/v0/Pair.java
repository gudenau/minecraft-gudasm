package net.gudenau.minecraft.asm.api.v0;

import java.util.Objects;

/**
 * A pair of things.
 *
 * Minecraft has one but this is to keep the MC stuff from getting loaded.
 *
 * Use this one instead of the MC one in transformers!
 * */
public final class Pair<A, B>{
    /**
     * Creates a new pair.
     *
     * @param a The A value
     * @param b The B value
     *
     * @return The pair
     * */
    public static <A, B> Pair<A, B> of(A a, B b){
        return new Pair<>(a, b);
    }
    
    /**
     * The A value.
     * */
    private final A a;
    
    /**
     * The B value.
     * */
    private final B b;
    
    private Pair(A a, B b){
        this.a = a;
        this.b = b;
    }
    
    /**
     * Get's the A value.
     *
     * @return The A value
     * */
    public A getA(){
        return a;
    }
    
    /**
     * Get's the B value.
     *
     * @return The B value
     * */
    public B getB(){
        return b;
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o == null || getClass() != o.getClass()){
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>)o;
        return Objects.equals(a, pair.a) &&
               Objects.equals(b, pair.b);
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(a, b);
    }
    
    @Override
    public String toString(){
        return "Pair{" +
               "a=" + a +
               ", b=" + b +
               '}';
    }
}
