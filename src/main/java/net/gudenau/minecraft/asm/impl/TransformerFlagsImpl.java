package net.gudenau.minecraft.asm.impl;

import net.gudenau.minecraft.asm.api.v0.Transformer;
import org.objectweb.asm.ClassWriter;

public class TransformerFlagsImpl implements Transformer.Flags{
    private boolean computeMaxes = false;
    private boolean computeFrames = false;
    
    public void requestMaxes(){
        computeMaxes = true;
    }
    
    public void requestFrames(){
        computeFrames = true;
    }
    
    public int getClassWriterFlags(){
        return (computeFrames ? ClassWriter.COMPUTE_FRAMES : 0) |
               (computeMaxes ? ClassWriter.COMPUTE_MAXS : 0);
    }
}
