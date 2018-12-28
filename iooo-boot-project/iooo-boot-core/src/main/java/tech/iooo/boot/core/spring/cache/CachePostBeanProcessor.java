package tech.iooo.boot.core.spring.cache;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Created on 2018-12-10 14:06
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
@Component
public class CachePostBeanProcessor implements BeanPostProcessor {

  @Override
  public Object postProcessAfterInitialization(@NonNull Object bean, String beanName) throws BeansException {
    if (bean.getClass().isAnnotationPresent(CacheProxy.class)) {
      //如果是代理的类
      Enhancer enhancer = new Enhancer();
      enhancer.setSuperclass(bean.getClass());
      enhancer.setCallback(new CacheMethodInterceptor(bean));
      return enhancer.create();
    } else {
      return bean;
    }
  }

  @Override
  public Object postProcessBeforeInitialization(@NonNull Object bean, String beanNames) throws BeansException {
    return bean;
  }
}