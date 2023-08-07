package kr.co.myProject.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.myProject.vo.BoardVO;
import kr.co.myProject.vo.CommentVO;
import kr.co.myProject.vo.FileVO;
import kr.co.myProject.vo.LikeVO;

@Mapper
@Repository
public interface BoardDAO {
	public int insertBoard(BoardVO vo);
	public int insertComment(CommentVO cv);
	public int insertFile(FileVO fv);
	public int insertLike(LikeVO lv);
	public List<BoardVO> selectBoards(int start);
	public List<BoardVO> selectSortByHit(int start);
	public List<BoardVO> selectSortByLike(int start);
	public List<BoardVO> selectSearchName(String searchName);
	public BoardVO selectBoard(int board_no);
	public int selectCountTotal();
	public List<CommentVO> selectComment(int no);
	public int selectCountComment(int no);
	public int selectCountLike(int parent);
	public int selectDupBoardLike(int parent, String username);
	public int selectAlreayDupBoardLike(int parent, String username);
	public FileVO selectFile(int fno);
	public String selectFiles(int fno);
	public void updateBoardLike(BoardVO vo);
	public void updateBoard(BoardVO vo);
	public void updateHit(BoardVO vo);
	public void deleteBoard(int board_no);
	public void deleteArticle(int no);
	public void deleteFile(int no);
	public void deleteLikeCancel(int parent, String username);
}
