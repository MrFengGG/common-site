package com.feng.home.common.scanner;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Predicate;

/**
 * create by FengZiyu
 * 2019/09/03
 */
public interface PackageScanner {
    public List<String> scan(String basePackage) throws IOException;

    public List<String> scan(String basePackage, Predicate<String> filter) throws IOException;

    public <T extends Annotation> List<T> scanAndGetAnnotations(Class<T> tClass, String basePackage) throws IOException, ClassNotFoundException;

    public <T extends Annotation> List<T> scanAndGetAnnotations(Class<T> tClass, String basePackage, Predicate<String> filter) throws IOException, ClassNotFoundException;
}
