package com.xoolibeut.extract;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class XoolibeutClassificationRB {
	private static final String compte_rg = "C:\\perso\\depenses\\compte_rawane";
	private static final String compte_fg = "C:\\perso\\depenses\\compte_fatou";
	public static void main(String[] args) {

		try {
			/**
			 * String[] libelleNot = {
			 * ExtractDebit.IDENTIFICATION_ENVOIE_WESTERN_UNION_COURBEVOIE,
			 * ExtractDebit.IDENTIFICATION_COMMERCE_LONDON,
			 * ExtractDebit.IDENTIFICATION_COMMERCE_LUXEMBOURG,
			 * ExtractDebit.IDENTIFICATION_SANTE_MEDECIN_FARCY_CORRINE };
			 * 
			 * String[] libelleIn = { "CARREFOUR" };
			 **/

			File fileFolder = new File(compte_rg);
			File[] files = fileFolder.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					generateCSVFromPDFTypeOperation(file.getAbsolutePath(), "depenses", ExtractDebit.PAIEMENT,
							ExtractDebit.PRLV, ExtractDebit.VIR_SEPA,
							ExtractDebit.IDENTIFICATION_ASSURANCE_TRANSPORT_HABITAT,
							ExtractDebit.IDENTIFICATION_ASSURANCE_TRANSPORT_PLANS_PREV,
							ExtractDebit.IDENTIFICATION_RETRAIT_DIVERS_RETRAIT,
							ExtractDebit.IDENTIFICATION_SPORT_CHEQUE,ExtractDebit.IDENTIFICATION_FRAIS_INTERVENTION);
				}
			}

			File fileFolderCF = new File(compte_fg);
			File[]	filesCF = fileFolderCF.listFiles();
			for (File file : filesCF) {
				if (file.isDirectory()) {
					generateCSVFromPDFTypeOperation(file.getAbsolutePath(), "depenses", ExtractDebit.PAIEMENT,
							ExtractDebit.PRLV, ExtractDebit.VIR_SEPA,
							ExtractDebit.IDENTIFICATION_ASSURANCE_TRANSPORT_HABITAT,
							ExtractDebit.IDENTIFICATION_ASSURANCE_TRANSPORT_PLANS_PREV,
							ExtractDebit.IDENTIFICATION_RETRAIT_DIVERS_RETRAIT,
							ExtractDebit.IDENTIFICATION_SPORT_CHEQUE,ExtractDebit.IDENTIFICATION_FRAIS_INTERVENTION);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void generateCSVFromPDFTypeOperation(String folder, String destination, String... typeOperation)
			throws IOException, ParseException {
		System.out.println(folder);
		File fileFolder = new File(folder);
		File fileFolderDest = new File(fileFolder.getParent() + File.separator + destination);
		if (!fileFolderDest.exists()) {
			fileFolderDest.mkdir();
		}
		System.out.println(fileFolderDest.getAbsolutePath());
		double total = 0.0;
		Date startDate = null;
		Date endDate = null;
		boolean isFirstLine = true;
		XoolibeutClasseurExcel xoolibeutClasseurExcel = new XoolibeutClasseurExcel();
		xoolibeutClasseurExcel.setFeuilleExcels(new ArrayList<XoolibeutFeuilleExcel>());
		File[] files = fileFolder.listFiles();
		for (File file : files) {

			if (file.isFile() && file.getName().endsWith(".pdf")) {

				Date startDateFile = null;
				XoolibeutFeuilleExcel xoolibeutFormatExcel = new XoolibeutFeuilleExcel();
				List<XoolibeutLine> xoolibeutLines = new ArrayList<XoolibeutLine>();
				xoolibeutFormatExcel.setLines(xoolibeutLines);

				PdfReader reader = new PdfReader(file.getAbsolutePath());
				int pages = reader.getNumberOfPages();
				System.out.println("Extracted content of PDF---- ");
				for (int i = 1; i <= pages; i++) {
					// Extract content of each page
					String contentOfPage = PdfTextExtractor.getTextFromPage(reader, i);
					String lines[] = contentOfPage.split("\\r?\\n");
					// System.out.println(lines.length);
					for (int k = 0; k < lines.length; k++) {
						boolean verifLine = false;
						String currentOperationMatch = "";
						String classification = null;
						for (String operation : typeOperation) {
							if (lines[k].contains(operation)) {
								if (!operation.equals(ExtractDebit.IDENTIFICATION_RETRAIT_DIVERS_RETRAIT)
										|| (!lines[k].contains(ExtractDebit.MOT_EXCLU_RETRAITE) && !lines[k]
												.startsWith(ExtractDebit.IDENTIFICATION_RETRAIT_DIVERS_RETRAIT))) {
									if (!operation.equals(ExtractDebit.VIR_SEPA)
											|| !lines[k].contains(ExtractDebit.MOT_EXCLU_VIR_SEPA_GAYE)) {

										verifLine = true;
										currentOperationMatch = operation;
										break;

									}
								}
							}
						}
						if (currentOperationMatch.equals(ExtractDebit.PAIEMENT)) {
							if (lines[k].contains(ExtractDebit.MOT_EXCLU_REMBOURSEMENT)) {
								verifLine = false;
							}
						}
						
						//ON vérifie format de Ligne 
						if(verifLine) {
						String[] arrayLineCheck = lines[k].trim().split(" ");
						if(arrayLineCheck.length<3) {
							verifLine=false;	
						}else {
							try {
							buildDate(arrayLineCheck[0]);
							buildDate(arrayLineCheck[1]);
							}catch (Exception e) {
								verifLine=false;
								//e.printStackTrace();
							}
						}
						
						}
						String complementAchat="";
						if (verifLine) {
							switch (currentOperationMatch) {
							case ExtractDebit.PAIEMENT: {
								classification = ExtractDebit.NATURE_DEPENSE_DIVERS;
								if (k + 1 < lines.length) {
									classification = determineClassicationModePaiementCB(lines[k + 1]);
									complementAchat=" "+lines[k + 1];
								}
								break;
							}
							case ExtractDebit.PRLV:
								classification = determineClassicationModePrevelement(lines[k]);
								break;
							case ExtractDebit.VIR_SEPA:
								classification = determineClassicationModeVirementSepa(lines[k]);
								break;
							case ExtractDebit.IDENTIFICATION_ASSURANCE_TRANSPORT_HABITAT:
								classification = ExtractDebit.NATURE_DEPENSE_ASSURANCE_TRANSPORT;
								break;
							case ExtractDebit.IDENTIFICATION_ASSURANCE_TRANSPORT_PLANS_PREV:
								classification = ExtractDebit.NATURE_DEPENSE_ASSURANCE_TRANSPORT;
								break;
							case ExtractDebit.IDENTIFICATION_RETRAIT_DIVERS_RETRAIT:
								classification = ExtractDebit.NATURE_DEPENSE_DIVERS;
								break;
							case ExtractDebit.IDENTIFICATION_SPORT_CHEQUE:
								classification = ExtractDebit.NATURE_DEPENSE_SPORT;
								break;
							case ExtractDebit.IDENTIFICATION_FRAIS_INTERVENTION:
								classification = ExtractDebit.NATURE_DEPENSE_FRAIS_COMISSION;
								break;								
							default:
							if(currentOperationMatch.contains(ExtractDebit.IDENTIFICATION_FRAIS_INTERVENTION)) {
								classification = ExtractDebit.NATURE_DEPENSE_FRAIS_COMISSION;
							}	

							}
							// System.out.println(lines[k]);
							String[] arrayLine = lines[k].trim().split(" ");
							List<String> listeElement = new ArrayList<String>(4);
							if (isFirstLine) {
								isFirstLine = false;
							}
							// System.out.println("classification " + classification);
							
							String dateFromFile=arrayLine[1];
							if (startDateFile != null) {
								if (startDateFile.compareTo(buildDate(dateFromFile)) > 0) {
									startDateFile = buildDate(dateFromFile);
								}
							} else {
								startDateFile = buildDate(dateFromFile);
							}

							if (startDate != null) {
								if (startDate.compareTo(buildDate(dateFromFile)) > 0) {
									startDate = buildDate(dateFromFile);
								}
							} else {
								startDate = buildDate(dateFromFile);
							}
							if (endDate != null) {
								if (endDate.compareTo(buildDate(dateFromFile)) < 0) {
									endDate = buildDate(dateFromFile);
								}
							} else {
								endDate = buildDate(dateFromFile);
							}

							if (arrayLine.length > 3) {
								listeElement.add(arrayLine[0]);
								listeElement.add(arrayLine[1]);
								String elt3 = arrayLine[2];
								for (int j = 3; j < arrayLine.length - 1; j++) {
									elt3 = elt3 + " " + arrayLine[j];
								}
								listeElement.add(elt3+complementAchat);
								String lastElement = arrayLine[arrayLine.length - 1];
								listeElement.add(lastElement.replaceAll("\\.", "").replace(",", "."));
								listeElement.add(classification);
								listeElement.add(XoolibeutCreateFileExcel.getCodeClassification(classification));
								try {
									total = total + Double.parseDouble(listeElement.get(3));
								} catch (Exception e) {
									e.printStackTrace();
									System.out.println(
											"-----------------error----------------------------------------------------");
									System.out.println("not number " + listeElement.get(3));
								}
							}

							XoolibeutLine xoolibeutLine = new XoolibeutLine();
							xoolibeutLine.setColumnA(listeElement.get(0));
							xoolibeutLine.setColumnB(listeElement.get(1));
							xoolibeutLine.setColumnC(listeElement.get(2));
							xoolibeutLine.setColumnD(listeElement.get(3));
							xoolibeutLine.setColumnE(listeElement.get(4));
							xoolibeutLine.setColumnF(listeElement.get(5));
							xoolibeutLines.add(xoolibeutLine);

						}
					}

				}
				reader.close();
				if (!xoolibeutLines.isEmpty()) {
					xoolibeutClasseurExcel.getFeuilleExcels().add(xoolibeutFormatExcel);
					Calendar calendarStartDate=Calendar.getInstance();
					calendarStartDate.setTime(startDateFile);
					Calendar calendarEndDate=Calendar.getInstance();
					calendarEndDate.setTime(endDate);
		 //Correction Date 
					if(calendarStartDate.get(Calendar.MONTH)!=calendarEndDate.get(Calendar.MONTH) && calendarStartDate.get(Calendar.DAY_OF_MONTH)> 15) {
						
						calendarStartDate.set(Calendar.MONTH, calendarEndDate.get(Calendar.MONTH));
						calendarStartDate.set(Calendar.DAY_OF_MONTH, 1);
					}
					xoolibeutFormatExcel.setName(buildFileNameMonthYear(calendarStartDate.getTime()));
					xoolibeutFormatExcel.setStartDate(startDateFile);
					xoolibeutFormatExcel.setEndDate(endDate);
					Collections.sort(xoolibeutFormatExcel.getLines(), new Comparator<XoolibeutLine>() {
						@Override
						public int compare(XoolibeutLine o1, XoolibeutLine o2) {
							return o1.getColumnF().compareTo(o2.getColumnF());

						}
					});
				}
			}
		}

		if (!isFirstLine) {
			Calendar calendarStartDate=Calendar.getInstance();
			calendarStartDate.setTime(startDate);
			Calendar calendarEndDate=Calendar.getInstance();
			calendarEndDate.setTime(endDate);
 //Correction Date 
			if(calendarStartDate.get(Calendar.MONTH)!=calendarEndDate.get(Calendar.MONTH) && calendarStartDate.get(Calendar.DAY_OF_MONTH)> 15) {
				
				calendarStartDate.set(Calendar.MONTH, calendarEndDate.get(Calendar.MONTH));
				calendarStartDate.set(Calendar.DAY_OF_MONTH, 1);
			}
			String fileName = fileFolderDest.getAbsolutePath() + File.separator + "DEPENSE_"
					+ buildNameByEndDate(calendarStartDate.getTime()) + "_AU_" + buildNameByEndDate(endDate) + ".xlsx";
			XoolibeutCreateFileExcel.createClasseurExcel(xoolibeutClasseurExcel, fileName);

		} else {
			System.out.println("Aucune extraction  ");

		}
		
	}

	private static String buildFileNameMonthYear(Date dateSt) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateSt);
		String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
		return month + "_" + calendar.get(Calendar.YEAR);
	}

	private static String buildNameByEndDate(Date dateSt) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateSt);
		String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
		// System.out.println(month);
		return calendar.get(Calendar.DAY_OF_MONTH) + "_" + month + "_" + calendar.get(Calendar.YEAR);
	}

	private static Date buildDate(String dateSt) throws ParseException {
		if (dateSt.isEmpty()) {
			return null;
		}
		DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date dateStart = sourceFormat.parse(dateSt);
		return dateStart;
	}

	private static String determineClassicationModePaiementCB(String contentLine) {
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_COURSE_CARREFOUR)
				|| contentLine.contains(ExtractDebit.IDENTIFICATION_CB_COURSE_LECLERC) ||  contentLine.contains(ExtractDebit.IDENTIFICATION_CB_COURSE_INTERMARCHE)) {
			return ExtractDebit.NATURE_DEPENSE_COURSE;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_COURSE_LIDL)
				|| contentLine.contains(ExtractDebit.IDENTIFICATION_CB_COURSE_MAGASIN_U)) {
			return ExtractDebit.NATURE_DEPENSE_COURSE;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_COURSE_BOUFFAY_LE_13)
				|| contentLine.contains(ExtractDebit.IDENTIFICATION_CB_COURSE_NESPRESSO)) {
			return ExtractDebit.NATURE_DEPENSE_COURSE;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_RESTAU_SORTI_ASKALE)
				|| contentLine.contains(ExtractDebit.IDENTIFICATION_CB_RESTAU_SORTI_BURGER_KING)) {
			return ExtractDebit.NATURE_DEPENSE_RESTAU_SORTI;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_RESTAU_SORTI_KFC)
				|| contentLine.contains(ExtractDebit.IDENTIFICATION_CB_RESTAU_SORTI_MCDONALD)) {
			return ExtractDebit.NATURE_DEPENSE_RESTAU_SORTI;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_RESTAU_SORTI_PITAYA) || contentLine.contains(ExtractDebit.IDENTIFICATION_CB_RESTAU) || contentLine.contains(ExtractDebit.IDENTIFICATION_CB_DIVERTISSEMENT_COMITEO)) {
			return ExtractDebit.NATURE_DEPENSE_RESTAU_SORTI;
		}

		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_BOULANGERIE)
				|| contentLine.contains(ExtractDebit.IDENTIFICATION_CB_BOULANGERIE_DOUCEUR)) {
			return ExtractDebit.NATURE_DEPENSE_BOULANGERIE;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_BOULANGERIE_LA_MIE_CALINE)
				|| contentLine.contains(ExtractDebit.IDENTIFICATION_CB_BOULANGERIE_PAINS_ET_GOURMAND)) {
			return ExtractDebit.NATURE_DEPENSE_BOULANGERIE;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_BOULANGERIE_PATISS_DE_LA_GARE)
				|| contentLine.contains(ExtractDebit.IDENTIFICATION_CB_BOULANGERIE_TARTINE_THOUA)) {
			return ExtractDebit.NATURE_DEPENSE_BOULANGERIE;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_ASSURANCE_TRANSPORT_SECURITEST)
				|| contentLine.contains(ExtractDebit.IDENTIFICATION_CB_ASSURANCE_TRANSPORT_COFIROUTE)) {
			return ExtractDebit.NATURE_DEPENSE_ASSURANCE_TRANSPORT;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_ASSURANCE_TRANSPORT_CARTER_CASH) || contentLine.contains(ExtractDebit.IDENTIFICATION_CB_ASSURANCE_TRANSPORT_GARAGE_RECYCLE_AUTO)) {
			return ExtractDebit.NATURE_DEPENSE_ASSURANCE_TRANSPORT;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_CHARGE_COPRIETAIRES_CABINET_THIERRY)) {
			return ExtractDebit.NATURE_DEPENSE_CHARGES_COPRO;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_COMMERCE_ALIEXPRES) || contentLine.contains(ExtractDebit.IDENTIFICATION_CB_COMMERCE_PAYPAL) || contentLine.contains(ExtractDebit.IDENTIFICATION_CB_COMMERCE_TEMU)) {
			return ExtractDebit.NATURE_DEPENSE_EXTRA;
		}
		
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_COMMERCE_COTIZUP)) {
			return ExtractDebit.NATURE_DEPENSE_EXTRA;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_EXTRA_WESTERN_UNION)) {
			return ExtractDebit.NATURE_DEPENSE_ENVOIE_SENEGAL;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_DIVERS_AMAZON)) {
			return ExtractDebit.NATURE_DEPENSE_DIVERS;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_DIVERS_CASTORAMA)) {
			return ExtractDebit.NATURE_DEPENSE_MEUBLE_JARDIN;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_SANTE_MEDECIN_FARCY_CORRINE)
				|| contentLine.contains(ExtractDebit.IDENTIFICATION_CB_SANTE_MEDECIN_KATELL_THOMAS) || contentLine.contains(ExtractDebit.IDENTIFICATION_CB_SANTE_MEDECIN_ANAIS_GONZALEZ)) {
			return ExtractDebit.NATURE_DEPENSE_SANTE;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_SANTE_MEDECIN_COMBY) || contentLine.contains(ExtractDebit.IDENTIFICATION_CB_SANTE_MEDECIN_PEROZ)) {
			return ExtractDebit.NATURE_DEPENSE_SANTE;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_PHARMACIE)) {
			return ExtractDebit.NATURE_DEPENSE_PHARMACIE;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_SPORT_DECATHLON) || contentLine.contains(ExtractDebit.IDENTIFICATION_CB_SPORT_HELLOASSO)) {
			return ExtractDebit.NATURE_DEPENSE_SPORT;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_CB_TABAC_TABAC_PRESSE)) {
			return ExtractDebit.NATURE_DEPENSE_TABAC;
		}

		return ExtractDebit.NATURE_DEPENSE_DIVERS;
	}

	private static String determineClassicationModePrevelement(String contentLine) {
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_PRLV_COURSE_CARREFOUR)) {
			return ExtractDebit.NATURE_DEPENSE_COURSE;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_PRLV_ASSURANCE_TRANSPORT_AVANSSUR)
				|| contentLine.contains(ExtractDebit.IDENTIFICATION_PRLV_ASSURANCE_TRANSPORT_SEMITAN)) {
			return ExtractDebit.NATURE_DEPENSE_ASSURANCE_TRANSPORT;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_PRLV_CANTINE_UNIQUE_TH) || contentLine.contains(ExtractDebit.IDENTIFICATION_PRLV_IMPOT_SGC_SAINT_HERBLAIN)) {
			return ExtractDebit.NATURE_DEPENSE_CANTINE;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_PRLV_ENERGIE_ENGIE)
				|| contentLine.contains(ExtractDebit.IDENTIFICATION_PRLV_ENERGIE_ENGIE_HOME)) {
			return ExtractDebit.NATURE_DEPENSE_EAU_ELEC_GAZ;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_PRLV_CSE_URSSAF)
				) {
			return ExtractDebit.NATURE_DEPENSE_RESTAU_SORTI;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_PRLV_ENERGIE_TOTALENERGIES)
				|| contentLine.contains(ExtractDebit.IDENTIFICATION_PRLV_ENERGIE_TRES_NANTES_MUNICIP)) {
			return ExtractDebit.NATURE_DEPENSE_EAU_ELEC_GAZ;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_PRLV_IMPOT_FINANCE_PUBLIQUE)) {
			return ExtractDebit.NATURE_DEPENSE_IMPOT;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_PRLV_INTERNET_EURO_INFORMATION)
				|| contentLine.contains(ExtractDebit.IDENTIFICATION_PRLV_INTERNET_FREE_FIXE)) {
			return ExtractDebit.NATURE_DEPENSE_INTERNET_TELEPHONIE;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_PRLV_INTERNET_FREE_MOBILE)
				|| contentLine.contains(ExtractDebit.IDENTIFICATION_PRLV_INTERNET_PRIXTEL) || contentLine.contains(ExtractDebit.IDENTIFICATION_PRLV_INTERNET_BOUYGUES)) {
			return ExtractDebit.NATURE_DEPENSE_INTERNET_TELEPHONIE;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_PRLV_INTERNET_EI_TELECOM) || contentLine.contains(ExtractDebit.IDENTIFICATION_PRLV_INTERNET_YOUPRICE)) {
			return ExtractDebit.NATURE_DEPENSE_INTERNET_TELEPHONIE;
		}

		return ExtractDebit.NATURE_DEPENSE_DIVERS;
	}

	private static String determineClassicationModeVirementSepa(String contentLine) {
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_VIR_SEPA_ASS_MAT)
				|| contentLine.contains(ExtractDebit.IDENTIFICATION_VIR_SEPA_ASS_MAT_PAIE) || contentLine.contains(ExtractDebit.IDENTIFICATION_VIR_SEPA_ASS_MAT_SALAIRE)) {
			return ExtractDebit.NATURE_DEPENSE_ASS_MAT;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_VIR_SEPA_ASS_MAT_QUEAU)) {
			return ExtractDebit.NATURE_DEPENSE_ASS_MAT;
		}
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_VIR_SEPA_ENERGIE_PAIE_EAU)) {
			return ExtractDebit.NATURE_DEPENSE_EAU_ELEC_GAZ;
		}
		
		if (contentLine.contains(ExtractDebit.IDENTIFICATION_VIR_SEPA_RESTAU_SORTI_CSE)) {
			return ExtractDebit.NATURE_DEPENSE_RESTAU_SORTI;
		}

		if (contentLine.contains(ExtractDebit.IDENTIFICATION_VIR_SEPA_SYNDIC) || contentLine.contains(ExtractDebit.IDENTIFICATION_VIR_SEPA_LEOPOLD) || contentLine.contains(ExtractDebit.IDENTIFICATION_VIR_SEPA_CHARGES)) {
			return ExtractDebit.NATURE_DEPENSE_CHARGES_COPRO;
		}
		return ExtractDebit.NATURE_DEPENSE_DIVERS;
	}

	public static double formatDouble(double montant) {
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.FRENCH);
		decimalFormatSymbols.setDecimalSeparator('.');
		DecimalFormat decimalFormat = new DecimalFormat("#.##", decimalFormatSymbols);
		decimalFormat.setRoundingMode(RoundingMode.UP);
		return Double.valueOf(decimalFormat.format(montant));
	}
}
