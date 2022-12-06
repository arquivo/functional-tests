package pt.fccn.sobre.arquivo.pages;

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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.fccn.arquivo.tests.util.AnalyzeURLs;




public class IndexSobrePage {
	WebDriver driver;

	private final int timeout = 50;
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
     * Click the link to the footer of the page
     * @throws FileNotFoundException
     */
    public CommonQuestionsPage goToCommonQuestionsPage( ) throws FileNotFoundException{
        try{
            System.out.println( "Start goToCommonQuestionsPage( ) method" );
            WebElement cQuestionsLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
            .until(
            		ExpectedConditions.presenceOfElementLocated(
            				By.xpath( "//*[@id=\"wp_editor_widget-8\"]/ul/li[1]/a" )
            				)
            		);
            cQuestionsLink.click( );
            System.out.println( "Finished goToCommonQuestionsPage( ) method" );
        }catch( NoSuchElementException e ){
        	System.out.println( "Could not find the link element" );
        	throw e;
        }

        return new CommonQuestionsPage( driver );
    }

    /**
     *
     * @return
     * @throws FileNotFoundException
     */
    public CollaboratePage goToCollaboratePage( String  language ) throws FileNotFoundException{
    	String idMenu = "";
    	if( language.equals( "EN" ) )
    		idMenu = "4398"; //*[@id="menu-item-4168"]/a
    	else
    		idMenu = "4397"; //*[@id="menu-item-4167"]/a
    	try{
    		System.out.println( "Start goToCollaboratePage() method" );
            WebElement collaborateLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
            .until(
            		ExpectedConditions.presenceOfElementLocated(
            				By.xpath( "//*[@id=\"menu-item-" + idMenu + "\"]/a" )
            				)
            		);
            collaborateLink.click( );
            System.out.println( "Finished goToCollaboratePage() method" );
        }catch( NoSuchElementException e ){
        	System.out.println( "Could not find the link element" );
        	throw e;
        }
    	return new CollaboratePage( driver );
    }


    /**
     *
     * @return
     * @throws FileNotFoundException
     */
	public SiteMapPage goToSiteMapPage( ) throws FileNotFoundException {

        try{
            System.out.println( "Start goToSiteMapPage() method" );

            Actions actions = new Actions( driver );
            WebElement menuHoverLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
                    .until(
                    		ExpectedConditions.presenceOfElementLocated(
                    				By.xpath( "//*[@id=\"menu-item-4391\"]/a" )//*[@id="menu-item-4396"]/a/span
                    				)
                    		);
            actions.moveToElement( menuHoverLink ).perform( ); //click in menu ajuda
            WebElement menuClickLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
                    .until(
                    		ExpectedConditions.presenceOfElementLocated(
                    				By.xpath( "//*[@id=\"menu-item-4425\"]/a" ) //*[@id="menu-item-4425"]/a
                    				)
                    		);
            menuClickLink.click( );
            System.out.println( "Finished goToSiteMapPage() method" );
        }catch( NoSuchElementException e ){
        	System.out.println( "Could not find the link element" );
        	throw e;
        }

        return new SiteMapPage( driver );
	}

    /**
     *
     * @return
     * @throws FileNotFoundException
     */
	public PublicationsPage goToPublicationsPage( String language ) throws FileNotFoundException {
		String idMenuPub 		= "";
		String idMenuPubCien 	= "";

		try{
            System.out.println( "Start goToPublicationsPage( ) method" );
            if( language.equals( "EN" ) ) {
            	idMenuPub 		= "menu-item-4395"; //*[@id="menu-item-4395"]/a
            	idMenuPubCien 	= "menu-item-4428"; //*[@id="menu-item-4428"]/a
            } else {
            	idMenuPub 		= "menu-item-4390"; //*[@id="menu-item-4160"]/a
            	idMenuPubCien 	= "menu-item-4401"; //*[@id="menu-item-4401"]/a
            }

            Actions actions = new Actions( driver );
            WebElement menuHoverLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
                    .until(
                    		ExpectedConditions.presenceOfElementLocated(
                    				By.xpath( "//*[@id=\""+ idMenuPub +"\"]/a" )
                    				)
                    		);
            actions.moveToElement( menuHoverLink ).perform( );
            WebElement menuClickLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
                    .until(
                    		ExpectedConditions.presenceOfElementLocated(
                    				By.xpath( "//*[@id=\"" + idMenuPubCien + "\"]/a" )
                    				)
                    		);
            menuClickLink.click( );
            System.out.println( "Finished goToPublicationsPage( ) method" );
        }catch( NoSuchElementException e ){
        	System.out.println( "Could not find the link element" );
        	throw e;
        }

        return new PublicationsPage( driver );
	}

