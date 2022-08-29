package org.exporecerca.planner.excel;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.exporecerca.planner.data.entity.Topic;
import org.exporecerca.planner.data.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExcelController {

	@Autowired
	private TopicService service;
	
	@Autowired
	private ExcelService excelGenerator;

	@GetMapping("/topic/export/excel")
	public void exportIntoExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=records_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		List<Topic> listOfRecords = service.findAll();

		//celGenerator.generate(response, null, listOfRecords);
	}

}
