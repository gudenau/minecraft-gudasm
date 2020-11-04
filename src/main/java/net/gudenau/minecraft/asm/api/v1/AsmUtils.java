package net.gudenau.minecraft.asm.api.v1;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.gudenau.minecraft.asm.api.v1.functional.BooleanFunction;
import net.gudenau.minecraft.asm.api.v1.type.FieldType;
import net.gudenau.minecraft.asm.api.v1.type.MethodType;
import net.gudenau.minecraft.asm.util.AsmUtilsImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * General ASM utils.
 * */
@SuppressWarnings({"unused", "RedundantSuppression", "PointlessBitwiseExpression"}) // Idea is silly
public final class AsmUtils{
    private AsmUtils(){
        throw new RuntimeException("Stop, get some help.");
    }
    
    // --- Annotation stuff ---
    
    /**
     * Checks if a method has an annotation.
     *
     * @param methodNode The method to check
     * @param type The annotation type to check for
     *
     * @return True if the annotation is present, false if absent
     * */
    public static boolean hasAnnotation(@NotNull MethodNode methodNode, @NotNull Type type){
        return hasAnnotation(methodNode.visibleAnnotations, methodNode.invisibleAnnotations, type);
    }
    
    /**
     * Checks if a class has an annotation.
     *
     * @param owner The class to check
     * @param type The annotation type to check for
     *
     * @return True if the annotation is present, false if absent
     * */
    public static boolean hasAnnotation(@NotNull ClassNode owner, @NotNull Type type){
        return hasAnnotation(owner.visibleAnnotations, owner.invisibleAnnotations, type);
    }
    
    /**
     * Checks if an annotation exists in lists.
     *
     * @param visibleAnnotations Visible annotations
     * @param invisibleAnnotations Invisible annotations
     * @param type The annotation type to check for
     *
     * @return True if the annotation is present, false if absent
     * */
    public static boolean hasAnnotation(@Nullable List<@NotNull AnnotationNode> visibleAnnotations, @Nullable List<@NotNull AnnotationNode> invisibleAnnotations, @NotNull Type type){
        return getAnnotation(visibleAnnotations, invisibleAnnotations, type).isPresent();
    }
    
    /**
     * Gets annotations in a method.
     *
     * @param method The method to get annotations from
     * @param type The annotation type to check for
     *
     * @return Found annotations
     * */
    @NotNull
    public static List<@NotNull AnnotationNode> getAnnotations(@NotNull MethodNode method, @NotNull Type type){
        return getAnnotations(method.visibleAnnotations, method.invisibleAnnotations, type);
    }
    
    /**
     * Gets annotations in a class.
     *
     * @param owner The class to get annotations from
     * @param type The annotation type to check for
     *
     * @return Found annotations
     * */
    @NotNull
    public static List<@NotNull AnnotationNode> getAnnotations(@NotNull ClassNode owner, @NotNull Type type){
        return getAnnotations(owner.visibleAnnotations, owner.invisibleAnnotations, type);
    }
    
    /**
     * Gets annotations in the lists.
     *
     * @param visibleAnnotations Visible annotations
     * @param invisibleAnnotations Invisible annotations
     * @param type The annotation type to check for
     *
     * @return Found annotations
     * */
    @NotNull
    public static List<@NotNull AnnotationNode> getAnnotations(@Nullable List<@NotNull AnnotationNode> visibleAnnotations, @Nullable List<@NotNull AnnotationNode> invisibleAnnotations, @NotNull Type type){
        return AsmUtilsImpl.getAnnotations(visibleAnnotations, invisibleAnnotations, type);
    }
    
    /**
     * Gets the first matching annotation in a method.
     *
     * @param method The method to get annotations from
     * @param type The annotation type to check for
     *
     * @return Found annotation
     * */
    @NotNull
    public static Optional<AnnotationNode> getAnnotation(@NotNull MethodNode method, @NotNull Type type){
        return getAnnotation(method.visibleAnnotations, method.invisibleAnnotations, type);
    }
    
