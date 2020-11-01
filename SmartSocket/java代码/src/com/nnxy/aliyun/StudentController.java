package com.nnxy.aliyun;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/student")
public class StudentController {

	public Map<String, Student> mapList = new HashMap<String, Student>();
	public StudentController(){
		mapList.put("n100", new Student(100,"张三"));
		mapList.put("n101", new Student(101,"李四"));
	}
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(Model model){
		model.addAttribute("mapList",mapList);
		return "/list"; // 表示list.jsp
	}
}
