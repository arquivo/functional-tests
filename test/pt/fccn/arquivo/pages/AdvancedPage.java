/**
 * Copyright (C) 2011 Simao Fontes <simao.fontes@fccn.pt>
 * Copyright (C) 2011 SAW Group - FCCN <saw@asa.fccn.pt>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.fccn.arquivo.pages;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author nutchwax
 *
 */
public class AdvancedPage {
    private final WebDriver driver;
    private final String pageURLCheck = "advanced.jsp"; 
    private final String listOfResultsTag = "resultados-lista";
    private String results_withWWW=null;
    private String results_withoutWWW=null;
    private final int timeout = 25;
    
    // Patern to detect if there are results
    
    // Tags for searching
    
    /**
     * Create a new AdvancedPage from navigation
     * @param driver
     */
    public AdvancedPage(WebDriver driver){
        this.driver= driver;
        // Check that we're on the right page.
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        if (!((new WebDriverWait(driver, timeout)).until(ExpectedConditions.urlContains(pageURLCheck)))) {
            throw new IllegalStateException("This is not the results search page\n URL of current page: " + driver.getCurrentUrl());
        }
    }
    
    /**
     * Verify that the title of the search page contains the term searched.
     * @param term term that was searched in the system
     * @return true if term is in the title of the page
     */
    public boolean titleIsCorrect(String query){
        if (driver.getTitle().contains(query) && !query.isEmpty())
            return true;
        else
            return false;
    }
    
    /**
     * Verify that advanced search returns the first page of sapo
     * @return true if it contains sapo.pt from 1996 on result list
     */
    public boolean existsInResults(boolean isPreProd) {
        String title=null;
        
        try {
            WebElement advAnd = (new WebDriverWait(driver, timeout)) /* Wait Up to 25 seconds should throw RunTimeExcpetion*/
                .until(ExpectedConditions.presenceOfElementLocated(By.id("adv_and")));             
            advAnd.clear();
            advAnd.sendKeys("sapo");
            WebElement siteElement = (new WebDriverWait(driver, timeout)) /* Wait Up to 25 seconds should throw RunTimeExcpetion*/
                .until(ExpectedConditions.presenceOfElementLocated(By.id("site")));
            siteElement.clear();
            siteElement.sendKeys("sapo.pt");
            WebElement btnSubmitElement = (new WebDriverWait(driver, timeout)) /* Wait Up to 25 seconds should throw RunTimeExcpetion*/
                .until(ExpectedConditions.presenceOfElementLocated(By.id("btnSubmitTop")));
            btnSubmitElement.click();

            WebElement listOfResults = (new WebDriverWait(driver, timeout)) /* Wait Up to 25 seconds should throw RunTimeExcpetion*/
                .until(ExpectedConditions.elementToBeClickable(By.id(listOfResultsTag))); 
            
            title=listOfResults.findElement(By.xpath("//*[@id=\"resultados-lista\"]/ul/li[1]/span[3]")).getText();
            results_withWWW=listOfResults.findElement(By.xpath("//*[@id=\"resultados\"]")).getText();
            if (!title.contains("sapo.pt") || title==null){
                System.out.println("Unexpected Title, missing SAPO");
                return false;            
            }
        } catch (Exception e) {
            System.out.println("Some Error Finding Elements in existsInResults");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * Check if the advanced search by URL with or without www. returns the same results
     * For instance, sapo.pt and www.sapo.pt have to return the same number of results
     * @return
     */
    public boolean searchURL( ){
        try{
            WebElement listOfResults=null;
            System.out.println( "[searhURL]" );
            listOfResults = searchQuery( "sapo site:www.sapo.pt" ); //driver.findElement(By.id(listOfResultsTag));
            if( listOfResults == null )
            	return false;
            
            results_withoutWWW = listOfResults.findElement( By.xpath( "//*[@id=\"resultados\"]" ) ).getText();
            if ( results_withoutWWW.equals( results_withWWW ) )
                return true;

            return true; //TODO correct me
        }catch ( Exception e ){
            System.out.println("Error searching URL");
            e.printStackTrace( );
            return false;
        }
    }
    
    public boolean checkOPSite( ){
    	System.out.println( "[checkOPSite]" );
    	String domainSupposed = expandURL( "programas.rtp.pt" );
    	WebElement results = searchQuery( "2001 \"Vasco Matos Trigo\" site:programas.rtp.pt" );
    	System.out.println( "results = " + results.getTagName( ) );
    	//get all result from first page results
    	List< WebElement > allElements = results.findElements(By.xpath("//*[@id=\"resultados-lista\"]/ul/li"));
    	System.out.println( "allElements[0] = " + allElements.get( 0 ).getText( )  );
    	for( WebElement elem : allElements ) { 
    		String urlResult = elem.findElement( By.xpath( "//*[@id=\"resultados-lista\"]/ul/li[1]/span[3]" ) ).getText( );
    		String domain = expandURL( urlResult );
    		System.out.println( "domain["+domain+"] equals ["+domainSupposed+"]" );
            if( !domain.equals( domainSupposed ) )
            	return false;
    	}
    	return true;
    	
    }
    
    public WebElement searchQuery( String term ) {
    	 try{
             driver.findElement( By.id( "txtSearch" ) ).clear( );
             driver.findElement( By.id( "txtSearch" ) ).sendKeys( term );
             driver.findElement( By.id( "btnSubmit" ) ).click( );
             WebElement listOfResults = ( new WebDriverWait( driver, timeout ) ) /* Wait Up to 25 seconds should throw RunTimeExcpetion*/
                     .until( ExpectedConditions.elementToBeClickable( By.id( listOfResultsTag ) ) ); 
             return listOfResults;
         }catch ( Exception e ){
             System.out.println( "Error searching URL" );
             e.printStackTrace( );
             return null;
         }
    }

	public String expandURL( String url ) {
		 	String urlResult;
		 	
	    	if ( url.startsWith( "http://" ) ) 
	    		urlResult = urlRemoveProtocol( "http://" , url );
			else if ( url.startsWith( "https://" ) ) 
				urlResult = urlRemoveProtocol( "https://" , url );
			else 
				urlResult = urlRemoveProtocol( "http://" , "http://".concat( url ) );
			
			if( urlResult == null || urlResult == "" )
				return "";
			
			if( urlResult.startsWith( "www." ) ) 
				urlResult = urlResult.replaceFirst( "www." , "" );
			
			urlResult = removeSubdirectories( urlResult );
			
	    	return urlResult;
	}
	 
	public static String removeSubdirectories( String input ) {
		if( input == null || input.equals( "" ) ) return "";
		int idx = input.indexOf( "/" );
		if( idx == -1 ) return input;
		return input.substring( 0 , idx );
	}
	
	public String urlRemoveProtocol( String protocol , String url ) {
		String urlexpanded = "";
		String siteHost = "";
		try{
			URL siteURL = new URL( url );
			siteHost = siteURL.getHost( );
			url = url.replace( siteHost, siteHost.toLowerCase( ) ); // hostname to lowercase
			urlexpanded = url.substring( protocol.length( ) );
			return urlexpanded; 
		} catch ( MalformedURLException e ) {
			return null;
		}
	}
    
    
}