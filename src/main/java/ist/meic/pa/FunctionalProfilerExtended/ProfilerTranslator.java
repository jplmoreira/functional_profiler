package ist.meic.pa.FunctionalProfilerExtended;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class ProfilerTranslator implements Translator {
    private final String writerTemplate = "ist.meic.pa.FunctionalProfilerExtended.Profiler.incrementWriter(\"%s\",\"%s\");" + // Increment the counter for the writes of the class
                                          "$proceed($$);";                                                                    // Proceed with the field write

    private final String constructorWriterTemplate = "if(this != $0)" +                                                                    // Ignore if the field access is an initialization
                                                     "  ist.meic.pa.FunctionalProfilerExtended.Profiler.incrementWriter(\"%s\",\"%s\");" + // Increment the counter for the writes of the class
                                                     "$proceed($$);";                                                                      // Proceed with the field write

    private final String readerTemplate = "ist.meic.pa.FunctionalProfilerExtended.Profiler.incrementReader(\"%s\",\"%s\");" + // Increment the counter for the reads of the class
                                          "$_ = $proceed();";                                                                 // Proceed with the field read

    private ExprEditor methodEditor = new ExprEditor() {
            public void edit(FieldAccess fa) throws CannotCompileException {
                if (!fa.isStatic()) {                                                     // If the field access is not static
                    String className = fa.getClassName();
                    String methodName = fa.where().getLongName();
                    try {
                        if (fa.where().getAnnotation(LimitScope.class) != null) {
                            return;                                             // Don't replace if method has annotation
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    if (fa.isWriter()) {                                                  // And if the field access is writer
                        fa.replace(String.format(writerTemplate, className, methodName)); // Replace the field access with the writer template
                    } else if (fa.isReader()) {                                           // Or if the field access is reader
                        fa.replace(String.format(readerTemplate, className, methodName)); // Replace the field access with the reader template
                    }
                        
                }
            }
        };

    private ExprEditor constructorEditor = new ExprEditor() {
            public void edit(FieldAccess fa) throws CannotCompileException {
                if (!fa.isStatic()) {                                                                // If the field access is not static
                    String className = fa.getClassName();
                    String methodName = fa.where().getLongName();
                    try {
                        if (fa.where().getAnnotation(LimitScope.class) != null) {
                            return;                                             // Don't replace if method has annotation
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    if (fa.isWriter()) {                                                             // And if the field access is writer
                        fa.replace(String.format(constructorWriterTemplate, className, methodName)); // Replace the field access with the writer template for constructors
                    } else if (fa.isReader()) {                                                      // Or if the field access is reader
                        fa.replace(String.format(readerTemplate , className, methodName));           // Replace the field access with the reader template
                    }
                }
            }
        };

    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {
        // Do nothing
    }

    public void onLoad(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
        CtClass ctClass = pool.get(className);                                  // Get the compile time class
        profileClass(ctClass);                                                  // Initiate the class profiling
    }

    private void profileClass(CtClass ctClass) throws CannotCompileException {
        for (CtMethod ctMethod: ctClass.getDeclaredMethods())
            ctMethod.instrument(methodEditor);                                  // Instrument the methods of the class

        for (CtConstructor ctConstructor: ctClass.getDeclaredConstructors())
            ctConstructor.instrument(constructorEditor);                        // Instrument the constructors of the class
    }
}
