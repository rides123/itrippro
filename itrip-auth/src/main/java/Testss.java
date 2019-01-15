import cn.itrip.auth.service.UserService;
import cn.itrip.beans.pojo.ItripUser;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Testss {
    @Test
    public void test(){
        ApplicationContext atx = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService userService = atx.getBean("userService",UserService.class);
        ItripUser user = new ItripUser();
        user.setUserCode("g591379654@163.com");
       
        try {
            userService.itriptxcreateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
