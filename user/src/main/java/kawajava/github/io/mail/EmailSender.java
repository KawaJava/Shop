package kawajava.github.io.mail;

public interface EmailSender {
    void send(String to, String subject, String msg);
}
