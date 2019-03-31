package com.my.batis.sqlsession;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

public class DaoFactory<T> {

    private static final SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();

    public T bind(Class clazz) {
        return (T) this.newProxyInstance(clazz);
    }

    private Object newProxyInstance(Class<T> clazz){
        Method[] methods = clazz.getMethods();
        List<Method> methodList = Arrays.asList(methods);
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object result = null;
                SqlSessionFactory builder = sqlSessionFactoryBuilder.builder();
                SqlSession sqlSession = builder.openSession();
                try{
                    for (Method m : methodList){
                        if (m.getName().equals(method.getName())){
                            String key = method.getDeclaringClass().getName() + "." + method.getName();
                            Object arg = null;
                            if (args != null){
                                 arg = args[0];
                            }
                            result = sqlSession.executor(key,arg);
                        }
                    }
                }finally {
                    sqlSession.close();
                }
                return result;
            }
        });
    }
}
