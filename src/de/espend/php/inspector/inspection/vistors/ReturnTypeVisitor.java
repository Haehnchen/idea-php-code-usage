package de.espend.php.inspector.inspection.vistors;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiWhiteSpace;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import de.espend.php.inspector.inspection.InspectionUtil;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

public class ReturnTypeVisitor {
    public static void visit(@NotNull ClassReference classReference, @NotNull List<JSONObject> jsonObjects, @NotNull String filename) {
        if(!PlatformPatterns.psiElement().afterLeafSkipping(
            PlatformPatterns.psiElement(PsiWhiteSpace.class),
            PlatformPatterns.psiElement().withText(":")).accepts(classReference)
            ) {
            return;
        }

        String fqn = classReference.getFQN();

        JSONObject classObj = new JSONObject();

        classObj.put("type", "return");
        classObj.put("class", fqn);
        classObj.put("context", InspectionUtil.getContextString(classReference));
        classObj.put("key", classReference.getTextRange().getStartOffset() + "-" + classReference.getTextRange().getEndOffset());

        jsonObjects.add(classObj);
    }
}
