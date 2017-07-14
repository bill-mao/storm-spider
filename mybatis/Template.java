package mybatis;

/**
 * Created by coral on 2017/7/6.
 */
public class Template {
    private int id;
    private String website_name;
    private String channel_name;
    private String channel_url;
    private String channel_url_nextpage;
    private String title_xpath;
    private String url_xpath;
    private String author_xpath;
    private String pubtime_xpath;
    private String content_xpath;
    private String create_time;

    public String getWebsite_name() {
        return website_name;
    }

    public void setWebsite_name(String website_name) {
        this.website_name = website_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannel_url_nextpage() {
        return channel_url_nextpage;
    }

    public void setChannel_url_nextpage(String channel_url_nextpage) {
        this.channel_url_nextpage = channel_url_nextpage;
    }

    public String getTitle_xpath() {
        return title_xpath;
    }

    public void setTitle_xpath(String title_xpath) {
        this.title_xpath = title_xpath;
    }

    public String getPubtime_xpath() {
        return pubtime_xpath;
    }

    public void setPubtime_xpath(String pubtime_xpath) {
        this.pubtime_xpath = pubtime_xpath;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getChannel_url() {
        return channel_url;
    }

    public void setChannel_url(String channel_url) {
        this.channel_url = channel_url;
    }

    public String getUrl_xpath() {
        return url_xpath;
    }

    public void setUrl_xpath(String url_xpath) {
        this.url_xpath = url_xpath;
    }

    public String getAuthor_xpath() {
        return author_xpath;
    }

    public void setAuthor_xpath(String author_xpath) {
        this.author_xpath = author_xpath;
    }

    public String getContent_xpath() {
        return content_xpath;
    }

    public void setContent_xpath(String content_xpath) {
        this.content_xpath = content_xpath;
    }
}
//
//
//`id` int,
//          `website_name` varchar(64),
//          `region` varchar(64),
//          `country` varchar(64),
//          `language` varchar(64),
//          `channel_name` varchar(128),
//          `channel_url` varchar(128),
//          `channel_url_nextpage` varchar(128),
//          `url_xpath` varchar(128),
//          `start_time` String,
//          `stop_time` String,
//          `status` varchar(64),
//          `author_xpath` varchar(128),
//          `pubtime_xpath` varchar(128),
//          `content_xpath` varchar(128),
//          `source_xpath` varchar(128),
//          `update_time` String,
//          `failure_count` int,
//          `create_time` String