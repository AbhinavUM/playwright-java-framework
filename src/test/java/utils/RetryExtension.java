package utils;

import org.junit.jupiter.api.extension.*;

public class RetryExtension implements TestExecutionExceptionHandler {

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {

        Retry retry = context.getRequiredTestMethod().getAnnotation(Retry.class);

        if (retry == null) {
            throw throwable;
        }

        int maxRetries = retry.value();
        int currentRetry = getRetryCount(context);

        if (currentRetry < maxRetries) {
            System.out.println("🔁 Retrying test... Attempt " + (currentRetry + 1));

            incrementRetryCount(context);

            // Re-run test
            context.getRequiredTestMethod().invoke(context.getRequiredTestInstance());
        } else {
            throw throwable;
        }
    }

    private int getRetryCount(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.GLOBAL)
                .getOrDefault(context.getUniqueId(), Integer.class, 0);
    }

    private void incrementRetryCount(ExtensionContext context) {
        int current = getRetryCount(context);
        context.getStore(ExtensionContext.Namespace.GLOBAL)
                .put(context.getUniqueId(), current + 1);
    }
}