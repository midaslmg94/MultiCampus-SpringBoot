package springboot.domain.posts;

import static org.junit.Assert.*;

import org.junit.Test;

public class PostsTest {

	@Test
	public void test() {
		// Posts posts = new Posts("내용", "제목", "작성자");
		Posts posts = Posts.builder().author("작성자").content("내용").title("제목").build();
		assertEquals("제목", posts.getTitle());
	}

}
