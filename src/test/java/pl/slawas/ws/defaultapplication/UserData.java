package pl.slawas.ws.defaultapplication;

import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.login.CredentialExpiredException;

import com.ibm.websphere.security.WSSecurityException;
import com.ibm.websphere.security.auth.WSSubject;
import com.ibm.websphere.security.cred.WSCredential;

public class UserData {

	public static String getCredential() {
		try {
			Subject subject = WSSubject.getRunAsSubject();
			Set<WSCredential> creds = subject.getPublicCredentials(WSCredential.class);
			for (WSCredential cred : creds) {
				System.out.println(String.format("-->WSCredential: " + "\n\t->RealmName: %s" + "\n\t->SecurityName: %s",
						cred.getRealmName(), cred.getSecurityName()));
			}
			return WSSubject.getCallerPrincipal();
		} catch (WSSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "n/a";
		} catch (CredentialExpiredException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "n/a";
		}
	}

}