    /**
     * Gets the first matching annotation in a class.
     *
     * @param owner The class to get annotations from
     * @param type The annotation type to check for
     *
     * @return Found annotations
     * */
    @NotNull
    public static Optional<AnnotationNode> getAnnotation(@NotNull ClassNode owner, @NotNull Type type){
        return getAnnotation(owner.visibleAnnotations, owner.invisibleAnnotations, type);
    }
    
    /**
     * Gets the first matching annotation in the lists.
     *
     * @param visibleAnnotations Visible annotations
     * @param invisibleAnnotations Invisible annotations
     * @param type The annotation type to check for
     *
     * @return Found annotation
     * */
    @NotNull
    public static Optional<AnnotationNode> getAnnotation(@Nullable List<@NotNull AnnotationNode> visibleAnnotations, @Nullable List<@NotNull AnnotationNode> invisibleAnnotations, @NotNull Type type){
        return AsmUtilsImpl.getAnnotation(visibleAnnotations, invisibleAnnotations, type);
    }
    
    /**
     * Adds an annotation to a method.
     *
     * @param method The method to add annotations to
     * @param visible Are they be visible
     * @param annotation The annotation to add
     * */
    public static void addAnnotation(@NotNull MethodNode method, boolean visible, @NotNull AnnotationNode annotation){
        addAnnotations(method, visible, Collections.singletonList(annotation));
    }
    
    /**
     * Adds an annotation to a class.
     *
     * @param owner The class to add annotations to
     * @param visible Are they be visible
     * @param annotation The annotation to add
     * */
    public static void addAnnotation(@NotNull ClassNode owner, boolean visible, @NotNull AnnotationNode annotation){
        addAnnotations(owner, visible, Collections.singletonList(annotation));
    }
    
    /**
     * Adds annotations to a method.
     *
     * @param method The method to add annotations to
     * @param visible Are they be visible
     * @param annotations The annotations to add
     * */
    public static void addAnnotations(@NotNull MethodNode method, boolean visible, @NotNull AnnotationNode... annotations){
        addAnnotations(method, visible, Arrays.asList(annotations));
    }
    
    /**
     * Adds annotations to a method.
     *
     * @param method The method to add annotations to
     * @param visible Are they be visible
     * @param annotations The annotations to add
     * */
    public static void addAnnotations(@NotNull MethodNode method, boolean visible, @NotNull Collection<@NotNull AnnotationNode> annotations){
        AsmUtilsImpl.addAnnotations(method, visible, annotations);
    }
    
    /**
     * Adds annotations to a class.
     *
     * @param owner The class to add annotations to
     * @param visible Are they be visible
     * @param annotations The annotations to add
     * */
    public static void addAnnotations(@NotNull ClassNode owner, boolean visible, @NotNull AnnotationNode... annotations){
        addAnnotations(owner, visible, Arrays.asList(annotations));
    }
    
    /**
     * Adds annotations to a class.
     *
     * @param owner The class to add annotations to
     * @param visible Are they be visible
     * @param annotations The annotations to add
     * */
    public static void addAnnotations(@NotNull ClassNode owner, boolean visible, @NotNull Collection<@NotNull AnnotationNode> annotations){
        AsmUtilsImpl.addAnnotations(owner, visible, annotations);
    }
    
    /**
     * Removes annotations from a method.
     *
     * @param method The method to remove annotations from
     * @param annotations The annotations to remove
     * */
    public static boolean removeAnnotations(@NotNull MethodNode method, @NotNull AnnotationNode... annotations){
        return removeAnnotations(method.visibleAnnotations, method.invisibleAnnotations, annotations);
    }
    
    /**
     * Removes annotations from a class.
     *
     * @param owner The class to remove annotations from
     * @param annotations The annotations to remove
     * */
    public static boolean removeAnnotations(@NotNull ClassNode owner, @NotNull AnnotationNode... annotations){
        return removeAnnotations(owner.visibleAnnotations, owner.invisibleAnnotations, annotations);
    }
    
