package org.jooq;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.jetbrains.annotations.Contract;

/**
 * Simple version of the JSR 305 annotation that allows for inspecting jOOQ code
 * and detect accidentally omitted calls to {@link Query#execute()} and the
 * likes in IntelliJ.
 * <p>
 * This annotation is {@link Internal}. Clients should not rely on its presence.
 * The annotation may be replaced with a more suitable one by JetBrains, e.g.
 * the {@link Contract} annotation, when a favourable use-case can be found.
 *
 * @author Lukas Eder
 * @see <a href=
 *      "https://github.com/jOOQ/jOOQ/issues/11718">https://github.com/jOOQ/jOOQ/issues/11718</a>
 * @see <a href=
 *      "https://youtrack.jetbrains.com/issue/IDEA-265263">https://youtrack.jetbrains.com/issue/IDEA-265263</a>
 */
@Documented
@Target(METHOD)
@Retention(CLASS)
@Internal
public @interface CheckReturnValue {
}
