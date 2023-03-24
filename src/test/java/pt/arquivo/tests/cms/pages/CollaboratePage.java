package pt.arquivo.tests.cms.pages;

import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.utils.AnalyzeURLs;

public class CollaboratePage {
	
	WebDriver driver;
	private final int timeout = 50;
	
	/**
	 * 
	 * @param driver
	 * @throws FileNotFoundException
	 */
	public CollaboratePage( WebDriver driver ) throws FileNotFoundException{
		this.driver = driver;
	}
	
	
	/**
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public SuggestionPage goToSuggestionSitePage( ) throws FileNotFoundException{
		
        try{
            System.out.println( "Start goToSuggestionSitePage() method" );
            WebElement suggestionSiteLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
            .until(
            		ExpectedConditions.presenceOfElementLocated(
            				By.xpath( "//*[@id=\"parent-fieldname-text\"]/ul/li[2]/a" )
            				)
            		);            
            suggestionSiteLink.click( );
            System.out.println( "Finished goToSuggestionSitePage() method" );
        }catch( NoSuchElementException e ){
        	System.out.println( "Could not find the link element" );
        	throw e;
        }
		
		return new SuggestionPage( driver );
	}
	
	
	public boolean checkCollaborateLinks( String language ) {
		System.out.println( "[checkCollaborateLinks]" );
		String xpatha = "//*[@id=\"parent-fieldname-text\"]/ul/li/a"; 
    	
	/*	if( language.equals( "EN" ) ) //redirect to english version
    		switchLanguage( );*/
    		
		try{
    		List< WebElement > results = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) )
	                .until( ExpectedConditions
	                        .visibilityOfAllElementsLocatedBy(
	                        		      By.xpath( xpatha )
	                        )
	        );
    		
    		System.out.println( "[Collaborate Links] results size = " + results.size( ) );
    		for( WebElement elem : results ) {
    			String url = elem.getAttribute( "href" );
    			int statusCode = AnalyzeURLs.linkExists( url );
    			String text = elem.getText( );
    			Charset.forName( "UTF-8" ).encode( text );
    			System.out.println( "Failed: text["+text+"] link[" + url + "] status-code[" + statusCode + "]" );
    			
    			/*if( !AnalyzeURLs.checkOk( statusCode ) ) {
    				System.out.println( "Failed: text["+text+"] link[" + url + "] status-code[" + statusCode + "]" );
    				return false;
    			}*/
    		}
    		
	    	return true;
    	} catch( NoSuchElementException e ){
            System.out.println( "Error in checkCollaborateLinks" );
            e.printStackTrace( );
            return false;
    	}
		
	}
	
    /**
    * Change to the English version
    */
    private void switchLanguage( ){
    	String xpathEnglishVersion = "//*[@id=\"menu-item-4424-en\"]/a";
    	//TODO //*[@id=\"menu-item-3862-en\"]/a -> new template 
      	if( driver.findElement( By.xpath( xpathEnglishVersion ) ).getText( ).equals( "English" ) ) {
      		System.out.println( "Change language to English" );
      		driver.findElement( By.xpath( xpathEnglishVersion ) ).click( );
      		IndexSobrePage.sleepThread( );
      	}
    }
	
}
