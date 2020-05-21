package org.apollo.game.fs.decoder;

/**
 * An exception thrown when a {@link SynchronousDecoder} fails to complete successfully.
 *
 * @author garyttierney
 */
public class SynchronousDecoderException extends Exception {
    public SynchronousDecoderException(String message) {
        super(message);
    }

    public SynchronousDecoderException(String message, Throwable cause) {
        super(message, cause);
    }
}
