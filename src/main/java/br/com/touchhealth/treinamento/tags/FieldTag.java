package br.com.touchhealth.treinamento.tags;


import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;


public class FieldTag extends SimpleTagSupport {

    private Integer labelSize = 100;
    private String label;

    public void setLabelSize(Integer labelSize) {
        this.labelSize = labelSize;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void doTag() throws JspException, IOException {
        JspWriter out = this.getJspContext().getOut();
        out.print("<div class=\"treinamento-field\">");
        doLabelTag();
        getJspBody().invoke(null);
        out.print("</div>");
    }

    private void doLabelTag() throws JspException, IOException {
        LabelTag labelTag = new LabelTag();
        JspContext jspContext = this.getJspContext();
        labelTag.setJspContext(jspContext);
        labelTag.setParent(this);
        labelTag.setSize(labelSize);
        labelTag.setJspBody(new JspFragment() {

            @Override
            public void invoke(Writer writer) throws JspException, IOException {
                Writer out = writer;
                if (out == null) {
                    out = jspContext.getOut();
                }
                out.write(label);
            }

            @Override
            public JspContext getJspContext() {
                return jspContext;
            }
        });
        labelTag.doTag();
    }

}
