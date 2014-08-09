package de.espend.php.inspector.inspection.vistors;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpClassHierarchyUtils;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.phpunit.PhpUnitUtil;
import de.espend.php.inspector.inspection.InspectionUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MethodReferenceVisitor {

    public static void visit(MethodReference methodReference, List<JSONObject> jsonObjects, String filename) {

        PsiElement psiElement = methodReference.resolve();
        if(psiElement instanceof Method) {

            PhpClass currentClass = ((Method) psiElement).getContainingClass();
            if(currentClass == null || PhpUnitUtil.isTestClass(currentClass)) {
                return;
            }

            final List<Method> methods = new ArrayList<Method>();
            methods.add((Method) psiElement);

            PhpClassHierarchyUtils.processSuperMembers((Method) psiElement, new PhpClassHierarchyUtils.HierarchyClassMemberProcessor() {
                @Override
                public boolean process(PhpClassMember member, PhpClass subClass, PhpClass baseClass) {
                    if ((member instanceof Method)) {
                        Method superMethod = baseClass.findOwnMethodByName(member.getName());
                        if (superMethod != null) {
                            PhpModifier.Access superAccess = superMethod.getModifier().getAccess();
                            PhpModifier.Access childAccess = member.getModifier().getAccess();
                            if ((childAccess.isPrivate()) || (superAccess.isPrivate())) return false;
                        }

                        if (superMethod != null) {
                            methods.add(superMethod);
                        }

                        return true;
                    }
                    return false;
                }
            });

            JSONObject obj = new JSONObject();
            JSONArray methodsJson = new JSONArray();

            obj.put("type", "method");
            obj.put("methods", methodsJson);
            obj.put("class", currentClass.getPresentableFQN());
            obj.put("context", InspectionUtil.getContextString(methodReference));
            obj.put("key", methodReference.getTextRange().getStartOffset() + "-" + methodReference.getTextRange().getEndOffset());
            obj.put("name", methodReference.getName());

            methodReference.getTextRange().getStartOffset();
            methodReference.getTextRange().getEndOffset();

            for(Method method: methods) {

                JSONObject value = new JSONObject();
                PhpClass containingClass = method.getContainingClass();
                if(containingClass != null) {
                    value.put("class", containingClass.getPresentableFQN());
                }

                methodsJson.put(value);
            }

            jsonObjects.add(obj);
        }
    }

}
