package me.skyun.anno.compiler;

import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

/**
 * Created by linyun on 16/10/28.
 */
public class AptUtils {
    /**
     * 返回element所在的类名
     */
    public static Symbol.ClassSymbol getEnclosingClass(Element element) {
        String elementName = element.toString();
        while (true) {
            if (element instanceof Symbol.ClassSymbol) {
                return (Symbol.ClassSymbol) element;
            } else {
                Element superElement = element.getEnclosingElement();
                if (superElement != null) {
                    element = superElement;
                    continue;
                } else {
                    throw new RuntimeException("Can't find enclosing class for " + elementName);
                }
            }
        }
    }

    /**
     * 返回element所在的package
     */
    public static Element getEnclosingPackage(Element element) {
        while (true) {
            Element superElement = element.getEnclosingElement();
            if (superElement.getKind().equals(ElementKind.PACKAGE)) {
                return superElement;
            } else if (superElement.asType().toString() == null ||
                    superElement.asType().toString().equals("")) {
                return element;
            } else {
                element = superElement;
            }
        }
    }

    public static Configuration initConfiguration() {
        Configuration config = new Configuration(Configuration.VERSION_2_3_25);
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        config.setLogTemplateExceptions(false);
        //            config.setDirectoryForTemplateLoading(new File(Const.TEMPLATE_PATH));
        config.setClassForTemplateLoading(AptUtils.class, Const.TEMPLATE_PATH);
        return config;
    }

    public static Attribute.Compound getAnnotatioMirror(Symbol symbol, Class annoClz) {
        for (Attribute.Compound compound : symbol.getAnnotationMirrors()) {
            if (compound.getAnnotationType().toString().equals(annoClz.getCanonicalName())) {
                return compound;
            }
        }
        return null;
    }

    public static String arrayJoin(String jointer, Collection<String> collection) {
        if (collection.isEmpty()) {
            return "";
        }
        Iterator<String> iterator = collection.iterator();
        StringBuilder builder = new StringBuilder(iterator.next());
        while (iterator.hasNext()) {
            builder.append(", ").append(iterator.next());
        }
        return builder.toString();
    }
}
