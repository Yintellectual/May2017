1. redirect

	@RequestMapping
	
	return "redirect:/devicemanager?type="+device.getType();
	
2. url encoding

	

	import java.net.URLEncoder;
	
	
	Better way is to use RedirectAttributes
	
	@RequestMapping(value = "/deletedevice", method = RequestMethod.GET)
	public String deleteDevice(@RequestParam int id,@RequestParam String type,
			RedirectAttributes attr, HttpServletRequest request) {
		User user = getSessionUser(request);
		logger.info(String.format("[User]%s??????[id=%s]", user.getName(),id));
		
		deviceService.deleteDevice(id);

		attr.addAttribute("type",type);
		return "redirect:/devicemanager";
	}
	
	
	
	