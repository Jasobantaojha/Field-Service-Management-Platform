package com.KeyStone.FieldServiceProject.Service;

import org.springframework.web.multipart.MultipartFile;


import com.KeyStone.FieldServiceProject.Entity.Attachment;


public interface AttachmentService {
	
	public Attachment upload(MultipartFile file, String folder);
	public Attachment getById(Long id);
	public void delete(Long id);

}
