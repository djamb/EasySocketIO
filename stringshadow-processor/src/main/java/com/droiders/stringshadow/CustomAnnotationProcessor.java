package com.droiders.stringshadow;

import com.android.streye.constant_share.ExtendsValue;
import com.android.streye.constant_share.InterfaceValue;
import com.android.streye.constant_share.MethodValue;
import com.android.streye.constant_share.StringValue;
//import com.android.streye.constant_share.InterfaceValue;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class CustomAnnotationProcessor extends AbstractProcessor {

  private static final String RANDOMIZER_SUFFIX = "_Shadow";

  private static final String SOCKET_ON_SELECTOR = "SocketOnSelector";
  private static final String SUPER_SERVICE = "SuperService";

  private static final String TARGET_STATEMENT_FORMAT = "target.%1$s = %2$s";
  private static final String CONST_PARAM_TARGET_NAME = "target";

  private static final char CHAR_DOT = '.';

  private static final List<Class<? extends Annotation>> STRING_TYPES = new ArrayList<>();
  private Messager messager;
  private Types typesUtil;
  private Elements elementsUtil;
  private Filer filer;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    System.out.println("aminano mierda1");
    STRING_TYPES.add(InterfaceValue.class);
    STRING_TYPES.add(ExtendsValue.class);
    STRING_TYPES.add(MethodValue.class);

    messager = processingEnv.getMessager();
    typesUtil = processingEnv.getTypeUtils();
    elementsUtil = processingEnv.getElementUtils();
    filer = processingEnv.getFiler();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> annotations = new LinkedHashSet<>();
    for (Class<? extends Annotation> annotation : STRING_TYPES) {
      annotations.add(annotation.getCanonicalName());
    }
    return annotations;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

    //Interface Add
    Map<String, List<AnnotatedShadowElement>> annotatedElementMap2 = new LinkedHashMap<>();
    for (Element element : roundEnv.getElementsAnnotatedWith(InterfaceValue.class)) {
      AnnotatedInterface randomizerElement2 = new AnnotatedInterface(element);
      messager.printMessage(Diagnostic.Kind.NOTE, randomizerElement2.toString());
      if (!randomizerElement2.isTypeValid(elementsUtil, typesUtil)) {
        messager.printMessage(Diagnostic.Kind.ERROR,
            randomizerElement2.getSimpleClassName().toString()
                + "#"
                + element.getSimpleName()
                + " is not in valid type String");
      }
      addAnnotatedElement(annotatedElementMap2, randomizerElement2);
    }

    Map<String, List<AnnotatedShadowElement>> annotatedElementMap3 = new LinkedHashMap<>();
    for (Element element1 : roundEnv.getElementsAnnotatedWith(ExtendsValue.class)) {
      AnnotatedExtends randomizerElement3 = new AnnotatedExtends(element1);
      addAnnotatedExtends(annotatedElementMap3, randomizerElement3);
    }

    //TODO VOY POR EL METHOD
    Map<String, List<AnnotatedShadowElement>> annotatedElementMap4 = new LinkedHashMap<>();
    for (Element element1 : roundEnv.getElementsAnnotatedWith(MethodValue.class)) {
      AnnotatedMethod randomizerElement4 = new AnnotatedMethod(element1);
      addAnnotatedElement(annotatedElementMap4, randomizerElement4);
    }

    if (annotatedElementMap2.size() == 0
        && annotatedElementMap3.size() == 0
        && annotatedElementMap4.size() == 0) {
      return true;
    }

    try {

      //Create Interface
      for (Map.Entry<String, List<AnnotatedShadowElement>> entry : annotatedElementMap2.entrySet()) {
        TypeSpec binder1 = createInterface(getClassName(entry.getKey()), entry.getValue());
        JavaFile javaFile1 = JavaFile.builder(getPackage(entry.getKey()), binder1).build();
        javaFile1.writeTo(filer);
      }

      String mierder = "";
      for (Map.Entry<String, List<AnnotatedShadowElement>> entry : annotatedElementMap4.entrySet()) {
        mierder = getPackage(entry.getKey());
      }

      TypeSpec binder = createMagicClass(annotatedElementMap4);
      JavaFile javaFile = JavaFile.builder(mierder, binder).build();
      javaFile.writeTo(filer);

      TypeSpec binder5 = createSocketOnClass(annotatedElementMap4);
      JavaFile javaFile5 = JavaFile.builder(mierder, binder5).build();
      javaFile5.writeTo(filer);

      //Create Extends2
      for (Map.Entry<String, List<AnnotatedShadowElement>> entry : annotatedElementMap3.entrySet()) {

        //TypeSpec binder2 = createClassWithExtends2(entry.getValue());
        //JavaFile javaFile2 = JavaFile.builder(getPackage(entry.getKey()), binder2).build();
        //javaFile2.writeTo(filer);

        TypeSpec binder3 = createrSuperService(entry.getValue());
        JavaFile javaFile3 = JavaFile.builder(mierder, binder3).build();
        javaFile3.writeTo(filer);
      }
    } catch (IOException e) {

      messager.printMessage(Diagnostic.Kind.ERROR, "Error on creating java file");
    }

    return true;
  }

  private MethodSpec createSuperConstructor(List<AnnotatedShadowElement> randomElements) {
    AnnotatedShadowElement firstElement = randomElements.get(0);
    MethodSpec.Builder builder = MethodSpec.constructorBuilder()
        .addModifiers(Modifier.PUBLIC)
        .addParameter(Integer.TYPE, "numero")
        .addStatement("super(numero)")

        .addStatement("listenALGO()");

    return builder.build();
  }

  private void addStatement(MethodSpec.Builder builder, AnnotatedShadowElement randomElement) {
    builder.addStatement(
        String.format(TARGET_STATEMENT_FORMAT, randomElement.getElementName().toString(),
            randomElement.getShadowValue()));
  }

  private TypeSpec createClass(String className, MethodSpec constructor) {
    return TypeSpec.classBuilder(className + RANDOMIZER_SUFFIX)
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addMethod(constructor)
        .build();
  }

  private String getPackage(String qualifier) {
    return qualifier.substring(0, qualifier.lastIndexOf(CHAR_DOT));
  }

  private String getClassName(String qualifier) {

    return qualifier.substring(qualifier.lastIndexOf(CHAR_DOT) + 1);
  }

  private void addAnnotatedElement(Map<String, List<AnnotatedShadowElement>> map,
      AnnotatedShadowElement randomElement) {
    String qualifier = randomElement.getQualifiedClassName().toString();
    if (map.get(qualifier) == null) {
      map.put(qualifier, new ArrayList<AnnotatedShadowElement>());
    }
    map.get(qualifier).add(randomElement);
  }

  private void addAnnotatedExtends(Map<String, List<AnnotatedShadowElement>> map,
      AnnotatedExtends randomElement) {

    String qualifier =
        getPackage(randomElement.getQualifiedClassName().toString()) + ".ExtendOf" + getClassName(
            TypeName.get(randomElement.getClassTypeMirror()).toString());

    //String qualifier = randomElement.getQualifiedClassName().toString();

    if (map.get(qualifier) == null) {
      map.put(qualifier, new ArrayList<AnnotatedShadowElement>());
    }

    map.get(qualifier).add(randomElement);
  }

  //TODO Add By aminano

  private TypeSpec createInterface(String className, List<AnnotatedShadowElement> randomElements) {

    TypeSpec.Builder tin = TypeSpec.interfaceBuilder(className + RANDOMIZER_SUFFIX + "1")
        .addModifiers(Modifier.PUBLIC);

    for (int i = 0; i < randomElements.size(); i++) {
      MethodSpec ms = MethodSpec.methodBuilder(randomElements.get(i).getShadowValue())
          .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
          .build();
      tin.addMethod(ms);
    }

    return tin.build();
  }

  private TypeSpec createClassWithExtends2(List<AnnotatedShadowElement> randomElements) {
    return TypeSpec.classBuilder("ExtendOf" + getClassName(
        TypeName.get(randomElements.get(0).getClassTypeMirror()).toString()))
        .addModifiers(Modifier.PUBLIC)
        //.addMethod(constructor)
        .addMethod(MethodSpec.methodBuilder("listenALGO").addModifiers(Modifier.PUBLIC)
            //.beginControlFlow("socket.on(EVENT_CONNECT, new Emitter.Listener() ")
            .beginControlFlow("socket.on(\"call_init\", args ->")

            .endControlFlow(")")
            //.returns(String.class)
            //.addStatement("return this.name")
            .build())
        .superclass(TypeName.get(randomElements.get(0).getClassTypeMirror()))
        .build();
  }

  private TypeSpec createSocketOnClass(Map<String, List<AnnotatedShadowElement>> randomElements) {
    ClassName requestManager2 = ClassName.get("io.socket.client", "Socket");
    FieldSpec bindClass3 = FieldSpec.builder(requestManager2, "socket").build();

    ParameterSpec bindClass2 = ParameterSpec.builder(requestManager2, "socket").build();

    String packageName = "";
    Set<String> validate = new HashSet<>();
    String addMethodToConstructor = "";
    for (Map.Entry<String, List<AnnotatedShadowElement>> entry : randomElements.entrySet()) {
      packageName = getPackage(entry.getKey());
      for (AnnotatedShadowElement a : entry.getValue()) {
        validate.add(a.getShadowValue().replaceAll("\"", ""));
      }
    }

    ClassName requestManager = ClassName.get(packageName, SOCKET_ON_SELECTOR);
    ParameterSpec strings = ParameterSpec.builder(requestManager, "bindClass").build();
    FieldSpec bindClass = FieldSpec.builder(requestManager, "bindClass").build();

    TypeSpec.Builder ts = TypeSpec.classBuilder("SocketOn")
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addField(bindClass3)
        .addField(bindClass);

    //String seconds="5000";
    int count = 0;
    for (String s : validate) {
      ts.addMethod(MethodSpec.methodBuilder("onMethod" + s)
          .addModifiers(Modifier.PUBLIC)
          .addCode("socket.on(\""
              + s
              + "\", args -> {\n"
              + "   bindClass.listenALGO(args,\""
              + s
              + "\");\n"
              + "});\n")

          .build());
      if (count <= validate.size() - 2) {
        addMethodToConstructor = addMethodToConstructor + "onMethod" + s + "();\n";
      } else {
        addMethodToConstructor = addMethodToConstructor + "onMethod" + s + "()";
      }
      count++;
    }

    ts.addMethod(MethodSpec.methodBuilder("socket")
        .addModifiers(Modifier.PUBLIC)
        .addStatement("this.socket=socket")
        .addParameter(bindClass2)
        .addCode(addMethodToConstructor+";\n")
        .build());

    TypeVariableName typeVariable = TypeVariableName.get("Object");
    ParameterSpec ObjectArray =
        ParameterSpec.builder(ArrayTypeName.of(typeVariable), "sendData").build();
    ts.addMethod(MethodSpec.methodBuilder("emit")
        .addModifiers(Modifier.PUBLIC)
        .addParameter(String.class, "dataEvent")
        .addParameter(ObjectArray)
        .varargs(true)
        .addCode("if (socket != null) {\n"
            + "  io.socket.emitter.Emitter emitter = socket.emit(dataEvent, sendData);\n"
            + "  return true;\n"
            + "}else{\n"
            + "  return false;\n"
            + "}\n")

        .returns(boolean.class)
        .build());

    MethodSpec constructorBuilder = MethodSpec.constructorBuilder()
        .addModifiers(Modifier.PUBLIC)
        .addParameter(strings)
        .addStatement("this.bindClass=bindClass")

        .build();

    ts.addMethod(constructorBuilder);

    return ts.build();
  }

  private TypeSpec createMagicClass(Map<String, List<AnnotatedShadowElement>> randomElements) {
    TypeVariableName typeVariable = TypeVariableName.get("Object");
    ParameterSpec ObjectArray =
        ParameterSpec.builder(ArrayTypeName.of(typeVariable), "object").build();
    ClassName requestManager2 = ClassName.get("java.lang", "Object");
    ParameterSpec strings2 = ParameterSpec.builder(requestManager2, "bindClass").build();
    return TypeSpec.classBuilder(SOCKET_ON_SELECTOR)
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .addField(Map.class, "methodsSolution")
        .addMethod(MethodSpec.methodBuilder("listenALGO")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(ObjectArray)
            .addParameter(String.class, "socketName")
            .addCode(
                "  java.util. List<com.droiders.stringshadow.MethodClass> methodClassList = (java.util.ArrayList<com.droiders.stringshadow.MethodClass>) methodsSolution.get(socketName);\n"
                    + "    for (com.droiders.stringshadow.MethodClass methodClass : methodClassList) {\n"
                    + "      try {\n"
                    + "        methodClass.getMethod().invoke(methodClass.getBindClass(), new Object[] { object });\n"
                    + "      } catch (IllegalAccessException e) {\n"
                    + "        e.printStackTrace();\n"
                    + "      } catch (java.lang.reflect.InvocationTargetException e) {\n"
                    + "        e.printStackTrace();\n"
                    + "      }\n"
                    + "    }\n")
            .build())

        .addMethod(MethodSpec.methodBuilder("bindViewer")
            .addParameter(strings2)
            .addModifiers(Modifier.PUBLIC)
            .addCode("            if (methodsSolution == null) {\n"
                + "              this.methodsSolution = new java.util.HashMap<String, java.util.List<com.droiders.stringshadow.MethodClass>>();\n"
                + "            }\n"
                + "            java.lang.reflect.Method[] methods = bindClass.getClass().getMethods();\n"
                + "            for (java.lang.reflect.Method method : methods) {\n"
                + "              com.android.streye.constant_share.MethodValue annos =\n"
                + "                  method.getAnnotation(com.android.streye.constant_share.MethodValue.class);\n"
                + "              if (annos != null) {\n"
                + "\n"
                + "                java.util.List<com.droiders.stringshadow.MethodClass> methodClassList =\n"
                + "                    (  java.util.ArrayList<com.droiders.stringshadow.MethodClass>) methodsSolution.get(annos.value());\n"
                + "                if (methodClassList == null) {\n"
                + "                  methodClassList = new   java.util.ArrayList<>();\n"
                + "                }\n"
                + "                methodClassList.add(new  com.droiders.stringshadow.MethodClass(bindClass, method));\n"
                + "\n"
                + "                methodsSolution.put(annos.value(), methodClassList);\n"
                + "              }\n"
                + "            }")
            .build())

        .addMethod(MethodSpec.methodBuilder("unBind")
            .addParameter(strings2)
            .addModifiers(Modifier.PUBLIC)
            .addCode(" for (Object list : methodsSolution.keySet()) {\n"
                + "      java.util.ArrayList<com.droiders.stringshadow.MethodClass>  arrayList=(java.util.ArrayList<com.droiders.stringshadow.MethodClass>)methodsSolution.get(list);\n"
                + "      for (com.droiders.stringshadow.MethodClass methodClass : arrayList) {\n"
                + "        if (methodClass.getBindClass() == bindClass) {\n"
                + "          arrayList.remove(methodClass);\n"
                + "          break;\n"
                + "        }\n"
                + "      }\n"
                + "    }")
            .build())

        .build();
  }

  //private MethodSpec createMagicConstructor() {
  //
  //  ClassName requestManager = ClassName.get("java.lang", "Object");
  //  ParameterSpec strings = ParameterSpec.builder(requestManager, "bindClass").build();
  //  MethodSpec.Builder builder = MethodSpec.constructorBuilder()
  //      .addModifiers(Modifier.PUBLIC)
  //      .addParameter(strings)
  //      .addStatement("this.bindClass=bindClass")
  //      .addStatement(
  //
  //          "            if (methodsSolution == null) {\n"
  //              + "              this.methodsSolution = new java.util.HashMap<String, java.util.List<com.droiders.stringshadow.MethodClass>>();\n"
  //              + "            }\n"
  //              + "            java.lang.reflect.Method[] methods = bindClass.getClass().getMethods();\n"
  //              + "            for (java.lang.reflect.Method method : methods) {\n"
  //              + "              com.android.streye.constant_share.MethodValue annos =\n"
  //              + "                  method.getAnnotation(com.android.streye.constant_share.MethodValue.class);\n"
  //              + "              if (annos != null) {\n"
  //              + "\n"
  //              + "                java.util.List<com.droiders.stringshadow.MethodClass> methodClassList =\n"
  //              + "                    (  java.util.ArrayList<com.droiders.stringshadow.MethodClass>) methodsSolution.get(annos.value());\n"
  //              + "                if (methodClassList == null) {\n"
  //              + "                  methodClassList = new   java.util.ArrayList<>();\n"
  //              + "                }\n"
  //              + "                methodClassList.add(new  com.droiders.stringshadow.MethodClass(bindClass, method));\n"
  //              + "\n"
  //              + "                methodsSolution.put(annos.value(), methodClassList);\n"
  //              + "              }\n"
  //              + "            }");
  //
  //  return builder.build();
  //}

  private TypeSpec createrSuperService(List<AnnotatedShadowElement> randomElements) {
    ClassName requestManager = ClassName.get("java.lang", "Object");
    FieldSpec bindClass = FieldSpec.builder(requestManager, "bindClass").build();
    TypeVariableName typeVariable = TypeVariableName.get("Object");
    ParameterSpec ObjectArray =
        ParameterSpec.builder(ArrayTypeName.of(typeVariable), "object").build();
    ClassName requestManager2 = ClassName.get("java.lang", "Object");
    ParameterSpec strings2 = ParameterSpec.builder(requestManager2, "bindClass").build();
    return TypeSpec.classBuilder(SUPER_SERVICE)
        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        .superclass(TypeName.get(randomElements.get(0).getClassTypeMirror()))
        .build();
  }
}
