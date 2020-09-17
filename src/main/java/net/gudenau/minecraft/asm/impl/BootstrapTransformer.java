package net.gudenau.minecraft.asm.impl;

import java.util.List;
import net.gudenau.minecraft.asm.api.v0.AsmUtils;
import net.gudenau.minecraft.asm.api.v0.Identifier;
import net.gudenau.minecraft.asm.api.v0.Transformer;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * A simple transformer to enable some JVM abuse.
 * */
public class BootstrapTransformer implements Transformer{
    // On the off chance that ForceInline is not around, we should not use it.
    private static final boolean ENABLED;
    
    static{
        boolean enable;
        try{
            ReflectionHelper.loadClass("jdk.internal.vm.annotation.ForceInline");
            enable = true;
        }catch(Throwable ignored){
            enable = false;
        }
        ENABLED = enable;
    }
    
    private static final AsmUtils ASM_UTILS = AsmUtils.getInstance();
    private static final Type FORCEBOOTLOADER = Type.getObjectType("net/gudenau/minecraft/asm/api/v0/annotation/ForceBootloader");
    private static final Type ASM_FORCEINLINE = Type.getObjectType("net/gudenau/minecraft/asm/api/v0/annotation/ForceInline");
    private static final Type JVM_FORCEINLINE = Type.getObjectType("jdk/internal/vm/annotation/ForceInline");
    
    @Override
    public Identifier getName(){
        return new Identifier("gud_asm", "bootstrap");
    }
    
    // Special case, this is always true when called.
    @Override
    public boolean handlesClass(String name, String transformedName){
        return ENABLED;
    }
    
    @Override
    public boolean transform(ClassNode classNode, Flags flags){
        boolean changed = ASM_UTILS.removeAnnotations(classNode, FORCEBOOTLOADER);
        
        for(MethodNode method : classNode.methods){
            List<AnnotationNode> annotations = ASM_UTILS.getAnnotations(method, ASM_FORCEINLINE);
            if(!annotations.isEmpty()){
                for(AnnotationNode annotation : annotations){
                    annotation.desc = JVM_FORCEINLINE.getDescriptor();
                    changed = true;
                }
            }
        }
        
        return changed;
    }
}
