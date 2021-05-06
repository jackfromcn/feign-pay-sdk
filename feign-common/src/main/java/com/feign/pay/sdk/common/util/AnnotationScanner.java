package com.feign.pay.sdk.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author wencheng
 */
@Slf4j
@Component
public class AnnotationScanner implements ResourceLoaderAware {

    /**
     * Spring容器注入
     */
    private ResourceLoader resourceLoader;

    public <T extends Annotation> List<Class> scanByAnnotation(Class<T> annotationClass, String locationPattern) {
        List<Class> result = new ArrayList<>(10);
        try {
            ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
            MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);
            Resource[] resources = resolver.getResources(locationPattern);

            for (Resource r : resources) {
                MetadataReader reader = metaReader.getMetadataReader(r);
                String className = reader.getClassMetadata().getClassName();
                Class<?> targetClass = Class.forName(className);
                T annotation = targetClass.getAnnotation(annotationClass);
                if (Objects.nonNull(annotation)) {
                    result.add(targetClass);
                }
            }
        } catch (Exception e) {
            log.error("AnnotationScanner[scanByAnnotation] 异常, 扫描{}异常,e:", annotationClass, e);
        }
        return result;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}

