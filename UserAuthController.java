package com.KeyStone.FieldServiceProject.Controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.KeyStone.FieldServiceProject.DTO.AuthResponseDTO;
import com.KeyStone.FieldServiceProject.DTO.LoginRequestDTO;
import com.KeyStone.FieldServiceProject.DTO.RegisterRequestDTO;
import com.KeyStone.FieldServiceProject.Service.UserAuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user_auth")
@RequiredArgsConstructor
public class UserAuthController {
	
	@Autowired
	private UserAuthService userAuthService;
	
	
	@PostMapping("/register")
	public ResponseEntity<String>register(@RequestBody RegisterRequestDTO  register){
		return ResponseEntity.ok(userAuthService.register(register));
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO>login(@RequestBody LoginRequestDTO login){
		
		return ResponseEntity.ok(userAuthService.login(login));
	}
	
	@PostMapping("/forgotPassword")
	public ResponseEntity<String>forgotPassword(@PathVariable String userEmail){
		userAuthService.forgotPassword(userEmail);
		
		return ResponseEntity.ok("reset mail sent on your Email");
		
	}
	
	@PostMapping("/resetPassword")
	public ResponseEntity<String>resetPassword(@RequestParam String token,
			                                @RequestParam String newPassword ){
		userAuthService.resetPassword(token, newPassword);
		
		return ResponseEntity.ok("Password reset successfully");
	}
	@PostMapping("loggedOut")
	public ResponseEntity<String>loggedOut(HttpServletRequest request){
		   return ResponseEntity.ok(userAuthService.logout(request));
	   }

}
