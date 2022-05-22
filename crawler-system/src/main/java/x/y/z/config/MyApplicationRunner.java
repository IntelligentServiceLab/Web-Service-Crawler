package x.y.z.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 应用程序启动的时候，删除session_user的表（这个方法实在应用启动之后立即执行）
 */
@Component
public class MyApplicationRunner implements ApplicationRunner {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void run(ApplicationArguments args){
        Set<String> keys = stringRedisTemplate.keys("*");//清空redis数据库中所有的键值对
        stringRedisTemplate.delete(keys);
        System.out.println("缓存全部删除了");
    }
}
