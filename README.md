# defaultapplication

Przykładowa Aplikacja Web "Hello World". Aplikacja pozwala na weryfikację nazwy zalogowanego użytkownika - jest ona prezentowana na stronie głównej. 

Dostęp do uruchomionej aplikacji:

```text
[http|https]://<server_name>:<port>/default/
```
Przykład: https://bpmbaw21test.hgdb.org:9443/default/

## Wykorzystanie w konfiguracji adaptera OIDRCP

Strona `/default/pages/index.jsp` może być wykorzystana w konfiguracji adaptera OIDRCP (interseptora) do weryfikacji nazwy gdy jest ustawione SSO z tokenem Ltpa. 

Zobacz projekt [websphere-oidcrp-adapter🔒](https://github.com/SciSoftwareSlawomirCichy/websphere-oidcrp-adapter). Poniżej przykład kodu adaptera, w którym referencja `this.chcekUserNameByLtpaTokenURL` przechowuje pełną ścieżkę do strony składowanej w aplikacji:

```java
String ltpaCookie = RelyingPartyUtils.getCookieValue(req, "LtpaToken2");
if (ltpaCookie != null && this.chcekUserNameByLtpaTokenURL != null) {
	try {
		HashMap<String, String> retVal = RelyingPartyUtils.invokeRequest("GET",
				this.chcekUserNameByLtpaTokenURL, (String) null, (RelyingPartyConfig) null,
				"LtpaToken2=" + ltpaCookie);
		String responseCode = retVal.get("responseCode");
		String responseMsg = retVal.get("responseMsg");
		if ("200".equals(responseCode)) {
			String context = req.getContextPath();
			String clientIP = req.getRemoteAddr();
			trcLogger.logp(Level.INFO, DISPLAY_CLASSNAME, METHOD_NAME,
					String.format("--> Client IP: %s; Context: %s; Logged by SSO LtpaToken2: %s", clientIP,
							context, responseMsg));
			return TAIResult.create(200, responseMsg);
		}
	} catch (RelyingPartyException e) {
		String errorMsg = String.format(EXCEPTION_S, e.getMessage());
		trcLogger.logp(Level.WARNING, DISPLAY_CLASSNAME, METHOD_NAME, errorMsg);
	}
}
```

## Wykorzystanie jako serwer plików

Aplikacja ma zaimplementowany mechanizm serwowania plików statycznych. Domyślnie pliki składowane są w katalogu głównym o nazwie `/opt/workspace/static-files/` co odpowiada kontekstowi aplikacji `/default/files/downloadFile`. Można robić podkatalogi, ale **tylko jednego poziomu zagnieżdżenia** według zasady: `<lokalizacja_katalogu_głownego>/<podkatalog>`. Przykładowo dla domyślnej lokalizacji katalogu gółwnego i podkatalogu o nazwie `officeItems` pliki składowane będą w lokalizacji `/opt/workspace/static-files/officeItems` co odpowiada kontekstowi aplikacji: `/default/files/downloadFile/officeItems`.

> [!Note]
> Lokalizację katalogu głównego można zmienić za pomocą parametru JVM `sci.static.file.dir` uruchamianej maszyny wirtulanej np. `-Dsci.static.file.dir=/opt/workspace/custom-dir`.

> [!Important]
> Dostep do plików po http (https) nie jest chroniony mechanizmami uwierzytelniającymi.
 
