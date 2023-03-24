package pt.arquivo.tests.cms.pages;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.utils.AnalyzeURLs;

public class AboutPage {

	WebDriver driver;
	private final int timeout = 50;
	
	public AboutPage( WebDriver driver ) throws FileNotFoundException{
		this.driver = driver;
		
		//TODO check page title
	}
	
	public boolean checkAboutLinks( String language ) {
		System.out.println( "[checkAboutLinks]" );
		String xpathVideos 			= "//*[@id=\"parent-fieldname-text\"]/ul[1]/li/a";
		String xpathDissemination 	= "//*[@id=\"parent-fieldname-text\"]/ul[2]/li/a";
    	String xpathTechnical 		= "//*[@id=\"parent-fieldname-text\"]/ul[3]/li/a";
    	/*if( language.equals( "EN" ) )
    		switchLanguage( );*/
    	
		if( !extractLinks( xpathVideos ) )
			return false;
		if( !extractLinks( xpathDissemination ) )
			return false;
		if( !extractLinks( xpathTechnical ) )
			return false;
		
		return true;
	}
	
	
	public boolean extractLinks( String xpath ) {
		try{
    		List< WebElement > results = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) )
	                .until( ExpectedConditions
	                        .visibilityOfAllElementsLocatedBy(
	                        		      By.xpath( xpath )
	                        )
	        );
    		
    		System.out.println( "[Video Links] results size = " + results.size( ) );
    		for( WebElement elem : results ) {
    			String url = elem.getAttribute( "href" );
    			int statusCode = AnalyzeURLs.linkExists( url );
    			if( !AnalyzeURLs.checkOk( statusCode ) ) {
    				System.out.println( "Failed: Url[" + url + "] status-code[" + statusCode + "]" );
    				return false;
    			}
    		}
    		
	    	return true;
    	} catch( NoSuchElementException e ){
            System.out.println( "Error in checkVideoLinks" );
            e.printStackTrace( );
            return false;
    	}
	}
	
    /**
    * Change to the English version
    */
    private void switchLanguage( ){
    	String xpathEnglishVersion = "//*[@id=\"menu-item-4424-en\"]/a";
      	if( driver.findElement( By.xpath( xpathEnglishVersion ) ).getText( ).equals( "English" ) ) {
      		System.out.println( "Change language to English" );
      		driver.findElement( By.xpath( xpathEnglishVersion ) ).click( );
      		IndexSobrePage.sleepThread( ); 
      	}
    }
	
	
}
