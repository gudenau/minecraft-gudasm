package net.gudenau.minecraft.asm.api.v0.functional;

/**
 * A {@link java.util.function.Function} that returns a boolean.
 *
 * Avoids boxing.
 * */
@FunctionalInterface
public interface BooleanFunction<T>{
    boolean apply(T object);
}
