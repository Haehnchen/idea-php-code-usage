package de.espend.php.inspector.inspection.vistors;

import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpReference;
import com.jetbrains.php.lang.psi.elements.PhpUse;
import de.espend.php.inspector.inspection.InspectionUtil;
import fr.adrienbrault.idea.symfony2plugin.util.PhpElementsUtil;
import org.json.JSONObject;

import java.util.List;

public class UseVisitor {

    public static void visit(PhpUse phpUse, List<JSONObject> jsonObjects, String filename) {

        PhpReference classReference = phpUse.getTargetReference();
        if(classReference != null) {
            String classFQN = classReference.getFQN();

            if(classFQN != null) {
                PhpClass phpClass = PhpElementsUtil.getClassInterface(phpUse.getProject(), classFQN);
                if(phpClass != null) {
                    JSONObject classObj = new JSONObject();

                    classObj.put("type", "use");
                    classObj.put("class", phpClass.getPresentableFQN());
                    classObj.put("context", InspectionUtil.getContextString(phpUse));
                    classObj.put("key", phpUse.getTextRange().getStartOffset() + "-" + phpUse.getTextRange().getEndOffset());

                    jsonObjects.add(classObj);

                }

            }
        }


    }

}
