package ist.meic.pa.FunctionalProfilerExtended;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface LimitScope {}
