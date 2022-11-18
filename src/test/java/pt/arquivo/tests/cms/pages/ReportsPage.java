package pt.arquivo.tests.cms.pages;

import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.List;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.utils.AnalyzeURLs;

public class ReportsPage {

	WebDriver driver;
	private final int timeout = 50;
	
	public ReportsPage( WebDriver driver ) throws FileNotFoundException{
		this.driver = driver;
		
	}
	
	public boolean checkReportsLinks( String language ) {
		System.out.println( "[checkReportsLinks]" );
		String idDiv = "";
		
		if( language.equals( "EN" ) ) {
			idDiv = "post-3196";
			//switchLanguage( );
		} else 
			idDiv = "post-2977";
		
		String xpathDiv = "//*[@id=\"" + idDiv + "\"]/div/div"; //get footer links
		try{
    		
    		
    		WebElement divTag = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
   	                .until(
   	                		ExpectedConditions.presenceOfElementLocated(
   	                				By.xpath( xpathDiv ) ) );
   	        
			//find all the a tags in the div tag
   		   	List< WebElement > allAnchors = divTag.findElements( By.tagName( "a" ) );
       		
    		
    		System.out.println( "[Reports] results size = " + allAnchors.size( ) );
    		for( WebElement elem : allAnchors ) {
    			String url = elem.getAttribute( "href" );
    			int statusCode = AnalyzeURLs.linkExists( url );
    			String text = elem.getText( );
    			Charset.forName( "UTF-8" ).encode( text );
    			if( !AnalyzeURLs.checkOk( statusCode ) ) {
    				System.out.println( "Failed: text[" + text + "] link[" + url + "] status-code[" + statusCode + "]" );
    				return false;
    			}
    		}
    		
	    	return true;
    	} catch( NoSuchElementException e ){
            System.out.println( "Error in checkOPSite" );
            e.printStackTrace( );
            return false;
    	}
	}
}
