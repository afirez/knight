package com.afirez.apt.compiler;


import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.List;


public class HelloWorldClass {
    public TypeElement classElement;
    public List<HelloWorldField> fields = new ArrayList<>();
    public Elements elementUtils;

    public HelloWorldClass(TypeElement classElement, Elements elementUtils) {
        this.classElement = classElement;
        this.elementUtils = elementUtils;
    }

    public JavaFile generateHelloWorld() {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("helloWorld")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(classElement.asType()), "host", Modifier.FINAL)
                .returns(TypeName.get(String.class));
        for (HelloWorldField field : fields) {

        }

        methodBuilder.addStatement("return \"Hello World!\"");
        // generate whole class
        TypeSpec helloWorldClass = TypeSpec.classBuilder(classElement.getSimpleName() + "Api")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodBuilder.build())
                .build();

        String packageName = elementUtils.getPackageOf(classElement).getQualifiedName().toString();
        // generate file
        return JavaFile.builder(packageName, helloWorldClass).build();
    }

    public void addField(HelloWorldField field) {
        fields.add(field);
    }

    public String getFullClassName() {
        return elementUtils.getPackageOf(classElement).getQualifiedName().toString()
                + classElement.getSimpleName() + "Api";
    }
}
