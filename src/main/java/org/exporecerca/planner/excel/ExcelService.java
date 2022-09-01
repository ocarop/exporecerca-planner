package org.exporecerca.planner.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.exporecerca.planner.data.entity.Contestant;
import org.exporecerca.planner.data.entity.Jury;
import org.exporecerca.planner.data.entity.Topic;
import org.exporecerca.planner.data.service.ContestantService;
import org.exporecerca.planner.data.service.JuryService;
import org.exporecerca.planner.data.service.TopicService;
import org.springframework.stereotype.Service;

@Service
public class ExcelService {

	private void writeHeader(XSSFWorkbook workbook) {
		XSSFSheet sheet = workbook.createSheet("Topics");

		Row row = sheet.createRow(0);

		XSSFCellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		createCell(sheet, row, 0, "ID", style);
		createCell(sheet, row, 1, "Student Name", style);
		createCell(sheet, row, 2, "Exam Year", style);
		createCell(sheet, row, 3, "Score", style);

	}

	private void createCell(XSSFSheet sheet, Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Long) {
			cell.setCellValue((Long) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void write(XSSFWorkbook workbook, XSSFSheet sheet, List<Topic> listTopic) {
		int rowCount = 1;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);

		for (Topic record : listTopic) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			createCell(sheet, row, columnCount++, record.getId(), style);
			createCell(sheet, row, columnCount++, record.getName(), style);
			;

		}
	}

	public void generate(HttpServletResponse response, XSSFSheet sheet, List<Topic> listTopic) throws IOException {
		XSSFWorkbook workbook;

		workbook = new XSSFWorkbook();
		writeHeader(workbook);
		write(workbook, sheet, listTopic);
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();

	}

	public void generate(OutputStream outputStream, List<Topic> listTopic) throws IOException {
		XSSFWorkbook workbook;
		XSSFSheet sheet;
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("Topics");
		writeHeader(workbook);
		write(workbook, sheet, listTopic);
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();

	}

	public String importTopics(InputStream fileData, TopicService topicService) {
		String log = "";
		XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(fileData);
			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			// skip first row
			Row row = rowIterator.next();

			// Till there is an element condition holds true
			while (rowIterator.hasNext() && !row.getCell(0).getStringCellValue().equals("")) {

				row = rowIterator.next();

				String name = row.getCell(0).getStringCellValue();
				String color = row.getCell(1).getStringCellValue();
				Topic topic=new Topic();
				topic.setName(name);
				topic.setColor(color);
				topicService.save(topic);
				System.out.print(name);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log = e.getMessage();
		}

		return log;
	}

	public String importJuries(InputStream fileData, JuryService juryService) {
		String log = "";
		XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(fileData);
			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			// skip first row
			Row row = rowIterator.next();

			// Till there is an element condition holds true
			while (rowIterator.hasNext() && !row.getCell(0).getStringCellValue().equals("")) {

				row = rowIterator.next();

				String firstName = row.getCell(0).getStringCellValue();
				String lastName = row.getCell(1).getStringCellValue();
				String email = row.getCell(2).getStringCellValue();
				Jury jury=new Jury();
				jury.setFirstName(firstName);
				jury.setLastName(lastName);
				if (email.equals("")) jury.setEmail(null);else jury.setEmail(email);
				juryService.save(jury);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log = e.getMessage();
		}

		return log;
	}

	public String importContestants(InputStream fileData, ContestantService contestantService, TopicService topicService) {
		String log = "";
		XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(fileData);
			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			// skip first row
			Row row = rowIterator.next();

			// Till there is an element condition holds true
			while (rowIterator.hasNext() && !row.getCell(0).getStringCellValue().equals("")) {

				row = rowIterator.next();

				String code = row.getCell(0).getStringCellValue();
				String title = row.getCell(1).getStringCellValue();
				String names = row.getCell(2).getStringCellValue();
				String center = row.getCell(3).getStringCellValue();
				String topic = row.getCell(4).getStringCellValue();
				Contestant contestant=new Contestant();
				contestant.setCode(code);
				contestant.setTitle(title);
				contestant.setNames(names);
				contestant.setCenter(center);
				Topic topicEntity=topicService.findByName(topic);
				contestant.setTopic(topicEntity);

				contestantService.save(contestant);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log = e.getMessage();
		}

		return log;
	}
	
}
