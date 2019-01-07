package cn.tedu.store.controller;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.tedu.store.entity.User;
import cn.tedu.store.service.IUserService;
import cn.tedu.store.util.ResponseResult;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
	
	@Autowired
	private IUserService userService;
	
	@PostMapping("/reg.do")
	public ResponseResult<Void> handleReg(
			User user) {
		userService.reg(user);
		return new ResponseResult<Void>(SUCCESS);
	}

	@PostMapping("/login.do")
	public ResponseResult<Void> handleLogin(
		@RequestParam("username") String username,
		@RequestParam("password") String password,
		HttpSession session) {
		// 执行登录
		User user
			= userService.login(username, password);
		// 将相关信息存入到Session
		session.setAttribute("uid", user.getId());
		session.setAttribute("username", user.getUsername());
		// 返回
		return new ResponseResult<>(SUCCESS);
	}
	
	@RequestMapping("/password.do")
	public ResponseResult<Void> changePassword(
		@RequestParam("old_password") String oldPassword,
		@RequestParam("new_password") String newPassword,
		HttpSession session) {
		// 获取当前登录的用户的id
		Integer uid = getUidFromSession(session);
		// 执行修改密码
		userService.changePassword(
				uid, oldPassword, newPassword);
		// 返回
		return new ResponseResult<>(SUCCESS);
	}
	
	@RequestMapping("/info.do")
	public ResponseResult<User> getInfo(
		HttpSession session) {
		// 获取当前登录的用户的id
		Integer id = getUidFromSession(session);
		// 执行查询，获取用户数据
		User user = userService.getById(id);
		// 返回
		return new ResponseResult<User>(SUCCESS, user);
	}
	
	@PostMapping("/change_info.do")
	public ResponseResult<Void> changeInfo(
	    User user, HttpSession session) {
		// 获取当前登录的用户的id
		Integer id = getUidFromSession(session);
		// 将id封装到参数user中，因为user是用户提交的数据，并不包含id
		user.setId(id);
		// 执行修改
		userService.changeInfo(user);
		// 返回
		return new ResponseResult<>(SUCCESS);
	}
	
	@PostMapping("/upload.do")
	public ResponseResult<String> handleUpload(
			HttpSession session,
			@RequestParam("file")MultipartFile file){
		String parentPath = session.getServletContext().getRealPath("upload");
		File parent = new File(parentPath);
		if(!parent.exists()){
			parent.mkdirs();
		}
		
		String orginalFileName = file.getOriginalFilename();
		int beginIndex = orginalFileName.lastIndexOf(".");
		String suffix = orginalFileName.substring(beginIndex);
		String fileName = System.currentTimeMillis()+""+(new Random().nextInt(900000)+100000)+suffix;
		
		File dest = new File(parent,fileName);
		
		try {
			file.transferTo(dest);
			System.out.println("上传完成!");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}







