package de.espend.php.inspector.inspection.vistors;

import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpUse;
import de.espend.php.inspector.inspection.InspectionUtil;
import fr.adrienbrault.idea.symfony2plugin.util.AnnotationBackportUtil;
import fr.adrienbrault.idea.symfony2plugin.util.PhpElementsUtil;
import org.json.JSONObject;

import java.util.List;

public class PhpDocTagVisitor {

    public static void visit(PhpDocTag phpDocTag, List<JSONObject> jsonObjects, String filename) {

        PhpDocComment phpDocComment = PsiTreeUtil.getParentOfType(phpDocTag, PhpDocComment.class);
        if(phpDocComment != null) {
            PhpClass phpClass = AnnotationBackportUtil.getAnnotationReference(phpDocTag, AnnotationBackportUtil.getUseImportMap(phpDocComment));
            if(phpClass != null) {
                JSONObject classObj = new JSONObject();

                classObj.put("type", "annotation");
                classObj.put("class", phpClass.getPresentableFQN());
                classObj.put("context", InspectionUtil.getContextString(phpDocTag));
                classObj.put("key", filename + "-" + classObj.getJSONObject("context").opt("line"));

                jsonObjects.add(classObj);

            }
        }

    }

}
