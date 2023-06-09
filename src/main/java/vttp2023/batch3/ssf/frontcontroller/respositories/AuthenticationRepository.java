package vttp2023.batch3.ssf.frontcontroller.respositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp2023.batch3.ssf.frontcontroller.model.Login;

// TODO Task 5
	// Use this class to implement CRUD operations on Redis
@Repository
public class AuthenticationRepository {
	@Autowired

	private RedisTemplate<String, Object> template;

	public void save(Login login) {
		template.opsForValue().set(login.getUsername(), login.toJSON().toString());
	}

	


}
