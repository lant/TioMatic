import org.apache.commons.mail.*;

@Grab(group='org.apache.commons', module='commons-email', version='1.2')
public class Tio {
	
	private String username
	private String password

	def debug
	def esqueletTexte

	def configura(String fitxerConfiguracio) {

	}

	def agafaLlistaParticipatns(String fitxerInfoParticipants) {
	
	}

	def sendEmail(def username, def password, def recipient, def texte) throws EmailException {
		Email email = new SimpleEmail();
		email.setSmtpPort(587);
		email.setAuthenticator(new DefaultAuthenticator(username, password));
		email.setDebug(false);
		email.setHostName("smtp.gmail.com");
		email.setFrom("phlegias@gmail.com");
		email.setSubject("[Tio] t'ha tocat...");
		email.setMsg(texte)
		email.addTo(recipient)
		email.setTLS(true);
		email.send();
	}

	def run() {
		// agafar i enllaçar participants + comprovació

		// per cada participant
			// crear texte
			// enviar e-mail	
	}

	static void main(String ...args) {
		Tio t = new Tio()
		// fitxer de configuracio
		// fitxer amb la llista
		// template del mail
		// debug
		t.run()
	}
}
