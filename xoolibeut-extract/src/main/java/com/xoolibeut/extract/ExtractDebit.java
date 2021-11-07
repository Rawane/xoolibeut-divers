package com.xoolibeut.extract;

public class ExtractDebit {
	public final static String PAIEMENT = "PAIEMENT";
	public final static String CB = "CB";
	public final static String PRLV = "PRLV";
	public final static String VIR = "VIR";
	public final static String VIR_SEPA = "VIR SEPA";
	// MOT EXCLU
	public final static String MOT_EXCLU_RETRAITE = "RETRAITE";
	public final static String MOT_EXCLU_REMBOURSEMENT = "REMBOURSEMENT";
	public final static String MOT_EXCLU_VIR_SEPA_GAYE = "GAYE";

//Identififier le type achat
	public final static String IDENTIFICATION_CB_COURSE_CARREFOUR = "CARREFOUR";
	public final static String IDENTIFICATION_CB_COURSE_MAGASIN_U = "MAGASIN U";
	public final static String IDENTIFICATION_CB_COURSE_LECLERC = "LECLERC";
	public final static String IDENTIFICATION_CB_COURSE_LIDL = "LIDL";
	public final static String IDENTIFICATION_PRLV_COURSE_CARREFOUR = "CARREFOUR";
	public final static String IDENTIFICATION_CB_COURSE_NESPRESSO = "NESPRESSO";
	public final static String IDENTIFICATION_CB_COURSE_BOUFFAY_LE_13 = "BOUFFAY LE 13";
	public final static String IDENTIFICATION_CB_EXTRA_WESTERN_UNION = "WESTERN";
	public final static String IDENTIFICATION_CB_COMMERCE_ALIEXPRES = "ALIEXPRESS";

	public final static String IDENTIFICATION_CB_SANTE_MEDECIN_FARCY_CORRINE = "CORINE";
	public final static String IDENTIFICATION_CB_SANTE_MEDECIN_KATELL_THOMAS = "KATELL";
	public final static String IDENTIFICATION_CB_PHARMACIE = "PHARM";

	public final static String IDENTIFICATION_CB_DIVERS_AMAZON = "AMAZON";
	public final static String IDENTIFICATION_RETRAIT_DIVERS_RETRAIT = "RETRAIT";
	public final static String IDENTIFICATION_CB_DIVERS_CASTORAMA = "CASTORAMA";

	public final static String IDENTIFICATION_VIR_SEPA_ENERGIE_PAIE_EAU = "PAIE EAU";
	public final static String IDENTIFICATION_PRLV_ENERGIE_TOTALENERGIES = "TOTALENERGIES";
	public final static String IDENTIFICATION_PRLV_ENERGIE_TRES_NANTES_MUNICIP = "TRES. NANTES MUNICIP";
	public final static String IDENTIFICATION_PRLV_ENERGIE_ENGIE = "ENGIE";
	public final static String IDENTIFICATION_PRLV_ENERGIE_ENGIE_HOME = "ENGIE HOME";

	public final static String IDENTIFICATION_CB_ASSURANCE_TRANSPORT_CARTER_CASH = "CARTER-CASH";
	public final static String IDENTIFICATION_ASSURANCE_TRANSPORT_PLANS_PREV = "PLANS PREV.";
	public final static String IDENTIFICATION_ASSURANCE_TRANSPORT_HABITAT = "HABITAT";
	public final static String IDENTIFICATION_PRLV_ASSURANCE_TRANSPORT_AVANSSUR = "AVANSSUR";
	public final static String IDENTIFICATION_CB_ASSURANCE_TRANSPORT_SECURITEST = "SECURITEST";
	public final static String IDENTIFICATION_CB_ASSURANCE_TRANSPORT_COFIROUTE = "COFIROUTE";
	public final static String IDENTIFICATION_PRLV_ASSURANCE_TRANSPORT_SEMITAN = "SEMITAN";

	public final static String IDENTIFICATION_PRLV_INTERNET_FREE_MOBILE = "FREE MOBILE";
	public final static String IDENTIFICATION_PRLV_INTERNET_FREE_FIXE = "FREE TELECOM";
	public final static String IDENTIFICATION_PRLV_INTERNET_PRIXTEL = "PRIXTEL";
	public final static String IDENTIFICATION_PRLV_INTERNET_EURO_INFORMATION = "EURO-INFORMATION";
	public final static String IDENTIFICATION_PRLV_INTERNET_EI_TELECOM = "EI TELECOM";

