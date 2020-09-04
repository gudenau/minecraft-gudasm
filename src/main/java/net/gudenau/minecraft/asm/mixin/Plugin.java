package net.gudenau.minecraft.asm.mixin;

import java.util.List;
import java.util.Set;
import net.gudenau.minecraft.asm.impl.Bootstrap;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

/**
 * The part that kicks off the entire mess.
 * */
public class Plugin implements IMixinConfigPlugin{
    static{
        Bootstrap.setup();
    }
    
    @Override
    public void onLoad(String mixinPackage){}
    
    @Override
    public String getRefMapperConfig(){
        return null;
    }
    
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName){
        return true;
    }
    
    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets){}
    
    @Override
    public List<String> getMixins(){
        return null;
    }
    
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo){}
    
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo){}
}
