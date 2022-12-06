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

public class PresentationsPage {
	
	WebDriver driver;
	private final int timeout = 50;
	
	public PresentationsPage( WebDriver driver ) throws FileNotFoundException{
		this.driver = driver;

		
	}
	
	public boolean checkPresentationLinks( String language ) {
		System.out.println( "[checkPresentationLinks]" );
		String idDiv = "";
		 
		if( language.equals( "PT" ) )
			idDiv = "post-3036";
		else{
			//switchLanguage( );
			idDiv = "post-3219";
		}
			
		
		String xpatha = "//*[@id=\""+ idDiv +"\"]/div/div/ul/li/a";
		try{
    		List< WebElement > results = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) )
	                .until( ExpectedConditions
	                        .visibilityOfAllElementsLocatedBy(
	                        		      By.xpath( xpatha )
	                        )
	        );
    		
    		System.out.println( "[Presentation Links] results size = " + results.size( ) );
    		for( WebElement elem : results ) {
    			String url = elem.getAttribute( "href" );
    			int statusCode = AnalyzeURLs.linkExists( url );
    			String text = elem.getText( );
    			Charset.forName( "UTF-8" ).encode( text );
    			System.out.println( "[Presentation Links]Failed: text["+text+"] link[" + url + "] status-code[" + statusCode + "]" );
    			/*if( !AnalyzeURLs.checkOk( statusCode ) ) {
    				System.out.println( "[Presentation Links]Failed: text["+text+"] link[" + url + "] status-code[" + statusCode + "]" );
    				return false;
    			}*/
    		}
    		
	    	return true;
    	} catch( NoSuchElementException e ){
            System.out.println( "Error in checkPresentationLinks" );
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
