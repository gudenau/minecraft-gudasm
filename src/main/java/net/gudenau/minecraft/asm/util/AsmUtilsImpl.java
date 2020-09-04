package net.gudenau.minecraft.asm.util;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.gudenau.minecraft.asm.api.v0.AsmUtils;
import net.gudenau.minecraft.asm.api.v0.TypeCache;
import net.gudenau.minecraft.asm.api.v0.functional.BooleanFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class AsmUtilsImpl implements AsmUtils{
    public static final AsmUtils INSTANCE = new AsmUtilsImpl();
    private static final IntSet METHOD_OPCODES = new IntOpenHashSet(new int[]{
        INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC, INVOKEINTERFACE
    });
    private static final IntSet RETURN_OPCODES = new IntOpenHashSet(new int[]{
        IRETURN, LRETURN, FRETURN, DRETURN, ARETURN, RETURN
    });
    private final TypeCache typeCache = new TypeCacheImpl();
    
    private AsmUtilsImpl(){}
    
    @Override
    public @NotNull TypeCache getTypeCache(){
        return typeCache;
    }
    
    @Override
    public List<AnnotationNode> getAnnotations(@Nullable List<AnnotationNode> visibleAnnotations, @Nullable List<AnnotationNode> invisibleAnnotations, @NotNull Type type){
        String desc = type.getDescriptor();
        List<AnnotationNode> annotations = new ArrayList<>();
        if(visibleAnnotations != null && !visibleAnnotations.isEmpty()){
            for(AnnotationNode annotation : visibleAnnotations){
                if(desc.equals(annotation.desc)){
                    annotations.add(annotation);
                }
            }
        }
        if(invisibleAnnotations != null && !invisibleAnnotations.isEmpty()){
            for(AnnotationNode annotation : invisibleAnnotations){
                if(desc.equals(annotation.desc)){
                    annotations.add(annotation);
                }
            }
        }
        return annotations;
    }
    
    @Override
    public Optional<AnnotationNode> getAnnotation(@Nullable List<AnnotationNode> visibleAnnotations, @Nullable List<AnnotationNode> invisibleAnnotations, @NotNull Type type){
        String desc = type.getDescriptor();
        if(visibleAnnotations != null && !visibleAnnotations.isEmpty()){
            for(AnnotationNode annotation : visibleAnnotations){
                if(desc.equals(annotation.desc)){
                    return Optional.of(annotation);
                }
            }
        }
        if(invisibleAnnotations != null && !invisibleAnnotations.isEmpty()){
            for(AnnotationNode annotation : invisibleAnnotations){
                if(desc.equals(annotation.desc)){
                    return Optional.of(annotation);
                }
            }
        }
        return Optional.empty();
    }
    
    @Override
    public void addAnnotations(@NotNull MethodNode method, boolean visible, @NotNull Collection<AnnotationNode> annotations){
        List<AnnotationNode> nodes = visible ? method.visibleAnnotations : method.invisibleAnnotations;
        if(nodes == null){
            nodes = new ArrayList<>();
            if(visible){
                method.visibleAnnotations = nodes;
            }else{
                method.invisibleAnnotations = nodes;
            }
        }
        nodes.addAll(annotations);
    }
    
    @Override
    public void addAnnotations(@NotNull ClassNode owner, boolean visible, @NotNull Collection<AnnotationNode> annotations){
        List<AnnotationNode> nodes = visible ? owner.visibleAnnotations : owner.invisibleAnnotations;
        if(nodes == null){
            nodes = new ArrayList<>();
            if(visible){
                owner.visibleAnnotations = nodes;
            }else{
                owner.invisibleAnnotations = nodes;
            }
        }
        nodes.addAll(annotations);
    }
    
    @Override
    public boolean removeAnnotations(@Nullable List<AnnotationNode> visibleAnnotations, @Nullable List<AnnotationNode> invisibleAnnotations, @NotNull Collection<AnnotationNode> annotations){
        boolean change = false;
        if(visibleAnnotations != null && !visibleAnnotations.isEmpty()){
            change |= visibleAnnotations.removeAll(annotations);
        }
        if(invisibleAnnotations != null && !invisibleAnnotations.isEmpty()){
            change |= invisibleAnnotations.removeAll(annotations);
        }
        return change;
    }
    
    @Override
    public boolean removeAnnotations(@Nullable List<AnnotationNode> visibleAnnotations, @Nullable List<AnnotationNode> invisibleAnnotations, @NotNull Type type){
        //TODO make this less stupid
        return removeAnnotations(visibleAnnotations, invisibleAnnotations, getAnnotations(visibleAnnotations, invisibleAnnotations, type));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <T extends AbstractInsnNode> List<T> findMatchingNodes(@NotNull InsnList instructions, @NotNull BooleanFunction<AbstractInsnNode> checker){
        List<T> result = new ArrayList<>();
        for(AbstractInsnNode instruction : instructions){
            if(checker.apply(instruction)){
                result.add((T)instruction);
            }
        }
        return result;
    }
    
    // This is nasty, but should hopefully be somewhat faster than doing more checks in the loop
    @Override
    public @NotNull List<MethodInsnNode> findMethodCalls(@NotNull InsnList instructions, int opcode, @Nullable String owner, @Nullable String name, @Nullable String description){
        BooleanFunction<AbstractInsnNode> checker;
        if(opcode == -1){
            if(owner == null){
                if(name == null){
                    if(description == null){
                        checker = (node)->METHOD_OPCODES.contains(node.getOpcode());
                    }else{
                        checker = (node)->
                            METHOD_OPCODES.contains(node.getOpcode()) &&
                            description.equals(((MethodInsnNode)node).desc);
                    }
                }else{
                    if(description == null){
                        checker = (node)->
                            METHOD_OPCODES.contains(node.getOpcode()) &&
                            name.equals(((MethodInsnNode)node).name);
                    }else{
                        checker = (node)->{
                            if(METHOD_OPCODES.contains(node.getOpcode())){
                                MethodInsnNode method = (MethodInsnNode)node;
                                return name.equals(method.name) &&
                                       description.equals(method.desc);
                            }else{
                                return false;
                            }
                        };
                    }
                }
            }else{
                if(name == null){
                    if(description == null){
                        checker = (node)->
                            METHOD_OPCODES.contains(node.getOpcode()) &&
                            owner.equals(((MethodInsnNode)node).owner);
                    }else{
                        checker = (node)->{
                            if(METHOD_OPCODES.contains(node.getOpcode())){
                                MethodInsnNode method = (MethodInsnNode)node;
                                return description.equals(method.desc) &&
                                       owner.equals(method.owner);
                            }else{
                                return false;
                            }
                        };
                    }
                }else{
                    if(description == null){
                        checker = (node)->{
                            if(METHOD_OPCODES.contains(node.getOpcode())){
                                MethodInsnNode method = (MethodInsnNode)node;
                                return name.equals(method.name) &&
                                       owner.equals(method.owner);
                            }else{
                                return false;
                            }
                        };
                    }else{
                        checker = (node)->{
                            if(METHOD_OPCODES.contains(node.getOpcode())){
                                MethodInsnNode method = (MethodInsnNode)node;
                                return name.equals(method.name) &&
                                       owner.equals(method.owner) &&
                                       description.equals(method.desc);
                            }else{
                                return false;
                            }
                        };
                    }
                }
            }
        }else{
            if(owner == null){
                if(name == null){
                    if(description == null){
                        checker = (node)->opcode == node.getOpcode();
                    }else{
                        checker = (node)->
                            opcode == node.getOpcode() &&
                            description.equals(((MethodInsnNode)node).desc);
                    }
                }else{
                    if(description == null){
                        checker = (node)->
                            opcode == node.getOpcode() &&
                            name.equals(((MethodInsnNode)node).name);
                    }else{
                        checker = (node)->{
                            if(opcode == node.getOpcode()){
                                MethodInsnNode method = (MethodInsnNode)node;
                                return name.equals(method.name) &&
                                       description.equals(method.desc);
                            }else{
                                return false;
                            }
                        };
                    }
                }
            }else{
                if(name == null){
                    if(description == null){
                        checker = (node)->
                            opcode == node.getOpcode() &&
                            owner.equals(((MethodInsnNode)node).owner);
                    }else{
                        checker = (node)->{
                            if(opcode == node.getOpcode()){
                                MethodInsnNode method = (MethodInsnNode)node;
                                return description.equals(method.desc) &&
                                       owner.equals(method.owner);
                            }else{
                                return false;
                            }
                        };
                    }
                }else{
                    if(description == null){
                        checker = (node)->{
                            if(opcode == node.getOpcode()){
                                MethodInsnNode method = (MethodInsnNode)node;
                                return name.equals(method.name) &&
                                       owner.equals(method.owner);
                            }else{
                                return false;
                            }
                        };
                    }else{
                        checker = (node)->{
                            if(opcode == node.getOpcode()){
                                MethodInsnNode method = (MethodInsnNode)node;
                                return name.equals(method.name) &&
                                       owner.equals(method.owner) &&
                                       description.equals(method.desc);
                            }else{
                                return false;
                            }
                        };
                    }
                }
            }
        }
        return findMatchingNodes(instructions, checker);
    }
    
    @Override
    public @NotNull List<AbstractInsnNode> findSurroundingNodes(@NotNull AbstractInsnNode node, int leading, int trailing){
        List<AbstractInsnNode> nodes = new ArrayList<>();
        AbstractInsnNode current = node.getPrevious();
        if(leading > 0){
            for(int i = 0; i < leading && current != null; i++){
                nodes.add(current);
                current = current.getPrevious();
            }
            if(!nodes.isEmpty()){
                Collections.reverse(nodes);
            }
        }
        nodes.add(node);
        current = node.getNext();
        if(trailing > 0){
            for(int i = 0; i < trailing && current != null; i++){
                nodes.add(current);
                current = current.getNext();
            }
        }
        return nodes;
    }

    @Override
    public @NotNull List<InsnNode> findReturns(@NotNull InsnList instructions){
        return findMatchingNodes(instructions, (node)->RETURN_OPCODES.contains(node.getOpcode()));
    }
    
    @Override
    public @Nullable List<AbstractInsnNode> findInRange(AbstractInsnNode start, AbstractInsnNode end){
        List<AbstractInsnNode> nodes = new ArrayList<>();
        AbstractInsnNode current = start.getNext();
        while(current != null && current != end){
            nodes.add(current);
            current = current.getNext();
        }
        return current == null ? null : nodes;
    }
    
    @Override
    public int getOpcodeFromHandleTag(int tag){
        switch(tag){
            case H_GETFIELD: return GETFIELD;
            case H_GETSTATIC: return GETSTATIC;
            case H_PUTFIELD: return PUTFIELD;
            case H_PUTSTATIC: return PUTSTATIC;
            case H_INVOKEVIRTUAL: return INVOKEVIRTUAL;
            case H_INVOKESTATIC: return INVOKESTATIC;
            case H_INVOKESPECIAL: return INVOKESPECIAL;
            case H_NEWINVOKESPECIAL: return NEW;
            case H_INVOKEINTERFACE: return INVOKEINTERFACE;
            default: return -1;
        }
    }
    
    @Override
    public int getHandleTagFromOpcode(int opcode){
        switch(opcode){
            case GETFIELD: return H_GETFIELD;
            case GETSTATIC: return H_GETSTATIC;
            case PUTFIELD: return H_PUTFIELD;
            case PUTSTATIC: return H_PUTSTATIC;
            case INVOKEVIRTUAL: return H_INVOKEVIRTUAL;
            case INVOKESTATIC: return H_INVOKESTATIC;
            case INVOKESPECIAL: return H_INVOKESPECIAL;
            case NEW: return H_NEWINVOKESPECIAL;
            case INVOKEINTERFACE: return H_INVOKEINTERFACE;
            default: return -1;
        }
    }
    
    @Override
    public @NotNull InsnList createExceptionList(@NotNull Type type, @Nullable String message){
        InsnList instructions = new InsnList();
        String name = type.getInternalName();
        instructions.add(new TypeInsnNode(NEW, name));
        instructions.add(new InsnNode(DUP));
        if(message == null){
            instructions.add(new MethodInsnNode(INVOKESPECIAL, name, "<init>", "()V", false));
        }else{
            instructions.add(new LdcInsnNode(message));
            instructions.add(new MethodInsnNode(INVOKESPECIAL, name, "<init>", "(Ljava/lang/String;)V", false));
        }
        instructions.add(new InsnNode(ATHROW));
        return instructions;
    }
    
    @Override
    public @NotNull Optional<MethodNode> findMethod(@NotNull ClassNode owner, @NotNull String name, @NotNull String desc){
        for(MethodNode method : owner.methods){
            if(
                name.equals(method.name) &&
                desc.equals(method.desc)
            ){
                return Optional.of(method);
            }
        }
        return Optional.empty();
    }
    
    @Override
    public @NotNull String getOpcodeName(int opcode){
        switch(opcode){
            case NOP: return "NOP";
            case ACONST_NULL: return "ACONST_NULL";
            case ICONST_M1: return "ICONST_M1";
            case ICONST_0: return "ICONST_0";
            case ICONST_1: return "ICONST_1";
            case ICONST_2: return "ICONST_2";
            case ICONST_3: return "ICONST_3";
            case ICONST_4: return "ICONST_4";
            case ICONST_5: return "ICONST_5";
            case LCONST_0: return "LCONST_0";
            case LCONST_1: return "LCONST_1";
            case FCONST_0: return "FCONST_0";
            case FCONST_1: return "FCONST_1";
            case FCONST_2: return "FCONST_2:";
            case DCONST_0: return "DCONST_0";
            case DCONST_1: return "DCONST_1";
            case BIPUSH: return "BIPUSH";
            case SIPUSH: return "SIPUSH";
            case LDC: return "LDC";
            case ILOAD: return "ILOAD";
            case LLOAD: return "LLOAD";
            case FLOAD: return "FLOAD";
            case DLOAD: return "DLOAD";
            case ALOAD: return "ALOAD";
            case IALOAD: return "IALOAD";
            case LALOAD: return "LALOAD";
            case FALOAD: return "FALOAD";
            case DALOAD: return "DALOAD";
            case AALOAD: return "AALOAD";
            case BALOAD: return "BALOAD";
            case CALOAD: return "CALOAD";
            case SALOAD: return "SALOAD";
            case ISTORE: return "ISTORE";
            case LSTORE: return "LSTORE";
            case FSTORE: return "FSTORE";
            case DSTORE: return "DSTORE";
            case ASTORE: return "ASTORE";
            case IASTORE: return "IASTORE";
            case LASTORE: return "LASTORE";
            case FASTORE: return "FASTORE";
            case DASTORE: return "DASTORE";
            case AASTORE: return "AASTORE";
            case BASTORE: return "BASTORE";
            case CASTORE: return "CASTORE";
            case SASTORE: return "SASTORE";
            case POP: return "POP";
            case POP2: return "POP2";
            case DUP: return "DUP";
            case DUP_X1: return "DUP_X1";
            case DUP_X2: return "DUP_X2";
            case DUP2: return "DUP2:";
            case DUP2_X1: return "DUP2_X1";
            case DUP2_X2: return "DUP2_X2";
            case SWAP: return "SWAP";
            case IADD: return "IADD";
            case LADD: return "LADD:";
            case FADD: return "FADD";
            case DADD: return "DADD";
            case ISUB: return "ISUB";
            case LSUB: return "LSUB";
            case FSUB: return "FSUB";
            case DSUB: return "DSUB";
            case IMUL: return "IMUL";
            case LMUL: return "LMUL";
            case FMUL: return "FMUL";
            case DMUL: return "DMUL";
            case IDIV: return "IDIV";
            case LDIV: return "LDIV";
            case FDIV: return "FDIV";
            case DDIV: return "DDIV";
            case IREM: return "IREM";
            case LREM: return "LREM";
            case FREM: return "FREM";
            case DREM: return "DREM";
            case INEG: return "INEG";
            case LNEG: return "LNEG";
            case FNEG: return "FNEG";
            case DNEG: return "DNEG";
            case ISHL: return "ISHL";
            case LSHL: return "LSHL";
            case ISHR: return "ISHR";
            case LSHR: return "LSHR";
            case IUSHR: return "IUSHR";
            case LUSHR: return "LUSHR";
            case IAND: return "IAND";
            case LAND: return "LAND";
            case IOR: return "IOR";
            case LOR: return "LOR";
            case IXOR: return "IXOR";
            case LXOR: return "LXOR";
            case IINC: return "IINC:";
            case I2L: return "I2L";
            case I2F: return "I2F:";
            case I2D: return "I2D";
            case L2I: return "L2I";
            case L2F: return "L2F";
            case L2D: return "L2D";
            case F2I: return "F2I";
            case F2L: return "F2L";
            case F2D: return "F2D";
            case D2I: return "D2I";
            case D2L: return "D2L";
            case D2F: return "D2F";
            case I2B: return "I2B";
            case I2C: return "I2C";
            case I2S: return "I2S";
            case LCMP: return "LCMP:";
            case FCMPL: return "FCMPL";
            case FCMPG: return "FCMPG";
            case DCMPL: return "DCMPL";
            case DCMPG: return "DCMPG";
            case IFEQ: return "IFEQ:";
            case IFNE: return "IFNE";
            case IFLT: return "IFLT";
            case IFGE: return "IFGE";
            case IFGT: return "IFGT";
            case IFLE: return "IFLE";
            case IF_ICMPEQ: return "IF_ICMPEQ";
            case IF_ICMPNE: return "IF_ICMPNE";
            case IF_ICMPLT: return "IF_ICMPLT";
            case IF_ICMPGE: return "IF_ICMPGE";
            case IF_ICMPGT: return "IF_ICMPGT";
            case IF_ICMPLE: return "IF_ICMPLE";
            case IF_ACMPEQ: return "IF_ACMPEQ";
            case IF_ACMPNE: return "IF_ACMPNE";
            case GOTO: return "GOTO";
            case JSR: return "JSR";
            case RET: return "RET";
            case TABLESWITCH: return "TABLESWITCH";
            case LOOKUPSWITCH: return "LOOKUPSWITCH";
            case IRETURN: return "IRETURN";
            case LRETURN: return "LRETURN";
            case FRETURN: return "FRETURN";
            case DRETURN: return "DRETURN";
            case ARETURN: return "ARETURN";
            case RETURN: return "RETURN";
            case GETSTATIC: return "GETSTATIC";
            case PUTSTATIC: return "PUTSTATIC";
            case GETFIELD: return "GETFIELD";
            case PUTFIELD: return "PUTFIELD";
            case INVOKEVIRTUAL: return "INVOKEVIRTUAL";
            case INVOKESPECIAL: return "INVOKESPECIAL";
            case INVOKESTATIC: return "INVOKESTATIC";
            case INVOKEINTERFACE: return "INVOKEINTERFACE";
            case INVOKEDYNAMIC: return "INVOKEDYNAMIC";
            case NEW: return "NEW:";
            case NEWARRAY: return "NEWARRAY";
            case ANEWARRAY: return "ANEWARRAY";
            case ARRAYLENGTH: return "ARRAYLENGTH";
            case ATHROW: return "ATHROW";
            case CHECKCAST: return "CHECKCAST";
            case INSTANCEOF: return "INSTANCEOF";
            case MONITORENTER: return "MONITORENTER";
            case MONITOREXIT: return "MONITOREXIT";
            case MULTIANEWARRAY: return "MULTIANEWARRAY";
            case IFNULL: return "IFNULL";
            case IFNONNULL: return "IFNONNULL";
            default: return "UNKNOWN";
        }
    }
}