    /**
     * Removes annotations from lists.
     *
     * @param visibleAnnotations The visible annotations
     * @param invisibleAnnotations The invisible annotations
     * @param annotations The annotations to remove
     * */
    public static boolean removeAnnotations(@Nullable List<@NotNull AnnotationNode> visibleAnnotations, @Nullable List<@NotNull AnnotationNode> invisibleAnnotations, @NotNull AnnotationNode... annotations){
        return removeAnnotations(visibleAnnotations, invisibleAnnotations, Arrays.asList(annotations));
    }
    
    /**
     * Removes annotations from a method.
     *
     * @param method The method to remove annotations from
     * @param annotations The annotations to remove
     * */
    public static boolean removeAnnotations(@NotNull MethodNode method, @NotNull Collection<@NotNull AnnotationNode> annotations){
        return removeAnnotations(method.visibleAnnotations, method.invisibleAnnotations, annotations);
    }
    
    /**
     * Removes annotations from a class.
     *
     * @param owner The class to remove annotations from
     * @param annotations The annotations to remove
     * */
    public static boolean removeAnnotations(@NotNull ClassNode owner, @NotNull Collection<@NotNull AnnotationNode> annotations){
        return removeAnnotations(owner.visibleAnnotations, owner.invisibleAnnotations, annotations);
    }
    
    /**
     * Removes annotations from lists.
     *
     * @param visibleAnnotations The visible annotations
     * @param invisibleAnnotations The invisible annotations
     * @param annotations The annotations to remove
     * */
    public static boolean removeAnnotations(@Nullable List<@NotNull AnnotationNode> visibleAnnotations, @Nullable List<@NotNull AnnotationNode> invisibleAnnotations, @NotNull Collection<@NotNull AnnotationNode> annotations){
        return AsmUtilsImpl.removeAnnotations(visibleAnnotations, invisibleAnnotations, annotations);
    }
    
    /**
     * Removes annotations from a method.
     *
     * @param method The method to remove annotations from
     * @param type The type of the annotations to remove
     * */
    public static boolean removeAnnotations(@NotNull MethodNode method, @NotNull Type type){
        return removeAnnotations(method.visibleAnnotations, method.invisibleAnnotations, type);
    }
    
    /**
     * Removes annotations from a class.
     *
     * @param owner The class to remove annotations from
     * @param type The type of the annotations to remove
     * */
    public static boolean removeAnnotations(@NotNull ClassNode owner, @NotNull Type type){
        return removeAnnotations(owner.visibleAnnotations, owner.invisibleAnnotations, type);
    }
    
    /**
     * Removes annotations from lists.
     *
     * @param visibleAnnotations The visible annotations
     * @param invisibleAnnotations The invisible annotations
     * @param type The type of the annotations to remove
     * */
    public static boolean removeAnnotations(@Nullable List<@NotNull AnnotationNode> visibleAnnotations, @Nullable List<@NotNull AnnotationNode> invisibleAnnotations, @NotNull Type type){
        return AsmUtilsImpl.removeAnnotations(visibleAnnotations, invisibleAnnotations, type);
    }
    
    /**
     * Removes an annotation from a method.
     *
     * @param method The method to remove an annotation from
     * @param annotation The annotation to remove
     * */
    public static boolean removeAnnotation(@NotNull MethodNode method, @NotNull AnnotationNode annotation){
        return removeAnnotation(method.visibleAnnotations, method.invisibleAnnotations, annotation);
    }
    
    /**
     * Removes an annotation from a class.
     *
     * @param owner The class to remove an annotation from
     * @param annotation The annotation to remove
     * */
    public static boolean removeAnnotation(@NotNull ClassNode owner, @NotNull AnnotationNode annotation){
        return removeAnnotation(owner.visibleAnnotations, owner.invisibleAnnotations, annotation);
    }
    
