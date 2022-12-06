package pt.arquivo.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class AnalyzeURLs {


	/**
	 * Check if link exists
	 * @param URLName
	 * @param text
	 * @return
	 */
	public static int linkExists( String URLName ) {
	    boolean redirect = false;
		try {
	    	System.out.println( "[linkExists] url[" + URLName + "]" );

	    	ignoreSSLCerts();

	    	HttpURLConnection con = ( HttpURLConnection ) new URL( URLName ).openConnection( );
	    	con.setConnectTimeout( 5000 );
	    	con.setRequestMethod( "HEAD" );
	    	con.addRequestProperty( "Accept-Language", "en-US,en;q=0.8" );
	    	con.addRequestProperty( "User-Agent", "Mozilla" );
	    	con.addRequestProperty( "Referer", "google.com" );

	    	// normally, 3xx is redirect
	    	int status = con.getResponseCode( );
	    	System.out.println( "Status-code = " + status );
	  	    redirect = checkRedirect( status );
	    	System.out.println( "[linkExists] url[" + URLName + "] Status-code = " + con.getResponseCode( ) );
	    	con.disconnect( );

	    	while( redirect ) {
	    		// get redirect url from "location" header field
	    		String newUrl = con.getHeaderField( "Location" );
	    		System.out.println( "Redirect: true url["+URLName+"] newurl["+newUrl+"]" );
	    		// get the cookie if need, for login
	    		String cookies = con.getHeaderField( "Set-Cookie" );

	    		// open the new connection again
				con = ( HttpURLConnection ) new URL( newUrl ).openConnection( );
				con.setConnectTimeout( 5000 );
				con.setRequestMethod( "HEAD" );
				if (cookies != null && !cookies.isEmpty()) {
					con.setRequestProperty( "Cookie", cookies );
				}
	    		con.addRequestProperty( "Accept-Language", "en-US,en;q=0.8" );
	    		con.addRequestProperty( "User-Agent", "Mozilla" );
	    		con.addRequestProperty( "Referer", "google.com" );
	    		status = con.getResponseCode( );

	    		URLName = newUrl;
	    		System.out.println( "Novo redirect status = " + status + " message = " + con.getResponseMessage( ) );
	    		redirect = checkRedirect( status );
	    		con.disconnect( );
	    	}

	    	return status;

	    } catch ( MalformedURLException e ) {
	    	System.out.println( "MalformedURLException e = " + e.getMessage( ) );
			return -1;
		} catch ( IOException e ) {
			System.out.println( "IOException e = " + e.getMessage( ) );
			return -1;
		}

	}

	public static void ignoreSSLCerts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[]{
		    new X509TrustManager() {
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		            return null;
		        }
		        public void checkClientTrusted(
		            java.security.cert.X509Certificate[] certs, String authType) {
		        }
		        public void checkServerTrusted(
		            java.security.cert.X509Certificate[] certs, String authType) {
		        }
		    }
		};

		// Install the all-trusting trust manager
		try {
		    SSLContext sc = SSLContext.getInstance("SSL");
		    sc.init(null, trustAllCerts, new java.security.SecureRandom());
		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Check if link exists
	 * @param URLName
	 * @return
	 */
	public static boolean linkExists( String URLName , String text , Map< String, String > textTolink ) {
	    boolean redirect = false;
		try {
	    	System.out.println( "[Footer] url[" + URLName + "]" );
	    	
	    	ignoreSSLCerts();

	    	HttpURLConnection con = ( HttpURLConnection ) new URL( URLName ).openConnection( );
	    	con.setConnectTimeout( 5000 );
	    	con.setRequestMethod( "HEAD" );
	    	con.addRequestProperty( "Accept-Language", "en-US,en;q=0.8" );
	    	con.addRequestProperty( "User-Agent", "Mozilla" );
	    	con.addRequestProperty( "Referer", "google.com" );

	    	// normally, 3xx is redirect
	    	int status = con.getResponseCode( );
	    	System.out.println( "Status-code = " + status );
	    	redirect = checkRedirect( status );

	    	System.out.println( "[Footer] url[" + URLName + "] Status-code = " + con.getResponseCode( ) );
	    	con.disconnect( );

	    	while( redirect ) {
	    		// get redirect url from "location" header field
	    		String newUrl = con.getHeaderField( "Location" );
	    		System.out.println( "Redirect: true url["+URLName+"] newurl["+newUrl+"]" );
	    		// get the cookie if need, for login
	    		String cookies = con.getHeaderField( "Set-Cookie" );

	    		// open the new connection again
				con = ( HttpURLConnection ) new URL( newUrl ).openConnection( );
				con.setConnectTimeout( 5000 );
				con.setRequestMethod( "HEAD" );
	    		con.setRequestProperty( "Cookie", cookies );


		    	con.addRequestProperty( "Accept-Language", "en-US,en;q=0.8" );
	    		con.addRequestProperty( "User-Agent", "Mozilla" );
	    		con.addRequestProperty( "Referer", "google.com" );
	    		status = con.getResponseCode( );

	    		URLName = newUrl;
	    		System.out.println( "Novo redirect status = " + status + " message = " + con.getResponseMessage( ) );
	    		redirect = checkRedirect( status );
	    		con.disconnect( );
	    	}

	    	System.out.println( "Compare textTolink.get( "+text+" ) = " + textTolink.get( text ) + " URLName = " + URLName + " Status-code = " + status );

	    	if( status == HttpURLConnection.HTTP_OK && textTolink.get( text ).trim( ).equals( URLName.trim( ) ) )
	    		return true;
	    	else
	    		return false;

	    } catch ( MalformedURLException e ) {
	    	System.out.println( "MalformedURLException e = " + e.getMessage( ) );
			return false;
		} catch ( IOException e ) {
			System.out.println( "IOException e = " + e.getMessage( ) );
			return false;
		}

	}


	/**
	 * Check if link exists
	 * @param URLName
	 * @param text
	 * @return
	 */
	public static boolean checkLink( String URLName ) {
		String url = URLName.startsWith("//") ? "http:" + URLName : URLName;

	    boolean redirect = false;
		try {
	    	System.out.println( "[linkExists] url[" + url + "]" );
	    	
	    	ignoreSSLCerts();

	    	HttpURLConnection con = ( HttpURLConnection ) new URL( url ).openConnection( );
	    	con.setConnectTimeout( 5000 );
	    	con.setRequestMethod( "HEAD" );
	    	con.addRequestProperty( "Accept-Language", "en-US,en;q=0.8" );
	    	con.addRequestProperty( "User-Agent", "Mozilla" );
	    	con.addRequestProperty( "Referer", "google.com" );

	    	// normally, 3xx is redirect
	    	int status = con.getResponseCode( );
	    	System.out.println( "Status-code = " + status );
	  	    redirect = checkRedirect( status );
	    	System.out.println( "[linkExists] url[" + url + "] Status-code = " + con.getResponseCode( ) );
	    	con.disconnect( );

	    	while( redirect ) {
	    		// get redirect url from "location" header field
	    		String newUrl = con.getHeaderField( "Location" );
	    		System.out.println( "Redirect: true url["+url+"] newurl["+newUrl+"]" );

	    		// open the new connection again
				con = ( HttpURLConnection ) new URL( newUrl ).openConnection( );
				con.setConnectTimeout( 5000 );
				con.setRequestMethod( "HEAD" );
	    		con.addRequestProperty( "Accept-Language", "en-US,en;q=0.8" );
	    		con.setRequestProperty( "Accept-Charset" , "UTF-8" );
	    		con.setRequestProperty( "Content-Type" , "application/x-www-form-urlencoded;charset=UTF-8" );
	    		con.addRequestProperty( "User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0" );
	    		con.addRequestProperty( "Referer", "google.com" );
	    		status = con.getResponseCode( );

	    		url = newUrl;
	    		System.out.println( "Link["+url+"] Novo redirect status = " + status + " message = " + con.getResponseMessage( ) );
	    		redirect = checkRedirect( status );
	    		con.disconnect( );
	    	}
	    	System.out.println( "return status-code["+status+"]" );
	    	return checkOk( status );

	    } catch ( MalformedURLException e ) {
	    	System.out.println( "MalformedURLException e = " + e.getMessage( ) );
			return false;
		} catch ( IOException e ) {
			System.out.println( "IOException e = " + e.getMessage( ) );
			return false;
		}

	}


	/**
	 *
	 * @param status
	 * @return
	 */
	public static boolean checkRedirect( int status ){
		if ( status != HttpURLConnection.HTTP_OK ) {
    		if ( status == HttpURLConnection.HTTP_MOVED_TEMP
    			|| status == HttpURLConnection.HTTP_MOVED_PERM
    				|| status == HttpURLConnection.HTTP_SEE_OTHER )
    		return true;
    	}
		return false;
	}

	/**
	 *
	 * @param status
	 * @return
	 */
	public static boolean checkOk( int status ){
		return ( status == HttpURLConnection.HTTP_OK );
	}


}
