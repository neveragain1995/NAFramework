package stu.napls.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import stu.napls.model.User;
import stu.napls.utils.CipherUtil;

@Controller
public class LoginController {

	/**
	 * ��ʼ��½����
	 * 
	 * @param request
	 * @return String
	 */
	@RequestMapping("/login")
	public String tologin(HttpServletRequest request) {
		// System.out.println("����IP[" + request.getRemoteHost() + "]�ķ���");
		return "login";
	}

	/**
	 * ��֤�û���������
	 * 
	 * @param request
	 * @return String
	 */
	@RequestMapping("/checkLogin")
	public String login(@ModelAttribute("user") User user) {
		String result = "login";
		// ȡ���û���
		String username = user.getUsername();
		// ȡ�� ���룬����MD5����
		String password = CipherUtil.generatePassword(user.getPassword());

		UsernamePasswordToken token = new UsernamePasswordToken(username, password);

		Subject currentUser = SecurityUtils.getSubject();
		try {
			if (!currentUser.isAuthenticated()) {// ʹ��shiro����֤
				token.setRememberMe(true);
				currentUser.login(token);// ��֤��ɫ��Ȩ��
			}
			result = "index";// ��֤�ɹ�
		} catch (Exception e) {
			e.printStackTrace();
			result = "failure";// ��֤ʧ��
		}
		return result;
	}

	/**
	 * �˳�
	 * 
	 * @param
	 * @return String
	 */
	@RequestMapping(value = "/logout")
	@ResponseBody
	public String logout() {

		Subject currentUser = SecurityUtils.getSubject();
		currentUser.logout();
		return "logout";
		//TODO redirect
	}

}
