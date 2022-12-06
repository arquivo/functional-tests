package pt.fccn.sobre.arquivo.pages;

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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonQuestionsPage {

	WebDriver driver;
	List< String > CommonQuestionsPT;
	List< String > CommonQuestionsEN;
	private final String dir = "sobreTestsFiles";
	private final int timeout = 50;

	public CommonQuestionsPage( WebDriver driver ) throws FileNotFoundException{
		this.driver = driver;
		CommonQuestionsPT = new ArrayList< String >( );
		CommonQuestionsEN = new ArrayList< String >( );
		if( !loadQuestions( "CommonQuestions_pt.txt" , "pt" ) )
			throw new FileNotFoundException( );

		if( !loadQuestions( "CommonQuestions_en.txt" , "en" ) )
			throw new FileNotFoundException( );
	}


	private boolean loadQuestions( String filename , String language ) {
		try {
			String line;
			InputStreamReader isr = new InputStreamReader(ClassLoader.getSystemResourceAsStream(dir + File.separator + filename), Charset.forName("UTF-8"));
		    BufferedReader br = new BufferedReader(isr);
		    while ((line = br.readLine()) != null) {
				if( language.equals( "pt" ) )
					CommonQuestionsPT.add( line );
				else
					CommonQuestionsEN.add( line );
			}
			br.close( );
			isr.close( );

			//printQuestions( ); //Debug

			return true;
		} catch ( FileNotFoundException exFile ) {
			System.out.println( exFile );
			return false;
		} catch ( IOException exIo ) {
			System.out.println( exIo );
			return false;
		}

	}

	public boolean inspectQuestions( String language ) {
		System.out.println( "[inspectQuestions]" );
    	String xpathDivs;
    	int idx = 0;
    	try{
    		if( language.equals( "EN" ) ) {
    			switchLanguage( );
    			xpathDivs = "//*[@id=\"post-2392\"]/div/div/div/h3"; //TODO verified
    		} else
    			xpathDivs = "//*[@id=\"post-2096\"]/div/div/div/h3";

    		List< WebElement > results = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) )
	                .until( ExpectedConditions
	                        .visibilityOfAllElementsLocatedBy(
	                        		      By.xpath( xpathDivs )
	                        )
	        );

    		if( language.equals( "PT" ) ) {
    			System.out.println( "List["+CommonQuestionsPT.size( )+"] != Results["+results.size( )+"]" );
        		if( CommonQuestionsPT.size( ) != results.size( ) )
        			return false;
    		} else {
    			System.out.println( "List["+CommonQuestionsEN.size( )+"] != Results["+results.size( )+"]" );
        		if( CommonQuestionsEN.size( ) != results.size( ) )
        			return false;
    		}


	        for( WebElement elem : results ) {
	    		String question = elem.getText( );
	    		Charset.forName( "UTF-8" ).encode( question );
	    		System.out.println( "question = " + question + " != " + ( language.equals( "PT" ) ? CommonQuestionsPT.get( idx ) : CommonQuestionsEN.get( idx ) ) );
	    		if( ( !question.equals( CommonQuestionsPT.get( idx ) ) && language.equals( "PT" ) )
	    				||  ( !question.equals( CommonQuestionsEN.get( idx ) ) && language.equals( "EN" ) ) )
	    			return false;

	    		idx++;
	    	}

	    	return true;
    	} catch( Exception e ){
            System.out.println("Error in inspectQuestions");
            System.out.println( e );
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


    @SuppressWarnings("unused")
	private void printQuestions( ){
		for( String question : CommonQuestionsPT ) {
			System.out.println( "QuestionPT => " + question );
		}

		for( String question : CommonQuestionsEN ) {
			System.out.println( "QuestionEN => " + question );
		}
	}

}
