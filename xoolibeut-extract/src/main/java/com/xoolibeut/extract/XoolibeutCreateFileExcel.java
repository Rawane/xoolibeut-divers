package com.xoolibeut.extract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

public class XoolibeutCreateFileExcel {

	public static void createClasseurExcel(XoolibeutClasseurExcel classeurExcel, String fileName) throws IOException {
		if (classeurExcel.getFeuilleExcels().isEmpty()) {
			return;
		}
		HSSFWorkbook workbook = new HSSFWorkbook();
		Collections.sort(classeurExcel.getFeuilleExcels(), new Comparator<XoolibeutFeuilleExcel>() {
			@Override
			public int compare(XoolibeutFeuilleExcel o1, XoolibeutFeuilleExcel o2) {
				return o1.getStartDate().compareTo(o2.getStartDate());

			}
		});
		HSSFSheet sheetResume = workbook.createSheet("Résumée annuelle");
		sheetResume.setColumnWidth(0, 30 * 256);
		sheetResume.setColumnWidth(1, 15 * 256);		
		sheetResume.setColumnWidth(3, 15 * 256);
		sheetResume.setColumnWidth(4, 25 * 256);
		sheetResume.setColumnWidth(5, 25 * 256);
		List<XoolibeutFeuilleExcel> listFeuille = classeurExcel.getFeuilleExcels();
		for (XoolibeutFeuilleExcel xoolibeutFeuilleExcel : listFeuille) {
			HSSFSheet sheet = workbook.createSheet(xoolibeutFeuilleExcel.getName());
			// sheet.autoSizeColumn(0);
			sheet.setColumnWidth(0, 30 * 256);
			sheet.setColumnWidth(1, 15 * 256);
			sheet.setColumnWidth(2, 40 * 256);
			sheet.setColumnWidth(4, 22 * 256);
			List<XoolibeutLine> xoolibeutLines = xoolibeutFeuilleExcel.getLines();

			int rownum = 1;
			Cell cell;
			Row row;
			// HSSFCellStyle style = createStyleForTitle(workbook);

			// Data

			HSSFRow rowTotal = sheet.createRow(rownum);
			rownum++;
			rownum++;
			Row rowTotalWithoutSEtEx = sheet.createRow(rownum);
			rownum++;
			rownum++;
			Row rowCourse = sheet.createRow(rownum);
			rownum++;
			Row rowBoulangerie = sheet.createRow(rownum);
			rownum++;
			Row rowEauElec = sheet.createRow(rownum);
			rownum++;
			Row rowAss = sheet.createRow(rownum);
			rownum++;
			Row rowAssuranceTransport = sheet.createRow(rownum);
			rownum++;
			Row rowInternetTel = sheet.createRow(rownum);
			rownum++;
			Row rowCantine = sheet.createRow(rownum);
			rownum++;
			Row rowImpot = sheet.createRow(rownum);
			rownum++;
			Row rowPharmacie = sheet.createRow(rownum);
			rownum++;
			Row rowDivers = sheet.createRow(rownum);
			rownum++;
			Row rowRestau = sheet.createRow(rownum);
			rownum++;
			Row rowSport = sheet.createRow(rownum);
			rownum++;
			Row rowTabac = sheet.createRow(rownum);
			rownum++;
			Row rowMeubleJardin = sheet.createRow(rownum);
			rownum++;
			Row rowChargeCorpro = sheet.createRow(rownum);
			rownum++;
			Row rowExtra = sheet.createRow(rownum);
			rownum++;
			Row rowSante = sheet.createRow(rownum);

			int startTabIndex = rownum + 2;

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

			String formula = "SUM(D" + (startTabIndex + 1) + ":D" + (rownum + 1) + ")";
			cell = rowTotal.createCell(0, CellType.STRING);
			cell.setCellValue("TOTAL");
			cell = rowTotal.createCell(3, CellType.FORMULA);
			cell.setCellFormula(formula);
			updateCell(ExtractDebit.NATURE_DEPENSE_COURSE, rowCourse, startTabIndex, rownum);
			updateCell(ExtractDebit.NATURE_DEPENSE_BOULANGERIE, rowBoulangerie, startTabIndex, rownum);
			updateCell(ExtractDebit.NATURE_DEPENSE_EAU_ELEC_GAZ, rowEauElec, startTabIndex, rownum);
			updateCell(ExtractDebit.NATURE_DEPENSE_ASS_MAT, rowAss, startTabIndex, rownum);
			updateCell(ExtractDebit.NATURE_DEPENSE_ASSURANCE_TRANSPORT, rowAssuranceTransport, startTabIndex, rownum);
			updateCell(ExtractDebit.NATURE_DEPENSE_INTERNET_TELEPHONIE, rowInternetTel, startTabIndex, rownum);
			updateCell(ExtractDebit.NATURE_DEPENSE_CANTINE, rowCantine, startTabIndex, rownum);
			updateCell(ExtractDebit.NATURE_DEPENSE_IMPOT, rowImpot, startTabIndex, rownum);
			updateCell(ExtractDebit.NATURE_DEPENSE_PHARMACIE, rowPharmacie, startTabIndex, rownum);
			updateCell(ExtractDebit.NATURE_DEPENSE_DIVERS, rowDivers, startTabIndex, rownum);
			updateCell(ExtractDebit.NATURE_DEPENSE_RESTAU_SORTI, rowRestau, startTabIndex, rownum);
			updateCell(ExtractDebit.NATURE_DEPENSE_SPORT, rowSport, startTabIndex, rownum);
			updateCell(ExtractDebit.NATURE_DEPENSE_TABAC, rowTabac, startTabIndex, rownum);
			updateCell(ExtractDebit.NATURE_DEPENSE_MEUBLE_JARDIN, rowMeubleJardin, startTabIndex, rownum);
			updateCell(ExtractDebit.NATURE_DEPENSE_CHARGES_COPRO, rowChargeCorpro, startTabIndex, rownum);
			updateCell(ExtractDebit.NATURE_DEPENSE_EXTRA, rowExtra, startTabIndex, rownum);
			updateCell(ExtractDebit.NATURE_DEPENSE_SANTE, rowSante, startTabIndex, rownum);

			formula = "SUM(D6:D20)";
			cell = rowTotalWithoutSEtEx.createCell(0, CellType.STRING);
			cell.setCellValue("TOTAL Participatif ");
			cell = rowTotalWithoutSEtEx.createCell(3, CellType.FORMULA);
			cell.setCellFormula(formula);

		}
		String formulaResume = "";
		int compte = 0;
		for (XoolibeutFeuilleExcel xoolibeutFeuilleExcel : listFeuille) {
			if (compte == 0) {
				formulaResume = xoolibeutFeuilleExcel.getName() + "!@NUM@";
			} else {
				formulaResume = formulaResume + "+" + xoolibeutFeuilleExcel.getName() + "!@NUM@";
			}
			compte++;

		}
		int rowResume = 0;

		Row rowTotalDepense = sheetResume.createRow(rowResume);
		String formula = "SUM(B6:B22)";
		Cell cell = rowTotalDepense.createCell(0, CellType.STRING);
		cell.setCellValue("TOTAL DEPENSE");
		cell = rowTotalDepense.createCell(1, CellType.FORMULA);
		cell.setCellFormula(formula);
		rowResume++;
		rowResume++;
		Row rowTotalDepenseParticipatif = sheetResume.createRow(rowResume);
		formula = "SUM(B6:B20)";
		cell = rowTotalDepenseParticipatif.createCell(0, CellType.STRING);
		cell.setCellValue("TOTAL DEPENSE PARTICIPATIF");
		cell = rowTotalDepenseParticipatif.createCell(1, CellType.FORMULA);
		cell.setCellFormula(formula);
		rowResume++;
		rowResume++;
		rowResume = createCellResume(ExtractDebit.NATURE_DEPENSE_COURSE, sheetResume, rowResume,
				formulaResume.replace("@NUM@", "D6"));

		rowResume = createCellResume(ExtractDebit.NATURE_DEPENSE_BOULANGERIE, sheetResume, rowResume,
				formulaResume.replace("@NUM@", "D7"));
		rowResume = createCellResume(ExtractDebit.NATURE_DEPENSE_EAU_ELEC_GAZ, sheetResume, rowResume,
				formulaResume.replace("@NUM@", "D8"));
		rowResume = createCellResume(ExtractDebit.NATURE_DEPENSE_ASS_MAT, sheetResume, rowResume,
				formulaResume.replace("@NUM@", "D9"));
		rowResume = createCellResume(ExtractDebit.NATURE_DEPENSE_ASSURANCE_TRANSPORT, sheetResume, rowResume,
				formulaResume.replace("@NUM@", "D10"));
		rowResume = createCellResume(ExtractDebit.NATURE_DEPENSE_INTERNET_TELEPHONIE, sheetResume, rowResume,
				formulaResume.replace("@NUM@", "D11"));
		rowResume = createCellResume(ExtractDebit.NATURE_DEPENSE_CANTINE, sheetResume, rowResume,
				formulaResume.replace("@NUM@", "D12"));
		rowResume = createCellResume(ExtractDebit.NATURE_DEPENSE_IMPOT, sheetResume, rowResume,
				formulaResume.replace("@NUM@", "D13"));
		rowResume = createCellResume(ExtractDebit.NATURE_DEPENSE_PHARMACIE, sheetResume, rowResume,
				formulaResume.replace("@NUM@", "D14"));
		rowResume = createCellResume(ExtractDebit.NATURE_DEPENSE_DIVERS, sheetResume, rowResume,
				formulaResume.replace("@NUM@", "D15"));
		rowResume = createCellResume(ExtractDebit.NATURE_DEPENSE_RESTAU_SORTI, sheetResume, rowResume,
				formulaResume.replace("@NUM@", "D16"));
		rowResume = createCellResume(ExtractDebit.NATURE_DEPENSE_SPORT, sheetResume, rowResume,
				formulaResume.replace("@NUM@", "D17"));
		rowResume = createCellResume(ExtractDebit.NATURE_DEPENSE_TABAC, sheetResume, rowResume,
				formulaResume.replace("@NUM@", "D18"));
		rowResume = createCellResume(ExtractDebit.NATURE_DEPENSE_MEUBLE_JARDIN, sheetResume, rowResume,
				formulaResume.replace("@NUM@", "D19"));
		rowResume = createCellResume(ExtractDebit.NATURE_DEPENSE_CHARGES_COPRO, sheetResume, rowResume,
				formulaResume.replace("@NUM@", "D20"));
		rowResume = createCellResume(ExtractDebit.NATURE_DEPENSE_EXTRA, sheetResume, rowResume,
				formulaResume.replace("@NUM@", "D21"));
		rowResume = createCellResume(ExtractDebit.NATURE_DEPENSE_SANTE, sheetResume, rowResume,
				formulaResume.replace("@NUM@", "D22"));

		int compteMonth = 0;
		Row row = sheetResume.getRow(1);
		if (row == null) {
			row = sheetResume.createRow(1);
		}
		cell = row.createCell(4, CellType.STRING);
		cell.setCellValue("Dépense Mensuel");
		cell = row.createCell(5, CellType.STRING);
		cell.setCellValue("Dépense Mensuel Participatif");
		for (XoolibeutFeuilleExcel xoolibeutFeuilleExcel : listFeuille) {
			row = sheetResume.getRow(2 + compteMonth);
			if (row == null) {
				row = sheetResume.createRow(6 + compteMonth);
			}
			System.out.println("Compteur " + compteMonth);
			cell = row.createCell(3, CellType.STRING);
			cell.setCellValue(
					xoolibeutFeuilleExcel.getName().substring(0, xoolibeutFeuilleExcel.getName().indexOf("_")));
			cell = row.createCell(4, CellType.FORMULA);
			formula = xoolibeutFeuilleExcel.getName() + "!D2";
			cell.setCellFormula(formula);

			cell = row.createCell(5, CellType.FORMULA);
			formula = xoolibeutFeuilleExcel.getName() + "!D4";
			cell.setCellFormula(formula);
			compteMonth++;

		}

		File file = new File(fileName);
		FileOutputStream outFile = new FileOutputStream(file);
		workbook.write(outFile);
		workbook.close();
		System.out.println("Created file: " + file.getAbsolutePath());

	}

	private static void updateCell(String natureDepense, Row row, int startTabIndex, int rownum) {
		// index++;
		Cell cell = row.createCell(0, CellType.STRING);
		cell.setCellValue(natureDepense.toUpperCase());
		String formula = "SUMIF(F" + (startTabIndex + 1) + ":F" + (rownum + 1) + ",\""
				+ getCodeClassification(natureDepense) + "\",D" + (startTabIndex + 1) + ":D" + (rownum + 1) + ")";
		cell = row.createCell(3, CellType.FORMULA);
		cell.setCellFormula(formula);
	}

	private static int createCellResume(String natureDepense, HSSFSheet sheetResume, int rownum, String formula) {
		rownum++;
		Row row = sheetResume.createRow(rownum);
		Cell cell = row.createCell(0, CellType.STRING);
		cell.setCellValue(natureDepense.toUpperCase());
		cell = row.createCell(1, CellType.FORMULA);
		cell.setCellFormula(formula);
		return rownum++;
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
