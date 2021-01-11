package yuan.auto.plugin.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.SkipWhenEmpty;
import org.gradle.api.tasks.TaskAction;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.JavaTypeResolverConfiguration;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.plugins.ToStringPlugin;
import yuan.auto.plugin.Utils;
import yuan.auto.plugin.mybatis.DateTypeResolver;
import yuan.auto.plugin.mybatis.PgPaginationPlugin;
import yuan.auto.plugin.mybatis.PgReturningIdInsertPlugin;
import yuan.auto.plugin.mybatis.RenameJavaFilePlugin;

@CacheableTask
public class MybatisGeneratorTask extends DefaultTask implements CompileTask {
  @Classpath
  private final ConfigurableFileCollection mgClasspath = this.getProject().files(new Object[0]);
  @InputFiles
  @PathSensitive(PathSensitivity.RELATIVE)
  @SkipWhenEmpty
  private final ConfigurableFileCollection classFiles = this.getProject().files(new Object[0]);

  public MybatisGeneratorTask() {
  }

  public ConfigurableFileCollection getMGClassPath() {
    return this.mgClasspath;
  }

  @TaskAction
  public void run() {
    boolean overwrite = true;
    DefaultShellCallback callback = new DefaultShellCallback(overwrite);
    List<String> warnings = new ArrayList();
    File configFile = new File("./mybatis-generator.xml");
    ConfigurationParser cp = new ConfigurationParser(warnings);

    try {
      Configuration config = cp.parseConfiguration(configFile);
      config.getContexts().stream().forEach((ctx) -> {
        (new ArrayList<Class>() {
          {
            this.add(ToStringPlugin.class);
            this.add(PgPaginationPlugin.class);
            this.add(PgReturningIdInsertPlugin.class);
            this.add(RenameJavaFilePlugin.class);
          }
        }).stream().forEach((p) -> {
          ctx.addPluginConfiguration(this.getPlugin(p));
        });
        JavaTypeResolverConfiguration resolverConfiguration = new JavaTypeResolverConfiguration();
        resolverConfiguration.setConfigurationType(DateTypeResolver.class.getName());
        resolverConfiguration.addProperty("useJSR310", "true");
        ctx.setJavaTypeResolverConfiguration(resolverConfiguration);
        Properties comment = new Properties();
        comment.setProperty("suppressAllComments", "true");
        comment.setProperty("suppressDate", "true");
        comment.setProperty("addRemarkComments", "false");
        ctx.getCommentGenerator().addConfigurationProperties(comment);
      });
      MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
      myBatisGenerator.generate((ProgressCallback)null);
    } catch (Exception var8) {
      Utils.errorPrint(new Object[]{" mybatis generator error : ", var8.getMessage()});
    }

  }

  private PluginConfiguration getPlugin(Class pluginCls) {
    PluginConfiguration pluginConfig = new PluginConfiguration();
    pluginConfig.setConfigurationType(pluginCls.getName());
    return pluginConfig;
  }
}