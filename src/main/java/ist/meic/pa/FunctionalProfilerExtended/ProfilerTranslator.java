package ist.meic.pa.FunctionalProfilerExtended;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class ProfilerTranslator implements Translator {
    private final String writerTemplate = "ist.meic.pa.FunctionalProfilerExtended.Profiler.incrementWriter(\"%s\",\"%s\");" +
                                          "$proceed($$);";

    private final String constructorWriterTemplate = "if(this != $0)" +
                                                     "ist.meic.pa.FunctionalProfilerExtended.Profiler.incrementWriter(\"%s\",\"%s\");" +
                                                     "$proceed($$);";

    private final String readerTemplate = "ist.meic.pa.FunctionalProfilerExtended.Profiler.incrementReader(\"%s\",\"%s\");" +
                                          "$_ = $proceed();";

    private ExprEditor methodEditor = new ExprEditor() {
                public void edit(FieldAccess fa) throws CannotCompileException {
                    String className = fa.getClassName();
                    String methodName = fa.where().getLongName();
                    try {
                        if (fa.where().getAnnotation(LimitScope.class) != null) {
                            System.out.println(">>> Skipping method (LimitScope): " + fa.where().getName());
                            return; // Don't replace if method has annotation
                        }
                    } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                    }
                    if (!className.equals("java.lang.System") &&
                        !className.equals("ist.meic.pa.FunctionalProfilerExtended.Profiler")&&
                        !className.equals("ist.meic.pa.FunctionalProfilerExtended.ProfilerData")) {
                        if (fa.isWriter()) {
                            fa.replace(String.format(writerTemplate, className, methodName));
                        } else if (fa.isReader()) {
                            fa.replace(String.format(readerTemplate, className, methodName));
                        }
                    }
                }
        };

    private ExprEditor constructorEditor = new ExprEditor() {
            public void edit(FieldAccess fa) throws CannotCompileException {
                String className = fa.getClassName();
                String methodName = fa.where().getLongName();
                try {
                    if (fa.where().getAnnotation(LimitScope.class) != null) {
                        System.out.println(">>> Skipping constructor (LimitScope): " + fa.where().getName());
                        return; // Don't replace if constructor has annotation
                    }
                } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                }

                if (!className.equals("java.lang.System") &&
                    !className.equals("ist.meic.pa.FunctionalProfilerExtended.Profiler") &&
                    !className.equals("ist.meic.pa.FunctionalProfilerExtended.ProfilerData")) {

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
            ctMethod.instrument(methodEditor);

        for (CtConstructor ctConstructor: ctClass.getDeclaredConstructors())
            ctConstructor.instrument(constructorEditor);
    }
}
