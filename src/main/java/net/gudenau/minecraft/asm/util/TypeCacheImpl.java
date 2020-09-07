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
    {
        stringLocker.writeLock(()->{
            // Warm up the primitives since these are `public static final`
            stringCache.put(Type.VOID_TYPE.getDescriptor(), new WeakReference<>(Type.VOID_TYPE));
            stringCache.put(Type.BOOLEAN_TYPE.getDescriptor(), new WeakReference<>(Type.BOOLEAN_TYPE));
            stringCache.put(Type.CHAR_TYPE.getDescriptor(), new WeakReference<>(Type.CHAR_TYPE));
            stringCache.put(Type.BYTE_TYPE.getDescriptor(), new WeakReference<>(Type.BYTE_TYPE));
            stringCache.put(Type.SHORT_TYPE.getDescriptor(), new WeakReference<>(Type.SHORT_TYPE));
            stringCache.put(Type.INT_TYPE.getDescriptor(), new WeakReference<>(Type.INT_TYPE));
            stringCache.put(Type.LONG_TYPE.getDescriptor(), new WeakReference<>(Type.LONG_TYPE));
            stringCache.put(Type.DOUBLE_TYPE.getDescriptor(), new WeakReference<>(Type.DOUBLE_TYPE));
        });
    }
    
    private final Locker methodLocker = new Locker();
    private final Map<Pair<Type, Type[]>, WeakReference<Type>> methodCache = new Object2ObjectOpenHashMap<>();

    private final Locker typeLocker = new Locker();
    private final Map<Class<?>, WeakReference<Type>> typeCache = new Object2ObjectOpenHashMap<>();
    
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
    public Type getType(Class<?> klass){
        WeakReference<Type> ref = typeLocker.computeIfAbsent(typeCache, klass, (n)->new WeakReference<>(Type.getType(n)));
        Type type = ref.get();
        if(type == null){
            type = Type.getType(klass);
            typeLocker.putIfAbsent(typeCache, klass, new WeakReference<>(type));
        }
        return type;
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
