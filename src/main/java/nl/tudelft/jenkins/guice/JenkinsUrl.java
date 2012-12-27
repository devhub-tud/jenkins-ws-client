package nl.tudelft.jenkins.guice;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD, PARAMETER, METHOD})
public @interface JenkinsUrl {}