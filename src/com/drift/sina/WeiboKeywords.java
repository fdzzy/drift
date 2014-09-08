package com.drift.sina;

import java.util.ArrayList;
import java.util.List;

import com.gy.ke.bean.Keyword;
import com.gy.ke.main.KeyExtract;

import weibo4j.Timeline;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

public class WeiboKeywords {
	
	//public static void getWeiboKeywords(String access_token) {
	public static List<Keyword> getWeiboKeywords(String libraryPath, String access_token) {
		Timeline tm = new Timeline();
		tm.client.setToken(access_token);
		
		List<String> weibos=new ArrayList<String>();
		
		try {
			//StatusWapper status = tm.getUserTimeline();
			StatusWapper status = tm.getUserTimelineByCount(100);
			for(Status s : status.getStatuses()){
				//Log.logInfo(s.toString());
				//System.out.println(s.toString());
				// add very weibo text
				String weibo = s.getText();
				System.out.println(weibo);
				// TODO: Filter out some useless information from this weibo, for example, other people's id
				weibos.add(weibo);
			}
			System.out.println(status.getNextCursor());
			System.out.println(status.getPreviousCursor());
			System.out.println(status.getTotalNumber());
			System.out.println(status.getHasvisible());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		
		System.out.println(KeyExtract.getKeys(libraryPath, weibos));
		return KeyExtract.getKeys(libraryPath, weibos);
	}
}
