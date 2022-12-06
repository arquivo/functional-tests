package pt.arquivo.tests.cms.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import pt.arquivo.tests.cms.tests.CommonQuestionsTest;
import pt.arquivo.tests.cms.tests.ExamplesTest;
import pt.arquivo.tests.cms.tests.FooterTest;
import pt.arquivo.tests.cms.tests.NavigationTest;
import pt.arquivo.tests.cms.tests.NewsTest;
import pt.arquivo.tests.cms.tests.PublicationsTest;
import pt.arquivo.tests.cms.tests.SearchTest;
import pt.arquivo.tests.cms.tests.SiteMapTest;
import pt.arquivo.tests.cms.tests.Soft404MessageTest;

	
/**
 * @author João Nobre
 *
 */
@RunWith( Suite.class )
@SuiteClasses( { CommonQuestionsTest.class , ExamplesTest.class , FooterTest.class  , Soft404MessageTest.class , NewsTest.class , SearchTest.class , SiteMapTest.class, PublicationsTest.class , NavigationTest.class  } )
public class TestSuite {

}

