package yuan.auto.plugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import yuan.auto.plugin.task.JavaTask;

/**
 * 用Java语言编写Plugin
 * @author lijiabin
 */
public class JavaPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {
        target.getExtensions().create(JavaPluginExtension.NAME, JavaPluginExtension.class);
        target.getTasks().create(JavaTask.NAME, JavaTask.class);
    }
}
