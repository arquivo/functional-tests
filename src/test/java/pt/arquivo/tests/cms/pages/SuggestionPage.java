package pt.arquivo.tests.cms.pages;

import java.io.FileNotFoundException;
import java.util.List;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SuggestionPage {

	WebDriver driver;
	private final int timeout = 50;
	String xpathiframe		= "//*[@id=\"wpcf7-f2384-p2063-o1\"]/form/div[5]/div/div/div/iframe";

	private String handlePrincipal;
	private String handleCheckBox;
	private String handleImages;
	private WebDriverWait wait;
	private WebElement checkBox;
	private WebElement iframeCheck; 
	
	public SuggestionPage( WebDriver driver ) throws FileNotFoundException{
		this.driver = driver;
	}
	
	
	public boolean sendSuggestion( String language ) {
		System.out.println( "[sendSuggestion]" );
		try{
			String xpathSites 		= "//*[@id=\"wpcf7-f2384-p2063-o1\"]/form/div[2]/span/textarea";
			String xpathDescription = "//*[@id=\"wpcf7-f2384-p2063-o1\"]/form/div[3]/span/input";
			String xpathEmail 		= "//*[@id=\"wpcf7-f2384-p2063-o1\"]/form/div[4]/span/input";
			String xpathSendButton 	= "//*[@id=\"wpcf7-f2384-p2063-o1\"]/form/p/input";
			String xpathCaptcha 	= "//*[@id=\"recaptcha-anchor\"]/div[5]";
			
			
	      	WebElement UrlsElement = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
	            .until( 
	            		ExpectedConditions.presenceOfElementLocated( 
	            				By.xpath( xpathSites ) ) );             
	      	UrlsElement.clear( );
	      	UrlsElement.sendKeys( "[Test Selenium] Url field." );
	        
	        WebElement descriptionElement = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
	            .until(
	            		ExpectedConditions.presenceOfElementLocated( 
	            				By.xpath( xpathDescription ) ) );
	        descriptionElement.clear( );
	        descriptionElement.sendKeys( "[Test Selenium] Description field." );
	
	        WebElement emailElement = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
	                .until(
	                		ExpectedConditions.presenceOfElementLocated( 
	                				By.xpath( xpathEmail ) ) );
	        emailElement.clear( );
	        emailElement.sendKeys( "[Test Selenium] Email field." );
	        
	     /*   WebElement iframeSwitch = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) 
	                .until(
	                		ExpectedConditions.presenceOfElementLocated( 
	                				By.xpath( xpathiframe ) ) );
	        
	        driver.switchTo( ).frame( iframeSwitch ); // switch to Captcha iframe
	        System.out.println( "Switched to iframe" );
	        driver.findElement( By.xpath( xpathCaptcha ) ).click( );
	        
	        driver.switchTo( ).defaultContent( ); */
	        // switch default content
	        captchaRobot( );
	        
	        IndexSobrePage.sleepThread( );
	        
	        WebElement btnSubmitElement = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
	            .until(
	            		ExpectedConditions.presenceOfElementLocated(
	            				By.id( xpathSendButton ) ) );
	        btnSubmitElement.click( );
	            
			return true;
			
		}catch( NoSuchElementException e ){
	    	System.out.println( "Could not find the link element" );
	    	throw e;
	    }
	
	}
	
	
	private void inicializar( ) {
		handlePrincipal = driver.getWindowHandle( );
		System.out.println("handlePrincipal -> " + handlePrincipal);
		WebElement iframeSwitch = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) 
	                .until(
	                		ExpectedConditions.presenceOfElementLocated( 
	                				By.xpath( xpathiframe ) ) );

		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		driver.switchTo().frame( iframeSwitch );
		handleCheckBox = driver.getWindowHandle( );
		System.out.println( "handleCheckBox -> " + handleCheckBox );

		wait = new WebDriverWait( driver, Duration.ofSeconds(20 ));
		checkBox = wait.until(
				ExpectedConditions.presenceOfElementLocated(
						By.id( "recaptcha-anchor" ) ) );

		Actions builder = new Actions( driver );

		builder.moveToElement( checkBox ).perform( );
		checkBox.click( );

	}
	
	private void captchaRobot( ) {


		inicializar( );
		IndexSobrePage.sleepThread( );

		boolean continuar = true;

		do {
			try {
				wait = new WebDriverWait( driver, Duration.ofSeconds(timeout - 30 ));
				driver.switchTo( ).window( handlePrincipal );
				for ( WebElement frame : driver.findElements( By.tagName( "iframe" ) ) ) {
					System.out.println( "FRAME -> " + frame.getAttribute( "title" ) );
				}
 
				List< WebElement > frames = wait.until(
						ExpectedConditions.presenceOfAllElementsLocatedBy(
								By.tagName("iframe") ) );
				try {
					driver.findElement(By.xpath("//span[contains(@class, 'rc-anchor-error-msg')]"));
					inicializar();
				} catch ( NoSuchElementException csee ) { }
				
				while ( frames.size( ) < 2 ) {
					wait = new WebDriverWait( driver, Duration.ofSeconds(20 ));
					frames = wait.until(
							ExpectedConditions.presenceOfAllElementsLocatedBy(
									By.tagName( "iframe" ) ) );
				}

				WebElement elementFrameTable = driver.findElements( 
						By.tagName( "iframe" ) ).get( 1 );
				System.out.println("name iframe table -> " + elementFrameTable.getAttribute( "title" ) );
				System.out.println("HTML iframe table -> " + elementFrameTable.getAttribute( "innerHTML" ) );
				driver.switchTo( ).frame( elementFrameTable.getAttribute( "title" ) );
				handleImages = driver.getWindowHandle( );
				System.out.println("handleImages -> " + handleImages);

				wait = new WebDriverWait(driver, Duration.ofSeconds(20));
				List<WebElement> imagens = wait.until(
						ExpectedConditions.presenceOfAllElementsLocatedBy(
								By.xpath( "//div[contains(@class, 'rc-image-tile-target')]" ) ) );

				Actions builderImagem1 = new Actions( driver );
				builderImagem1.moveToElement( imagens.get( 0 ) ).perform( );
				imagens.get( 0 ).click( );

				IndexSobrePage.sleepThread( );
				Actions builderImagem2 = new Actions( driver );
				builderImagem2.moveToElement( imagens.get( 1 ) ).perform( );
				imagens.get( 1 ).click( );

				IndexSobrePage.sleepThread( );
				Actions builderImagem3 = new Actions( driver );
				builderImagem3.moveToElement( imagens.get( 5 ) ).perform( );
				imagens.get(5).click( );

				IndexSobrePage.sleepThread( );
				WebElement botaoVerify = driver.findElement( By.id( "recaptcha-verify-button" ) );

				Actions builderVerify = new Actions( driver );
				builderVerify.moveToElement( botaoVerify ).perform( );
				botaoVerify.click( );

				for ( WebElement elemento : imagens ) {
					System.out.println( elemento.getAttribute( "innerHTML" ) );
				}

				wait = new WebDriverWait( driver, Duration.ofSeconds(timeout - 30 ));
				driver.switchTo( ).window( handlePrincipal );

				try {
					driver.findElement( 
							By.xpath( "//span[contains(@class, 'rc-anchor-error-msg')]" ) );
					inicializar( );
				} catch (NoSuchElementException csee) {
				}

				wait = new WebDriverWait( driver, Duration.ofSeconds(timeout - 30 ));
				wait.until(
						ExpectedConditions.presenceOfElementLocated( 
								By.tagName( "iframe" ) ) );
				System.out.println("name iframe checkbox -> " + iframeCheck.getAttribute("title"));
				driver.switchTo( ).frame( iframeCheck.getAttribute( "title" ) );
				handleCheckBox = driver.getWindowHandle();
				System.out.println("handleCheckBox -> " + handleCheckBox);
				wait = new WebDriverWait( driver , Duration.ofSeconds(timeout) );
				checkBox = wait.until(
						ExpectedConditions.presenceOfElementLocated(
								By.id("recaptcha-anchor") ) );
				continuar = !checkBox.getAttribute( "class" ).contains( "recaptcha-checkbox-checked" );

			} catch ( StaleElementReferenceException sere ) {
				continuar = true;
				continue;
			}

			if ( !continuar ) {
				wait = new WebDriverWait( driver, Duration.ofSeconds(timeout ));
				driver.switchTo( ).window( handlePrincipal );
				driver.findElement( By.tagName( "button" ) ).click( );
				try {
					driver.findElement(
							By.xpath( "//span[contains(@class, 'ui-messages-error-summary')]" ) );
					inicializar( );
					continuar = true;
				} catch (NoSuchElementException csee) {
				}
			}

		} while (continuar);

		IndexSobrePage.sleepThread( );
	}
	
	
	
}
