package yuan.auto.plugin.task;

import org.gradle.api.Task;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.tasks.Classpath;

public interface CompileTask extends Task {
  @Classpath
  ConfigurableFileCollection getMGClassPath();
}
