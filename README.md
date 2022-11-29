# Treinamento Java Web

Neste projeto vamos apresentar os conceitos básicos de Java para Web: 
* Tomcat 9.0.x
* Servlets 4.0
* JSP 2.3
* Expression Language 3.0
* JSTL 1.2.5
* Taglibs

## Setup e Pré-Requisitos

1. Tenha o docker e docker-compose instalado na sua máquina
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

#### Diretivas

##### @page

É usada para fazer a configuração básica da página jsp. Os atributos mais importantes são:

| Atributos     | Descrição                                                                         |
|---------------|-----------------------------------------------------------------------------------|
| language      | sempre `java`                                                                     |
| import        | Permite a página JSp importar pacotes e classes que serão utilizados pela página. |
| session	     | Define se a página fará uso de sessão, o valor padrão é true.                     |
| errorPage     | Define o caminho relativo a uma página de erro, caso ocorra uma exceção.          |
| contentType   | Informa o tipo de saída do documento. O valor default é text/html.                |
| isErrorPage	 | Informa se a página é uma página de erro.                                         |
| pageEncoding	 | Define o caracter enconding da página.                                            |

JSP:
```
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"  %>
<%@ page import="java.util.* %>
<%@ page import="java.text.* %>
```

JSPX:
```
<jsp:root xmlns="http://www.w3.org/1999/xhtml" version="2.0" 
          xmlns:jsp="http://java.sun.com/JSP/Page">
    <jsp:directive.page contentType="text/html" pageEncoding="UTF-8" />
    <jsp:directive.page import="java.util.*" />
    <jsp:directive.page import="java.text.*" />
</jsp:root>
```

##### @include

É usada para incluir uma outra página jsp ou jspx dentro da atual.

JSP:
```
<%@include file="terceiro.jsp"%>
```

JSPX:
```
<jsp:include page="terceiro.jsp"/>
```

##### @taglib

É usada para declarar taglibs ou tags

JSP:
```
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://www.touchtec.com.br/treinamento-tags" %>
```

JSPX:
```
<jsp:root xmlns="http://www.w3.org/1999/xhtml" version="2.0"
          xmlns:jsp="http://java.sun.com/JSP/Page"
          xmlns:c="http://java.sun.com/jsp/jstl/core"
          xmlns:t="http://www.touchtec.com.br/treinamento-tags">
</jsp:root>
```

##### @tag

Usada para a declaração de tag files

JSP:
```
<%@ tag pageEncoding="UTF-8" %>
```

### Scriptlets 

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

##### Objetos Implícitos

| Nome        | Tipo                                   |
|-------------|----------------------------------------|
| application | javax.servlet.ServletContext           |
| config      | javax.servlet.ServletConfig            |
| out         | javax.servlet.jsp.JspWriter            |
| pageContext | javax.servlet.jsp.PageContext          |
| request     | javax.servlet.http.HttpServletRequest  |
| response    | javax.servlet.http.HttpServletResponse |
| session     | javax.servlet.http.HttpSession         |

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

Para construir sua própria tag de componente, são necessarias pelo menos duas coisas:

1. A implementação da Tag em sí

```java
package br.com.touchhealth.treinamento.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class LabelTag extends SimpleTagSupport {

    private Integer size;

    public void setSize(Integer size) {
        this.size = size;
    }

    public void doTag() throws JspException, IOException {
        JspWriter out = this.getJspContext().getOut();
        out.print("<span class=\"treinamento-label\"");
        if (size != null) {
            out.print(" style=\"width: "+ size +"px;\"");
        }
        out.print(">");
        getJspBody().invoke(null);
        out.print("</span>");
    }

}
```

2. A declaração da existencia da tag em um arquivo .tld
`META-INF/treinamento-tags.tld`

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<taglib xmlns="http://java.sun.com/xml/ns/j2ee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="2.0" xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd">
  <description>Biblioteca de componentes do Treinamento</description>
  <display-name>Treinamento Tags</display-name>
  <tlib-version>1.0</tlib-version>
  <short-name>treinamento</short-name>
  <uri>http://www.touchtec.com.br/treinamento-tags</uri>
  <tag>
    <description>Label</description>
    <name>label</name>
    <tag-class>br.com.touchhealth.treinamento.tags.LabelTag</tag-class>
    <body-content>scriptless</body-content>
    <attribute>
      <name>size</name>
      <required>false</required>
      <rtexprvalue>true</rtexprvalue>
    </attribute>
  </tag>
