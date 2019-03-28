/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ist.meic.pa.FunctionalProfiler;

import javassist.*;

import java.lang.reflect.InvocationTargetException;

public class WithFunctionalProfiler {
    
    public static void main(String[] args) {
        try {
            if(args.length < 1){
                System.err.println("Usage: java WithFunctionalProfiler <Class> <Arg1 ... Argn>");
                System.exit(1);
            }

            Translator translator = new ProfilerTranslator();
            ClassPool pool = ClassPool.getDefault();
            Loader classLoader = new Loader();
            classLoader.delegateLoadingOf("ist.meic.pa.FunctionalProfiler.Profiler");
            classLoader.addTranslator(pool, translator);
            String[] restArgs = new String[args.length - 1];
            System.arraycopy(args, 1, restArgs, 0, restArgs.length);
            classLoader.run(args[0], restArgs);
            ist.meic.pa.FunctionalProfiler.Profiler.printStatus();
        }
        catch (NotFoundException | CannotCompileException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            System.out.println("Error running main function in " + args[0]);
            e.printStackTrace();
        }
    }
}
