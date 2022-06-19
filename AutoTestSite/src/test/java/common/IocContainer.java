package common;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class IocContainer {

  static Map<Class<?>, Object> container = new ConcurrentHashMap<>();
  static ReentrantLock lock = new ReentrantLock();

  public static Object getInstance(Class<?> aClass) {
    lock.lock();
    Object returnObject;
    if (container.containsKey(aClass)) {
      returnObject = container.get(aClass);
    }
    else {
      returnObject = createInstance(aClass);
    }
    lock.unlock();
    return returnObject;
  }

  public static Object createInstance(Class<?> aClass) {
    try {
      return aClass.getConstructor().newInstance();
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
