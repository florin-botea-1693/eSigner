package tests;

import model.certificates.Certificate;
import model.certificates.MSCAPICertificatesHolder;

public class MSCAPICertificatesHolderTest {
	
	public static void main(String[] args) {
		MSCAPICertificatesHolder holder = new MSCAPICertificatesHolder();
		Certificate cert = holder.getCertificates().get(0);
		System.out.println( cert.getName() );
		System.out.println( cert.getPrincipal().getName() );
	}
}
