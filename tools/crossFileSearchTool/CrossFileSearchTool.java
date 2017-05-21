import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class CrossFileSearchTool{
	
	public static void main(String ... args)throws Exception{
			
		Path start = Paths.get("C:","SpringWorkSpace","zhiyuninfo");
		
		
		
		
		Files.walk(start, FileVisitOption.FOLLOW_LINKS).filter(path->(!Files.isDirectory(path,LinkOption.NOFOLLOW_LINKS))).map(path-> {
		
			try{
				return Files.lines(path).filter(str->str.trim().length()>2).filter(str->(str!=null)&&!(str.trim().isEmpty())).map(str->str.trim()+"  \n(from "+path+")"+"\n\n").collect(Collectors.toList());
			}catch(Exception e){
				//System.out.println(e);
				return Arrays.asList(new String[]{"dd"});
			}
			
			
		})
		
		.flatMap(list->list.stream())
		//.parallel()
		.filter(str->str.toLowerCase().contains("change the heart beat rate from"))
		.filter(str->!(str.trim().isEmpty()))
		.filter(str-> str!=null)
		.forEach(System.out::println);
		
	}
}