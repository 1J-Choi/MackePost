package com.markepost.suspend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/suspend")
public class SuspendController {
	@GetMapping("/suspendView")
	public String suspendView() {
		return "suspend/suspendView";
	}
}
