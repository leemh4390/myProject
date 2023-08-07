package kr.co.myProject.service;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.myProject.dao.BoardDAO;
import kr.co.myProject.vo.BoardVO;
import kr.co.myProject.vo.CommentVO;
import kr.co.myProject.vo.FileVO;
import kr.co.myProject.vo.LikeVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardService {
	
	@Autowired
	private BoardDAO dao;
	
	public int insertComment(CommentVO cv) {
		
		Timestamp currentDate = new Timestamp(System.currentTimeMillis());
		cv.setDate(currentDate);
		
		return dao.insertComment(cv);
	}
	
	public int insertBoard(BoardVO bv) {
		
	    Timestamp currentDate = new Timestamp(System.currentTimeMillis());
	    bv.setBoard_date(currentDate);
	    
		// 게시글 등록
		int result = dao.insertBoard(bv);
		
		// 파일 업로드
		FileVO fvo = fileUpload(bv);
		
		// 파일 등록
		if(fvo != null) {
			fvo.setRdate(currentDate);
			
			dao.insertFile(fvo);
		}
		return result;
	};
	
	public int insertLike(LikeVO lv) {
		
		return dao.insertLike(lv);
	}
	
	public List<BoardVO> selectBoards(int start) {
		return dao.selectBoards(start);
	};
	
	public List<BoardVO> selectSortByHit(int start) {
		return dao.selectSortByHit(start);
	};
	
	public List<BoardVO> selectSortByLike(int start) {
		return dao.selectSortByLike(start);
	};
	
	public List<BoardVO> selectSearchName(String searchName){
		return dao.selectSearchName(searchName);
	};
	
	public BoardVO selectBoard(int board_no) {
		return dao.selectBoard(board_no);
	};
	
	public int selectCountTotal() {
		return dao.selectCountTotal();
	}
	
	public int selectCountLike(int parent) {
		return dao.selectCountLike(parent);
	}
	
	public int selectDupBoardLike(int parent, String username) {
		return dao.selectDupBoardLike(parent, username);
	}
	
	public int selectAlreayDupBoardLike(int parent, String username) {
		return dao.selectAlreayDupBoardLike(parent, username);
	}
	
	public void updateBoardLike(BoardVO vo) {
		dao.updateBoardLike(vo);
	}
	
	public List<CommentVO> selectComment(int no) {
		return dao.selectComment(no);
	};
	
	public int selectCountComment(int no) {
		return dao.selectCountComment(no);
	};
	
	public FileVO selectFile(int fno) {
		return dao.selectFile(fno);
	}
	
	public String selectFiles(int no) {
		return dao.selectFiles(no);
	}
	
	public void deleteLikeCancel(int parent, String username) {
		dao.deleteLikeCancel(parent, username);
	}
	
	// 파일 업로드 설정
	@Value("${spring.servlet.multipart.location}")
	private String uploadPath;
	
	public ResponseEntity<Resource> fileDownload(FileVO fv) throws IOException{
		
		Path path = Paths.get(uploadPath+"/"+fv.getNewName());
		
		String ContentType = Files.probeContentType(path);
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentDisposition(ContentDisposition.builder("attachment")
				.filename(fv.getOriName(), StandardCharsets.UTF_8)
				.build());
		
		headers.add(HttpHeaders.CONTENT_TYPE, ContentType);
		
		Resource resource = new InputStreamResource(Files.newInputStream(path));
		
		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}
	
	// 파일 첨부 설정
	public FileVO fileUpload(BoardVO bv) {
		// 첨부 파일
		MultipartFile file = bv.getBoard_fname();
		FileVO fvo = null;
		
		if(!file.isEmpty()) {
			// 시스템 경로
			String path = new File(uploadPath).getAbsolutePath();
			
			// 새 파일명 생성
			String oName = file.getOriginalFilename();
			String ext = oName.substring(oName.lastIndexOf("."));
			String nName = UUID.randomUUID().toString()+ext;
			
			// 파일 저장
			try {
				file.transferTo(new File(path, nName));
			} catch (IllegalStateException e) {
				log.error(e.getMessage());
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			
			fvo = FileVO.builder()
					.parent(bv.getBoard_no())
					.oriName(oName)
					.newName(nName)
					.build();
		}
		
		return fvo;
	}
	
	// 현재 페이지 번호
	public int getCurrentPage(String pg) {
		
		// 시작 페이지는 1
		int currentPage = 1;
		
		if(pg != null) {
			currentPage = Integer.parseInt(pg);
		}
		
		return currentPage;
	}
	
	// 페이지 시작 값
	public int getLimitStart(int currentPage) {
		
		return (currentPage - 1) * 7;
	}
	
	// 마지막 페이지 번호
	public int getLastPageNum(int total) {
		
		int lastPageNum = 0;
		
		if(total % 7 == 0) {
			// 토탈 갯수가 10 으로 나눠서 나머지가 0이면 페이지 번호는 10 으로 나눈 값
			lastPageNum = total / 7;
		}else {
			// 나머지가 0 이 아니라면 + 1
			lastPageNum = total / 7 + 1;
		}
		return lastPageNum;
	}
	
	public int getPageStartNum(int total, int start) {
		// 게시글은 역순으로 나타내야함
		return total - start;
	}
	
	// 페이지 그룹
	public int[] getPageGroup(int currentPage, int lastPageNum) {
		
		int groupCurrent = (int) Math.ceil(currentPage / 5.0);
		int groupStart = (currentPage - 1) / 5 * 5 + 1;
		int groupEnd = groupStart + 4;
		
		if(groupEnd > lastPageNum) {
			groupEnd = lastPageNum;
		}
		
		int[] groups = {groupStart, groupEnd};
		
		return groups;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
