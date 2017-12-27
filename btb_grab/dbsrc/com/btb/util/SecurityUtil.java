package com.btb.util;

import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class SecurityUtil {
	/**
	 * init the TLS1.2 Security
	 */
	public static void initSecurity() {
		SSLContext sslContext = null;
	 	MyX509TrustManager xtm = new MyX509TrustManager();
	 	MyHostnameVerifier hnv = new MyHostnameVerifier();
        try {
            sslContext = SSLContext.getInstance("TLSv1.2"); //或SSL、TLS、TLSv1.0、TLSv1.1（jdk1.7开始支持）、TLS1.2（jdk1.7开始支持）S
            X509TrustManager[] xtmArray = new X509TrustManager[] {xtm};
            sslContext.init(null, xtmArray, new java.security.SecureRandom());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        if (sslContext != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        }
        HttpsURLConnection.setDefaultHostnameVerifier(hnv);
	}
}

class MyHostnameVerifier implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
        //System.out.println("Warning: URL Host: " + hostname + " vs. " + session.getPeerHost());
        return true;
    }
}
class MyX509TrustManager implements X509TrustManager {
    
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }


    public void checkServerTrusted(X509Certificate[] chain, String authType) {
       //System.out.println("cert: " + chain[0].toString() + ", authType: " + authType);
    }


    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}