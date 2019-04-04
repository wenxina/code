package ${package_name}.utils;

/**
 * @author ${author}
 */
public class BeanUtils {
    public static void copyToBean(Object bean,Object beans){
        org.springframework.beans.BeanUtils.copyProperties(bean,beans);
    }
}
