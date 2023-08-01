package kr.co.myProject.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.myProject.vo.UserVO;

@Mapper
@Repository
public interface UserDAO {
	
	public void insertUser(UserVO vo); 
}
