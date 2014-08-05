package de.espend.php.inspector.inspection.vistors;

import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpUse;
import com.jetbrains.php.phpunit.PhpUnitUtil;
import de.espend.php.inspector.inspection.InspectionUtil;
import fr.adrienbrault.idea.symfony2plugin.util.PhpElementsUtil;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UseVisitor {

    public static void visit(PhpUse phpUse, List<JSONObject> jsonObjects, String filename) {

        ClassReference classReference = phpUse.getClassReference();
        if(classReference != null) {
            String classFQN = classReference.getFQN();

            if(classFQN != null) {
                PhpClass phpClass = PhpElementsUtil.getClassInterface(phpUse.getProject(), classFQN);
                if(phpClass != null) {
                    JSONObject classObj = new JSONObject();

                    classObj.put("type", "use");
                    classObj.put("class", phpClass.getPresentableFQN());
                    classObj.put("context", InspectionUtil.getContextString(phpUse));
                    classObj.put("key", filename + "-" + classObj.getJSONObject("context").opt("line"));

                    jsonObjects.add(classObj);

                }

            }
        }


    }

}
