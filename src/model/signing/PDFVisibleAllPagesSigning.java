package model.signing;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.model.pades.SignatureImageParameters;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentHorizontal;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentVertical;
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
	    for (PDPage pdPage : pdDocument.getPages()) {
	        addSignatureField(pdDocument, pdPage, signatureField);
	    }
	    pdDocument.getDocumentCatalog().getCOSObject().setNeedToBeUpdated(true);
	    pdDocument.save(result);
      	pdDocument.close();
      	
      	// gasim campul de semnatura
      	FileDocument toSignDocument = new FileDocument(result);
		service.getAvailableSignatureFields(toSignDocument);
		padesParameters.setSignatureFieldId(this.service.getAvailableSignatureFields(toSignDocument).get(0));
		SignatureImageParameters sip = signatureAspect.generateSignatureImageParameters(pdDocument, 1);
		padesParameters.setImageParameters(sip);

      	SignatureTokenConnection token = certificatesHolder.getToken();
      	Certificate cert = certificatesHolder.getSelectedCertificate();
		// semneaza campul respectiv
		ToBeSigned dataToSign = service.getDataToSign(toSignDocument, padesParameters);
		SignatureValue signatureValue = token.sign(dataToSign, padesParameters.getDigestAlgorithm(), cert.getPrivateKey());
		DSSDocument signedDocument = service.signDocument(toSignDocument, padesParameters, signatureValue);
		signedDocument.save(result);
	}

	//=======================||
	// HELPERS
	//=======================||
	public void addSignatureField(PDDocument pdDocument, PDPage pdPage, PDSignatureField signatureField) throws IOException 
	{
	    PDAnnotationWidget widget = signatureField.getWidgets().get(0);
	    Rectangle r = signatureAspect.simulateInPage(pdPage);
	    //PDRectangle rect = new PDRectangle((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
	    
	    //float posX = this.getSignaturePosX(pdPage, (int) r.getWidth());
	    //float posY = this.getSignaturePosY(pdPage, (int) r.getHeight());
	    double posY = pdPage.getMediaBox().getHeight() - r.getY() - r.getHeight();
	    PDRectangle rect = new PDRectangle((int) r.getX(), (int) posY, (int) r.getWidth(), (int) r.getHeight());
	    widget.setRectangle(rect);
	    widget.setPage(pdPage);
	    widget.getCOSObject().setNeedToBeUpdated(true);

	    PDStream stream = new PDStream(pdDocument);
	    PDFormXObject form = new PDFormXObject(stream);
	    PDResources res = new PDResources();
	    form.setResources(res);
	    form.setFormType(1);
	    PDRectangle bbox = new PDRectangle(rect.getWidth(), rect.getHeight());
	    //float height = bbox.getHeight();
	    form.setBBox(bbox);

	    PDAppearanceDictionary appearance = new PDAppearanceDictionary();
	    appearance.getCOSObject().setDirect(true);
	    PDAppearanceStream appearanceStream = new PDAppearanceStream(form.getCOSObject());
	    appearance.setNormalAppearance(appearanceStream);
	    widget.setAppearance(appearance);
	    
	    pdPage.getAnnotations().add(widget);
	    pdPage.getCOSObject().setNeedToBeUpdated(true);
	}
	
	// o mut de aici.... si o fac generala....
	private float getSignaturePosX(PDPage pdPage, float signatureWidth) 
	{
		VisualSignatureAlignmentHorizontal alignment = signatureAspect.getPosition().getHorizontalAlignment();
		float visibleRange = pdPage.getMediaBox().getWidth() - signatureWidth;
		
		if (alignment == VisualSignatureAlignmentHorizontal.LEFT)
			return 0;
		
		if (alignment == VisualSignatureAlignmentHorizontal.CENTER)
			return visibleRange/2;
		
		if (alignment == VisualSignatureAlignmentHorizontal.RIGHT)
			return visibleRange;
		
		return (float) (signatureAspect.getCustomPosition().getX() * visibleRange / 100);
	}
	
	private float getSignaturePosY(PDPage pdPage, float signatureHeight) 
	{
		VisualSignatureAlignmentVertical alignment = signatureAspect.getPosition().getVerticalAlignment();
		float visibleRange = pdPage.getMediaBox().getHeight() - signatureHeight;
		
		if (alignment == VisualSignatureAlignmentVertical.TOP)
			return visibleRange;
			
		if (alignment == VisualSignatureAlignmentVertical.MIDDLE)
			return visibleRange/2;
		
		if (alignment == VisualSignatureAlignmentVertical.BOTTOM)
			return signatureHeight;
		
		double invertedPctY = 100 - signatureAspect.getCustomPosition().getY();
		return (float) (invertedPctY * visibleRange / 100);
	}
}
