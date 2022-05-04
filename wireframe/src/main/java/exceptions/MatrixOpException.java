package exceptions;

public class MatrixOpException extends Exception{

        public MatrixOpException() {
            super();
        }

        public MatrixOpException(String message) {
            super(message);
        }

        public MatrixOpException(String message, Throwable cause) {
            super(message, cause);
        }

        public MatrixOpException(Throwable cause) {
            super(cause);
        }

        protected MatrixOpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }


}

