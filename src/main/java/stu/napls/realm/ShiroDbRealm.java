package stu.napls.realm;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import stu.napls.model.User;
import stu.napls.service.UserService;
import stu.napls.utils.CipherUtil;

public class ShiroDbRealm extends AuthorizingRealm {

	@Resource
	private UserService userService;

	public ShiroDbRealm() {
		super();
	}

	/**
	 * ��֤��½
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		User user = userService.findUserByUsername(token.getUsername());
		if (user != null) {
			return new SimpleAuthenticationInfo(user.getUsername(), CipherUtil.generatePassword(user.getPassword()),
					getName());
		} else {
			throw new AuthenticationException();
		}
	}

	/**
	 * ��½�ɹ�֮�󣬽��н�ɫ��Ȩ����֤
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		/* ����Ӧ�ø���userNameʹ��role��permission ��serive�������жϣ�������Ӧ ��Ȩ�޼ӽ��������������һ�� */
		Set<String> roleNames = new HashSet<String>();
		Set<String> permissions = new HashSet<String>();
		String username = (String) getAvailablePrincipal(principals);
		System.out.println("==================================");
		System.out.println(username);
		roleNames.add("admin");// ��ӽ�ɫ����Ӧ��index.jsp
		roleNames.add("administrator");
		permissions.add("create");// ���Ȩ��,��Ӧ��index.jsp
		permissions.add("login?main");
		permissions.add("login?logout");
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
		info.setStringPermissions(permissions);
		return info;
	}

//	@Override
//	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//		if (principals == null) {
//            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
//        }
//        String username = (String) getAvailablePrincipal(principals);
//        User user = User.dao.getByUserName(username);
//        int roleid = user.getInt("role_id");
//        List<Permission> perms = Permission.dao.getPermissions(roleid);
//        Set<String> roleSet = new HashSet<String>();
//        roleSet.add(String.valueOf(roleid));
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleSet);
//        info.setStringPermissions(getPermissionSet(perms));
//		return info;
//	}
	
	
	/**
	 * ��������û���Ȩ��Ϣ����.
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * ��������û���Ȩ��Ϣ����.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

}
