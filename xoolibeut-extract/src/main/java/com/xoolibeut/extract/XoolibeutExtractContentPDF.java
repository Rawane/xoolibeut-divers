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

public class XoolibeutExtractContentPDF {
	private static final String compte_rg = "C:\\perso\\depenses\\compte_rawane";
	private static final String compte_fg = "C:\\perso\\depenses\\compte_fatou";

	public static void main(String[] args) {

		try {
			generateAllCSVFromFolder(compte_rg, true);
			generateAllCSVFromFolder(compte_fg, false);
			generateCSVFromPDFTypeOperation(compte_fg, "WESTER_UNION",false, null, "COURBEVOIE");
			generateCSVFromPDFTypeOperation(compte_fg, "ALIE_EXPRESS",false, null, "LONDON", "LUXEMBOURG");
			String[] libelleNot = { "COURBEVOIE","LONDON","LUXEMBOURG","CORINE" };
			generateCSVFromPDFTypeOperation(compte_rg, "ONE",false, libelleNot, ExtractDebit.PAIEMENT, ExtractDebit.PRLV,
					ExtractDebit.VIR_SEPA);
			generateCSVFromPDFTypeOperation(compte_fg, "ONE",false, libelleNot, ExtractDebit.PAIEMENT, ExtractDebit.PRLV,
					ExtractDebit.VIR_SEPA);
			String[] libelleIn = { "CARREFOUR" };
			generateCSVFromPDFTypeOperation(compte_rg, "CARREFOUR",true, libelleIn, ExtractDebit.PAIEMENT, ExtractDebit.PRLV,ExtractDebit.VIR_SEPA
					);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void generateAllCSVFromFolder(String folder, boolean extractSepa) throws IOException, ParseException {
		File fileFolder = new File(folder);
		File fileFolderDest = new File(folder + "_csv");
		if (!fileFolderDest.exists()) {
			fileFolderDest.mkdir();
		}
		File[] files = fileFolder.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				generateCSVFromPDF(file.getAbsolutePath(), fileFolderDest.getAbsolutePath(), extractSepa);
			}
		}

	}

	public static void generateCSVFromPDF(String fileName, String repFileOuput, boolean extractSepa)
			throws IOException, ParseException {
		PdfReader reader = new PdfReader(fileName);
		int pages = reader.getNumberOfPages();
		System.out.println("Extracted content of PDF---- ");
		String contentOut = "";
		double total = 0.0;
		String startDate = "";
		String endDate = "";
		boolean isFirstLine = true;
		for (int i = 1; i <= pages; i++) {
			// Extract content of each page
			String contentOfPage = PdfTextExtractor.getTextFromPage(reader, i);
			String lines[] = contentOfPage.split("\\r?\\n");
			System.out.println(lines.length);
			for (int k = 0; k < lines.length; k++) {
				boolean verifLine = lines[k].contains(ExtractDebit.PAIEMENT) || lines[k].contains(ExtractDebit.PRLV);
				if (extractSepa) {
					verifLine = lines[k].contains(ExtractDebit.PAIEMENT) || lines[k].contains(ExtractDebit.PRLV)
							|| lines[k].contains(ExtractDebit.VIR_SEPA);
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
				}
			}

			// System.out.println(contentOfPage);
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
			FileWriter fw = new FileWriter(repFileOuput + File.separator + ouputFileName);
			contentOut = contentOut + "\n\n" + ";;Total Dépense : ;" + formatDouble(total);
			fw.write(contentOut);
			fw.close();
			reader.close();
		} else {
			System.out.println("Aucune extraction pour ce fichier " + fileName);

		}

	}

	public static void generateCSVFromPDFTypeOperation(String folder, String endFolder, boolean libelleIn,
			String[] libelleOperation, String... typeOperation) throws IOException, ParseException {
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

		File[] files = fileFolder.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				// file.getAbsolutePath(), fileFolderDest.getAbsolutePath()

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
						for (String operation : typeOperation) {
							if (lines[k].contains(operation)) {
								verifLine = true;
								break;
							}
						}
						if (verifLine) {
							System.out.println(lines[k]);
							boolean verifieException = false;
							
								if (libelleOperation != null) {
									for (String libelle : libelleOperation) {
										if (lines[k].contains(libelle)) {
											verifieException = true;
											break;
										}
									}
									if (!verifieException && k + 1 < lines.length && lines[k].contains(ExtractDebit.PAIEMENT)) {
										for (String libelle : libelleOperation) {
											if (lines[k + 1].contains(libelle)) {
												verifieException = true;
												break;
											}
										}
									}

								}
							
							if ((libelleIn && verifieException) ||(!libelleIn && !verifieException)) {
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
							}
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
