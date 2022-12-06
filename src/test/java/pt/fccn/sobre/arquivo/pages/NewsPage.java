package pt.fccn.sobre.arquivo.pages;

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

import pt.fccn.arquivo.tests.util.AnalyzeURLs;

public class NewsPage {


	WebDriver driver;
	private final int timeout = 50;

	public NewsPage( WebDriver driver ) throws FileNotFoundException{
		this.driver = driver;


	}


	/**
	 *
	 * @param language
	 * @return
	 */
	public boolean checkNewsLinks( String language ) {
		System.out.println( "[checkLinks]" );
		String idDiv = "";
		String idaRss = "";

		if( language.equals( "EN" ) ) {
			idDiv = "post-2336";
			switchLanguage( );
		} else {
			idDiv = "post-1857";
		}

		System.out.println( "Page title = " + driver.getTitle( ) );

		String xpathDiv = "//*[@id=\"" + idDiv + "\"]/div/div/aside/div"; //get news links

		try{
    		WebElement divTag = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
   	                .until(
   	                		ExpectedConditions.presenceOfElementLocated(
   	                				By.xpath( xpathDiv ) ) );

			//find all the a tags in the div tag
   		   	List< WebElement > allAnchors = divTag.findElements( By.tagName( "a" ) );


    		System.out.println( "[footer] results size = " + allAnchors.size( ) );
    		for( WebElement elem : allAnchors ) {
    			String url = elem.getAttribute( "href" );
    			int statusCode = AnalyzeURLs.linkExists( url );
    			if( !AnalyzeURLs.checkOk( statusCode ) ) {
    				System.out.println( "Failed: Url[" + url + "] status-code[" + statusCode + "]" );
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
