package com.search.images;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchImages {
	public   static   final   Pattern   PATTERN   =   Pattern.compile("<img\\s+(?:[^>]*)src\\s*=\\s*([^>]+)",   Pattern.CASE_INSENSITIVE   |   Pattern.MULTILINE);  
	static StringBuffer buffer = new StringBuffer();
	static StringBuffer cssBuffer = new StringBuffer();
	static StringBuffer jsBuffer = new StringBuffer();
	static List<String> use = new ArrayList<String>();
	static List<String> alList = new ArrayList<String>();
	static List<String> delList = new ArrayList<String>();
	public static void main(String[] args) {
		  String pathname ="D:\\workspaceclouddev\\cloud-stmt\\velocityCatalog\\1418\\001\\pc";
		  String  cssPath="D:\\workspaceclouddev\\cloud-stmt\\WebContent\\style\\1418\\001\\pc";
		  String  jspath="D:\\workspaceclouddev\\cloud-stmt\\WebContent\\script\\1418\\001\\pc";
		  String  matchPathString="D:\\workspaceclouddev\\cloud-stmt\\WebContent\\images\\1418\\001\\pc";
		  File file  = new File(pathname);
		  File file2 = new File(cssPath);
		  File file3 = new File(matchPathString);
		  File file4 = new File(jspath);
		  try {
			search(file,1);
			//search(file4, 1);
			//System.out.println(buffer.toString());
			List<String> list = (List<String>)getImgSrc(buffer.toString());
			for(int i=0;i<list.size();i++){
			 int index=	list.get(i).lastIndexOf("/");
				//System.out.println(list.get(i).substring(index+1));
			    use.add(list.get(i).substring(index+1));
			}
			search(file2,2);
			
			search(file3, 3);
			for(String u:alList){
				 if(!use.contains(u)){
					//System.out.println(u); 
					delList.add(u);
					
				 }
			}
			search(file4, 4);
			for(String us:delList){
				if(!jsBuffer.toString().contains(us)){
					//System.out.println(us);
					File delFile = new File(matchPathString+File.separator+us);
					if(delFile.exists()){
						delFile.delete();
					}
				}
			}
			//System.out.println(cssBuffer.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void  search(File file,int s)throws Exception{
		
		if(!file.isDirectory()){
			  if(s==3){
				  alList.add(file.getName());
				  return;
			  }
		    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		    String  str =null;
		      while((str=reader.readLine())!=null){
		    	//  System.out.println(str);
		    	  if(str.contains(".jpg")||str.contains(".png")){
		    		 // System.out.println(str);
		    		  if(s==1){
		    			  buffer.append(str); 
		    		  }else if(s==2) {
						//cssBuffer.append(str);
		    			  int index=	str.lastIndexOf("/");
		    			  int last = str.lastIndexOf(")");
		  				//System.out.println(str.substring(index+1,last));
		    			  use.add(str.substring(index+1,last));
					}else {
						 //int index=	str.lastIndexOf("/");
		    			 // int last = str.lastIndexOf(".");
		  				//System.out.println(str.substring(index+1,last));
		    			//.add(str.substring(index+1,last+3));
		    			 // System.out.println(str);
		    			  jsBuffer.append(str);
					}
		    		 
		    	  }
		    	  
		      }
			//System.out.println(file.getPath());
			return ;
		}
		File[] files=  file.listFiles();
		for(int i=0;i<files.length;i++){
			
				 search(files[i],s);   
			   
		}
		
		   
	}
	 public   static   List<String>   getImgSrc(String   html)   {  
	        Matcher   matcher   =   PATTERN.matcher(html);  
	        List<String>   list   =   new   ArrayList<String>();  
	        while   (matcher.find())   {  
	            String   group   =   matcher.group(1);  
	            if   (group   ==   null)   {  
	                continue;  
	            }  
	            //   这里可能还需要更复杂的判断,用以处理src="...."内的一些转义符  
	            if   (group.startsWith("'"))   {  
	                list.add(group.substring(1,   group.indexOf("'",   1)));  
	            }   else   if   (group.startsWith("\""))   {  
	                list.add(group.substring(1,   group.indexOf("\"",   1)));  
	            }   else   {  
	                list.add(group.split("\\s")[0]);  
	            }  
	        }  
	        return   list;   
	 }

}
