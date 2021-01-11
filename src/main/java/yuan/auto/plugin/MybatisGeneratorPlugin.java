package yuan.auto.plugin;
import java.util.ArrayList;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.tasks.TaskContainer;
import yuan.auto.plugin.task.MybatisGeneratorTask;

/**
 * 用Java语言编写Plugin
 * @author lijiabin
 */
public class MybatisGeneratorPlugin implements Plugin<Project> {

    private Configuration mgConfiguration;


    public void apply(Project project) {

        this.registerTasks(project);
        System.out.println("+++++++++ apply new application plugin end line ++++++++++");
    }

    private void registerTasks(Project project) {
        TaskContainer tc = project.getTasks();

        this.mgConfiguration = (Configuration)project.getConfigurations().create("mgConfiguration");
        this.mgConfiguration.defaultDependencies((dependencies) -> {
            dependencies.addAll(new ArrayList<Dependency>() {
                {
                    this.add(project.getDependencies().create("org.mybatis.generator:mybatis-generator-core:1.4.0"));
                    this.add(project.getDependencies().create("org.postgresql:postgresql:42.2.5"));
                }
            });
        });
        tc.register("generator", MybatisGeneratorTask.class, (task) -> {
            task.setGroup("mybatis auto");
            task.setDescription("mybatis generator...");
            task.getMGClassPath().from(new Object[]{this.mgConfiguration});
        });
    }
}
