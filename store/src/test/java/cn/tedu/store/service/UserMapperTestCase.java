package cn.tedu.store.service;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.tedu.store.entity.User;
import cn.tedu.store.service.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTestCase {

	@Autowired
	private IUserService userService;

	@Test
	public void addnew(){
		try {
            Date now = new Date();
            User user = new User();
            user.setUsername("admin");
            user.setPassword("1234");
            user.setGender(1);
            user.setPhone("13800138002");
            user.setEmail("admin@tedu.cn");
            
            User result = userService.reg(user);
            System.err.println("result=" + result);
        } catch (ServiceException e) {
            System.err.println("错误类型：" + e.getClass().getName());
            System.err.println("错误描述：" + e.getMessage());
        }
	}
	
}
