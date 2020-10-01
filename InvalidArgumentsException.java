public class InvalidArgumentsException extends Exception  {
    public InvalidArgumentsException(String invalidArg, String randomValue)
    {
        super(invalidArg + randomValue);
    }
    public InvalidArgumentsException(String invalidArg, String randomValue, String randomValue2)
    {
        super(invalidArg + randomValue + " " + randomValue2);
    }
    public InvalidArgumentsException(String message)
    {
        super(message);
    }
}
