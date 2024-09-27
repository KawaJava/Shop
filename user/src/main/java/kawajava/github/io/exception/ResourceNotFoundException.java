package kawajava.github.io.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName) {
        super("Podany użytkownik o nazwie/mailu:  " + resourceName + " nie istnieje");
    }
}
