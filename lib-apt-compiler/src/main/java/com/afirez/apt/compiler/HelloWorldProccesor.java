package com.afirez.apt.compiler;

import com.afirez.apt.api.HelloWorld;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@AutoService(Processor.class)
public class HelloWorldProccesor extends AbstractProcessor {

    private Map<String, HelloWorldClass> annotatedClassMap = new HashMap<>();
    private Filer filer;
    private Elements elementUtils;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new HashSet<>();
        annotationTypes.add(HelloWorld.class.getName());
        return annotationTypes;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        annotatedClassMap.clear();
        try {
            processHelloWold(roundEnv);
        } catch (IllegalArgumentException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            return true; // stop process
        }

        for (HelloWorldClass annotatedClass : annotatedClassMap.values()) {
            try {
                messager.printMessage(Diagnostic.Kind.NOTE, "Generating file for " + annotatedClass.getFullClassName());
                annotatedClass.generateHelloWorld().writeTo(filer);
            } catch (IOException e) {
                messager.printMessage(Diagnostic.Kind.NOTE, "Generate file failed, reason: " + e.getMessage());
                return true;
            }
        }
        return true;
    }

    private void processHelloWold(RoundEnvironment roundEnv) throws IllegalArgumentException {
        for (Element element : roundEnv.getElementsAnnotatedWith(HelloWorld.class)) {
            HelloWorldClass helloWorldClass = getAnnotatedClass(element);
            HelloWorldField field = new HelloWorldField(element);
            helloWorldClass.addField(field);
        }
    }

    private HelloWorldClass getAnnotatedClass(Element element) {
        TypeElement classElement = (TypeElement) element.getEnclosingElement();
        String fullClassName = classElement.getQualifiedName().toString();
        HelloWorldClass annotatedClass = annotatedClassMap.get(fullClassName);
        if (annotatedClass == null) {
            annotatedClass = new HelloWorldClass(classElement, elementUtils);
            annotatedClassMap.put(fullClassName, annotatedClass);
        }
        return annotatedClass;
    }
}
