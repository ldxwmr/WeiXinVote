package com.neuedu.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neuedu.model.po.Candidate;
import com.neuedu.model.po.LW;
import com.neuedu.model.service.VoteService;

@Controller
public class DetailsController {
	
	@Autowired
	private VoteService voteService;
	
	@RequestMapping(value="/candidate/{aid}/{cid}", method=RequestMethod.GET)
	@ResponseBody
	public Candidate getCandidateById(@PathVariable int aid, @PathVariable int cid)
	{
		//更新数据
		voteService.updateCandidate(aid, cid);
		
		Candidate c = voteService.getCandidateById(cid);
		
		return c;
	}
	
	@RequestMapping(value="/candidate/{aid}/{cid}", method=RequestMethod.PUT)
	@ResponseBody
	public String vote(@PathVariable int aid, @PathVariable int cid, HttpServletRequest request)
	{
		String str ="";
		//1. 判断是否能投票
		if(voteService.canVote(aid))
		{
			//2. 如果能投票，调用投票方法
			//2.1 获取客户端ip地址
			String ip = request.getRemoteAddr();
			voteService.updateVoteInfos(aid, cid, ip);
			str = "{\"result\":\"投票成功\"}";
		}
		else
		{
			//反馈信息
			str = "{\"result\":\"活动已经结束，不能再投票\"}";
		}
		
		return str;		
	}
	
	@RequestMapping(value="/lws", method=RequestMethod.GET)
	@ResponseBody
	public List<LW> getAllLW()
	{
		return voteService.getAllLW();
	}
	
	@RequestMapping(value="/candidate/lw/{aid}/{cid}/{lwid}/{lwcount}", method=RequestMethod.PUT)
	@ResponseBody
	public Candidate addLW(@PathVariable int aid, @PathVariable int cid, @PathVariable int lwid, @PathVariable int lwcount, HttpServletRequest request)
	{
		//得到当前用户的ip
		String openid = request.getRemoteAddr();
		
		voteService.updateCandidateGifts(aid, cid, lwid, lwcount, openid);
		
		return voteService.getCandidateById(cid);
		
	}

}
