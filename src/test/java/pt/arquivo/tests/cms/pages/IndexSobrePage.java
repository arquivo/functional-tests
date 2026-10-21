package pt.arquivo.tests.cms.pages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.utils.AnalyzeURLs;




public class IndexSobrePage {
	WebDriver driver;

	private static final int timeout = 50;
	Map< String, String > textTolink;
	private final String dir = "sobreTestsFiles";


	/**
	 *
	 * @param driver
	 * @throws IOException
	 */
    public IndexSobrePage( WebDriver driver ) throws IOException {
        this.driver = driver;
        Properties prop = new Properties( );
    	InputStream input = null;
    	try {
			prop.load( new InputStreamReader( ClassLoader.getSystemResourceAsStream( "sobreTestsFiles/props_indexPage.properties" ), Charset.forName("UTF-8") ) ); //load a properties file
    	} catch ( FileNotFoundException e ) {
    		throw e;
    	} catch ( IOException e1) {
    		if( input != null) input.close( );
    		throw e1;
		}

    	try {
          Thread.sleep( 5000 );  //wait for page to load
        } catch( InterruptedException ex ) {
          Thread.currentThread( ).interrupt( );
        }

        // Check that we're on the right page.
        String pageTitle= driver.getTitle( );
        Charset.forName( "UTF-8" ).encode( pageTitle );
        System.out.println( "Page title = " + pageTitle + " == " + prop.getProperty( "title_pt" ) );
       /* if ( !( pageTitle.toLowerCase( ).contains( prop.getProperty( "title_pt" ).toLowerCase( ) ) )  ||
        		!( pageTitle.toLowerCase( ).contains( prop.getProperty( "title_en" ).toLowerCase( ) ) )){
            throw new IllegalStateException("This is not the index page\n Title of current page: " + pageTitle);
        } */
    }

    /**
     * Hovers a menu item and the menus that contain it, so its link and sub menu are visible.
     * The top menus only open on hover and their ids change when the menu is edited, so the
     * items are found by their own id.
     * @param id the number in the menu-item-[id] id of the item
     * @return the link of the menu item
     */
    public static WebElement hoverMenuItem( WebDriver driver, String id ) {
        WebElement link = ( new WebDriverWait( driver, Duration.ofSeconds( timeout ) ) )
            .until( ExpectedConditions.presenceOfElementLocated( By.cssSelector( "#menu-item-" + id + " > a" ) ) );
        Actions actions = new Actions( driver );
        // includes the item itself, from the top menu down
        for( WebElement menu : link.findElements( By.xpath( "ancestor::li[starts-with(@id,'menu-item')]/a" ) ) )
            actions.moveToElement( menu );
        actions.perform( );
        return link;
    }

    /**
     * Clicks a menu item, hovering the menus that contain it first.
     * @param id the number in the menu-item-[id] id of the item
     */
    public static void clickMenuItem( WebDriver driver, String id ) {
        // hovers again until the sub-menu opens, a hover while the page is still loading can be lost
        WebElement link = ( new WebDriverWait( driver, Duration.ofSeconds( timeout ) ) )
            .ignoring( StaleElementReferenceException.class )
            .until( d -> {
                WebElement item = hoverMenuItem( d, id );
                // closed sub-menus are moved off the screen, to the left
                return item.isDisplayed( ) && item.isEnabled( ) && item.getLocation( ).getX( ) >= 0 ? item : null;
            } );
        link.click( );
    }

    /**
     * Go to the general information page of the common questions
     * @throws FileNotFoundException
     */
    public CommonQuestionsPage goToCommonQuestionsPage( ) throws FileNotFoundException{
        clickMenuItem( driver, "4418" );
        return new CommonQuestionsPage( driver );
    }

    /**
     * There isn't a Collaborate page, only a menu with the pages about collaborating, so this opens the menu.
     * @throws FileNotFoundException
     */
    public CollaboratePage goToCollaboratePage( String  language ) throws FileNotFoundException{
        hoverMenuItem( driver, language.equals( "EN" ) ? "11510" : "11376" );
        return new CollaboratePage( driver );
    }

	public SiteMapPage goToSiteMapPage( ) throws FileNotFoundException {
        clickMenuItem( driver, "4425" );
        return new SiteMapPage( driver );
	}

