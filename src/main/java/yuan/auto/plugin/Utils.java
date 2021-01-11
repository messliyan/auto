package yuan.auto.plugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import org.codehaus.groovy.runtime.ProcessGroovyMethods;
import org.gradle.api.invocation.Gradle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Utils {
  private static final Logger logger = LoggerFactory.getLogger(Utils.class);
  public static Integer logSwitch = 0;
  public static final AtomicInteger line = new AtomicInteger(0);

  public Utils() {
  }

  public static String reverse(String input) {
    char[] in = input.toCharArray();
    int begin = 0;

    for(int end = in.length - 1; end > begin; ++begin) {
      char temp = in[begin];
      in[begin] = in[end];
      in[end] = temp;
      --end;
    }

    return new String(in);
  }

  public static void print(Object... args) {
    if (logSwitch < 4) {
      StringBuilder builder = new StringBuilder();
      Object[] var2 = args;
      int var3 = args.length;

      for(int var4 = 0; var4 < var3; ++var4) {
        Object s = var2[var4];
        builder.append(s);
      }

      line.getAndAdd(1);
      PrintStream var10000 = System.out;
      String var10001 = Thread.currentThread().getName();
      var10000.println("\u001b[33m\t> [   auto-build ] : [" + var10001 + "] line: " + line.get() + "\u001b[0m");
      System.out.println("\u001b[36m\t  |- " + builder.toString() + "\u001b[0m");
    }

  }

  public static void infoPrint(Object... args) {
    if (logSwitch < 3) {
      StringBuilder builder = new StringBuilder();
      Object[] var2 = args;
      int var3 = args.length;

      for(int var4 = 0; var4 < var3; ++var4) {
        Object s = var2[var4];
        builder.append(s);
      }

      line.getAndAdd(1);
      PrintStream var10000 = System.out;
      String var10001 = Thread.currentThread().getName();
      var10000.println("\u001b[33m\t> [ auto-build ] : [" + var10001 + "] line: " + line.get() + "\u001b[0m");
      System.out.println("\u001b[36m\t  |- " + builder.toString() + "\u001b[0m");
    }

  }

  public static void debugPrint(Object... args) {
    if (logSwitch < 2) {
      StringBuilder builder = new StringBuilder();
      Object[] var2 = args;
      int var3 = args.length;

      for(int var4 = 0; var4 < var3; ++var4) {
        Object s = var2[var4];
        builder.append(s);
      }

      line.getAndAdd(1);
      PrintStream var10000 = System.out;
      String var10001 = Thread.currentThread().getName();
      var10000.println("\u001b[33m\t> [ auto-build ] : [" + var10001 + "] line: " + line.get() + "\u001b[0m");
      System.out.println("\u001b[36m\t  |- " + builder.toString() + "\u001b[0m");
    }

  }

  public static void errorPrint(Object... args) {
    if (logSwitch > 0) {
      StringBuilder builder = new StringBuilder();
      Object[] var2 = args;
      int var3 = args.length;

      for(int var4 = 0; var4 < var3; ++var4) {
        Object s = var2[var4];
        builder.append(s);
      }

      line.getAndAdd(1);
      PrintStream var10000 = System.out;
      String var10001 = Thread.currentThread().getName();
      var10000.println("\u001b[31m\t> [ auto-build ] : [" + var10001 + "] line: " + line.get() + "\u001b[0m");
      System.out.println("\u001b[31m\t  |- " + builder.toString() + "\u001b[0m");
    }

  }

  public static void tipPrint(Object... args) {
    StringBuilder builder = new StringBuilder();
    Object[] var2 = args;
    int var3 = args.length;

    for(int var4 = 0; var4 < var3; ++var4) {
      Object s = var2[var4];
      builder.append(s);
    }

    line.getAndAdd(1);
    PrintStream var10000 = System.out;
    String var10001 = Thread.currentThread().getName();
    var10000.println("\u001b[37m\t> [  auto-build ] : [" + var10001 + "] line: " + line.get() + "\u001b[0m");
    System.out.println("\u001b[37m\t  |- - - - " + builder.toString() + " - - - -|\u001b[0m");
  }

  public static void setLoggerLevel() {
    java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Gradle.class.getName());
    logger.setLevel(Level.ALL);
    Handler handler = new ConsoleHandler();
    handler.setLevel(Level.ALL);
    logger.addHandler(handler);
    logger.setUseParentHandlers(false);
    logSwitch = 4;
  }

  public static void createFile(String origin) {
    Path path = Paths.get(origin);
    StringBuilder subPather = (new StringBuilder()).append("/");
    path.getParent().forEach((p) -> {
      String curPath = subPather.append(p).append("/").toString();

      try {
        if (Files.notExists(Paths.get(curPath), new LinkOption[0])) {
          Files.createDirectory(Paths.get(curPath));
        }
      } catch (Exception var4) {
        var4.printStackTrace();
      }

    });
    if (Files.notExists(path, new LinkOption[0])) {
      try {
        if (Files.notExists(path, new LinkOption[0])) {
          Files.createFile(path);
        }
      } catch (Exception var4) {
        var4.printStackTrace();
      }
    }

  }

  public static void textToFile(String path, String line) {
    File file = new File(path);

    try {
      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(line);
      bw.close();
    } catch (IOException var5) {
      var5.printStackTrace();
      System.exit(-1);
    }

  }


  public static void runShell(String cmd, Consumer<String> callBack) {
    boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("windows");
    debugPrint("判断操作系统" + isWin);
    ProcessBuilder builder = new ProcessBuilder(new String[0]);
    if (isWin) {
      builder.command("cmd.exe", "/c", cmd);
    } else {
      builder.command("sh", "-c", cmd);
    }

    builder.directory();

    try {
      Process process = builder.start();
      Utils.CommandStream cs = new Utils.CommandStream(process.getInputStream(), callBack);
      Executors.newSingleThreadExecutor().submit(cs);
    } catch (Exception var6) {
      errorPrint(" Error : shell process execute error ", var6.getStackTrace(), " msg : ", var6.getMessage());
    }

  }

  public static void currentProcessRunShell(String cmd, Consumer<String> callBack) {
    try {
      Process execute = ProcessGroovyMethods.execute(cmd);
      String b = ProcessGroovyMethods.getText(execute).replace("\r", "").replace("\n", "");
      tipPrint("==", b, "==");
      callBack.accept(b);
    } catch (Exception var4) {
      errorPrint("Error : shell current process execute error ", var4.getMessage());
    }

  }

  public static boolean matchGitBranchUsingSuffixRule(String targetBranch) {
    try {
      Process execute = ProcessGroovyMethods.execute("git rev-parse --abbrev-ref HEAD");
      String b = ProcessGroovyMethods.getText(execute).replace("\r", "").replace("\n", "");
      debugPrint(" 执行脚本 : ", "git rev-parse --abbrev-ref HEAD");
      debugPrint(" 比较当前分支 : ", b, " = 渠道设置分支 : ", targetBranch);
      return b.endsWith(targetBranch);
    } catch (Exception var3) {
      errorPrint("Error : shell current process execute error ", var3.getMessage());
      return false;
    }
  }


  private static class CommandStream implements Runnable {
    private InputStream inputStream;
    private Consumer<String> consumer;

    public CommandStream(InputStream inputStream, Consumer<String> consumer) {
      this.inputStream = inputStream;
      this.consumer = consumer;
    }

    public void run() {
      (new BufferedReader(new InputStreamReader(this.inputStream))).lines().forEach(this.consumer);
    }
  }
}
