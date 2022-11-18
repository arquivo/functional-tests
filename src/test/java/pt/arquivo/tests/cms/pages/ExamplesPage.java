package pt.arquivo.tests.cms.pages;

import java.util.List;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.utils.AnalyzeURLs;

public class ExamplesPage {
	
	WebDriver driver;
	private final int timeout = 50;
	
	public ExamplesPage( WebDriver driver ) {
		this.driver = driver;
		
	}

	/**
	 * Test links Examples
	 * @return
	 */
	public boolean checkLinksExamples( String language ) {
		System.out.println( "[checkLinksExamples]" );
    	String xpatha = ""; 
    	try{
    		
    		if( language.equals( "EN" ) ) {  
    			switchLanguage( );
    			xpatha = "//*[@id=\"post-2357\"]/div/div/div/div/div/div/p[2]/a"; //TODO verified
    		} else
    			xpatha = "//*[@id=\"post-1861\"]/div/div/div/div/div/div/p[2]/a"; //*[@id="post-1861"]/div
    		             
    		
    		List< WebElement > results = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) )
	                .until( ExpectedConditions
	                        .visibilityOfAllElementsLocatedBy(
	                        		      By.xpath( xpatha )
	                        )
	        );
    		
    		System.out.println( "results size = " + results.size( ) );
    		for( WebElement elem : results ) {
    			String url = elem.getAttribute( "href" );
    			int statusCode = AnalyzeURLs.linkExists( url );
    			if( !AnalyzeURLs.checkOk( statusCode ) )
    				return false;
    		}
    		
	    	return true;
    	} catch( Exception e ){
            System.out.println( "Error in checkOPSite" );
            e.printStackTrace( );
            return false;
    	}
	}

    /**
    * Change to the English version
    */ 
    private void switchLanguage( ){
    	String xpathEnglishVersion = "//*[@id=\"menu-item-4424-en\"]/a"; //*[@id="menu-item-4424-en"]/a
      	if( driver.findElement( By.xpath( xpathEnglishVersion ) ).getText( ).equals( "English" ) ) {
      		System.out.println( "Change language to English" );
      		driver.findElement( By.xpath( xpathEnglishVersion ) ).click( );
      		IndexSobrePage.sleepThread( );
      	}
    }
	
}
