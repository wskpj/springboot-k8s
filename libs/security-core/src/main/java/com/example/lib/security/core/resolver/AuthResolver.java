package com.example.lib.security.core.resolver;

import java.lang.annotation.Annotation;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthResolver {
    boolean hasAnnotation(HttpServletRequest request, Class<? extends Annotation> annotationClass);
}
