package tmp;

import mybatis.Template;
import mybatis.TemplateMapper;
import mybatis.Website;
import mybatis.WebsiteMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.xsoup.Xsoup;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by coral on 2017/7/7.
 */
public class Parse {

    // TODO: 2017/7/13  catch exception network problems 

    public static void main(String args[]) throws Exception {

        BloomFilter bloomFilter = new BloomFilter();

        TemplateMapper tm = new TemplateMapper();
        //read DB, get all templates
        List<Template> templates = null;
        try {
            templates = tm.findAllTemplate();
        } catch (Exception e) {
            System.out.println("parse.java can't find tempaltes");
            e.printStackTrace();
        }
        Parse parse = new Parse();
        // mybatis.WebsiteMapper --- manipulate DB
        WebsiteMapper webMap = new WebsiteMapper();


        //get all templates' websites
        for (int i = 0; i < templates.size(); i++) {
            // TODO: 2017/7/1 ?? next size change iteration change?  delete when large?
            System.out.println("template size is :  "+ templates.size());

            Template template = templates.get(i);
            // TODO: 2017/7/9   get url html
            Document urlHtml = Jsoup.connect(template.getChannel_url()).get();
            String html = urlHtml.html();

            //nextpage
            Template tmp = parse.nextPageTemplate(html, template);
            if(tmp != null)
                templates.add(tmp);

            List<String> urls = parse.getXpathContent(html, template.getUrl_xpath());
            System.out.println("the number of urls is :" + urls.size());

            //get every url's website, and insert into DB
            for (int j = 0; j < urls.size(); j++) {
                String currentUrl =urls.get(j);
                //ignore repeated url
                if(bloomFilter.contains(currentUrl)){
                    System.out.println("duplicated url ");
                    continue;
                }
                else    bloomFilter.addValue(currentUrl);

                // TODO: 2017/7/9  get web html
// ***               Document webHtml = Jsoup.connect(template.getChannel_url()).get();
                Document webHtml = Jsoup.connect(currentUrl).get();
                if (webHtml == null) {
                    System.out.println("wrong url, fail to load html content");
                    continue;
                }
//                mybatis.Website web = parse.getWebsite2(webHtml, template);
                Website web = parse.getWebsite(webHtml.html(), template);
                webMap.insertWebsite(web);
            }
        }
    }

    // TODO: 2017/7/10  ??Create_time
    //modify channel_url
    private Template nextPageTemplate(String html, Template template){
        Template tpl = cloneTemplate(template);
        List<String> l = getXpathContent(html, tpl.getChannel_url_nextpage());
        //0 or null(exception wrong xpath)
        if(l == null  || l.size() == 0 ){
            System.out.println("has no next page");
            return null;
        }
        String url = l.get(0);
        tpl.setChannel_url(url);
        return tpl;
    }


    //deal with wrong xpath exception
    public List<String> getXpathContent(String html, String xpath) {
        List<String> content = null;
        try {
            content = Xsoup.compile(xpath).evaluate(Jsoup.parse(html)).list();
        } catch (Exception e) {
            System.out.println("xpath is wrong");
            e.printStackTrace();
        }
        return content;
    }

    //catch exception to keep program continue to run
    public String xsoupHandleNull(Document doc, String xpath) {
        String out;
        try {
            //// TODO: 2017/7/9   test
//            WriteTxt.write(doc.html() + "\n" + "xpath==" + xpath + "\n\n", "default" + "\n\n");
            out = Xsoup.compile(xpath).evaluate(doc).get();
        } catch (Exception e) {
            System.out.println("xsoupHandleNull -- null 值，website不全？");
            e.printStackTrace();
            return null;
        }
        return out;
    }

    public Website getWebsite2(Document doc, Template template){
        Website web = new Website();
        try {
            //// TODO: 2017/7/9
            web.setWebsite_name(template.getWebsite_name());
            web.setChannel_name(template.getChannel_name());

            web.setTitle(xsoupHandleNull(doc, template.getTitle_xpath()));
            web.setContent(xsoupHandleNull(doc, template.getContent_xpath()));
            web.setPubtime(xsoupHandleNull(doc, template.getPubtime_xpath()));
            web.setAuthor(xsoupHandleNull(doc, template.getAuthor_xpath()));

//            web.setTitle( Xsoup.compile(template.getTitle_xpath()).evaluate(doc).get() );
//            web.setContent(Xsoup.compile(template.getContent_xpath()).evaluate(doc).get());
//            web.setPubtime(Xsoup.compile(template.getPubtime_xpath()).evaluate(doc).get());
//            web.setAuthor(Xsoup.compile(template.getAuthor_xpath()).evaluate(doc).get());

            Timestamp time = new Timestamp(System.currentTimeMillis());
            web.setCrawler_time(time);
            web.setUrl(template.getChannel_url());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return web;
    }

    public Website getWebsite(String html, Template template) {
        Website web = new Website();// ORM : mapped to entity website
        Document doc = Jsoup.parse(html);

        try {
            //// TODO: 2017/7/9  
            web.setWebsite_name(template.getWebsite_name());
            web.setChannel_name(template.getChannel_name());
            //template id --> web tid
            web.setTid(template.getId());

            web.setTitle(xsoupHandleNull(doc, template.getTitle_xpath()));
            web.setContent(xsoupHandleNull(doc, template.getContent_xpath()));
            web.setPubtime(xsoupHandleNull(doc, template.getPubtime_xpath()));
            web.setAuthor(xsoupHandleNull(doc, template.getAuthor_xpath()));

//            web.setTitle( Xsoup.compile(template.getTitle_xpath()).evaluate(doc).get() );
//            web.setContent(Xsoup.compile(template.getContent_xpath()).evaluate(doc).get());
//            web.setPubtime(Xsoup.compile(template.getPubtime_xpath()).evaluate(doc).get());
//            web.setAuthor(Xsoup.compile(template.getAuthor_xpath()).evaluate(doc).get());

            Timestamp time = new Timestamp(System.currentTimeMillis());
            web.setCrawler_time(time);
            web.setUrl(template.getChannel_url());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return web;
    }

    private Template cloneTemplate(Template template){
        //with id;
        Template tpl = new Template();
        tpl.setId(template.getId());
        tpl.setWebsite_name(template.getWebsite_name());
        tpl.setChannel_name(template.getChannel_name());
        tpl.setChannel_url(template.getChannel_url());
        tpl.setChannel_url_nextpage(template.getChannel_url_nextpage());
        tpl.setTitle_xpath(template.getTitle_xpath());
        tpl.setUrl_xpath(template.getUrl_xpath());
        tpl.setAuthor_xpath(template.getAuthor_xpath());
        tpl.setPubtime_xpath(template.getPubtime_xpath());
        tpl.setContent_xpath(template.getContent_xpath());
        tpl.setCreate_time(template.getCreate_time());
        return tpl;
    }
}
