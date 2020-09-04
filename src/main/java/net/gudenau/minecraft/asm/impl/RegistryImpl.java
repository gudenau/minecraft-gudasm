package net.gudenau.minecraft.asm.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.gudenau.minecraft.asm.api.v0.ClassCache;
import net.gudenau.minecraft.asm.api.v0.AsmRegistry;
import net.gudenau.minecraft.asm.api.v0.Identifier;
import net.gudenau.minecraft.asm.api.v0.Transformer;

// Basic registry implementation
public class RegistryImpl implements AsmRegistry{
    public static final RegistryImpl INSTANCE = new RegistryImpl();
    
    private final List<Transformer> earlyTransformers = new LinkedList<>();
    private final List<Transformer> transformers = new LinkedList<>();
    private final List<ClassCache> classCaches = new LinkedList<>();
    
    private volatile Boolean frozen = null;
    
    private RegistryImpl(){}
    
    @Override
    public void registerEarlyTransformer(Transformer transformer){
        if(frozen == null || frozen){
            throw new RuntimeException("Attempted to register transformer outside initializer");
        }
        earlyTransformers.add(transformer);
    }
    
    @Override
    public void registerTransformer(Transformer transformer){
        if(frozen == null || frozen){
            throw new RuntimeException("Attempted to register transformer outside initializer");
        }
        transformers.add(transformer);
    }
    
    @Override
    public void registerClassCache(ClassCache cache){
        if(frozen == null || frozen){
            throw new RuntimeException("Attempted to register class cache outside initializer");
        }
        classCaches.add(cache);
    }
    
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<ClassCache> cache;
    
    @SuppressWarnings("OptionalAssignedToNull")
    public Optional<ClassCache> getCache(){
        if(cache == null){
            if(classCaches.isEmpty()){
                cache = Optional.empty();
            }else{
                String enabled = Configuration.ENABLED_CACHE.get();
                if(enabled != null){
                    Optional<ClassCache> existing = classCaches.stream()
                        .filter((c)->c.getName().toString().equals(enabled))
                        .findAny();
                    if(existing.isPresent()){
                        cache = existing;
                        return existing;
                    }
                }
                
                ClassCache newCache = classCaches.get(0);
                Configuration.ENABLED_CACHE.set(newCache.getName().toString());
                cache = Optional.of(newCache);
            }
        }
        return cache;
    }
    
    public List<String> getCacheNames(){
        return classCaches.stream().map(ClassCache::getName).map(Identifier::toString).collect(Collectors.toList());
    }
    
    public List<Transformer> getTransformers(){
        return transformers;
    }
    
    public List<Transformer> getEarlyTransformers(){
        return earlyTransformers;
    }
    
    @SuppressWarnings("NonAtomicOperationOnVolatileField")
    public void setFrozen(boolean frozen){
        if(this.frozen == null){
            this.frozen = frozen;
        }else{
            this.frozen |= frozen;
        }
    }
}
