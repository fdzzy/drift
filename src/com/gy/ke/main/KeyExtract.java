package com.gy.ke.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import com.gy.ke.bean.Keyword;
import com.gy.ke.bean.ReadText;
import com.gy.ke.pre.PreProcess;

public class KeyExtract {
	private static Map<String,Float> idf=null;
	private static Map<String, Float> POS_SCORE = new HashMap<String, Float>();
	private static int nKeyword =500;
	private static boolean initialized = false;
	private static Lock lock = new ReentrantLock();
	
	static {
		//各个词性的权值，词性说明见：http://ansjsun.github.io/ansj_seg/
		
		
		POS_SCORE.put("n", 1.0f);//名词
		POS_SCORE.put("ns", 0.8f);//地名
		POS_SCORE.put("nr", 0.5f);//人名
		POS_SCORE.put("nrf", 0.5f);//音译人名
		POS_SCORE.put("nw", 0.1f); //新词
		POS_SCORE.put("nt", 0.5f);//机构团体名
		POS_SCORE.put("nz", 0.2f);//其它专名
		
		POS_SCORE.put("t", 0.1f);//时间词
		
		
		POS_SCORE.put("r", 0.1f);//代词
		POS_SCORE.put("vi", 0.7f);//不及物动词
		POS_SCORE.put("vn", 0.8f);//名动词
		
		POS_SCORE.put("en", 0.1f);//英文
		
		POS_SCORE.put("m", 0.1f);//数量词
		
	}
	
	public static void init(String libraryPath){
		
		if(initialized) return;
		
		lock.lock();
		
		// Double check Singleton
		if(initialized) {
			lock.unlock();
			return;
		}
		
		//加载idf，此idf数据统计于真实微博数据，已经去除低频词
		//if(idf!=null)
		//	return;
		idf=new HashMap<String,Float>();
		ReadText r=new ReadText(libraryPath + "library/idf.txt");
		String line=null;
		while((line=r.readLine())!=null){
			String[] t = line.split("\t");
			idf.put(t[0], Float.valueOf(t[1]));
		}
		 
		initialized = true;
		lock.unlock();
	}
	
	private static String regEx = "[^\u4e00-\u9fa50-9a-zA-Z]+";//非汉字  字母  数字
    private static Pattern p = Pattern.compile(regEx);
    
	private static boolean filter(String str){
		//过滤一些不需要的词
		if(str.length()<2)
			return true;
		Matcher m=p.matcher(str);
		if(m.find())
			return true;
		return false;
	}
	
	/**
	 * 返回关键词
	 * @param weibos
	 * @return
	 */
	public static List<Keyword> getKeys(String libraryPath, List<String> weibos){
		return getKeys(libraryPath, weibos,nKeyword);
	}
	
	/**
	 * 
	 * @param weibos 
	 * @param nKeyword 返回关键词数
 	 * @return
	 */
	public static List<Keyword> getKeys(String libraryPath, List<String> weibos,int nKeyword){

		init(libraryPath);
		
		Map<String,Keyword> kws=new HashMap<String,Keyword>();
		for(String weibo:weibos){
			List<Term> terms = ToAnalysis.parse(PreProcess.pre(weibo));
			
			for(Term term:terms){
				//filter
				if(filter(term.getName()))
					continue;
				
				
				Keyword kw=kws.get(term.getName());
				if(kw==null){
					kws.put(term.getName(), 
							new Keyword(term.getName(),
									term.natrue().natureStr,
									idf.get(term.getName()),
									POS_SCORE.get(term.natrue().natureStr))
									);
				}else{
					kw.updateScore();
				}
			}
		}
		
		TreeSet<Keyword> treeSet = new TreeSet<Keyword>(kws.values());

		ArrayList<Keyword> arrayList = new ArrayList<Keyword>(treeSet);
		if (treeSet.size() <= nKeyword) {
			return arrayList;
		} else {
			return arrayList.subList(0, nKeyword);
		}
		
	}
	
	
}
