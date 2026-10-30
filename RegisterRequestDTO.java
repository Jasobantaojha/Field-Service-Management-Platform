package com.KeyStone.FieldServiceProject.DTO;

import com.KeyStone.FieldServiceProject.ENUM.Role;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {
	
	public String userName;
	public String userEmail;
	public String password;
	public String phone;
	public Role role;
	

}