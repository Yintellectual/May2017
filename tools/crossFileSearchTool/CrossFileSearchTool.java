import java.nio.file.*;
import java.util.*;
import java.util.stream.*;


public class CrossFileSearchTool{
	
	public static void main(String ... args)throws Exception{

		Path path1 = Paths.get("C:","Users","admin","temp","zhiyuninfo","serviceportal");
		//Path path2 = Paths.get("C:","SpringWorkSpace","zhiyuninfo","deviceservice");
		
		
		
		//searchDirectory(path2, "@RequestMapping").stream().forEach(System.out::println);
		searchDirectory(path1, "device.name").stream().forEach(System.out::println);
		
		
		
		

		
	}
	
	private static List<String> searchDirectory(Path start, String targetString)throws Exception{
			
		final String TARGET_STRING_IN_LOWER_CASE = targetString.toLowerCase();
	
	
	return 
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
		.filter(str->str.toLowerCase().contains(TARGET_STRING_IN_LOWER_CASE))
		.filter(str->!(str.trim().isEmpty()))
		.filter(str-> str!=null)
		.collect(Collectors.toList());
	}
}