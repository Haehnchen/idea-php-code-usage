package de.espend.php.inspector.writer;

import com.intellij.openapi.project.Project;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class XmlWriter {

    private static Map<Project, XmlWriter> instances = new HashMap<>();

    public static XmlWriter getInstance(Project project) {

        if(!instances.containsKey(project)) {
            instances.put(project, new XmlWriter(project));
        }

        return instances.get(project);
    }

    final private Project project;

    private XmlWriter(Project project) {
        this.project = project;
    }

    public void add(JSONObject jsonObject) {
        System.out.println(jsonObject.toString());
    }

}
