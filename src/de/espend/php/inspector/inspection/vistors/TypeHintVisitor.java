package de.espend.php.inspector.inspection.vistors;

import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import de.espend.php.inspector.inspection.InspectionUtil;
import fr.adrienbrault.idea.symfony2plugin.util.PhpElementsUtil;
import org.json.JSONObject;

import java.util.List;

public class TypeHintVisitor {

    public static void visit(ClassReference classReference, List<JSONObject> jsonObjects, String filename) {

        String classFQN = classReference.getFQN();

        if(classFQN != null) {
            PhpClass phpClass = PhpElementsUtil.getClassInterface(classReference.getProject(), classFQN);
            if(phpClass != null) {
                String presentableFQN = phpClass.getPresentableFQN();
                if(presentableFQN == null) {
                    return;
                }

                JSONObject obj = new JSONObject();

                obj.put("type", "type_hint");

                obj.put("class", presentableFQN);
                obj.put("is_interface", phpClass.isInterface());
                obj.put("context", InspectionUtil.getContextString(classReference));
                obj.put("key", classReference.getTextRange().getStartOffset() + "-" + classReference.getTextRange().getEndOffset());

                jsonObjects.add(obj);
            }
        }

    }
}
