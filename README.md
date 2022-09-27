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

### Servlets

### JSP

### Expression Language

### Taglibs e JSTL

### Construindo sua propria TagLib