    /**
     * Removes an annotation from lists.
     *
     * @param visibleAnnotations The visible annotations
     * @param invisibleAnnotations The invisible annotations
     * @param annotation The annotation to remove
     * */
    public static boolean removeAnnotation(@Nullable List<@NotNull AnnotationNode> visibleAnnotations, @Nullable List<@NotNull AnnotationNode> invisibleAnnotations, @NotNull AnnotationNode annotation){
        return removeAnnotations(visibleAnnotations, invisibleAnnotations, Collections.singletonList(annotation));
    }
    
    // --- Instruction stuff ---
    
    /**
     * Finds all nodes in the method that are of the provided type and opcode.
     *
     * @param method The method to search
     * @param opcode The instruction opcode
     * @param <T> The node type
     *
     * @return A list of all matching nodes
     * */
    @NotNull
    public static <T extends AbstractInsnNode> List<@NotNull T> findMatchingNodes(@NotNull MethodNode method, int opcode){
        return findMatchingNodes(method.instructions, (node)->node.getOpcode() == opcode);
    }
    
    /**
     * Finds all nodes in the instructions that are of the provided type and opcode.
     *
     * @param instructions The instructions to search
     * @param opcode The instruction opcode
     * @param <T> The node type
     *
     * @return A list of all matching nodes
     * */
    @NotNull
    public static <T extends AbstractInsnNode> List<@NotNull T> findMatchingNodes(@NotNull InsnList instructions, int opcode){
        return findMatchingNodes(instructions, (node)->node.getOpcode() == opcode);
    }
    
    /**
     * Finds all nodes in the method that are of the provided type.
     *
     * @param method The method to search
     * @param type The node type
     *
     * @return A list of all matching nodes
     * */
    @NotNull
    public static <T extends AbstractInsnNode> List<@NotNull T> findMatchingNodes(@NotNull MethodNode method, @NotNull Class<T> type){
        return findMatchingNodes(method.instructions, type::isInstance);
    }
    
    /**
     * Finds all nodes in the instructions that are of the provided type.
     *
     * @param instructions The instructions to search
     * @param type The node type
     *
     * @return A list of all matching nodes
     * */
    @NotNull
    public static <T extends AbstractInsnNode> List<@NotNull T> findMatchingNodes(@NotNull InsnList instructions, @NotNull Class<T> type){
        return findMatchingNodes(instructions, type::isInstance);
    }
    
    /**
     * Finds all nodes in the method that are match the provider checker.
     *
     * @param method The method to search
     * @param checker The node checker
     * @param <T> The type of node the checker looks for
     *
     * @return A list of all matching nodes
     * */
    @NotNull
    public static <T extends AbstractInsnNode> List<@NotNull T> findMatchingNodes(@NotNull MethodNode method, @NotNull BooleanFunction<@NotNull AbstractInsnNode> checker){
        return findMatchingNodes(method.instructions, checker);
    }
    
    /**
     * Finds all nodes in the instructions that are match the provider checker.
     *
     * @param instructions The instructions to search
     * @param checker The node checker
     * @param <T> The type of node the checker looks for
     *
     * @return A list of all matching nodes
     * */
    @NotNull
    public static <T extends AbstractInsnNode> List<@NotNull T> findMatchingNodes(@NotNull InsnList instructions, @NotNull BooleanFunction<@NotNull AbstractInsnNode> checker){
        return AsmUtilsImpl.findMatchingNodes(instructions, checker);
    }

    /**
     * Finds the next node that matches.
     *
     * @param start The start of the search, exclusive
     * @param checker The node checker
     * @param <T> The type of node the checker looks for
     *
     * @return The found node
     */
    @NotNull
    public static <T extends AbstractInsnNode> Optional<@NotNull T> findNextNode(@NotNull AbstractInsnNode start, @NotNull BooleanFunction<@NotNull AbstractInsnNode> checker){
        return findNextNode(start, checker, Integer.MAX_VALUE);
    }

