package com.lagou.edu.factory;

import com.lagou.edu.annotation.CustomAutowired;
import com.lagou.edu.annotation.CustomService;
import com.lagou.edu.annotation.CustomTransactional;
import org.reflections.Reflections;
import org.springframework.util.StringUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 应癫
 */
public class BeanFactory {

    /**
     * 任务一：读取解析xml，通过反射技术实例化对象并且存储待用（map集合）
     * 任务二：对外提供获取实例对象的接口（根据id获取）
     */

    private static Map<String, Object> map = new HashMap<>();  // 存储对象

    static {
        try {
            parseServices();
            parseAutowired();
            parseTransactional();
        } catch (Exception e) {

        }
    }

    /**
     * 实例化
     * @throws Exception
     */
    public static void parseServices () throws Exception {
        Reflections reflections = new Reflections("com.lagou.edu");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(CustomService.class);
        for (Class<?> clazz : classes) {
            CustomService annotation = clazz.getAnnotation(CustomService.class);
            String name = annotation.value();
            if (StringUtils.isEmpty(name)) {
                String typeName = clazz.getTypeName();
                int lastIndex = typeName.lastIndexOf(".");
                name = toLowerCaseFirstOne(typeName.substring(lastIndex + 1));
            }
            map.put(name, clazz.newInstance());
        }
    }

    /**
     * 若为autowired修饰，则注入
     * @throws Exception
     */
    public static void parseAutowired () throws Exception {

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object obj = entry.getValue();
            Class clazz = obj.getClass();

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                boolean isAutowired = field.isAnnotationPresent(CustomAutowired.class) && field.getAnnotation(CustomAutowired.class).value();
                if (!isAutowired) {
                    continue;
                }
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    String fieldName = toLowerCaseFirstOne(field.getName());
                    if (method.getName().equalsIgnoreCase("set" + fieldName)) {
                        method.invoke(obj, map.get(fieldName));
                    }
                }

            }
        }
    }

    /**
     * 若为transactional修饰，则生成代理类
     * @throws Exception
     */
    public static void parseTransactional () throws Exception {
        ProxyFactory proxyFactory = (ProxyFactory) map.get("proxyFactory");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object obj = entry.getValue();
            Class clazz = obj.getClass();

            boolean trans = clazz.isAnnotationPresent(CustomTransactional.class);
            if (!trans) {
                continue;
            }
            map.put(entry.getKey(), proxyFactory.getJdkProxy(obj)) ;
        }
    }

    //首字母小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        }
        return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    // 任务二：对外提供获取实例对象的接口（根据id获取）
    public static  Object getBean(String id) {
        return map.get(id);
    }

}
