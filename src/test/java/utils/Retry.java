package utils;

import org.junit.jupiter.api.extension.ExtendWith;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(RetryExtension.class)
public @interface Retry {
    int value() default 2; // number of retries
}