    /**
     * Finds the next node that matches.
     *
     * @param start The start of the search, exclusive
     * @param checker The node checker
     * @param limit The max amount of nodes to search
     * @param <T> The type of node the checker looks for
     *
     * @return The found node
     */
    @NotNull
    public static <T extends AbstractInsnNode> Optional<T> findNextNode(@NotNull AbstractInsnNode start, @NotNull BooleanFunction<@NotNull AbstractInsnNode> checker, int limit){
        return AsmUtilsImpl.findNextNode(start, checker, limit);
    }

    /**
     * Finds the previous node that matches.
     *
     * @param start The start of the search, exclusive
     * @param checker The node checker
     * @param <T> The type of node the checker looks for
     *
     * @return The found node
     */
    @NotNull
    public static <T extends AbstractInsnNode> Optional<T> findPreviousNode(@NotNull AbstractInsnNode start, @NotNull BooleanFunction<@NotNull AbstractInsnNode> checker){
        return findPreviousNode(start, checker, Integer.MAX_VALUE);
    }

    /**
     * Finds the previous node that matches.
     *
     * @param start The start of the search, exclusive
     * @param checker The node checker
     * @param limit The max amount of nodes to search
     * @param <T> The type of node the checker looks for
     *
     * @return The found node
     */
    @NotNull
    public static <T extends AbstractInsnNode> Optional<T> findPreviousNode(@NotNull AbstractInsnNode start, @NotNull BooleanFunction<@NotNull AbstractInsnNode> checker, int limit){
        return AsmUtilsImpl.findPreviousNode(start, checker, limit);
    }

    /**
     * Tells method search methods to ignore the owner of the target method when searching.
     * */
    public static final int METHOD_FLAG_IGNORE_OWNER = 1 << 0;
    /**
     * Tells method search methods to ignore the name of the target method when searching.
     * */
    public static final int METHOD_FLAG_IGNORE_NAME = 1 << 1;
    /**
     * Tells method search methods to ignore the description of the target method when searching.
     * */
    public static final int METHOD_FLAG_IGNORE_DESCRIPTION = 1 << 2;
    /**
     * Tells method search methods to ignore the opcode of the target method when searching.
     * */
    public static final int METHOD_FLAG_IGNORE_OPCODE = 1 << 3;
    
    /**
     * Searches for method calls in an instruction list.
     *
     * @param instructions The method to search
     * @param flags Flags to control the search
     * @param opcode Opcode, or -1 if it doesn't matter
     * @param method The method to search for
     *
     * @return A list of all matching method calls
     */
    @NotNull
    public static List<@NotNull MethodInsnNode> findMethodCalls(@NotNull MethodNode instructions, int flags, int opcode, @NotNull MethodType method){
        return findMethodCalls(instructions.instructions, flags, opcode, method);
    }
    
    /**
     * Searches for method calls in an instruction list.
     *
     * @param instructions The instructions to search
     * @param flags Flags to control the search
     * @param opcode Opcode, or -1 if it doesn't matter
     * @param method The method to search for
     *
     * @return A list of all matching method calls
     */
    @NotNull
    public static List<@NotNull MethodInsnNode> findMethodCalls(@NotNull InsnList instructions, int flags, int opcode, @NotNull MethodType method){
        return AsmUtilsImpl.findMethodCalls(instructions, flags, opcode, method);
    }
    
    /**
     * Searches for the next method call after this node.
     *
     * @param node The starting point of the search, exclusive
     * @param flags Flags to control the search
     * @param opcode Opcode, or -1 if it doesn't matter
     * @param method The method to search for
     *
     * @return The matching method node
     * */
    @NotNull
    public static Optional<MethodInsnNode> findNextMethodCall(@NotNull AbstractInsnNode node, int flags, int opcode, @NotNull MethodType method){
        return findNextMethodCall(node, flags, opcode, method, Integer.MAX_VALUE);
    }
    
