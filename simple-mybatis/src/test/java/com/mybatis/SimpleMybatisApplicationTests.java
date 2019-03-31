package com.mybatis;

import com.mybatis.bean.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.Reader;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleMybatisApplicationTests {

	@Test
	public void contextLoads() {

		String resource = "Configuration.xml";
		Reader reader;
		try {
			reader = Resources.getResourceAsReader(resource);
			SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);

			SqlSession session = sqlMapper.openSession();
			try {
				User user =  session.selectOne("com.mybatis.dao.UserMapper.getUser", 1);
				System.out.println(user.getId() + "," + user.getName() + "," + user.getAge());
			} finally {
				session.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
