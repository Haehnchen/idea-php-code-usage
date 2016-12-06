package de.espend.php.inspector.visitor;

import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.jetbrains.php.lang.PhpFileType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import de.espend.php.inspector.inspection.vistors.PhpDocTypeVisitor;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @see de.espend.php.inspector.inspection.vistors.PhpDocTypeVisitor
 */
public class PhpDocTypeVisitorTest extends LightCodeInsightFixtureTestCase {
    public void testReturnTypeOnDocComment() {
        myFixture.configureByText("test.php", "<?php\n" +
            "\n" +
            "namespace Foobar\n" +
            "{\n" +
            "    class Foo {}\n" +
            "}"
        );

        PsiFile psiFile = myFixture.configureByText(PhpFileType.INSTANCE, "<?php\n" +
            "namespace Car;\n" +
            "use Foobar\\Foo;\n" +
            "class DefaultController\n" +
            "{\n" +
            "    /**\n" +
            "     * @return Foo\n" +
            "     */\n" +
            "    public function indexAction($name)\n" +
            "    {\n" +
            "    }\n" +
            "}"
        );

        PhpDocType phpDocType = PsiTreeUtil.findChildOfType(psiFile, PhpDocType.class);

        List<JSONObject> jsonObject = new ArrayList<>();

        PhpDocTypeVisitor.visit(phpDocType, jsonObject, "foobar");

        assertEquals("return", jsonObject.get(0).getString("type"));
        assertEquals("Foobar\\Foo", jsonObject.get(0).getString("class"));
        assertEquals("86-89", jsonObject.get(0).getString("key"));
    }
}
