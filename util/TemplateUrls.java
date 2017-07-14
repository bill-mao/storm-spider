package util;

import mybatis.Template;

/**
 * Created by coral on 2017/7/13.
 */
public class TemplateUrls {
    private Template templ;
    private String[] urls;

    //fastJson
    public TemplateUrls(){

    }

    public TemplateUrls(Template template, String urls[]){
        //fastjson
//        super();
        this.templ = template;
        this.urls = urls;
    }

    public Template getTempl() {
        return templ;
    }

    public String[] getUrls() {
        return urls;
    }
}
