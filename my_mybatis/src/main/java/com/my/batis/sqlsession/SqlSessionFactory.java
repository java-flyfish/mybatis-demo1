package com.my.batis.sqlsession;

import com.my.batis.config.Configuration;
import com.my.batis.config.Mapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class SqlSessionFactory {
    private InputStream inputStream;
    //需要传入一个流对象
    public SqlSessionFactory(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    //获取SqlSessiom 对象
    public SqlSession openSession() {
        Configuration configuration = new Configuration();
        loadConfiguration(configuration);
        return new SqlSessionImpl(configuration);
    }

    //读取配置文件
    private void loadConfiguration(Configuration cfg){

        SAXReader saxReader = new SAXReader();
        Document doc = null;
        Element rootElement = null;
        try {
            doc = saxReader.read(inputStream);
            rootElement = doc.getRootElement();
            List<Element> list = rootElement.selectNodes("//property");
            for (Element e : list) {
                //e.attributeValue(name) :获取指定的属性名的属性值
                //如果取出的name的属性值是driver，name就可以封装大搜Configuration中的driverClass属性中
                if(e.attributeValue("name").equalsIgnoreCase("driver")){
                    cfg.setDriverClass(e.attributeValue("value"));
                }
                if(e.attributeValue("name").equalsIgnoreCase("url")){
                    cfg.setUrl(e.attributeValue("value"));
                }
                if(e.attributeValue("name").equalsIgnoreCase("username")){
                    cfg.setUsername(e.attributeValue("value"));
                }
                if(e.attributeValue("name").equalsIgnoreCase("password")){
                    cfg.setPassword(e.attributeValue("value"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //加载另一个配置文件
        Element mappers = rootElement.element("mappers");
        List<Element> mapperList = mappers.elements();
        for (Element e : mapperList) {
            String mapperPath = e.attributeValue("resource");
            loadMapperConfiguration(mapperPath, cfg);
        }

    }

    private void loadMapperConfiguration(String mapperPath, Configuration cfg) {
        InputStream inputStream = SqlSessionFactory.class.getClassLoader().getResourceAsStream(mapperPath);
        SAXReader saxReader = new SAXReader();
        try {
            Document doc = saxReader.read(inputStream);
            Element rootElement = doc.getRootElement();

            Map<String, Mapper> mapperMap = cfg.getMappers();

            String key = null;
            String namespace = rootElement.attributeValue("namespace");

            //获取根节点下所有的子节点
            List<Element> elements = rootElement.elements();
            for (Element element : elements) {
                String id = element.attributeValue("id");
                //com.itheima.mapper.UserDao.findAll
                key = namespace +"."+ id;
                // value：存储Mapper对象，返回值类型和sql语句
                Mapper mapper = new Mapper();
                //获取返回值类型
                String resultType = element.attributeValue("resultType");
                mapper.setResultType(resultType);
                //获取sql语句:getTextTrim{获取节点中的文本且去除两侧空白
                String sql = element.getTextTrim();
                mapper.setSql(sql);
                mapperMap.put(key,mapper);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
}