</taglib>
```

Seu uso fica assim:

```html
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" uri="http://www.touchtec.com.br/treinamento-tags" %>
<t:label>a:</t:label>
<t:label size="200">b:</t:label>
```

### Tag Files

[JEE 5 Tutorial - Tag Files](https://docs.oracle.com/javaee/5/tutorial/doc/bnama.html)

Usar tag files é uma forma simples de criar componentes de tela mais simples, ele utiliza fragmentos de código JSP 
e devem ser armazenadas na pasta `WEB-INF/tags/` com a extensão `.tag`:

``` 
<%@ tag pageEncoding="UTF-8" %>
<b>Cabeçalho Padrão</b>
```

#### Diretivas

| Diretiva  | Descrição                                                                                              |
|-----------|--------------------------------------------------------------------------------------------------------|
| taglib    | Análoga a usada para taglibs.                                                                          |
| include   | Análoga a include padrão, porém algumas sintaxes podem não ser suportadas em includes usados nas tags. |
| tag       | Similar a diretiva page, mas para arquivos do tipo tag.                                                |
| attribute | Declara um atributo da tag.                                                                            |
| variable  | Declara uma variável EL que será exposta pela tag para a página.                                       |

#### Atributos importantes da diretiva tag

| Atributo           | Descrição                                                 |
|--------------------|-----------------------------------------------------------|
| body-content       | empty, tagdependent, ou scriptless. (Default scriptless). |
| dynamic-attributes | Indica se a tag suporta atributos dinâmicos.              |
| import             | mesmo da diretiva page.                                   |
| pageEncoding       | mesmo da diretiva page.                                   |

#### Atributos importantes da diretiva attribute

| Atributo    | Descrição                                                           |
|-------------|---------------------------------------------------------------------|
| name        | O nome do atributo.                                                 |
| required    | Se o atributo é obrigatório (true) ou não (false). Default é false. |
| rtexprvalue | Se o atributo pode ser definido por uma expressão. Default é true.  |
| type        | Tipo do atributo. Default é java.lang.String.                       |
| fragment    | Define se o atributo é um fragmento de JSP                          |

#### Atributos importantes da diretiva variable

| Atributo                          | Descrição                                                                                                                                                                                                                                                                                                   |
|-----------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| name-given ou name-from-attribute | Define uma variável EL para ser usada na pagina. Ou name-given ou name-from-attribute deve ser especificada. <br/> Se name-given for especificada, o valor é o nome da variável. <br/>Se name-from-attribute for especificado, o nome da variável é o valor de um atributo cujo nome foi especificado aqui. |
| alias                             | define variable com um nome local para ser usada pela tag. Requerido no caso do `name-from-attribute` ter sido utilizado, e por isso não conhecemos o nome real da variável.                                                                                                                                |
| variable-class                    | O tipo da variavel. Default é java.lang.String.                                                                                                                                                                                                                                                             |
| declare                           | Se a variável é declarada ou não. Default é true.                                                                                                                                                                                                                                                           |
| scope                             | AT_BEGIN, AT_END, ou NESTED. Default é NESTED.                                                                                                                                                                                                                                                              |

### Exemplos

**Tag com Parametros**: WEB-INF/tags/treinamento/olamundo.tag

```html
<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="greeting" required="true" %>
<span>Olá Mundo, bem vindo <b>${greeting}</b></span>
```

**Tag com Corpo**: WEB-INF/tags/treinamento/field.tag

```html
<%@ tag pageEncoding="UTF-8" %>
<%@ taglib prefix="t" uri="http://www.touchtec.com.br/treinamento-tags" %>
<%@ attribute name="label" required="true" %>
<div class="treinamento-field">
	<t:label>${label}</t:label>
	<jsp:doBody />
</div>
```

**Tag com Corpo e Variáveis**: WEB-INF/tags/treinamento/iterate.tag

```html
<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="iterations" required="true" type="java.lang.Integer" %>
<%@ attribute name="var" required="true" rtexprvalue="false" %>
<%@ variable alias="it" name-from-attribute="var" %>
<%
for (int i = 0; i < iterations; i++) {
	jspContext.setAttribute("it", i);
%>
	<jsp:doBody />
<%	
}
%>
```

**Tag com Corpo e Fragmentos**: WEB-INF/tags/treinamento/layout.tag

```html
<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="head" required="true" fragment="true" %>
<%@ attribute name="left" fragment="true" %>
<div style="position: relative;height: 480px; overflow: auto;">
	<div style="position: absolute;top: 0;left: 0;right: 0;height: 80px">
		<jsp:invoke fragment="head"/>
	</div>
	<div style="position: absolute;top: 80px;left: 0;bottom: 0;width: 340px">
		<jsp:invoke fragment="left"/>
	</div>
	<div style="position: absolute;top: 80px;left: 340px;bottom: 0;right: 0">
		<jsp:doBody />
	</div>
</div>
```

**Uso no JSP**:

````html
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tt" tagdir="/WEB-INF/tags/treinamento" %>
<tt:layout>
	<jsp:attribute name="head">
		<tt:simple></tt:simple>
	</jsp:attribute>
	<jsp:attribute name="left">
		<tt:olamundo greeting="Fernando" />
	</jsp:attribute>
	<jsp:body>
		<ul>	
			<tt:iterate iterations="5" var="k">
				<li>Iteração ${k}</li>
			</tt:iterate>
		</ul>
	</jsp:body>
</tt:layout>
````