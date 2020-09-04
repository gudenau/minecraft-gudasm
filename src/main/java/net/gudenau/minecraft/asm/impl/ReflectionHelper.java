package net.gudenau.minecraft.asm.impl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Please be good and never touch this outside gudASM.
 * */
@SuppressWarnings({"SameParameterValue", "unused", "RedundantSuppression"})
public class ReflectionHelper{
    private static final long AccessibleObject$override = findOverride();
    
    private static final MethodHandles.Lookup IMPL_LOOKUP = forceGetField(
        MethodHandles.Lookup.class,
        null,
        Modifier.STATIC | Modifier.FINAL,
        MethodHandles.Lookup.class
    );
    
    @SuppressWarnings("unchecked")
    private static <O, T> T forceGetField(Class<O> owner, O instance, int mods, Class<T> type){
        for(Field field : owner.getDeclaredFields()){
            if(
                field.getModifiers() == mods &&
                field.getType() == type
            ){
                try{
                    forceSetAccessible(field, true);
                    return (T)field.get(instance);
                }catch(ReflectiveOperationException ignored){}
            }
        }
        throw new RuntimeException(String.format(
            "Failed to get field from %s of type %s",
            owner.getName(),
            type.getName()
        ));
    }
    
    @SuppressWarnings("deprecation")
    private static long findOverride(){
        AccessibleObject object = UnsafeHelper.allocateInstance(AccessibleObject.class);
        for(long cookie = 0; cookie < 64; cookie += 4){
            int original = UnsafeHelper.getInt(object, cookie);
            object.setAccessible(true);
            if(original != UnsafeHelper.getInt(object, cookie)){
                UnsafeHelper.putInt(object, cookie, original);
                if(!object.isAccessible()){
                    return cookie;
                }
            }
            object.setAccessible(false);
        }
        return -1;
    }
    
    private static void forceSetAccessible(AccessibleObject object, boolean accessible){
        UnsafeHelper.putInt(object, AccessibleObject$override, accessible ? 1 : 0);
    }
    
    public static MethodHandle findGetter(Class<?> owner, String name, Class<?> type) throws ReflectiveOperationException{
        return IMPL_LOOKUP.findGetter(owner, name, type);
    }
    
    public static <O, T extends O> MethodHandle findGetter(Class<T> owner, O instance, String name, Class<?> type) throws ReflectiveOperationException{
        return IMPL_LOOKUP.findGetter(owner, name, type).bindTo(instance);
    }
    
    public static MethodHandle findStaticGetter(Class<?> owner, String name, Class<?> type) throws ReflectiveOperationException{
        return IMPL_LOOKUP.findStaticGetter(owner, name, type);
    }
    
    public static MethodHandle findSetter(Class<?> owner, String name, Class<?> type) throws ReflectiveOperationException{
        return IMPL_LOOKUP.findSetter(owner, name, type);
    }
    
    public static <O, T extends O> MethodHandle findSetter(Class<T> owner, O instance, String name, Class<?> type) throws ReflectiveOperationException{
        return IMPL_LOOKUP.findSetter(owner, name, type).bindTo(instance);
    }
    
    public static MethodHandle findStaticSetter(Class<?> owner, String name, Class<?> type) throws ReflectiveOperationException{
        return IMPL_LOOKUP.findStaticSetter(owner, name, type);
    }
    
    public static <T> Class<T> loadClass(String name) throws ReflectiveOperationException{
        return loadClass(ReflectionHelper.class.getClassLoader(), name);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> Class<T> loadClass(ClassLoader loader, String name) throws ReflectiveOperationException{
        return (Class<T>)loader.loadClass(name);
    }
    
    public static <O, T extends O> MethodHandle findVirtual(Class<T> owner, O instance, String name, MethodType type) throws ReflectiveOperationException{
        return IMPL_LOOKUP.findVirtual(owner, name, type).bindTo(instance);
    }
    
    public static MethodHandle findStatic(Class<?> owner, String name, Class<?> returnType, Class<?>... params) throws ReflectiveOperationException{
        return IMPL_LOOKUP.findStatic(
            owner,
            name,
            MethodType.methodType(returnType, params)
        );
    }
    
    private static final class UnsafeHelper{
        private static final Class<?> Unsafe = loadUnsafe();
        private static final Object theUnsafe = getUnsafe();
        
        private static Class<?> loadUnsafe(){
            try{
                return loadClass("sun.misc.Unsafe");
            }catch(ReflectiveOperationException e2){
                System.err.println("Failed to load Unsafe class");
                e2.printStackTrace();
                System.exit(0);
                return null;
            }
        }
        
        private static Object getUnsafe(){
            final int modifiers = Modifier.PRIVATE | Modifier.STATIC | Modifier.FINAL;
            for(Field field : Unsafe.getDeclaredFields()){
                if(field.getModifiers() == modifiers && field.getType() == Unsafe){
                    try{
                        field.setAccessible(true);
                        Object unsafe = field.get(null);
                        if(unsafe != null){
                            return unsafe;
                        }
                    }catch(ReflectiveOperationException ignored){}
                }
            }
            System.err.println("Failed to find Unsafe instance");
            System.exit(0);
            return null;
        }
        
        private static MethodHandle findMethod(String name, Class<?>... arguments){
            for(Method method : Unsafe.getDeclaredMethods()){
                if(
                    method.getName().equals(name) &&
                    Arrays.equals(arguments, method.getParameterTypes())
                ){
                    try{
                        method.setAccessible(true);
                        MethodHandle handle = MethodHandles.lookup().unreflect(method);
                        return handle.bindTo(theUnsafe);
                    }catch(ReflectiveOperationException ignored){}
                }
            }
            
            System.err.println("Failed to find Unsafe." + name);
            System.exit(0);
            return null;
        }
        
        private static final MethodHandle allocateInstance = findMethod("allocateInstance", Class.class);
        @SuppressWarnings("unchecked")
        static <T> T allocateInstance(Class<T> type){
            try{
                return (T)((Object)allocateInstance.invokeExact(type));
            }catch(Throwable throwable){
                System.err.println("Failed to allocate " + type.getName());
                throwable.printStackTrace();
                System.exit(0);
                return null;
            }
        }
        
        private static final MethodHandle putInt = findMethod("putInt", Object.class, long.class, int.class);
        static void putInt(Object o, long offset, int x){
            try{
                putInt.invokeExact(o, offset, x);
            }catch(Throwable throwable){
                System.err.println("Failed to put int");
                throwable.printStackTrace();
                System.exit(0);
            }
        }
        
        private static final MethodHandle getInt = findMethod("getInt", Object.class, long.class);
        static int getInt(Object o, long offset){
            try{
                return (int)getInt.invokeExact(o, offset);
            }catch(Throwable throwable){
                System.err.println("Failed to get int");
                throwable.printStackTrace();
                System.exit(0);
                return -1;
            }
        }
    }
}
