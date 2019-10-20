package com.feng.home.common.scanner;

import com.feng.home.common.common.StringUtil;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * create by FengZiyu
 * 2019/09/03
 */
public class SamplePackageScanner implements PackageScanner {

    private static final String CLASS_SUFFIX = ".class";

    @Override
    public List<String> scan(String basePackage) throws IOException {

        String basePackagePath = this.classToPath(basePackage);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(basePackagePath);

        if(url.getProtocol().equals("file")){
            String classPath = URLDecoder.decode(classLoader.getResource("").getFile(), "UTF-8");
            if(classPath.startsWith("/")){
                classPath = classPath.replaceFirst("/", "");
            }
            return scanClassFromFile(basePackage, Paths.get(classPath + basePackagePath));
        }else if(url.getProtocol().equals("jar")){
            return scanClassFromJar(url);
        }

        throw new IllegalArgumentException("不支持的包:" + basePackage);
    }

    @Override
    public List<String> scan(String basePackage, Predicate<String> filter) throws IOException {
        return this.scan(basePackage).stream().filter(filter).collect(Collectors.toList());
    }

    @Override
    public <T extends Annotation> List<T> scanAndGetAnnotations(Class<T> tClass, String basePackage) throws IOException, ClassNotFoundException {
        List<String> fullClassNameList = this.scan(basePackage);
        return getAnnotationFromClass(fullClassNameList, tClass);
    }

    @Override
    public <T extends Annotation> List<T> scanAndGetAnnotations(Class<T> tClass, String basePackage, Predicate<String> filter) throws IOException, ClassNotFoundException {
        List<String> fullClassNameList = this.scan(basePackage, filter);
        return getAnnotationFromClass(fullClassNameList, tClass);
    }

    private <T extends Annotation> List<T> getAnnotationFromClass(List<String> fullNameList, Class<T> tClass) throws ClassNotFoundException {
        List<T> annotations = new LinkedList<>();
        for(String fullClassName : fullNameList){
            Class<?> targetClass;
            try {
                targetClass = Class.forName(fullClassName);
                Annotation t = targetClass.getAnnotation(tClass);
                if(t != null && t.annotationType() == tClass){
                    annotations.add((T) t);
                }
            }catch (Exception e){

            }
        }
        return annotations;
    }

    private List<String> scanClassFromFile(String basePackage, Path packagePath) throws IOException {
        List<String> classFullNames = new LinkedList<>();
        List<Path> paths = Files.list(packagePath).collect(Collectors.toList());
        for(Path path : paths){
            String pathName = path.toFile().getName();
            if(Files.isDirectory(path)){
                basePackage = StringUtil.isEmpty(basePackage) ? pathName : basePackage + "." + pathName;
                classFullNames.addAll(this.scanClassFromFile(basePackage, path));
            }else if(isClassFile(pathName)){
                classFullNames.add(this.pathToClass(basePackage + "." + path.toFile().getName()));
            }
        }
        return classFullNames;
    }

    private List<String> scanClassFromJar(URL url) throws IOException {
        JarURLConnection jarFile = (JarURLConnection) url.openConnection();
        Enumeration<JarEntry> jarEntryEnumeration =  jarFile.getJarFile().entries();

        LinkedList<String> classNameList = new LinkedList<>();
        while(jarEntryEnumeration.hasMoreElements()){
            JarEntry jarEntry = jarEntryEnumeration.nextElement();
            String jarName = jarEntry.getName();
            if(isClassFile(jarName)){
                classNameList.add(this.pathToClass(jarName));
            }
        }
        return classNameList;
    }

    private boolean isClassFile(String filePath){
        return filePath.endsWith(CLASS_SUFFIX);
    }

    private String pathToClass(String path){
        return path.replace(CLASS_SUFFIX, "").replaceAll(Matcher.quoteReplacement(File.separator), ".");
    }

    private String classToPath(String classFullName){
        return classFullName.replace(".", Matcher.quoteReplacement(File.separator));
    }
}
