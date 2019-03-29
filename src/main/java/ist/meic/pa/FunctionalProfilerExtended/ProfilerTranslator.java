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

    /*******************************************************
     * Adds code to the field accesses on a method, which increments
     * the counter of reads or writes of a class, depending on the type
     * of the field access. Ignores the method if it has the LimitScope annotation. 
     *******************************************************/
    private ExprEditor methodEditor = new ExprEditor() {
            public void edit(FieldAccess fa) throws CannotCompileException {
                if (!fa.isStatic()) {
                    String className = fa.getClassName();
                    String methodName = fa.where().getLongName();
                    try {
                        if (fa.where().getAnnotation(LimitScope.class) != null) {
                            return;
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    if (fa.isWriter()) {
                        fa.replace(String.format(writerTemplate, className, methodName));
                    } else if (fa.isReader()) {
                        fa.replace(String.format(readerTemplate, className, methodName));
                    }
                        
                }
            }
        };

    /*******************************************************
     * Adds code to the field accesses on a constructor, which increments
     * the counter of reads or writes of a class, depending on the type
     * of the field access. But it has to take into consideration if the
     * field access an initialization. Ignores the constructor if it has
     * the LimitScope annotation.
     *******************************************************/
    private ExprEditor constructorEditor = new ExprEditor() {
            public void edit(FieldAccess fa) throws CannotCompileException {
                if (!fa.isStatic()) {
                    String className = fa.getClassName();
                    String methodName = fa.where().getLongName();
                    try {
                        if (fa.where().getAnnotation(LimitScope.class) != null) {
                            return;
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    if (fa.isWriter()) {
                        fa.replace(String.format(constructorWriterTemplate, className, methodName));
                    } else if (fa.isReader()) {
                        fa.replace(String.format(readerTemplate , className, methodName));
                    }
                }
            }
        };

    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {
        // Do nothing
    }

    public void onLoad(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
        CtClass ctClass = pool.get(className);
        profileClass(ctClass);
    }

    private void profileClass(CtClass ctClass) throws CannotCompileException {
        for (CtMethod ctMethod: ctClass.getDeclaredMethods())
            ctMethod.instrument(methodEditor);                                  // Instrument the methods of the class

        for (CtConstructor ctConstructor: ctClass.getDeclaredConstructors())
            ctConstructor.instrument(constructorEditor);                        // Instrument the constructors of the class
    }
}
