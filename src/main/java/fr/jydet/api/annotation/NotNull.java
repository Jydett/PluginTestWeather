package fr.jydet.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * On a method : indicate that the return type cannot be null
 * <p>On a parameter : indicate that the method cannot handle null parameter
 *
 * <p>This annotation can be linked to Intellij to hint method's constract violation,
 * go to Settings (Ctrl+Alt+S) > Editor > Inspections > Java > Probable bugs > Nullability problems and
 * select @NotNull/@Nullable problems. Make sure the inspections is enabled, click on the 'Configure Annotations'
 * buttons and navigate to find this class.
 *
 * @see Nullable
 * @author Jydet
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface NotNull {
}
