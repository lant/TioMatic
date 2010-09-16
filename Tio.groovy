import org.apache.commons.mail.*;

@Grab(group='org.apache.commons', module='commons-email', version='1.2')
public class Tio {
	
	private String username
	private String password

	public void sendEmail() throws EmailException {
		Email email = new SimpleEmail();
		email.setSmtpPort(587);
		email.setAuthenticator(new DefaultAuthenticator(username, password));
		email.setDebug(true);
		email.setHostName("smtp.gmail.com");
		email.setFrom("phlegias@gmail.com");
		email.setSubject("TestMail");
		email.setMsg("This is a test mail ... :-)");
		email.addTo("marc.de.palol@gmail.com");
		email.setTLS(true);
		email.send();
	}

	static void main(String ...args) {
		Tio t = new Tio()
	}
}
