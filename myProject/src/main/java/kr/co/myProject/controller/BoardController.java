package kr.co.myProject.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import kr.co.myProject.service.BoardService;
import kr.co.myProject.vo.BoardVO;
import kr.co.myProject.vo.CommentVO;
import kr.co.myProject.vo.FileVO;
import kr.co.myProject.vo.LikeVO;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService service;
	
	@GetMapping({"board/list","board"})
	public String list(Model model, String pg, String sort, String searchName) {
		
		int currentPage = service.getCurrentPage(pg);
		int start = service.getLimitStart(currentPage);
		
		int total = service.selectCountTotal();
		int lastPageNum = service.getLastPageNum(total);
		int pageStartNum = service.getPageStartNum(total, start);
		int groups[] = service.getPageGroup(currentPage, lastPageNum);
		
		if("no".equals(sort) || sort == null) {
			List<BoardVO> lists = service.selectBoards(start);
			model.addAttribute("lists", lists);
		}else if("hit".equals(sort)){
			List<BoardVO> lists = service.selectSortByHit(start);
			model.addAttribute("lists", lists);
			model.addAttribute("sort",sort);
		}else if("like".equals(sort)){
			List<BoardVO> lists = service.selectSortByLike(start);
			model.addAttribute("lists", lists);
			model.addAttribute("sort",sort);
		}
		
		if(searchName != null) {
			List<BoardVO> lists = service.selectSearchName(searchName);
			model.addAttribute("lists", lists);
			model.addAttribute("searchName", searchName);
		}
		
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("lastPageNum", lastPageNum);
		model.addAttribute("pageStartNum", pageStartNum);
		model.addAttribute("groups", groups);

		return "board/list";
	}
	
	@GetMapping("board/write")
	public String board_write() {
		return "board/write";
	}
	
	@PostMapping("board/write")
	public String board_write(BoardVO bv, Authentication authentication) {
		
		String username = authentication.getName();
		
		bv.setBoard_writer(username);
		
		service.insertBoard(bv);
		
		return "redirect:/board";
	}
	
	@GetMapping("board/view")
	public String board_view(Model model, int no, Authentication authentication) {
		
		BoardVO article = service.selectBoard(no);
		
		String username = authentication.getName();
		
		int result = service.selectCountComment(no);
		
		int alreayArticleLike = service.selectAlreayDupBoardLike(no, username);
		
		if(result > 0) {
			List<CommentVO> comments = service.selectComment(no);
			
			model.addAttribute("comments", comments);
		}
		
		model.addAttribute("article", article);
		model.addAttribute("username", username);
		model.addAttribute("alreayArticleLike", alreayArticleLike);
		
		
		return "board/view";
	}
	
	@ResponseBody
	@GetMapping("board/modify")
	public String modify() {
		return "여긴 제작중이에요..";
	}
	
	@ResponseBody
	@GetMapping("board/like")
	public Map<String, Integer> like(@RequestParam("parent") int parent ,@RequestParam("username") String username, LikeVO lv, BoardVO vo) {
		
		Map<String, Integer> dup = new HashMap<>();
		Map<String, Integer> like = new HashMap<>();
		
		int duplication = service.selectDupBoardLike(parent, username);
		
		if(duplication > 0) {
			System.out.println("여기 확인...1");

			dup.put("duplication", duplication);
			
			service.deleteLikeCancel(parent, username);
			
			int board_like = service.selectCountLike(parent);
			
			vo.setBoard_like(board_like);
			vo.setBoard_no(parent);
			
			service.updateBoardLike(vo);
			
			System.out.println("vo.getBoard_like() : " + vo.getBoard_like());
			
			dup.put("like", vo.getBoard_like());
			
			return dup;
			
		}else {
			int result = service.insertLike(lv);
			
			int board_like = service.selectCountLike(parent);
			
			vo.setBoard_like(board_like);
			vo.setBoard_no(parent);
			
			service.updateBoardLike(vo);
			
			like.put("board_like", board_like);
			
			return like;
		}
	}
	
	@ResponseBody
	@GetMapping("board/likeCancel")
	public Map<String, Integer> likeCancel(@RequestParam("parent") int parent ,@RequestParam("username") String username, LikeVO lv, BoardVO vo) {
		
		Map<String, Integer> like = new HashMap<>();
		
		service.deleteLikeCancel(parent, username);
		
		int board_like = service.selectCountLike(parent);
		
		vo.setBoard_like(board_like);
		vo.setBoard_no(parent);
		
		service.updateBoardLike(vo);
		
		like.put("board_like", board_like);
		
		return like;
	}
	
	@ResponseBody
	@PostMapping("board/insertComment")
	public int board_Comment(@RequestParam("no") int no, 
								@RequestParam("content") String content, CommentVO cv, Authentication authentication) {
		
		String username = authentication.getName();
		
		cv.setUsername(username);
		
		int result = service.insertComment(cv);
		
		return result;
	}
	
	@GetMapping("download")
	public ResponseEntity<Resource> download(int fno) throws IOException{
		
		System.out.println("fno : " + fno);
		
		// 파일 조회
		FileVO fv = service.selectFile(fno);
		
		ResponseEntity<Resource> respEntity = service.fileDownload(fv);
		
		return respEntity;
	}
	
	@RequestMapping("/deleteFile")
	public Map<String, String> deleteFile(@RequestParam("fileName") String fileName) throws IOException {
        // 파일 위치를 지정합니다.
        String uploadPath = "src/main/resources/static/file";
        String folderPath = uploadPath;	
        
        File folder = new File(folderPath);
        if (!folder.exists()) {
        	Map<String, String> response = new HashMap<>();
            response.put("message", "폴더를 찾을 수 없습니다.");
            return response;
        }
        
        Path fileToDelete = Paths.get(folderPath, fileName);
        if (Files.exists(fileToDelete)) {
            Files.delete(fileToDelete);
            Map<String, String> response = new HashMap<>();
            response.put("message", "파일이 삭제되었습니다.");
            return response;
        }else {
        	Map<String, String> response = new HashMap<>();
            response.put("message", "파일을 찾을 수 없습니다.");
            return response;
        }
	}
}