    /**
     *
     * @return
     * @throws FileNotFoundException
     */
	public ReportsPage goToReportsPage( String language ) throws FileNotFoundException {
		String idMenu 		= "";
		String idSubMenu 	= "";
        try{
            System.out.println( "Start goToReportsPage() method" );
            if( language.equals( "EN" ) ) {
            	idMenu 		= "menu-item-4395"; //*[@id="menu-item-4165"]/a
            	idSubMenu 	= "menu-item-4429"; //*[@id="menu-item-4429"]/a
            } else {
            	idMenu 		= "menu-item-4390"; //*[@id="menu-item-4160"]/a
            	idSubMenu 	= "menu-item-4402"; //*[@id="menu-item-4402"]/a
            }
            Actions actions = new Actions( driver );
            WebElement menuHoverLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
                    .until(
                    		ExpectedConditions.presenceOfElementLocated(
                    				By.xpath( "//*[@id=\"" + idMenu + "\"]/a" )
                    				)
                    		);
            actions.moveToElement( menuHoverLink ).perform( );
            WebElement menuClickLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
                    .until(
                    		ExpectedConditions.presenceOfElementLocated(
                    				By.xpath( "//*[@id=\"" + idSubMenu + "\"]/a" )
                    				)
                    		);
            menuClickLink.click( );
            System.out.println( "Finished goToReportsPage() method" );
        }catch( NoSuchElementException e ){
        	System.out.println( "Could not find the link element" );
        	throw e;
        }

        return new ReportsPage( driver );
	}

    /**
     *
     * @return
     * @throws FileNotFoundException
     */
	public NewsOnMediaPage goToNewOnMediaPage( String language ) throws FileNotFoundException {
		String idDiv = "";
		String idDivSubMenu = "";
		if( language.equals( "EN" ) ) {
			idDiv = "4395";
			idDivSubMenu = "4430"; //*[@id="menu-item-4430"]/a
		} else {
			idDiv = "4390";
			idDivSubMenu = "4403"; //*[@id="menu-item-4403"]/a
		}
        try{
            System.out.println( "Start goToNewOnMediaPage() method" );

            Actions actions = new Actions( driver );
            WebElement menuHoverLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
                    .until(
                    		ExpectedConditions.presenceOfElementLocated(
                    				By.xpath( "//*[@id=\"menu-item-"+idDiv+"\"]/a" )
                    				)
                    		);
            actions.moveToElement( menuHoverLink ).perform( );
            WebElement menuClickLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
                    .until(
                    		ExpectedConditions.presenceOfElementLocated(
                    				By.xpath( "//*[@id=\"menu-item-"+idDivSubMenu+"\"]/a" )
                    				)
                    		);
            menuClickLink.click( );
            System.out.println( "Finished goToNewOnMediaPage() method" );
        }catch( NoSuchElementException e ){
        	System.out.println( "Could not find the link element" );
        	throw e;
        }

        return new NewsOnMediaPage( driver );
	}

    /**
     *
     * @return
     * @throws FileNotFoundException
     */
	public AudioPage goToAudioPage( String language ) throws FileNotFoundException {
		String xpathPublicationMenu = "";
		String xpathAudio = "";

		if( language.equals( "PT" ) ){
			xpathAudio = "//*[@id=\"menu-item-4404\"]/a";  //*[@id="menu-item-4174"]/a
			xpathPublicationMenu = "//*[@id=\"menu-item-4390\"]/a";
		}
		else{
			xpathAudio = "//*[@id=\"menu-item-4431\"]/a";  //*[@id="menu-item-4201"]/a
			xpathPublicationMenu = "//*[@id=\"menu-item-4395\"]/a";
		}

        try{
            System.out.println( "Start goToAudioPage() method" );

            Actions actions = new Actions( driver );
            WebElement menuHoverLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
                    .until(
                    		ExpectedConditions.presenceOfElementLocated(
                    				By.xpath( xpathPublicationMenu )
                    				)
                    		);
            actions.moveToElement( menuHoverLink ).perform( );
            WebElement menuClickLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
                    .until(
                    		ExpectedConditions.presenceOfElementLocated(
                    				By.xpath( xpathAudio )
                    				)
                    		);
            menuClickLink.click( );
            System.out.println( "Finished goToAudioPage() method" );
        }catch( NoSuchElementException e ){
        	System.out.println( "Could not find the link element" );
        	throw e;
        }

        return new AudioPage( driver );
	}

