package com.xoolibeut.extract;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class XoolibeutClassificationRB {
	private static final String compte_rg = "C:\\devs\\divers\\test";
	private static final String compte_fg = "C:\\perso\\depenses\\compte_fatou";

	public static void main(String[] args) {

		try {			
			String[] libelleNot = { ExtractDebit.IDENTIFICATION_ENVOIE_WESTERN_UNION_COURBEVOIE,
					ExtractDebit.IDENTIFICATION_COMMERCE_LONDON, ExtractDebit.IDENTIFICATION_COMMERCE_LUXEMBOURG,
					ExtractDebit.IDENTIFICATION_SANTE_MEDECIN_FARCY_CORRINE };
			generateCSVFromPDFTypeOperation(compte_rg, "ONE", false, libelleNot, ExtractDebit.PAIEMENT,
					ExtractDebit.PRLV, ExtractDebit.VIR_SEPA);
			generateCSVFromPDFTypeOperation(compte_fg, "ONE", false, libelleNot, ExtractDebit.PAIEMENT,
					ExtractDebit.PRLV, ExtractDebit.VIR_SEPA);
			String[] libelleIn = { "CARREFOUR" };
			
			generateCSVFromPDFTypeOperation(compte_rg, ExtractDebit.IDENTIFICATION_COURSE_CARREFOUR, true, libelleIn, ExtractDebit.PAIEMENT,
					ExtractDebit.PRLV, ExtractDebit.VIR_SEPA,ExtractDebit.IDENTIFICATION_ASSURANCE_TRANSPORT_HABITAT,ExtractDebit.IDENTIFICATION_ASSURANCE_TRANSPORT_PLANS_PREV,
					ExtractDebit.IDENTIFICATION_DIVERS_RETRAIT);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	public static void generateCSVFromPDFTypeOperation(String folder, String endFolder, String... typeOperation) throws IOException, ParseException {
		File fileFolder = new File(folder);
		File fileFolderDest = new File(folder + "_" + endFolder);
		if (!fileFolderDest.exists()) {
			fileFolderDest.mkdir();
		}
		String contentOut = "";
		double total = 0.0;
		String startDate = "";
		String endDate = "";
		boolean isFirstLine = true;
		XoolibeutFormatExcel xoolibeutFormatExcel = new XoolibeutFormatExcel();
		List<XoolibeutLine> xoolibeutLines = new ArrayList<XoolibeutLine>();
		xoolibeutFormatExcel.setLines(xoolibeutLines);
		File[] files = fileFolder.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				PdfReader reader = new PdfReader(file.getAbsolutePath());
				int pages = reader.getNumberOfPages();
				System.out.println("Extracted content of PDF---- ");
				for (int i = 1; i <= pages; i++) {
					// Extract content of each page
					String contentOfPage = PdfTextExtractor.getTextFromPage(reader, i);
					String lines[] = contentOfPage.split("\\r?\\n");
					System.out.println(lines.length);
					for (int k = 0; k < lines.length; k++) {
						boolean verifLine = false;
						String currentOperationMatch="";
						for (String operation : typeOperation) {
							if (lines[k].contains(operation)) {
								verifLine = true;	
								currentOperationMatch=operation;
								break;
							}
						}
						if (verifLine) {
							
							
								System.out.println(lines[k]);							
								String[] arrayLine = lines[k].trim().split(" ");
								List<String> listeElement = new ArrayList<String>(4);
								if (isFirstLine) {
									startDate = arrayLine[0];
									isFirstLine = false;
								}
								endDate = arrayLine[0];
								if (arrayLine.length > 3) {
									listeElement.add(arrayLine[0]);
									listeElement.add(arrayLine[1]);
									String elt3 = arrayLine[2];
									for (int j = 3; j < arrayLine.length - 1; j++) {
										elt3 = elt3 + " " + arrayLine[j];
									}
									listeElement.add(elt3);
									String lastElement = arrayLine[arrayLine.length - 1];
									listeElement.add(lastElement.replaceAll("\\.", "").replace(",", "."));
									try {
										total = total + Double.parseDouble(listeElement.get(3));
									} catch (Exception e) {
										e.printStackTrace();
										System.out.println(
												"-----------------error----------------------------------------------------");
										System.out.println("not number " + listeElement.get(3));
									}
								}
								// System.out.println(String.join(";", listeElement));
								contentOut = contentOut + String.join(";", listeElement) + "\n\n";
								XoolibeutLine xoolibeutLine = new XoolibeutLine();
								xoolibeutLine.setColumnA(listeElement.get(0));
								xoolibeutLine.setColumnB(listeElement.get(1));
								xoolibeutLine.setColumnC(listeElement.get(2));
								xoolibeutLine.setColumnD(listeElement.get(3));
								xoolibeutLines.add(xoolibeutLine);
							
						}
					}

					// System.out.println(contentOfPage);
				}
				reader.close();
			}
		}

		if (!isFirstLine) {
			DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
			String ouputFileName = "Depense_du_";
			Date dateStart = sourceFormat.parse(startDate);
			Date dateEnd = sourceFormat.parse(endDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateStart);
			String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
			System.out.println(month);
			ouputFileName = ouputFileName + calendar.get(Calendar.DAY_OF_MONTH) + "_" + month + "_"
					+ calendar.get(Calendar.YEAR);
			calendar.setTime(dateEnd);
			month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
			ouputFileName = ouputFileName + "_au_" + calendar.get(Calendar.DAY_OF_MONTH) + "_" + month + "_"
					+ calendar.get(Calendar.YEAR) + ".csv";
			System.out.println(ouputFileName);

			System.out.println(startDate);
			System.out.println(endDate);
			FileWriter fw = new FileWriter(fileFolderDest.getAbsolutePath() + File.separator + ouputFileName);
			contentOut = contentOut + "\n\n" + ";;Total Dépense : ;" + formatDouble(total);
			fw.write(contentOut);
			fw.close();

		} else {
			System.out.println("Aucune extraction  ");

		}
	}

	public static double formatDouble(double montant) {
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.FRENCH);
		decimalFormatSymbols.setDecimalSeparator('.');
		DecimalFormat decimalFormat = new DecimalFormat("#.##", decimalFormatSymbols);
		decimalFormat.setRoundingMode(RoundingMode.UP);
		return Double.valueOf(decimalFormat.format(montant));
	}
}
