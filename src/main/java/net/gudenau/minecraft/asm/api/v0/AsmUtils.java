package net.gudenau.minecraft.asm.api.v0;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.gudenau.minecraft.asm.api.v0.functional.BooleanFunction;
import net.gudenau.minecraft.asm.util.AsmUtilsImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

/**
 * General ASM utils.
 * */
@SuppressWarnings({"unused", "RedundantSuppression"}) // Idea is silly
public interface AsmUtils{
    /**
     * Get the handle to AsmUtils.
     *
     * @return The AsmUtils handle
     * */
    @NotNull
    static AsmUtils getInstance(){
        return AsmUtilsImpl.INSTANCE;
    }

    /**
     * Get the handle to the Type cache.
     *
     * @return The handle to the type cache
     * */
    @Deprecated
    @NotNull
    TypeCache getTypeCache();
    
    // --- Annotation stuff ---
    
    /**
     * Checks if a method has an annotation.
     *
     * @param methodNode The method to check
     * @param type The annotation type to check for
     *
     * @return True if the annotation is present, false if absent
     * */
    default boolean hasAnnotation(@NotNull MethodNode methodNode, @NotNull Type type){
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
    default boolean hasAnnotation(@NotNull ClassNode owner, @NotNull Type type){
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
    default boolean hasAnnotation(@Nullable List<AnnotationNode> visibleAnnotations, @Nullable List<AnnotationNode> invisibleAnnotations, @NotNull Type type){
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
    default List<AnnotationNode> getAnnotations(@NotNull MethodNode method, @NotNull Type type){
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
    default List<AnnotationNode> getAnnotations(@NotNull ClassNode owner, @NotNull Type type){
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
    List<AnnotationNode> getAnnotations(@Nullable List<AnnotationNode> visibleAnnotations, @Nullable List<AnnotationNode> invisibleAnnotations, @NotNull Type type);
    
    /**
     * Gets the first matching annotation in a method.
     *
     * @param method The method to get annotations from
     * @param type The annotation type to check for
     *
     * @return Found annotation
     * */
    default Optional<AnnotationNode> getAnnotation(@NotNull MethodNode method, @NotNull Type type){
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
    default Optional<AnnotationNode> getAnnotation(@NotNull ClassNode owner, @NotNull Type type){
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
    Optional<AnnotationNode> getAnnotation(@Nullable List<AnnotationNode> visibleAnnotations, @Nullable List<AnnotationNode> invisibleAnnotations, @NotNull Type type);
    
    /**
     * Adds an annotation to a method.
     *
     * @param method The method to add annotations to
     * @param visible Are they be visible
     * @param annotation The annotation to add
     * */
    default void addAnnotation(@NotNull MethodNode method, boolean visible, @NotNull AnnotationNode annotation){
        addAnnotations(method, visible, Collections.singletonList(annotation));
    }
    
    /**
     * Adds an annotation to a class.
     *
     * @param owner The class to add annotations to
     * @param visible Are they be visible
     * @param annotation The annotation to add
     * */
    default void addAnnotation(@NotNull ClassNode owner, boolean visible, @NotNull AnnotationNode annotation){
        addAnnotations(owner, visible, Collections.singletonList(annotation));
    }
    
    /**
     * Adds annotations to a method.
     *
     * @param method The method to add annotations to
     * @param visible Are they be visible
     * @param annotations The annotations to add
     * */
    default void addAnnotations(@NotNull MethodNode method, boolean visible, @NotNull AnnotationNode... annotations){
        addAnnotations(method, visible, Arrays.asList(annotations));
    }
    
    /**
     * Adds annotations to a method.
     *
     * @param method The method to add annotations to
     * @param visible Are they be visible
     * @param annotations The annotations to add
     * */
    void addAnnotations(@NotNull MethodNode method, boolean visible, @NotNull Collection<AnnotationNode> annotations);
    
    /**
     * Adds annotations to a class.
     *
     * @param owner The class to add annotations to
     * @param visible Are they be visible
     * @param annotations The annotations to add
     * */
    default void addAnnotations(@NotNull ClassNode owner, boolean visible, @NotNull AnnotationNode... annotations){
        addAnnotations(owner, visible, Arrays.asList(annotations));
    }
    
    /**
     * Adds annotations to a class.
     *
     * @param owner The class to add annotations to
     * @param visible Are they be visible
     * @param annotations The annotations to add
     * */
    void addAnnotations(@NotNull ClassNode owner, boolean visible, @NotNull Collection<AnnotationNode> annotations);
    
    /**
     * Removes annotations from a method.
     *
     * @param method The method to remove annotations from
     * @param annotations The annotations to remove
     * */
    default boolean removeAnnotations(@NotNull MethodNode method, @NotNull AnnotationNode... annotations){
        return removeAnnotations(method.visibleAnnotations, method.invisibleAnnotations, annotations);
    }
    
    /**
     * Removes annotations from a class.
     *
     * @param owner The class to remove annotations from
     * @param annotations The annotations to remove
     * */
    default boolean removeAnnotations(@NotNull ClassNode owner, @NotNull AnnotationNode... annotations){
        return removeAnnotations(owner.visibleAnnotations, owner.invisibleAnnotations, annotations);
    }
    
    /**
     * Removes annotations from lists.
     *
     * @param visibleAnnotations The visible annotations
     * @param invisibleAnnotations The invisible annotations
     * @param annotations The annotations to remove
     * */
    default boolean removeAnnotations(@Nullable List<AnnotationNode> visibleAnnotations, @Nullable List<AnnotationNode> invisibleAnnotations, @NotNull AnnotationNode... annotations){
        return removeAnnotations(visibleAnnotations, invisibleAnnotations, Arrays.asList(annotations));
    }
    
    /**
     * Removes annotations from a method.
     *
     * @param method The method to remove annotations from
     * @param annotations The annotations to remove
     * */
    default boolean removeAnnotations(@NotNull MethodNode method, @NotNull Collection<AnnotationNode> annotations){
        return removeAnnotations(method.visibleAnnotations, method.invisibleAnnotations, annotations);
    }
    
    /**
     * Removes annotations from a class.
     *
     * @param owner The class to remove annotations from
     * @param annotations The annotations to remove
     * */
    default boolean removeAnnotations(@NotNull ClassNode owner, @NotNull Collection<AnnotationNode> annotations){
        return removeAnnotations(owner.visibleAnnotations, owner.invisibleAnnotations, annotations);
    }
    
    /**
     * Removes annotations from lists.
     *
     * @param visibleAnnotations The visible annotations
     * @param invisibleAnnotations The invisible annotations
     * @param annotations The annotations to remove
     * */
    boolean removeAnnotations(@Nullable List<AnnotationNode> visibleAnnotations, @Nullable List<AnnotationNode> invisibleAnnotations, @NotNull Collection<AnnotationNode> annotations);
    
    /**
     * Removes annotations from a method.
     *
     * @param method The method to remove annotations from
     * @param type The type of the annotations to remove
     * */
    default boolean removeAnnotations(@NotNull MethodNode method, @NotNull Type type){
        return removeAnnotations(method.visibleAnnotations, method.invisibleAnnotations, type);
    }
    
    /**
     * Removes annotations from a class.
     *
     * @param owner The class to remove annotations from
     * @param type The type of the annotations to remove
     * */
    default boolean removeAnnotations(@NotNull ClassNode owner, @NotNull Type type){
        return removeAnnotations(owner.visibleAnnotations, owner.invisibleAnnotations, type);
    }
    
    /**
     * Removes annotations from lists.
     *
     * @param visibleAnnotations The visible annotations
     * @param invisibleAnnotations The invisible annotations
     * @param type The type of the annotations to remove
     * */
    boolean removeAnnotations(@Nullable List<AnnotationNode> visibleAnnotations, @Nullable List<AnnotationNode> invisibleAnnotations, @NotNull Type type);
    
    /**
     * Removes an annotation from a method.
     *
     * @param method The method to remove an annotation from
     * @param annotation The annotation to remove
     * */
    default boolean removeAnnotation(@NotNull MethodNode method, @NotNull AnnotationNode annotation){
        return removeAnnotation(method.visibleAnnotations, method.invisibleAnnotations, annotation);
    }
    
    /**
     * Removes an annotation from a class.
     *
     * @param owner The class to remove an annotation from
     * @param annotation The annotation to remove
     * */
    default boolean removeAnnotation(@NotNull ClassNode owner, @NotNull AnnotationNode annotation){
        return removeAnnotation(owner.visibleAnnotations, owner.invisibleAnnotations, annotation);
    }
    
    /**
     * Removes an annotation from lists.
     *
     * @param visibleAnnotations The visible annotations
     * @param invisibleAnnotations The invisible annotations
     * @param annotation The annotation to remove
     * */
    default boolean removeAnnotation(@Nullable List<AnnotationNode> visibleAnnotations, @Nullable List<AnnotationNode> invisibleAnnotations, @NotNull AnnotationNode annotation){
        return removeAnnotations(visibleAnnotations, invisibleAnnotations, Collections.singletonList(annotation));
    }
    
    // --- Instruction stuff ---
    
    /**
     * Finds all nodes in the method that are of the provided type and opcode.
     *
     * @param method The method to search
     * @param opcode The instruction opcode
     * @param type The node type
     *
     * @return A list of all matching nodes
     * */
    @NotNull
    default <T extends AbstractInsnNode> List<T> findMatchingNodes(@NotNull MethodNode method, int opcode, @NotNull Class<T> type){
        return findMatchingNodes(method.instructions, (node)->node.getOpcode() == opcode && type.isInstance(node));
    }
    
    /**
     * Finds all nodes in the instructions that are of the provided type and opcode.
     *
     * @param instructions The instructions to search
     * @param opcode The instruction opcode
     * @param type The node type
     *
     * @return A list of all matching nodes
     * */
    @NotNull
    default <T extends AbstractInsnNode> List<T> findMatchingNodes(@NotNull InsnList instructions, int opcode, @NotNull Class<T> type){
        return findMatchingNodes(instructions, (node)->node.getOpcode() == opcode && type.isInstance(node));
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
    default <T extends AbstractInsnNode> List<T> findMatchingNodes(@NotNull MethodNode method, @NotNull Class<T> type){
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
    default <T extends AbstractInsnNode> List<T> findMatchingNodes(@NotNull InsnList instructions, @NotNull Class<T> type){
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
    default <T extends AbstractInsnNode> List<T> findMatchingNodes(@NotNull MethodNode method, @NotNull BooleanFunction<AbstractInsnNode> checker){
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
    <T extends AbstractInsnNode> List<T> findMatchingNodes(@NotNull InsnList instructions, @NotNull BooleanFunction<AbstractInsnNode> checker);

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
    default <T extends AbstractInsnNode> Optional<T> findNextNode(@NotNull AbstractInsnNode start, @NotNull BooleanFunction<AbstractInsnNode> checker){
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
    <T extends AbstractInsnNode> Optional<T> findNextNode(@NotNull AbstractInsnNode start, @NotNull BooleanFunction<AbstractInsnNode> checker, int limit);

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
    default <T extends AbstractInsnNode> Optional<T> findPreviousNode(@NotNull AbstractInsnNode start, @NotNull BooleanFunction<AbstractInsnNode> checker){
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
    <T extends AbstractInsnNode> Optional<T> findPreviousNode(@NotNull AbstractInsnNode start, @NotNull BooleanFunction<AbstractInsnNode> checker, int limit);

    /**
     * Searches for method call instructions in a method.
     *
     * @param method The method to search in
     * @param owner Owner, or null if it doesn't matter
     * @param name Name, or null if it doesn't matter
     * @param description Description, or null if it doesn't matter
     *
     * @return A list of all matching method calls
     * */
    @NotNull
    default List<MethodInsnNode> findMethodCalls(@NotNull MethodNode method, @Nullable String owner, @Nullable String name, @Nullable String description){
        return findMethodCalls(method.instructions, -1, owner, name, description);
    }
    
    /**
     * Searches for method call in an instruction list.
     *
     * @param instructions The instructions to search in
     * @param owner Owner, or null if it doesn't matter
     * @param name Name, or null if it doesn't matter
     * @param description Description, or null if it doesn't matter
     *
     * @return A list of all matching method calls
     * */
    @NotNull
    default List<MethodInsnNode> findMethodCalls(@NotNull InsnList instructions, @Nullable String owner, @Nullable String name, @Nullable String description){
        return findMethodCalls(instructions, -1, owner, name, description);
    }
    
    /**
     * Searches for method call instructions in a method.
     *
     * @param method The method to search in
     * @param opcode Opcode, or -1 if it doesn't matter
     * @param owner Owner, or null if it doesn't matter
     * @param name Name, or null if it doesn't matter
     * @param description Description, or null if it doesn't matter
     *
     * @return A list of all matching method calls
     * */
    @NotNull
    default List<MethodInsnNode> findMethodCalls(@NotNull MethodNode method, int opcode, @Nullable String owner, @Nullable String name, @Nullable String description){
        return findMethodCalls(method.instructions, opcode, owner, name, description);
    }
    
    /**
     * Searches for method call in an instruction list.
     *
     * @param instructions The instructions to search in
     * @param opcode Opcode, or -1 if it doesn't matter
     * @param owner Owner, or null if it doesn't matter
     * @param name Name, or null if it doesn't matter
     * @param description Description, or null if it doesn't matter
     *
     * @return A list of all matching method calls
     * */
    @NotNull
    List<MethodInsnNode> findMethodCalls(@NotNull InsnList instructions, int opcode, @Nullable String owner, @Nullable String name, @Nullable String description);

    /**
     * Searches for the next method call after this node.
     *
     * @param node The starting point of the search, exclusive
     * @param owner Owner, or null if it doesn't matter
     * @param name Name, or null if it doesn't matter
     * @param description Description, or null if it doesn't matter
     * @param limit The max amount of nodes to check
     *
     * @return A list of all matching method calls
     * */
    @NotNull
    default Optional<MethodInsnNode> findNextMethodCall(@NotNull AbstractInsnNode node, @Nullable String owner, @Nullable String name, @Nullable String description, int limit){
        return findNextMethodCall(node, -1, owner, name, description, limit);
    }

    /**
     * Searches for the next method call after this node.
     *
     * @param node The starting point of the search, exclusive
     * @param opcode Opcode, or -1 if it doesn't matter
     * @param owner Owner, or null if it doesn't matter
     * @param name Name, or null if it doesn't matter
     * @param description Description, or null if it doesn't matter
     * @param limit The max amount of nodes to check
     *
     * @return A list of all matching method calls
     * */
    @NotNull
    Optional<MethodInsnNode> findNextMethodCall(@NotNull AbstractInsnNode node, int opcode, @Nullable String owner, @Nullable String name, @Nullable String description, int limit);

    /**
     * Searches for the previous method call after this node.
     *
     * @param node The starting point of the search, exclusive
     * @param owner Owner, or null if it doesn't matter
     * @param name Name, or null if it doesn't matter
     * @param description Description, or null if it doesn't matter
     * @param limit The max amount of nodes to check
     *
     * @return A list of all matching method calls
     * */
    @NotNull
    default Optional<MethodInsnNode> findPreviousMethodCall(@NotNull AbstractInsnNode node, @Nullable String owner, @Nullable String name, @Nullable String description, int limit){
        return findPreviousMethodCall(node, -1, owner, name, description, limit);
    }

    /**
     * Searches for the previous method call after this node.
     *
     * @param node The starting point of the search, exclusive
     * @param opcode Opcode, or -1 if it doesn't matter
     * @param owner Owner, or null if it doesn't matter
     * @param name Name, or null if it doesn't matter
     * @param description Description, or null if it doesn't matter
     * @param limit The max amount of nodes to check
     *
     * @return A list of all matching method calls
     * */
    @NotNull
    Optional<MethodInsnNode> findPreviousMethodCall(@NotNull AbstractInsnNode node, int opcode, @Nullable String owner, @Nullable String name, @Nullable String description, int limit);
    
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
    default List<AbstractInsnNode> findTrailingNodes(@NotNull AbstractInsnNode node, int count){
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
    default List<AbstractInsnNode> findLeadingNodes(@NotNull AbstractInsnNode node, int count){
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
    List<AbstractInsnNode> findSurroundingNodes(@NotNull AbstractInsnNode node, int leading, int trailing);
    
    /**
     * Finds return instructions in a method.
     *
     * @param method The method to search
     *
     * @return A list of return instructions
     * */
    @NotNull
    default List<InsnNode> findReturns(@NotNull MethodNode method){
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
    List<InsnNode> findReturns(@NotNull InsnList instructions);
    
    /**
     * Finds nodes between a beginning and end, exclusive.
     *
     * @param range A pair of nodes
     *
     * @return The middle instructions, or null on error
     * */
    @Nullable
    default List<AbstractInsnNode> findInRange(Pair<AbstractInsnNode, AbstractInsnNode> range){
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
    List<AbstractInsnNode> findInRange(AbstractInsnNode start, AbstractInsnNode end);

    // --- Dynamic instruction stuff ---
    
    /**
     * Translates the handle's tag to an opcode.
     *
     * @param handle The handle
     *
     * @return The handle's corresponding opcode
     * */
    default int getOpcodeFromHandle(@NotNull Handle handle){
        return getOpcodeFromHandleTag(handle.getTag());
    }
    
    /**
     * Translates the a handle tag to an opcode.
     *
     * @param tag The handle tag
     *
     * @return The tag's corresponding opcode
     * */
    int getOpcodeFromHandleTag(int tag);
    
    /**
     * Translates an instruction's opcode into a handle tag.
     *
     * @param instruction The instruction
     *
     * @return The instruction's corresponding tag
     * */
    default int getHandleTagFromInstruction(@NotNull AbstractInsnNode instruction){
        return getHandleTagFromOpcode(instruction.getOpcode());
    }
    
    /**
     * Translates an opcode into a handle tag.
     *
     * @param opcode The opcode
     *
     * @return The opcode's corresponding tag
     * */
    int getHandleTagFromOpcode(int opcode);
    
    // --- Code generation stuff ---
    
    /**
     * Generates a `throw new type` InsnList.
     *
     * @param type The type of exception
     *
     * @return The instruction list
     * */
    @NotNull
    default InsnList createExceptionList(@NotNull Type type){
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
    InsnList createExceptionList(@NotNull Type type, @Nullable String message);
    
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
    Optional<MethodNode> findMethod(@NotNull ClassNode owner, @NotNull String name, @NotNull String desc);
    
    // --- Misc ---
    
    /**
     * Gets the name of an instruction.
     *
     * @param instruction The instruction
     *
     * @return The name of the instruction
     * */
    @NotNull
    default String getInstructionName(@NotNull AbstractInsnNode instruction){
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
    String getOpcodeName(int opcode);
}