    /**
     *
     * @return
     * @throws FileNotFoundException
     */
	public VideoPage goToVideoPage( String language ) throws FileNotFoundException {
		String xpathVideo = "";
		String xpathDiv = "";
		if( language.equals( "EN" ) ){
			xpathVideo = "//*[@id=\"menu-item-4432\"]/a"; //*[@id="menu-item-4202"]/a
			xpathDiv = "//*[@id=\"menu-item-4395\"]/a";
		} else {
			xpathVideo = "//*[@id=\"menu-item-4405\"]/a"; //*[@id="menu-item-4175"]/a
			xpathDiv =  "//*[@id=\"menu-item-4390\"]/a";
		}
        try{
            System.out.println( "Start goToVideoPage() method" );

            Actions actions = new Actions( driver );
            WebElement menuHoverLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
                    .until(
                    		ExpectedConditions.presenceOfElementLocated(
                    				By.xpath( xpathDiv )
                    				)
                    		);
            actions.moveToElement( menuHoverLink ).perform( );
            WebElement menuClickLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
                    .until(
                    		ExpectedConditions.presenceOfElementLocated(
                    				By.xpath( xpathVideo )
                    				)
                    		);
            menuClickLink.click( );
            System.out.println( "Finished goToVideoPage() method" );
        }catch( NoSuchElementException e ){
        	System.out.println( "Could not find the link element" );
        	throw e;
        }

        return new VideoPage( driver );
	}


    /**
     *
     * @return
     * @throws FileNotFoundException
     */
	public PresentationsPage goToPresentationsPage( String language ) throws FileNotFoundException {

		String xpathDivMenu = "";
		String xpathDibSubMenu = "";

		if( language.equals( "EN" ) ) {
			xpathDivMenu =  "//*[@id=\"menu-item-4395\"]/a";
			xpathDibSubMenu = "//*[@id=\"menu-item-4433\"]/a"; //*[@id="menu-item-4203"]/a
		} else {
			xpathDivMenu = "//*[@id=\"menu-item-4390\"]/a";
			xpathDibSubMenu = "//*[@id=\"menu-item-4406\"]/a"; //*[@id="menu-item-4176"]
		}

		try{
            System.out.println( "Start goToPresentationsPage() method" );

            Actions actions = new Actions( driver );
            WebElement menuHoverLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
                    .until(
                    		ExpectedConditions.presenceOfElementLocated(
                    				By.xpath( xpathDivMenu )
                    				)
                    		);
            actions.moveToElement( menuHoverLink ).perform( );
            WebElement menuClickLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
                    .until(
                    		ExpectedConditions.presenceOfElementLocated(
                    				By.xpath( xpathDibSubMenu )
                    				)
                    		);
            menuClickLink.click( );
            System.out.println( "Finished goToPresentationsPage() method" );
        }catch( NoSuchElementException e ){
        	System.out.println( "Could not find the link element" );
        	throw e;
        }

        return new PresentationsPage( driver );
	}




    /**
     *
     * @return
     */
    public ExamplesPage goToExamplePage( ) {

        try{
            System.out.println( "Start goToExamplePage() method" );
            WebElement examplePageLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
            .until(
            		ExpectedConditions.presenceOfElementLocated(
            				By.xpath( "//*[@id=\"menu-item-4389\"]/a" ) //*[@id="menu-item-4389"]/a
            				)
            		);
            examplePageLink.click( );
            System.out.println( "Finished goToExamplePage() method" );
        }catch( NoSuchElementException e ){
        	System.out.println( "Could not find the link element" );
        	throw e;
        }

        return new ExamplesPage( driver );

    }


    /**
     *
     * @throws FileNotFoundException
     */
    public NewsPage goToNewsPage( ) throws FileNotFoundException{
        try{
            System.out.println( "Start goToNewsPage( ) method" );
            WebElement newsLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
            .until(
            		ExpectedConditions.presenceOfElementLocated(
            				By.xpath( "//*[@id=\"menu-item-4388\"]/a" ) //*[@id="menu-item-4388"]/a
            				)
            		);
            newsLink.click( );
            System.out.println( "Finished goToNewsPaindexge( ) method" );
        }catch( NoSuchElementException e ){
        	System.out.println( "Could not find the link element" );
        	throw e;
        }

        return new NewsPage( driver );
    }

    /**
     *
     * @throws FileNotFoundException
     */
    public AboutPage goToAboutPage( String language ) throws FileNotFoundException{
        String idDiv = "";

        if( language.equals( "EN" ) )
        	idDiv = "menu-item-4400"; //*[@id="menu-item-4170"]/a
        else
        	idDiv = "menu-item-4399"; //*[@id="menu-item-4169"]/a

    	try{
            System.out.println( "Start goToAboutPage( ) method" );
            WebElement newsLink = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
            .until(
            		ExpectedConditions.presenceOfElementLocated(
            				By.xpath( "//*[@id=\""+idDiv+"\"]/a" )
            				)
            		);
            newsLink.click( );
            System.out.println( "Finished goToAboutPage( ) method" );
        }catch( NoSuchElementException e ){
        	System.out.println( "Could not find the link element" );
        	throw e;
        }

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

