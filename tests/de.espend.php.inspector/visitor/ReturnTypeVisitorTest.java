package de.espend.php.inspector.visitor;

import com.intellij.psi.PsiElement;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.jetbrains.php.lang.PhpFileType;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import de.espend.php.inspector.inspection.vistors.ReturnTypeVisitor;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @see de.espend.php.inspector.inspection.vistors.ReturnTypeVisitor
 */
public class ReturnTypeVisitorTest extends LightCodeInsightFixtureTestCase {
    public void testReturnType() {
        myFixture.configureByText(PhpFileType.INSTANCE, "<?php \n" +
            "class DefaultController\n" +
            "{\n" +
            "    public function indexAction($name) : \\Dat<caret>eTime\n" +
            "    {\n" +
            "    }\n" +
            "}"
        );

        PsiElement psiElement = myFixture.getFile().findElementAt(myFixture.getCaretOffset());

        List<JSONObject> jsonObject = new ArrayList<>();

        ReturnTypeVisitor.visit((ClassReference) psiElement.getParent(), jsonObject, "foobar");

        assertEquals("return", jsonObject.get(0).getString("type"));
        assertEquals("\\DateTime", jsonObject.get(0).getString("class"));
        assertEquals("74-83", jsonObject.get(0).getString("key"));
    }
}