    /**
     * Searches for the next method call after this node.
     *
     * @param node The starting point of the search, exclusive
     * @param flags Flags to control the search
     * @param opcode Opcode, or -1 if it doesn't matter
     * @param method The method to search for
     * @param limit The max amount of nodes to check
     *
     * @return The matching method node
     * */
    @NotNull
    public static Optional<MethodInsnNode> findNextMethodCall(@NotNull AbstractInsnNode node, int flags, int opcode, @NotNull MethodType method, int limit){
        return AsmUtilsImpl.findNextMethodCall(node, flags, opcode, method, limit);
    }
    
    /**
     * Searches for the previous method call after this node.
     *
     * @param node The starting point of the search, exclusive
     * @param flags Flags to control the search
     * @param opcode Opcode, or -1 if it doesn't matter
     * @param method The method to search for
     *
     * @return The matching method node
     * */
    @NotNull
    public static Optional<MethodInsnNode> findPreviousMethodCall(@NotNull AbstractInsnNode node, int flags, int opcode, @NotNull MethodType method){
        return findPreviousMethodCall(node, flags, opcode, method, Integer.MAX_VALUE);
    }
    
    /**
     * Searches for the previous method call after this node.
     *
     * @param node The starting point of the search, exclusive
     * @param flags Flags to control the search
     * @param opcode Opcode, or -1 if it doesn't matter
     * @param method The method to search for
     * @param limit The max amount of nodes to check
     *
     * @return The matching method node
     * */
    @NotNull
    public static Optional<MethodInsnNode> findPreviousMethodCall(@NotNull AbstractInsnNode node, int flags, int opcode, @NotNull MethodType method, int limit){
        return AsmUtilsImpl.findPreviousMethodCall(node, flags, opcode, method, limit);
    }
    
    /**
     * Finds up to count nodes after the provided node.
     *
     * This is exclusive.
     *
     * @param node The initial node
     * @param count The maximum amount of nodes
     *
     * @return A list of count or fewer trailing nodes
     * */
    @NotNull
    public static List<@NotNull AbstractInsnNode> findTrailingNodes(@NotNull AbstractInsnNode node, int count){
        List<AbstractInsnNode> nodes = findSurroundingNodes(node, 0, count);
        nodes.remove(0);
        return nodes;
    }
    
    /**
     * Finds up to count nodes before the provided node.
     *
     * This is exclusive.
     *
     * @param node The initial node
     * @param count The maximum amount of nodes
     *
     * @return A list of count or fewer leading nodes
     * */
    @NotNull
    public static List<@NotNull AbstractInsnNode> findLeadingNodes(@NotNull AbstractInsnNode node, int count){
        List<AbstractInsnNode> nodes = findSurroundingNodes(node, 0, count);
        nodes.remove(nodes.size() - 1);
        return nodes;
    }
    
    /**
     * Finds up to nodes surrounding another node.
     *
     * This is inclusive.
     *
     * @param node The initial node
     * @param leading The maximum amount of nodes
     * @param trailing The maximum amount of trailing nodes
     *
     * @return A list of matching nodes
     * */
    @NotNull
    public static List<@NotNull AbstractInsnNode> findSurroundingNodes(@NotNull AbstractInsnNode node, int leading, int trailing){
        return AsmUtilsImpl.findSurroundingNodes(node, leading, trailing);
    }
    
    /**
     * Finds return instructions in a method.
     *
     * @param method The method to search
     *
     * @return A list of return instructions
     * */
    @NotNull
    public static List<@NotNull InsnNode> findReturns(@NotNull MethodNode method){
        return findReturns(method.instructions);
    }
    
    /**
     * Finds return instructions in a list of instructions.
     *
     * @param instructions The instructions to search
     *
     * @return A list of return instructions
     * */
    @NotNull
    public static List<@NotNull InsnNode> findReturns(@NotNull InsnList instructions){
        return AsmUtilsImpl.findReturns(instructions);
    }
    
