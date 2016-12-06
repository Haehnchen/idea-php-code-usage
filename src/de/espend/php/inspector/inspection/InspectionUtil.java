package de.espend.php.inspector.inspection;

import com.intellij.diff.util.DiffUtil;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiElement;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class InspectionUtil {

    public static JSONObject getContextString(PsiElement methodReference) {
        Document document = methodReference.getContainingFile().getViewProvider().getDocument();

        int inlineLine = document.getLineNumber(methodReference.getTextRange().getStartOffset());
        int startLine = inlineLine;
        int endLine = document.getLineNumber(methodReference.getTextRange().getEndOffset());

        if(startLine > 2) {
            startLine = startLine - 3;
        }

        if(endLine < document.getLineCount() - 3) {
            endLine = endLine + 3;
        }

        JSONArray jsonArray = new JSONArray();

        List<String> lines = DiffUtil.getLines(document, startLine, endLine);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            JSONObject context = new JSONObject();
            context.put("line", startLine + i);
            context.put("content", line);

            if(inlineLine == startLine + i) {
                context.put("is_highlight", true);
            }

            jsonArray.put(context);
        }

        JSONObject returnObject = new JSONObject();

        returnObject.put("context", jsonArray);
        returnObject.put("line", inlineLine);
        returnObject.put("start_line", startLine);
        returnObject.put("end_line", endLine);

        return returnObject;
    }
}
