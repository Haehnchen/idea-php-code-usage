package de.espend.php.inspector.inspection.vistors;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.parser.PhpElementTypes;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.phpunit.PhpUnitUtil;
import de.espend.php.inspector.inspection.InspectionUtil;
import fr.adrienbrault.idea.symfony2plugin.util.PhpElementsUtil;
import org.json.JSONObject;

import java.util.List;

public class InstanceOfVisitor {

    public static void visit(BinaryExpression binaryExpression, List<JSONObject> jsonObjects, String filename) {

        if(binaryExpression.getNode().getElementType() == PhpElementTypes.INSTANCEOF_EXPRESSION) {
            PsiElement psiElement = binaryExpression.getRightOperand();
            if(psiElement instanceof ClassReference) {
                String classFQN = ((ClassReference) psiElement).getFQN();
                if(classFQN != null) {
                    PhpClass phpClass = PhpElementsUtil.getClassInterface(binaryExpression.getProject(), classFQN);
                    if(phpClass != null && !PhpUnitUtil.isTestClass(phpClass)) {

                        String presentableFQN = phpClass.getPresentableFQN();

                        if(presentableFQN == null) {
                            return;
                        }

                        JSONObject obj = new JSONObject();

                        obj.put("type", "instanceof");

                        obj.put("class", presentableFQN);
                        obj.put("is_interface", phpClass.isInterface());
                        obj.put("context", InspectionUtil.getContextString(binaryExpression));
                        obj.put("key", binaryExpression.getTextRange().getStartOffset() + "-" + binaryExpression.getTextRange().getEndOffset());

                        jsonObjects.add(obj);
                    }

                }
            }
        }
    }
}
