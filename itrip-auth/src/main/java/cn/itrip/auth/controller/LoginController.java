package cn.itrip.auth.controller;

import java.util.Calendar;

import cn.itrip.common.MD5;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import springfox.documentation.annotations.ApiIgnore;
import cn.itrip.auth.service.TokenService;
import cn.itrip.auth.service.UserService;
import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.beans.vo.ItripTokenVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.ErrorCode;

/**
 * 用户登录控制器
 * @author hduser
 *
 */
@Controller
@RequestMapping(value = "/api")
public class LoginController {
	@Resource
	private UserService userService;
	@Resource
	private TokenService tokenService;

	@RequestMapping(value="/dologin",method= RequestMethod.POST,produces= "application/json")
	public @ResponseBody Dto dologin(@RequestParam String name, @RequestParam String password, HttpServletRequest request) {
		try{
			ItripUser user = userService.login(name, MD5.getMd5(password,32));
			if (EmptyUtils.isNotEmpty(user)){
				String userAgent = request.getHeader("user-agent");
				String token = tokenService.generateToken(userAgent,user);
				tokenService.save(token,user);

				ItripTokenVO vo = new ItripTokenVO(token,
						Calendar.getInstance().getTimeInMillis()+2*60*60*1000,
						Calendar.getInstance().getTimeInMillis());
				return DtoUtil.returnDataSuccess(vo);
			}else {
				return DtoUtil.returnFail("用户密码错误", ErrorCode.AUTH_AUTHENTICATION_FAILED);
			}
		}catch (Exception e){
			e.printStackTrace();
			return DtoUtil.returnFail(e.getMessage(), ErrorCode.AUTH_AUTHENTICATION_FAILED);
		}
	}

	@RequestMapping(value="/logout",method=RequestMethod.GET,produces="application/json",headers="token")
	public @ResponseBody Dto logout(HttpServletRequest request){
		String token = request.getHeader("token");
		try{
			if (tokenService.validate(request.getHeader("user-agent"),token)){
				tokenService.delete(token);
				return DtoUtil.returnSuccess();
			}
			else
				return DtoUtil.returnFail("token无效",ErrorCode.AUTH_TOKEN_INVALID);
		}catch (Exception e){
			return DtoUtil.returnFail(e.getMessage(),ErrorCode.AUTH_TOKEN_INVALID);
		}
	}

}
