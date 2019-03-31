package com.my.batis;

import com.my.batis.bean.User;
import com.my.batis.dao.UserMapper;
import com.my.batis.sqlsession.DaoFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyMybatisApplicationTests {

	@Test
	public void contextLoads() throws IOException {
        DaoFactory<UserMapper> daoFactory = new DaoFactory<>();
        UserMapper userMapper = daoFactory.bind(UserMapper.class);
//        System.out.println(userMapper.getClass());
       /* List<User> users = userMapper.findAll();
        System.out.println(users);
        users = userMapper.getUser(1);
        System.out.println(users);*/

        User user = new User();
        user.setName("湾湾二号");
        user.setAge(21);
//        user.setId(3);
        Integer i = userMapper.insertUser(user);
        System.out.println("插入用户影响的行数：" + i);

//        Integer integer = userMapper.deleteByUserById(4);
//        System.out.println("插入用户影响的行数：" + integer);
    }
}
