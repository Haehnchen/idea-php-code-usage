package de.espend.php.inspector.inspection;


import com.intellij.codeInspection.*;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocType;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.parser.PhpElementTypes;
import com.jetbrains.php.lang.psi.PhpElementType;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.phpunit.PhpUnitUtil;
import de.espend.php.inspector.inspection.vistors.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainInspector extends LocalInspectionTool {

    @Nullable
    @Override
    public ProblemDescriptor[] checkFile(@NotNull PsiFile psiFile, @NotNull InspectionManager manager, boolean isOnTheFly) {

        if(isOnTheFly || PhpUnitUtil.isPhpUnitTestFile(psiFile)) {
            return super.checkFile(psiFile, manager, isOnTheFly);
        }

        VirtualFile virtualFile = psiFile.getVirtualFile();
        String relativePath = VfsUtil.getRelativePath(virtualFile, psiFile.getProject().getBaseDir(), '/');
        if(relativePath == null) {
            return super.checkFile(psiFile, manager, isOnTheFly);
        }

        String path = relativePath.replace("/", "-");

        VirtualFile composerFile = getVirtualComposerFile(psiFile);
        if(composerFile == null) {
            System.out.println("skipping " + psiFile.getVirtualFile().getPath());
            return super.checkFile(psiFile, manager, isOnTheFly);
        }

        PsiFile composerPsiFile = PsiManager.getInstance(psiFile.getProject()).findFile(composerFile);
        if(composerPsiFile == null) {
            System.out.println("skipping " + psiFile.getVirtualFile().getPath());
            return super.checkFile(psiFile, manager, isOnTheFly);
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(composerPsiFile.getText());
        } catch (JSONException e) {
            System.out.println("INVALID JSON FILE!!!! " + composerFile.getPath());
            return super.checkFile(psiFile, manager, isOnTheFly);
        }

        if(!jsonObject.has("name")) {
            System.out.println("skipping nameless " + psiFile.getVirtualFile().getPath());
            return super.checkFile(psiFile, manager, isOnTheFly);
        }

        JSONObject composerJson = new JSONObject();
        composerJson.put("name", jsonObject.optString("name"));
        if(jsonObject.has("homepage")) {
            composerJson.put("homepage", jsonObject.optString("homepage"));
        }

        final String filename = VfsUtil.getRelativePath(virtualFile, composerFile.getParent(), '/');
        if(filename == null) {
            return super.checkFile(psiFile, manager, isOnTheFly);
        }

        composerJson.put("file", filename);

        final List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
        psiFile.acceptChildren(new PsiRecursiveElementWalkingVisitor() {
            @Override
            public void visitElement(PsiElement element) {

                if(element instanceof PhpClass && !PhpUnitUtil.isTestClass((PhpClass) element)) {
                    ClassVisitor.visit((PhpClass) element, jsonObjects, filename);
                }

                if(element instanceof MethodReference) {
                    MethodReferenceVisitor.visit((MethodReference) element, jsonObjects, filename);
                }

                if(element instanceof NewExpression) {
                    NewExpressionVisitor.visit((NewExpression) element, jsonObjects, filename);
                }

                if(element instanceof PhpUse) {
                    UseVisitor.visit((PhpUse) element, jsonObjects, filename);
                }

                if(element instanceof PhpDocTag) {
                    PhpDocTagVisitor.visit((PhpDocTag) element, jsonObjects, filename);
                }

                if(element instanceof PhpDocType) {
                    PhpDocTypeVisitor.visit((PhpDocType) element, jsonObjects, filename);
                }

                if(element instanceof Parameter) {
                    PsiElement classRef = element.getFirstChild();
                    if(classRef instanceof ClassReference) {
                        TypeHintVisitor.visit((ClassReference) classRef, jsonObjects, filename);
                    }
                }

                if(element instanceof BinaryExpression && element.getNode().getElementType() == PhpElementTypes.INSTANCEOF_EXPRESSION) {
                    InstanceOfVisitor.visit((BinaryExpression) element, jsonObjects, filename);
                }

                super.visitElement(element);
            }
        });

        if(jsonObjects.size() == 0) {
            return super.checkFile(psiFile, manager, isOnTheFly);
        }


        JSONArray jsonArray = new JSONArray();
        for(JSONObject object: jsonObjects) {
            jsonArray.put(object);
        }

        composerJson.put("items", jsonArray);
        writeFile(composerJson.toString(), psiFile.getProject().getBasePath() + "/inspector/" + path);

        return super.checkFile(psiFile, manager, isOnTheFly);
    }

    @Nullable
    private static VirtualFile getVirtualComposerFile(PsiFile psiFile) {

        VirtualFile virtualFile = psiFile.getVirtualFile();
        String pathName = VfsUtil.getRelativePath(virtualFile, psiFile.getProject().getBaseDir(), '/');

        while (virtualFile != null && pathName != null) {
            VirtualFile composer = virtualFile.findFileByRelativePath("composer.json");
            if(composer != null) {
                return composer;
            }

            virtualFile = virtualFile.getParent();
            pathName = VfsUtil.getRelativePath(virtualFile, psiFile.getProject().getBaseDir(), '/');
        }

        return null;

    }

    public static void writeFile(String outputString, String outputPath) {

        File file = new File(outputPath);

        // create .idea folder, should not occur
        File folder = new File(file.getParent());
        if(!folder.exists() && !folder.mkdir()) {
            return;
        }

        FileWriter fw;
        try {
            fw = new FileWriter(file);
            fw.write(outputString);
            fw.close();
        } catch (IOException ignored) {
        }

    }

}
