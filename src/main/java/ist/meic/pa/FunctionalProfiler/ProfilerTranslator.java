package ist.meic.pa.FunctionalProfiler;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.CtConstructor;
import javassist.Translator;
import javassist.ClassPool;
import javassist.CtMethod;
import javassist.CtClass;
import javassist.expr.FieldAccess;
import javassist.expr.ExprEditor;

public class ProfilerTranslator implements Translator {
    private final String writerTemplate = "ist.meic.pa.FunctionalProfiler.ProfilerMaps.incrementWriter(\"%s\");" +
                                          "$proceed($$);";

    private final String readerTemplate = "ist.meic.pa.FunctionalProfiler.ProfilerMaps.incrementReader(\"%s\");" +
                                          "$_ = $proceed();";

    private ExprEditor methodEditor = new ExprEditor() {
            public void edit(FieldAccess fa) throws CannotCompileException {
                String className = fa.getClassName();
                if (!className.equals("java.lang.System") &&
                    !className.equals("ist.meic.pa.FunctionalProfiler.ProfilerMaps")) {
                    if (fa.isWriter()) {
                        fa.replace(String.format(writerTemplate, className));
                    } else if (fa.isReader()) {
                        fa.replace(String.format(readerTemplate, className));
                    }
                }
            }
        };

    private ExprEditor constEditor = new ExprEditor() {
            public void edit(FieldAccess fa) throws CannotCompileException {
                String className = fa.getClassName();
                boolean initialization = className.equals(fa.where().getDeclaringClass().getName());
                if (!className.equals("java.lang.System") &&
                    !className.equals("ist.meic.pa.FunctionalProfiler.ProfilerMaps")) {
                    if (fa.isWriter() && !initialization) {
                        fa.replace(String.format(writerTemplate, className));
                    } else if (fa.isReader()) {
                        fa.replace(String.format(readerTemplate, className));
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
            ctConstructor.instrument(constEditor);
    }
}
