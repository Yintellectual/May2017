<<<<<<< HEAD
Suppose a POJO PojoExample is to be validated. 

1. in PojoExample.java, 

		

2. in controller

	the bottom line is to make sure both models exist:
	
		1. "": PojoExample
		
		2. "": ResultBinding
		
	Ususally it can be simply done by (@Valid PojoExample, ResultBinding)

3. in JSP, both spring form taglib and themeleaf taglib can do the job. 
=======
spring validation is some old thing that works with JSP. 

To use spring validation:

First the validation rules should be specified. 
Second, the validation should be performed and the Errors should be produced.
Third, the errors should be passed to the JSP and be consumed by the spring form errors tag.

Part one, validation rules in POJO

	import javax.validation.constraints.NotNull;
	import javax.validation.constraints.Size;
	
	@NotNull
	@Size(min=1,max=20,message="{devicetype.name}")
	private String name;
	
	the message can be provided in a property bundle, like "ValidationMessages_zh.properties in webapp/WEB-INF/classes"
	devicetype.name=\u8BBE\u5907\u7C7B\u578B\u540D\u79F0\u7684\u957F\u5EA6\u5FC5\u987B\u5927\u4E8E{min}\u5C0F\u4E8E{max}\u3002

Part two, in the controller that handles the submission, 

	import javax.validation.Valid;
	import org.springframework.validation.Errors;
	
	public String createDeviceType(@Valid DeviceType deviceType, Errors errors, HttpServletRequest request,
			RedirectAttributes attr,Model model)

	if (errors.hasErrors()) 
	errors.getFieldError("name")
	errors.rejectValue("name", "duplicate.user", "登录名\"" + user.getName() + "\"已使用，请更换新的登录名。")
	
Part three, in the JSP file

	<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
	<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
	<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
	
	<sf:errors path="name" element="div" cssClass="errors" />
	
	Note that, sf:errors reads "path='name'" according to 'commandName' in sf:form. The commandName must be same with the POJO type name. 
	
	If the errors tag just ignores errors, then the commandName must be wrong
	
	Also, return "redirect:/blabla" won't support spring validation.
>>>>>>> f14c345b6de5f65ece4ce973e0715c07f1598904
