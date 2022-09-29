# Treinamento Java Web

Neste projeto vamos apresentar os conceitos básicos de Java para Web: 
* Tomcat 9.0.x
* Servlets 4.0
* JSP 2.3
* Expression Language 3.0
* JSTL 1.2.5
* Taglibs

## Setup e Pré-Requisitos

1. Tenha o docker instalado na sua máquina
2. Opcionais (mas importantes)
   1. Java 8 instalado
   2. Maven 3.6+ instalado
   3. IDE: InteliJ Ultimate ou Eclipse (InteliJ básico não dá suporte a JSPs)

## Estrutura básica do projeto

```
pom.xml             -- define o projeto maven e as dependencias
src             
  main
    java            -- fontes java
    resources       -- recursos, properties, xmls e etc
    webapp          -- base da aplicação web
      WEB-INF       -- diretório de configuração da aplicação web
        web.xml     -- arquivo de configuração da aplicação web
deploy              -- configurações de deploy no tomcat
Dockerfile          -- configuração de build e geração de imagem via docker
docker-compose.yml  -- compose para subir a aplicação
```

## Tópicos

### Documentação Tutoriais

[JavaEE 5](https://docs.oracle.com/javaee/5/tutorial) \
[JavaEE 6](https://docs.oracle.com/javaee/6/tutorial)

### Servlets

[Especificação](https://javaee.github.io/servlet-spec/downloads/servlet-4.0/servlet-4_0_FINAL.pdf)

[Javadoc](https://jakarta.ee/specifications/servlet/4.0/apidocs)

#### Servlet, Request e Response

Servlets correspondem a estrutura mais básica para o tratamento de chamadas HTTP dentro de um Servidor 
Java EE / Jakarta EE. 

**Servlet** é a classe que implementa o tratamento de uma ou mais URLs. As URLs tratadas são definidas através de um 
padrão de URLs como:  
* Exato: `/servlet/primeiro` 
* Terminando com: `*.action`, `*.jsp`
* Começando com: `/api/teste/*` 

Este padrão é correspondido com o caminho da URL, deixando de lado os parâmetros `?param1=a&param2=b`.

Uma servlet deve tratar pelo menos um padrão, mas podem tratar mais. É muito importante que elas não conflitem 
entre sí, pois se duas servlets tratarem o mesmo padrão, um erro será gerado ao subir a aplicação.

Basicamente operam os conceitos de `javax.servlet.http.HttpServletRequest` e `javax.servlet.http.HttpServletResponse`, 
em um método **service**, porém uma classe base muito comum já realiza algumas implementações que facilitam o uso final.
Alguns métodos podem ser implementados diretamente para o verbo HTML utilizado `GET`,`POST`, `PUT`, `DELETE` e etc.

```java
package javax.servlet.http;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.ResourceBundle;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public abstract class HttpServlet extends GenericServlet {

   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
   protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
   protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
   protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
   protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
   protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

   protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      String method = req.getMethod();
      long lastModified;
      if (method.equals("GET")) {
         lastModified = this.getLastModified(req);
         if (lastModified == -1L) {
            this.doGet(req, resp);
         } else {
            long ifModifiedSince = req.getDateHeader("If-Modified-Since");
            if (ifModifiedSince < lastModified) {
               this.maybeSetLastModified(resp, lastModified);
               this.doGet(req, resp);
            } else {
               resp.setStatus(304);
            }
         }
      } else if (method.equals("HEAD")) {
         lastModified = this.getLastModified(req);
         this.maybeSetLastModified(resp, lastModified);
         this.doHead(req, resp);
      } else if (method.equals("POST")) {
         this.doPost(req, resp);
      } else if (method.equals("PUT")) {
         this.doPut(req, resp);
      } else if (method.equals("DELETE")) {
         this.doDelete(req, resp);
      } else if (method.equals("OPTIONS")) {
         this.doOptions(req, resp);
      } else if (method.equals("TRACE")) {
         this.doTrace(req, resp);
      } else {
         String errMsg = lStrings.getString("http.method_not_implemented");
         Object[] errArgs = new Object[]{method};
         errMsg = MessageFormat.format(errMsg, errArgs);
         resp.sendError(501, errMsg);
      }
   }
}
```

Veja, modifique, brinque com alguns exemplos apresentados no projeto como: \
`br.com.touchhealth.treinamento.web.PrimeroServlet` \
`br.com.touchhealth.treinamento.web.SegundoServlet`

Os Servlets precisam ser declaradas para que o container possa encontrar, configurar e inicializar. A configuração pode 
ser por anotações ou pelo `web.xml`. O arquivo xml se faz util quando a Servlet faz parte de algum framework, e precisa
receber parametros para ser configurado, ou quando questões como permissões de uso serão definidas apenas na aplicação 
final ou cliente do framework.

Exemplos:

**Anotação**
```java
@WebServlet(urlPatterns = "/servlet/primeiro")
@DeclareRoles("admin")
@ServletSecurity(@HttpConstraint(rolesAllowed = "admin"))
public class PrimeroServlet extends HttpServlet {

}
```

**web.xml**
```xml
<web-app version="4.0"
         xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
        http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd">

   <servlet>
      <servlet-name>PrimeiroServlet</servlet-name>
      <servlet-class>br.com.touchhealth.treinamento.web.PrimeroServlet</servlet-class>
   </servlet>

   <servlet-mapping>
      <servlet-name>PrimeiroServlet</servlet-name>
      <url-pattern>/servlet/primeiro</url-pattern>
   </servlet-mapping>

   <security-role>
      <role-name>admin</role-name>
   </security-role>

   <security-constraint>
      <web-resource-collection>
         <web-resource-name>Admin Area</web-resource-name>
         <url-pattern>/servlet/primeiro</url-pattern>
      </web-resource-collection>
      <auth-constraint>
         <role-name>admin</role-name>
      </auth-constraint>
   </security-constraint>

</web-app>
```

#### Filters

Filtros são outra estrutura importante, que também opera com os conceitos de `HttpServletRequest` e 
`HttpServletResponse`. A diferença principal é que um filtro pode ser aplicado a um ou mais padrões, e vários filtros 
podem tratar o mesmo padrão, ou terem algum tipo de intersecção. Eles trabalham em uma cadeia de responsabilidades, 
onde o primeiro filtro delega para o segundo depois para o terceiro, assim sucessivamente.

No caso dos filtros, a ordem de declaração é de extrema importância, ainda mais se eles não forem totalmente 
independentes, ou seja, um depende do que os anteriores prepararam. Eles podem interromper o processo da cadeia no meio,
ou até mesmo funcionarem como uma servlet, tratando e escrevendo a resposta da chamada. Um exemplo com 3 Filtros e uma 
Servlet executariam da seguinte ordem: 
```
 Filtro 1 (processando request)
 Filtro 2 (processando request)
 Filtro 3 (processando request)
 Servlet 
 Fintro 3 (processando response)
 Fintro 2 (processando response)
 Fintro 1 (processando response)
```

Filtros normalmente extendem a classe `javax.servlet.http.HttpFilter`:

```java
package br.com.touchhealth.treinamento.web;

//imports

@WebFilter(urlPatterns = { "/servlet/*", "/admin/*" })
public class AuditFilter extends HttpFilter {

   @Override
   protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
           throws IOException, ServletException {
      long t1 = System.currentTimeMillis();
      System.out.println("AuditFilter Antes: " + req.getRequestURL());
      chain.doFilter(req, res);
      long t2 = System.currentTimeMillis();
      System.out.println("AuditFilter Depois: " + (t2 - t1) + " ms");
   }
}
```

Para garantir a ordem de execução, é recomendado usar o **web.xml**. A ordem de declaração dos filter-mapping é a ordem 
de execução dos filtros.

```xml
<web-app version="4.0"
         xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
        http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd">

   <filter>
      <filter-name>AuditFilter</filter-name>
      <filter-class>br.com.touchhealth.treinamento.web.AuditFilter</filter-class>
   </filter>
   <filter>
      <filter-name>HeaderFilter</filter-name>
      <filter-class>br.com.touchhealth.treinamento.web.HeaderFilter</filter-class>
   </filter>

   <filter-mapping>
      <filter-name>AuditFilter</filter-name>
      <url-pattern>/servlet/*</url-pattern>
      <url-pattern>/admin/*</url-pattern>
   </filter-mapping>
   <filter-mapping>
      <filter-name>HeaderFilter</filter-name>
      <url-pattern>/servlet/*</url-pattern>
      <url-pattern>/admin/*</url-pattern>
   </filter-mapping>

</web-app>
```

#### Listeners

Podem ser usados para detectar eventos relacionados ao Servidor como por exemplo:

O `ServletRequestListener` pode ser usado para verificar o inicio e o fim de um request, podendo manipular, salvar ou 
apenas gerar estatisticas associadas a esse tipo de evento.

```java
package javax.servlet;

import java.util.EventListener;

public interface ServletRequestListener extends EventListener {
    default void requestDestroyed(ServletRequestEvent sre) {
    }

    default void requestInitialized(ServletRequestEvent sre) {
    }
}
```

`ServletContextListener` pode ser usado para detectar que a aplicação subiu ou foi destruida, pode ser usado para 
inicializar ou liberar recursos associados.

```java
package javax.servlet;

import java.util.EventListener;

public interface ServletContextListener extends EventListener {
    default void contextInitialized(ServletContextEvent sce) {
    }

    default void contextDestroyed(ServletContextEvent sce) {
    }
}
```

e Listeners para eventos relativos a Sessão

`HttpSessionActivationListener`\
`HttpSessionAttributeListener`\
`HttpSessionBindingListener`\
`HttpSessionIdListener`\
`HttpSessionListener`

Exemplo:

```java
package br.com.touchhealth.treinamento.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class ApplicationLifecycleListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Aplicação Inicializada com Sucesso " + sce.getServletContext().getContextPath());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Aplicação Destruida com Sucesso " + sce.getServletContext().getContextPath());
    }
}
```

#### Session e Cookies

Sessão e Cookies são duas formas de armazenar informações do usuário entre requests. A Sessão armazena as informações no
servidor, e o Cookie armazena as informações no cliente (Browser).

Sessões armazenam objetos java, que normalmente devem ser `java.io.Serializable`, pois a sessão pode ter que ser 
armazenada em disco, ou ser transferida entre servidores para garantir alta disponibilidade.

Cookies são limitados em tamanho e armazenam texto. Funcionam de uma forma que garante que o cookie seja transmitido ao 
servidor em todo o request subsequente a sua criação, até a sua destruição. Cookies podem ser acessíveis ou não ao 
contexto javascript, permitindo que o javascript leia o seu conteúdo. Esse tipo de cookie mais acessível não deve ser 
usado para implementações relativas a segurança por exemplo.

### JSP

[Especificação](https://download.oracle.com/otndocs/jcp/jsp-2_3-mrel2-spec/)

[Javadoc](https://jakarta.ee/specifications/pages/2.3/apidocs/)

#### Sintaxes básicas  

##### 1. JSP 

É a sintaxe mais comum, não força que o arquivo seja um XML bem formado. Semelhante a um HTML normal, com a 
possiblidade de usar algumas tags especiais   

#####  2. JSPX 

É a sintaxe muito comum nos projetos da Touch, arquivo seja um XML bem formado compatível com o XHTML.
Essa sintaxe possui algumas construções diferentes. 

#### Scriptlets 

Corresponde ao uso de código java dentro da sintaxe HTML. É considerado "feio", pelo abuso que pode ser cometido, 
por exemplo implementando toda a lógica da view e controller no próprio JSP. Porém hoje vemos frameworks modernos como 
React fazendo um uso semelhante.

##### Na sintaxe JSP

`<%  %>` usa um código java qualquer. Algumas variáveis especiais estão disponíveis
`<%= %>` imprime na resposta o resultado da expressão java

```html
<% if (a == 10) { %>
<div> ... Codigo HTML / JSP aqui ...</div> 
<% } else { %>
<div> ... Outro HTML ... </div>
<% } %>

<% for (int i = 0; i < 100; i++) { %>
<div><%= i %></div>
<% } %>
```

##### Na sintaxe JSPX

`<jsp:scriptlet><![CDATA[ ]]></jsp:scriptlet>` usa um código java qualquer. Algumas variáveis especiais estão disponíveis
`<jsp:expression><![CDATA[ ]]></jsp:expression>` imprime na resposta o resultado da expressão java

```html
<jsp:scriptlet><![CDATA[if (a == 10) {]]></jsp:scriptlet>
    <div> ... Codigo HTML / JSP aqui ...</div>
<jsp:scriptlet><![CDATA[} else {]]></jsp:scriptlet>
    <div> ... Outro HTML ... </div>
<jsp:scriptlet><![CDATA[}]]></jsp:scriptlet>

<jsp:scriptlet><![CDATA[for (int i = 0; i < 100; i++) {]]></jsp:scriptlet>
    <div><jsp:expression><![CDATA[i]]></jsp:expression></div>
<jsp:scriptlet><![CDATA[}]]></jsp:scriptlet>
```

### Expression Language

`${}` É uma expressão que pode ser usada de forma semelhante a `<%= %>` ou <jsp:expression><![CDATA[ ]]></jsp:expression> 
porém com uma sintaxe especial, mais simples para os cenários de uso mais comuns. 

[Especificação](https://download.oracle.com/otndocs/jcp/el-3_0-fr-eval-spec/index.html)

[Javadoc](https://javadoc.io/doc/org.glassfish/javax.el/latest/index.html)

#### Exemplos de uso no JSP

```html
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty propriedade}">
    <div>propriedade antes: ${propriedade}</div>
</c:if>

<c:if test="${not empty propriedadeCliente}">
    <div>propriedade cliente: ${propriedadeCliente}</div>
</c:if>

<div>propriedade nova: ${propriedadeNova}</div>
```

#### Exemplos de uso no código Java

```java
javax.el.ELProcessor processor = new javax.el.ELProcessor();
processor.setValue("a", 10d);
processor.setValue("b", 4d);
processor.setValue("c", 2d);
Double val = processor.eval("(a + b) * c");
```

### Taglibs e JSTL

[Artigo no Baeldung](https://www.baeldung.com/jstl)

[Tagdoc](https://jakarta.ee/specifications/tags/1.2/tagdocs/)

### Construindo sua propria TagLib
