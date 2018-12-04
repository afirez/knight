package com.afirez.apt.compiler;

import com.afirez.apt.api.HelloWorld;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.VariableElement;

public class HelloWorldField {
    private VariableElement mFieldElement;

    private String name;

    public HelloWorldField(Element element) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(
                    String.format("Only fields can be annotated with @%s", HelloWorld.class.getSimpleName()));
        }
        mFieldElement = (VariableElement) element;
        HelloWorld bindView = mFieldElement.getAnnotation(HelloWorld.class);
        name = bindView.value();
    }


}
