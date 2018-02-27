package com.sargije.web.poslovanje.views;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.sargije.web.poslovanje.model.Porudzbenica;
import com.sargije.web.poslovanje.model.PorudzbenicaStavke;



public class PdfView extends AbstractPdfView {
	
	final static String FONT = "fonts/arial.ttf";
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Porudzbenica porudzbenica = (Porudzbenica) model.get("porudzbenica");
		BaseFont courier = BaseFont.createFont(FONT, "cp1251", BaseFont.EMBEDDED);
		Font  font = new Font(courier, 12, Font.NORMAL);
		Font fontBold  = new Font(courier, 12, Font.BOLD);
		Font fontBold24 = new Font(courier, 24, Font.BOLD);
		Font font24 = new Font(courier, 24, Font.NORMAL);
		Font font16 = new Font(courier, 16, Font.NORMAL);
		document.add(new Paragraph("МАНАСТИР СВЕТОГ ПРВОМУЧЕНИКА И АРХИЂАКОНА СТЕФАНА", fontBold));
		document.add(new Paragraph("17501 Врање, Горње Жапско", font));
		document.add(new Paragraph("Тел. 017/59-192", font));
		document.add(new Paragraph("E-mail: manastirsvstefan@gmail.com", font));
		document.add(new Paragraph("Жиро рачун: ALPHA BANK AD BEOGRAD 180-7191210011857-74", font));
		document.add(new Paragraph("Пиб: 105241068", font));
		
		
		Paragraph preface = new Paragraph("Доставница бр." + porudzbenica.getPorudzbenicaId(), fontBold24);
		
		preface.setAlignment(Element.ALIGN_CENTER);
		document.add(preface);
		
		Paragraph korisnik = new Paragraph(porudzbenica.getKorisnik().getKorisnikNaziv(), font24);
		
		korisnik.setAlignment(Element.ALIGN_CENTER);
		document.add(korisnik);
		
		document.add(new Paragraph("  ", font));
		SimpleDateFormat dt1 = new SimpleDateFormat("dd.MM.yyyy");
		document.add(new Paragraph("Датум:" + dt1.format(porudzbenica.getDatumPorudzbenice()), font));
		
		document.add(new Paragraph("  ", font));

		PdfPTable table = new PdfPTable(5);
		
		table.setWidthPercentage(100);
		table.setWidths(new int[]{2, 14, 4, 5, 6});
		PdfPCell spacer = new PdfPCell(new Phrase(" ", font));
		spacer.setColspan(5);
		
        table.setSpacingBefore(5);
        table.setSpacingAfter(5);
        PdfPCell rbr = new PdfPCell(new Phrase("р.бр.", fontBold));
        rbr.setFixedHeight(35);

        PdfPCell naziv =  new PdfPCell(new Phrase("НАЗИВ", fontBold));
        naziv.setFixedHeight(35);
        PdfPCell ccena = new PdfPCell(new Phrase("ЦЕНА", fontBold));
        ccena.setFixedHeight(35);
        PdfPCell kolicina = new PdfPCell(new Phrase("КОЛИЧИНА", fontBold));
        kolicina.setFixedHeight(35);
        PdfPCell iiznos = new PdfPCell(new Phrase("ИЗНОС", fontBold));
        iiznos.setFixedHeight(35);
        table.addCell(rbr);
        table.addCell(naziv);
        table.addCell(ccena);
        table.addCell(kolicina);
        table.addCell(iiznos);
        
		for (PorudzbenicaStavke stavka : porudzbenica.getPorudzbenicaStavkes()) {
			table.addCell(new PdfPCell(new Phrase(new Integer(stavka.getId().getPorudzbenicaStavkaId()).toString(), font)));
			table.addCell(new PdfPCell(new Phrase(stavka.getRobaCenovnik().getRoba().getRobaNaziv(), font)));

			BigDecimal cena1 =stavka.getRobaCenovnik().getCena();
			table.addCell(new PdfPCell(new Phrase(cena1.toString(), font)));
			table.addCell(new PdfPCell(new Phrase(stavka.getKolicina().toString(), font)));
			StringBuilder sb = new StringBuilder();
			sb.append(cena1.multiply(new BigDecimal(stavka.getKolicina())).toString());
			sb.append(" ");
			sb.append(stavka.getRobaCenovnik().getValuta().getValutaOznaka());
			table.addCell(new PdfPCell(new Phrase(sb.toString(), font)));
		}
		BigDecimal ukupno = new BigDecimal(0);
		if(porudzbenica.getPopust() != null) {

				table.addCell(spacer);
				PdfPCell iznos4 = new PdfPCell(new Phrase("Укупно:", font16));
				iznos4.setColspan(3);
				iznos4.setFixedHeight(25);
				iznos4.setUseBorderPadding(true);
				table.addCell(iznos4);
				
				PdfPCell iznos41 = new PdfPCell(new Phrase(porudzbenica.getCena().toString()+ " " 
						+ porudzbenica.getKorisnik().getValuta().getValutaOznaka(), font16));
				iznos41.setColspan(3);
				iznos41.setFixedHeight(25);
				iznos41.setUseBorderPadding(true);
				table.addCell(iznos41);
				
				table.addCell(spacer);
				PdfPCell iznos = new PdfPCell(new Phrase("Попуст:", font16));
				iznos.setColspan(3);
				iznos.setFixedHeight(25);
				iznos.setUseBorderPadding(true);
				
				PdfPCell iznos3 = new PdfPCell(new Phrase(porudzbenica.getPopust().toString() + "%", font16));
				iznos3.setColspan(1);
				iznos3.setFixedHeight(25);
				iznos3.setUseBorderPadding(true);
				ukupno = porudzbenica.getCena().subtract(porudzbenica.getCena().multiply(porudzbenica.getPopust()).divide(new BigDecimal(100)));
				PdfPCell iznos1 = new PdfPCell(new Phrase(porudzbenica.getCena().subtract(ukupno).toString()+ " " 
												+ porudzbenica.getKorisnik().getValuta().getValutaOznaka(), font16));
				iznos1.setColspan(1);
				iznos1.setFixedHeight(25);
				iznos1.setBorderWidth(2);
				iznos1.setUseBorderPadding(true);
				table.addCell(iznos);
				table.addCell(iznos3);
				table.addCell(iznos1);

		 }else {
			ukupno = porudzbenica.getCena(); 
		 }
		
		table.addCell(spacer);
		PdfPCell iznos = new PdfPCell(new Phrase("За плаћање:", font16));
		iznos.setColspan(3);
		iznos.setFixedHeight(35);
		iznos.setBorderWidth(2);
		iznos.setUseBorderPadding(true);
		
		PdfPCell iznos1 = new PdfPCell(new Phrase(ukupno.toString()+ " " + porudzbenica.getKorisnik().getValuta().getValutaOznaka(), font16));
		iznos1.setColspan(2);
		iznos1.setFixedHeight(35);
		iznos1.setBorderWidth(2);
		iznos1.setUseBorderPadding(true);
		table.addCell(iznos);
		table.addCell(iznos1);
		
		document.add(table);
		
		document.add(new Paragraph("  ", font));
		
		Chunk glue = new Chunk(new VerticalPositionMark());
		Paragraph p = new Paragraph("Издао:", font);
		p.add(new Chunk(glue));
		p.add("Примио");
		document.add(p);
		Paragraph pp = new Paragraph("_______________", font);
		pp.add(new Chunk(glue));
		pp.add("________________");
		document.add(pp);
	}
}
