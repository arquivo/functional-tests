package pt.arquivo.tests.cms.pages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchPage {
	WebDriver driver;
	private final String dir = "sobreTestsFiles";
	List< String > topicsPT;
	List< String > topicsEN;
	private final int timeout = 50;

	public SearchPage( WebDriver driver ) throws FileNotFoundException{
		this.driver = driver;
		topicsPT = new ArrayList< String >( );
		topicsEN = new ArrayList< String >( );
		if( !loadTopics( "SearchLinksPT.txt" , "pt" ) )
			throw new FileNotFoundException( );

		if( !loadTopics( "SearchLinksEN.txt" , "en" ) )
			throw new FileNotFoundException( );

	}

	public boolean checkSearch( String language ) {
		System.out.println( "[checkSearch]" );
		//String xpathResults = "//*[@id=\"search-4\"]/form/label/input"; //get search links
		String xpathResults = "//*[@id=\"gsc-i-id1\"]";

		try{

			if( language.equals( "EN" ) )
				if( searchEN( xpathResults ) )
					return true;
				else
					return false;
			else
				if( searchPT( xpathResults ) )
					return true;
				else
					return false;

    	}catch( NoSuchElementException e ){
            System.out.println( "Error in checkOPSite" );
            e.printStackTrace( );
            return false;
    	}

	}


	/**
	 * Checks that the topic is highlighted in the snippet of a search result
	 */
	private boolean checkResults( String topic ) {
		System.out.println( "[checkResults]" );
		try{
			List< WebElement > highlights = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) )
				.until( ExpectedConditions.presenceOfAllElementsLocatedBy( By.cssSelector( ".gsc-webResult .gs-snippet b" ) ) );
			for( WebElement highlight : highlights ) {
				String text = highlight.getText( );
				System.out.println( "[checkResults] text["+text+"] equals topic["+topic+"]" );
				if( text.equalsIgnoreCase( topic ) )
					return true;
			}
			return false;
		} catch( TimeoutException e ){
			System.out.println( "No search results for " + topic );
			return false;
		}
	}

	/*private boolean checkResults( String topic ) {
		System.out.println( "[checkResults]" );
		String xpathResults = "//*[@id=\"___gcse_0\"]/div/div/div/div[5]/div[2]/div/div/div[3]"; //get search links
		String xpathText = "//div/div/div/table/tbody/tr/td/div[@class=\"gs-bidi-start-align gs-snippet\"]/b[1]";
		String teste = "//div/div/div/table/tbody/tr/td/div[@class=\"gs-bidi-start-align gs-snippet\"]";
		// //*[@id="___gcse_0"]/div/div/div/div[5]/div[2]/div/div/div[3]/div[1]/div[1]/table/tbody/tr/td[2]/div[3]
		try{sleepThread( )
    		WebElement divElem = ( new WebDriverWait( driver, Duration.ofSeconds(timeout + 40 )) )
	                .until( ExpectedConditions
	                			.presenceOfElementLocated(
	                        		      By.xpath( xpathResults )
	                        )
	        );

    		//System.out.println( "[checkSearch] results size = " + divElem.getAttribute( "innerHTML" ) );
    		System.out.println( "HTML => " + divElem.getAttribute( "innerHTML" ));

    		List< WebElement > results = divElem.findElements( By.xpath( xpathText ) );

    		WebElement test = divElem.findElement( By.xpath( xpathText ) );
    		System.out.println( "HTML TESTE ===> " + test.getText( ) );
    		System.out.println( "Number of results = " + results.size( ) );
   			for( WebElement elem : results ) {
   				String text = elem.getText( );
				Charset.forName( "UTF-8" ).encode( text );

				if( elem.getText( ) == null || elem.getText( ).equals( "" ) )
					continue;

				if( !text.toLowerCase( ).equals( topic.toLowerCase( ) ) ){
					System.out.println( "Failed text["+text+"] not contains topic["+topic+"]" );
					return true;
				}
				System.out.println( "Success text["+text+"] equals topic["+topic+"]" );
   			}

	    	return true;
    	} catch( NoSuchElementException e ){
            System.out.println( "Error in checkOPSite" );
            e.printStackTrace( );
            return false;
    	}
	}*/

	private boolean searchEN( String xpathResults ) {
		System.out.println( "[searchEN]" );
        for( String topic : topicsEN ) {
			WebElement emailElement = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
	                .until(
	                		ExpectedConditions.presenceOfElementLocated(
	                				By.xpath( xpathResults ) ) );
	        emailElement.clear( );
	        emailElement.sendKeys( topic );


	        IndexSobrePage.sleepThread( );

	        // the search button is hidden on narrow windows
	        emailElement.sendKeys( Keys.ENTER );

	        sleepThread( );

	        if( !checkResults( topic ) )
	        	return false;
		}
        return true;
	}

	private boolean searchPT( String xpathResults ) {
		System.out.println( "[searchPT]" );
        for( String topic : topicsPT ) {
        	System.out.println( "Search for " + topic );
    		WebElement emailElement = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
                    .until(
                    		ExpectedConditions.presenceOfElementLocated(
                    				By.xpath( xpathResults ) ) );
            emailElement.clear( );
            emailElement.sendKeys( topic );

            IndexSobrePage.sleepThread( );

            // the search button is hidden on narrow windows
            emailElement.sendKeys( Keys.ENTER );

            sleepThread( );

            if( !checkResults( topic ) )
            	return false;
            //break; //TODO debug break - REMOVE !!!!
        }
        return true;
	}

	private boolean loadTopics( String filename , String language ) {
		try {
			String line;
		    InputStreamReader isr = new InputStreamReader( ClassLoader.getSystemResourceAsStream ( dir.concat( File.separator ).concat( filename ) ), Charset.forName( "UTF-8" ) );
		    BufferedReader br = new BufferedReader(isr);
		    while ( ( line = br.readLine( ) ) != null ) {
				if( language.equals( "pt" ) )
					topicsPT.add( line );
				else
					topicsEN.add( line );
			}
			br.close( );
			isr.close( );

			//printQuestions( ); Info Debug

			return true;
		} catch ( FileNotFoundException exFile ) {
			exFile.printStackTrace( );
			return false;
		} catch ( IOException exIo ) {
			exIo.printStackTrace( );
			return false;
		}

	}


	private void sleepThread( ) {
		try {
			Thread.sleep( 6000 );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
