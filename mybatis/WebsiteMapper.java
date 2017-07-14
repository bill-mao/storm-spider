package mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Created by coral on 2017/7/7.
 */
public class WebsiteMapper implements Serializable{
    public SqlSession getSqlSession () throws IOException {
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        return factory.openSession();
    }

    public void insertWebsite(Website web) throws Exception {
        SqlSession session = getSqlSession();
        //---------------------
        session.insert("mybatis.WebsiteMapper.insertWebsite", web);
        session.commit();   //增删改，一定一定要加上commit操作
        //----------------------
        session.close();
    }


}
