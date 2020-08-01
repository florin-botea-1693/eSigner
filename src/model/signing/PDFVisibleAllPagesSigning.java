package model.signing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.model.pades.SignatureImageParameters;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.token.SignatureTokenConnection;
import model.certificates.Certificate;
import model.certificates.CertificatesHolder;
import model.signing.visible.SignatureAspect;

public class PDFVisibleAllPagesSigning implements SigningMode {

	private CertificatesHolder certificatesHolder;
	private PAdESService service;
	private PAdESSignatureParameters padesParameters;
	private SignatureAspect signatureAspect;

	public PDFVisibleAllPagesSigning(CertificatesHolder certificatesHolder, PAdESService service, PAdESSignatureParameters padesParameters, SignatureAspect signatureAspect) 
	{
		this.certificatesHolder = certificatesHolder;
		this.service = service;
		this.padesParameters = padesParameters;
		this.signatureAspect = signatureAspect;
	}

	@Override
	public void performSign(File file) throws FileNotFoundException, IOException {
		System.out.println("Signing with visible signature on all pages");
		InputStream resource = new FileInputStream(file);
		String result = file.getAbsolutePath() + "-semnat.pdf";

		PDDocument pdDocument = PDDocument.load(resource);
	    PDAcroForm acroForm = pdDocument.getDocumentCatalog().getAcroForm();
	    if (acroForm == null)
	        pdDocument.getDocumentCatalog().setAcroForm(acroForm = new PDAcroForm(pdDocument));
	    acroForm.setSignaturesExist(true);
	    acroForm.setAppendOnly(true);
	    acroForm.getCOSObject().setDirect(true);
	    List<PDField> acroFormFields = acroForm.getFields();

	    PDSignatureField signatureField = new PDSignatureField(acroForm);
	    acroFormFields.add(signatureField);
	    SignatureImageParameters sip = this.signatureAspect.getSIP();
	    for (PDPage pdPage : pdDocument.getPages()) {
	        addSignatureField(pdDocument, pdPage, sip, signatureField);
	    }
	    pdDocument.getDocumentCatalog().getCOSObject().setNeedToBeUpdated(true);
	    pdDocument.save(result);
      	pdDocument.close();
      	
      	// gasim campul de semnatura
      	FileDocument toSignDocument = new FileDocument(result);
		this.service.getAvailableSignatureFields(toSignDocument);
		this.padesParameters.setSignatureFieldId(this.service.getAvailableSignatureFields(toSignDocument).get(0));
		this.padesParameters.setImageParameters(this.signatureAspect.getSIP());
      	System.out.println(this.service.getAvailableSignatureFields(toSignDocument).get(0));
      	SignatureTokenConnection token = this.certificatesHolder.getToken();
      	Certificate cert = this.certificatesHolder.getSelectedCertificate();
		// semneaza campul respectiv
		ToBeSigned dataToSign = this.service.getDataToSign(toSignDocument, this.padesParameters);
		SignatureValue signatureValue = token.sign(dataToSign, this.padesParameters.getDigestAlgorithm(), cert.getPrivateKey());
		DSSDocument signedDocument = this.service.signDocument(toSignDocument, this.padesParameters, signatureValue);
		signedDocument.save(result);
	}

	//=======================||
	// HELPERS
	//=======================||
	public void addSignatureField(PDDocument pdDocument, PDPage pdPage, SignatureImageParameters sip, PDSignatureField signatureField) throws IOException {
	    
	    PDAnnotationWidget widget = signatureField.getWidgets().get(0);
	    float posX = this.getSignaturePosX(pdPage, sip); float posY = this.getSignaturePosY(pdPage, sip);
	    PDRectangle rect = new PDRectangle(posX, posY, sip.getWidth(), sip.getHeight());
	    widget.setRectangle(rect);
	    widget.setPage(pdPage);
	    widget.getCOSObject().setNeedToBeUpdated(true);

	    PDStream stream = new PDStream(pdDocument);
	    PDFormXObject form = new PDFormXObject(stream);
	    PDResources res = new PDResources();
	    form.setResources(res);
	    form.setFormType(1);
	    PDRectangle bbox = new PDRectangle(rect.getWidth(), rect.getHeight());
	    float height = bbox.getHeight();
	    form.setBBox(bbox);

	    PDAppearanceDictionary appearance = new PDAppearanceDictionary();
	    appearance.getCOSObject().setDirect(true);
	    PDAppearanceStream appearanceStream = new PDAppearanceStream(form.getCOSObject());
	    appearance.setNormalAppearance(appearanceStream);
	    widget.setAppearance(appearance);
	    
	    pdPage.getAnnotations().add(widget);
	    pdPage.getCOSObject().setNeedToBeUpdated(true);
	}
	
	public float getSignaturePosX(PDPage pdPage, SignatureImageParameters sip) {
		float x = 0;
		switch (sip.getAlignmentHorizontal()) {
			case LEFT:
			break;
			case CENTER:
				x = (pdPage.getMediaBox().getWidth() - sip.getWidth())/2;
			break;
			case RIGHT:
				x = pdPage.getMediaBox().getWidth() - sip.getWidth();
			break;
		}
		return x;
	}
	
	public float getSignaturePosY(PDPage pdPage, SignatureImageParameters sip) {
		float y = 0;
		switch (sip.getAlignmentVertical()) {
			case TOP:
				y = pdPage.getMediaBox().getHeight() - sip.getHeight();
			break;
			case MIDDLE:
				y = (pdPage.getMediaBox().getHeight() - sip.getHeight())/2;
			break;
			case BOTTOM:
			break;
		}
		return y;
	}
}
