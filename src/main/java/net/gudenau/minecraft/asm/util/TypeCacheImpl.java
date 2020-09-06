package net.gudenau.minecraft.asm.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.function.Function;
import net.gudenau.minecraft.asm.api.v0.Pair;
import net.gudenau.minecraft.asm.api.v0.TypeCache;
import org.objectweb.asm.Type;

public class TypeCacheImpl implements TypeCache{
    public static final TypeCache INSTANCE = new TypeCacheImpl();

    private TypeCacheImpl(){}

    private final Locker stringLocker = new Locker();
    private final Map<String, WeakReference<Type>> stringCache = new Object2ObjectOpenHashMap<>();
    
    private final Locker methodLocker = new Locker();
    private final Map<Pair<Type, Type[]>, WeakReference<Type>> methodCache = new Object2ObjectOpenHashMap<>();
    
    private Type getString(String name, Function<String, Type> factory){
        WeakReference<Type> ref = stringLocker.computeIfAbsent(stringCache, name, (n)->new WeakReference<>(factory.apply(n)));
        Type type = ref.get();
        if(type == null){
            type = factory.apply(name);
            stringLocker.putIfAbsent(stringCache, name, new WeakReference<>(type));
        }
        return type;
    }
    
    @Override
    public Type getType(String descriptor){
        return getString(descriptor, Type::getType);
    }
    
    @Override
    public Type getObjectType(String name){
        return getString(name, Type::getObjectType);
    }
    
    @Override
    public Type getMethodType(String descriptor){
        return getString(descriptor, Type::getMethodType);
    }
    
    @Override
    public Type getMethodType(Type returnType, Type... arguments){
        Pair<Type, Type[]> key = Pair.of(returnType, arguments);
        WeakReference<Type> ref = methodLocker.computeIfAbsent(methodCache, key, (n)->new WeakReference<>(Type.getMethodType(returnType, arguments)));
        Type type = ref.get();
        if(type == null){
            type = Type.getMethodType(returnType, arguments);
            methodLocker.putIfAbsent(methodCache, key, new WeakReference<>(type));
        }
        return type;
    }
}