	public PublicationsPage goToPublicationsPage( String language ) throws FileNotFoundException {
        clickMenuItem( driver, language.equals( "EN" ) ? "4428" : "4401" );
        return new PublicationsPage( driver );
	}

	public ReportsPage goToReportsPage( String language ) throws FileNotFoundException {
        clickMenuItem( driver, language.equals( "EN" ) ? "19137" : "4402" );
        return new ReportsPage( driver );
	}

	public NewsOnMediaPage goToNewOnMediaPage( String language ) throws FileNotFoundException {
        clickMenuItem( driver, language.equals( "EN" ) ? "4430" : "4403" );
        return new NewsOnMediaPage( driver );
	}

	public AudioPage goToAudioPage( String language ) throws FileNotFoundException {
        clickMenuItem( driver, language.equals( "EN" ) ? "4431" : "4404" );
        return new AudioPage( driver );
	}

	public VideoPage goToVideoPage( String language ) throws FileNotFoundException {
        clickMenuItem( driver, language.equals( "EN" ) ? "4432" : "4405" );
        return new VideoPage( driver );
	}

	public PresentationsPage goToPresentationsPage( String language ) throws FileNotFoundException {
        clickMenuItem( driver, language.equals( "EN" ) ? "4433" : "4406" );
        return new PresentationsPage( driver );
	}

    public ExamplesPage goToExamplePage( ) {
        clickMenuItem( driver, "4389" );
        return new ExamplesPage( driver );
    }

    public NewsPage goToNewsPage( ) throws FileNotFoundException{
        clickMenuItem( driver, "4388" );
        return new NewsPage( driver );
    }

    /**
     * There isn't an About page, only a menu with the pages about Arquivo.pt, so this opens the menu.
     * @throws FileNotFoundException
     */
    public AboutPage goToAboutPage( String language ) throws FileNotFoundException{
        hoverMenuItem( driver, language.equals( "EN" ) ? "11519" : "11380" );
        return new AboutPage( driver );
    }

    /**
    * Change to the English version
    */
    private void switchLanguage( ){
    	String xpathEnglishVersion = "//*[@id=\"menu-item-4424-en\"]/a"; //menu-item-4194-en
      	if( driver.findElement( By.xpath( xpathEnglishVersion ) ).getText( ).equals( "English" ) ) {
      		System.out.println( "Change language to English" );
      		driver.findElement( By.xpath( xpathEnglishVersion ) ).click( );
      		IndexSobrePage.sleepThread( );
      	}
    }

    /**
     *
     * @param l//*[@id=\"menu-item-1858\"]/aanguage
     * @return
     */
    public boolean checkFooterLinks( String language ) {
		System.out.println( "[checkFooterLinks]" );
    	String xpatha = "//*[@id=\"footer-widgets\"]/div/div/div/aside/ul/li/a"; //get footer links



   		if( language.equals( "EN" ) ) {
   			readFromFileToMap( "FooterLinksEN.txt" );
   			switchLanguage( );
   		} else
   			readFromFileToMap( "FooterLinksPT.txt" );

    	try{
    		List< WebElement > results = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) )
	                .until( ExpectedConditions
	                        .visibilityOfAllElementsLocatedBy(
	                        		      By.xpath( xpatha )
	                        )
	        );

    		System.out.println( "[footer] results size = " + results.size( ) );
    		for( WebElement elem : results ) {
    			String url = elem.getAttribute( "href" );
    			String text = elem.getText( );
    			Charset.forName( "UTF-8" ).encode( text );
    			if( url.startsWith( "http://www.facebook.com/" ) ||
    				url.startsWith( "https://www.facebook.com/" ) ||
                    url.startsWith("https") ){ /*TODO:: https inspect link version*/
    				if( !textTolink.get( text ).equals( url ) ) {
    					System.out.println( "Wrong link ["+textTolink.get( text ).trim( )+"] != ["+url+"]" );
    					return false;
    				}
    			} else {
    				if( !AnalyzeURLs.linkExists( url , text , textTolink ) )
    					return false;


    			}
    			//Debug System.out.println( "Return = " + linkExists( url , text ) );

    		}

