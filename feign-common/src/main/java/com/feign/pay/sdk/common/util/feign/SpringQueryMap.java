package com.feign.pay.sdk.common.util.feign;

import feign.QueryMap;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Spring MVC equivalent of OpenFeign's {@link QueryMap} parameter annotation.
 *
 * @author Aram Peres
 * @see QueryMap
 * @see org.springframework.cloud.openfeign.annotation.QueryMapParameterProcessor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface SpringQueryMap {

    /**
     * @see QueryMap#encoded()
     * @return alias for {@link #encoded()}.
     */
    @AliasFor("encoded")
    boolean value() default false;

    /**
     * @see QueryMap#encoded()
     * @return Specifies whether parameter names and values are already encoded.
     */
    @AliasFor("value")
    boolean encoded() default false;

}
