package kr.co.myProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.myProject.entity.User;

// CRUD 함수를 JpaRepository 가 들고 있음.
// Repository 라는 어노테이션이ㅣ 없어도 IoC 가 됨. 이유는 JpaRepository 를 상속했기 때문에
public interface UserRepository extends JpaRepository<User, Integer>{

	public User findByUsername(String username);
}
