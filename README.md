# defaultapplication

Przykładowa Aplikacja Web "Hello World". 

Aplikacja pozwala na weryfikację nazwy zalogowanego użytkownika - jest ona prezentowana na stronie głównej. 

Strona `/default/pages/index.jsp` może być wykorzystana w konfiguracji adaptera OIDRCP (interseptora) do weryfikacji 
nazwy gdy jest ustawione SSO z tokenem Ltpa. 
Poniżej przykład kodu adaptera, w którym referencja `this.chcekUserNameByLtpaTokenURL` przechowuje pełną ścieżkę do tej strony:


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


