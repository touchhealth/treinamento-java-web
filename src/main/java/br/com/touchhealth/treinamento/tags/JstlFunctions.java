/*
 * Copyright (c) 1999-2009 Touch Tecnologia e Informatica Ltda. Gomes de Carvalho, 1666, 3o. Andar, Vila Olimpia, Sao
 * Paulo, SP, Brasil. Todos os direitos reservados. Este software e confidencial e de propriedade da Touch Tecnologia e
 * Informatica Ltda. (Informacao Confidencial). As informacoes contidas neste arquivo nao podem ser publicadas, e seu
 * uso esta limitado de acordo com os termos do contrato de licenca.
 */

package br.com.touchhealth.treinamento.tags;


import java.util.Collection;


public class JstlFunctions {

    public static Boolean contains(Collection collection, Object object) {
        if (collection != null) {
            return collection.contains(object);
        }
        return false;
    }

}
