https://r12a.github.io/apps/conversion/

Character reference is not like character encoding. 

Both Unicode and ACSII are character encodings. Unicode got different versions including utf-8 and utf-16. UTF-8 is the most commonly used character encoding. 

Character reference is for xml files like html. Decimal Numerical Character Reference, or decimal NCR is widely used and best supported by email servers. 

Note that, utf-8 is good enough for http contents. However, stmp, the email protocal, need character reference in some cases. For example, chinese characters in the html part of a
mime email. 

The methods below should accomplish the conversion between utf-8 and decimal NCR:
 
	public static String utf8ToDecimalNcr(String utf8String){
		return utf8String.chars().mapToObj(i->"&#"+i+"").collect(Collectors.joining(";"))+";";
	}
	public static String decimalNcrToUtf8(String decimalNcrString){
		return Arrays.stream(decimalNcrString.split(";|&#"))
				.filter(s->s!=null).filter(s->!s.isEmpty()).mapToInt(s->new Integer(s))
				.mapToObj(c->Character.toString((char)c)).reduce((s1,s2)->s1+s2).get();
	}