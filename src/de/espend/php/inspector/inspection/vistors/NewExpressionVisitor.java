package de.espend.php.inspector.inspection.vistors;

import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.NewExpression;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.phpunit.PhpUnitUtil;
import de.espend.php.inspector.inspection.InspectionUtil;
import fr.adrienbrault.idea.symfony2plugin.util.PhpElementsUtil;
import org.json.JSONObject;

import java.util.List;

public class NewExpressionVisitor {

    public static void visit(NewExpression newExpression, List<JSONObject> jsonObjects, String filename) {
        ClassReference classReference = newExpression.getClassReference();
        if(classReference != null) {
            String classFQN = classReference.getFQN();

            if(classFQN != null) {
                PhpClass phpClass = PhpElementsUtil.getClassInterface(newExpression.getProject(), classFQN);
                if(phpClass != null && !PhpUnitUtil.isTestClass(phpClass)) {
                    String presentableFQN = phpClass.getPresentableFQN();
                    if(presentableFQN == null) {
                        return;
                    }

                    JSONObject obj = new JSONObject();

                    obj.put("type", "instance");

                    obj.put("class", presentableFQN);
                    obj.put("is_interface", phpClass.isInterface());
                    obj.put("context", InspectionUtil.getContextString(newExpression));
                    obj.put("key", filename + "-" + obj.getJSONObject("context").opt("line"));

                    jsonObjects.add(obj);
                }
            }

        }
    }

}