    /**
     * Finds nodes between a beginning and end, exclusive.
     *
     * @param range A pair of nodes
     *
     * @return The middle instructions, or null on error
     * */
    @Nullable
    public static List<@NotNull AbstractInsnNode> findInRange(@NotNull Pair<@NotNull AbstractInsnNode, @NotNull AbstractInsnNode> range){
        return findInRange(range.getA(), range.getB());
    }
    
    /**
     * Finds nodes between a beginning and end, exclusive.
     *
     * @param start The first node
     * @param end The last node
     *
     * @return The middle instructions, or null on error
     * */
    @Nullable
    public static List<@NotNull AbstractInsnNode> findInRange(@NotNull AbstractInsnNode start, @NotNull AbstractInsnNode end){
        return AsmUtilsImpl.findInRange(start, end);
    }
    
    /**
     * Tells method search methods to ignore the owner of the target field when searching.
     * */
    public static final int FIELD_FLAG_IGNORE_OWNER = 1 << 0;
    /**
     * Tells method search methods to ignore the name of the target field when searching.
     * */
    public static final int FIELD_FLAG_IGNORE_NAME = 1 << 1;
    /**
     * Tells method search methods to ignore the description of the target field when searching.
     * */
    public static final int FIELD_FLAG_IGNORE_DESCRIPTION = 1 << 2;
    /**
     * Tells method search methods to ignore the opcode of the target field when searching.
     * */
    public static final int FIELD_FLAG_IGNORE_OPCODE = 1 << 3;
    
    /**
     * Find field instructions in a method.
     *
     * @param method The method to search
     * @param flags Flags to control how the search works
     * @param opcode Opcode, -1 if it doesn't matter
     * @param field The field to look for
     *
     * @return The found field instructions
     */
    @NotNull
    public static List<@NotNull FieldInsnNode> findFieldNodes(@NotNull MethodNode method, int flags, int opcode, @NotNull FieldType field){
        return findFieldNodes(method.instructions, flags, opcode, field);
    }
    
    /**
     * Find field instructions in an instruction list.
     *
     * @param instructions The instructions to search
     * @param flags Flags to control how the search works
     * @param opcode Opcode, -1 if it doesn't matter
     * @param field The field to look for
     *
     * @return The found field instructions
     */
    @NotNull
    public static List<@NotNull FieldInsnNode> findFieldNodes(@NotNull InsnList instructions, int flags, int opcode, @NotNull FieldType field){
        return AsmUtilsImpl.findFieldNodes(instructions, flags, opcode, field);
    }
    
    /**
     * Find the next field instruction in a list.
     *
     * @param node The start of the search
     * @param flags Flags to control how the search works
     * @param opcode Opcode, -1 if it doesn't matter
     * @param field The field to look for
     *
     * @return The found field instructions
     */
    @NotNull
    public static Optional<FieldInsnNode> findNextFieldNode(@NotNull AbstractInsnNode node, int flags, int opcode, @NotNull FieldType field){
        return findNextFieldNode(node, flags, opcode, field, Integer.MAX_VALUE);
    }
    
    /**
     * Find the next field instruction in a list.
     *
     * @param node The start of the search
     * @param flags Flags to control how the search works
     * @param opcode Opcode, -1 if it doesn't matter
     * @param field The field to look for
     * @param limit The max amount of nodes to check
     *
     * @return The found field instructions
     */
    @NotNull
    public static Optional<FieldInsnNode> findNextFieldNode(@NotNull AbstractInsnNode node, int flags, int opcode, @NotNull FieldType field, int limit){
        return AsmUtilsImpl.findNextFieldNode(node, flags, opcode, field, limit);
    }
    
