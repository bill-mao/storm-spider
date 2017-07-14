package mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by coral on 2017/7/6.
 */
public class TemplateMapper {

    public SqlSession getSqlSession () throws IOException {
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        return factory.openSession();
    }

    public List<Template> findAllTemplate() throws Exception{
        SqlSession session = getSqlSession();
        //start transaction
        //参数一：namespace.id
        List<Template> templates = session.selectList("mybatis.TemplateMapper.findTemplateAll");
        //end
        session.close();
        return templates;
    }

    public List<Template> findTemplateById(int id) throws Exception{
        SqlSession session = getSqlSession();
        //start transaction
        //参数一：namespace.id
        List template = session.selectOne("mybatis.TemplateMapper.findTemplateById",id); //参数一：namespace.id
        //end
        session.close();
        return template;

    }



}
