package ist.meic.pa.FunctionalProfiler;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class ProfilerTranslator implements Translator {
    private final String writerTemplate = "ist.meic.pa.FunctionalProfiler.Profiler.incrementWriter(\"%s\");" +
                                          "$proceed($$);";

    private final String constructorWriterTemplate = "if(this != $0)" +
                                                     "ist.meic.pa.FunctionalProfiler.Profiler.incrementWriter(\"%s\");" +
                                                     "$proceed($$);";

    private final String readerTemplate = "ist.meic.pa.FunctionalProfiler.Profiler.incrementReader(\"%s\");" +
                                          "$_ = $proceed();";

    private ExprEditor methodEditor = new ExprEditor() {
            public void edit(FieldAccess fa) throws CannotCompileException {
                String className = fa.getClassName();
                if (!className.equals("java.lang.System") &&
                    !className.equals("ist.meic.pa.FunctionalProfiler.Profiler")) {
                    if (fa.isWriter()) {
                        fa.replace(String.format(writerTemplate, className));
                    } else if (fa.isReader()) {
                        fa.replace(String.format(readerTemplate, className));
                    }
                }
            }
        };

    private ExprEditor constructorMethod = new ExprEditor() {
            public void edit(FieldAccess fa) throws CannotCompileException {
                String className = fa.getClassName();

                //boolean isInitialization = className.equals(fa.where().getDeclaringClass().getName());
                if (!className.equals("java.lang.System") &&
                    !className.equals("ist.meic.pa.FunctionalProfiler.Profiler")) {
                    if (fa.isWriter()) {
                        fa.replace(String.format(constructorWriterTemplate, className));
                    } else if (fa.isReader()) {
                        fa.replace(String.format(readerTemplate , className));
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
            ctMethod.instrument(methodEditor);

        for (CtConstructor ctConstructor: ctClass.getDeclaredConstructors())
            ctConstructor.instrument(constructorMethod);
    }
}