    /**
     * Find the previous field instruction in a list.
     *
     * @param node The start of the search
     * @param flags Flags to control how the search works
     * @param opcode Opcode, -1 if it doesn't matter
     * @param field The field to look for
     *
     * @return The found field instructions
     */
    @NotNull
    public static Optional<FieldInsnNode> findPreviousFieldNode(@NotNull AbstractInsnNode node, int flags, int opcode, @NotNull FieldType field){
        return findPreviousFieldNode(node, flags, opcode, field, Integer.MAX_VALUE);
    }
    
    /**
     * Find the previous field instruction in a list.
     *
     * @param node The start of the search
     * @param flags Flags to control how the search works
     * @param opcode Opcode, -1 if it doesn't matter
     * @param field The field to look for
     * @param limit The max amount of nodes to check
     *
     * @return The found field instructions
     */
    @NotNull
    public static Optional<FieldInsnNode> findPreviousFieldNode(@NotNull AbstractInsnNode node, int flags, int opcode, @NotNull FieldType field, int limit){
        return AsmUtilsImpl.findPreviousFieldNode(node, flags, opcode, field, limit);
    }

    // --- Dynamic instruction stuff ---
    
    /**
     * Translates the handle's tag to an opcode.
     *
     * @param handle The handle
     *
     * @return The handle's corresponding opcode
     * */
    public static int getOpcodeFromHandle(@NotNull Handle handle){
        return getOpcodeFromHandleTag(handle.getTag());
    }
    
    /**
     * Translates the a handle tag to an opcode.
     *
     * @param tag The handle tag
     *
     * @return The tag's corresponding opcode
     * */
    public static int getOpcodeFromHandleTag(int tag){
        return AsmUtilsImpl.getOpcodeFromHandleTag(tag);
    }
    
    /**
     * Translates an instruction's opcode into a handle tag.
     *
     * @param instruction The instruction
     *
     * @return The instruction's corresponding tag
     * */
    public static int getHandleTagFromInstruction(@NotNull AbstractInsnNode instruction){
        return getHandleTagFromOpcode(instruction.getOpcode());
    }
    
    /**
     * Translates an opcode into a handle tag.
     *
     * @param opcode The opcode
     *
     * @return The opcode's corresponding tag
     * */
    public static int getHandleTagFromOpcode(int opcode){
        return AsmUtilsImpl.getHandleTagFromOpcode(opcode);
    }
    
    // --- Code generation stuff ---
    
    /**
     * Generates a `throw new type` InsnList.
     *
     * @param type The type of exception
     *
     * @return The instruction list
     * */
    @NotNull
    public static InsnList createExceptionList(@NotNull Type type){
        return createExceptionList(type, null);
    }
    
    /**
     * Generates a `throw new type` InsnList.
     *
     * @param type The type of exception
     * @param message The message, or null for none
     *
     * @return The instruction list
     * */
    @NotNull
    public static InsnList createExceptionList(@NotNull Type type, @Nullable String message){
        return AsmUtilsImpl.createExceptionList(type, message);
    }
    
    // -- ClassNode stuff --
    
    /**
     * Finds a method in a class.
     *
     * @param owner The class
     * @param name The name of the method
     * @param desc The description of the method
     *
     * @return The method
     * */
    @NotNull
    public static Optional<MethodNode> findMethod(@NotNull ClassNode owner, @NotNull String name, @NotNull String desc){
        return AsmUtilsImpl.findMethod(owner, name, desc);
    }
    
    // --- Misc ---
    
    /**
     * Gets the name of an instruction.
     *
     * @param instruction The instruction
     *
     * @return The name of the instruction
     * */
    @NotNull
    public static String getInstructionName(@NotNull AbstractInsnNode instruction){
        return getOpcodeName(instruction.getOpcode());
    }
    
    /**
     * Gets the name of an opcode.
     *
     * @param opcode The opcode
     *
     * @return The name of the opcode
     * */
    @NotNull
    public static String getOpcodeName(int opcode){
        return AsmUtilsImpl.getOpcodeName(opcode);
    }
}