	public final static String IDENTIFICATION_CB_CHARGE_COPRIETAIRES_CABINET_THIERRY = "CABINET THIERRY";
	public final static String IDENTIFICATION_CB_SPORT_DECATHLON = "DECATHLON";
	public final static String IDENTIFICATION_SPORT_CHEQUE = "CHEQUE";
	public final static String IDENTIFICATION_CB_RESTAU_SORTI_KFC = "KFC";
	public final static String IDENTIFICATION_CB_RESTAU_SORTI_MCDONALD = "MCDO";
	public final static String IDENTIFICATION_CB_RESTAU_SORTI_BURGER_KING = "BURGER KING";
	public final static String IDENTIFICATION_VIR_SEPA_RESTAU_SORTI_CSE = "CSE";
	public final static String IDENTIFICATION_CB_RESTAU_SORTI_ASKALE = "ASKALE";
	public final static String IDENTIFICATION_CB_RESTAU_SORTI_PITAYA = "PITAYA";

	public final static String IDENTIFICATION_CB_BOULANGERIE_DOUCEUR = "DOUCEURS";
	public final static String IDENTIFICATION_CB_BOULANGERIE = "BOULANGERIE";
	public final static String IDENTIFICATION_CB_BOULANGERIE_PAINS_ET_GOURMAND = "PAINS ET GOURMAND";
	public final static String IDENTIFICATION_CB_BOULANGERIE_PATISS_DE_LA_GARE = "PATISS";
	public final static String IDENTIFICATION_CB_BOULANGERIE_LA_MIE_CALINE = "LA MIE CALINE";
	public final static String IDENTIFICATION_CB_BOULANGERIE_TARTINE_THOUA = "TARTINE DE THOUA";
	public final static String IDENTIFICATION_CB_TABAC_TABAC_PRESSE = "TABAC PRESSE";
	public final static String IDENTIFICATION_CB_TABAC_GARLAN = "GARLAN";

	public final static String IDENTIFICATION_CB_MODE_BEAUTE_SEPHORA = "SEPHORA";
	public final static String IDENTIFICATION_VIR_SEPA_ASS_MAT = "ASS";
	public final static String IDENTIFICATION_VIR_SEPA_ASS_MAT_PAIE = "PAIE"; 
	public final static String IDENTIFICATION_VIR_SEPA_ASS_MAT_QUEAU = "QUEAU";

	public final static String IDENTIFICATION_PRLV_CANTINE_UNIQUE_TH = "UNIQUE TH";
	public final static String IDENTIFICATION_PRLV_IMPOT_FINANCE_PUBLIQUE = "FINANCES PUBLIQUES";

	// classer les achats
	// DAT
	public final static String NATURE_DEPENSE_ASSURANCE_TRANSPORT = "assurance et transport";
	// DAM
	public final static String NATURE_DEPENSE_ASS_MAT = "Assistante Maternelle";
	// DBO
	public final static String NATURE_DEPENSE_BOULANGERIE = "Boulangerie";
	// DCA
	public final static String NATURE_DEPENSE_CANTINE = "cantine";
	// DCC
	public final static String NATURE_DEPENSE_CHARGES_COPRO = "Charges corpopriétaires";
	// DCO
	public final static String NATURE_DEPENSE_COURSE = "Course";
	// DDI
	public final static String NATURE_DEPENSE_DIVERS = "divers";
	// DEE
	public final static String NATURE_DEPENSE_EAU_ELEC_GAZ = "Eau Electricité Gaz";
	// exemple ali express et envoie western
	// DEX
	public final static String NATURE_DEPENSE_EXTRA = "Extra";
	// DIT
	public final static String NATURE_DEPENSE_INTERNET_TELEPHONIE = "internet et téléphonie";
	// DIM
	public final static String NATURE_DEPENSE_IMPOT = "impôts";
	// DMJ
	public final static String NATURE_DEPENSE_MEUBLE_JARDIN = "meuble jardin";
	// DPH
	public final static String NATURE_DEPENSE_PHARMACIE = "pharmacie";
	// DRS
	public final static String NATURE_DEPENSE_RESTAU_SORTI = "restau et sortie";
	// DSP
	public final static String NATURE_DEPENSE_SPORT = "sport";
	// DSA
	public final static String NATURE_DEPENSE_SANTE = "Santé";
	// DTA
	public final static String NATURE_DEPENSE_TABAC = "tabac presse";

}
