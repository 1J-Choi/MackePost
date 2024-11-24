package com.markepost.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Component // Spring bean에 올려야하는데 애매할 때 쓰는 어노테이션
@Slf4j
public class FileManagerService {
	// 실제 업로드 된 이미지가 저장될 서버 경로
	public static final String FILE_UPLOAD_PATH = "C:\\Users\\최원제\\Desktop\\WJ\\공부\\코딩\\백엔드\\학원\\신보람\\7_개인_프로젝트\\markepost\\markepost_workspace\\images/";
	
	// input: MultipartFile, userLoginId
		// output: String(이미지 경로)
		public String uploadFile(MultipartFile file, String loginId) {
			// 폴더(디렉토리) 생성
			// ex: aaaa_17237482334/sun.png
			String directoryName = loginId + "_" + System.currentTimeMillis(); // aaaa_17237482334
			// C:\\Users\\최원제\\Desktop\\WJ\\공부\\코딩\\백엔드\\학원\\신보람\\6_spring_project\\memo\\memo_workspace\\images/aaaa_17237482334/
			String filePath = FILE_UPLOAD_PATH + directoryName + "/";
			
			// 폴더 생성
			File directory = new File(filePath);
			if (directory.mkdir() == false) {
				return null; // 폴더 생성시 실패하면 경로를 null로 리턴(에러 아님)
			}
			
			// 파일 업로드 (오류가 발생하더라도 null이 BO에게 반환되어야 한다)
			try {
				byte[] bytes = file.getBytes();
				// ★★★★★ 나중에 파일명을 영문자로 변경할 것!(한글명은 업로드 불가) ★★★★★
				Path path = Paths.get(filePath + file.getOriginalFilename());
				Files.write(path, bytes);
			} catch (IOException e) {
				e.printStackTrace();
				return null; // 이미지 업로드 시 실패하면 null로 리턴(에러 아님)
			}
			
			// 파일 업로드가 성공하면 이미지 url path 리턴
			// 주소는 이렇게 될 것이다.(예언)
			// 		/images/aaaa_17237482334/sun.png
			return "/images/" + directoryName + "/" + file.getOriginalFilename();
		}
		
		public void deleteFile(String image) {
			Path path = Paths.get(FILE_UPLOAD_PATH + image.replace("/images/", ""));
			
			// 삭제할 이미지가 있는가?
			if(Files.exists(path)) {
				// 이미지 파일 삭제
				try {
					Files.delete(path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.info("[#####파일매니저 파일 삭제#####] imagePath:{}", image);
					return; // 이미지 파일에 실패하였으므로 폴더 실패 생략
				}
				
				// 폴더(directory) 삭제
				path = path.getParent(); // 부모 요소로 이동 => directory
				if(Files.exists(path)) {
					try {
						Files.delete(path);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						log.info("[#####파일매니저 directory 삭제#####] imagePath:{}", image);
					}
				}
			}
		}
}
