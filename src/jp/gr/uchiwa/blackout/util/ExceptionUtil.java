/**
 * 
 */
package jp.gr.uchiwa.blackout.util;


/**
 * @author jabaraster
 * 
 */
public final class ExceptionUtil {

    private ExceptionUtil() {
        // 処理なし.
    }

    /**
     * @param pCause
     * @return pCauseがnullの場合はnullを返すが、それ以外の場合は決して値を返すことはない(何らかの例外がスローされる).
     */
    public static RuntimeException rethrow(final Throwable pCause) {
        if (pCause == null) {
            return null;
        }
        if (pCause instanceof RuntimeException) {
            throw (RuntimeException) pCause;
        }
        if (pCause instanceof Error) {
            throw (Error) pCause;
        }
        throw new IllegalStateException(pCause);
    }
}
