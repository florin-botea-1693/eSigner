package model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import model.certificates.Certificate;
import model.certificates.CertificatesHolder;
import model.certificates.MSCAPICertificatesHolder;
import utils.PropertyChangeSupportExtended;

abstract public class SigningModel 
{
	
	private File[] selectedFiles = new File[] {};
	private CertificatesHolder certificatesHolder;
	//private PropertyChangeSupportExtended observed = new PropertyChangeSupportExtended(this);

	public File[] getSelectedFiles() {
		return this.selectedFiles;
	}

	public void setSelectedFiles(File[] selectedFiles)
	{
		this.selectedFiles = selectedFiles;
	}

	public SigningModel(CertificatesHolder certificatesHolder) 
	{
		this.certificatesHolder = certificatesHolder;
	}
	
	public CertificatesHolder getCertificatesHolder() {
		return certificatesHolder;
	}
	
	public void addPropertyChangeListener(String prop, PropertyChangeListener propertyChangeListener) 
	{
		//observed.addPropertyChangeListener(prop, propertyChangeListener);
		//propertyChangeListener.propertyChange(new PropertyChangeEvent(this, prop, null, null));
	}
	
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		//observed.removePropertyChangeListener(pcl);
	}
	
	public void setSigningCertificate(Certificate selectedItem) 
	{
		System.out.println("signing certificate is " + selectedItem.toString());
		Certificate oldVal = this.certificatesHolder.getSelectedCertificate();
		certificatesHolder.selectCertificate(selectedItem);
		//observed.firePropertyChange("selectedCertificate", oldVal, selectedItem);
	}
}
