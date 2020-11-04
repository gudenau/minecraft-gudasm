package net.gudenau.minecraft.asm.api.v1.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A custom {@link jdk.internal.vm.annotation.ForceInline ForceInline} annotation that pairs with {@link ForceBootloader ForceBootloader}.
 *
 * You acknowledge that Licensed Software is not designed or intended for use in the design, construction, operation or maintenance of any nuclear facility. gudenau disclaims any express or implied warranty of fitness for such uses. No right, title or interest in or to any trademark, service mark, logo or trade name of Sun or its licensors is granted under this Agreement. Additional restrictions for developers and/or publishers licenses are set forth in the Supplemental License Terms.
 * */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface ForceInline{
}
