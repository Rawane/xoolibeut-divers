package com.xoolibeut.extract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

public class XoolibeutCreateFileExcel {
	private static HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook) {
		HSSFFont font = workbook.createFont();
		font.setBold(true);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		return style;
	}

	public static void createClasseurExcel(XoolibeutClasseurExcel classeurExcel, String fileName) throws IOException {

		HSSFWorkbook workbook = new HSSFWorkbook();
		Collections.sort(classeurExcel.getFeuilleExcels(), new Comparator<XoolibeutFeuilleExcel>() {
			@Override
			public int compare(XoolibeutFeuilleExcel o1, XoolibeutFeuilleExcel o2) {
				return o1.getStartDate().compareTo(o2.getStartDate());

			}
		});
		HSSFSheet sheetResume = workbook.createSheet("Résumée annuelle");
		List<XoolibeutFeuilleExcel> listFeuille = classeurExcel.getFeuilleExcels();
		for (XoolibeutFeuilleExcel xoolibeutFeuilleExcel : listFeuille) {
			HSSFSheet sheet = workbook.createSheet(xoolibeutFeuilleExcel.getName());
			//sheet.autoSizeColumn(0);
			
			sheet.setColumnWidth(0, 30 * 256);
			sheet.setColumnWidth(1, 15 * 256);
			sheet.setColumnWidth(2, 40 * 256);
			sheet.setColumnWidth(4, 22 * 256);
			List<XoolibeutLine> xoolibeutLines = xoolibeutFeuilleExcel.getLines();

			int rownum = 0;
			Cell cell;
			Row row;
			// HSSFCellStyle style = createStyleForTitle(workbook);

			// Data
			for (XoolibeutLine xoolibeutLine : xoolibeutLines) {
				rownum = rownum + 2;
				row = sheet.createRow(rownum);

				cell = row.createCell(0, CellType.STRING);
				cell.setCellValue(xoolibeutLine.getColumnA());

				cell = row.createCell(1, CellType.STRING);
				cell.setCellValue(xoolibeutLine.getColumnB());

				cell = row.createCell(2, CellType.STRING);
				cell.setCellValue(xoolibeutLine.getColumnC());

				cell = row.createCell(3, CellType.NUMERIC);
				cell.setCellValue(Double.parseDouble(xoolibeutLine.getColumnD()));

				cell = row.createCell(4, CellType.STRING);
				cell.setCellValue(xoolibeutLine.getColumnE());

				CellStyle backgroundStyle = workbook.createCellStyle();

				backgroundStyle.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
				// backgroundStyle.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.index);
				// backgroundStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				cell = row.createCell(5, CellType.STRING);
				cell.setCellValue(xoolibeutLine.getColumnF());
				cell.setCellStyle(backgroundStyle);

			}
			// row = sheet.createRow(rownum);
			int index = rownum;
			index++;
			index++;
			row = sheet.createRow(index);
			String formula = "SUM(D1:D" + rownum + ")";
			cell = row.createCell(0, CellType.STRING);
			cell.setCellValue("TOTAL");
			cell = row.createCell(3, CellType.FORMULA);
			cell.setCellFormula(formula);
			index++;
			index = createCell(ExtractDebit.NATURE_DEPENSE_COURSE, sheet, rownum, index);
			index = createCell(ExtractDebit.NATURE_DEPENSE_BOULANGERIE, sheet, rownum, index);
			index = createCell(ExtractDebit.NATURE_DEPENSE_EAU_ELEC_GAZ, sheet, rownum, index);
			index = createCell(ExtractDebit.NATURE_DEPENSE_ASS_MAT, sheet, rownum, index);
			index = createCell(ExtractDebit.NATURE_DEPENSE_ASSURANCE_TRANSPORT, sheet, rownum, index);
			index = createCell(ExtractDebit.NATURE_DEPENSE_INTERNET_TELEPHONIE, sheet, rownum, index);
			index = createCell(ExtractDebit.NATURE_DEPENSE_CANTINE, sheet, rownum, index);
			index = createCell(ExtractDebit.NATURE_DEPENSE_IMPOT, sheet, rownum, index);
			index = createCell(ExtractDebit.NATURE_DEPENSE_PHARMACIE, sheet, rownum, index);			
			index = createCell(ExtractDebit.NATURE_DEPENSE_DIVERS, sheet, rownum, index);
			index = createCell(ExtractDebit.NATURE_DEPENSE_RESTAU_SORTI, sheet, rownum, index);
			index = createCell(ExtractDebit.NATURE_DEPENSE_SPORT, sheet, rownum, index);
			index = createCell(ExtractDebit.NATURE_DEPENSE_TABAC, sheet, rownum, index);
			index = createCell(ExtractDebit.NATURE_DEPENSE_MEUBLE_JARDIN, sheet, rownum, index);
			index = createCell(ExtractDebit.NATURE_DEPENSE_CHARGES_COPRO, sheet, rownum, index);
			index = createCell(ExtractDebit.NATURE_DEPENSE_EXTRA, sheet, rownum, index);
			index = createCell(ExtractDebit.NATURE_DEPENSE_SANTE, sheet, rownum, index);
			
			index++;
			index++;
			row = sheet.createRow(index);
			formula = "SUM(D"+(rownum+4)+":D" + (rownum+19) + ")";
			cell = row.createCell(0, CellType.STRING);
			cell.setCellValue("TOTAL SANS SANTE et EXTRA");
			cell = row.createCell(3, CellType.FORMULA);
			cell.setCellFormula(formula);

			
		
			
		}
		File file = new File(fileName);
		FileOutputStream outFile = new FileOutputStream(file);
		workbook.write(outFile);
		workbook.close();
		System.out.println("Created file: " + file.getAbsolutePath());

	}

	private static int createCell(String natureDepense, HSSFSheet sheet, int rownum, int index) {
		index++;
		//index++;
		Row row = sheet.createRow(index);
		Cell cell = row.createCell(0, CellType.STRING);
		cell.setCellValue(natureDepense.toUpperCase());
		String formula = "SUMIF(F1:F" + rownum + ",\"" + getCodeClassification(natureDepense) + "\",D1:D" + rownum
				+ ")";
		cell = row.createCell(3, CellType.FORMULA);
		cell.setCellFormula(formula);

		return index;
	}

	private static int createCellOLD(String natureDepense, HSSFSheet sheet, int rownum, int index) {
		index++;
		//index++;
		Row row = sheet.createRow(index);
		Cell cell = row.createCell(0, CellType.STRING);
		cell.setCellValue(natureDepense.toUpperCase());
		String formula = "SUMIF(F1:F" + rownum + ",\"" + getCodeClassification(natureDepense) + "\",D1:D" + rownum
				+ ")";
		cell = row.createCell(3, CellType.FORMULA);
		cell.setCellFormula(formula);

		return index;
	}
	public static String getCodeClassification(String classification) {

		String codeClassification;
		if (classification.contains(" ")) {
			int indexOf = classification.lastIndexOf(" ");
			codeClassification = ("D" + classification.substring(0, 1)
					+ classification.substring(indexOf + 1, indexOf + 2)).toUpperCase();
		} else {
			codeClassification = ("D" + classification.substring(0, 2)).toUpperCase();
		}

		return codeClassification;
	}
}
