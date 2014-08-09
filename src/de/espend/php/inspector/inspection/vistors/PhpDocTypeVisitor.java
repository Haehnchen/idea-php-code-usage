package de.espend.php.inspector.inspection.vistors;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.ResolveResult;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import de.espend.php.inspector.inspection.InspectionUtil;
import org.json.JSONObject;

import java.util.List;

public class PhpDocTypeVisitor {

    public static void visit(PhpDocType phpDocType, List<JSONObject> jsonObjects, String filename) {

        ResolveResult[] resolveResults = ((PsiPolyVariantReference) phpDocType.getReference()).multiResolve(false);
        for(ResolveResult resolveResult : resolveResults) {
            PsiElement element = resolveResult.getElement();
            if(element instanceof PhpClass) {
                JSONObject classObj = new JSONObject();

                classObj.put("type", "doc_type");
                classObj.put("class", ((PhpClass) element).getPresentableFQN());
                classObj.put("context", InspectionUtil.getContextString(phpDocType));
                classObj.put("key", phpDocType.getTextRange().getStartOffset() + "-" + phpDocType.getTextRange().getEndOffset());

                jsonObjects.add(classObj);

                return;
            }
        }

    }

}
