package pers.yujie.dashboard.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.annotation.Value;

/**
 * This annotation is responsible for ensuring the controllers do not receive deceptive or malicious
 * requests. Note that the actual handling is in {@link pers.yujie.dashboard.config.RequestManagerConfig}.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @since 16/02/2023
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Encrypted {

}
