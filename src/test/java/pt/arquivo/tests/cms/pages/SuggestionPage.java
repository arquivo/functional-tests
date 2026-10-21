package pt.arquivo.tests.cms.pages;

import java.io.FileNotFoundException;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SuggestionPage {

	WebDriver driver;
	private final int timeout = 50;

	public SuggestionPage( WebDriver driver ) throws FileNotFoundException{
		this.driver = driver;
	}

	/**
	 * Checks that the suggestion form, a Google Form embedded on the page, is shown.
	 * It isn't submitted, it would send a suggestion to the team and it has a reCAPTCHA.
	 * @param language
	 * @return
	 */
	public boolean checkSuggestionForm( String language ) {
		System.out.println( "[checkSuggestionForm]" );
		WebElement iframe = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) )
			.until(
					ExpectedConditions.visibilityOfElementLocated(
							By.cssSelector( "iframe.contact-form" ) ) );
		String src = iframe.getAttribute( "src" );
		System.out.println( "Suggestion form = " + src );
		if( src == null || !src.startsWith( "https://docs.google.com/forms/" ) )
			return false;
		try {
			driver.switchTo( ).frame( iframe );
			( new WebDriverWait( driver, Duration.ofSeconds(timeout )) )
				.until(
						ExpectedConditions.visibilityOfElementLocated(
								By.cssSelector( "form textarea, form input[type=text]" ) ) );
			return true;
		} finally {
			driver.switchTo( ).defaultContent( );
		}
	}

}
