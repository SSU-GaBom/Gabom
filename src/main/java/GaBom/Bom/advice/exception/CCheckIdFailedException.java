package GaBom.Bom.advice.exception;

public class CCheckIdFailedException extends RuntimeException{

    public CCheckIdFailedException(String msg, Throwable t) {
        super(msg, t);
    }

    public CCheckIdFailedException(String msg) {
        super(msg);
    }
    
    public CCheckIdFailedException(){
        super();
    }

}
