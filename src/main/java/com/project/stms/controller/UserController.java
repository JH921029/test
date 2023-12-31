package com.project.stms.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.stms.command.UserVO;
import com.project.stms.service.user.UserService;

@Controller
public class UserController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserService userService;



	@GetMapping("/joinUser")
	public String joinUser() {
		return "user/joinUser";
	}

	@GetMapping("/joinEngineer")
	public String joinEngineer() {
		return "user/joinEngineer";
	}

	@PostMapping("/joinUserForm")
	public String joinUserForm(@Valid @ModelAttribute("userVO") UserVO userVO, Errors errors, Model model) {

		
		if(errors.hasErrors()) { //에러가 있다면 true, 없다면 false
			//1. 유효성 검사에 실패한 에러 확인
			List<FieldError> list = errors.getFieldErrors();
			
			//2. 반복처리
			for(FieldError err : list ) {
				//System.out.println(err);
				//System.out.println(err.getField()); //에러가 난 필드명
				//System.out.println(err.getDefaultMessage()); //메시지 출력
				//System.out.println(err.isBindingFailure()); //유효성검사에 의해서 err라면 false, 아니라면 true반환
				
				if(err.isBindingFailure()) {
					model.addAttribute("valid_" + err.getField() , "잘못된 값 입력입니다" );
				} else {
					model.addAttribute("valid_" + err.getField() , err.getDefaultMessage());								
				}
			}
			
			return "/user/joinUser"; //실패시 원래 화면으로
			
		} //err end
		
		
		//비번 단방향 암호화
		String pw = bCryptPasswordEncoder.encode(userVO.getUser_pw());
		userVO.setUser_pw(pw);

		userService.join(userVO);


		return "redirect:/login";
	}
	
	@PostMapping("/joinEngineerForm")
	public String joinEngineerForm(@Valid @ModelAttribute("userVO") UserVO userVO, Errors errors, Model model) {

		
		if(errors.hasErrors()) { //에러가 있다면 true, 없다면 false
			//1. 유효성 검사에 실패한 에러 확인
			List<FieldError> list = errors.getFieldErrors();
			
			//2. 반복처리
			for(FieldError err : list ) {
				//System.out.println(err);
				//System.out.println(err.getField()); //에러가 난 필드명
				//System.out.println(err.getDefaultMessage()); //메시지 출력
				//System.out.println(err.isBindingFailure()); //유효성검사에 의해서 err라면 false, 아니라면 true반환
				
				if(err.isBindingFailure()) {
					model.addAttribute("valid_" + err.getField() , "잘못된 값 입력입니다" );
				} else {
					model.addAttribute("valid_" + err.getField() , err.getDefaultMessage());								
				}
			}
			
			return "/user/joinEngineer"; //실패시 원래 화면으로
			
		} //err end
		
		
		//비번 단방향 암호화
		String pw = bCryptPasswordEncoder.encode(userVO.getUser_pw());
		userVO.setUser_pw(pw);

		userService.join(userVO);


		return "redirect:/login";
	}

	@GetMapping("/login")
	public String login(@RequestParam (value = "err", required = false)String err, Model model) {
		
		if(err != null) {
			model.addAttribute("msg", "아이디 비밀번호를 확인하세요");
		}
		
		return "user/login";
	}
	
	@GetMapping("/findID")
	public String findID() {
		return "user/findID";
	}
	
	@GetMapping("/findPW")
	public String findPW() {
		return "user/findPW";
	}
	
	
	@PostMapping("/changePW")
	public String changePW(UserVO userVO) {
		System.out.println(userVO.toString());
		
		String pw = bCryptPasswordEncoder.encode(userVO.getUser_pw());
		userVO.setUser_pw(pw);
		userService.changePW(userVO);
		return "redirect:/login";
	}
	

}