	    	return true;
    	} catch( NoSuchElementException e ){
            System.out.println( "Error in checkOPSite" );
            e.printStackTrace( );
            return false;
    	}


    }

    /**
     *
     * @param
     * @return
     */
    public boolean checkFooterURLs( String language ) {
		System.out.println( "[checkFooterLinks]" );
    	String xpatha = "//*[@id=\"footer-widgets\"]/div/div/div/aside/ul/li/a"; //get footer links

    	if( language.equals( "EN" ) )
   			switchLanguage( );

    	try{
    		List< WebElement > results = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) )
	                .until( ExpectedConditions
	                        .visibilityOfAllElementsLocatedBy(
	                        		      By.xpath( xpatha )
	                        )
	        );

    		System.out.println( "[footer] results size = " + results.size( ) );
    		for( WebElement elem : results ) {
    			String url = elem.getAttribute( "href" );
    			if( !url.startsWith( "http://www.facebook.com/" ) &&
    				!url.startsWith( "https://www.facebook.com/" ) &&
                    !url.startsWith("https") /*TODO:: Check also https links*/){
    				System.out.println( "Check footer link: " + url );
    				if( !AnalyzeURLs.checkLink( url ) )
    					return false;
    			}
    		}

	    	return true;
    	} catch( NoSuchElementException e ){
            System.out.println( "Error in checkFooterURLs" );
            e.printStackTrace( );
            return false;
    	}

    }


    public boolean checkSearchLinks( String language ) { //TODO: connect to preprod
		System.out.println( "[checkSearchLinks]" );
    	String xpathInput 	= "//*[@id=\"search-4\"]/form/label/input";
    	String xpathbtn 	= "//*[@id=\"wp_editor_widget-17\"]/div/div[2]/div/span/a";

    	List< String > searchTerms = readFromFileToList( "FooterLinks.txt" );
    	try{
    		for( String term : searchTerms ) {
        		WebElement input = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
        	            .until(
        	            		ExpectedConditions.presenceOfElementLocated(
        	            				By.xpath( xpathInput )
        	            				)
        	            		);

        		WebElement btnsearch = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
        	            .until(
        	            		ExpectedConditions.presenceOfElementLocated(
        	            				By.xpath( xpathbtn )
        	            				)
        	            		);
        		input.clear( );
    			input.sendKeys( term );
    			btnsearch.click( );
    			sleepThread( );
    		}

    		return true;
    	} catch( NoSuchElementException e ){
            System.out.println( "Error in checkOPSite" );
            e.printStackTrace( );
            return false;
    	}

    }

    /**
     *
     * @param filename
     * @return
     */
	private List< String > readFromFileToList( String filename ) {
		List< String > searchTerms = new ArrayList< String >( );
		try {
			BufferedReader in = new BufferedReader(
											new InputStreamReader( ClassLoader.getSystemResourceAsStream (dir +  File.separator + filename ) , "UTF8" ) );
			String line = "";
			while( ( line = in.readLine( ) ) != null ) {
				searchTerms.add( line );
			}
			in.close( );
			System.out.println( "searchTerms => " + textTolink.toString( ) );
			return searchTerms;
		} catch ( FileNotFoundException e ) {
			e.printStackTrace( );
			return null;
		} catch (IOException e) {
			e.printStackTrace( );
			return null;
		}
	}


    /**
     *
     * @param filename
     * @return
     */
	private boolean readFromFileToMap( String filename ) {
		textTolink = new HashMap< String , String >( );
		try {
			BufferedReader in = new BufferedReader(
											new InputStreamReader(
													ClassLoader.getSystemResourceAsStream (dir.concat( File.separator ).concat( filename ) ) , "UTF8" ) );
			String line = "";
			while( ( line = in.readLine( ) ) != null ) {
				String parts[ ] = line.split( "," );
				textTolink.put( parts[ 0 ], parts[ 1 ] );
			}
			in.close( );
			System.out.println( "Map => " + textTolink.toString( ) );
			return true;
		} catch ( FileNotFoundException e ) {
			e.printStackTrace( );
			return false;
		} catch (IOException e) {
			e.printStackTrace( );
			return false;
		}
	}

	public static void sleepThread( ) {
		try {
			Thread.sleep( 4000 );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

