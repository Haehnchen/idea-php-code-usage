package de.espend.php.inspector.inspection.vistors;

import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClassVisitor {

    public static void visit(PhpClass phpClass, List<JSONObject> jsonObjects, String filename) {

        JSONObject classObj = new JSONObject();
        classObj.put("type", "class");
        classObj.put("class", phpClass.getPresentableFQN());

        PhpDocComment phpDocComment = phpClass.getDocComment();
        if(phpDocComment != null) {
            classObj.put("doc_comment", phpDocComment.getText());
        }

        jsonObjects.add(classObj);

        ClassStructureContainer container = new ClassStructureContainer();
        isInstanceOf(container, phpClass, true);

        for(PhpClass extendClass: container.getClasses()) {
            JSONObject obj = new JSONObject();

            obj.put("type", "extends");
            obj.put("class", phpClass.getPresentableFQN());
            obj.put("is_interface", phpClass.isInterface());
            obj.put("child", extendClass.getPresentableFQN());

            jsonObjects.add(obj);
        }

        for(PhpClass interfaceClass: container.getInterfaces()) {

            JSONObject obj = new JSONObject();

            obj.put("type", "implements");
            obj.put("class", phpClass.getPresentableFQN());
            obj.put("is_interface", phpClass.isInterface());
            obj.put("child", interfaceClass.getPresentableFQN());

            jsonObjects.add(obj);
        }

    }


    private static void isInstanceOf(ClassStructureContainer container, @NotNull PhpClass subjectClass) {
        isInstanceOf(container, subjectClass, false);
    }

    private static void isInstanceOf(ClassStructureContainer container, @NotNull PhpClass subjectClass, boolean first) {

        if(!first) {
            if (subjectClass.isInterface()) {
                container.addInterface(subjectClass);
            } else {
                container.addClass(subjectClass);
            }
        }

        for (PhpClass implementedInterface : subjectClass.getImplementedInterfaces()) {
            isInstanceOf(container, implementedInterface);
        }

        if (null == subjectClass.getSuperClass()) {
            return;
        }

        isInstanceOf(container, subjectClass.getSuperClass());
    }

    private static class ClassStructureContainer {

        final private List<PhpClass> interfaces = new ArrayList<>();
        final private List<PhpClass> classes = new ArrayList<>();

        public ClassStructureContainer() {

        }

        public List<PhpClass> getInterfaces() {
            return interfaces;
        }

        public List<PhpClass> getClasses() {
            return classes;
        }

        public void addClass(PhpClass phpClass) {
            this.classes.add(phpClass);
        }

        public void addInterface(PhpClass phpClass) {
            this.interfaces.add(phpClass);
        }
    }

}
