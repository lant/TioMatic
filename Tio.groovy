import org.apache.commons.mail.*;

@Grab(group='org.apache.commons', module='commons-email', version='1.2')
public class Tio {
	
	private String username
	private String password

	def debug
	def esqueletTexte

	def participants = [:]

	def llegeixTemplate(String fitxerTemplate) {
		esqueletTexte = new File(fitxerTemplate).getText()	
	}

	def configura(String fitxerConfiguracio) {
		File conf = new File(fitxerConfiguracio)
		conf.eachLine() {
			line ->
			if (!line.startsWith("#")) {
				def parts = line.split("=")

				if (parts[0].matches("username")) {
					username = parts[1]		
				}
				if (parts[0].matches("password")) {
					password = parts[1]		
				}

			}
		}
	}

	def agafaLlistaParticipants(String fitxerInfoParticipants) {
		File parti = new File(fitxerInfoParticipants) 
		parti.eachLine() {
			line ->
			if (!line.startsWith("#")) {
				def parts = line.split("\t")
				if (parts.length != 2 ) {
					println "Error llegint l'entrada: ${line}"
					System.exit(-1)
				}			
				participants[parts[0]] = parts[1]
			}	
		}
		println "Tenim: ${participants}"	
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
		println "Enviat e-mail a ${recipient}"
	}

	def ferSorteig() {
		def regalats = participants.keySet().asList()
		def regala = participants.keySet().asList()
		def resultat = [:]
	
		regala.each() {
			regalador ->
			while (regalats.get(0).matches(regalador)) {
				Collections.shuffle(regalats, new Random())
			}
			resultat[regalador] = regalats.get(0)
			regalats.remove(0)
		}
		
		// unes comprovacions per veure queno l'hem liat.
		assert(regalats.isEmpty())	
		assert(resultat.keySet().containsAll(participants.keySet()))
		assert(resultat.keySet().size() == participants.keySet().size())
		assert(resultat.values().containsAll(participants.keySet()))
		assert(resultat.values().size() == participants.keySet().size())

		/* UNCOMMENT TO VISUALIZE	RESULTS
		def kset = resultat.keySet() 
		kset.each() {
			entry ->
			println "${entry} -> ${resultat[entry]}"
		}
		*/

		resultat
	}

	def run() {
		// agafar i enllaçar participants + comprovació
		def mapaResultat = ferSorteig()
	
		def iterador = mapaResultat.entrySet() 
		// per cada participant
		iterador.each() {
			it ->
			// crear texte
			def texte = crearTexte(it.key, it.value)
			// enviar e-mail	
			sendEmail(username, password, participants[it.key] , texte) 
		}
	}

	def crearTexte(def regalador, def regalat) {
		esqueletTexte.replaceAll("nom1", regalador).replaceAll("nom2",regalat)		
	}

	static void main(String ...args) {
		if (args.length != 3) {
			println "El programa necessita 3 paràmeters"
			println "<Fitxer de configuracio>"
			println "<Fitxer amb participants>"	
			println "<Fitxer amb el template de l'email>"	
			System.exit(-1)
		}

		Tio t = new Tio()
		// fitxer de configuracio
		t.configura(args[0])
		// fitxer amb la llista
		t.agafaLlistaParticipants(args[1])
		// template del mail
		t.llegeixTemplate(args[2])
		// debug
		t.run()
	}
}
