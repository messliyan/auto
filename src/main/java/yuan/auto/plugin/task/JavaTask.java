package yuan.auto.plugin.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/**
 * 用Java语言编写Task
 * @author devwiki
 */
public class JavaTask extends DefaultTask {

    public static final String NAME = "gen";

    public JavaTask() {
        setGroup("auto-yuan");
        setDescription("代码生成");
    }

    @TaskAction
    void task() {
        System.out.println("java plugin: Hello, Java!");
    }
}
