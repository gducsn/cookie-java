![output](https://user-images.githubusercontent.com/94108883/175052018-0b1b64ca-6981-4a54-855b-77d820b4bcd0.gif)

# Cookies

Web app per creare e gestire cookie tramite JSP e servlet. All’interno ci 
sono due classi e due pagine JSP. 

La prima classe contiene i metodi per creare e successivamente recuperare 
il cookie. Le due pagine JSP ci mostrano prima un form in cui inserire il 
valore del cookie e successivamente ce lo mostra.

Se non ci sono cookie mostra un messaggio di avvertimento.

---

Cosa sono i cookie?

> An **HTTP cookie** (web cookie, browser cookie) is a small piece of data 
that a server sends to a user's web browser. The browser may store the 
cookie and send it back to the same server with later requests. Typically, 
an HTTP cookie is used to tell if two requests come from the same 
browser—keeping a user logged in, for example. It remembers stateful 
information for the 
[stateless](https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview#http_is_stateless_but_not_sessionless) 
HTTP protocol.

Un 
[cookie](https://docs.oracle.com/javaee/7/api/javax/servlet/http/Cookie.html) 
è una piccola quantità di informazioni inviate da un servlet a un browser 
Web, salvate dal browser e successivamente rispedite al server. Il valore 
di un cookie può identificare in modo univoco un client, quindi i cookie 
sono comunemente usati per la gestione della sessione.

Un cookie ha un nome, un unico valore e attributi facoltativi come un 
commento, qualificatori di percorso e dominio, un'età massima e un numero 
di versione.

Il servlet invia i cookie al browser utilizzando il metodo 
`response.addCookie(cookie)`, che aggiunge campi alle intestazioni delle 
risposte HTTP per inviare i cookie al browser, uno alla volta. Il browser 
dovrebbe supportare 20 cookie per ciascun server Web, 300 cookie in totale 
e può limitare la dimensione dei cookie a 4 KB ciascuno.

Il browser restituisce i cookie al servlet aggiungendo campi alle 
intestazioni delle richieste HTTP. I cookie possono essere recuperati da 
una richiesta utilizzando il metodo `request.getCookies()`. Diversi cookie 
potrebbero avere lo stesso nome ma attributi di percorso diversi.

Struttura:

- CookieUtils.java
- CookieServlet.java
- Create.jsp
- Result.jsp
- Error.jsp

---

Cookie Utils 

```java
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

public static void addCookie(HttpServletResponse httpServletResponse, int 
cookieExpiration, String cookieKey,
			String cookieValue) {
		Cookie cookie = new Cookie(cookieKey, cookieValue);
		cookie.setPath("/");
		cookie.setMaxAge(cookieExpiration);
		httpServletResponse.addCookie(cookie);
	}

	public static Cookie getCookie(HttpServletRequest 
httpServletRequest, String cookieKey) {
		Cookie[] cookies = httpServletRequest.getCookies();
		if (cookies == null) {
			return null;
		}

		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookie.getName().equals(cookieKey)) {
				return cookie;
			}
		}

		return null;
	}

}
```

Per un futuro utilizzo ho separato i metodi di creazione e recupero cookie 
in un’altra classe. I metodi sono publici e statici così da recuperali 
dove servono senza istanziare ogni volta la classe.

Creazione cookie:

```java
public static void addCookie(HttpServletResponse httpServletResponse, int 
cookieExpiration, String cookieKey,
			String cookieValue) {
		Cookie cookie = new Cookie(cookieKey, cookieValue);
		cookie.setPath("/");
		cookie.setMaxAge(cookieExpiration);
		httpServletResponse.addCookie(cookie);
	}
```

Il metodo .addCookie prende come argomenti la response della chiamata 
HTTP, il numero che specifica la vita del cookie, la chiave e il valore.

Creiamo un nuovo cookie con il suo costruttore e passiamo la chiave e il 
valore. Definiamo il path in cui è visibile il cookie e decidiamo sia 
visibile in tutta l’app.

Configuriamo l’età del cookie. Nel servlet gli daremo come valore ‘60’, 
quindi dopo 1 minuto il nostro cookie si cancellerà .

Infine dobbiamo spedirlo al browser tramite il metodo .addCookie();

Recupero cookie:

```java
public static Cookie getCookie(HttpServletRequest httpServletRequest, 
String cookieKey) {
		Cookie[] cookies = httpServletRequest.getCookies();
		if (cookies == null) {
			return null;
		}

		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookie.getName().equals(cookieKey)) {
				return cookie;
			}
		}

		return null;
	}
```

Il metodo ritorna l’Array di cookie. Tra gli argomenti inseriremo il nome 
del cookie che vogliamo ritornare così da poterlo usare.

La prima cosa da fare è inserire tutti i cookies in un array. Per poterli 
recuperare utilizziamo il metodo getCookies().

Se l’array è vuoto allora ritorniamo null.

Dobbiamo iterare il nostro array per ritornare solo quello che ci 
interessa. Iteriamo l’array per tutta la sua lunghezza. Creiamo una nuova 
variabile in cui assegniamo come valore il cookie iterato ad ogni ciclo.

Se il nome del cookie corrisponde con quello che abbiamo immesso come 
argomento del metodo il ciclo si ferma e viene ritornato il cookie 
cercato.

Questi due metodi ci permetto, per l’intera applicazione, di creare e 
tornare cookies.

---

Cookie Servlet:

```java
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.CookieUtils;

@WebServlet("/insert")
public class CookieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CookieServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, 
HttpServletResponse response)
			throws ServletException, IOException {

		String uservalue = request.getParameter("user");

		CookieUtils.addCookie(response, 60, "usercookie", 
uservalue);

		response.sendRedirect(request.getContextPath() + 
"/result");

	}

	protected void doPost(HttpServletRequest request, 
HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
```

Gestiamo l’azione dell’utente nel metodo doGet.

```java
protected void doGet(HttpServletRequest request, HttpServletResponse 
response)
			throws ServletException, IOException {

		String uservalue = request.getParameter("user");

		CookieUtils.addCookie(response, 60, "usercookie", 
uservalue);

		response.sendRedirect(request.getContextPath() + 
"/result");

	}
```

Ci serve recuperare il valore immesso dall’utente nel form, lo facciamo 
utilizzando il metodo getParameter(), il quale si riferisce al valore 
dell’attributo ‘name’ scritto nel campo input del form.

In questo modo possiamo creare dinamicamente il nostro cookie ogni volta 
che l’utente clicca ‘submit’.

Prendiamo il metodo dalla classe utils, .addCookie(), inseriamo la 
risposta, quanto tempo deve vivere il nostro cookie, il nome e il suo 
valore.

Infine reindirizziamo l’utente ad un’altra pagina. Non abbiamo utilizzato 
il dispatcher e forward in questa occasione poiché il suo utilizzo non ci 
avrebbe dato il cookie nell’immediato. Per ottenerlo, infatti, 
bisognerebbe ricaricare la pagina. Con il redirect è immediato.

Questa è la logica per poter creare un cookie. Ora gestiamo il recupero 
direttamente nella pagina JSP.

---

Create JSP

```java
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" 
href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css">
<link rel="icon" type="image/x-icon" 
href="https://img.icons8.com/ios/344/cookies.png">
<title>Enter Data</title>
</head>
<body>

<div class="p-5 d-flex justify-content-center">
<form action="insert" method="post" class="form-group">

<input  type="text" name="user">

<button  class="btn btn-dark" type="submit">Cookie!</button>

</form>
</div>

</body>
</html>
```

La pagina principale in cui è presente il form e l’attributo name per 
poter recuperare il valore dell’input.

---

Result JSP

```java
<%@page import="utils.CookieUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" 
href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css">
<meta charset="UTF-8">
<link rel="icon" type="image/x-icon" 
href="https://img.icons8.com/ios/344/cookies.png">
<title>Your cookies</title>
</head>
<body>

<%

String cookievalue = "";

Cookie nameString = CookieUtils.getCookie(request, "usercookie");

if(nameString != null){
	cookievalue = nameString.getValue();
} 

%>

<c:set var="valueinfo" scope="session" value="<%= cookievalue %>"/>

<div class="d-flex justify-content-center flex-column">
<c:if test="${ valueinfo.length() > 1 }">
<div class="p-5 d-flex flex-column justify-content-center 
align-items-center">
<h1 class="lead h-5 p-5 flex-column justify-content-center 
align-items-center"> Your cookie is: <b class="h-3"><%= cookievalue %></b> 
</h1> 
<a href="<%= request.getContextPath()%>">Home</a>
</div>
</c:if>

 

<c:if test="${ valueinfo.length() == 0 }">
<div class="p-5 d-flex flex-column justify-content-center 
align-items-center">
<h1 class="lead h-5 p-5"> You have not added cookies</h1> 
<a href="<%= request.getContextPath()%>">Home</a>
</div>
</c:if>

</div>

</body>
</html>
```

La pagina che gestisce la risposta e ci mostra il cookie. E’ presente uno 
scriptlet per recuperare il cookie.

```java
<%

String cookievalue = "";

Cookie nameString = CookieUtils.getCookie(request, "usercookie");

if(nameString != null){
	cookievalue = nameString.getValue();
} 

%>
```

Creiamo una nuova variabile e le assegniamo un valore nullo. 

Creiamo un oggetto di tipo cookie il quale richiama il metodo .getCookie() 
dalla classe utils e recupera il cookie che ci interessa.

Adesso, con il costrutto if diamo una condizione: se ‘nameString’ non è 
nullo allora il valore di cookievalue è il nome che cerchiamo.

Questo procedimento ci serve per evitare si inalzino errori e per gestire 
dinamicamente anche la pagina HTML.

```java
<c:set var="valueinfo" scope="session" value="<%= cookievalue %>"/>

<div class="d-flex justify-content-center flex-column">
<c:if test="${ valueinfo.length() > 1 }">
<div class="p-5 d-flex flex-column justify-content-center 
align-items-center">
<h1 class="lead h-5 p-5 flex-column justify-content-center 
align-items-center"> Your cookie is: <b class="h-3"><%= cookievalue %></b> 
</h1> 
<a href="<%= request.getContextPath()%>">Home</a>
</div>
</c:if>

 

<c:if test="${ valueinfo.length() == 0 }">
<div class="p-5 d-flex flex-column justify-content-center 
align-items-center">
<h1 class="lead h-5 p-5"> You have not added cookies</h1> 
<a href="<%= request.getContextPath()%>">Home</a>
</div>
</c:if>

</div>
```

Definiamo una proprietà nel tag c:set che ci servirà nel costrutto c:if.

La nostra proprietà è ‘cookievalue’ che, di default è null.

Quindi se l’utente invia senza scrivere nulla o se passano 60 secondi 
dalla creazione del cookie, verrà visualizzato ‘You have not added 
cookies’.
Mentre, se è presente il cookie, verrà visualizzato.

Insomma, gestiamo l’eventuale presenza o meno del cookie.

---

Adesso dobbiamo configurare il file xml, il quale ci permette di 
configurare l’home page e dove l’utente sarà indirizzato.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns="http://xmlns.jcp.org/xml/ns/javaee"
	xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee 
http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
	version="4.0">
	<display-name>cookies</display-name>
	<welcome-file-list>
		<welcome-file>Create.jsp</welcome-file>
	</welcome-file-list>

	<servlet>
		<servlet-name>result</servlet-name>
		<jsp-file>/Result.jsp</jsp-file>
	</servlet>

	<servlet-mapping>
		<servlet-name>result</servlet-name>
		<url-pattern>/result</url-pattern>
	</servlet-mapping>

</web-app>
```

L’utente una volta cliccato su ‘submit’ sarà poi indirizzato, grazie al 
servlet, alla pagina Result.jsp. Nel browser non uscirà come endpoint 
‘Result.jsp’ ma solo ‘/result’.

Info 
[qui](https://cloud.google.com/appengine/docs/flexible/java/configuring-the-web-xml-deployment-descriptor).

---

[**[Atomic 
Habits](https://www.amazon.it/Atomic-Habits-Proven-Build-Break/dp/0735211299)** 
di James Clear](Cookies%2080f1a597ccca460d81bd8e9e808a0a17/www5.webp)

**[Atomic 
Habits](https://www.amazon.it/Atomic-Habits-Proven-Build-Break/dp/0735211299)** 
di James Clear

Cercare di fare meglio ogni giorno, anche l’1%, ti porterà ad essere il 
38% migliore in un anno.

Immagina se fai di più.
