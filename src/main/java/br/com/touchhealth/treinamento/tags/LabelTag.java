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
