package springboot.web;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import springboot.config.oauth.dto.SessionUser;
import springboot.service.PostsService;
import springboot.web.dto.PostsResponseDto;

@RequiredArgsConstructor
@Controller
public class IndexController {

	private final PostsService postsService;
	private final HttpSession httpSession;
	
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("postsList", postsService.findAllDesc());
		
		// 로그인 한 사용자이면 화면에 userName을 전달 -> 세션 유무로 판단
		SessionUser user =(SessionUser)httpSession.getAttribute("user");
		if(user!=null) {
			model.addAttribute("LoginUserName", user.getName());
			if(user.getPicture()==null) {
				// 이미지가 없는경우 기본 이미지 찍어줌
			}
			model.addAttribute("LoginUserPicture", user.getPicture());
		}
		
		return "index";    
	}
	
	@GetMapping("/posts/save")
	public String postsSave() {
		return "posts-save";
	}
	
	@GetMapping("/posts/update/{id}")
	public String postsUpdate(@PathVariable Long id, Model model) {		
		PostsResponseDto dto = postsService.findById(id);
		model.addAttribute("post", dto);
		return "posts-update";
	}
}
