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



添加基于spring validation 的用户表单验证功能

Spring Validation 用法简介：

	spring validation 遵循mvc原则。 简单来讲， 当验证不通过时， 一个包含错误信息的属性被添加到model中。 
	这个错误信息属性的名字为“org.springframework.validation.BindingResult.<cmmandName>“， 而内容为一个Errors
	或BindingResult。 在view中， 这个model被commandName所对应的表单下的错误信息标签所解析并展示。 但是这里有个条件，
	那就是表单的commandName所对应的model必须存在。

	代码示例：
		controller中
		model.addAttribute("newItem", user);
		model.addAttribute("org.springframework.validation.BindingResult.newItem", errors);

		jsp中
		<sf:form method="post" id="newUserForm" commandName="newItem" role="form">
			<sf:errors path="*" element="div" cssClass="errors" />
		</sf:form>


在serviceportal中， 用户表单提交一共出现了四次。 

	1. 用户注册

	2. 管理员添加用户

	3. 管理员更新用户

	4. 用户更新信息

总的原则是， 当验证错误时， 则返回到原表单， 并在表单中保留用户输入信息。 错误信息集中展示到表单的最上部。 

错误信息给出的原则是尽可能全面而精确。 

信息验证的特殊原则为用户名， email， 电话号码三个条目不允许互相重复。 



在四种具体情况中， 其中“4. 用户更新信息”采用了异步表单提交， 所以并未以jsp方式展示错误信息， 而是以ajax异步方法展示错误信息。 

另外“2. 管理员更新用户”并不允许更新密码， 然而为了利用spring validation 的@Valid 机制， 所以在表单中增添了隐藏的密码输入框。 


最后， 具体到返回原表单的做法是在表单处理的handler中， 将model作为参数返回表格生成的handler方法. 

	代码示例：

		@RequestMapping(value = "/users",method = RequestMethod.POST)
		public String addUser(HttpServletRequest request, @Valid User newUser, Errors errors, Model model, Locale locale) {
		
			if errors
				return getUsers(model, request, null, null);

		}
	
		


	

	
