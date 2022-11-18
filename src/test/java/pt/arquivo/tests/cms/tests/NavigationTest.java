package pt.arquivo.tests.cms.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.tests.cms.pages.AboutPage;
import pt.arquivo.tests.cms.pages.AudioPage;
import pt.arquivo.tests.cms.pages.CollaboratePage;
import pt.arquivo.tests.cms.pages.IndexSobrePage;
import pt.arquivo.tests.cms.pages.NewsOnMediaPage;
import pt.arquivo.tests.cms.pages.NewsPage;
import pt.arquivo.tests.cms.pages.PresentationsPage;
import pt.arquivo.tests.cms.pages.PublicationsPage;
import pt.arquivo.tests.cms.pages.ReportsPage;

public class NavigationTest  extends WebDriverTestBaseParallel {

	public NavigationTest(String platformName, String platformVersion, String browser, String browserVersion, String deviceName, String deviceOrientation, String automationName, String resolution) {
		super(platformName, platformVersion, browser, browserVersion, deviceName, deviceOrientation, automationName, resolution);
	}
	
	@Test
	@Retry
	public void navigation( )  {
		System.out.println( "Going to the NavigationTest" );
		IndexSobrePage index = null;
		try{
			index = new IndexSobrePage( driver );
			/**************************/
			/*** Portuguese version ***/ 
			/**************************/
			NewsPage news = index.goToNewsPage( );
			assertTrue("[Nabigation Test] Failed The News Page Test in Portuguese", news.checkNewsLinks( "PT" ) );
			PublicationsPage publications = index.goToPublicationsPage( "PT" );
			ReportsPage reports = index.goToReportsPage( "PT" );
			assertTrue("[Nabigation Test] Failed The Reports Page Test in Portuguese", reports.checkReportsLinks( "PT" ) );
			NewsOnMediaPage newsonmedia = index.goToNewOnMediaPage( "PT" );
			assertTrue("[Nabigation Test] Failed The New on Media Page Test in Portuguese", newsonmedia.checkNewsLinks( "PT") );
			AudioPage audio = index.goToAudioPage( "PT" );
			assertTrue("[Nabigation Test] Failed The Audio Page Test in Portuguese", audio.checkAudioLinks( "PT" ) );
			/*TODO to slow VideoPage video = index.goToVideoPage( "PT" );
			assertTrue("Failed The Video Page Test in Portuguese", video.checkVideoLinks( "PT" ) );*/
			PresentationsPage pres = index.goToPresentationsPage( "PT" );
			assertTrue("[Nabigation Test] Failed The Presentations Page Test in Portuguese", pres.checkPresentationLinks( "PT" ) );
			CollaboratePage coll = index.goToCollaboratePage( "PT" );
			assertTrue("[Nabigation Test] Failed The Collaborate Page Test in Portuguese", coll.checkCollaborateLinks( "PT" ) );
			AboutPage about = index.goToAboutPage( "PT" );
			assertTrue("[Nabigation Test] Failed The Collaborate Page Test in Portuguese", about.checkAboutLinks( "PT" ) );
			System.out.println( "Success The Navigation Test in Portuguese" );
			
			/***********************/
			/*** English version ***/ 
			/***********************/
			NewsPage newsEN = index.goToNewsPage( );
			assertTrue("[Nabigation Test] Failed The News Page Test in Portuguese", newsEN.checkNewsLinks( "EN" ) );
			PublicationsPage publicationsEN = index.goToPublicationsPage( "EN" );
			ReportsPage reportsEN = index.goToReportsPage( "EN" );
			assertTrue("[Nabigation Test] Failed The Reports Page Test in Portuguese", reports.checkReportsLinks( "EN" ) );
			NewsOnMediaPage newsonmediaEN = index.goToNewOnMediaPage( "EN" );
			assertTrue("[Nabigation Test] Failed The New on Media Page Test in Portuguese", newsonmediaEN.checkNewsLinks( "EN") );
			AudioPage audioEN = index.goToAudioPage( "EN" );
			assertTrue("[Nabigation Test] Failed The Audio Page Test in Portuguese", audioEN.checkAudioLinks( "EN" ) );
			//TODO to slow VideoPage video = index.goToVideoPage( "PT" );
			//TODO assertTrue("Failed The Video Page Test in Portuguese", video.checkVideoLinks( "EN" ) );
			PresentationsPage presEN = index.goToPresentationsPage( "EN" );
			assertTrue("[Nabigation Test] Failed The Presentations Page Test in Portuguese", presEN.checkPresentationLinks( "EN" ) );
			CollaboratePage collEN = index.goToCollaboratePage( "EN" );
			assertTrue("[Nabigation Test] Failed The Collaborate Page Test in Portuguese", collEN.checkCollaborateLinks( "EN" ) );
			AboutPage aboutEN = index.goToAboutPage( "EN" );
			assertTrue("[Nabigation Test] Failed The Collaborate Page Test in Portuguese", aboutEN.checkAboutLinks( "EN" ) );
			System.out.println( "Success The Navigation Test in English" );
	        
		} catch( IOException e ) {
			fail("IOException -> navigation");
		}
		
    }
	

}
