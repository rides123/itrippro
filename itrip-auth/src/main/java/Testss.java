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
        user.setUserName("test");
        try {
            userService.itriptxcreateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test2(){
        ApplicationContext atx = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService userService = atx.getBean("userService",UserService.class);
        ItripUser user = new ItripUser();
        try {
            if(userService.activate("g591379654@163.com","587b631d1b1f4483c00826a9d70a2a09")==true){
                System.out.println("修改成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
