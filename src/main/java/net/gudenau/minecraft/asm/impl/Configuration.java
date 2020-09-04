package net.gudenau.minecraft.asm.impl;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Basic config for the mod
 */
public class Configuration{
    /**
     * Selects the enable class cache, if available.
     * */
    public static final Value<String> ENABLED_CACHE = new ListValue("cache", RegistryImpl.INSTANCE.getCacheNames());
    
    /**
     * The class dumping mode for debugging.
     * */
    public static final Value<DumpMode> DUMP = new EnumValue<>("dump", DumpMode.OFF);
    
    private static final Map<String, Value<?>> VALUES;
    static{
        Map<String, Value<?>> values = new Object2ObjectOpenHashMap<>();
        values.put(ENABLED_CACHE.getName(), ENABLED_CACHE);
        values.put(DUMP.getName(), DUMP);
        VALUES = Collections.unmodifiableMap(values);
        
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try{
                save(false);
            }catch(IOException e){
                e.printStackTrace();
            }
        }, "gudASM Config Saver"));
    }
    
    /**
     * The dump mode.
     * */
    public enum DumpMode{
        /**
         * Don't dump any classes.
         * */
        OFF,
        /**
         * Dump classes we changed.
         * */
        ON,
        /**
         * Dump everything.
         * */
        FORCE
    }
    
    /**
     * Load the config from disk.
     * */
    public static void load() throws IOException{
        Path gudConfig = FabricLoader.getInstance().getConfigDir().resolve("gud");
        if(!Files.exists(gudConfig)){
            Files.createDirectories(gudConfig);
        }
        
        Path configPath = gudConfig.resolve("asm.cfg");
        if(Files.exists(configPath)){
            Map<String, String> rawConfig = new Object2ObjectOpenHashMap<>();
            try(BufferedReader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)){
                for(String line = reader.readLine(); line != null; line = reader.readLine()){
                    if(line.isEmpty()){
                        continue;
                    }
                    String original = line;
                    int index = line.indexOf("#");
                    if(index != -1){
                        line = line.substring(0, index);
                    }
                    index = line.indexOf("=");
                    if(index == -1){
                        System.err.printf(
                            "Invalid line \"%s\" in \"gud/asm.cfg\"",
                            original
                        );
                    }
                    
                    String[] split = line.split("=", 2);
                    rawConfig.put(split[0], split[1]);
                }
            }
    
            Set<Value<?>> valueSet = new HashSet<>(VALUES.values());
            for(Map.Entry<String, String> entry : rawConfig.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                Value<?> config = VALUES.get(key);
                if(config != null){
                    config.parse(value);
                    valueSet.remove(config);
                }
            }
            
            if(!valueSet.isEmpty()){
                save(true);
            }
        }else{
            save(true);
        }
    }
    
    /**
     * Save the configs.
     *
     * @param force Skip dirty checks
     * */
    private static void save(boolean force) throws IOException{
        Map<String, String> options = new Object2ObjectOpenHashMap<>();
        List<String> keys = new ArrayList<>();
        
        boolean shouldSave = force;
        for(Value<?> value : VALUES.values()){
            String key = value.getName();
            keys.add(key);
            Object valueValue = value.get();
            options.put(key, valueValue == null ? "" : String.valueOf(valueValue).toLowerCase());
            shouldSave |= value.isDirty();
            value.clean();
        }
        
        if(!shouldSave){
            return;
        }
        
        keys.sort(String::compareToIgnoreCase);
    
        Path gudConfig = FabricLoader.getInstance().getConfigDir().resolve("gud");
        if(!Files.exists(gudConfig)){
            Files.createDirectories(gudConfig);
        }
    
        Path configPath = gudConfig.resolve("asm.cfg");
        
        try(BufferedWriter writer = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE)){
            for(String key : keys){
                writer.write(key);
                writer.write('=');
                writer.write(options.get(key));
                writer.write('\n');
            }
        }
    }
    
    public static class Value<T>{
        private final String name;
        private final Function<String, T> parser;
        private T value;
        private boolean dirty = false;
    
        Value(String name, T defaultValue, Function<String, T> parser){
            this.name = name;
            this.value = defaultValue;
            this.parser = parser;
        }
        
        void parse(String rawValue){
            T value = parser.apply(rawValue);
            if(value != null){
                this.value = value;
            }
        }
    
        public String getName(){
            return name;
        }
    
        public T get(){
            return value;
        }
    
        public void set(T value){
            this.value = value;
            dirty = true;
        }
    
        boolean isDirty(){
            return dirty;
        }
        
        void clean(){
            dirty = false;
        }
    }
    
    private static class ListValue extends Value<String>{
        ListValue(String name, List<String> values){
            super(name, values.isEmpty() ? null : values.get(0), (key)->
                values.contains(key) ? key : null
            );
        }
    }
    
    private static class EnumValue<E extends Enum<E>> extends Value<E>{
        EnumValue(String name, E defaultValue){
            super(name, defaultValue, (key)->{
                for(E value : defaultValue.getDeclaringClass().getEnumConstants()){
                    if(value.name().equalsIgnoreCase(key)){
                        return value;
                    }
                }
                return null;
            });
        }
    }
}
